package com.cy.aa;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Delayed;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TestPriorityQueue {

    static PriorityBlockingQueue<MyQueue> mWorkerQueue = new PriorityBlockingQueue<>();

    public static void main(String[] args) {

        MyQueue myQueue1 = new MyQueue("11", 0, 2000);
        myQueue1.calculateScheduledTime();
        MyQueue myQueue5 = new MyQueue("55", 0, 7000);
        myQueue5.calculateScheduledTime();
        MyQueue myQueue6 = new MyQueue("66", 0, 5000);
        myQueue6.calculateScheduledTime();
        MyQueue myQueue4 = new MyQueue("44", 9, 0);
        myQueue4.calculateScheduledTime();
        MyQueue myQueue7 = new MyQueue("77", 0, 6000);
        myQueue7.calculateScheduledTime();
        MyQueue myQueue2 = new MyQueue("22", 10, 0);
        myQueue2.calculateScheduledTime();
        MyQueue myQueue8 = new MyQueue("88", 0, 4000);
        myQueue8.calculateScheduledTime();
        MyQueue myQueue9 = new MyQueue("99", 8, 0);
        myQueue9.calculateScheduledTime();
        MyQueue myQueue3 = new MyQueue("33", 0, 3000);
        myQueue3.calculateScheduledTime();

        mWorkerQueue.add(myQueue1);
        mWorkerQueue.add(myQueue2);
        mWorkerQueue.add(myQueue3);
        mWorkerQueue.add(myQueue4);
        mWorkerQueue.add(myQueue5);
        mWorkerQueue.add(myQueue6);
        mWorkerQueue.add(myQueue7);
        mWorkerQueue.add(myQueue8);
        mWorkerQueue.add(myQueue9);
        
        Iterator<MyQueue> iter = mWorkerQueue.iterator();
        while(iter.hasNext()) {
            MyQueue mq = iter.next();
            System.out.println(mq.toString());
        }
        System.out.println("====================================");
        MyQueue myQueue;
        
        synchronized (mWorkerQueue) {
            while (true) {
                if (mWorkerQueue.size() == 0) {
                    return;
                }
                
                MyQueue first = mWorkerQueue.peek();
                if(first.mPriority <= 0) {
                    long waitTime = first.mScheduledTimeMillis - System.currentTimeMillis();
                    if (waitTime > 0) {
                        try {
                            mWorkerQueue.wait(waitTime);
                        } catch (InterruptedException e) {
                        }
                    }
                }
                
                myQueue = mWorkerQueue.poll();
                if (myQueue != null) {
//                    // Got work to do. Break out of waiting loop
//                    break;
                    System.out.println(myQueue.toString());
                }
            }
        }
    }

}

class MyQueue implements Delayed {
    
    @Override
    public String toString() {
        return mName;
    }

    public MyQueue(String name, long priority, long delayMillis) {
        mName = name;
        mPriority = priority;
        mDelayMillis = delayMillis;
    }

    public String mName;
    public long mPriority = 0;
    public long mDelayMillis = 0;

    public long mScheduledTimeMillis = 0;

    void calculateScheduledTime() {
        mScheduledTimeMillis = System.currentTimeMillis() + mDelayMillis;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(mScheduledTimeMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed another) {
        MyQueue anotherArgs = (MyQueue) another;
        if(mPriority == 0) {
            if (this.mScheduledTimeMillis == anotherArgs.mScheduledTimeMillis) {
                return 0;
            } else if (this.mScheduledTimeMillis < anotherArgs.mScheduledTimeMillis) {
                return -1;
            } else {
                return 1;
            }
        } else if (mDelayMillis == 0) {
            if(mPriority == anotherArgs.mPriority) {
                return 0;
            } else if (mPriority < anotherArgs.mPriority) {
                return 1;
            } else {
                return -1;
            }
        }
        
        return 0;
    }

}
