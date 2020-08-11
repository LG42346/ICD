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

@WebServlet("/SendQuestion")
public class SendQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SendQuestion() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

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
		
		
		// Set the MIME type for the response message
		// response.setContentType("text/html");

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
			
			String question = request.getParameter("question");
			String student = request.getParameter("student");


			socketTCP = new Socket(host, serverPort);

			xmlRequest commandXML = new xmlRequest();	
			Document sendQuestionRequest = commandXML.requestQuestion(theme,question, student);

			XMLReadWrite.documentToSocket(sendQuestionRequest, socketTCP);

			out.write("OK");
			out.flush();

		} catch (Exception e) {

			e.printStackTrace();
			out.write("Error: " + e.getMessage());

		} finally {

			socketTCP.close();
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}

}
