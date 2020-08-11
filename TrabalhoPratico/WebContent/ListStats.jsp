<%@page import="java.net.Socket"%>
<%@page import="java.util.Hashtable"%>
<%@page import="server.Stats"%>
<%@page import="java.net.DatagramPacket"%>
<%@page import="tools.Constants"%>
<%@page import="java.net.DatagramSocket"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ListStats</title>
</head>
<body>

	<%
		int port = Constants.DEFAULT_PORT;

	DatagramSocket socketUDP;

	try {
		socketUDP = new DatagramSocket(port);

	} catch (Exception e) {

		socketUDP = null;
	}

	byte inputBuffer[] = new byte[Constants.DIM_BUFFER];
	DatagramPacket inputPacket = new DatagramPacket(inputBuffer, inputBuffer.length);
	inputPacket.setLength(Constants.DIM_BUFFER);

	System.out.println("@Waiting");
	
	if (socketUDP != null) {
		socketUDP.receive(inputPacket);
	}
	
	System.out.println("@Received");

	String questions = "0";
	String answers = "0";
	String corrects = "0";
	String incorrects = "0";

	if (inputPacket.getAddress() != null) {

		Object statsObj = Stats.deserialize(inputPacket.getData());
		Hashtable<String, Integer> stats = (Hashtable<String, Integer>) statsObj;

		questions = "" + stats.get(Stats.keyNumberQuestions);
		answers = "" + stats.get(Stats.keyNumberAnswers);
		corrects = "" + stats.get(Stats.keyNumberCorrects);
		incorrects = "" + stats.get(Stats.keyNumberIncorrects);

	}

	if (socketUDP != null) {
		socketUDP.close();
	}
	%>

	<!-- UDP Stats table -->
	<div class="navbar fixed-bottom">

		<table class="fixed-bottom table table-striped table-dark">
			<tr>
				<th>Total Perguntas</th>
				<th class="d-flex justify-content-end"><%=questions%></th>
			</tr>
			<tr>
				<th>Total Respostas</th>
				<td class="d-flex justify-content-end"><%=answers%></td>
			</tr>
			<tr>
				<th>Respostas Corretas</th>
				<td class="d-flex justify-content-end"><%=corrects%></td>
			</tr>
			<tr>
				<th>Respostas Incorretas</th>
				<td class="d-flex justify-content-end"><%=incorrects%></td>
			</tr>
		</table>
	</div>


</body>
</html>