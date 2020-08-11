package studentClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import server.Stats;
import tools.Constants;
import tools.Theme;
import tools.XMLReadWrite;
import tools.xmlRequest;

public class Student {

	private studentVariables v;
	private GUIRegistration registration;
	private GUIStudent gui;

	private int state;
	private final int WAIT_REGISTRATION = 1;
	private final int REGISTRATION = 2;
	private final int WAIT = 3;
	private final int SHOW_QUESTION = 4;
	private final int WAIT_ANSWER = 5;
	private final int SEND_ANSWER = 6;
	private final int FINISH = 0;

	private String question;
	private ArrayList<String> answers;
	private ArrayList<String> photos;

	private String selectedTheme;
	private int selectedQuestion;
	
	DatagramSocket udp;
	Socket tcp;
	
	private xmlRequest commandXML;
	private Document request;
	private Document reply;
	
	public Stats sts;
	private HandleConnectionThread thUDP;
	
	private final long timeout = 60000;
	private long timestamp;


	public static void main(String[] args) {

		String host = Constants.DEFAULT_HOSTNAME;
		int serverPort = Constants.DEFAULT_SERVER_PORT;
		int port = Constants.DEFAULT_PORT;
		
		if (args.length > 0) {
			host = args[0];
		}

		if (args.length > 1) {
			try {
				port = Integer.parseInt(args[1]);
				if (port < 1 || port > 65535)
					port = Constants.DEFAULT_PORT;
			}
			catch (NumberFormatException e) {
				System.err.println("Error allocating port" + e.getMessage());
			}
		}

		System.out.println("-> " + host + ":" + port);

		Socket socket = null;
		DatagramSocket sockUDP = null;

		try {
			
			socket = new Socket(host, serverPort);
			sockUDP = new DatagramSocket(port);
			
			Student s = new Student(socket, sockUDP);
			s.run();

		} catch (Exception e) {
			System.err.println("Connection Error: " + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (socket != null)
					socket.close();

				if (sockUDP != null)
					sockUDP.close();

			} catch (Exception e) {
				// if an I/O error occurs when closing this socket
				System.err.println("Connection closure Error: " + e.getMessage());
			}
		}
	}


	public Student(Socket tcp, DatagramSocket udp) throws SocketException, UnknownHostException {

		this.tcp = tcp;
		this.udp = udp;
	

		v = new studentVariables();
		registration = new GUIRegistration(v);

		state = WAIT_REGISTRATION;
		
		thUDP = new HandleConnectionThread(udp, gui);
		thUDP.start();
	}


	public void run() {

		while (state != FINISH) {
//			try {
//				this.sockUDP = new DatagramSocket();
//				
//				sockUDP.setSoTimeout(1000);
//			} catch (SocketException e) {
//				// Auto-generated catch block
//				e.printStackTrace();
//			}
			studentSwitch();
		}
		Document request = new xmlRequest().requestExit(v.getNumber());
		
		XMLReadWrite.documentToSocket(request, tcp);
		
		System.err.println("Student Thread terminated");
		System.exit(0);
	}


	private void studentSwitch() {

		switch (state) {

		case WAIT_REGISTRATION:

			activityWait(500);

			if (v.isEnd()) {
				state = FINISH;
			}

			if (v.isRegistered() && state == WAIT_REGISTRATION) {
				state = REGISTRATION;
			}					

			break;
			
		case REGISTRATION:
			
			commandXML = new xmlRequest();
			request = commandXML.requestRegistration(v.getName(), v.getNumber(), v.getEmail(), v.getDate(), v.getPassword());

			XMLReadWrite.documentToSocket(request, tcp);
			reply = XMLReadWrite.documentFromSocket(tcp);
			
			String status = reply.getElementsByTagName("status").item(0).getTextContent();
			
			if (status.equals("success") && state == REGISTRATION) {
				
				registration.dispose();	
				gui = new GUIStudent(v.getName(), v);
				thUDP.setGUI(gui);
				
				state = WAIT;
			}
			else if (state == REGISTRATION) {
				
				registration.sendMessage();
				
				state = WAIT_REGISTRATION;
			}
			
			break;
			
		case WAIT:			
			
			reply = XMLReadWrite.documentFromSocket(tcp);
			
			String s = reply.getElementsByTagName("theme").item(0).getTextContent();
			System.out.println("@REPLY: " + s);

			if (v.isEnd()) {
				state = FINISH;
			}
			if (reply == null) {
				break;
			}

			if (state == WAIT) {				
				state = SHOW_QUESTION;
			}
			break;

		case SHOW_QUESTION:
			
			Node themeName = reply.getElementsByTagName("theme").item(0);	
			Node questionNumber = reply.getElementsByTagName("number").item(0);
			
			String numberStr = questionNumber.getTextContent();
			selectedQuestion = Integer.parseInt(numberStr);
			
			selectedTheme = themeName.getTextContent();
			
			Node questionNode = reply.getElementsByTagName("text").item(0);
			question = questionNode.getTextContent();
			
			photos = new ArrayList<>();
			NodeList photosNodes = reply.getElementsByTagName("photo");
			for (int i = 0; i < photosNodes.getLength(); i++) {
				photos.add(photosNodes.item(i).getTextContent());
			}
			
			answers = new ArrayList<>();
			NodeList options = reply.getElementsByTagName("option");
			for (int i = 0; i < options.getLength(); i++) {
				answers.add(options.item(i).getTextContent());
			}
			
			Node multiple = reply.getElementsByTagName("multiple").item(0);
			boolean multipleCorrects = (multiple.getTextContent().equals("true")) ? true : false;
			
			v.setQuestion(question);
			v.setAnswers(answers);
			v.setPhotos(photos);
			v.setMultipleCorrects(multipleCorrects);

			gui.showQuestionInfo();
			
			timestamp = System.currentTimeMillis();

			if (state == SHOW_QUESTION) {
				state = WAIT_ANSWER;
			}
			
			break;

		case WAIT_ANSWER:

			activityWait(500);

			if (v.isEnd()) {
				state = FINISH;
			}

			if (v.isAnswered() && state == WAIT_ANSWER) {
				state = SEND_ANSWER;
			}
			
			if (System.currentTimeMillis() - timestamp > timeout && state == WAIT_ANSWER) {
				gui.reset();
				v.setAnswer(new ArrayList<>());
				state = SEND_ANSWER;
			}
			
			break;

		case SEND_ANSWER:

			v.setAnswered(false);
			
			commandXML = new xmlRequest();
			request = commandXML.requestAnswer(selectedTheme, selectedQuestion, v.getAnswer());
			
			XMLReadWrite.documentToSocket(request, tcp);
			reply = XMLReadWrite.documentFromSocket(tcp);

			v.setAnswer(new ArrayList<>());

			if (state == SEND_ANSWER) {
				state = WAIT;
			}
			break;

		case FINISH:
			break;

		default:
			System.err.println("Error in class Student: StudentSwitch() -> State: " + state);
			break;
		}
	}


	private void activityWait(long time) {
		
		try {
		
			Thread.sleep(time);
		
		} catch (InterruptedException e) {
			System.err.println(
					"Student activityWait(time) error - Thread.sleep(tempo) not working: " + e.getMessage());
		}
	}
}



class HandleConnectionThread extends Thread {

	private DatagramSocket socketUDPReceived;
	
	private GUIStudent gui;

	public HandleConnectionThread(DatagramSocket connectionUDP, GUIStudent gui) throws SocketException, UnknownHostException {

		this.socketUDPReceived = connectionUDP;
		socketUDPReceived.setSoTimeout(4000);
		
		this.gui = gui;

	}
	
	public void setGUI(GUIStudent gui) {
		this.gui = gui;
	}
	
	public void run() {

		while (true) {
			
			try {
				
				Thread.sleep(1500);
				
				byte inputBuffer[]  = new byte[Constants.DIM_BUFFER];
				DatagramPacket inputPacket = new DatagramPacket(inputBuffer, inputBuffer.length);
				inputPacket.setLength(Constants.DIM_BUFFER);

				System.out.println("@Waiting");
				socketUDPReceived.receive(inputPacket);
				System.out.println("@Received");
				
				Object statsObj = Stats.deserialize(inputPacket.getData());
				Hashtable<String, Integer> stats = (Hashtable<String, Integer>) statsObj;
				gui.RefreshStats(stats);

//				InetAddress address = inputPacket.getAddress();
//				int port = inputPacket.getPort();
//				String s = new String(inputPacket.getData(), 0, inputPacket.getLength());
//				System.out.println("@" + s);
//				System.out.println("@FROM " + inputPacket.getPort() + s);
			
			}catch(Exception e) {
				
				System.err.println("@@RESTART UDP");
				e.printStackTrace();
			}
		}
	}
	
}

