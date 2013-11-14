/*
 ** Copyright (C) 2013 Mellanox Technologies
 **
 ** Licensed under the Apache License, Version 2.0 (the "License");
 ** you may not use this file except in compliance with the License.
 ** You may obtain a copy of the License at:
 **
 ** http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing, software
 ** distributed under the License is distributed on an "AS IS" BASIS,
 ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 ** either express or implied. See the License for the specific language
 ** governing permissions and  limitations under the License.
 **
 */
package com.mellanox.jxio.tests.benchmarks;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mellanox.jxio.ClientSession;
import com.mellanox.jxio.EventQueueHandler;
import com.mellanox.jxio.Msg;

public class StatClientSession {
	private final static Log LOG = LogFactory.getLog(StatTest.class.getCanonicalName());

	EventQueueHandler        eqh = null;
	int                      clients_count;
	ClientSession            clients[];

	public StatClientSession(EventQueueHandler eqh, String uriString, int num_clients) {
		this.clients_count = 0;
		this.eqh = eqh;
		this.clients = new ClientSession[num_clients];
		
		URI uri = null;
	    try {
	    	uri = new URI(uriString);
	    } catch (URISyntaxException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
		
		for (int i = 0; i < num_clients; i++) {
			this.clients[i] = new ClientSession(eqh, uri, new StatSesClientCallbacks(i));
		}
	}

	public void close() {
		for (ClientSession cs : clients) {
			cs.close();
		}
	}

	public void print(String str) {
		LOG.debug("********" + str);
	}

	class StatSesClientCallbacks implements ClientSession.Callbacks {
		int session_num;

		public StatSesClientCallbacks(int index) {
			this.session_num = index;
		}

		public void onMsgError() {
			// logger.log(Level.debug, "onMsgErrorCallback");
			print("onMsgErrorCallback");
		}

		public void onSessionEstablished() {
			// logger.log(Level.debug, "[SUCCESS] Session Established! Bring the champagne!");
			print(session_num + " Session Established");
			clients[session_num].close();
		}

		public void onSessionEvent(int session_event, String reason) {

			String event;
			switch (session_event) {
				case 0:
					event = "SESSION_REJECT";
					break;
				case 1:
					event = "SESSION_TEARDOWN";
					print(session_num + " Session Teardown");
					clients_count++;
					if (clients_count == clients.length) {
						print(session_num + " Stopping Event Loop");
						eqh.breakEventLoop();
					}
					break;
				case 2:
					event = "CONNECTION_CLOSED";
					// This is fine - connection closed by choice
					// there are two options: close session or reopen it
					break;
				case 3:
					event = "CONNECTION_ERROR";
					// this.close(); //through the bridge calls connection close
					break;
				case 4:
					event = "SESSION_ERROR";
					break;
				default:
					event = "UNKNOWN_EVENT";
					break;
			}
			print(session_num + "GOT EVENT " + event + " because of " + reason);
		}

		public void onReply(Msg msg) {

		}
	}
}
