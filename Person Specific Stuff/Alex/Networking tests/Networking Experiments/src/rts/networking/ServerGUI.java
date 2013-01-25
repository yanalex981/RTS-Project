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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import rts.networking.gui.ComboBoxFileModel;
import rts.networking.gui.ListPlayerModel;

public class ServerGUI extends JFrame {
	private static final long serialVersionUID = -357002641827289509L;
	
	private boolean acceptingConnections = false;
//	private boolean gameRunning = false;
	Map map;
	
	ArrayList<File> mapFiles = new ArrayList<File>(0);
	HashSet<InetAddress> ips = new HashSet<InetAddress>(0);
	HashMap<InetAddress, Player> players = new HashMap<InetAddress, Player>(0);
	
	DatagramPacket packetIn;
	DatagramPacket packetOut;
	DatagramSocket server;
	
	// TODO add a timer to process unfinished tasks in the task queue?
	Timer executor = new Timer();
	Thread sender;
	Thread receiver;
	Thread interpretor;
	
	// TODO add a queue to store packets that have not finished execution?
	Vector<DatagramPacket> packetsInUse = new Vector<DatagramPacket>();
	LinkedBlockingQueue<DatagramPacket> inbound = new LinkedBlockingQueue<DatagramPacket>();
	LinkedBlockingQueue<DatagramPacket> outbound = new LinkedBlockingQueue<DatagramPacket>();
	
//	JList lstPlayers = new JList(new String[] {"Player 1", "Player 2", "Player 3"});
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
//	JComboBox cboMaps = new JComboBox(mapFiles.toArray);
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
		
//		lstPlayers.setSelectedIndex(0);
		
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

		executor.schedule(new TimerTask() {
			@Override
			public void run() {
				
			}
		}, 20);
		
		// TODO Move the packet sizes from Packet class to this class
		receiver = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (acceptingConnections) {
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
		});
		
		sender = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (acceptingConnections) {
						if (outbound.size() > 0) {
//							DatagramPacket temp = outbound.remove();
							// TODO add the sender address or else this will not send
//							packetOut = new DatagramPacket(temp, temp.length);
							server.send(outbound.remove());
						}
					}
				}
				catch (IOException e) {
					cout("IOException caught. You might want to figure out what happened...");
					e.printStackTrace();
				}
			}
		});
		
		interpretor = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// TODO make boolean gameRunning
					// TODO change to gameRunning
					while (acceptingConnections) {
						DatagramPacket packet = inbound.poll();
						
						if (packet == null)
							continue;
						
						// TODO Convert all byte[] to DatagramPacket...
						int instruction = DataInterpretor.getInstruction(packet.getData());
						
						switch (instruction) {
						case DataFactory.PACKET_CONNECT:
//							cout(DataInterpretor.getStringData(packet.getData(), 1, 64).trim() + " sent a packet from " + packet.getAddress().toString());
							if (players.size() != 2) {
								// TODO change the size now
							}
							
							// get map size
							// set player size
							// name size
							// get player name
							// add name to name array
							// get ip
							// make player
							// add player to player array
							
							break;
						case DataFactory.PACKET_DISCONNECT:
							// stop game
							
							break;
						case DataFactory.PACKET_WIN:
							// stop game
							
							break;
						case DataFactory.PACKET_LOSE:
							// stop game
							
							break;
						case DataFactory.PACKET_UPDATE_XY:
							// get sender IP
							// get player from map
							// get unit id
							// get next x
							// get next y
							// send new x, y to player
							
							break;
						case DataFactory.PACKET_ATTACK:
							
							break;
						case DataFactory.PACKET_UPDATE_HP:
							
							break;
						case DataFactory.PACKET_BUILD:
							
							break;
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

	private void addActionListeners() {
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				cboMaps.setEnabled(false);
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
				try {
					cboMaps.setEnabled(false);
					btnAccept.setEnabled(false);
					btnDecline.setEnabled(true);
					
					// TODO need to get the real selected map
					map = Map.createMap(mapFiles.get(0));
					
					acceptingConnections = true;
					
					players = new HashMap<InetAddress, Player>(map.getPlayerSize());
					ips = new HashSet<InetAddress>(map.getPlayerSize());
					
					receiver.start();
					cout("Packet receiver thread started");
					
					sender.start();
					cout("Packet sender thread started");
					
					interpretor.start();
					cout("Packet interpretor thread started");
					
					
				}
				catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				catch (IOException e1) {
					cout("IOException caught...");
					e1.printStackTrace();
				}
			}
		});
		
		btnDecline.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptingConnections = false;
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
				getMapList();
				
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
	
	private void getMapList() {
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
		for (int i = 0; i < mapFiles.size(); ++i) {
			String name = mapFiles.get(i).getName();
			name = name.substring(0, name.lastIndexOf('.'));
			
			File test = new File(mapDir.getName() + "//" + name + ".geom");
			
			if (!test.exists()) {
				mapFiles.remove(i);
				--i;
			}
		}
		
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
