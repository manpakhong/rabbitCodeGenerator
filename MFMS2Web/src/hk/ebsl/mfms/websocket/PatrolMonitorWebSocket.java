package hk.ebsl.mfms.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value = "/websocket/patrolMonitor")
//@ServerEndpoint(value = "/websocket/emulator", encoders = { JsonEncoder.class
//}, decoders = { JsonDecoder.class })
public class PatrolMonitorWebSocket {

	// private static final List<Sticker> stickers = Collections
		// .synchronizedList(new LinkedList<Sticker>());
		private static final Set<Session> sessions = Collections
				.synchronizedSet(new HashSet<Session>());

		public PatrolMonitorWebSocket() {
			// TODO Auto-generated constructor stub
		}

		@OnOpen
		public void open(Session session, EndpointConfig config) {
			
			if(!sessions.contains(session))
				session.setMaxIdleTimeout(1000*60*120);
				sessions.add(session);
			// for (Sticker sticker : stickers) {
			// session.getBasicRemote().sendObject(sticker);
			// }
		}

		@OnClose
		public void onClose(Session session) {
			sessions.remove(session);
		}

		@OnMessage
		public void onMessage(Session session, String msg) {
			// public void onMessage(Session session, JsonMsg msg) {

			for (Session openSession : sessions) {
//				synchronized (this) {
					try {
						openSession.getBasicRemote().sendText(msg);
						// openSession.getBasicRemote().sendObject(msg);
					} catch (IOException e) {
						e.printStackTrace();
						// sessions.remove(openSession);
					}
//				}
			}

		}
		
		@OnError
		 public void onError(Session session, java.lang.Throwable throwable){
	        System.err.println("Guest" + session.getId() + " error: " + throwable);
	        onClose(session);
	    }
}
