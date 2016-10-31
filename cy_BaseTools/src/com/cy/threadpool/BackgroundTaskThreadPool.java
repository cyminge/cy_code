package com.cy.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cy.global.WatchDog;

import android.os.Handler;

public class BackgroundTaskThreadPool extends AbstractThreadPool {

	private static volatile BackgroundTaskThreadPool mInstance;
	
	public BackgroundTaskThreadPool() {
		super();
	}
	
	public static BackgroundTaskThreadPool getInstance() {
    	if(null == mInstance) {
    		synchronized (BackgroundTaskThreadPool.class) {
    			if (null == mInstance) {
    				mInstance = new BackgroundTaskThreadPool();
    	        }
			}
    	}
        
        return mInstance;
    }
	
	@Override
	protected ThreadPoolExecutor createThreadPool() {
		return new ThreadPoolExecutor(Thread.NORM_PRIORITY, Thread.NORM_PRIORITY, 30L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}

	@Override
	protected Handler createHandler() {
		return WatchDog.mMainHandler;
	}

	@Override
	protected int getThreadPoolType() {
		return AbstractThreadPool.THREAD_POOL_TYPE_BACKGROUND_TASK;
	}

}
