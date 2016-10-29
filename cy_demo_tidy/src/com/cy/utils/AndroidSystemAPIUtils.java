package com.cy.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AndroidSystemAPIUtils {

	/**
	 * 判断是否存在包名
	 * @param context
	 * @param packageName
	 * @return
	 */
	public boolean checkBrowser(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
//			ApplicationInfo info = 
			context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
		} catch (NameNotFoundException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否存在包名
	 * @param context
	 * @return
	 */
	public boolean checkPackage(Context context, String packageName){
		try {
			PackageManager pm = context.getPackageManager();
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
		} catch (Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * 检查是否存在Activity
	 * @param context
	 * @return
	 */
	public boolean checkActivity(Context context){
		try {
			Class.forName("com.cy.MainActivity");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}
	
}
