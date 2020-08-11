package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import tools.xmlRequest;
import tools.Constants;
import tools.XMLReadWrite;

public class Server {

	private ArrayList<Info> infos;   
	private Stats stats;

	public Server() {

		infos = new ArrayList<>();
		stats = new Stats();
	}

	private ArrayList<Info> getInfos() {

		return infos;
	}
	
	private Stats getStats() {
		return stats;
	}


	public static void main(String[] args){

		int port = Constants.DEFAULT_SERVER_PORT; 

		if (args.length > 0) {

			try {

				port = Integer.parseInt(args[0]);

			}catch (NumberFormatException e) {

				System.err.println("Error with chosen port" + e.getMessage());
			}
		}


		ServerSocket serverSocket = null;
		Socket sock = null;  
		DatagramSocket sockUDP = null;

		try {
			
			Server server = new Server();
			ArrayList<Info> infos = server.getInfos();
			Stats stats = server.getStats();
			
			//UDP semaphore is not needed because server only has one thread to handle UDP COM
			//Also there are no individual sockets for individual connections
			sockUDP = new DatagramSocket(port);
			Thread thUDP = new HandleConnectionThread(sockUDP, stats);
			thUDP.start();

			
			serverSocket = new ServerSocket(port);
			
			for( ; ; ) {

				//waits for client TCP connection to create dedicated thread
				System.out.println("Server  waiting for connection on port " + port + "..." );

				sock = serverSocket.accept(); 
				Thread th = new HandleConnectionThread(sock, infos, stats);
				th.start();

			}

		}catch (Exception e) {

			System.err.println("ServerFault: " + e);
		}
	}
} 


class HandleConnectionThread extends Thread {

	private Socket socketReceived;
	private DatagramSocket socketUDPReceived;
	private ArrayList<Info> infos;
	private Stats stats;
	private Semaphore semaphore;

	private InetAddress broadcast;
	
	private int threadID;


	public HandleConnectionThread(Socket connection, ArrayList<Info> infos, Stats stats) throws SocketException {

		this.socketReceived = connection;
		this.infos = infos;
		this.stats = stats;

		semaphore = new Semaphore(1);
		
		threadID = 0;

//		this.socketReceived.setSoTimeout(Constants.refreshStatsTimeMillis);
	}


	public HandleConnectionThread(DatagramSocket connectionUDP, Stats stats) throws SocketException, UnknownHostException {

		this.socketUDPReceived = connectionUDP;
		this.stats = stats;
		
		broadcast = InetAddress.getByName(Constants.DEFAULT_BROADCAST);

	}


	public void run() { 

		//differentiate if is UDP/TCP Thread
		if(socketReceived != null) {

			runTCP();

		}

		if(socketUDPReceived != null) {

			runUDP();
		}

	}


	private void runTCP() {

		Document xmlRequest = null;		

		try {

			System.out.println("Thread " + this.getId() + ": " + socketReceived.getRemoteSocketAddress());

			for (;;) {

				semaphore.acquire();
				
				xmlRequest = XMLReadWrite.documentFromSocket(socketReceived);
				Document xmlReply = new xmlRequest(xmlRequest).reply(infos, socketReceived, semaphore, stats);
				XMLReadWrite.documentToSocket(xmlReply, socketReceived);

				semaphore.release();

				blockStudentThread(xmlReply, semaphore);
			}	

		}catch (Exception e) {

			System.err.println("Finished with the " + socketReceived + " connection: " + e.getMessage());

		}finally {

			System.out.println("@@FINALLY!!!!!");

			try {

				if (socketReceived != null) {
					socketReceived.close();
				}	
				
				if(socketUDPReceived != null) {
					socketUDPReceived.close();
				}

			} catch (Exception e) {

				System.err.println("Connection closure error: " + e.getMessage());
			}
		}
	}


	private void runUDP() {

		byte inputBuffer[]  = new byte[Constants.DIM_BUFFER];
		DatagramPacket inputPacket = new DatagramPacket(inputBuffer, inputBuffer.length);
		inputPacket.setLength(Constants.DIM_BUFFER);

		for ( ; ; ) {

			try {
				currentThread().sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				System.out.println("@BROADCASTING");
				
				byte[] s = stats.serialize();
				broadcastStats(s);
				
				System.out.println("@BROADCASTED");


			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	
	private void blockStudentThread(Document xmlReply, Semaphore semaphore) {

		if (xmlReply.getElementsByTagName("registration").getLength() == 1 && 
				xmlReply.getElementsByTagName("status").item(0).getTextContent().equals("success") || threadID == 1) {

			threadID = 1;
			try {

				semaphore.acquire();

			} catch (InterruptedException e) {

				System.err.println("Error -> Thread student Semaphore.acquire() : " + e.getMessage());
			}
			
		}
		
	}


	private void broadcastStats(byte[] broadcastBuffer) throws IOException, TransformerException, InterruptedException {

		//fills broadcastBuffer with the stats document data
//		byte[] broadcastBuffer = documentToByteArray(stats);
		DatagramPacket broadcastPacket = new DatagramPacket(broadcastBuffer, broadcastBuffer.length, broadcast, Constants.DEFAULT_PORT);

		socketUDPReceived.setBroadcast(true);
		socketUDPReceived.send(broadcastPacket);
		
//		String s = new String(broadcastPacket.getData(), 0, broadcastPacket.getLength());
//		System.out.println("@" + s);
	}


	private byte[] documentToByteArray(Document doc) throws TransformerException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();	
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(bos);
		transformer.transform(source, result);
		return bos.toByteArray();
	}


	private void activityWait(long time) {

		try {

			Thread.sleep(time);

		} catch (InterruptedException e) {

			System.err.println("Server activityWait(time) error - Thread.sleep(time) not working: " + e.getMessage());
		}
		
	}

} 
