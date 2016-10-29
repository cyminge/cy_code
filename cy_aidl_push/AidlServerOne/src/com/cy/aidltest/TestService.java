package com.cy.aidltest;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
    
    public static Context mContext;
    
    public static int i;
    
    public static String mStr;
    
    public static String invoke() {
       return mStr;
    }
    
    @SuppressLint("HandlerLeak") 
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 9527 :
                break;
            }
        };
    };
  
    public void onStart(Intent intent, int startId) {
        if(null != intent) {
            Log.e("cyTest", "test String : "+intent.getStringExtra("cy"));
            mStr = "haha";
        }
        
    };
    
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        mContext = this;
        i = 10;
        Log.e("cyTest", "mContext:"+mContext);
        
//        mHandler.postDelayed(new Runnable() {
//            
//            @Override
//            public void run() {
////                while(true) {
////                    //
////                }
//            }
//        }, 1000000);
    }

    @Override
    public IBinder onBind(Intent intent) {
    	
    	Log.i("cyTest", "=== 服务端绑定了要暴露的接口 ！！ ====");
        return mBinder;    //暴露给客户端的对象接口mBinder而不是一般Service中的null
    }

}
