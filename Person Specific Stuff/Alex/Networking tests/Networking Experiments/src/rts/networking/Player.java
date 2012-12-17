package rts.networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Player implements Runnable {
	private String name;
	private InetAddress playerAddress;
	private InetAddress serverAddress;
	private DatagramSocket serverSocket;
	private DatagramPacket packets;
	private int port;
	
	public Player(InetAddress server, int port) {
		try {
			setPlayerAddress(InetAddress.getLocalHost());
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		setServerAddress(server);
	}
	
	@Override
	public void run() {
		
	}
	
	public static void main(String[] args) {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InetAddress getPlayerAddress() {
		return playerAddress;
	}

	public void setPlayerAddress(InetAddress playerAddress) {
		this.playerAddress = playerAddress;
	}

	public InetAddress getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(InetAddress serverAddress) {
		this.serverAddress = serverAddress;
	}

	public DatagramSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(DatagramSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public DatagramPacket getPackets() {
		return packets;
	}

	public void setPackets(DatagramPacket packets) {
		this.packets = packets;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
