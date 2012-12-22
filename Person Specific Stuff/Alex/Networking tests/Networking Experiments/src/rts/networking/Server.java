package rts.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

// Important classes:
// DatagramSocket
// DatagramPacket
// MulticastSocket
public class Server {
	/* TODO
	 * Generate a random int
	 * Assign to player
	 * Attach to packet
	 * Verify number
	 * To prevent small hacks
	 */

	DatagramSocket socket;
	DatagramPacket packet;
	byte[] data = new byte[666];
	
	public Server() {
		try {
			socket = new DatagramSocket(666, InetAddress.getLocalHost());
			packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 666);
		}
		catch (SocketException e) {
			e.printStackTrace();
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		try {
			while (true) {
				socket.receive(packet);
				System.out.println(new String(data) + " " + data.length);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
