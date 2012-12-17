package rts.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws IOException {
		byte[] buf = new byte[] {'T', 'E', 'S', 'T'};
		
//		DatagramSocket socket = new DatagramSocket(666, InetAddress.getLocalHost());
		DatagramSocket socket = new DatagramSocket();
		
		DatagramPacket out;// = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 666);
		
		String in = "";
		Scanner scan = new Scanner(System.in);
		
		while (!in.equals("quit")) {
			System.out.print("Enter something: ");
			in = scan.nextLine();
			buf = in.getBytes();
			out = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 666);
			socket.send(out);
		}
	}
}
