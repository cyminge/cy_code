package com.cy.jackson;

import com.cy.jackson.bean.EPGRecordBean;
import com.cy.jackson.util.HtmlParser;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class JacksonMainActivity extends Activity {

	private JacksonHandler mJacksonHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		mJacksonHandler = new JacksonHandler();
		mJacksonHandler.obtainMessage(94).sendToTarget();
			
		
	}
	
	class JacksonHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 94 :
				Thread thread = new Thread(mRunnable);
				thread.start();
			}
		}
	}
	
	Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			main();
		}
	};
	
	/**
	 * 解析json数据
	 */
	public static void main() {
		String ss = HtmlParser.getHtmlContent("http://192.168.0.101//service/api/?do=liveprogram&time=1372402354&code=992883bb0cc2d630e359ab1045de56dc", "utf-8");
		if(ss == null || ss.length() == 0){
			return ;
		}
//		ObjectMapper objectMapper = new ObjectMapper(); // 这个是jackson包的类
		Gson gson = new Gson();
		Log.d("cyTest", "ss = "+ss);
		try {
//			ModelPageBean mpb = objectMapper.readValue(ss, ModelPageBean.class);
			EPGRecordBean epgRecord = gson.fromJson(ss, EPGRecordBean.class);
			Log.d("cyTest", "epgRecord.singleEPGBeanList = "+epgRecord.page);
			Log.d("cyTest", "epgRecord.singleEPGBeanList = "+epgRecord.record.get(0).create_time);
			
		} catch (JsonParseException e) {
			Log.d("cyTest", "===============error = "+e);
			e.printStackTrace();
		}
	}
	
}
