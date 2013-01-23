package rts.networking;

import java.io.IOException;

public interface PacketInterpretor {
	public int getInstruction(byte[] packet) throws IOException;
	public int[] getIntData(byte[] packet, int offset) throws IOException;
	public float[] getFloatData(byte[] packet, int offset) throws IOException;
	public String getStringData(byte[] packet, int offset, int stringLength) throws IOException;
}
