package rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/RestEndpoint") 
public class RestEndpoint {

	@GET 
	@Path("/test") 
	@Produces(MediaType.TEXT_HTML) 
	public String test() {
		
		return "rested";
	}
}
