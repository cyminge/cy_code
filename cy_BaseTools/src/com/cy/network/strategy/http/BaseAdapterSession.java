package com.cy.network.strategy.http;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cy.tracer.Tracer;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import de.greenrobot.event.EventBus;

public abstract class BaseAdapterSession extends BaseSession {
	public static final String NWT_ERROR = "-1000";
	public static final String FORMAT_ERROR = "-1001";
	protected final Class<?> mRespClazz;
	private EventBus specifiedEventBus = null;

	public void setSpecifiedEventBus(EventBus specifiedEventBus) {
		this.specifiedEventBus = specifiedEventBus;
	}

	protected BaseAdapterSession(Class<?> clazz) {
		mRespClazz = clazz;
	}

	protected void putParamIfNotEmpty(RequestParams params, String keys, String value) {
		if (params != null) {
			if (value != null && !value.trim().equals("") && keys != null && !keys.trim().equals("")) {
				params.put(keys, value);
			}
		}
	}

	protected void postEvent(Object event) {
		if (specifiedEventBus != null) {
			specifiedEventBus.post(event);
		} else {
			EventBus.getDefault().post(event);
		}
	}

	public void postNetErrorResult(Class<?> event) {
		try {
			Object object = event.newInstance();
			Method setError = event.getMethod("setError", String.class);

			if (setError != null) {
				setError.invoke(object, NWT_ERROR);
			} else {
				try {
					Field field = event.getField("error");
					if (field != null) {
						field.set(object, NWT_ERROR);
					}
				} catch (NoSuchFieldException e) {
					Tracer.debugException(e);
				}
			}
			postEvent(object);
		} catch (InstantiationException e) {
			Tracer.debugException(e);
		} catch (IllegalAccessException e) {
			Tracer.debugException(e);
		} catch (NoSuchMethodException e) {
			Tracer.debugException(e);
		} catch (IllegalArgumentException e) {
			Tracer.debugException(e);
		} catch (InvocationTargetException e) {
			Tracer.debugException(e);
		}
	}

	@Override
	public boolean getSyncParam() {
		return false;
	}

	@Override
	public void onFailure(Throwable arg0) {
		postNetErrorResult(mRespClazz);
	}
	
	@Override
	public String onSuccessBefore(String responce) {
		return responce;
	}
	
	@Override
	public Object onSuccessDoMore(String responce, Object respObj) {
		return respObj;
	}
	
	@Override
	public void onSuccessAfter(Object respObj) {
	}

	@Override
	public void onSuccess(String responce) {
		try {
			responce = onSuccessBefore(responce);
			Tracer.i("wode", "responce:"+responce);
			Gson gson = new Gson();
			Object obj = gson.fromJson(responce, mRespClazz);

			obj = onSuccessDoMore(responce, obj);
			if (obj != null) {
				postEvent(obj);
			} else {
				postNetErrorResult(mRespClazz);
				Tracer.d("BaseAdapterSession", "on success but parse json failed " + responce);
			}
			
			onSuccessAfter(obj);
			
		} catch (Exception e) {
			postNetErrorResult(mRespClazz);
			String ext = "BaseAdapterSession:"+getUrl()+"|"+getRequestParams()+"|"+responce;
			Tracer.debugException(e, ext);
		}
	}
	
	@Override
	public RequestParams getRequestParams() {
		RequestParams params = new RequestParams();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date());
		params.put("timestamp", time);
		params.put("format", "json");
		params.put("app_key", "test");
		params.put("v", "0.1");
//		params.put("loginname", "young");
//		params.put("password", "AaBbCcDdEeFfGgHhIiJjKkLlMmNn1234");
		return params;
	}
}
