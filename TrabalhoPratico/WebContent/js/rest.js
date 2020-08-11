function restAJAX(){

	$.ajax({ 
		type: "GET",
		datatype: "text/html",
		url: "http://localhost:9091/TrabalhoPratico/rest/RestEndpoint/test",
		success: function(data){        
			alert(data);
		}
	});

}

function restJSAJAX(){

	var xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			document.getElementById("output").innerHTML = this.responseText;
		}
	};

	xhttp.open("GET", "http://localhost:9091/TrabalhoPratico/rest/RestEndpoint/test", true);
	xhttp.send();
}