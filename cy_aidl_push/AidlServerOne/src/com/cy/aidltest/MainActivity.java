package com.cy.aidltest;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cy.aidlserveronetest.R;

/**
 * aidl服务端启动
 * @author zhanmin
 *
 */
public class MainActivity extends Activity {
	TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
//		Intent intent = new Intent();
//		intent.setClass(this, TestService.class);
//		intent.putExtra("cy", "can get this ??");
//		startService(intent);
		
//		mHandler.sendEmptyMessageDelayed(9257, 2000);
		
		persistentSharedPre();
	}
	
	Handler mHandler = new Handler() {
	  public void handleMessage(android.os.Message msg) {
	  };  
	};
	
	 public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
	        // Retrieve all services that can match the given intent
	        PackageManager pm = context.getPackageManager();
	        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
//	        1. Make sure only one match was found
//	        if (resolveInfo == null || resolveInfo.size() != 1) {
//	            return null;
//	        }
//	        2. if has two or more also do next
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
	 
	 boolean connected = false;
	 
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
	
	public void killApp(View view) {
//	    try {
//            android.os.Process.killProcess(android.os.Process.myPid());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
	    
//	    Intent mIntent = new Intent();
//        mIntent.setAction("com.cy.aidltest.TestService.BIND");
//        Intent eintent = new Intent(getExplicitIntent(this, mIntent));
//        bindService(eintent, serviceConn, Context.BIND_AUTO_CREATE);
	}
	
	private void persistentSharedPre() {
	    SharedPreferences preferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("test", "另一个应用能拿到么");
        editor.commit();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}