package com.example.util;


import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.SparseArray;

public class BitmapUtil {

    private static final String WARNING_LEAK = "WARNING: bitmap leak detected";

    private static class BitmapBean {
        private Bitmap mBitmap;
        private String mStackInfo;

        private BitmapBean(Bitmap bitmap, String stackInfo) {
            mBitmap = bitmap;
            mStackInfo = stackInfo;
        }

        private void recycle() {
            if (!isBitmapEmpty(mBitmap)) {
                mBitmap.recycle();
            }
        }
    }

    private static final SparseArray<BitmapBean> BITMAP_ARRAY = new SparseArray<BitmapBean>();

    public static Bitmap createBitmap(int width, int height, Bitmap.Config config) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
//        saveBitmap(bitmap);
        return bitmap;
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(source, x, y, width, height);
//        saveBitmap(bitmap);
        return bitmap;
    }

    public static Bitmap decodeFile(String pathName) {
        return decodeFile(pathName, null);
    }

    public static Bitmap decodeFile(String pathName, BitmapFactory.Options opts) {
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
//        saveBitmap(bitmap);
        if (opts == null && bitmap == null) {
        }
        return bitmap;
    }

    public static Bitmap decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts) {
        Bitmap bitmap = BitmapFactory.decodeStream(is, outPadding, opts);
//        saveBitmap(bitmap);
        return bitmap;
    }

    public static Bitmap decodeResource(Resources res, int id) {
        return decodeResource(res, id, null);
    }

    public static Bitmap decodeResource(Resources res, int id, BitmapFactory.Options opts) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, id, opts);
//        saveBitmap(bitmap);
        return bitmap;
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length);
//        saveBitmap(bitmap);
        return bitmap;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (!isBitmapEmpty(bitmap)) {
//            bitmap.recycle();
//            ThreadPoolUtil.post(new Runnable() {
//                @Override
//                public void run() {
//                    synchronized (BITMAP_ARRAY) {
//                        BITMAP_ARRAY.remove(bitmap.hashCode());
//                    }
//
//                }
//            });
        }
    }

    public static boolean isBitmapEmpty(Bitmap bitmap) {
        return null == bitmap || bitmap.isRecycled();
    }

    public static void onApplicationExit() {
//        ThreadPoolUtil.removeCallbacks(sCheckTask);
//        ThreadPoolUtil.postDelayed(sCheckTask, Constant.SECOND_30);
    }


}

