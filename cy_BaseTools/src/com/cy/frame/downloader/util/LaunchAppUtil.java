package com.cy.frame.downloader.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cy.constant.Constant;

public class LaunchAppUtil {
	private static long sLastVisitTime;

    private static boolean isTimeTooShort() {
        long currentTime = System.currentTimeMillis();
        long interval = Math.abs(currentTime - sLastVisitTime);
        boolean tooShort = interval < Constant.MILLIS_700;
        if (!tooShort) {
            sLastVisitTime = currentTime;
        }
        return tooShort;
    }

    public static void startActivity(Context context, Intent intent) {
        if (context == null || isTimeTooShort()) {
            return;
        }

        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        if (activity == null || isTimeTooShort()) {
            return;
        }

        activity.startActivityForResult(intent, requestCode);
    }
}
