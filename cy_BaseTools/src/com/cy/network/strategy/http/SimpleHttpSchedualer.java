package com.cy.network.strategy.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;

public class SimpleHttpSchedualer {
	public static AsyncHttpClient ansycSchedual(Context context,BaseSession session){
		AsyncHttpClient client = HttpRequestFactory.getClient();
		
		if(session.getRequestParams() != null) {
			client.get(context, session.getUrl(), session.getRequestParams(), session.getResponseHandler());
		} else {
			client.get(context, session.getUrl(), session.getResponseHandler());
		}
		return client;
	}
	
	/**
	 * post请求
	 * @param context
	 * @param session
	 * @param request
	 * @return
	 */
	public static AsyncHttpClient ansycSchedual(Context context,BaseSession session, String request){
		AsyncHttpClient client = HttpRequestFactory.getClient();
		if(session.getRequestParams() != null) {
			client.post(context, session.getUrl(), session.getRequestParams(), session.getResponseHandler());
		} else {
			client.post(context, session.getUrl(), null, session.getResponseHandler());
		}
		return client;
	}
}
