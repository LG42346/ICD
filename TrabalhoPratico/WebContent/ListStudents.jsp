<%@page import="org.w3c.dom.NodeList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="tools.XMLReadWrite"%>
<%@page import="server.Info"%>
<%@page import="java.net.Socket"%>
<%@page import="tools.Constants"%>
<%@page import="tools.xmlRequest"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ListStudents</title>
</head>
<body>

	<%
		String host = Constants.DEFAULT_HOSTNAME;
	int serverPort = Constants.DEFAULT_SERVER_PORT;

	Socket socketTCP = null;
	String errorMessage = "none";

	try {

		socketTCP = new Socket(host, serverPort);

		System.out.println("after");

		xmlRequest commandXML = new xmlRequest();
		Document requestInfos = commandXML.requestInfos();

		XMLReadWrite.documentToSocket(requestInfos, socketTCP);
		Document reply = XMLReadWrite.documentFromSocket(socketTCP);

		ArrayList<Info> studentsList = new ArrayList<>();
		NodeList nodeReceived = reply.getElementsByTagName("student");

		if (nodeReceived.getLength() == 0) {
			System.out.println("\nNo student is online right now!");
		} else {
			for (int i = 0; i < nodeReceived.getLength(); i++) {

		Info info = new Info(null, null);

		NodeList attrs = nodeReceived.item(i).getChildNodes();
		if (attrs.item(0).getNodeName().equals("name")) {

			info.setName(attrs.item(0).getTextContent());
			info.setNumber(Integer.parseInt(attrs.item(1).getTextContent()));

		} else {

			info.setName(attrs.item(1).getTextContent());
			info.setNumber(Integer.parseInt(attrs.item(0).getTextContent()));
		}

		studentsList.add(info);
			}

			for (Info studentInfo : studentsList) {
	%>

	<h1><%=studentInfo.getName()%><h1>

			<%
				}

			}

			} catch (Exception e) {

				System.out.println(e.getMessage());

			} finally {
				socketTCP.close();
			}
			%>
		
</body>
</html>