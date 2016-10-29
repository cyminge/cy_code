package com.cy.network.strategy.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpPollTask {
	public final static int INT_DEF = 60 * 1000;
	public final static int INT_1S = 1000;
	public final static int INT_5S = 5000;
	public final static int INT_10S = 10 * 1000;
	public final static int INT_15S = 15 * 1000;
	public final static int INT_20S = 20 * 1000;
	public final static int INT_40S = 40 * 1000;
	public final static int INT_80S = 80 * 1000;
	public final static int MAX_TIMES = Integer.MAX_VALUE;

	public final static int POLL_TIME = 30 * 60000;

	private AsyncHttpClient client = null;

	public String getUrl() {
		return session.getUrl();
	}

	public long getTimes() {
		return times;
	}

	public int getTimeInterval() {
		return timeInterval;
	}

	public int setTimeInterval(int time) {
		timeInterval = time;
		return timeInterval;
	}

	public AsyncHttpResponseHandler getHandler() {
		return session.getResponseHandler();
	}

	public void countDown() {
		times--;
	}

	public void cancelTask() {
		if (client != null) {
			client.cancelRequests(context, true);
		}
	}

	public void execute() {
		String url = session.getUrl();
		RequestParams requestParams = session.getRequestParams();
		if (null != url && null != requestParams) {
			if (client == null) {
				client = new AsyncHttpClient();
			} else {
				/* if there has task not finished, cancel task */
				cancelTask();
			}
			
			client.get(context, url, requestParams, session.getResponseHandler());
		}
	}

	public HttpPollTask(Context context, BaseSession request, int firstInterval, int times, int timeInterval) {
		super();
		this.context = context;
		session = request;
		this.times = times;
		this.timeInterval = timeInterval;
		this.firstInterval = firstInterval;
	}

	public int getFirstInterval() {
		return firstInterval;
	}

	private Context context;
	private int times = 0;
	private int timeInterval = 0;
	private int firstInterval = 0;
	private BaseSession session = null;
}
