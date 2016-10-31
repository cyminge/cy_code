package com.cy.imageloader;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cy.cache.disk.DiskCache;
import com.cy.cache.disk.impl.LruDiskCache;
import com.cy.cache.disk.naming.FileNameGenerator;
import com.cy.cache.disk.naming.HashCodeFileNameGenerator;
import com.cy.cache.memory.MemoryCache;
import com.cy.cache.memory.impl.LruMemoryCache;
import com.cy.constant.Constant;
import com.cy.global.WatchDog;
import com.cy.imageloader.listener.ImageLoadingListener;
import com.cy.imageloader.task.ImageLoadTask;
import com.cy.imageloader.ui.AlphaAnimImageView;
import com.cy.tracer.Tracer;
import com.cy.utils.bitmap.BitmapUtils;
import com.cy.utils.storage.StorageUtils;

/**
 * 图片处理管理类 
 * 1. 滑动时的下载处理
 * 2. 快速上下滑动卡顿 ?? (一个是线程池优化，另一个滑动时的启动加载的优化)
 * 
 * @author JLB6088
 * 
 */
public enum ImageLoader {
	INSTANCE;

    private static final String TAG = "ImageLoader";

    private Context mContext;
    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;
    private boolean mPauseLoad = false; // 是否暂停加载
    private static final int IMAGE_LOAD_DELAY = 200;
    private int mTaskDelay = IMAGE_LOAD_DELAY;
    private static final int IMAGE_LOAD_DELAY_STEP = 40;
    private static final int DELAY_STEP = 40;
    
    private Map<View, String> mViewNeedToLoad = new HashMap<View, String>(); 

    private static final String IMAGE_DISK_CACHE_PATH = "cyTest";

    private ImageLoader() {
    }

    public static void initialize(Context context, int maxSize) {
		INSTANCE.startInitialize(context, maxSize);
	}

	private void startInitialize(Context context, int maxSize) {
		synchronized (ImageLoader.class) {
			int maxMemory = (int) Runtime.getRuntime().maxMemory();
	        if (0 == maxSize) {
	            maxSize = maxMemory / 8;
	        }
	        Tracer.i(TAG, "image loader max Memory : " + maxMemory);
	        mContext = context;
	        mMemoryCache = new LruMemoryCache(maxSize);
	        mDiskCache = createDiskCache(context);
		}

	}

    private DiskCache createDiskCache(Context context) {
        return createDiskCache(context, createFileNameGenerator(), 100 * 1024 * 1024, 0);
    }

    private FileNameGenerator createFileNameGenerator() {
        return new HashCodeFileNameGenerator();
    }

    private DiskCache createDiskCache(Context context, FileNameGenerator diskCacheFileNameGenerator,
            long diskCacheSize, int diskCacheFileCount) {
        File reserveCacheDir = createReserveDiskCacheDir(context);
        if (diskCacheSize > 0 || diskCacheFileCount > 0) {
            File individualCacheDir = StorageUtils.getIndividualCacheDirectory(context);
            try {
                return new LruDiskCache(individualCacheDir, reserveCacheDir, diskCacheFileNameGenerator,
                        diskCacheSize, diskCacheFileCount);
            } catch (IOException e) {
            }
        }

        return null;
    }

    private File createReserveDiskCacheDir(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        File individualDir = new File(cacheDir, IMAGE_DISK_CACHE_PATH);
        if (individualDir.exists() || individualDir.mkdir()) {
            cacheDir = individualDir;
        }
        return cacheDir;
    }

    public void putMemoryCacheBitmap(String bitmapUrl, Bitmap bitmap) {
        mMemoryCache.put(hashKeyForDisk(bitmapUrl), bitmap);
    }

    public boolean putDiskCacheBitmap(String bitmapUrl, Bitmap bitmap) {
        try {
            return mDiskCache.save(bitmapUrl, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取缓存图片
     * 
     * @param bitmapUrl
     * @return
     */
    public Bitmap getMemoryCacheBitmap(String bitmapUrl) {
        return mMemoryCache.get(hashKeyForDisk(bitmapUrl));
    }

    /**
     * 获取磁盘图片
     * 
     * @param key
     * @param targetView
     * @return
     */
    public Bitmap getDiskCacheBitmap(String bitmapUrl, View targetView) {
        Bitmap bitmap = null;
        File imageFile = mDiskCache.get(bitmapUrl);
        if (imageFile != null && imageFile.exists() && imageFile.length() > 0) {
            bitmap = getDiskCacheBitmap(imageFile.getPath());
        }

        return bitmap;
    }

    /**
     * 使用MD5算法对传入的key进行加密并返回。
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public Bitmap getNetBitmap(String url, View targetView) {
        File file = ImageLoaderHelper.downloadImage(mContext, url);
        if (null != file) {
            return getDiskCacheBitmap(file.getPath());
        }

        return null;
    }

    private Bitmap getDiskCacheBitmap(String bitmapPath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapUtils.decodeFile(bitmapPath, opts);

        return bitmap;
    }

    public Bitmap resizeBitmap(Bitmap bitmap, View view) {
        if (view == null || bitmap == null) {
            return bitmap;
        }

        int desWidth = view.getWidth();
        int desHeight = view.getHeight();
        return BitmapUtils.extractThumbnailIfNeed(bitmap, desWidth, desHeight);
    }

    /**
     * 显示图像
     * 
     * @param position
     * @param iconUrl
     * @param view
     * @param defBitmapId
     */
    public void displayImage(String iconUrl, ImageView view, int defBitmapId, ImageLoadingListener imageLoadingListener) {
        Log.e("aa", "加载");
        if (TextUtils.isEmpty(iconUrl)) {
            view.setImageResource(defBitmapId);
            return;
        }
        
        Bitmap bitmap = getBitmap(iconUrl, view, imageLoadingListener);
        
        if (BitmapUtils.isBitmapEmpty(bitmap)) {
            view.setImageResource(defBitmapId);
            return;
        }

        if (view instanceof AlphaAnimImageView) {
            ((AlphaAnimImageView) view).setImageBitmap(bitmap, false);
        } else {
            view.setImageBitmap(bitmap);
        }
    }

    public Bitmap getBitmap(String iconUrl, View view) {
        if (null != view) {
            view.setTag(hashKeyForDisk(iconUrl));
        }
        
        return loadBitmap(iconUrl, view, null);
    }

    /**
     * 
     * @param position
     * @param iconUrl
     * @param view
     * @return
     */
    private Bitmap loadBitmap(String iconUrl, View view, ImageLoadingListener imageLoadingListener) {
        Bitmap bitmap = mMemoryCache.get(hashKeyForDisk(iconUrl)); // 缓存
        if (null != bitmap) {
//            Log.e("cyTest", "应该这里就返回啊");
            return bitmap;
        }
        
        // 从硬盘获取或网络获取
        startLoadBitmapTask(iconUrl, view, imageLoadingListener);
        return null;
        // && StorageUtils.isSDCardMounted()

    }

    /**
     * view maybe null, please check it before use.
     */
    protected boolean onFinish(View view, Bitmap bitmap) {
        if (null == mContext) {
            return false;
        }

        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return false;
        }

        if (null == view) {
            return true;
        }

        // int index = (Integer) view.getTag();
        // if (position != index) {
        // return false;
        // }

        setImageBitmap(view, bitmap);
        return true;
    }

    protected void setImageBitmap(final View view, final Bitmap bitmap) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ImageView) view).setImageBitmap(bitmap);
                if (view.getVisibility() != View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public boolean onHandleFinish(View view, Bitmap bitmap, String iconUrl, ImageLoadingListener imageLoadingListener) {
    	if(null != imageLoadingListener) {
    		imageLoadingListener.onLoadingComplete(iconUrl, view, bitmap);
    		return true;
    	}
        if (!hashKeyForDisk(iconUrl).equals(view.getTag().toString())) {
            return true;
        }

        return onFinish(view, bitmap);
    }

    /**
     * 如果缓存没有图片，则启动加载任务，该任务与ImageLoader形成一个弱引用关系，防止ImageLoader无法及时回收
     * 
     * @param position
     * @param iconUrl
     */
    private void startLoadBitmapTask(String iconUrl, View view, ImageLoadingListener imageLoadingListener) {
        if (mPauseLoad) {
//            mViewNeedToLoad.put(view, iconUrl);
            return;
        }
        
        Message msg = Message.obtain();
        msg.what = getTaskKey();
        msg.obj = new ImageLoadTask(this, iconUrl, view, imageLoadingListener);
        long delay = getTaskDelay();
        WatchDog.mMainHandler.sendMessageDelayed(msg, delay);
    }

    private int getTaskKey() {
        return ImageLoader.this.hashCode();
    }

    private int getTaskDelay() {
        mTaskDelay += DELAY_STEP;
        int delay = (mTaskDelay / IMAGE_LOAD_DELAY) * IMAGE_LOAD_DELAY;
        delay = Math.min(delay, Constant.SECOND_1);
        return Math.max(delay, IMAGE_LOAD_DELAY);
    }

    /**
     * 设置是否暂停加载图片的任务
     * 
     * @param pauseLoad
     */
    public void setPauseLoad(boolean pauseLoad) {
        mPauseLoad = pauseLoad;
    }

    public void updateTaskDelay() {
        mTaskDelay -= IMAGE_LOAD_DELAY_STEP;
    }

    public void removeCheckTask() {
        resetTaskDelay();
        WatchDog.mMainHandler.removeMessages(getTaskKey());
    }
    
    private void resetTaskDelay() {
        mTaskDelay = IMAGE_LOAD_DELAY;
    }
    
    /**
     * 这个方法是搞什么飞机的
     */
    public void reDisplayImage() {
        if(mViewNeedToLoad.size() > 0 ) { // mViewNeedToLoad 要不要同步
            removeCheckTask();
            Iterator<Entry<View, String>> iter = mViewNeedToLoad.entrySet().iterator();
            while(iter.hasNext()) {
                Entry<View, String> entry = iter.next();
                loadBitmap(entry.getValue(), entry.getKey()); // ???
//                Message msg = Message.obtain();
//                msg.what = getTaskKey();
//                msg.obj = new ImageLoadTask(this, entry.getValue(), entry.getKey());
//                long delay = getTaskDelay();
//                InitialWatchDog.mMainHandler.sendMessageDelayed(msg, delay);
            }
            mViewNeedToLoad.clear();
        }
    }

    public void exit() {
        recycle();
    }

    public void recycle() {
        mPauseLoad = false;
        // synchronized (mIconsArray) {
        // int size = mIconsArray.size();
        // for (int i = 0; i < size; i++) {
        // Bitmap bitmap = mIconsArray.valueAt(i);
        // BitmapUtil.recycleBitmap(bitmap);
        // }
        //
        // mAnchorPosition = Constant.DEFAULT_NUM;
        // mCurPosition = Constant.ZERO_NUM;
        // mIconsArray.clear();
        // mViewArray.clear();
        // mUrlLockSet.clear();
        // removeCheckTask();
        // }
    }

}
