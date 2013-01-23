package rts.networking;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import rts.networking.collections.UnitList;

public class Player implements Runnable {
	private String name;
	private InetAddress playerAddress;
	private DatagramPacket packets;
	private UnitList units;
	private int port;
	
	public Player(InetAddress server, int port) {
		try {
			// TODO get the player's address
			// TODO establish packet structures
			setPlayerAddress(InetAddress.getLocalHost());
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	// TODO figure out the player listening threads
	@Override
	public void run() {
		
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
	
	public int getNumberOfUnits() {
		return units.size();
	}
}
