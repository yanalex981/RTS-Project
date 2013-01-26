

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
	public static final byte PACKET_MOVE = 4;		// 2 floats
	public static final byte PACKET_UPDATE_XY = 5;	// 2 floats
	public static final byte PACKET_ATTACK = 6;		// 1 int
	public static final byte PACKET_UPDATE_HP = 7;	// 1 int
	public static final byte PACKET_BUILD = 8;		// 1 int
	public static final byte PACKET_ADD_UNIT = 9;
	public static final byte PACKET_REMOVE_UNIT = 10;
	
	public static final int UNIT_MARINE = 0;			// 1 int
	public static final int UNIT_WORKER = 1;			// 1 int
	public static final int UNIT_COMMAND_CENTER = 2;	// 1 int
	public static final int UNIT_BARRACKS = 3;			// 1 int
	public static final int UNIT_GENERATOR = 4;			// 1 int
	
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
			throw new InvalidParameterException("String data too long");
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
	
	public byte[] createMovePacket(String username, int unitID, float x, float y) throws IOException {
		dataOut.writeByte(PACKET_MOVE);
		addStringToPacket(username);
		addIntToPacket(unitID);
		addFloatToPacket(x, y);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createUpdateXYPacket(String username, int unitID, float newX, float newY) throws IOException {
		dataOut.writeByte(PACKET_UPDATE_XY);
		addStringToPacket(username);
		addIntToPacket(unitID);
		addFloatToPacket(newX, newY);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createAttackPacket(String username, int unitID, int targetID) throws IOException {
		dataOut.writeByte(PACKET_ATTACK);
		addStringToPacket(username);
		addIntToPacket(unitID, targetID);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createUpdateHPPacket(String username, int unitID, int targetID, int newHP) throws IOException {
		dataOut.writeByte(PACKET_UPDATE_HP);
		addStringToPacket(username);
		addIntToPacket(unitID, targetID, newHP);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
	
	public byte[] createBuildPacket(String username, int unitType, int x, int y) throws IOException {
		dataOut.writeByte(PACKET_BUILD);
		addStringToPacket(username);
		addIntToPacket(unitType, x, y);
		
		byte[] temp = bytesOut.toByteArray();
		
		flushOutput();
		
		return temp;
	}
}
