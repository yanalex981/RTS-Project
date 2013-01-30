package rts.networking;

import java.net.InetAddress;

import rts.elements.ControllableObject;
import rts.networking.collections.UnitList;

/**
 * Player class to hold player data and units
 * 
 * @author Alex
 */
public class Player {
	/**
	 * Username
	 */
	private String name;
	
	/**
	 * IP of the player
	 */
	private InetAddress playerAddress;
	
	/**
	 * Player's units
	 */
	private UnitList units;
	
	/**
	 * Amount of electrical resources available for new units
	 */
	private int power;
	
	public Player(InetAddress remote, String playerName) {
		playerAddress = remote;
		name = playerName;
		units = new UnitList();
	}

	/**
	 * gets the username
	 * 
	 * @return	current username of this player
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the username
	 * 
	 * @param name	new username of this player
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets the IP address
	 * 
	 * @return	current IP of this player
	 */
	public InetAddress getPlayerAddress() {
		return playerAddress;
	}

	/**
	 * sets the IP address
	 * 
	 * @param playerAddress	new IP address
	 */
	public void setPlayerAddress(InetAddress playerAddress) {
		this.playerAddress = playerAddress;
	}
	
	/**
	 * gets the number of units that belong to this instance of the player
	 * 
	 * @return	number of units that belong to this player
	 */
	public int getNumberOfUnits() {
		return units.size();
	}
	
	/**
	 * toString() is used with some JComponents, haven't gotten them to work yet
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * gets the amount of electrical power
	 * 
	 * @return	amount of electrical power this player has
	 */
	public int getPower() {
		return power;
	}

	/**
	 * check whether a unit exists or not
	 * 
	 * @param unitID	id of the unit
	 * @return			true of id exists, false if otherwise
	 */
	public boolean unitExists(int unitID) {
		return units.exists(unitID);
	}
	
	public ControllableObject getUnit(int unitID) {
		if (unitExists(unitID)) {
			return units.get(unitID);
		}
		
		return null;
	}
	
	public void increasePower() {
		
	}
	
	public void decreasePower() {
		
	}
}
