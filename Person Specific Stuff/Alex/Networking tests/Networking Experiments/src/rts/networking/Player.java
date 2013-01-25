package rts.networking;

import java.net.InetAddress;

import rts.networking.collections.UnitList;

public class Player {
	/*
	 * TODO possibly re-add the DatagramPacket
	 * to listen for player packets. This will
	 * be able to remove the 128 bytes occupied
	 * by the player name in the data section
	 */
	private String name;
	private InetAddress playerAddress;
	private UnitList units;
	
	public Player(InetAddress remote, String playerName) {
		playerAddress = remote;
		name = playerName;
		units = new UnitList();
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
	
	public int getNumberOfUnits() {
		return units.size();
	}
	
	public String toString() {
		return name;
	}
}
