package com.example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;

public class HookHelper {
    public static void init(Context context) {
        try {
            Log.d("ProxyInstrumentation", "HookHelper::init start.");
            Class cActivityThread = Class.forName("android.app.ActivityThread");
//            Method mCurrentActivityThread = cActivityThread.getDeclaredMethod("currentActivityThread", new Class[0]);
            Method mCurrentActivityThread = cActivityThread.getDeclaredMethod("currentActivityThread", (Class[]) null); // 获取当前ActivityThread对象引用
            mCurrentActivityThread.setAccessible(true);
//            Object oCurrentActivityThread = mCurrentActivityThread.invoke(null, new Object[0]);
            Object oCurrentActivityThread = mCurrentActivityThread.invoke(null, (Object[]) null);
            Field fInstrumentation = cActivityThread.getDeclaredField("mInstrumentation");
            fInstrumentation.setAccessible(true);
            Object oInstrumentation = fInstrumentation.get(oCurrentActivityThread);
            Instrumentation proxyInstrumentation = new ProxyInstrumentation(context,
                    (Instrumentation) oInstrumentation);
            fInstrumentation.set(oCurrentActivityThread, proxyInstrumentation);
            Log.d("ProxyInstrumentation", "HookHelper::init end.");
        } catch (Exception e) {
            Log.e("ProxyInstrumentation", "HookHelper::init failed - " + e.getMessage());
            e.printStackTrace();
        }
        
        
//        ProxyInstrumentation ins = new ProxyInstrumentation();
//
//        Class cls = Class.forName("android.app.ActivityThread"); // ActivityThread被隐藏了，所以通过这种方式获得class对象
//        Method mthd = cls.getDeclaredMethod("currentActivityThread", (Class[]) null); // 获取当前ActivityThread对象引用
//
//        Object currentAT = mthd.invoke(null, (Object[]) null);
//
//        Field mInstrumentation = currentAT.getClass().getDeclaredField("mInstrumentation");
//
//        mInstrumentation.setAccessible(true);
//
//        mInstrumentation.set(currentAT, ins); // 修改ActivityThread.mInstrumentation值


    }
}
