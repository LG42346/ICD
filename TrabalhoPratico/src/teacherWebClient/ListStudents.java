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

@WebServlet("/ListStudents")
public class ListStudents extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ListStudents() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

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

			socketTCP = new Socket(host, serverPort);

			System.out.println("after");

			xmlRequest commandXML = new xmlRequest();
			Document requestInfos = commandXML.requestInfos();

			XMLReadWrite.documentToSocket(requestInfos, socketTCP);
			Document reply = XMLReadWrite.documentFromSocket(socketTCP);

			ArrayList<Info> studentsList = new ArrayList<>();
			NodeList nodeReceived = reply.getElementsByTagName("student");

			// TODO what returns if no students ajax
			if (nodeReceived.getLength() == 0) {
				System.out.println("\nNo student is online right now!");

			} else {

				for (int i = 0; i < nodeReceived.getLength(); i++) {

					Info info = new Info(null, null);

					NodeList attrs = nodeReceived.item(i).getChildNodes();

					if (attrs.item(0).getNodeName().equals("name")) {

						info.setName(attrs.item(0).getTextContent());
						info.setNumber(Integer.parseInt(attrs.item(1).getTextContent()));
						
						;

					} else {

						info.setName(attrs.item(1).getTextContent());
						info.setNumber(Integer.parseInt(attrs.item(0).getTextContent()));
					}
					
					long t = System.currentTimeMillis() - Long.parseLong(attrs.item(2).getTextContent());
					info.setLoginTime(t);
					studentsList.add(info);

				}

				for (Info studentInfo : studentsList) {

					resultJSON.put("" + studentInfo.getNumber(), studentInfo.getName() + " ->" + (int) ((studentInfo.getLoginTime() / (1000*60)) % 60) + "minutos");
				}
			}

			out.write(resultJSON.toString());
			out.flush();

		} catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {

			socketTCP.close();
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}

}
