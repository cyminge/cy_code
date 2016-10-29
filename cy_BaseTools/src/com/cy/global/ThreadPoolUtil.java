package com.cy.global;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import android.os.Message;

public class ThreadPoolUtil {
    private static volatile ExecutorService sThreadPool;

    public static ExecutorService getThreadPool() {
        if (sThreadPool == null) {
            sThreadPool = Executors.newCachedThreadPool();
        }
        return sThreadPool;
    }

    public static void post(Runnable task) {
        try {
            getThreadPool().execute(task);
        } catch (RejectedExecutionException e) {
        }
    }

    public static void postDelayed(final Runnable task, long delayMillis) {
        Message msg = Message.obtain();
        msg.obj = task;
        InitialWatchDog.mMainHandler.sendMessageDelayed(msg, delayMillis);
    }

    public static void removeCallbacks(Runnable task) {
        InitialWatchDog.mMainHandler.removeCallbacksAndMessages(task);
    }

}
