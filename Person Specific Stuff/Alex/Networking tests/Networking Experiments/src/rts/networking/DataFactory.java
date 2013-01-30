package rts.networking;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * DataFactory is a factory class that needs to be instantiated,
 * upon doing so, various byte[] can be created as the data
 * section of a DatagramPacket
 * 
 * @author Alex
 */
public class DataFactory {
	// TODO Decide whether or not packet size consts are needed
	private static final int INSTRUCTION_DATA_SIZE = 1;						// 1 byte
	private static final int STRING_DATA_LENGTH = 64 * Character.SIZE / 8;	// 64 characters
	private static final int NUMERICAL_DATA_SIZE = 4 * Integer.SIZE / 8;	// 4 integers/floats
	public static final int PACKET_SIZE_BYTES = INSTRUCTION_DATA_SIZE + STRING_DATA_LENGTH + NUMERICAL_DATA_SIZE;
	
	/**
	 * Connect packet
	 */
	public static final byte PACKET_CONNECT = 0;
	
	/**
	 * Disconnect packet
	 */
	public static final byte PACKET_DISCONNECT = 1;
	
	/**
	 * Winning packet, might not use
	 */
	public static final byte PACKET_WIN = 2;
	
	/**
	 * Losing packet, might not use
	 */
	public static final byte PACKET_LOSE = 3;
	
	/**
	 * Moving packet
	 */
	public static final byte PACKET_MOVE = 4;
	
	/**
	 * Update position packet, may not use
	 */
	public static final byte PACKET_UPDATE_XY = 5;
	
	/**
	 * Attack packet
	 */
	public static final byte PACKET_ATTACK = 6;
	
	/**
	 * Update health packet, may not use
	 */
	public static final byte PACKET_UPDATE_HP = 7;
	
	/**
	 * Build packet. Builds units
	 */
	public static final byte PACKET_BUILD = 8;
	
	/**
	 * Add unit packet, creates a unit for the recipient
	 */
	public static final byte PACKET_ADD_UNIT = 9;
	
	/**
	 * Remove unit packet, removes a unit for the recipient
	 */
	public static final byte PACKET_REMOVE_UNIT = 10;
	
	/**
	 * Start game packet, launches the game window
	 */
	public static final byte PACKET_START_GAME = 11;
	
	/**
	 * Mining packet
	 */
	public static final byte PACKET_MINE = 12;
	
	/**
	 * Marine unit
	 */
	public static final int UNIT_MARINE = 0;
	
	/**
	 * Worker unit
	 */
	public static final int UNIT_WORKER = 1;
	
	/**
	 * Command Center
	 */
	public static final int UNIT_COMMAND_CENTER = 2;
	
	/**
	 * Barracks
	 */
	public static final int UNIT_BARRACKS = 3;
	
	/**
	 * Power generator
	 */
	public static final int UNIT_GENERATOR = 4;
	
	/**
	 * Underlying byte writer
	 */
	private ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
	
	/**
	 * Uses bytesOut to write primitive data and String
	 */
	private DataOutputStream dataOut = new DataOutputStream(bytesOut);
	
	public DataFactory() {}
	
	/**
	 * Clears the content in the output
	 * 
	 * @throws IOException
	 */
	private void flushOutput() throws IOException {
		bytesOut.flush();
		dataOut.flush();
	}
	
	/**
	 * Writes a string to the packet before retrieving
	 * 
	 * @param data The string to be written
	 * @throws IOException
	 */
	private void addStringToPacket(String data) throws IOException {
		// TODO implement trimming of string length
		if (data.length() > STRING_DATA_LENGTH / (Character.SIZE / 8)) {
			dataOut.writeChars(data.substring(0, STRING_DATA_LENGTH));
		}
		else {
			dataOut.writeChars(String.format("%-" + STRING_DATA_LENGTH / (Character.SIZE / 8) + "s", data));
		}
	}
	
	/**
	 * Writes an int, or a series of ints to the packet before retrieving
	 * 
	 * @param data int data to be written
	 * @throws IOException
	 */
	private void addIntToPacket(int... data) throws IOException {
		if (data == null || data.length == 0) {
			throw new InvalidParameterException("You can't create a packet without any data");
		}
		else {
			int maxLimit = Math.min(STRING_DATA_LENGTH / (Integer.SIZE / 8), data.length);
			
			for (int i = 0; i < maxLimit; ++i) {
				dataOut.writeInt(data[i]);
			}
		}
	}
	
	/**
	 * Writes float data to the packet before retrieving
	 * 
	 * @param data float data to be written
	 * @throws IOException
	 */
	private void addFloatToPacket(float... data) throws IOException {
		if (data == null || data.length == 0) {
			throw new InvalidParameterException("You can't create a packet without any data");
		}
		else {
			int maxLimit = Math.min(STRING_DATA_LENGTH / (Float.SIZE / 8), data.length);
			
			for (int i = 0; i < maxLimit; ++i) {
				dataOut.writeFloat(data[i]);
			}
		}
	}
	
	/**
	 * Creates a Start Game packet
	 * 
	 * @return data section of the packet
	 * @throws IOException
	 */
	public byte[] createGameStartPacket() throws IOException {
		dataOut.writeByte(PACKET_START_GAME);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Creates a connection packet
	 * 
	 * @param username username of the player
	 * @return data section of the packet
	 * @throws IOException
	 */
	public byte[] createConnectPacket(String username) throws IOException {
		dataOut.writeByte(PACKET_CONNECT);
		addStringToPacket(username);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Creates a disconnect packet, and disconnects the player
	 * 
	 * @param username
	 * @return data section of the packet
	 * @throws IOException
	 */
	public byte[] createDisconnectPacket(String username) throws IOException {
		dataOut.writeByte(PACKET_DISCONNECT);
		addStringToPacket(username);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Creates the winning packet
	 * 
	 * @param username username of the winner
	 * @return data section of the packet
	 * @throws IOException
	 */
	public byte[] createWinningPacket(String username) throws IOException {
		dataOut.writeByte(PACKET_WIN);
		addStringToPacket(username);
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Creates the losing packet
	 * 
	 * @param username username of the losers
	 * @return data section of the packet
	 * @throws IOException
	 */
	public byte[] createLosingPacket(String username) throws IOException {
		dataOut.writeByte(PACKET_LOSE);
		addStringToPacket(username);
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Creates the moving packet
	 * 
	 * @param unitID unit ID of the unit that needs to be moved
	 * @param x destination x position of the unit
	 * @param y destination y position of the unit
	 * @return data section of the packet
	 * @throws IOException
	 */
	public byte[] createMovePacket(int unitID, float x, float y) throws IOException {
		dataOut.writeByte(PACKET_MOVE);
		addIntToPacket(unitID);
		addFloatToPacket(x, y);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Create the update position packet
	 * 
	 * @param unitID	ID of the unit that needs position updated
	 * @param newX		new x position of the unit
	 * @param newY		new y position of the unit
	 * @return byte[]	data section of the packet
	 * @throws IOException
	 */
	public byte[] createUpdateXYPacket(int unitID, float newX, float newY) throws IOException {
		dataOut.writeByte(PACKET_UPDATE_XY);
		addIntToPacket(unitID);
		addFloatToPacket(newX, newY);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Creates attack packet
	 * 
	 * @param unitID	ID of the attacking unit
	 * @param targetID	ID of the target unit
	 * @return byte[]	Data section of the packet
	 * @throws IOException
	 */
	public byte[] createAttackPacket(int unitID, int targetID) throws IOException {
		dataOut.writeByte(PACKET_ATTACK);
		addIntToPacket(unitID, targetID);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Creates update HP packet
	 * 
	 * @param unitID	ID of the unit that needs HP updated
	 * @param newHP		updated HP of the unit
	 * @return byte[]	data section of the packet
	 * @throws IOException
	 */
	public byte[] createUpdateHPPacket(int unitID, int newHP) throws IOException {
		dataOut.writeByte(PACKET_UPDATE_HP);
		addIntToPacket(unitID, newHP);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Creates build unit packet
	 * 
	 * @param unitType	unit type to be created
	 * @param x			x position to be created
	 * @param y			y position to be created
	 * @return byte[]	data section of the packet
	 * @throws IOException
	 */
	public byte[] createBuildPacket(int unitType, float x, float y) throws IOException {
		dataOut.writeByte(PACKET_BUILD);
		addIntToPacket(unitType);
		addFloatToPacket(x, y);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Creates an add unit packet
	 * 
	 * @param unitType	type of unit to be created
	 * @param unitID	id of the unit
	 * @param hp		hp of unit
	 * @param x			x of unit
	 * @param y			y of unit
	 * @return			data section of the packet
	 * @throws IOException
	 */
	public byte[] createAddUnitPacket(int unitType, int unitID, int hp, float x, float y) throws IOException {
		dataOut.writeByte(PACKET_ADD_UNIT);
		addIntToPacket(unitType, unitID, hp);
		addFloatToPacket(x, y);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Create remove unit packet
	 * 
	 * @param unitID	ID of the unit to be removed
	 * @return			data section of the packet
	 * @throws IOException
	 */
	public byte[] createRemoveUnitPacket(int unitID) throws IOException {
		dataOut.write(PACKET_REMOVE_UNIT);
		addIntToPacket(unitID);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	/**
	 * Create mining packet
	 * 
	 * @param unitID	ID of the worker
	 * @param mineralID	ID of the mineral resource
	 * @return			data section of the packet
	 * @throws IOException
	 */
	public byte[] createMinePacket(int unitID, int mineralID) throws IOException {
		dataOut.write(PACKET_MINE);
		addIntToPacket(unitID, mineralID);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
}
