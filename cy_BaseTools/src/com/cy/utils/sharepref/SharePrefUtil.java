package com.cy.utils.sharepref;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.cy.constant.Constant;

@SuppressLint("NewApi") 
public class SharePrefUtil {

    private static final String GAME_PREF = "game_pref";
    private static SharedPreferences sPre;

    public static void init(Context context) {
        sPre = context.getSharedPreferences(GAME_PREF, Context.MODE_PRIVATE);
    }

    public static void remove(String key) {
        sPre.edit().remove(key).apply();
    }

    public static void putInt(String key, int value) {
        sPre.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        return sPre.getInt(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        sPre.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sPre.getBoolean(key, defValue);
    }

    public static void putString(String key, String value) {
        sPre.edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        return getString(key, Constant.EMPTY_STRING);
    }

    public static String getString(String key, String defValue) {
        return sPre.getString(key, defValue);
    }

    public static void putLong(String key, long value) {
        sPre.edit().putLong(key, value).apply();
    }

    public static long getLong(String key, long defValue) {
        return sPre.getLong(key, defValue);
    }

    public static void putHashTime(String key, long value) {
//        long randomTime = value + Utils.getRandomTimeMs();
//        putLong(key, randomTime);
    }

    public static void putHashTime(String key, long value, long maxMillis) {
//        long randomTime = value + Utils.getRandomTimeMs(maxMillis);
//        putLong(key, randomTime);
    }

    public static void putFloat(String key, float value) {
        sPre.edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key, float defValue) {
        return sPre.getFloat(key, defValue);
    }

    public static void saveVersion(String prefKey, String jsonData) {
//        try {
//            JSONObject json = new JSONObject(jsonData);
//            String version = json.getString(JsonConstant.VERSION);
//            PreferenceManager.putString(prefKey, version);
//        } catch (Exception e) {
//        }
    }

    public static String getVersion(String prefKey) {
        return getString(prefKey, Constant.DEFAULT_VERSION);
    }
    
    public static void clearPreference(String key) {
    	sPre.edit().remove(key).apply();
    }
}
