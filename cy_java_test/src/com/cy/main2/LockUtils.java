package com.cy.main2;

public class LockUtils {
    private Object mLock = new Object();
    private static LockUtils sInstance;

    private LockUtils() {
    }

    public synchronized static LockUtils getInstance() {
        if (sInstance == null) {
            sInstance = new LockUtils();
        }

        return sInstance;
    }

    public void lock() {
        synchronized (mLock) {
            try {
                System.out.println("lockUtil. lock");
                mLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void release() {
        synchronized (mLock) {
            System.out.println("lockUtil. release");
            mLock.notifyAll();
        }
    }

    private boolean isLocked = false;

    public synchronized void lock11() throws InterruptedException {
//        System.out.println(Thread.currentThread().getName()+  "--11");
        while (isLocked) {
            wait();
            System.out.println(Thread.currentThread().getName()+  "--22");
        }
        isLocked = true;
//        System.out.println("33");
    }

    public synchronized void unlock11() {
        isLocked = false;
        notifyAll();
    }

}
