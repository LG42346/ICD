package tools;

import java.io.File;

public class Constants {
	
	public static final String pathProtocolXSD = "WebContent" + File.separator + "protocol.xsd"; 
	public static final String pathScienceXML = "WebContent" + File.separator + "themes" + File.separator + "science.xml"; 
	public static final String pathGeographyXML = "WebContent" + File.separator + "themes" + File.separator + "geography.xml"; 
	public static final String pathCultureXML = "WebContent" + File.separator + "themes" + File.separator + "culture.xml"; 

	public static final String scienceFileName = "science.xml"; 
	public static final String geographyFileName = "geography.xml"; 
	public static final String cultureFileName = "culture.xml";
	
	public static final String pathStatsDoc = "WebContent" + File.separator + "doc.dat";
	public static final String statsDocName = "doc.dat";
	
	public static final int refreshStatsTimeMillis = 5000;
	
	public final static int DIM_BUFFER  = 64 * 1024;

	
	public static final int DEFAULT_PORT = 5025;
	public static final int DEFAULT_TEACHER_PORT = 5024;
	public static final int DEFAULT_SERVER_PORT = 5023;
	
	public static final String DEFAULT_BROADCAST = "255.255.255.255";
	public static final String DEFAULT_LOCAL_BROADCAST = "192.168.1.255";
	public static final String DEFAULT_MULTICAST = "230.0.0.0";
	public static final String DEFAULT_HOSTNAME = "localhost";
	
}
