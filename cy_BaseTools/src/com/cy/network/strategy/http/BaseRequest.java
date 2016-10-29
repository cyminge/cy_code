package com.cy.network.strategy.http;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public interface BaseRequest {
	public boolean getSyncParam();
	public String getUrl();
	public RequestParams getRequestParams();
	public AsyncHttpResponseHandler getResponseHandler(); 
	
	public String onSuccessBefore(String responce);
	public Object onSuccessDoMore(String responce, Object respObj);
	public void onSuccessAfter(Object respObj);
}
