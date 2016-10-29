package com.cy.dealwithexception;

import com.cy.utils.LogUtils;

import android.app.Application;  

public class CrashApplication extends Application {  
    @Override  
    public void onCreate() {  
        super.onCreate();  
        
        LogUtils.getInstance(this);
        
//        CrashHandler crashHandler = CrashHandler.getInstance();  
//        crashHandler.init(getApplicationContext());  
    }  
}  
