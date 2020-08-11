package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import tools.Constants;

public class Server {

	public Server() {
		
	
	}
	
	public static void main(String[] args) throws IOException {
		
		InetAddress broadcast = InetAddress.getByName(Constants.DEFAULT_BROADCAST);
		DatagramSocket ds = new DatagramSocket(Constants.DEFAULT_SERVER_PORT);
		ds.setSoTimeout(1000);
		ds.setBroadcast(true);

		byte[] buffer = new String("").getBytes();
		//PORT: packet tells from what port this comes from(server) not to whom it goes
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcast, Constants.DEFAULT_SERVER_PORT);
		ds.send(packet);
		ds.close();

		
//		byte[] buf = new byte[256];
//		InetAddress multicast = InetAddress.getByName(Constants.DEFAULT_MULTICAST);
//		MulticastSocket ms = new MulticastSocket(Constants.DEFAULT_SERVER_PORT);
//		ms.joinGroup(multicast);
//		DatagramPacket packet1 = new DatagramPacket(buf, buf.length);
//		ms.receive(packet1);
//		String received = new String(packet1.getData(), 0, packet1.getLength());
//		ms.leaveGroup(multicast);
//	    ms.close();

	}
	
}
