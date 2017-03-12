package com.cy.frame.downloader.core;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cy.frame.downloader.config.DownloadConfiguration;
import com.cy.frame.downloader.controller.DownloadOrderMgr;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.manager.DownloadDB;
import com.cy.frame.downloader.manager.DownloadService;
import com.cy.listener.GameListenerManager;
import com.cy.utils.Utils;
import com.cy.utils.storage.GNStorageUtils;

/**
 * 下载内容管理
 * @author JLB6088
 *
 */
public class DownloadInfoMgr {

    private ConcurrentHashMap<String, DownloadInfo> mDownloadInfoMap = new ConcurrentHashMap<String, DownloadInfo>(); // 文件下载信息集合
    private static DownloadService sDownloadService;
    private DownloadManager mDownloadManager;

    private boolean mIsSilent;
    private static boolean sSynDB = false;

    public static boolean isSynDB() {
        return sSynDB;
    }

    public DownloadInfoMgr(DownloadManager dlMgr, boolean isSilent) {
        mIsSilent = isSilent;
        mDownloadManager = dlMgr;
    }

    public void initDownloadInfo() {
        ArrayList<DownloadInfo> downloadList = DownloadDB.getInstance().queryAll(mIsSilent);
        initDownloadItems(downloadList);
        onOrderChange();
        mDownloadManager.notifyChange();
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
            info.mStatus = DownloadStatusConstant.TASK_STATUS_PAUSED;
            info.mReason = sdCardMounted ? getPauseReason() : DownloadStatusConstant.TASK_PAUSE_DEVICE_NOT_FOUND;
            DownloadDB.getInstance().update(info);
        } else if (info.isDownloading() && downloadingCount < DownloadConfiguration.MAX_DOWNLOADING_TASK) {
//            DownloadRunnable task = mIsSilent ? new SilentDownloadRunnable(info) : new DownloadRunnable(info); // cyminge modify
            DownloadRunnable task = new DownloadRunnable(info);
            postTask(info.packageName, task);
            downloadingCount++;
        } else {
            info.mStatus = DownloadStatusConstant.TASK_STATUS_PENDING;
            DownloadDB.getInstance().update(info);
        }

        return downloadingCount;
    }

    private int getPauseReason() {
//        if (Utils.isMobileNet() && !SettingUtils.getAllowByMobileNet()) {// cyminge modify
        if (Utils.isMobileNet()) {
            return DownloadStatusConstant.TASK_PAUSE_WAIT_WIFI;
        } else {
            return DownloadStatusConstant.TASK_PAUSE_NO_NETWORK;
        }
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
        mDownloadManager.notifyChange();
        deleteDB(pkgName, info);
    }

    private void deleteDB(String pkgName, DownloadInfo info) {
        DownloadDB.getInstance().delete(pkgName);
    }

    private void downloadCountChange() {
        GameListenerManager.onEvent(GameListenerManager.DOWNLOAD_COUNT_CHANGE);
    }

    /**
     * 静默下载的apk不展示在列表上
     */
    private void onOrderChange() {
        if (mIsSilent) {
            return;
        }

        DownloadOrderMgr.onOrderChange(mDownloadInfoMap);
        downloadCountChange();
    }

    public void orderChange() {
        onOrderChange();
        mDownloadManager.notifyChange();
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
        mDownloadManager.notifyChange();
        if (updateDb) {
            DownloadDB.getInstance().update(info);
        }
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
