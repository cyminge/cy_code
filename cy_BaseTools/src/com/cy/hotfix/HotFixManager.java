package com.cy.hotfix;

import android.content.Context;

public enum HotFixManager {
	INSTANCE;

	private HotFixManager() {}

	private Context mContext;
	public boolean sIsInitializeDone = false;

	public static boolean init(Context context) {
		context = context.getApplicationContext();
		return INSTANCE.initialize(context);
	}

	public static void deInit(Context context) {
		context = context.getApplicationContext();
		INSTANCE.Deinitialize(context);
	}

	public static Context getContext() {
		return INSTANCE.mContext;
	}

	/**
	 * 初始化函数，app启动时调用
	 * 
	 * @param context
	 * @return
	 */
	private boolean initialize(Context context) {
		if (sIsInitializeDone) {
			return true;
		}
		context = context.getApplicationContext();
		mContext = context;
		
		//TODO 
		checkDexUpdate();

		sIsInitializeDone = true;
		return true;
	}

	private void Deinitialize(Context context) {
		if (!sIsInitializeDone) {
			return;
		}

		sIsInitializeDone = false;
	}
	
	/**
	 * 检查是否有dex更新
	 */
	private void checkDexUpdate() {
		
	}

}
