package rts.networking;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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

public class ServerGUI extends JFrame implements PacketInterpretor {
	private static final long serialVersionUID = -357002641827289509L;
	
	private boolean acceptingConnections = false;
	private boolean gameRunning = false;
	
	ArrayList<File> mapFiles = new ArrayList<File>(0);
	HashSet<String> playerNames;
	HashMap<String, Player> players;
	
	DatagramPacket packetIn;
	DatagramPacket packetOut;
	DatagramSocket server;
	
	Thread sender;
	Thread receiver;
	Thread processor;
	
	LinkedBlockingQueue<byte[]> inbound = new LinkedBlockingQueue<byte[]>();
	LinkedBlockingQueue<byte[]> outbound = new LinkedBlockingQueue<byte[]>();
	
	JList lstPlayers = new JList(new String[] {"Player 1", "Player 2", "Player 3"});
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
	JComboBox cboMaps = new JComboBox(mapFiles.toArray());
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
		
		lstPlayers.setSelectedIndex(0);
		
		txaResults.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
		txaResults.setTabSize(4);
		txaResults.setEditable(false);
		
		btnDecline.setEnabled(false);
		btnStart.setEnabled(false);
		btnStop.setEnabled(false);
		
		addActionListeners();
		
		setTitle("RTS Server");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);

		// TODO Move the packet sizes from Packet class to this class
		receiver = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (acceptingConnections) {
						packetIn = new DatagramPacket(new byte[PacketFactory.PACKET_SIZE_BYTES], PacketFactory.PACKET_SIZE_BYTES);
						server.receive(packetIn);
						inbound.put(packetIn.getData());
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
							byte[] buffer = outbound.remove();
							// TODO add the sender address or else this will not send
							packetOut = new DatagramPacket(buffer, buffer.length);
							server.send(packetOut);
						}
					}
				}
				catch (IOException e) {
					cout("IOException caught. You might want to figure out what happened...");
					e.printStackTrace();
				}
			}
		});
		
		processor = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// TODO make boolean gameRunning
					// TODO change to gameRunning
					while (acceptingConnections) {
						byte[] packet = inbound.poll();
						
						if (packet == null)
							continue;
						
						int instruction = getInstruction(packet);
						System.out.println(instruction);
						switch (instruction) {
						case PacketFactory.CONNECT:
							cout(getStringData(packet, 1, 64).trim() + " sent a packet!!");
							break;
						case PacketFactory.DISCONNECT:
							
							break;
						case PacketFactory.WIN:
							
							break;
						case PacketFactory.LOSE:
							
							break;
						case PacketFactory.UPDATE_XY:
							
							break;
						case PacketFactory.ATTACK:
							
							break;
						case PacketFactory.UPDATE_HP:
							
							break;
						case PacketFactory.BUILD:
							
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
		
		cout("Checking for maps...");
		findMapFiles();
		setComponentStates();
	}
	
	public int getInstruction(byte[] packet) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(packet);
		DataInputStream dataIn = new DataInputStream(byteIn);
		
		return dataIn.readByte();
	}
	
	public int[] getIntData(byte[] packet, int offset) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(packet);
		DataInputStream dataIn = new DataInputStream(byteIn);
		dataIn.skipBytes(offset);
		
		int[] data = new int[dataIn.available() / (Integer.SIZE / 8)];
		
		for (int i = 0; i < data.length; ++i) {
			data[i] = dataIn.readInt();
		}
		
		return data;
	}
	
	public float[] getFloatData(byte[] packet, int offset) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(packet);
		DataInputStream dataIn = new DataInputStream(byteIn);
		dataIn.skipBytes(offset);
		
		float[] data = new float[dataIn.available() / (Float.SIZE / 8)];
		
		for (int i = 0; i < data.length; ++i) {
			data[i] = dataIn.readFloat();
		}
		
		return data;
	}
	
	public String getStringData(byte[] packet, int offset, int stringLength) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(packet);
		DataInputStream dataIn = new DataInputStream(byteIn);
		dataIn.skipBytes(offset);
		
		int length = Math.min(dataIn.available() / (Character.SIZE / 8), stringLength);
		String s = "";
		
		for (int i = 0; i < length; ++i) {
			s += dataIn.readChar();
		}
		
		return s;
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
				// TODO Set player array size
				acceptingConnections = true;
				players = new HashMap<String, Player>();
				cboMaps.setEnabled(false);
				btnAccept.setEnabled(false);
				btnDecline.setEnabled(true);
				
				receiver.start();
				cout("Receiver thread started");
				
				sender.start();
				cout("Sender thread started");
				
				processor.start();
				cout("Packet processor thread started");
				
				System.out.println(processor.isAlive());
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
	}
	
	private synchronized void cout(String s) {
		txaResults.append(s + "\n");
	}
	
	public static void main(String[] args) {
		new ServerGUI();
	}
}
