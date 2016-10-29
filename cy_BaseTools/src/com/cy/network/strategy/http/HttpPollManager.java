package com.cy.network.strategy.http;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class HttpPollManager {
	private static String POLLMGR_THREAD_NAME = "pollmgr";
	private static int MIN_INTERVAL = 1 * 1000;

	private static PollManagerThreadHandler mThreadHandler = null;
	private static HandlerThread mHanderThread = null;
	private static Vector<PollRunnable> mRunnableVector = null;

	private void enqueueRunnable(PollRunnable runnable) {
		if (mRunnableVector == null) {
			mRunnableVector = new Vector<HttpPollManager.PollRunnable>();
		}
		mRunnableVector.addElement(runnable);
	}

	private void dequeueRunnable(PollRunnable runnable) {
		if (mRunnableVector != null) {
			if (mRunnableVector.contains(runnable)) {
				mRunnableVector.remove(runnable);
			}
			if (mRunnableVector.isEmpty()) {
				mRunnableVector = null;
			}
		}
	}

	static class PollManagerThreadHandler extends Handler {
		PollManagerThreadHandler(Looper looper) {
			super(looper);
		}
	}

	static public class PollRunnable implements Runnable {
		private HttpPollTask mTask;
		private AtomicBoolean isEnable;

		public PollRunnable(HttpPollTask task) {
			mTask = task;
			isEnable = new AtomicBoolean(true);
		}

		public void disableTask() {
			mThreadHandler.removeCallbacks(this);
			isEnable.set(false);
		}

		public boolean isTaskEnable() {
			return isEnable.get();
		}

		public void reschedule() {
			mThreadHandler.removeCallbacks(this);
			mThreadHandler.postAtFrontOfQueue(this);
		}

		public void reschedule(int delay) {
			mThreadHandler.removeCallbacks(this);
			mThreadHandler.postDelayed(this, delay);
		}

		@Override
		public void run() {
			if (isTaskEnable() && isValidTask(mTask)) {
				mTask.execute();
				mTask.countDown();
				mThreadHandler.postDelayed(this, mTask.getTimeInterval());
			}
		}
	}

	static private boolean isValidTask(HttpPollTask task) {
		if (task != null
				&& task.getUrl() != null
				&& task.getTimeInterval() >= MIN_INTERVAL
				&& task.getTimes() > 0) {
			return true;
		}
		return false;
	}

	public PollRunnable addPollTask(HttpPollTask task) {
		PollRunnable runnable = null;

		if (mHanderThread == null) {
			mHanderThread = new HandlerThread(POLLMGR_THREAD_NAME);
			mHanderThread.start();
			mThreadHandler = new PollManagerThreadHandler(mHanderThread.getLooper());
		}

		if (isValidTask(task)) {
			runnable = new PollRunnable(task);
			mThreadHandler.postDelayed(runnable, task.getFirstInterval());
			enqueueRunnable(runnable);
		}

		return runnable;
	}

	public void cancelAllTask() {
		mThreadHandler.removeCallbacksAndMessages(null);

		if (mRunnableVector != null) {

			for (int i = 0; i < mRunnableVector.size(); i++) {
				PollRunnable runnable = mRunnableVector.elementAt(i);
				runnable.disableTask();
				dequeueRunnable(runnable);
				runnable.mTask.cancelTask();
			}
		}
		stopManagerThread();
	}

	public void cancelTask(PollRunnable runnable) {
		if (runnable != null) {
			runnable.disableTask();
			dequeueRunnable(runnable);
			mThreadHandler.removeCallbacks(runnable);
			runnable.mTask.cancelTask();
		}
	}

	private void stopManagerThread() {
		if (mThreadHandler != null) {
			mThreadHandler.getLooper().quit();
			mThreadHandler = null;
		}
		mHanderThread = null;
	}

	private static HttpPollManager mManager;

	public static HttpPollManager getInstance() {
		if (mManager == null) {
			mManager = new HttpPollManager();
		}
		return mManager;
	}

}
