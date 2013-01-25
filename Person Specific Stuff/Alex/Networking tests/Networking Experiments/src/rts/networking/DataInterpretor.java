package rts.networking;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class DataInterpretor {
	private DataInterpretor() {}
	
	public static int getInstruction(byte[] packet) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(packet);
		DataInputStream dataIn = new DataInputStream(byteIn);
		
		return dataIn.readByte();
	}
	
	public static int getIntData(byte[] packet, int offset) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(packet);
		DataInputStream dataIn = new DataInputStream(byteIn);
		dataIn.skipBytes(offset);
		
		return dataIn.readInt();
	}
	
	public static float getFloatData(byte[] packet, int offset) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(packet);
		DataInputStream dataIn = new DataInputStream(byteIn);
		dataIn.skipBytes(offset);
		
		return dataIn.readFloat();
	}
	
	public static String getStringData(byte[] packet, int offset, int stringLength) throws IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(packet);
		DataInputStream dataIn = new DataInputStream(byteIn);
		dataIn.skipBytes(offset);
		
		int length = Math.min(dataIn.available() / (Character.SIZE / 8), stringLength);
		String s = "";
		
		for (int i = 0; i < length; ++i) {
			s += dataIn.readChar();
		}
		
		return s;
	}
}
