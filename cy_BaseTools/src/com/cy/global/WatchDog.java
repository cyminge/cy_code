package com.cy.global;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.cy.frame.downloader.downloadmanager.DownloadService;
import com.cy.imageloader.ImageLoader;
import com.cy.threadpool.AbstractThreadPool;
import com.cy.tracer.Tracer;
import com.cy.utils.sharepref.SharePrefUtil;

/**
 * app启动初始化类
 * 
 * @author JLB6088
 * 
 */
public enum WatchDog {
    INSTANCE;

    private WatchDog() {}

    private Context mContext;
    public boolean sIsInitializeDone = false;

    private List<BaseActivity> mBaseActivities = Collections.synchronizedList(new LinkedList<BaseActivity>());

    public static boolean init(Context context) {
        context = context.getApplicationContext();
        return INSTANCE.initialize(context);
    }

    public static void deInit(Context context) {
        context = context.getApplicationContext();
        INSTANCE.Deinitialize(context);
    }

    public void addActivity(BaseActivity activity, boolean canAsBootActivity) {
        mBaseActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        mBaseActivities.remove(activity);
    }

    public BaseActivity getActivity(int location) {
        if (mBaseActivities.isEmpty() || location > (mBaseActivities.size() - 1) || location < 0) {
            return null;
        }
        return mBaseActivities.get(location);
    }

    public BaseActivity getTopActivity() {
        return getActivity(mBaseActivities.size() - 1);
    }

    public int getSize() {
        return mBaseActivities.size();
    }

    public void exit() {
        List<Activity> activities = new LinkedList<Activity>();
        activities.addAll(mBaseActivities);
        try {
            for (Activity activity : activities) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
        }
    }

    public void tryKillBackgroundProcess() {
        // if (!hasSeviceRunning() && (mBaseActivities == null ||
        // mBaseActivities.isEmpty())) {
        // android.os.Process.killProcess(android.os.Process.myPid());
        // }
    }

    public static Context getContext() {
        return INSTANCE.mContext;
    }

    /**
     * 初始化函数，app启动时调用
     * 
     * @param context
     * @return
     */
    private boolean initialize(Context context) {
        if (sIsInitializeDone) {
            return true;
        }
        context = context.getApplicationContext();
        mContext = context;

        Tracer.init(context);
        SharePrefUtil.init(context);
        ImageLoader.initialize(context, 0);
        
        DownloadService.startDownloadService(context);
        
//        DownloadInfoMgr.initDownloadService(); // 下载服务
//        registerReceiver();

        sIsInitializeDone = true;
        return true;
    }

    private void Deinitialize(Context context) {
        if (!sIsInitializeDone) {
            return;
        }

        sIsInitializeDone = false;
    }
    
    public static void post(Runnable runnable) {
        mMainHandler.post(runnable);
    }
    
    public static void postDelayed(Runnable runnable, long delayMillis) {
        mMainHandler.postDelayed(runnable, delayMillis);
    }
    
    public static void removeRunnable(Runnable runnable) {
        mMainHandler.removeCallbacks(runnable);
    }
    
    public static Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
        	if(!(msg.obj instanceof Runnable)) {
        		return;
        	}
        	
        	AbstractThreadPool abstractThreadPool = AbstractThreadPool.mThreadPoolMap.get(msg.arg2);
        	if(null != abstractThreadPool) {
        		abstractThreadPool.post(((Runnable)msg.obj));
    		}
        }
    };
    
    private static HandlerThread sHandlerThread;
    private static volatile Handler sLoopHandler;
    
    public static Handler getLoopHandler() {
        initHandlerThread();
        return sLoopHandler;
    }

    private static void initHandlerThread() {
        if (sLoopHandler == null) {
            sHandlerThread = new HandlerThread("DaemonThread");
            sHandlerThread.start();
            sLoopHandler = new Handler(sHandlerThread.getLooper());
        }
    }

}
