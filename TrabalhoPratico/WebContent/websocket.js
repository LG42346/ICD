var wsUri = "ws://" + document.location.host + document.location.pathname + "test";
var websocket = new WebSocket(wsUri);

websocket.onerror = function(evt) { onError(evt) };

var output = document.getElementById("output");

websocket.onopen = function(evt) { onOpen(evt) };

function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function writeToScreen(message) {
    output.innerHTML += message + "<br>";
}

function onOpen() {
    writeToScreen("Connected to " + wsUri);
}

websocket.onmessage = function(evt) { onMessage(evt) };

function sendText(json) {
    console.log("sending text: " + json);
    websocket.send(json);
}
                
function onMessage(evt) {
    console.log("received: " + evt.data);
    drawImageText(evt.data);
}

