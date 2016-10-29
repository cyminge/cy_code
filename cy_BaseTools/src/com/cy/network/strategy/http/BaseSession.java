package com.cy.network.strategy.http;

import java.io.Serializable;

import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class BaseSession extends AsyncHttpResponseHandler implements BaseRequest, Serializable, Taggable {
	
	private static final long serialVersionUID = -4603686282777547036L;
	
	public static String NWT_ERROR = "-1000";

	@Override
	public AsyncHttpResponseHandler getResponseHandler() {
		return this;
	}

	@Override
	public String getTag() {
		return toString();
	}
	
}
