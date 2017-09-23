package com.cy.aa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestThreadPool {

    public static void main(String[] args) {

//        testGetActiveCount();
        testMultiTask();
//        testMultiTaskInvokeAll();

//        testSingleTask();
        
//        new Thread(new Runnable() {
//            
//            @Override
//            public void run() {
//                testNormalMultiThread();
//            }
//        }).start();
        

    }
    
    static void testNormalMultiThread() {
        System.out.println(System.currentTimeMillis()/1000+" ---->  start ");
        Thread[] threads = new Thread[3];
        for (int i = 0; i < threads.length; i++) {
            final int index = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        Thread.sleep(5000);
                        int ran = new Random().nextInt(5000);
                        Thread.sleep(ran);
                        System.out.println("ran index-"+index+"："+ran);
                    } catch (InterruptedException e) {
                        System.out.println("lalalalalalala");
                    }
                    System.out.println(System.currentTimeMillis()/1000+" ---->  "+"这是第"+index+"个");
                }
            });
            threads[i].start();
        }
        blockByThread(threads);
    }
    
    private static void blockByThread(Thread[] threads) {
        try {
            int waitTime = 2000;
            long deltaTime = 0;
            for (Thread thread : threads) {
                long startTime = System.currentTimeMillis();
                waitTime -= deltaTime;
                waitTime = Math.max(waitTime, 0);
                System.out.println(System.currentTimeMillis()/1000+" ---->  "+"等待时间："+waitTime);
                thread.join(waitTime);
                deltaTime = System.currentTimeMillis() - startTime;
                System.out.println(System.currentTimeMillis()/1000+" ---->  "+"deltaTime:"+deltaTime);
            }
        } catch (InterruptedException e) {
            System.out.println("lalala");
        }
        
        System.out.println(System.currentTimeMillis()/1000+" ---->  end ");
    }
    
    public static void testSingleTask() {
        FutureTask futureTask = new FutureTask(new SingleTask("我是111"), "我是111111111111") {
            @Override
            protected void done() {
                try {
                    System.out.println("done()");
                    System.out.println(get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        
//        ThreadPoolExecutor exec = new MyThreadPool(1, 2, 30L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingDeque<Runnable>());
//        exec.submit(futureTask);
//        futureTask.cancel(true);
        
        ScheduledExecutorService exec = new MySchedulerThreadPool(1);
        exec.submit(futureTask);
//        futureTask.cancel(true);
//        Future future1 = exec.submit(new SingleTask("我是111"));
//        Future future2 = exec.submit(new SingleTask("我是222"));
//        Future future3 = exec.submit(new SingleTask("我是333"));
//        future1.cancel(true);
//        try {
//            System.out.println(future2.get());
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    public static void testMultiTask() {
        
        /**
         * 内部维护11个线程的线程池
         */
        ExecutorService exec = Executors.newCachedThreadPool();
        /**
         * 容量为10的阻塞队列
         */
        final BlockingQueue<Future<String>> queue = new LinkedBlockingDeque<Future<String>>(5);
        // 实例化CompletionService
        final CompletionService<String> completionService = new ExecutorCompletionService<String>(exec);// (exec, queue);


        System.out.println(System.currentTimeMillis()/1000+"-start");
        
        ArrayList<Future<String>> ff = new ArrayList<Future<String>>();

        /**
         * 模拟瞬间产生10个任务，且每个任务执行时间不一致
         */
        for (int i = 0; i < 3; i++) {
            final int ii = i;
            Future<String> f = completionService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    int ran = new Random().nextInt(5000);
                    Thread.sleep(ran);
                    System.out.println(System.currentTimeMillis() / 1000 + "--> " + ii + "-" + ran);
                    return "ran:" + ran;
                }
            });
            ff.add(f);
        }
        
//        try {
//            int waitTime = Constant.SECOND_20;
//            long deltaTime = 0;
//            for (Thread thread : threads) {
//                long startTime = System.currentTimeMillis();
//                waitTime -= deltaTime;
//                waitTime = Math.max(waitTime, 0);
//                thread.join(waitTime);
//                deltaTime = System.currentTimeMillis() - startTime;
//            }
//        } catch (InterruptedException e) {
//        }

        int waitTime = 2000;
        long deltaTime = 0;
        long startTime = 0;
        
        for (int i = 0; i < ff.size(); i++) {
            try {
                // 谁最先执行完成，直接返回
//                Future<String> f = completionService.take(); // take方法会等待线程执行完
//                Future<String> f = completionService.poll(2000, TimeUnit.MILLISECONDS);
//                if(f != null) {
////                    System.out.println("结果：ff.get(" + i + "):" + f.get());
//                }
                
                startTime = System.currentTimeMillis();
                waitTime -= deltaTime;
                waitTime = Math.max(waitTime, 0);
                
                System.out.println(System.currentTimeMillis() / 1000+" - run "+i + " - 等待时间  : "+waitTime);
                System.out.println(System.currentTimeMillis() / 1000 +  "------------------> " + " : 结果：ff.get(" + i + ")-->" + ff.get(i).get(waitTime, TimeUnit.MILLISECONDS));
                deltaTime = System.currentTimeMillis() - startTime;
//                System.out.println(System.currentTimeMillis() / 1000 +  "--> " + " : 结果：ff.get(" + 0 + "):" + ff.get(0).get(2000, TimeUnit.MILLISECONDS));
//                System.out.println(System.currentTimeMillis() / 1000+"   for");
//                System.out.println(System.currentTimeMillis() / 1000 +  "--> " + " : 结果：ff.get(" + 1 + "):" + ff.get(1).get(2000, TimeUnit.MILLISECONDS));
//                System.out.println(System.currentTimeMillis() / 1000+"   for");
//                System.out.println(System.currentTimeMillis() / 1000 +  "--> " + " : 结果：ff.get(" + 2 + "):" + ff.get(2).get(2000, TimeUnit.MILLISECONDS));
//                System.out.println(System.currentTimeMillis() / 1000+"   for");
//                System.out.println(System.currentTimeMillis() / 1000 +  "--> " + " : 结果：ff.get(" + 3 + "):" + ff.get(3).get(2000, TimeUnit.MILLISECONDS));
//                System.out.println(System.currentTimeMillis() / 1000+"   for");
//                System.out.println(System.currentTimeMillis() / 1000 +  "--> " + " : 结果：ff.get(" + 4 + "):" + ff.get(4).get(6000, TimeUnit.MILLISECONDS));
            } catch (Exception e) {
//                e.printStackTrace();
                deltaTime = System.currentTimeMillis() - startTime;
            } 
//            catch (ExecutionException e) {
////                e.printStackTrace();
//            }
//            catch (TimeoutException e) {
////                e.printStackTrace();
////                ff.get(i).cancel(true);
//            }
        }

        System.out.println(System.currentTimeMillis()/1000+" - end");

        exec.shutdown();
    }

    public static void testMultiTaskInvokeAll() {
        ExecutorService exec = Executors.newFixedThreadPool(10);

        List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
        Callable<Integer> task = null;
        for (int i = 0; i < 10; i++) {
            task = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int ran = new Random().nextInt(1000);
                    Thread.sleep(ran);
                    System.out.println(Thread.currentThread().getName() + " 休息了 " + ran);
                    return ran;
                }
            };

            tasks.add(task);
        }

        long s = System.currentTimeMillis();

        List<Future<Integer>> results = null;
        try {
            results = exec.invokeAll(tasks);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("执行任务消耗了 ：" + (System.currentTimeMillis() - s) + "毫秒");

        for (int i = 0; i < results.size(); i++) {
            try {
                System.out.println(results.get(i).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        exec.shutdown();
    }

    public static void testGetActiveCount() {
//        MyThreadPool exec = null;
        ScheduledExecutorService exec = null;
        try {
//            exec = new MyThreadPool(2, 2, 30L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>());
//            exec = new MyThreadPool(1, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
            exec = new MySchedulerThreadPool(3);

            // 想线程池中放入三个任务
//            exec.execute(new Task());
//            exec.execute(new Task());
//            exec.execute(new Task());
//            exec.execute(new Task());
            exec.execute(new Task());
            exec.execute(new Task());
            exec.execute(new Task());
            exec.execute(new Task());
//            Future<?> fff = exec.submit(new Task(), "11");
//            System.out.println("fff.get():"+fff.get());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (exec != null) {
//                exec.shutdown();
            }
        }
    }

}

class MySchedulerThreadPool extends ScheduledThreadPoolExecutor {

    public MySchedulerThreadPool(int arg0) {
        super(arg0);
    }

    @Override
    protected void afterExecute(Runnable arg0, Throwable arg1) {
        super.afterExecute(arg0, arg1);
        synchronized (this) {
            System.out.println("--> activeCount:" + getActiveCount());
            System.out.println("--> CompletedTaskCount:" + getCompletedTaskCount());
            System.out.println("--> TaskCount:" + getTaskCount());
//            System.out.println("--> getCorePoolSize:" + getCorePoolSize());
//            System.out.println("--> getMaximumPoolSize:" + getMaximumPoolSize());
            System.out.println("--> getQueue:" + getQueue().size());
            System.out.println("--> ======================================================");

        }

    }
}

class MyThreadPool extends ThreadPoolExecutor {

    public MyThreadPool(int arg0, int arg1, long arg2, TimeUnit arg3, BlockingQueue<Runnable> arg4) {
        super(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    protected void afterExecute(Runnable arg0, Throwable arg1) {
        super.afterExecute(arg0, arg1);
        synchronized (this) {
            System.out.println("--> activeCount:" + getActiveCount());
            System.out.println("--> CompletedTaskCount:" + getCompletedTaskCount());
            System.out.println("--> TaskCount:" + getTaskCount());
//            System.out.println("--> getCorePoolSize:" + getCorePoolSize());
//            System.out.println("--> getMaximumPoolSize:" + getMaximumPoolSize());
            System.out.println("--> getQueue:" + getQueue().size());
            System.out.println("--> ======================================================");

        }

    }
}

class Task implements Runnable, Comparable<Task> {
    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Task arg0) {
        return 0;
    }
}

class SingleTask implements Runnable {

    private String mPrint;

    public SingleTask(String print) {
        mPrint = print;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            System.out.println(mPrint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
