package com.cy.aa;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestSchedule {
    public static void main(String args[]) throws ExecutionException, InterruptedException {
        ScheduledExecutorService schedule = Executors
                .newScheduledThreadPool(1);

//        /**
//         * 延迟1秒执行任务一次
//         */
//        schedule.schedule(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("hello world runnable");
//            }
//        }, 1000, TimeUnit.MILLISECONDS);
//
//
//        /**
//         * 延迟2秒执行任务一次
//         */
//        Future result = schedule.schedule(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                return "hello world callable";
//            }
//        }, 2000, TimeUnit.MILLISECONDS);
//
//        System.out.println(result.get());

//        // 以固定的频率来周期的执行任务
        schedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("定时周期任务执行1"+", 当前线程:"+Thread.currentThread().getName());
            }
        }, 1000, 2000, TimeUnit.MILLISECONDS);

        // 以固定的延迟来周期的执行任务
        schedule.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("定时周期任务执行2"+", 当前线程:"+Thread.currentThread().getName());
            }
        }, 1000, 2000, TimeUnit.MILLISECONDS);
        
        schedule.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("定时周期任务执行3"+", 当前线程:"+Thread.currentThread().getName());
            }
        }, 1000, 2000, TimeUnit.MILLISECONDS);
//
//        // 关闭定时和周期任务的执行
//        Thread.sleep(1000 * 10);
//        schedule.shutdown();//平缓的关闭
//        System.out.println("pool shutdown:" + schedule.isShutdown());
//        while (!schedule.isTerminated()) {
//            schedule.awaitTermination(1, TimeUnit.SECONDS);
//        }
    }
}
