package com.example.activitytest;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.cy.R;
import com.example.toast.ToastUtil;

public class Activity_B extends Activity  implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_b);
		Log.e("cyTest", "Activity_B.onCreate");
		
//		setDataConnectionState(this, false);
//		mHandler.sendEmptyMessageDelayed(9527, 10000);
//		mHandler.sendEmptyMessageDelayed(9528, 2000); 
		
		Log.e("cyTest", "===============================================");
        readVoldFile();
        for(String str : sVold) {
            Log.e("cyTest", "str=="+str);
        }
        
        File file = new File("/storage/sdcard0/testSdcard/cy/image");
        Log.e("cyTest", "file=="+file);
        if (!file.exists()) {
            boolean flag = file.mkdirs();
            Log.e("cyTest", "flag=="+flag);
        }
	}
	
	private static ArrayList<String> sVold = new ArrayList<String>();
    private static String DefPath = "/mnt/sdcard";
    
    private static void readVoldFile() {
        try {
            sVold.add(Environment.getExternalStorageDirectory().getPath());
            if (!sVold.contains(DefPath))
                sVold.add(DefPath);
        } catch (Exception e) {
        }
        
        try {
            File mountFile = new File("/proc/mounts");
            if(mountFile.exists()){
                Scanner scanner = new Scanner(mountFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("/dev/block/vold/")) {
                        String[] lineElements = line.split(" ");
                        String element = lineElements[1];

                        // don't add the default mount path
                        // it's already in the list.
                        if (!sVold.contains(element))
                            sVold.add(element);
                    }
                }
            }
        } catch (Exception e) {
        }

//        try {
//            Scanner scanner = new Scanner(new File("/system/etc/vold.fstab"));
//            while (scanner.hasNext()) {
//                String line = scanner.nextLine();
//                if (line.startsWith("dev_mount")) {
//                    String[] lineElements = line.split(" ");
//                    String element = lineElements[2];
//
//                    if (element.contains(":"))
//                        element = element.substring(0, element.indexOf(":"));
//
//                    if (element.contains("usb"))
//                        continue;
//
//                    // don't add the default vold path
//                    // it's already in the list.
//                    if (!sVold.contains(element))
//                        sVold.add(element);
//                }
//            }
//        } catch (Exception e) {
//        }
    }
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 9527 :
				setDataConnectionState(Activity_B.this, true);
				break;
			case 9528 :
				ToastUtil.showToast(Activity_B.this, "测试自定义Toast", Toast.LENGTH_SHORT);
				break;
			}
		};
	};
	
	// =========================================================================
	// 重启网络
	
	private static int tryLoginTimes = 0;  // 尝试登录的次数
	
	private boolean isResetDataConnection = false; // 是否重启数据连接
	
	public static void setDataConnectionState(Context cxt, boolean state) {
		Log.e("cyTest", "重启数据连接： state="+state);
		ConnectivityManager connectivityManager = null;
		Class<?> connectivityManagerClz = null;
		try {
			connectivityManager = (ConnectivityManager) cxt.getSystemService("connectivity");
			connectivityManagerClz = connectivityManager.getClass();
			Method method = connectivityManagerClz.getMethod("setMobileDataEnabled", new Class[] { boolean.class });
			method.invoke(connectivityManager, state);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.btn){
			Intent intent = new Intent();
			intent.setClass(this, Activity_C.class);
			startActivity(intent);
		}
	}
	
	   @Override
	    public void onWindowFocusChanged(boolean hasFocus) {
	        super.onWindowFocusChanged(hasFocus);
	        Log.e("cyTest", "Activity_B.onWindowFocusChanged");
	    }
	    
	    @Override
	    public void onAttachedToWindow() {
	        super.onAttachedToWindow();
	        Log.e("cyTest", "Activity_B.onAttachedToWindow");
	    }
	    
	    @Override
	    protected void onNewIntent(Intent intent) {
	        Log.e("cyTest", "Activity_B.onNewIntent");
	        super.onNewIntent(intent);
	    }
	    
	    @Override
	    protected void onRestart() {
	        Log.e("cyTest", "Activity_B.onRestart");
	        super.onRestart();
	    }
	    
	    @Override
	    protected void onStart() {
	        Log.e("cyTest", "Activity_B.onStart");
	        super.onStart();
	    }
	    
	    @Override
	    protected void onResume() {
	        super.onResume();
	        Log.e("cyTest", "Activity_B.onResume");
	    }
	    
	    int counts;
	    
	    @Override
	    protected void onPause() {
	        Log.e("cyTest", "Activity_B.onPause");
	        super.onPause();
	        
//	        for(int i=0; i<100000000;i++) {
//	            counts ++;
//	        }
//	        Log.e("cyTest", "Activity_B.onPause.counts-->"+counts);
	    }

	    @Override
	    protected void onStop() {
	        Log.e("cyTest", "Activity_B.onStop");
	        super.onStop();
	    }
	    
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        Log.e("cyTest", "Activity_B.onSaveInstanceState");
	        super.onSaveInstanceState(outState);
	        outState.putInt("aa", 11);
	    }
	    
	    @Override
	    public void onDetachedFromWindow() {
	        super.onDetachedFromWindow();
	        Log.e("cyTest", "Activity_B.onDetachedFromWindow");
	    }
	    
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        Log.e("cyTest", "Activity_B.onDestroy");
	    }

}
