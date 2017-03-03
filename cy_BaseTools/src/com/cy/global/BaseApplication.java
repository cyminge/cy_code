package com.cy.global;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;

import com.cy.constant.Constant;
import com.cy.optimizationtools.fluency.UiThreadBlockWatcher;

public class BaseApplication extends Application {

    private static BaseApplication mBaseApplication = null;
    private static Context mBaseAppContext = null;

    public static Context getAppContext() {
        return mBaseAppContext;
    }

    public static BaseApplication getBaseApplication() {
        return mBaseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UiThreadBlockWatcher.install(50);
        mBaseApplication = this;
        mBaseAppContext = mBaseApplication.getApplicationContext();
        if(isMainProcess()) {
            WatchDog.init(this);
        }
    }
    
    private boolean isMainProcess() {
        String processName = Constant.EMPTY;
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> allProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo process : allProcesses) {
            if (process.pid == pid) {
                processName = process.processName;
                break;
            }
        }
        return getPackageName().equals(processName);
    }

}
