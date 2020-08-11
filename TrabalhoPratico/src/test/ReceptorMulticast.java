package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ReceptorMulticast {

	
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];

	public ReceptorMulticast() throws SocketException {
		socket = new DatagramSocket(4446);
	}

	public void run() throws IOException, InterruptedException {
		running = true;

		while (running) {
			
			Thread.sleep(1000);

			DatagramPacket packet = new DatagramPacket(buf, buf.length);
//			System.out.println("@Waiting");
			socket.receive(packet);
//			System.out.println("@Received");

			InetAddress address = packet.getAddress();
			int port = packet.getPort();
			packet = new DatagramPacket(buf, buf.length, address, port);
			String s = new String(packet.getData(), 0, packet.getLength());
			System.out.println("@" + s);
			System.out.println("@FROM " + packet.getPort() + s);

			if (s.equals("end")) {
				running = false;
				continue;
			}
			socket.send(packet);
		}
		socket.close();
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		ReceptorMulticast r = new ReceptorMulticast();
		while (true) {
			r.run();
		}
	}
}
