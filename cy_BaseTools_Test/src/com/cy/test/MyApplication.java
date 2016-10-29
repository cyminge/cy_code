package com.cy.test;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;
import android.os.Debug;
import android.os.Build.VERSION;
import android.os.StrictMode;

@SuppressLint("NewApi")
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		startStrictMode();
		
		invokeStrictMode();

	}
	
	/**
	 * 生成TraceView文件 ，QEMU是什么？
	 */
	public void showTracing(){
		Debug.startMethodTracing("/sdcard/awesometrace.trace");
		// do something
		Debug.stopMethodTracing();
	}

	private void startStrictMode() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectCustomSlowCalls() // API等级11，使用StrictMode.noteSlowCode 用来检测应用种执行缓慢的代码或潜在缓慢的代码
				.detectDiskReads()
				.detectDiskWrites()
				.detectNetwork()
				.penaltyLog()
				.penaltyFlashScreen() // API等级11
				.build());
		
		// 其实和性能无关，但如果使用StrictMode，最好也定义VM策略
		try {
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects()
					.setClassInstanceLimit(Class.forName("com.apress.proandroid.SomeClass"),100)
					.penaltyLog()
					.build());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 开发和测试阶段时使用.
	 * 
	 * 检查内存泄露
	 */
	private void invokeStrictMode() {
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		builder.detectLeakedSqlLiteObjects();
		if (VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			builder.detectActivityLeaks().detectLeakedClosableObjects(); // builder.detectAll();
		}

		builder.penaltyLog(); // penaltyDeath();

		StrictMode.VmPolicy vmp = builder.build();
		StrictMode.setVmPolicy(vmp);
	}

	/**
	 * 当发生内存不足时会调用
	 */
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	/**
	 * 适用于模拟器环境，真机不会调用
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	/**
	 * 后台运行时，如果发生低内存会调用
	 */
	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
	}

}
