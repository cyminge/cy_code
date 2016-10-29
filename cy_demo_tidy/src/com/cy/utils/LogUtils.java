package com.cy.utils;

import com.cy.config.TVDVBConfig;

import android.content.Context;
import android.util.Log;

/**
 * log 输出控制类
 * 
 * @author zhanmin 2013.03.12
 */
public class LogUtils {
	
	private static TVDVBConfig mTVDVBConfig;
	
	public LogUtils(){
	}
	
	/**
	 * 初始化日志类
	 * @param context
	 */
	public static void getInstance(Context context){
		if(mTVDVBConfig==null){
//			mTVDVBConfig = TVDVBConfig.getInstance(context.getAssets());
			mTVDVBConfig = TVDVBConfig.getInstance(context.getApplicationContext().getResources().getAssets());
		}
	}
	
	public static void verbose(String tag, String msg){
		if (mTVDVBConfig.isVerbose()) {
			Log.v(tag, msg);
		}
	}
	
	public static void debug(String tag, String msg) {
		if (mTVDVBConfig.isDebug()) {
			Log.d(tag, msg);
		}
	}

	public static void info(String tag, String msg) {
		if (mTVDVBConfig.isDebug()) {
			Log.i(tag, msg);
		}
	}

	public static void warn(String tag, String msg) {
		if (mTVDVBConfig.isDebug()) {
			Log.w(tag, msg);
		}
	}

	public static void error(String tag, String msg) {
		if (mTVDVBConfig.isDebug()) {
			Log.e(tag, msg);
		}
	}
}
