package com.cy.test;

import android.content.Context;
import android.util.Log;

import com.cy.network.strategy.http.BaseAdapterSession;
import com.cy.network.strategy.http.SimpleHttpSchedualer;
import com.cy.tracer.Tracer;
import com.loopj.android.http.RequestParams;

public class MobileActivateSession extends BaseAdapterSession {
	private static final long serialVersionUID = -5817225288737949193L;

	private static final String TAG = "MobileActivateSession";
	
	private String loginname;
	private String password;
	private final static String LOGINNAME = "loginname";
	private final static String PASSWORD = "password";

	private Context mContext;
	private int urlIndex = 0;
	private static final String[] candidateUrls;

	static {
		candidateUrls = new String[] {
//				HostProperties.getHost(HostProperties.SOFTPHONE_REGISTER_DOMAIN)
		};
	}
	
	public MobileActivateSession(Context context, Class<?> entity, String loginname, String password) {
		super(entity.getClass());
		mContext = context;
		this.loginname = loginname;
		this.password = password;
	}
	
	@Override
	public void onFailure(Throwable arg0) {
		Tracer.e(TAG, "login error urlIndex = "+urlIndex);
		if (urlIndex == 0) {
			urlIndex++;
			SimpleHttpSchedualer.ansycSchedual(mContext, this, null); // post
		} else {
			super.onFailure(arg0);
		}
	}
	
	@Override
	public String getUrl() {
		if (urlIndex < 0 || urlIndex >= candidateUrls.length) {
			Log.e("UpStatisticsSession", "Shuold not run here, url index is invaldi");
//			return HostProperties.getHost(HostProperties.SOFTPHONE_REGISTER);
			return null;
		}
		if(urlIndex == 0) {
//			return HostProperties.getHost(HostProperties.SOFTPHONE_REGISTER);
			return null;
		}
		return candidateUrls[0];
	}

	@Override
	public RequestParams getRequestParams() {

		RequestParams params = new RequestParams();
		putParamIfNotEmpty(params, LOGINNAME, loginname);
		putParamIfNotEmpty(params, PASSWORD, password);
		return params;
	}
	
}
