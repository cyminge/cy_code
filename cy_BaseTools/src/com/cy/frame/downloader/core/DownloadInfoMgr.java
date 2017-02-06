package com.cy.frame.downloader.core;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cy.frame.downloader.controller.DownloadOrderMgr;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.downloadmanager.DownloadDB;
import com.cy.frame.downloader.downloadmanager.DownloadService;
import com.cy.global.WatchDog;
import com.cy.listener.GameListenerManager;
import com.cy.utils.Utils;
import com.cy.utils.storage.GNStorageUtils;

/**
 * 下载内容管理
 * @author JLB6088
 *
 */
public class DownloadInfoMgr {

    public interface DownloadChangeListener {
        public void onDownloadChange();
    }

    public interface StatusChangeListener {
        public void onStatusChange();
    }

    private static volatile DownloadInfoMgr sNormalInstance;
    private static volatile DownloadInfoMgr sSilentInstance;

    private ArrayList<DownloadChangeListener> mDownloadListeners = new ArrayList<DownloadChangeListener>();
    private ArrayList<StatusChangeListener> mStatusListeners = new ArrayList<StatusChangeListener>();
    private ConcurrentHashMap<String, DownloadInfo> mDownloadInfoMap = new ConcurrentHashMap<String, DownloadInfo>(); // 文件下载信息集合
    private static DownloadService sDownloadService;

    public static final int NOTIFY_PROGRESS_DELAY_TIME = 1500;
    private long mLastNotifyTime = 0;
    private boolean mIsSilent;
    private static boolean sSynDB = false;
    private Runnable mOnProgressChangeRunnable = new Runnable() {

        @Override
        public void run() {
            notifyChange(false);
            syncToDB();
        }
    };

    public static DownloadInfoMgr getNormalInstance() {
        if (sNormalInstance == null) {
            synchronized (DownloadInfoMgr.class) {
                if (sNormalInstance == null) {
                    sNormalInstance = new DownloadInfoMgr(false);
                }
            }
        }
        return sNormalInstance;
    }

    public static DownloadInfoMgr getSilentInstance() {
        if (sSilentInstance == null) {
            synchronized (DownloadInfoMgr.class) {
                if (sSilentInstance == null) {
                    sSilentInstance = new DownloadInfoMgr(true);
                }
            }
        }
        return sSilentInstance;
    }

    public static DownloadInfoMgr getInstance(DownloadInfo info) {
        if (info.mIsSilentDownload) {
            return getSilentInstance();
        } else {
            return getNormalInstance();
        }
    }

    public static boolean isSynDB() {
        return sSynDB;
    }

    private DownloadInfoMgr(boolean isSilent) {
        this.mIsSilent = isSilent;
    }

    public synchronized void initDownloadTask() {
        ArrayList<DownloadInfo> downloadList = DownloadDB.getInstance().queryAll(mIsSilent);

        initDownloadItems(downloadList);
        onOrderChange();
        notifyChange();
        
        sSynDB = true;
    }

    private void initDownloadItems(ArrayList<DownloadInfo> downloadList) {
        int downloadingCount = 0;
        boolean hasNetwork = Utils.isWifiNet();
        boolean sdCardMounted = GNStorageUtils.isSDCardMounted();
        for (DownloadInfo info : downloadList) {
            if (info.isRunning()) {
                downloadingCount = initRunningTask(downloadingCount, hasNetwork, sdCardMounted, info);
            }

            putToMap(info);
        }
    }

    private int initRunningTask(int downloadingCount, boolean hasNetwork, boolean sdCardMounted,
            DownloadInfo info) {
        if (!sdCardMounted || !hasNetwork) {
            info.mStatus = DownloadStatusMgr.TASK_STATUS_PAUSED;
            info.mReason = sdCardMounted ? getPauseReason() : DownloadStatusMgr.PAUSE_DEVICE_NOT_FOUND;
            DownloadDB.getInstance().update(info);
        } else if (info.isDownloading() && downloadingCount < DownloadStatusMgr.MAX_DOWNLOADING_TASK) {
//            DownloadRunnable task = mIsSilent ? new SilentDownloadRunnable(info) : new DownloadRunnable(info); // cyminge modify
            DownloadRunnable task = new DownloadRunnable(info);
            postTask(info.packageName, task);
            downloadingCount++;
        } else {
            info.mStatus = DownloadStatusMgr.TASK_STATUS_PENDING;
            DownloadDB.getInstance().update(info);
        }

        return downloadingCount;
    }

    private int getPauseReason() {
//        if (Utils.isMobileNet() && !SettingUtils.getAllowByMobileNet()) {// cyminge modify
        if (Utils.isMobileNet()) {
            return DownloadStatusMgr.PAUSE_WAIT_WIFI;
        } else {
            return DownloadStatusMgr.PAUSE_NO_NETWORK;
        }
    }

    public synchronized void clearListener() {
        mDownloadListeners.clear();
        mStatusListeners.clear();
    }

    public synchronized void addChangeListener(DownloadChangeListener listener) {
        if (listener == null || mDownloadListeners.contains(listener)) {
            return;
        }
        mDownloadListeners.add(listener);
    }

    public synchronized void removeChangeListener(DownloadChangeListener listener) {
        mDownloadListeners.remove(listener);
    }

    public synchronized void addChangeListener(StatusChangeListener listener) {
        if (listener == null || mStatusListeners.contains(listener)) {
            return;
        }
        mStatusListeners.add(listener);
    }

    public synchronized void removeChangeListener(StatusChangeListener listener) {
        mStatusListeners.remove(listener);
    }

    public void notifyChange() {
        notifyChange(true);
    }

    private void notifyChange(boolean statusChange) {
        mLastNotifyTime = System.currentTimeMillis();
//        synchronized (DownloadInfoMgr.this) {
            for (DownloadChangeListener listener : mDownloadListeners) {
                listener.onDownloadChange();
            }
            if (statusChange) {
                for (StatusChangeListener listener : mStatusListeners) {
                    listener.onStatusChange();
                }
            }
//        }
    }

    public void syncToDB() {
        DownloadDB.getInstance().updateAll(mDownloadInfoMap.values());
    }

    public boolean isDownloading(String pkgName) {
        DownloadInfo info = getDownloadInfo(pkgName);
        return info != null && !info.isCompleted();
    }

    public boolean hasDownloadInfo(String pkgName) {
        return mDownloadInfoMap.containsKey(pkgName);
    }

    public DownloadInfo getDownloadInfo(String pkgName) {
        return mDownloadInfoMap.get(pkgName);
    }

    public static DownloadInfo getDownloadInfoInAll(String pkgName) {
        DownloadInfo info = getNormalInstance().getDownloadInfo(pkgName);
        if (null == info) {
            info = getSilentInstance().getDownloadInfo(pkgName);
        }
        return info;
    }

    public Set<Entry<String, DownloadInfo>> getDownloadSet() {
        return mDownloadInfoMap.entrySet();
    }

    public ArrayList<String> getAllDownloadPkg() {
        return new ArrayList<String>(mDownloadInfoMap.keySet());
    }

    public void addDownloadInfo(final DownloadInfo info) {
        putToMap(info);
        onOrderChange();
    }

    public void removeDownloadInfo(String pkgName) {
        DownloadInfo info = getDownloadInfo(pkgName);
        if (info == null) {
            return;
        }

        removeTask(info);
        mDownloadInfoMap.remove(pkgName);
        onOrderChange();
        notifyChange();
        deleteDB(pkgName, info);
    }

    private void deleteDB(String pkgName, DownloadInfo info) {
        DownloadDB.getInstance().delete(pkgName);
    }

    private void downloadCountChange() {
        GameListenerManager.onEvent(GameListenerManager.DOWNLOAD_COUNT_CHANGE);
    }

    private void onOrderChange() {
        if (mIsSilent) {
            return;
        }

        DownloadOrderMgr.onOrderChange(mDownloadInfoMap);
        downloadCountChange();
    }

    public void orderChange() {
        onOrderChange();
        notifyChange();
    }

    public void updateDownloadInfo(DownloadInfo info) {
        updateDownloadInfo(info, true);
    }

    public void updateDownloadInfo(DownloadInfo info, boolean updateDb) {
        if (!hasDownloadInfo(info.packageName)) {
            return;
        }
        putToMap(info);
        if (info.isCompleted()) {
            onOrderChange();
        }
        notifyChange();
        if (updateDb) {
            DownloadDB.getInstance().update(info);
        }
    }

    public void updateProgress(DownloadInfo info) {
        if (!hasDownloadInfo(info.packageName)) {
            return;
        }
        putToMap(info);

        long delay = (NOTIFY_PROGRESS_DELAY_TIME - (System.currentTimeMillis() - mLastNotifyTime));
        delay = Math.max(0, delay);
        delay = Math.min(NOTIFY_PROGRESS_DELAY_TIME, delay);
        
        WatchDog.getLoopHandler().removeCallbacks(mOnProgressChangeRunnable);
        WatchDog.getLoopHandler().postDelayed(mOnProgressChangeRunnable, delay);
    }

    public void updateProgressNoDelay(DownloadInfo info) {
        if (!hasDownloadInfo(info.packageName)) {
            return;
        }
        putToMap(info);

        notifyChange(false);
        syncToDB();
    }

    public void putToMap(DownloadInfo info) {
        mDownloadInfoMap.put(info.packageName, info);
    }

    public void removeFromMap(String pkgName) {
        mDownloadInfoMap.remove(pkgName);
    }

    private static void postTask(String pkgName, DownloadRunnable task) {
        DownloadService.postTask(pkgName, task);
    }

    private static void removeTask(DownloadInfo info) {
        DownloadService.removeTask(info, info.mStatus);
    }

    public ArrayList<DownloadInfo> getDownloadInfos(ArrayList<String> packageList) {
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<DownloadInfo>();
        for (String pkgNmae : packageList) {
            downloadInfos.add(mDownloadInfoMap.get(pkgNmae));
        }
        return downloadInfos;
    }

}
