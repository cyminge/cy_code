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
public class TestServiceTwo extends Service {
  
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
