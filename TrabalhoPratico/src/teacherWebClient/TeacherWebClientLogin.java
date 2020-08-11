package teacherWebClient;



import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class first
 */
@WebServlet("/TeacherWebClientLogin")
public class TeacherWebClientLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TeacherWebClientLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
 // Runs on a thread whenever there is HTTP GET request
    // Take 2 arguments, corresponding to HTTP request and response
    public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {
  
       // Set the MIME type for the response message
       response.setContentType("text/html");
       // Write to network
       PrintWriter out = response.getWriter();
  
       // Your servlet's logic here
       out.write("<html>\r\n  ");
       double num = Math.random();
       if (num > 0.95) {
          out.write("<h2>You will have a luck day!");
          out.write("</h2><p>(");
          out.print( num );
          out.write(")</p>\r\n");
       } else {
          out.write("\r\n    ");
          out.write("<h2>Well, life goes on ... ");
          out.write("</h2><p>(");
          out.print( num );
          out.write(")</p>\r\n  ");
       }
       out.write("<a href=\"");
       out.print( request.getRequestURI() );
       out.write("\">");
       out.write("<h3>Try Again</h3></a>\r\n");
       out.write("</html>\r\n");
    }
  
    // Runs as a thread whenever there is HTTP POST request
    public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {
       // do the same thing as HTTP GET request
       doGet(request, response);
    }
  

}
