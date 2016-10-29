package com.example.activitytest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

@SuppressLint({ "WorldReadableFiles", "NewApi" }) 
public class GNGameHallSDK {

	private static final String LAUNCH_GAME_EXTRA_FROM_VALUE = "GNGameHall_Privilege";
	private static final String GAME_PREF_NAME = "game_hall_privilege";
	private static final String PREF_KEY_FROM = "from";
	private static final String PREF_KEY_PACKAGENAME = "packageName";
	private static final String GAME_HALL_PACKAGE_NAME = "com.example.androidttt";

	public static void launchGame(Context context, String packageName) {
		try {
			saveSource(context, packageName, LAUNCH_GAME_EXTRA_FROM_VALUE);

			PackageManager pm = context.getPackageManager();
			Intent targetIntent = pm.getLaunchIntentForPackage(packageName);
			targetIntent.putExtra(PREF_KEY_FROM, LAUNCH_GAME_EXTRA_FROM_VALUE);
			targetIntent.setPackage(null);
			targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(targetIntent);
		} catch (Exception e) {
			
		}
		
	}

	public static void saveSource(Context context, String packageName, String from) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(GAME_PREF_NAME,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		Editor editor = sharedPreferences.edit();
		editor.putString(PREF_KEY_PACKAGENAME, packageName);
		editor.putString(PREF_KEY_FROM, from);
		editor.apply();
		
		Log.e("cyTest", "--> PREF_KEY_FROM : "+sharedPreferences.getString(PREF_KEY_FROM, ""));
	}

	public static boolean isFromGNGameHall(Context context, Intent intent, String packageName) {
		if(null == intent) {
			Context gameHallContext = null;
			try {
				gameHallContext = context.createPackageContext(GAME_HALL_PACKAGE_NAME,
						Context.CONTEXT_IGNORE_SECURITY);
				SharedPreferences sharedPreferences = gameHallContext.getSharedPreferences(GAME_PREF_NAME,
						Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
				String prefPackageName = sharedPreferences.getString(PREF_KEY_PACKAGENAME, "");
				if(!prefPackageName.equals(packageName)) {
					return false;
				}
				
				String prefFrom = sharedPreferences.getString(PREF_KEY_FROM, "");
				sharedPreferences.edit().remove(GAME_PREF_NAME);
				return LAUNCH_GAME_EXTRA_FROM_VALUE.equals(prefFrom); 
				
			} catch (NameNotFoundException e) {
				return false;
			} 
        }
		
		String from = intent.getStringExtra(PREF_KEY_FROM);
		if(null == from) {
			Context gameHallContext = null;
			try {
				gameHallContext = context.createPackageContext(GAME_HALL_PACKAGE_NAME,
						Context.CONTEXT_IGNORE_SECURITY);
				SharedPreferences sharedPreferences = gameHallContext.getSharedPreferences(GAME_PREF_NAME,
						Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
				String prefPackageName = sharedPreferences.getString(PREF_KEY_PACKAGENAME, "");
				if(!prefPackageName.equals(packageName)) {
					return false;
				}
				
				String prefFrom = sharedPreferences.getString(PREF_KEY_FROM, "");
				sharedPreferences.edit().remove(GAME_PREF_NAME);
				return LAUNCH_GAME_EXTRA_FROM_VALUE.equals(prefFrom); 
				
			} catch (NameNotFoundException e) {
				return false;
			} 
		}

		return LAUNCH_GAME_EXTRA_FROM_VALUE.equals(from);
	}

}
