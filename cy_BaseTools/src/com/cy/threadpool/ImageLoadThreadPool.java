package com.cy.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cy.global.InitialWatchDog;

import android.os.Handler;

public class ImageLoadThreadPool extends AbstractThreadPool {

	private static volatile ImageLoadThreadPool mInstance;
	
	public ImageLoadThreadPool() {
		super();
	}
	
	public static ImageLoadThreadPool getInstance() {
    	if(null == mInstance) {
    		synchronized (ImageLoadThreadPool.class) {
    			if (null == mInstance) {
    				mInstance = new ImageLoadThreadPool();
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
		return InitialWatchDog.mMainHandler;
	}

	@Override
	protected int getThreadPoolType() {
		return AbstractThreadPool.THREAD_POOL_TYPE_IMAGELOAD;
	}

}
