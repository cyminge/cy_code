package com.cy.global;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cy.tracer.CrashTracer;
import com.cy.tracer.Tracer;
import com.cy.tracer.export.IOnShowExceptionInfo;

/**
 * app启动初始化类
 * 
 * @author JLB6088
 * 
 */
public enum InitialWatchDog {
    INSTANCE;

    private InitialWatchDog() {
    }

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

        initialEnvironment(context); // log打印初始化
        Tracer.persisteLog(false); // 保存日志文件

        sIsInitializeDone = true;
        return true;
    }

    private int initialEnvironment(Context context) {
        /* 优先初始化log */
        initialLogDebug(context);
        return 0;
    }

    // 上报日志
    public static int initialLogDebug(Context context) {
        Tracer.init(context, new OnMaxLogFrame(context));
        CrashTracer.getInstance().init(context);

        final Context cxt = context.getApplicationContext();
        CrashTracer.getInstance().setShowInfo(new IOnShowExceptionInfo() {

            @Override
            public boolean isShownInfo() {
                return true;
            }
        });
        return 0;
    }

    public static Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof Runnable) {
                ThreadPoolUtil.post((Runnable) msg.obj);
            }
        }
    };

    private void Deinitialize(Context context) {
        if (!sIsInitializeDone) {
            return;
        }

        sIsInitializeDone = false;
    }

}
