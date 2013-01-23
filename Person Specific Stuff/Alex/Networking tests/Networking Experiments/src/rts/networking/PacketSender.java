package rts.networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class PacketSender extends JFrame {
	private static final long serialVersionUID = 4704574043125486387L;
	String name = "";
	
	DatagramSocket socket;
	DatagramPacket packet;
	
	JTabbedPane tbpMain = new JTabbedPane();
	JPanel pnlInfo = new JPanel();
	JPanel pnlConnection = new JPanel();
	
	JButton btnConnect = new JButton("Connect");
	JButton btnDisconnect = new JButton("Disconnect");
	JButton btnName = new JButton("Set name");
	
	JTextField tfName = new JTextField(32);
	
	PacketFactory factory = new PacketFactory();
	
	public PacketSender() {
		tbpMain.addTab("Player Info", pnlInfo);
		tbpMain.addTab("Connection Packets", pnlConnection);
		
		pnlConnection.add(btnConnect);
		pnlConnection.add(btnDisconnect);
		
		pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
		
		pnlInfo.add(tfName);
		pnlInfo.add(btnName);
		
		add(tbpMain);
		
		try {
			socket = new DatagramSocket();
		}
		catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		btnName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				name = tfName.getText();
			}
		});
		
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				byte[] packetData;
				
				try {
					packetData = factory.createConnectPacket(name);
					packet = new DatagramPacket(packetData, packetData.length, InetAddress.getLocalHost(), 666);
					socket.send(packet);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btnDisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Pseudo-client");
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
//		System.out.println(InetAddress.getLocalHost().toString());
		new PacketSender();
	}
}
