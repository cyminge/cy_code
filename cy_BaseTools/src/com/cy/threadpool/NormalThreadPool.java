package com.cy.threadpool;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cy.global.WatchDog;

import android.os.Handler;

public class NormalThreadPool extends AbstractThreadPool {
	private static volatile NormalThreadPool mInstance;

	public NormalThreadPool() {
		super();
	}

	public static NormalThreadPool getInstance() {
		if (null == mInstance) {
			synchronized (NormalThreadPool.class) {
				if (null == mInstance) {
					mInstance = new NormalThreadPool();
				}
			}
		}

		return mInstance;
	}

	@Override
	protected ThreadPoolExecutor createThreadPool() {
		return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>());
	}

	@Override
	protected Handler createHandler() {
		return WatchDog.mMainHandler;
	}

	@Override
	protected int getThreadPoolType() {
		return THREAD_POOL_TYPE_NOMAL;
	}

}
