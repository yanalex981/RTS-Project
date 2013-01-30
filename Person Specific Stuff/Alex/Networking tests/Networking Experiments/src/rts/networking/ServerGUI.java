package rts.networking;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import rts.networking.gui.ComboBoxFileModel;

/**
 * GUI server console
 * 
 * @author Alex
 */
public class ServerGUI extends JFrame {
	private static final long serialVersionUID = -357002641827289509L;
	
	private boolean waitingForConnections = false;
	private boolean gameRunning = false;
	DataFactory factory = new DataFactory();
	
	ArrayList<File> mapFiles = new ArrayList<File>(0);
	ConcurrentHashMap<InetAddress, Player> players = new ConcurrentHashMap<InetAddress, Player>(0);
	
	DatagramPacket packetIn;
	DatagramPacket packetOut;
	DatagramSocket server;
	
	Timer sender = new Timer();
	Runnable receiver;
	Runnable interpretor;

//	Vector<Unit> unitsInAction = new Vector<Unit>();
	LinkedBlockingQueue<DatagramPacket> inbound = new LinkedBlockingQueue<DatagramPacket>();
	LinkedBlockingQueue<DatagramPacket> outbound = new LinkedBlockingQueue<DatagramPacket>();
	
	JList lstPlayers;
	JButton btnKick = new JButton("Kick");
	JButton btnInfo = new JButton("Info");
	JButton btnAccept = new JButton("Accept Connections");
	JButton btnDecline = new JButton("Decline Connections");
	JButton btnChangePort = new JButton("Change Port");
	JButton btnStart = new JButton("Start Game");
	JButton btnStop = new JButton("Stop Game");
	JButton btnRefresh = new JButton("Refresh Map List");
	JPanel pnlConnection = new JPanel();
	JPanel pnlGameControl = new JPanel();
	JPanel pnlSouth = new JPanel();
	JPanel pnlWest = new JPanel();
	JPanel pnlPlayers = new JPanel();
	JComboBox cboMaps = new JComboBox();
	JTextArea txaResults = new JTextArea();
	JScrollPane scpPlayers = new JScrollPane(lstPlayers);
	JScrollPane scpResults = new JScrollPane(txaResults);
	
	public ServerGUI() {
		pnlConnection.setLayout(new BoxLayout(pnlConnection, BoxLayout.X_AXIS));
		pnlGameControl.setLayout(new BoxLayout(pnlGameControl, BoxLayout.X_AXIS));
		pnlSouth.setLayout(new BoxLayout(pnlSouth, BoxLayout.Y_AXIS));
		pnlPlayers.setLayout(new BoxLayout(pnlPlayers, BoxLayout.X_AXIS));
		pnlWest.setLayout(new BoxLayout(pnlWest, BoxLayout.Y_AXIS));
		
		add(cboMaps, BorderLayout.NORTH);
		add(scpResults);
		add(pnlSouth, BorderLayout.SOUTH);
		add(pnlWest, BorderLayout.WEST);
		
		pnlSouth.add(pnlConnection);
		pnlSouth.add(pnlGameControl);
		
		pnlConnection.add(btnAccept);
		pnlConnection.add(btnDecline);
		
		pnlGameControl.add(btnStart);
		pnlGameControl.add(btnStop);
		pnlGameControl.add(btnRefresh);
		
		pnlWest.add(scpPlayers);
		pnlWest.add(pnlPlayers);
		
		pnlPlayers.add(btnKick);
		pnlPlayers.add(btnInfo);
		
		txaResults.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
		txaResults.setTabSize(4);
		txaResults.setEditable(false);
		
		btnDecline.setEnabled(false);
		btnStart.setEnabled(false);
		btnStop.setEnabled(false);
		
		addActionListeners();
		
		cout("Checking for maps...");
		findMapFiles();
		setComponentStates();
		
		setTitle("RTS Server");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);

		initializeThreads();
	}
	
	private void initializeThreads() {
		receiver = new Runnable() {
			@Override
			public void run() {
				try {
					while (waitingForConnections) {
						packetIn = new DatagramPacket(new byte[DataFactory.PACKET_SIZE_BYTES], DataFactory.PACKET_SIZE_BYTES);
						server.receive(packetIn);
						inbound.put(packetIn);
					}
				}
				catch (IOException e) {
					cout("IOException caught. You might want to figure out what happened...");
					e.printStackTrace();
				}
				catch (InterruptedException e) {
					cout("InterruptedException caught. The receiver threader was interrupted when inserting a packet");
					e.printStackTrace();
				}
			}
		};
		
		/*
		 * sender thread is may not be needed anymore because we'll send updating packets every 200ms instead.
		 * Timers and TimerTasks are great for that.
		 */

//		sender = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					while (waitingForConnections) {
//						if (outbound.size() > 0) {
////							DatagramPacket temp = outbound.remove();
//							// TODO add the sender address or else this will not send
////							packetOut = new DatagramPacket(temp, temp.length);
//							server.send(outbound.remove());
//						}
//					}
//				}
//				catch (IOException e) {
//					cout("IOException caught. You might want to figure out what happened...");
//					e.printStackTrace();
//				}
//			}
//		});
		
		interpretor = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (waitingForConnections) {
						DatagramPacket source = inbound.poll();
						
						if (source != null) {
							int instruction = DataInterpretor.getInstruction(source.getData());
							
							if (instruction == DataFactory.PACKET_CONNECT && !players.contains(source.getAddress())) {
//							if (instruction == DataFactory.PACKET_CONNECT) {
//								cout(DataInterpretor.getStringData(source.getData(), 1, 64).trim() + " is trying to connect");
								connect(source);
							}
						}
					}
					
					while (gameRunning) {
						DatagramPacket source = inbound.poll();
						
						if (source != null) {
							int instruction = DataInterpretor.getInstruction(source.getData());
							int unitID;
							
							if (instruction != DataFactory.PACKET_DISCONNECT) {
								unitID = DataInterpretor.getInstruction(source.getData());
								
								// checking whether or not the unit exists
								for (Player p: players.values()) {
									if (!p.unitExists(unitID)) {
										byte[] data = factory.createRemoveUnitPacket(unitID);
										outbound.add(new DatagramPacket(data, data.length, source.getAddress(), 666));
										continue;
									}
								}
							}
							
							/*
							 * reactions after parsing the packets have not finished
							 * because specific functions for units and buildings
							 * have not been implemented
							 */
							switch (instruction) {
							case DataFactory.PACKET_START_GAME:
								// start up the graphical client
								break;
							case DataFactory.PACKET_DISCONNECT:
								disconnect(source);
								break;
							case DataFactory.PACKET_UPDATE_HP:
								updateHP(source);
								break;
							case DataFactory.PACKET_UPDATE_XY:
								updateXY(source);
								break;
							case DataFactory.PACKET_ADD_UNIT:
								addUnit(source);
								break;
							case DataFactory.PACKET_REMOVE_UNIT:
								removeUnit(source);
								break;
							}
						}
					}
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	private void connect(DatagramPacket packet) throws IOException {
		if (players.size() < 2) {
			InetAddress ip = packet.getAddress();
			
			String name = DataInterpretor.getStringData(packet.getData(), 1, 64);
			
			cout("Connection request from " + packet.getAddress().toString() + " by " + name);
			
			Player p = new Player(ip, name);
			players.put(ip, p);
			
			if (players.size() == 2) {
				btnStart.setEnabled(true);
			}
		}
	}
	
	private void disconnect(DatagramPacket packet) {
		// show window
		// close game
		// Bobby: maybe
	}
	
	private void updateHP(DatagramPacket packet) throws IOException {
		InetAddress source = packet.getAddress();
		
		Player p = players.get(source);
		int unitID = DataInterpretor.getIntData(packet.getData(), 1);
		int hp = DataInterpretor.getIntData(packet.getData(), 5);
		
		p.getUnit(unitID).setHP(hp);
	}
	
	private void updateXY(DatagramPacket packet) throws IOException {
		InetAddress source = packet.getAddress();
		Player p = players.get(source);
		int unitID = DataInterpretor.getIntData(packet.getData(), 1);
		float x = DataInterpretor.getFloatData(packet.getData(), 5);
		float y = DataInterpretor.getFloatData(packet.getData(), 9);
		
		p.getUnit(unitID).setX(x);
		p.getUnit(unitID).setX(y);
	}
	
	private void addUnit(DatagramPacket packet) throws IOException {
		InetAddress source = packet.getAddress();
		Player p = players.get(source);
		int unitID = DataInterpretor.getIntData(packet.getData(), 1);
		int hp = DataInterpretor.getIntData(packet.getData(), 5);
		float x = DataInterpretor.getIntData(packet.getData(), 9);
		float y = DataInterpretor.getIntData(packet.getData(), 13);
		
		switch (unitID) {
		case DataFactory.UNIT_COMMAND_CENTER:
			// p.add(new CommandCenter(position, owner, false));
			break;
		case DataFactory.UNIT_BARRACKS:
			// p.add(new Barracks(position, owner, false));
			break;
		case DataFactory.UNIT_GENERATOR:
			// p.add(new Generator(position, owner, false));
			break;
		case DataFactory.UNIT_MARINE:
			// p.add(new Marine(position, owner, false));
			break;
		case DataFactory.UNIT_WORKER:
			// p.add(new Worker(position, owner, false));
			break;
		}
	}
	
	private void removeUnit(DatagramPacket packet) throws IOException {
		InetAddress source = packet.getAddress();
		Player p = players.get(source);
		int unitID = DataInterpretor.getIntData(packet.getData(), 1);
		
//		p.remove(unitiD);
	}

	private void addActionListeners() {
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				cboMaps.setEnabled(false);
				
				try {
					for (Player p: players.values()) {
						byte[] bytes = factory.createGameStartPacket();
						DatagramPacket start = new DatagramPacket(bytes, bytes.length, p.getPlayerAddress(), 666);
						server.send(start);
					}
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
				
				sender.schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							DatagramPacket p = outbound.remove();
							
							if (p != null) {
								server.send(p);
							}
						}
						catch (IOException e) {
							cout("Big IO error caught. Figure out what was wrong");
						}
					}
				}, 200);
			}
		});
		
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnStop.setEnabled(false);
				btnStart.setEnabled(true);
				cboMaps.setEnabled(true);
			}
		});
		
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				findMapFiles();
				setComponentStates();
			}
		});
		
		btnAccept.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cboMaps.setEnabled(false);
				btnAccept.setEnabled(false);
				btnDecline.setEnabled(true);
				
				waitingForConnections = true;
				
				new Thread(receiver).start();
				cout("Packet receiver thread started");
				
				new Thread(interpretor).start();
				cout("Packet interpretor thread started");
			}
		});
		
		btnDecline.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				waitingForConnections = false;
				cboMaps.setEnabled(true);
				btnDecline.setEnabled(false);
				btnAccept.setEnabled(true);
			}
		});
		
		btnKick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lstPlayers.getSelectedValue() != null) {
					if (JOptionPane.showConfirmDialog(null, "Do you really want to kick that player?") == JOptionPane.YES_OPTION) {
						// kick player
					}
				}
			}
		});
	}
	
	private void setComponentStates() {
		if (mapFiles.size() < 1) {
			btnAccept.setEnabled(false);
			btnDecline.setEnabled(false);
			btnStart.setEnabled(false);
			btnStop.setEnabled(false);
			cboMaps.setEnabled(false);
		}
		else {
			cout(mapFiles.size() + " valid map(s) found");
			cboMaps.setSelectedIndex(0);
			
			try {
				if (server == null) {
					server = new DatagramSocket(666);
					
					if (server.getLocalPort() == 666) {
						cout("Socket bound to the evil port");
					}
					else {
						cout("Socket bound to port" + server.getLocalPort());
					}
				}
			}
			catch (SocketException e) {
				cout("SocketException thrown. Try changing the port");
				e.printStackTrace();
			}
		}
	}
	
	private void findMapFiles() {
		File mapDir = new File("Maps");
		
		if (mapDir.exists() && mapDir.isDirectory()) {
			if (mapDir.listFiles().length > 1) {
				filterMapList();
				
				if (mapFiles.size() < 1) {
					cout("Error: No usable map files!");
				}
			}
			else {
				cout("Error: No maps found in the Maps directory!");
			}
		}
		else {
			cout("Error: Maps directory does not exist!");
		}
	}
	
	private void filterMapList() {
		File mapDir = new File("Maps");
		mapFiles = new ArrayList<File>(Arrays.asList(mapDir.listFiles()));
		
		// filter out non .map files
		for (int i = 0; i < mapFiles.size(); ++i) {
			if (!mapFiles.get(i).getName().toLowerCase().endsWith(".map")) {
				mapFiles.remove(i);
				--i;
			}
		}
		
		// filter out .map that does not have .geom file pairs
//		for (int i = 0; i < mapFiles.size(); ++i) {
//			String name = mapFiles.get(i).getName();
//			name = name.substring(0, name.lastIndexOf('.'));
//			
//			File test = new File(mapDir.getName() + "//" + name + ".geom");
//			
//			if (!test.exists()) {
//				mapFiles.remove(i);
//				--i;
//			}
//		}
		
		// filter out invalid maps
		try {
			for (int i = 0; i < mapFiles.size(); ++i) {
				if (!Map.validateMapFile(mapFiles.get(i), true)) {
					mapFiles.remove(i);
					--i;
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		cboMaps.setModel(new ComboBoxFileModel(mapFiles));
	}
	
	private synchronized void cout(String s) {
		txaResults.append(s + "\n");
	}
	
	public static void main(String[] args) {
		new ServerGUI();
	}
}