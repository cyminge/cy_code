package com.cy.network.strategy.http;

import java.util.Vector;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;


public class HttpRequestFactory {
	private static Vector<AsyncHttpClient> clientVector = new Vector<AsyncHttpClient>();
	
//	public static SyncHttpClient getSyncHttpClient() {
//		AsyncHttpClient client = new SyncHttpClient();
//		clientVector.add(client);
//		return client;
//	}
	
	public static AsyncHttpClient getClient() {
		AsyncHttpClient client = new AsyncHttpClient();
		return client;
	}
	
	public static void retrieveClient(Context context, AsyncHttpClient client) {
		if(client == null) {
			return;
		}
		if(clientVector.contains(client)){
			client.cancelRequests(context, true);
			clientVector.remove(client);
		}
	}
	
	public static void retrieveAll(Context context) {
		AsyncHttpClient client = null;
		for(int i = 0; i < clientVector.size(); i ++) {
			client = clientVector.get(i);
			client.cancelRequests(context, true);
		}
		clientVector.clear();
	}
}
