package ws;
//package ws;
//
//
//import java.io.IOException;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//
//@ServerEndpoint(value = "/ws")
//public class WebSocketServer {
//    private static final AtomicInteger sequence = new AtomicInteger(1);
//    private final String username;
//    private Session session;
//
//    public WebSocketServer() {
//        username = "User" + sequence.getAndIncrement();
//    }
//
//    @OnOpen
//    public void start(Session session) {
//        this.session = session;
//        String message = "*" + username + "* connected.";
//        System.out.println("OnOpen Triggered");
//        sendMessage(message);
//    }
//
//    @OnClose
//    public void end() {
//        // clean up once the WebSocket connection is closed
//    }
//
//    @OnMessage
//    public void receiveMessage(String message) {
//        
//    	// one should never trust the client, and sensitive HTML
//        // characters should be replaced with &lt; &gt; &quot; &amp;
//        String reversedMessage = new StringBuffer(message).reverse().toString();
//        sendMessage("[" + username + "] " + reversedMessage);
//    }
//
//    @OnError
//    public void handleError(Throwable t) {
//        t.printStackTrace();
//    }
//
//    private void sendMessage(String text) {
//        // uses *this* object's session to call sendText()
//        try {
//            this.session.getBasicRemote().sendText(text);
//        } catch (IOException e) {
//            // clean up once the WebSocket connection is closed
//            try {
//                this.session.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }
//}