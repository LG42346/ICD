package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class EmissorBroadCast {

	private static DatagramSocket socket = null;

	public static void main(String[] args) throws IOException, InterruptedException {
		while(true) {
			broadcast("Hello", InetAddress.getByName("255.255.255.255"));
			Thread.sleep(1000);
		}
	}

	public static void broadcast(String broadcastMessage, InetAddress address) throws IOException {
		
		socket = new DatagramSocket();
		socket.setBroadcast(true);

		byte[] buffer = broadcastMessage.getBytes();

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 2115);
		System.out.println("@SENDING");
		socket.send(packet);
		System.out.println("@SENT");
//		socket.close();
	}
}
