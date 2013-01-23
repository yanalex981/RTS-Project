import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import rts.networking.PacketFactory;

public class PacketTesting {
	PacketFactory factory = new PacketFactory();
	String name = "ALEX";
	byte[] packet;
	byte[] controlPacket;
	
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	DataOutputStream dataOut = new DataOutputStream(out);
	
	FileOutputStream fileOut;
	
	File test = new File("test.packet");
	File control = new File("control.packet");
	
	@Test
	public void test() {
		try {
			packet = factory.createConnectPacket(name);
			
			dataOut.writeByte(0);
			dataOut.writeChars(String.format("%-" + 64 + "S", name));
			
			controlPacket = out.toByteArray();
			
			System.out.println(packet.length);
			System.out.println(controlPacket.length);
			
			fileOut = new FileOutputStream(test);
			fileOut.write(packet);
			
			fileOut = new FileOutputStream(control);
			fileOut.write(controlPacket);
			
			if (!compareBytes(packet, controlPacket)) {
				fail("Packets do not match");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean compareBytes(byte[] a, byte[] b) {
		int minLength = Math.min(a.length, b.length);
		
		for (int i = 0; i < minLength; ++i) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		
		return true;
	}
}
