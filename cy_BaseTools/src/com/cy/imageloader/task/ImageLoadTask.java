package com.cy.imageloader.task;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.cy.imageloader.ImageLoader;

public class ImageLoadTask implements Runnable {
    private WeakReference<ImageLoader> mImageLoaderWeakRef;
    private String mUrl;
    private View mView;

    public ImageLoadTask(ImageLoader imageLoader, String url, View view) {
        mImageLoaderWeakRef = new WeakReference<ImageLoader>(imageLoader);
        mUrl = url;
        mView = view;
    }

    public void run() {
        Log.e("cyTest", Thread.currentThread().getName() + "线程被调用了。" + "主线程："+Looper.getMainLooper().getThread().getName()); 
        final ImageLoader imageLoader = mImageLoaderWeakRef.get();
        if (null == imageLoader) {
            return;
        }

        imageLoader.updateTaskDelay();

        Bitmap bitmap = imageLoader.getMemoryCacheBitmap(mUrl);
        if (null == bitmap) {
            bitmap = imageLoader.getDiskCacheBitmap(mUrl, mView); // 从本地缓存获取
            if (null == bitmap) {
                bitmap = imageLoader.getNetBitmap(mUrl, mView); // 从网络获取
            }
        }

        if (null != bitmap) {
            Bitmap retBitmap = imageLoader.resizeBitmap(bitmap, mView);
            boolean isCached = imageLoader.putDiskCacheBitmap(mUrl, retBitmap);
            if(isCached) {
                onLoadBitmap(retBitmap, mView);
            }
        }

    }

    private void onLoadBitmap(Bitmap retBitmap, View view) {
        final ImageLoader imageLoader = mImageLoaderWeakRef.get();
        if (null == imageLoader) {
            return;
        }

        boolean isUseful = imageLoader.onHandleFinish(view, retBitmap, mUrl);
        if (isUseful) {
            imageLoader.putMemoryCacheBitmap(mUrl, retBitmap);
        }
    }
}
