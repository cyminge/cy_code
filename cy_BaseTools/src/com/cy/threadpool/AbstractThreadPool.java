package com.cy.threadpool;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;

public abstract class AbstractThreadPool {
	private static final String TAG = "AbstractThreadPool";
	
	public static final int THREAD_POOL_TYPE_IMAGELOAD = 1001;
	public static final int THREAD_POOL_TYPE_BACKGROUND_TASK = 1002;
	public static final int THREAD_POOL_TYPE_NOMAL = 1003;
	public static SparseArray<AbstractThreadPool> mThreadPoolMap;
	
	static {
		mThreadPoolMap = new SparseArray<AbstractThreadPool>();
		mThreadPoolMap.put(THREAD_POOL_TYPE_IMAGELOAD, ImageLoadThreadPool.getInstance());
		mThreadPoolMap.put(THREAD_POOL_TYPE_BACKGROUND_TASK, BackgroundTaskThreadPool.getInstance());
		mThreadPoolMap.put(THREAD_POOL_TYPE_NOMAL, NormalThreadPool.getInstance());
	}
	
    private ThreadPoolExecutor sThreadPool;
    private Handler mMainHandler;
    
    public AbstractThreadPool() {
    	sThreadPool = createThreadPool();
    	mMainHandler = createHandler();
    }
    
    protected abstract ThreadPoolExecutor createThreadPool();
    
    protected abstract Handler createHandler();

    public void post(Runnable task) {
        try {
        	sThreadPool.execute(task);
        } catch (RejectedExecutionException e) {
//            LogUtils.loge(TAG, "ThreadPool name = " + task.getClass() + e);
        }
    }
    
    public void postDelayed(final Runnable task, long delayMillis) {
        Message msg = Message.obtain();
        msg.obj = task;
        msg.arg2 = getThreadPoolType();
        mMainHandler.sendMessageDelayed(msg, delayMillis);
    }
    
    protected abstract int getThreadPoolType();
    
    public void postDelayed(final Message msg, long delayMillis) {
    	msg.arg2 = getThreadPoolType();
        mMainHandler.sendMessageDelayed(msg, delayMillis);
    }

    public void removeCallbacks(Runnable task) {
        mMainHandler.removeCallbacksAndMessages(task);
    }
    
    public void removeHandlerMsg(int what) {
    	if(!mMainHandler.hasMessages(what)) {
    		return;
    	}
    	mMainHandler.removeMessages(what);
    }
}
