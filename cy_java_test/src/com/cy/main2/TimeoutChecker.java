package com.cy.main2;

public abstract class TimeoutChecker {

    private static final int DEFAULT_DELAY_MS = 10000;
    private static final String INITIAL_DATA = "initial_data";

    private String mData;

    public String start() {
        return start(DEFAULT_DELAY_MS);
    }

    public String start(int timeoutMs) {
        mData = INITIAL_DATA;

        Thread thread = new Thread() {
            public void run() {
                mData = getData();
            };
        };

        thread.start();
        try {
            thread.join(timeoutMs);
        } catch (InterruptedException e) {
        }

        if (INITIAL_DATA.equals(mData)) {
            onTimeout();
            return null;
        } else {
            onSuccess(mData);
            return mData;
        }
    }

    public void onSuccess(String data) {

    }

    public void onTimeout() {

    }

    public String getData() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}

