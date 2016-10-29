package com.cy.network.strategy.http;

import android.content.Context;

import com.cy.tracer.Tracer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/* queue task for executed one by one, and then response for caller with eventbus */

public class HttpConditionTaskExecutor extends AsyncHttpResponseHandler {
	private BaseRequest mRequest;
	private Context mContext;
	private AsyncHttpClient mAsyncHttpClient;

	public HttpConditionTaskExecutor(Context context, BaseRequest request) {
		mContext = context;
		mRequest = request;
		mAsyncHttpClient = HttpRequestFactory.getClient();
	}

	public void execute() {
		executeNext();
	}

	private void executeNext() {
		String url = mRequest.getUrl();
		if (null == url) {
			return; // 停止轮训
		}
		
		RequestParams params = mRequest.getRequestParams();
		if (params != null) {
			mAsyncHttpClient.get(mContext, url, params, this);
		} else {
			mAsyncHttpClient.get(mContext, url, this);
		}
	}

	/* when success, notify observer and execute next */
	@Override
	public void onSuccess(String response) {
		super.onSuccess(response);

		try {
			if (mRequest.getResponseHandler() != null) {
				mRequest.getResponseHandler().onSuccess(response);
			}
		} catch (Exception e) {
			String ext = "HttpConditionTaskExecutor:"+response;
			Tracer.debugException(e, ext);
		}
		
		executeNext(); // 继续下一个轮训
	}

	/* quit if one task failed */
	@Override
	public void onFailure(Throwable execption) {
		if (mRequest.getResponseHandler() != null) {
			mRequest.getResponseHandler().onFailure(execption);
			
			executeNext(); // 继续下一个轮训
		}
		else {
			Tracer.debugException(execption);
		}
	}
}