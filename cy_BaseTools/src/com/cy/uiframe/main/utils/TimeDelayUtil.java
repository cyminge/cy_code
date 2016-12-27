package com.cy.uiframe.main.utils;

import com.cy.global.WatchDog;

public class TimeDelayUtil {
	public interface Callback {
        public void onTimeOut();
    }
	
	/**
	 * 延时展示数据
	 * @param startMs
	 * @param delayMs
	 * @param callback
	 */
    public static void start(long startMs, long delayMs, final Callback callback) {
        long deltaTime = Math.abs(System.currentTimeMillis() - startMs);
        long remainTime = delayMs - deltaTime;
        remainTime = Math.max(remainTime, 0);
        remainTime = Math.min(remainTime, delayMs);
        WatchDog.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTimeOut();
            }
        }, remainTime);
    }
}
