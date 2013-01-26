package rts.networking;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

public class DataFactory {
	// TODO Decide whether or not packet size consts are needed
	private static final int INSTRUCTION_DATA_SIZE = 1;						// 1 byte
	private static final int STRING_DATA_LENGTH = 64 * Character.SIZE / 8;	// 64 characters
	private static final int NUMERICAL_DATA_SIZE = 4 * Integer.SIZE / 8;	// 4 integers/floats
	public static final int PACKET_SIZE_BYTES = INSTRUCTION_DATA_SIZE + STRING_DATA_LENGTH + NUMERICAL_DATA_SIZE;
	
	public static final byte PACKET_CONNECT = 0;
	public static final byte PACKET_DISCONNECT = 1;
	public static final byte PACKET_WIN = 2;
	public static final byte PACKET_LOSE = 3;
	public static final byte PACKET_MOVE = 4;
	public static final byte PACKET_UPDATE_XY = 5;
	public static final byte PACKET_ATTACK = 6;
	public static final byte PACKET_UPDATE_HP = 7;
	public static final byte PACKET_BUILD = 8;
	public static final byte PACKET_ADD_UNIT = 9;
	public static final byte PACKET_REMOVE_UNIT = 10;
	
	public static final int UNIT_MARINE = 0;
	public static final int UNIT_MINER = 1;
	public static final int UNIT_COMMAND_CENTER = 2;
	public static final int UNIT_FACTORY = 3;
	public static final int UNIT_GENERATOR = 4;
	
	private ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
	private DataOutputStream dataOut = new DataOutputStream(bytesOut);
	
	// TODO Move packet size to ServerGUI
	public DataFactory() {}
	
	private void flushOutput() throws IOException {
		bytesOut.flush();
		dataOut.flush();
	}
	
	private void addStringToPacket(String data) throws IOException {
		// TODO implement trimming of string length
		if (data.length() > STRING_DATA_LENGTH / (Character.SIZE / 8)) {
			dataOut.writeChars(data.substring(0, STRING_DATA_LENGTH));
		}
		else {
			dataOut.writeChars(String.format("%-" + STRING_DATA_LENGTH / (Character.SIZE / 8) + "s", data));
		}
	}
	
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
	
	public byte[] createConnectPacket(String username) throws IOException {
		dataOut.writeByte(PACKET_CONNECT);
		addStringToPacket(username);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createDisconnectPacket(String username) throws IOException {
		dataOut.writeByte(PACKET_DISCONNECT);
		addStringToPacket(username);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createWinningPacket(String username) throws IOException {
		dataOut.writeByte(PACKET_WIN);
		addStringToPacket(username);
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createLosingPacket(String username) throws IOException {
		dataOut.writeByte(PACKET_LOSE);
		addStringToPacket(username);
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createMovePacket(int unitID, float x, float y) throws IOException {
		dataOut.writeByte(PACKET_MOVE);
		addIntToPacket(unitID);
		addFloatToPacket(x, y);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createUpdateXYPacket(int unitID, float newX, float newY) throws IOException {
		dataOut.writeByte(PACKET_UPDATE_XY);
		addIntToPacket(unitID);
		addFloatToPacket(newX, newY);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createAttackPacket(int unitID, int targetID) throws IOException {
		dataOut.writeByte(PACKET_ATTACK);
		addIntToPacket(unitID, targetID);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createUpdateHPPacket(int unitID, int targetID, int newHP) throws IOException {
		dataOut.writeByte(PACKET_UPDATE_HP);
		addIntToPacket(unitID, targetID, newHP);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createBuildPacket(int unitType, float x, float y) throws IOException {
		dataOut.writeByte(PACKET_BUILD);
		addIntToPacket(unitType);
		addFloatToPacket(x, y);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	// add unit
	public byte[] createAddUnitPacket(int unitType, int unitID, int hp, float x, float y) throws IOException {
		dataOut.writeByte(PACKET_ADD_UNIT);
		addIntToPacket(unitType, unitID, hp);
		addFloatToPacket(x, y);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	// remove unit
	public byte[] createRemoveUnitPacket(int unitID) throws IOException {
		dataOut.write(PACKET_REMOVE_UNIT);
		addIntToPacket(unitID);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
}
