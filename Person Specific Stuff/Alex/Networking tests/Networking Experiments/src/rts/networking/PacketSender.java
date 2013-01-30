package rts.networking;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * Packet sending/interpretation test class. Used to send packets
 * Not all packets are implemented into this yet
 * 
 * @author Alex
 */
public class PacketSender extends JFrame {
	private static final long serialVersionUID = 4704574043125486387L;
	// username to send
	String name = "Alex";
	
	DatagramSocket socket;
	DatagramPacket packet;
	
	JTabbedPane tbpMain = new JTabbedPane();
	JPanel pnlInfo = new JPanel();
	JPanel pnlConnection = new JPanel();
	
	// sends connect packet
	JButton btnConnect = new JButton("Connect");
	
	// sends the disconnect packet. May not need.
	JButton btnDisconnect = new JButton("Disconnect");
	
	// changes the username
	JButton btnName = new JButton("Set name");
	
	// changes the host IP
	JButton btnChangeHost = new JButton("Change Host IP");
	
	JTextField tfName = new JTextField(32);
	
	DataFactory factory = new DataFactory();
	
	InetAddress host;
	
	public PacketSender() {
		tbpMain.addTab("Player Info", pnlInfo);
		tbpMain.addTab("Connection Packets", pnlConnection);
		
		pnlConnection.add(btnConnect);
		pnlConnection.add(btnDisconnect);
		
		pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
		
		pnlInfo.add(tfName);
		pnlInfo.add(btnName);
		pnlInfo.add(btnChangeHost);
		
		add(tbpMain);
		
		try {
			socket = new DatagramSocket();
			host = InetAddress.getLocalHost();
		}
		catch (SocketException e1) {
			e1.printStackTrace();
		}
		catch (UnknownHostException e1) {
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
					packet = new DatagramPacket(packetData, packetData.length, host, 666);
					socket.send(packet);
					System.out.println(name);
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btnDisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		btnChangeHost.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					host = InetAddress.getByName(JOptionPane.showInputDialog("Enter the new IP"));
				}
				catch (HeadlessException e1) {
					e1.printStackTrace();
				}
				catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Pseudo-client");
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) throws IOException {
		new PacketSender();
	}
}
