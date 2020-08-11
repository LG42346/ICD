package teacherWebClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import teacherClient.Teacher;
import tools.Constants;

@WebServlet("/TeacherWebClient")
public class TeacherWebClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TeacherWebClient() {
		super();
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		// Set the MIME type for the response message
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.write("");

		out.write("<!DOCTYPE html>");
		out.write("<html>");
		out.write("<head>");
		out.write("<meta charset='UTF-8'>");
		out.write("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
		out.write("<title>'Login'</title>");
		out.write("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">");
		out.write("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css\" integrity=\"sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk\" crossorigin=\"anonymous\">");
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bg.css\">");
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/navbar.css\">");
		out.write("<script type=\"text/javascript\" src=\"js/jquery-3.4.1.min.js\"></script>");
		out.write("<script src=\"js/formValidator.js\"></script>");
		out.write("</head>");

		out.write("<div class=\"navbar\">");
		out.write("<a class=\"active\" href=\"#\"><i class=\"fa fa-fw fa-home\"></i> Home</a>");
		out.write("<a href=\"#\"><i class=\"fa fa-fw fa fa-cloud-download\"></i> Importar</a>");
		out.write("<a href=\"#\"><i class=\"fa fa-fw fa fa-cloud-upload\"></i> Exportar</a>");
		out.write("<a href=\"#\"><i class=\"fa fa-fw  fa fa-users\"></i> Presenças</a>");
		out.write("<a href=\"#\"><i class=\"fa fa-fw fa-user\"></i> Login</a>");
		out.write("</div>");

		out.write("<body>");
		out.write("<div id=\"login\" class=\"d-flex justify-content-center\">");
		out.write("<form class=\"form-signin\">");
		out.write("<h1 class=\"h3 mb-3 font-weight-normal d-flex justify-content-center \">Please sign in</h1>");
		out.write("<input type=\"text\" class=\"form-control\" name=\"username\" placeholder=\"Type your username\">");
		out.write("<input type=\"password\" class=\"form-control\" name=\"password\" placeholder=\"Type your password\">");
		out.write("<button id=\"submit\" class=\"btn btn-lg btn-primary btn-block\" type=\"submit\" value=\"Send\" >Sign in</button>");
		out.write("</form>");	  
		out.write("</div>");
		
		
		
		
//       out.write("<div class=\"w3-light-grey w3-round\">");
//       out.write("<div class=\"w3-container w3-round w3-blue\" style=\"width:25%\">25%</div>");
//       out.write("</div>");

		out.write("</body>");
		out.write("</html>");

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}

}
