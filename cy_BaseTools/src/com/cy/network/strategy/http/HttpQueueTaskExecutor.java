package com.cy.network.strategy.http;

import java.util.Queue;

import android.content.Context;

import com.cy.tracer.Tracer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/* queue task for executed one by one, and then response for caller with eventbus */

public class HttpQueueTaskExecutor extends AsyncHttpResponseHandler {
	private Queue<BaseRequest> mQueue;
	private Context mContext;
	private AsyncHttpClient mAsyncHttpClient;

	public HttpQueueTaskExecutor(Context context, Queue<BaseRequest> requestQueue) {
		mContext = context;
		mQueue = requestQueue;
		mAsyncHttpClient = HttpRequestFactory.getClient();
	}

	public void execute() {
		executeInternal();
	}

	private void executeInternal() {
		if (null == mQueue) {
			return;
		}
		
		if (mQueue.size() > 0) {
			BaseRequest request = mQueue.peek();

			if (request.getRequestParams() != null) {
				mAsyncHttpClient.get(mContext, request.getUrl(), request.getRequestParams(), this);
			} else {
				mAsyncHttpClient.get(mContext, request.getUrl(), this);
			}
		}
	}

	/* when success, notify observer and execute next */
	@Override
	public void onSuccess(String response) {
		super.onSuccess(response);

		if (null != mQueue ) {
			if (mQueue.size() > 0) {
				BaseRequest request = mQueue.poll();
				try {
					if (request.getResponseHandler() != null) {
						request.getResponseHandler().onSuccess(response);
					}
				} catch (Exception e) {
					String ext = "HttpQueueTaskExecutor:"+response;
					Tracer.debugException(e, ext);
				}
			}
			executeInternal();
		}
	}

	/* quit if one task failed */
	@Override
	public void onFailure(Throwable execption) {
		if (null != mQueue) {
			if (mQueue.size() > 0) {
				BaseRequest request = mQueue.peek();
				if (request.getResponseHandler() != null) {
					request.getResponseHandler().onFailure(execption);
				}

				mQueue.poll();
			}
			mQueue.clear();
			mQueue = null;
		}
	}
	
	public synchronized void cancelRequest() {
		if (null != mQueue) {
			mQueue.clear();
		}
		if (null != mAsyncHttpClient) {
			mAsyncHttpClient.cancelRequests(mContext, true);
		}
	}
	
	public boolean isCompleted() {
		if (null != mQueue) {
			return mQueue.isEmpty();
		}
		return true;
	}
}