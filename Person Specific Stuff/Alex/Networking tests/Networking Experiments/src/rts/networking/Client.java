package rts.networking;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Client extends JFrame {
	private static final long serialVersionUID = -370906439160448533L;
	
	JButton connect = new JButton("Connect");
	JButton disconnect = new JButton("Disconnect");
	
	DatagramSocket socket;
	DatagramPacket packet;
	
	byte[] c = "connect".getBytes();
	byte[] d = "disconnect".getBytes();
	
	public Client() {
		setTitle("Client");
		setLayout(new FlowLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		add(connect);
		add(disconnect);
		
		try {
			socket = new DatagramSocket();
		}
		catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					packet = new DatagramPacket(c, c.length, InetAddress.getLocalHost(), 666);
					socket.send(packet);
				}
				catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		disconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					packet = new DatagramPacket(d, d.length, InetAddress.getLocalHost(), 666);
					socket.send(packet);
				}
				catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Client();
	}
}
