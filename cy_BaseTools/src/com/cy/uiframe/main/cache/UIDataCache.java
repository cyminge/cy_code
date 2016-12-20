package com.cy.uiframe.main.cache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.cy.constant.Constant;
import com.cy.global.WatchDog;
import com.cy.utils.Utils;

/**
 * UI缓存数据 账号切换时需要提供清除特定URL缓存的机制 需要提供版本升级后清楚URL缓存的机制
 * 
 * @author JLB6088
 * 
 */
@SuppressLint("NewApi") 
public enum UIDataCache {
	INSTANCE;

	private static final String UI_CACHE_DATA = "ui_cache_data";
	private SharedPreferences mSharedPref;
	private static final String CLIENT_VERSION = "client_version";
	
	private Context mContext;

	private UIDataCache() {
		mContext = WatchDog.getContext();
		init();
	}

	private void init() {
		mSharedPref = mContext.getSharedPreferences(UI_CACHE_DATA, Context.MODE_PRIVATE);
		checkVersion();
	}
	
	/**
	 * will block ui thread when first invoke ??
	 */
	private void checkVersion() {
        String curVersion = Utils.getVersionCode(mContext);
        String oldVersion = mSharedPref.getString(CLIENT_VERSION, Constant.EMPTY);

        if (!curVersion.equals(oldVersion)) {
            clear();
            mSharedPref.edit().putString(CLIENT_VERSION, curVersion);
        }
    }
	
	public void clear() {
		mSharedPref.edit().clear().apply();
	}
	
	public void remove(String key) {
		mSharedPref.edit().remove(key).apply();
    }

    public boolean contains(String key) {
        return mSharedPref.contains(key);
    }

    public void putString(String key, String value) {
    	mSharedPref.edit().putString(key, value).apply();
    }

    public String getString(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

}
