package tools;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import server.Info;
import server.Stats;

public class xmlRequest {
	Document doc = null; 

	public xmlRequest() {
		doc = XMLReadWrite.documentFromString("<?xml version='1.0' encoding='ISO-8859-1'?><protocol></protocol>");
	}

	public xmlRequest(Document D) {
		doc = D;
	}

	public void show() {
		XMLReadWrite.writeDocument(doc, System.out);
		System.out.println();
	}

	public Document reply(ArrayList<Info> infos, Socket socket, Semaphore semaphore, Stats stats) throws SAXException {  // usar no servidor

		Document com = null;

		//if (xmlUtil.validateDocument(doc, Constants.pathProtocolXSD))
		//	System.out.println("\nValidação da mensagem do cliente realizada com sucesso!");

		if (doc.getElementsByTagName("registerteacher").getLength() == 1)
			com = replyTeacherRegister(infos, socket, semaphore);

		if (doc.getElementsByTagName("registration").getLength() == 1)
			com = replyRegister(infos, socket, semaphore);

		if (doc.getElementsByTagName("refresh").getLength() == 1)
			com = replyRefresh(infos);

		if (doc.getElementsByTagName("questions").getLength() == 1)
			com = replyAllQuestions();

		if (doc.getElementsByTagName("question").getLength() == 1)
			com = replyQuestion(infos, stats, semaphore);

		if (doc.getElementsByTagName("answer").getLength() == 1)
			com = replyAnswer(infos, stats);

		if (doc.getElementsByTagName("exit").getLength() == 1)
			com = replyExit(infos);

		if(com == null)
			return doc;

		else {
			//if (xmlUtil.validateDocument(com, Constants.pathProtocolXSD))
			//	System.out.println("\nValidação com XSD realizada com sucesso!");
			return com;
		}
	}	

	public Document requestTeacherRegistration(String password) {

		Element student = doc.createElement("registerteacher");
		Element request = doc.createElement("request");


		try (PrintWriter out = new PrintWriter("password.txt")) {
			out.println(password);
		}catch(Exception e) {}

		student.appendChild(request);

		Element protocol = (Element) doc.getElementsByTagName("protocol").item(0);	
		protocol.appendChild(student);

		return doc;
	}

	private Document replyTeacherRegister(ArrayList<Info> infos, Socket socket, Semaphore semaphore) {


		Info info = new Info(socket, semaphore);

		info.setName("Teacher");
		info.setNumber(-1);
		info.setEmail("");
		info.setDate("");

		String status = "success";
		infos.add(info);


		Element reply = doc.createElement("reply");
		Element clone = (Element) doc.createElement("status");
		clone.setTextContent(status);
		reply.appendChild(clone);

		Element root = (Element) doc.getElementsByTagName("registerteacher").item(0);
		root.appendChild(reply);

		return doc;
	}


	public Document requestRegistration(String name, String number, String email, String date, String password) {

		Element student = doc.createElement("registration");
		Element request = doc.createElement("request");

		student.appendChild(request);

		Element protocol = (Element) doc.getElementsByTagName("protocol").item(0);	
		protocol.appendChild(student);

		Element nameNode = doc.createElement("name");	
		nameNode.appendChild(doc.createTextNode(name));
		request.appendChild(nameNode);

		Element numberNode = doc.createElement("number");	
		numberNode.appendChild(doc.createTextNode(number));
		request.appendChild(numberNode);

		Element emailNode = doc.createElement("email");	
		emailNode.appendChild(doc.createTextNode("" + email));
		request.appendChild(emailNode);

		Element dateNode = doc.createElement("date");	
		dateNode.appendChild(doc.createTextNode(date));
		request.appendChild(dateNode);

		Element passwordNode = doc.createElement("password");	
		passwordNode.appendChild(doc.createTextNode(password));
		request.appendChild(passwordNode);

		return doc;
	}

	private Document replyRegister(ArrayList<Info> infos, Socket socket, Semaphore semaphore) {

		Node passwordNode = doc.getElementsByTagName("password").item(0);
		String pwd = passwordNode.getTextContent();
		String pw = "";

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("password.txt"));
			pw = reader.readLine();
			reader.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(!pwd.equals(pw)) {

			String status = "unsuccess";

			Element reply = doc.createElement("reply");

			Element clone = (Element) doc.createElement("status");
			clone.setTextContent(status);
			reply.appendChild(clone);

			Element root = (Element) doc.getElementsByTagName("registration").item(0);
			root.appendChild(reply);

			return doc;
		}

		Node nameNode = doc.getElementsByTagName("name").item(0);
		Node numberNode = doc.getElementsByTagName("number").item(0);
		Node emailNode = doc.getElementsByTagName("email").item(0);
		Node dateNode = doc.getElementsByTagName("date").item(0);



		Info info = new Info(socket, semaphore);

		info.setName(nameNode.getTextContent());
		int number = Integer.parseInt(numberNode.getTextContent());
		info.setNumber(number);
		info.setEmail(emailNode.getTextContent());
		info.setDate(dateNode.getTextContent());

		String status = "";
		if (infos.size() > 0) {
			for (int i = 0; i < infos.size(); i++) {
				if (infos.get(i).getNumber() == number) {
					status = "unsuccess";
					break;
				}
				if (i == infos.size() - 1) {
					status = "success";
					infos.add(info);
					break;
				}
			}
		}
		else {
			status = "success";
			infos.add(info);
		}

		Element reply = doc.createElement("reply");

		Element clone = (Element) doc.createElement("status");
		clone.setTextContent(status);
		reply.appendChild(clone);

		Element root = (Element) doc.getElementsByTagName("registration").item(0);
		root.appendChild(reply);

		return doc;
	}

	public Document requestInfos() {

		Element refresh = doc.createElement("refresh");
		Element request = doc.createElement("request");
		refresh.appendChild(request);

		Element protocol = (Element) doc.getElementsByTagName("protocol").item(0);
		protocol.appendChild(refresh);

		return doc;
	}

	private Document replyRefresh(ArrayList<Info> infos) {

		System.out.println("@REFRESH");

		Element reply = doc.createElement("reply");

		for (Info i : infos) {
			if (i.getNumber() != -1) {

				Element student = doc.createElement("student");
				
				Element cloneName = (Element) doc.createElement("name");
				cloneName.setTextContent(i.getName());
				student.appendChild(cloneName);

				Element cloneNumber = (Element) doc.createElement("number");
				cloneNumber.setTextContent(i.getNumber() + "");
				student.appendChild(cloneNumber);

				Element cloneLoginTime = (Element) doc.createElement("login");
				cloneLoginTime.setTextContent("" +i.getLoginTime());
				student.appendChild(cloneLoginTime);
				
				reply.appendChild(student);
			}
		}

		Element root = (Element) doc.getElementsByTagName("refresh").item(0);
		root.appendChild(reply);

		return doc;
	}

	public Document requestQuestions(String theme) {  // usar no cliente

		// <protocol><questions><request><theme>...</theme></request></questions></obter>

		Element questions = doc.createElement("questions");
		Element request = doc.createElement("request");	
		questions.appendChild(request);

		Element protocol = (Element) doc.getElementsByTagName("protocol").item(0);
		protocol.appendChild(questions);

		Element themeNode = doc.createElement("theme");	
		themeNode.appendChild(doc.createTextNode(theme));
		request.appendChild(themeNode);

		return doc;
	}

	private Document replyAllQuestions() {

		Node theme = doc.getElementsByTagName("theme").item(0);

		String themeName = theme.getTextContent();

		ArrayList<String> questions = new Theme(themeName).getAllThemeQuestions();

		Element reply = doc.createElement("reply");

		for (int i = 0; i < questions.size(); i++) {
			Element clone = (Element) doc.createElement("question");
			clone.setTextContent(questions.get(i));
			reply.appendChild(clone);
		}

		Element root = (Element) doc.getElementsByTagName("questions").item(0);
		root.appendChild(reply);

		return doc;
	}

	public Document requestQuestion(String theme, String number, String student) { 

		Element question = doc.createElement("question");
		Element request = doc.createElement("request");
		question.appendChild(request);

		Element protocol = (Element) doc.getElementsByTagName("protocol").item(0);
		protocol.appendChild(question);

		Element themeNode = doc.createElement("theme");	
		themeNode.appendChild(doc.createTextNode(theme));
		request.appendChild(themeNode);

		Element numberNode = doc.createElement("number");	
		numberNode.appendChild(doc.createTextNode(number));
		request.appendChild(numberNode);

		Element studentNode = doc.createElement("student");	
		studentNode.appendChild(doc.createTextNode(student));
		request.appendChild(studentNode);

		return doc;
	}

	private Document replyQuestion(ArrayList<Info> infos, Stats stats, Semaphore semaphore) {

		stats.addQuestion();
		stats.refreshStats();

		Element reply = doc.createElement("reply");
		Element root = (Element) doc.getElementsByTagName("question").item(0);
		root.appendChild(reply);

		replyQuestionStudent();

		Node studentNode = doc.getElementsByTagName("student").item(0);
		String student = studentNode.getTextContent();
		int studentNumber = Integer.parseInt(student);

		int studentCount = 0;
		int index = 0;

		for (int i = 0; i < infos.size(); i++) {
			if(infos.get(i).getNumber() == -1) {

				if (studentNumber == 0)  {
					infos.get(i).setSemaphore(new Semaphore(infos.size() - 2));
				}
				else {
					infos.get(i).setSemaphore(new Semaphore(0));
				}
				semaphore = infos.get(i).getSemaphore();

				index = i;
				break;
			}
		}

		for(Info i: infos) {

			if(i.getNumber() == studentNumber) {

				XMLReadWrite.documentToSocket(doc, i.getSocket());

				i.releaseSemaphore();

				try {

					for (Info info : infos) {
						if (info.getNumber() == -1) {
							info.getSemaphore().acquire();
							break;
						}
					}	

				} catch (InterruptedException e) {
					System.err.println("Error Teacher Semaphore.acquire() at replyQuestion(infos, semaphore) failed: " + e.getMessage());
				}

				break;
			}

			else if(i.getNumber() != -1 && studentNumber == 0) {

				XMLReadWrite.documentToSocket(doc, i.getSocket());

				i.releaseSemaphore();

				try {

					for (Info info : infos) {
						if (info.getNumber() == -1) {
							System.out.println("teacher going to acquire: " + info.getSemaphore().availablePermits());
							info.getSemaphore().acquire();
							System.out.println("teacher acquired: " + info.getSemaphore().availablePermits());
							break;
						}
					}

				} catch (InterruptedException e) {
					System.err.println("Error Teacher Semaphore.acquire() at replyQuestion(infos, semaphore) failed: " + e.getMessage());
				}		

			}
		}

		root.removeChild(reply);

		show();

		if (studentNumber == 0) {
			for (Info i : infos) {
				if (i.getNumber() != -1) {
					studentCount++;

					if (studentCount >= (infos.size() - 1)) {

						Element answered = doc.createElement("reply");
						Element clone = (Element) doc.createElement("sended");
						clone.setTextContent("sended successfully");
						answered.appendChild(clone);

						root.appendChild(answered);

						return doc;
					}
					else {
						try {
							infos.get(index).getSemaphore().acquire();
						} catch (InterruptedException e) {
							System.err.println("Teacher's Semaphore Error: " + e.getMessage());
						}
					}
				}
			}
		}
		else {
			Element answered = doc.createElement("reply");
			Element clone = (Element) doc.createElement("sended");
			clone.setTextContent("sended successfully");
			answered.appendChild(clone);

			root.appendChild(answered);
		}

		return doc;
	}

	private void replyQuestionStudent() {

		Node reply = doc.getElementsByTagName("reply").item(0);

		Node themeNode = doc.getElementsByTagName("theme").item(0);
		String themeName = themeNode.getTextContent();

		Node numberNode = doc.getElementsByTagName("number").item(0);
		String number = numberNode.getTextContent();
		int questionNumber = Integer.parseInt(number);

		Theme theme = new Theme(themeName);

		String question = theme.getQuestion(questionNumber);
		Node cloneQ = doc.createElement("text");
		cloneQ.setTextContent(question);
		reply.appendChild(cloneQ);

		ArrayList<String> photos = theme.getImagesPaths(questionNumber);
		for (String photo : photos) {
			Node cloneP = doc.createElement("photo");
			cloneP.setTextContent(photo);
			reply.appendChild(cloneP);
		}

		ArrayList<String> answers = theme.getOptions(questionNumber);
		for (String option : answers) {
			Node cloneA = doc.createElement("option");
			cloneA.setTextContent(option);
			reply.appendChild(cloneA);
		}

		String multiple = (theme.isMultiple(questionNumber)) ? "true" : "false";
		Node cloneA = doc.createElement("multiple");
		cloneA.setTextContent(multiple);
		reply.appendChild(cloneA);

		show();
	}

	public Document requestAnswer(String theme, int number, ArrayList<String> options) { 

		Element answer = doc.createElement("answer");
		Element request = doc.createElement("request");
		answer.appendChild(request);

		Element protocol = (Element) doc.getElementsByTagName("protocol").item(0);
		protocol.appendChild(answer);

		Element themeNode = doc.createElement("theme");	
		themeNode.appendChild(doc.createTextNode(theme));
		request.appendChild(themeNode);

		Element numberNode = doc.createElement("number");	
		numberNode.appendChild(doc.createTextNode("" + number));
		request.appendChild(numberNode);

		for (String option : options) {
			Element optionNode = doc.createElement("option");	
			optionNode.appendChild(doc.createTextNode(option));
			request.appendChild(optionNode);
		}

		//System.out.println("Thread student requestAnswer -> doc to STUDENT socket: ");
		//show();

		return doc;
	}


	private Document replyAnswer(ArrayList<Info> infos, Stats stats) {

		stats.addAnswer();

		Node themeNode = doc.getElementsByTagName("theme").item(0);
		String themeName = themeNode.getTextContent();

		Node numberNode = doc.getElementsByTagName("number").item(0);
		String number = numberNode.getTextContent();
		int questionNumber = Integer.parseInt(number);

		Element reply = doc.createElement("reply");

		NodeList options = doc.getElementsByTagName("option");

		ArrayList<String> correctAnswers = new Theme(themeName).getCorrectAnswer(questionNumber);

		int correct = 0;
		for (int i = 0; i < options.getLength(); i++) {
			for (int a = 0; a < correctAnswers.size(); a++) {
				String option = options.item(i).getTextContent();
				if (option.equals(correctAnswers.get(a))) {
					correct++;
					break;
				}	
			}
		}

		String correction = "";
		if (options.getLength() > correctAnswers.size()) {
			correction = "wrong";
			stats.addIncorrect();
		}
		else if (correct == correctAnswers.size()) {
			correction = "correct";
			stats.addCorrect();
		}
		else {
			correction = "wrong";
			stats.addIncorrect();
		}

		stats.refreshStats();

		Element clone = (Element) doc.createElement("chosen");
		clone.setTextContent(correction);
		reply.appendChild(clone);

		Element root = (Element) doc.getElementsByTagName("answer").item(0);
		root.appendChild(reply);


		for(Info i: infos) {
			if(i.getNumber() == -1) {
				i.releaseSemaphore();
			}
		}

		return doc;
	}

	public Document requestExit(String studentNumber) {

		Element exit = doc.createElement("exit");
		Element request = doc.createElement("request");
		exit.appendChild(request);

		Element protocol = (Element) doc.getElementsByTagName("protocol").item(0);
		protocol.appendChild(exit);

		Element numberNode = doc.createElement("number");	
		numberNode.appendChild(doc.createTextNode(studentNumber));
		request.appendChild(numberNode);

		//System.out.println("Thread student requestAnswer -> doc to STUDENT socket: ");
		//show();

		return doc;
	}

	private Document replyExit(ArrayList<Info> infos) {

		Node numberNode = doc.getElementsByTagName("number").item(0);
		String number = numberNode.getTextContent();
		int studentNumber = Integer.parseInt(number);

		Element reply = doc.createElement("reply");

		Element clone = (Element) doc.createElement("exited");
		clone.setTextContent("successfully");
		reply.appendChild(clone);

		Element root = (Element) doc.getElementsByTagName("exit").item(0);
		root.appendChild(reply);


		for(Info i: infos) {
			if(i.getNumber() == studentNumber) {
				infos.remove(i);

				if (infos.size() == 1) {
					infos.get(0).releaseSemaphore();
				}
				break;
			}
		}

		return doc;
	}
}
