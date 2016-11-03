package com.cy.imageloader.task;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.cy.imageloader.ImageLoader;
import com.cy.imageloader.listener.ImageLoadingListener;

public class ImageLoadTask implements Runnable {
    private WeakReference<ImageLoader> mImageLoaderWeakRef;
    private String mUrl;
    private View mView;
    private ImageLoadingListener mImageLoadingListener;

    public ImageLoadTask(ImageLoader imageLoader, String url, View view, ImageLoadingListener imageLoadingListener) {
        mImageLoaderWeakRef = new WeakReference<ImageLoader>(imageLoader);
        mUrl = url;
        mView = view;
        mImageLoadingListener = imageLoadingListener;
    }

    public void run() {
//        Log.e("cyTest", Thread.currentThread().getName() + "线程被调用了。" + "主线程："+Looper.getMainLooper().getThread().getName()); 
    	Log.e("cyTest", "--> mUrl:"+mUrl);
        final ImageLoader imageLoader = mImageLoaderWeakRef.get();
        if (null == imageLoader) {
            return;
        }

        imageLoader.updateTaskDelay();

        Bitmap bitmap = imageLoader.getMemoryCacheBitmap(mUrl);
        if (null == bitmap) {
            bitmap = imageLoader.getLocalCacheBitmap(mUrl); // 从本地缓存获取
            if (null == bitmap) {
                bitmap = imageLoader.getNetBitmap(mUrl); // 从网络获取
            }
        }

        if (null != bitmap) {
            Bitmap retBitmap = imageLoader.resizeBitmap(bitmap, mView);
            boolean isCached = imageLoader.putDiskCacheBitmap(mUrl, retBitmap);
            if(isCached) {
                onLoadBitmap(retBitmap, mView, mImageLoadingListener);
            }
        }

    }

    private void onLoadBitmap(Bitmap retBitmap, View view, ImageLoadingListener imageLoadingListener) {
        final ImageLoader imageLoader = mImageLoaderWeakRef.get();
        if (null == imageLoader) {
            return;
        }

        boolean isUseful = imageLoader.onHandleFinish(view, retBitmap, mUrl, imageLoadingListener);
        if (isUseful) {
            imageLoader.putMemoryCacheBitmap(mUrl, retBitmap);
        }
    }
}
