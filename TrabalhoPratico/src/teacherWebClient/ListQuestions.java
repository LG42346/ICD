package teacherWebClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import server.Info;
import tools.Constants;
import tools.XMLReadWrite;
import tools.xmlRequest;

@WebServlet("/ListQuestions")
public class ListQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ListQuestions() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		// Set the MIME type for the response message
				// response.setContentType("text/html");

				String theme = request.getParameter("theme");

				if (theme.equalsIgnoreCase("CULTURA")) {

					theme = Constants.cultureFileName;

				}
				if (theme.equalsIgnoreCase("GEOGRAFIA")) {

					theme = Constants.geographyFileName;

				}
				if (theme.equalsIgnoreCase("CIÊNCIA")) {

					theme = Constants.scienceFileName;

				}

				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");

				// TODO getFromSesh
				String host = Constants.DEFAULT_HOSTNAME;
				int serverPort = Constants.DEFAULT_SERVER_PORT;

				Socket socketTCP = null;
				String errorMessage = "none";

				JSONObject resultJSON = new JSONObject();

				try {

					socketTCP = new Socket(host, serverPort);

					System.out.println("after");
					System.out.println("theme: " + theme);

					xmlRequest commandXML = new xmlRequest();
					Document requestQuestions = commandXML.requestQuestions(theme);

					XMLReadWrite.documentToSocket(requestQuestions, socketTCP);
					Document reply = XMLReadWrite.documentFromSocket(socketTCP);

					ArrayList<String> questions = new ArrayList<>();
					NodeList received = reply.getElementsByTagName("question");

					if (received.getLength() == 0) {
						System.out.println("\nThis theme does not exist!");
					} else {
						for (int i = 0; i < received.getLength(); i++) {
							questions.add(received.item(i).getTextContent());
						}
					}

					int i = 1;
					for (String question : questions) {

						resultJSON.put("" + i, question);
						i++;
					}

					out.write(resultJSON.toString());
					out.flush();

				} catch (Exception e) {

					e.printStackTrace();

				} finally {

					socketTCP.close();
				}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
		
		
	}

}
