package com.cy.uiframe.main.load;

import java.util.HashMap;
import android.annotation.SuppressLint;
import com.cy.constant.Constant;
import com.cy.frame.downloader.util.JsonUtils;

@SuppressLint("Assert") 
public class MultiUrlBean implements IUrlBean {
    private String[] mUrlArray;
    private String mCacheKey;
    private final int mUrlCount;

    public MultiUrlBean(String[] urlArray) {
        assert (urlArray != null && urlArray.length > 1);
        mUrlCount = urlArray.length;
        mUrlArray = urlArray.clone();
        setCacheKey();
    }

    private void setCacheKey() {
        StringBuilder allUrl = new StringBuilder();
        for (String url : mUrlArray) {
            allUrl.append(url);
        }
        mCacheKey = String.valueOf(allUrl.toString().hashCode());
    }

    @Override
    public String getCacheKey() {
        return mCacheKey;
    }

    @Override
    public String postData(final HashMap<String, String> map) {
        Thread[] threads = new Thread[mUrlCount];
        final String[] requestResults = new String[mUrlCount];
        for (int i = 0; i < mUrlCount; i++) {
            final int index = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    requestResults[index] = JsonUtils.postData(mUrlArray[index], map);
                }
            });
            threads[i].start();
        }
        blockByThread(threads);
        return JsonUtils.mergeRequestResult(requestResults);
    }

    private void blockByThread(Thread[] threads) {
        try {
            int waitTime = Constant.SECOND_20;
            long deltaTime = 0;
            for (Thread thread : threads) {
                long startTime = System.currentTimeMillis();
                waitTime -= deltaTime;
                waitTime = Math.max(waitTime, 0);
                thread.join(waitTime);
                deltaTime = System.currentTimeMillis() - startTime;
            }
        } catch (InterruptedException e) {
        }
    }
}
