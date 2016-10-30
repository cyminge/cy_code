package com.cy.frame.downloader.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import android.widget.Toast;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.controller.DownloadOrderMgr;
import com.cy.frame.downloader.download.DownloadUtils;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.download.entity.DownloadRequest;
import com.cy.frame.downloader.downloadmanager.DownloadDB;
import com.cy.frame.downloader.downloadmanager.DownloadService;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager;
import com.cy.frame.downloader.util.GameActionUtil;
import com.cy.global.BaseApplication;
import com.cy.threadpool.NomalThreadPool;
import com.cy.utils.Utils;
import com.cy.utils.storage.GNStorageUtils;

public class DownloadStatusMgr {

    public static final int TASK_STATUS_PENDING = 0x100;
    public static final int TASK_STATUS_DOWNLOADING = 0x101;
    public static final int TASK_STATUS_PAUSED = 0x102;
    public static final int TASK_STATUS_SUCCESSFUL = 0x103;
    public static final int TASK_STATUS_FAILED = 0x104;
    public static final int TASK_STATUS_DELETE = 0x105;

    public static final int REASON_NONE = -1;
    // fail reason
    public static final int FAIL_UNKNOWN = 0x300;
    public static final int FAIL_HTTP_DATA_ERROR = 0x301;
    public static final int FAIL_URL_ERROR = 0x302;
    public static final int FAIL_APK_ERROR = 0x303;
    public static final int FAIL_URL_UNREACHABLE = 0x304;
    public static final int FAIL_URL_UNRECOVERABLE = 0x305;
    public static final int FAIL_CONTENT_TYPE_ERROR = 0x306;
    public static final int FAIL_GAME_NOT_EXIST = 0x308;
    public static final int FAIL_CANNOT_RESUME = 0x309;

    // pause reason
    public static final int PAUSE_NO_NETWORK = 0x509;
    public static final int PAUSE_WAIT_WIFI = 0x510;
    public static final int PAUSE_BY_USER = 0x511;
    public static final int PAUSE_FILE_ERROR = 0x512;
    public static final int PAUSE_INSUFFICIENT_SPACE = 0x513;
    public static final int PAUSE_DEVICE_NOT_FOUND = 0x514;
    public static final int PAUSE_WIFI_INVALID = 0x515;
    public static final int PAUSE_XUNLEI_WAITING_TO_RETRY = 0x517;

    // resume reason
    public static final int RESUME_NETWORK_RECONNECT = 0x600;
    public static final int RESUME_BY_USER = 0x601;
    public static final int RESUME_DEVICE_RECOVER = 0x602;

    public static final int REASON_START = 0x700;
    public static final int USER_DELETED = 0x701;
    public static final int REASON_PENDING = 0x703;

    public static final int MAX_DOWNLOADING_TASK = 2; // 最大正在下载任务数
    public static final int MAX_DOWNLOAD_TASK = 100; // 最大下载任务数
    private int mLastNewtorkType = Constant.NETWORK_NO_NET;

    private static DownloadStatusMgr sSilentInstance;
    private static DownloadStatusMgr sNormalInstance;

    protected DownloadInfoMgr mDownloadInfoMgr;

    public static DownloadStatusMgr getNormalInstance() {
        if (sNormalInstance == null) {
            sNormalInstance = new DownloadStatusMgr(DownloadInfoMgr.getNormalInstance());
        }

        return sNormalInstance;
    }

    public static DownloadStatusMgr getSilentInstance() {
        if (sSilentInstance == null) {
            // sSilentInstance = new
            // SilentDownloadStatusMgr(DownloadInfoMgr.getSilentInstance()); //
            // cyminge modify
        }
        return sSilentInstance;
    }

    public static DownloadStatusMgr getInstance(DownloadInfo info) {
        if (info.mIsSilentDownload) {
            return getSilentInstance();
        } else {
            return getNormalInstance();
        }
    }

    protected DownloadStatusMgr(DownloadInfoMgr downloadInfoMgr) {
        this.mDownloadInfoMgr = downloadInfoMgr;
    }

    /**
     * 下载
     * 
     * @param downloadArgs
     * @param delayTime
     * @return
     */
    public long download(DownloadArgs downloadArgs, int delayTime) {
        if (!DownloadInfoMgr.isSynDB()) { // 这是个什么东西 ??
            return DownloadDB.NO_DOWN_ID;
        }

        String packageName = downloadArgs.mPackageName;
        String localPath = File.separator + packageName;
        if (GamesUpgradeManager.isIncreaseType(downloadArgs.mPackageName)) {
            localPath = localPath + ".patch" + Constant.TMP_FILE_EXT;
        } else {
            localPath = localPath + Constant.APK + Constant.TMP_FILE_EXT;
        }

        DownloadRequest request = new DownloadRequest(downloadArgs);
        // request.mAllowByMobileNet = SettingUtils.getAllowByMobileNet(); //
        // 是否允许移动网络下载 cyminge modify
        request.mAllowByMobileNet = true;
        request.mFilePath = localPath;
        return enqueue(request, delayTime);
    }

    /**
     * 入队列下载
     * 
     * @param request
     * @param delayTime
     * @return
     */
    protected long enqueue(DownloadRequest request, int delayTime) {
        DownloadInfo info = DownloadInfoMgr.getDownloadInfoInAll(request.mPackageName);
        info = handlerExistInfo(info);
        if (null != info) {
            return info.mDownId;
        }
        info = new DownloadInfo(request);

        long id = DownloadDB.NO_DOWN_ID;
        // if (ServerConfig.isXunleiEnable()) { // cyminge modify
        // info.mIsXunlei = true;
        // id = xunleiDownload(info);
        // } else {
        // id = DownloadDB.getInstance().insert(info);
        // }
        id = DownloadDB.getInstance().insert(info); // 插入数据库

        if (DownloadDB.NO_DOWN_ID != id) {
            info.mDownId = id;
            mDownloadInfoMgr.addDownloadInfo(info);
            delayAddInfoForAnimation(info, delayTime);
            GameActionUtil.postGameAction(info.mPackageName, Constant.ACTION_START_DOWNLOAD); // 什么东东???
        }
        return id;
    }

    private long xunleiDownload(DownloadInfo info) {
        // try {
        // return XunleiManager.getInstance().download(info);
        // } catch (Exception e) {
        // Log.e("TAG", e.getLocalizedMessage(), e);
        // info.mIsXunlei = false;
        // return DownloadDB.getInstance().insert(info);
        // }
        return DownloadDB.getInstance().insert(info);
    }

    private void delayAddInfoForAnimation(final DownloadInfo info, int delayTime) {
        if (delayTime <= 0) {
            enqueue(info);
            return;
        }

        NomalThreadPool.getInstance().postDelayed(new Runnable() {
            @Override
            public void run() {
                enqueue(info);
            }

        }, delayTime);
    }

    private void enqueue(DownloadInfo info) {
        if (Utils.isMobileNet() && (!info.mAllowByMobileNet || info.mAutoDownload)) {
            pauseDownloadTask(info.mPackageName, PAUSE_WAIT_WIFI, false);
        } else {
            resumeDownloadTask(info.mPackageName, REASON_START);
        }
    }

    protected DownloadInfo handlerExistInfo(DownloadInfo info) {
        if (null != info && info.mIsSilentDownload) {
            getSilentInstance().onDeleteTask(info);
            return null;
        }
        return info;
    }

    public void pauseDownloadTask(String pkgName, int reason, boolean isDownloading) {
        switchSingleTaskStatus(pkgName, TASK_STATUS_PAUSED, reason);
        if (isDownloading) {
            switchPendingToDownload();
        }

    }

    public void onDownloadingExit(String pkgName, int targetStatus, int reason) {
        if (mDownloadInfoMgr.hasDownloadInfo(pkgName)) {
            switchSingleTaskStatus(pkgName, targetStatus, reason);
            switchPendingToDownload();
        }
    }

    public void onSilentInstallingError(String pkgName) {
        if (mDownloadInfoMgr.hasDownloadInfo(pkgName)) {
            switchSingleTaskStatus(pkgName, TASK_STATUS_FAILED, FAIL_APK_ERROR);
            mDownloadInfoMgr.orderChange();
        }
    }

    public void onDeleteTask(DownloadInfo info) {
        int oldStatus = info.mStatus;
        info.mStatus = TASK_STATUS_DELETE;
        ButtonStatusManager.removeDownloaded(info.mPackageName);
        mDownloadInfoMgr.removeDownloadInfo(info.mPackageName);
        Utils.delAllfiles(info.mPackageName);
        if (oldStatus == DownloadStatusMgr.TASK_STATUS_DOWNLOADING) {
            switchPendingToDownload();
        }
    }

    public void switchPendingToDownload() {
        ArrayList<String> packageList = getSortPkgList();
        int downloadingCount = getDownloadingCount();
        if (downloadingCount >= MAX_DOWNLOADING_TASK) {
            return;
        }

        for (String pkg : packageList) {
            DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
            if (downloadingCount >= MAX_DOWNLOADING_TASK) {
                return;
            }

            if (info.mStatus == TASK_STATUS_PENDING) {
                switchSingleTaskStatus(pkg, TASK_STATUS_DOWNLOADING, REASON_NONE);
                downloadingCount++;
            }
        }
    }

    protected ArrayList<String> getSortPkgList() {
        return DownloadOrderMgr.getSortPkgList();
    }

    private int getDownloadingCount() {
        int downloadingCount = 0;
        Set<Entry<String, DownloadInfo>> entrySet = mDownloadInfoMgr.getDownloadSet();
        for (Entry<String, DownloadInfo> entry : entrySet) {
            DownloadInfo info = entry.getValue();
            if (info.isDownloading()) {
                downloadingCount++;
            }
        }

        return downloadingCount;
    }

    public void resumeDownloadTask(String pkgName, int reason) {
        if (!Utils.hasNetwork()) {
            showlimitedToast(R.string.no_net_msg);
            return;
        }

        // if (reason != REASON_START) { // 重新下载统计 cyminge modify
        // DownloadUtils.sendResumeStatis(pkgName, reason);
        // }

        int targetStatus = TASK_STATUS_DOWNLOADING;
        if (getDownloadingCount() >= MAX_DOWNLOADING_TASK) {
            targetStatus = TASK_STATUS_PENDING;
            reason = REASON_PENDING;
        }

        switchSingleTaskStatus(pkgName, targetStatus, reason);
    }

    protected void showlimitedToast(int msgId) {
        Toast.makeText(BaseApplication.getAppContext(), msgId, Toast.LENGTH_SHORT).show();
    }

    public void onSameNetworkTypeChanged() {
        ArrayList<String> packageList = getSortPkgList();
        for (String pkg : packageList) {
            DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
            if (info.isDownloading() && info.mIsXunlei) {
            }
        }
    }

    public void onNetworkChanged() {
        int networkType = Utils.getNetworkType();
        if (networkType == Constant.NETWORK_NO_NET) {
            switchAllRunningTask(TASK_STATUS_PAUSED, PAUSE_NO_NETWORK);
            if (DownloadOrderMgr.hasDownload()) {
                Toast.makeText(BaseApplication.getAppContext(), R.string.network_off, Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (mLastNewtorkType != networkType) {
            if (Utils.isMobileNet(networkType)) {
                onMobileNetworkConnect();
            } else if (Utils.isWifiNet(networkType)) {
                onWifiNetworkConnect();
            }
        }
        mLastNewtorkType = networkType;
    }

    private void onMobileNetworkConnect() {
        int reason = REASON_NONE;
        // if (SettingUtils.getAllowByMobileNet()) { // cyminge modify
        reason = PAUSE_NO_NETWORK;
        // } else {
        // reason = PAUSE_WAIT_WIFI;
        // }

        switchAllRunningTask(TASK_STATUS_PAUSED, reason);
    }

    private void onWifiNetworkConnect() {
        ArrayList<String> packageList = getSortPkgList();
        for (String pkg : packageList) {
            DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
            if (info.mReason == PAUSE_NO_NETWORK || info.mReason == PAUSE_WAIT_WIFI) {
                resumeDownloadTask(info.mPackageName, RESUME_NETWORK_RECONNECT);
            }
        }
    }

    public void onMediaChanged(boolean isEject) {
        if (isEject && GNStorageUtils.isSDCardChange()) {
            switchAllRunningTask(TASK_STATUS_PAUSED, PAUSE_DEVICE_NOT_FOUND);
            showlimitedToast(R.string.device_not_found);
        } else {
            ArrayList<String> packageList = getSortPkgList();
            for (String pkg : packageList) {
                DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
                if (shouldResumeOnMediaChanged(info)) {
                    resumeDownloadTask(info.mPackageName, RESUME_DEVICE_RECOVER);
                }
            }
        }
    }

    public void onSdChanged() {
        ArrayList<String> packageList = getSortPkgList();
        for (String pkg : packageList) {
            DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
            if (info.mIsXunlei) {
                info.mXunleiNeedRedownload = true;
                mDownloadInfoMgr.updateDownloadInfo(info, false);
            }
        }
    }

    protected boolean shouldResumeOnMediaChanged(DownloadInfo info) {
        return info.mReason == PAUSE_DEVICE_NOT_FOUND;
    }

    private void switchAllRunningTask(int status, int reason) {
        Set<Entry<String, DownloadInfo>> entrySet = mDownloadInfoMgr.getDownloadSet();
        for (Entry<String, DownloadInfo> entry : entrySet) {
            DownloadInfo info = entry.getValue();
            if (info.isRunning()) {
                String pkgName = info.mPackageName;
                info.mNeedUpdateDB = true;
                DownloadInfo targetInfo = switchTaskStatus(pkgName, status, reason);
                mDownloadInfoMgr.updateDownloadInfo(targetInfo, false);
            }
        }

        if (!entrySet.isEmpty()) {
            mDownloadInfoMgr.syncToDB();
        }
    }

    public boolean isDownloading(String pkgName) {
        DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkgName);
        return info != null && info.isDownloading();
    }

    public void onAllowByMobileNetChanged(boolean enable) {
        ArrayList<String> packageList = getSortPkgList();
        for (String pkg : packageList) {
            DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
            if (info != null && !info.isCompleted()) {
                info.mAllowByMobileNet = enable;
                info.mNeedUpdateDB = true;
            }
        }

        if (!packageList.isEmpty()) {
            mDownloadInfoMgr.syncToDB();
        }

        if (Utils.isMobileNet() && !enable) {
            switchAllRunningTask(TASK_STATUS_PAUSED, PAUSE_WAIT_WIFI);
        }
    }

    protected void switchSingleTaskStatus(String pkgName, int targetStatus, int reason) {
        DownloadInfo info = switchTaskStatus(pkgName, targetStatus, reason);
        if (info != null) {
            mDownloadInfoMgr.updateDownloadInfo(info);
        }
    }

    private DownloadInfo switchTaskStatus(String pkgName, int targetStatus, int reason) {
        DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkgName);
        if (info == null || (info.mStatus == targetStatus && info.mReason == reason)) {
            return info;
        }
        if (info.mStatus == TASK_STATUS_DOWNLOADING) {
            DownloadService.removeTask(info, targetStatus);
        }
        info.mLastStatus = info.mStatus;

        info.mStatus = targetStatus;
        info.mReason = reason;
        switch (targetStatus) {
        case TASK_STATUS_DOWNLOADING:
            checkNeedRedownload(info);
            DownloadRunnable task = createDownloadRunnable(info);
            DownloadService.postTask(pkgName, task);
            break;
        case TASK_STATUS_PAUSED:
        case TASK_STATUS_FAILED:
            showToastByReason(reason);
            break;
        case TASK_STATUS_SUCCESSFUL:
            info.setCompleteTime(System.currentTimeMillis());
            break;
        default:
            break;
        }
        return info;
    }

    private void checkNeedRedownload(DownloadInfo info) {
        if (info.mXunleiNeedRedownload) {
            // XunleiManager.getInstance().delete(info.mDownId);
            // long newId = XunleiManager.getInstance().download(info);
            // info.mDownId = newId;
            // info.mXunleiNeedRedownload = false;
        }
    }

    protected void showToastByReason(int reason) {
        DownloadUtils.toast(reason);
    }

    protected DownloadRunnable createDownloadRunnable(DownloadInfo info) {
        return new DownloadRunnable(info);
    }
}
