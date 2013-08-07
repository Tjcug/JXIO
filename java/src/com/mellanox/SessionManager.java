package com.mellanox;

import java.util.concurrent.atomic.AtomicInteger;

public class SessionManager {
	private EventQueueHandler eventQHndl = null;
	
	public SessionManager(String url, int port){
		eventQHndl = new EventQueueHandler ();
		eventQHndl.runEventLoop(1, 0);
//		JXBridge.startServer(url, port, eventQHndl.getID());
	}
	
	public void close(){
//		JXBridge.stopServer();
	}
	
	
	/*amir's code
	
	private static AtomicInteger id_counter ;
	private static JXSessionBase[] sessions ;
	private static SessionManager manager = null ;
	
	
	private SessionManager(int array_size){
		
		id_counter = new AtomicInteger(0);
		sessions = new JXSessionBase[array_size];
	}
	
	public static SessionManager getSessionManager(){
		if(manager==null){
			manager = new SessionManager(1000);
		}
		return manager;
	}
	
	public synchronized  int setSessionEntry(JXSessionBase s){
		int session_id = id_counter.getAndIncrement();
		if(session_id == sessions.length){
			enlargeArray();
		}
		sessions[session_id] = s;
		return session_id;
	}
	
	public void deleteSessionEntry(int session_id)
	{}
	
	public JXSessionBase getSession(int session_id){
		return sessions[session_id];
	}
	
	
	// to be implemented
	private void enlargeArray()
	{}

	*/
}
