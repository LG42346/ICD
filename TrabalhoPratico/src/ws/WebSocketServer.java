//package ws;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//
//@ServerEndpoint("/test")
//public class WebSocketServer {
//
//    @OnMessage
//    public String onMessage(String msg) {
//    	System.out.println("msg:" + msg);
//        return null;
//    }
//
//    
//    @OnOpen
//    public void onOpen (Session peer) {
//       System.out.println("HIII");
//    }
//
//    @OnClose
//    public void onClose (Session peer) {
//    	System.out.println("BYEEEE");
//    }
//}