package com.cy.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 将ITest接口通过此service暴露给客户端程序
 * @author zhanmin
 *
 */
public class TestService extends Service {
    public static final String BIND = "com.cy.AidlTest.TestService.BIND"; 
    
    private final ITest.Stub mBinder = new IPersonImpl();
  
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
    	
    	Log.i("cyTest", "=== 服务端绑定了要暴露的接口 ！！ ====");
        return mBinder;    //暴露给客户端的对象接口mBinder而不是一般Service中的null
    }

}
