package com.cy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.cy.utils.encrypt.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SharedPrefUtil {
    public static void removePreferenceKey(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static boolean loadPreferenceBoolean(Context context, String key) {
        boolean value = false;
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        if (null != preferences) {
            value = preferences.getBoolean(key, false);
        }
        return value;
    }

    public static void persistentPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static int loadPreferenceInt(Context context, String sharedName, String key) {
        int value = 0;
        SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        if (null != preferences) {
            value = preferences.getInt(key, -1);
        }
        return value;
    }

    public static void persistentPreferenceInt(Context context, String sharedName, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int loadPreferenceInt(Context context, String key) {
        int value = 0;
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        if (null != preferences) {
            value = preferences.getInt(key, -1);
        }
        return value;
    }

    public static void persistentPreferenceInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static long loadPreferenceLong(Context context, String key) {
        long value = 0L;
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        if (null != preferences) {
            value = preferences.getLong(key, 0L);
        }
        return value;
    }

    public static void persistentPreferenceLong(Context context, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long loadPreferenceLong(Context context, String sharedName, String key) {
        long value = 0L;
        SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        if (null != preferences) {
            value = preferences.getLong(key, 0L);
        }
        return value;
    }

    public static void persistentPreferenceLong(Context context, String sharedName, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static String loadPreferenceString(Context context, String sharedName, String key) {
        String value = null;
        SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        if (null != preferences) {
            value = preferences.getString(key, null);
        }
        return value;
    }

    public static void persistentPreferenceString(Context context, String sharedName, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }



    public static void persistentPreferenceObject(Context context, String prefName, String key, Object obj) {
        SharedPreferences preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            String map_Base64 = new String(Base64.encodeBase64(baos.toByteArray()));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, map_Base64);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object loadPreferenceObj(Context context, String prefName, String key) {
        Object obj = null;
        SharedPreferences preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        String mapBase64 = preferences.getString(key, "");
        byte[] base64 = Base64.decodeBase64(mapBase64.getBytes());
        if (base64.length <= 0) {
            return null;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            ObjectInputStream bis = new ObjectInputStream(bais);
            obj = bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

}
