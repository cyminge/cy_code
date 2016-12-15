package com.cy;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPool {

	public static void main(String[] args) throws InterruptedException {
		// ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 4, 0,
		// TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());//设置线程池只启动一个线程
		// 阻塞队列一个元素
		// pool.setRejectedExecutionHandler(new
		// ThreadPoolExecutor.AbortPolicy());
		// //设置策略为抛出异常
		// for (int i = 0; i < 10; i++) {
		// final int j = i;
		// pool.execute(new Runnable() {
		// public void run() {
		// System.out.println("线程:"+j +
		// Thread.currentThread().getName()+"  开始执行");
		// try {
		// Thread.sleep(1000L);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// System.out.println("线程:"+j +
		// Thread.currentThread().getName()+"  执行完毕");
		// }
		// });
		// }
		// Thread.sleep(5000L);
		// pool.shutdown();
		// System.out.println("关闭后线程终止了吗？" + pool.isTerminated());

		ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(1));
		// 设置线程池的拒绝策略为"CallerRunsPolicy"
		pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		// 新建10个任务，并将它们添加到线程池中。
		for (int i = 0; i < 10; i++) {
			Runnable myrun = new MyRunnable("task-" + i);
			pool.execute(myrun);
		}
	}

	static class MyRunnable implements Runnable {
		private String name;

		public MyRunnable(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			try {
				System.out.println(this.name + " is running.");
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
