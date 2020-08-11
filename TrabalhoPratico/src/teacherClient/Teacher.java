package teacherClient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import server.Info;
import server.Stats;
import tools.Constants;
import tools.XMLReadWrite;
import tools.xmlRequest;


public class Teacher extends Thread {

	private GUITeacher gui;
	private teacherVariables v;

	private int state;
	private final int REGISTER = 1;
	private final int WAIT = 2;
	private final int CHOOSE_THEME = 3;
	private final int REFRESH_STUDENTS = 4;
	private final int SEND_QUESTION = 5;
	private final int WAIT_ANSWER = 6;
	private final int FINISH = 0;

	static String password ="";

	DatagramSocket udp;
	Socket tcp;

	private xmlRequest commandXML;
	private Document request;
	private Document reply;


	public static void main(String[] args) {

		//TODO DISABLE PORT ATTRIBUTION SO UDP KNOWS WHERE TO SEND

		String host = Constants.DEFAULT_HOSTNAME;
		int serverPort = Constants.DEFAULT_SERVER_PORT;
		int port = Constants.DEFAULT_PORT -1;

		if (args.length > 0) {

			password = args[0];
		}

		if (args.length > 1) {

			host = args[0];
		}

		if (args.length > 2) {

			try {

				port = Integer.parseInt(args[1]);

				if (port < 1 || port > 65535) {

					port = Constants.DEFAULT_PORT;
				}

			}catch (NumberFormatException e) {

				System.err.println("Error allocating port" + e.getMessage());
			}
		}

		System.out.println("-> " + host + ":" + port);

		Socket socket = null;
		DatagramSocket sockUDP = null;

		try {

			socket = new Socket(host, serverPort);
			sockUDP = new DatagramSocket(port);

			Teacher t = new Teacher(socket, sockUDP);
			t.run();

		} catch (Exception e) {

			System.err.println("Connection Error: " + e.getMessage());
			e.printStackTrace();


		} finally {

			try {

				if (socket != null) {

					socket.close();
				}

				if (sockUDP != null) {

					sockUDP.close();
				}

			} catch (Exception e) {

				// if an I/O error occurs when closing this socket
				System.err.println("Connection closure Error: " + e.getMessage());
			}
		}
	}


	public Teacher(Socket tcp, DatagramSocket udp) throws SocketException, UnknownHostException {

		this.tcp = tcp;
		this.udp = udp;

		v = new teacherVariables();		
		gui = new GUITeacher(v, tcp);

		state = REGISTER;

		Thread thUDP = new HandleConnectionThread(udp, gui);
		thUDP.start();
	}


	public void run() {

		while (state != FINISH) {

			teacherSwitch();
		}

		System.err.println("Teacher Thread terminated");
		System.exit(0);
	}


	private void teacherSwitch() {

		switch (state) {

		case REGISTER:

			// send registration to server
			commandXML = new xmlRequest();
			request = commandXML.requestTeacherRegistration(password);

			XMLReadWrite.documentToSocket(request, tcp);
			reply = XMLReadWrite.documentFromSocket(tcp);

			String status = reply.getElementsByTagName("status").item(0).getTextContent();

			//if reg fails sys.exit(0) or start registration frame again				
			if (status.equals("success") && state == REGISTER) {
				state = WAIT;
			}
			else {
				System.err.println("Teacher registration failed");
				state = FINISH;
			}

			break;

		case WAIT:

			activityWait(500);

			if (v.isEnd()) {
				state = FINISH;
			}
			if (!v.getTheme().equals("") && state == WAIT) {
				state = CHOOSE_THEME;
			}
			if (v.isRefresh() && state == WAIT) {
				state = REFRESH_STUDENTS;
			}
			if (v.isQuestionSent() && state == WAIT) {
				state = SEND_QUESTION;
			}

			break;

		case CHOOSE_THEME:

			String theme = v.getTheme();

			commandXML = new xmlRequest();
			request = commandXML.requestQuestions(theme);

			XMLReadWrite.documentToSocket(request, tcp);
			reply = XMLReadWrite.documentFromSocket(tcp);

			ArrayList<String> questions = new ArrayList<>();
			NodeList received = reply.getElementsByTagName("question");

			if (received.getLength() == 0) {
				System.out.println("\nThis theme does not exist!");
			}
			else {
				for (int i = 0; i < received.getLength(); i++) {	
					questions.add(received.item(i).getTextContent());
				}
			}

			v.setQuestions(questions);		
			gui.setListOfQuestions();
			v.setTheme("");

			if (state == CHOOSE_THEME) {
				state = WAIT;
			}

			break;

		case REFRESH_STUDENTS:

			v.setRefresh(false);

			commandXML = new xmlRequest();
			request = commandXML.requestInfos();

			XMLReadWrite.documentToSocket(request, tcp);
			reply = XMLReadWrite.documentFromSocket(tcp);

			ArrayList<Info> studentsList = new ArrayList<>();
			NodeList nodeReceived = reply.getElementsByTagName("student");

			if (nodeReceived.getLength() == 0) {
				System.out.println("\nNo student is online right now!");
			}
			else {
				for (int i = 0; i < nodeReceived.getLength(); i++) {

					Info info = new Info(null, null);

					NodeList attrs = nodeReceived.item(i).getChildNodes();
					if (attrs.item(0).getNodeName().equals("name")){

						info.setName(attrs.item(0).getTextContent());
						info.setNumber(Integer.parseInt(attrs.item(1).getTextContent()));

					}else {

						info.setName(attrs.item(1).getTextContent());
						info.setNumber(Integer.parseInt(attrs.item(0).getTextContent()));
					}

					studentsList.add(info);
				}
			}

			v.setStudentsList(studentsList);		
			gui.refreshStudentsList();

			if (state == REFRESH_STUDENTS) {
				state = WAIT;
			}

			break;

		case SEND_QUESTION:

			v.setQuestionSent(false);	
			gui.enableButtons(false);

			xmlRequest commandXML = new xmlRequest();	
			Document request = commandXML.requestQuestion(v.getThemeToSend(), v.getQuestionNumberToSend(), v.getStudentNumberToSend());

			XMLReadWrite.documentToSocket(request, tcp);

			if (state == SEND_QUESTION) {
				state = WAIT_ANSWER;
			}

			break;

		case WAIT_ANSWER:

			Document reply = XMLReadWrite.documentFromSocket(tcp);

			if (v.isEnd()) {
				state = FINISH;
			}
			if (reply == null) {
				break;
			}

			String s = reply.getElementsByTagName("sended").item(0).getTextContent();

			System.out.println("@REPLY:" + s);

			gui.enableButtons(true);

			if (state == WAIT_ANSWER) {
				state = WAIT;
			}

			break;

		case FINISH:

			break;

		default:

			System.err.println("Error in class Teacher: teacherSwitch() -> State: " + state);

			break;
		}
	}

	private void activityWait(long time) {

		try {
			Thread.sleep(time);

		} catch (InterruptedException e) {

			System.err.println("Teacher activityWait(time) error - Thread.sleep(tempo) not working: " + e.getMessage());
		}
	}
}


class HandleConnectionThread extends Thread {

	private DatagramSocket socketUDPReceived;

	private GUITeacher gui;

	public HandleConnectionThread(DatagramSocket connectionUDP, GUITeacher gui) throws SocketException, UnknownHostException {

		this.socketUDPReceived = connectionUDP;
		socketUDPReceived.setSoTimeout(4000);

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
