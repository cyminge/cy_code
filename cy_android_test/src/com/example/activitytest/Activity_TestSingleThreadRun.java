package com.example.activitytest;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.cy.R;
import com.example.global.BaseApplication;

public class Activity_TestSingleThreadRun extends Activity {

	private static final String HandleThreadName = "GroupsHodlerHandlerThread";
	private HandlerThread mHandlerThread;
	private volatile ThreadHandler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_single_thread_run);

		mHandlerThread = new HandlerThread(HandleThreadName, Process.THREAD_PRIORITY_BACKGROUND);
		mHandlerThread.start();
		mHandler = new ThreadHandler(mHandlerThread.getLooper());
//		mHandler.sendEmptyMessageDelayed(9527, 1);
		mHandler.sendEmptyMessageDelayed(9528, 0);
		
//		mHandler = new ThreadHandler();
		
//		Thread testThread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						test();
					}
				});
//			}
//		});
//		testThread.start();
		
		
//		getInfoString();
//		test1();
//		test2();
	}

	@Override
	protected void onStart() {
		super.onStart();
		test1();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		test2();
	}
	
	private void test1() {
		Log.e("cyTest", "--> test1, 执行时间："+System.currentTimeMillis());
	}
	
	private void test2() {
		Log.e("cyTest", "--> test2, 执行时间："+System.currentTimeMillis());
	}
	
	private void test3() {
		Log.e("cyTest", "--> test3, 执行时间："+System.currentTimeMillis());
	}

	private final class ThreadHandler extends Handler {
		
		public ThreadHandler() {
			
		}

		public ThreadHandler(Looper looper) {
			super(looper);
			getInfoString();
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 9527:
//				getInfoString();
				break;
			case 9528:
				xx();
				break;
			default:
				break;
			}
		}
	}
	
	private void xx() {
		
		long timeXX = System.currentTimeMillis();
		long time = 0;
		while(flag) {
			if(System.currentTimeMillis()-timeXX>10000) {
				break;
			}
			if(System.currentTimeMillis()-time > 1000) {
				test3();
				time = System.currentTimeMillis();
			}
		}
	}
	
	private boolean flag = true;
	
	private void test() {
		flag = false;
		Log.e("cyTest", "--> 来捣乱的方法, 执行时间："+System.currentTimeMillis());
		
	}

	private String getInfoString() {
		StringBuilder sb = new StringBuilder();
		ArrayList<PackageInfo> res = getPackageInfo();
		long wholeTime = System.currentTimeMillis();
		for (PackageInfo p : res) {
			long startTime = System.currentTimeMillis();
			String md5 = null;
			try {
				File file = new File(p.applicationInfo.publicSourceDir);
				md5 = getFileMd5(file);
			} catch (Exception e) {
			}
			
			Log.e("cyTest", "包名"+p.packageName+"所用时间："+(System.currentTimeMillis()-startTime) +"--md5："+md5);
		}
		
		Log.e("cyTest", "所用的总时间："+(System.currentTimeMillis()-wholeTime));
		
		return sb.toString();
	}

	public static final String AMIGO_PLAY_PACKAGE_NAME = "com.gionee.gsp";

	private ArrayList<PackageInfo> getPackageInfo() {
		ArrayList<PackageInfo> packageInfos = getPackageInfos(PackageManager.GET_UNINSTALLED_PACKAGES);
		PackageInfo gameHallInfo = getPackageInfoByName(BaseApplication.getInstance().getPackageName());
		if (null != gameHallInfo) {
			packageInfos.add(gameHallInfo);
		}
		PackageInfo gspInfo = getPackageInfoByName(AMIGO_PLAY_PACKAGE_NAME);
		if (gspInfo != null) {
			packageInfos.add(gspInfo);
		}
		return packageInfos;
	}

	public PackageInfo getPackageInfoByName(String packageName) {
		return getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
	}

	public PackageInfo getPackageInfo(String packageName, int flags) {
		PackageManager pm = BaseApplication.getInstance().getPackageManager();
		try {
			return pm.getPackageInfo(packageName, flags);
		} catch (Exception e) {
		}
		return null;
	}

	public ArrayList<PackageInfo> getPackageInfos(int flags) {
		PackageManager packageManger = BaseApplication.getInstance().getPackageManager();
		ArrayList<PackageInfo> res = new ArrayList<PackageInfo>();
		try {
			List<PackageInfo> packs = packageManger.getInstalledPackages(flags);

			for (PackageInfo p : packs) {
				if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
					res.add(p);
				}
			}
		} catch (Exception e) {
		}
		return res;
	}

	public String getFileMd5(File file) {
		byte[] digest = null;
		FileInputStream in = null;
		if (file == null) {
			return null;
		}
		try {
			MessageDigest digester = MessageDigest.getInstance("MD5");
			byte[] bytes = new byte[8192];
			in = new FileInputStream(file);
			int byteCount;
			while ((byteCount = in.read(bytes)) > 0) {
				digester.update(bytes, 0, byteCount);
			}
			digest = digester.digest();
		} catch (Exception cause) {
		} finally {
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (Exception e) {
				}

			}
		}
		return (digest == null) ? null : byteArrayToString(digest);
	}

	private String byteArrayToString(byte[] bytes) {
		StringBuilder ret = new StringBuilder(bytes.length << 1);
		for (byte aByte : bytes) {
			ret.append(Character.forDigit((aByte >> 4) & 0xf, 16));
			ret.append(Character.forDigit(aByte & 0xf, 16));
		}
		return ret.toString();
	}

}
