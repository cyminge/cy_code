package com.cy.aidlclienttest;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.cy.aidlclienttwotest.R;
import com.cy.aidltest.IPersonCallback;
import com.cy.aidltest.ITest;

/**
 * aidl客户端启动
 * 
 * @author zhanmin
 * 
 */
@SuppressLint("NewApi")
public class MainActivity extends Activity {
    TextView tv;
    boolean connected = false;
    MyHandler mHandler;

    /**
     * 此callback会被服务端调用
     */
    private IPersonCallback.Stub callback = new IPersonCallback.Stub() {

        @Override
        public String getCYmingeInfo() throws RemoteException {
            return "······詹敏+30+已婚+无房无车无钱······";
        }
    };

    private ITest objTest = null;

    /**
     * 连接服务
     */
    private ServiceConnection serviceConn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            objTest = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            objTest = ITest.Stub.asInterface(service);
            connected = true;
            Log.i("cyTest", "client  MainActivity  onServiceConnected");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        // 1.在Activity的onCreate中加入绑定服务的代码
//         Intent intent = new Intent();
//         intent.setAction("com.cy.aidltest.TestService.BIND");
//         intent.setPackage(getPackageName());//这里你需要设置你应用的包名   ??????
//         bindService(intent, serviceConn, Context.BIND_AUTO_CREATE);

         Intent mIntent = new Intent();
         mIntent.setAction("com.cy.aidltest.TestService.BIND");
         Intent eintent = new Intent(getExplicitIntent(this, mIntent));
         bindService(eintent, serviceConn, Context.BIND_AUTO_CREATE);

        // 2.调用,连接需要一定时间
        tv = (TextView) findViewById(R.id.name);
        tv.setText("------------");
        
        mHandler = new MyHandler();
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case 1:
                new Thread(new Runnable() {
                    public void run() {
                        int waittime = 0;
                        if (connected == false) {
                            Log.e("cyTest", "还没连接上");
                            waittime++;
                            mHandler.postDelayed(this, 200); // 如果没有连接就继续连接
                        }
                        String getName = getName();
                        obtainMessage(2, getName).sendToTarget();;
                        try {
                            objTest.testCallback(callback); // 向服务端回传一个callback,让服务端可以获取客户端信息
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
                break;
            case 2 :
                tv.setText(msg.obj.toString() + "---->client");
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConn); // 取消绑定
        super.onDestroy();
    }

    /**
     * 得到服务器传来的信息
     * 
     * @return
     */
    private String getName() {
        String strName = null;
        if (objTest != null) {
            try {
                strName = objTest.display();
                String str2[] = new String[10];
                str2[0] = "test";
                objTest.setAge(27, str2);
                Log.i("cyTest", "得到服务器传来的信息 -- 年纪str2[0] = " + str2[0]);
            } catch (RemoteException e) {
            }
        }
        return strName;
    }
    
    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
//        1. Make sure only one match was found
//        if (resolveInfo == null || resolveInfo.size() != 1) {
//            return null;
//        }
//        2. if has two or more also do next
        if(null == resolveInfo) {
            return null;
        }
        
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}
