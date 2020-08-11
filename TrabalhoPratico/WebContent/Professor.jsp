<%@page import="java.net.Socket"%>
<%@page import="tools.Constants"%>
<%@page import="tools.xmlRequest"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="tools.XMLReadWrite"%>
<%@page import="org.w3c.dom.Element"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">
<title>ProfessorJSP</title>
<%@include file="links.html"%>

</head>

<%@include file="navbar.html"%>

<style>
html, body {
	height: 100%;
}
</style>

<body>
	


	<div class="container-fluid text-center">

		<div class="row justify-content-center">
			<img src="photos/isel.bmp" alt="ISEL">
		</div>
		
		<p> http://localhost:8080/TrabalhoPratico/Professor.jsp</p>
		

		<div class="row">

			<div class="col-sm">

				<select
					id="selectTheme" class="custom-select custom-select-lg mb-3 bg-dark text-white">
					<option value="" disabled selected>Temas:</option>
					<option value="1">Cultura</option>
					<option value="2">Geografia</option>
					<option value="1">Ciência</option>
				</select> <select
					id="selectQuestion" class="custom-select custom-select-lg mb-3 bg-dark text-white">
					<option value="" disabled selected>Perguntas:</option>
				</select>
				 <select
					id="selectStudent" class="custom-select custom-select-lg mb-3 bg-dark text-white">
					<option value="" disabled selected>Aluno:</option>
				</select>

			</div>

			<div class="col-sm">
				

					<input type="text" class="form-control" name="hostname"
						placeholder="Type your hostname" readonly value="localhost"> 
						
						<input type="text" class="form-control" name="port"
						placeholder="Type your port (1-65535)"> 
						
					<input type="password"
						class="form-control" name="password"
						placeholder="Type your password">

					<button id="submit" class="btn btn-lg btn-primary btn-block"
						type="submit" value="Send">Sign in</button>

				</form>

			</div>



			<div class="col-sm btn-group-vertical">
				<button id="send1" type="button" class="btn btn-dark">Enviar para
					aluno</button>
				<button id="sendRand" type="button" class="btn btn-dark">Enviar para
					aleatório</button>
				<button id = "sendAll" vtype="button" class="btn btn-dark">Enviar para
					todos</button>
			</div>

		</div>



	</div>

	<div class="row ">

		<div id="progressBar">
			<div></div>
		</div>
		<script></script>

	</div>

	</div>
	
	
	<!-- 
	<div>
		<input class="form-control" type="text"
			placeholder="Readonly input here…" readonly>
	</div>
	-->


<%-- 	<% --%>
	
// 		Socket socketTCP = null;

// 	String host = Constants.DEFAULT_HOSTNAME;
// 	int serverPort = Constants.DEFAULT_SERVER_PORT;

// 	String errorMessage = "none";

// 	try {

// 		socketTCP = new Socket(host, serverPort);

// 		System.out.println("after");

// 		xmlRequest commandXML = new xmlRequest();
// 		Document registerRequest = commandXML.requestTeacherRegistration();

// 		XMLReadWrite.documentToSocket(registerRequest, socketTCP);
// 		Document registerReply = XMLReadWrite.documentFromSocket(socketTCP);
// 		Element root = registerReply.getDocumentElement();
// 		String registerReplyText = root.toString();

// 		String status = registerReply.getElementsByTagName("status").item(0).getTextContent();
<%-- 	%> --%>

<!-- 	<pre> -->
<%-- 	<%=registerReplyText%> --%>
<!-- 	</pre> -->

<%-- 	<script>$.notify("<%=status%>", "info");</script> --%>

<%-- 	<% --%>
// 		} catch (Exception e) {

// 		System.out.println(e.getMessage());

// 		errorMessage = e.getMessage();

// 		out.print("<h1>Exception</h1>");
<%-- 	%> --%>

<%-- 	<script>$.notify("<%=errorMessage%>");</script> --%>

<%-- 	<% --%>
// 		} finally {
// 		if (socketTCP != null) {

// 			socketTCP.close();
// 		}
// 	}
<%-- 	%> --%>

	<div id="output"></div>
	
	<script>
	
	$("#selectStudent").focus(function(){
		  $.ajax({dataType: "json", url: "ListStudents", success: function(result){
			  
			  var nStudents = Object.keys(result).length; 
			  $("#nStudents").text(nStudents);

			  
			  $("#selectStudent").empty();			  
			  
			  $.each( result, function( key, val ) { 
				  var $option = '<option value="'+key+'">'+val+'</option>';
				  $("#selectStudent").append($option);
			  });

			  
			
		  }});
		});
	
	</script>
	
	<script>
	
	$("#selectQuestion").focus(function(){
		  $.ajax({method: "POST", data: {theme: $('#selectTheme').find(":selected").text()} ,dataType: "json", url: "ListQuestions", success: function(result){
			  
			  //var nSQuestions = Object.keys(result).length; 
			  //$("#nStudents").text(nStudents);

			  
			  $("#selectQuestion").empty();
			  
			  $.each( result, function( key, val ) { 
				  var $option = '<option value="'+key+'">'+val+'</option>';
				  $("#selectQuestion").append($option);
			  });

			  
			
		  }});
		});
	
	</script>
	
	<script>
	
	function waitAnswer(){
		
		$("#send1").prop("disabled",true);
		$("#sendRand").prop("disabled",true);
		$("#sendAll").prop("disabled",true);
		
		progress(60, 60, $('#progressBar'));

	}
	
	</script>
	
	<script>
	
	$("#send1").click(function(){
		
		
		
		var stu = $('#selectStudent').find(":selected").val();
		var the =  $('#selectTheme').find(":selected").text();
		var que = $('#selectQuestion').find(":selected").val();
			
		  $.ajax({method: "POST", data: {student: stu, question: que, theme: the} ,dataType: "json", url: "SendQuestion", success: function(result){
			 $.notify(result);			
		  }});
		  
		  waitAnswer();
		});
	
	$("#sendRand").click(function(){
		
		var len = $("#selectStudent")[0].length;
		//var len = $("#selectStudent").length;
		var rand = Math.floor(Math.random() * len);  
		var stu = $('#selectStudent')[0][rand].value;
		var the = $('#selectTheme').find(":selected").text();
		var que =$('#selectQuestion').find(":selected").val();
			
		  $.ajax({method: "POST", data: {student: stu, question: que, theme: the} ,dataType: "json", url: "SendQuestion", success: function(result){
			 $.notify(result);			
		  }});
		  
		  waitAnswer();
		});
	
	$("#sendAll").click(function(){
		
		var stu = 0;
		var the =  $('#selectTheme').find(":selected").text();
		var que = $('#selectQuestion').find(":selected").val();
			
		  $.ajax({method: "POST", data: {student: stu, question: que, theme: the} ,dataType: "json", url: "SendQuestion", success: function(result){
			 $.notify(result);			
		  }});
		  
		  waitAnswer();
		});
	
	</script>
	
	

	<div id="listStats">
		<jsp:include page="ListStats.jsp" />
	</div>


	<!-- Import Modal -->
	<div class="modal" id="importModal">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Importar Perguntas</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">pergunta 1</div>
				<div class="modal-body">pergunta 2</div>


				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Students Modal -->
	<div class="modal" id="studentsModal">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Alunos Presentes</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">aluno 1</div>
				<div class="modal-body">aluno 2</div>


				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

</body>

</html>