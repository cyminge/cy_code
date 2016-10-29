package com.cy.global;

import android.app.Application;
import android.content.Context;

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

        mBaseApplication = this;
        mBaseAppContext = mBaseApplication.getApplicationContext();
        InitialWatchDog.init(this);
    }

    public static BaseApplication getInstance() {
        return mBaseApplication;
    }

}
