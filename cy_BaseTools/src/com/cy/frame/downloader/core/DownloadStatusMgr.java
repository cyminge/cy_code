package com.cy.frame.downloader.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import android.widget.Toast;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.config.DownloadConfiguration;
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
import com.cy.global.WatchDog;
import com.cy.utils.Utils;
import com.cy.utils.storage.GNStorageUtils;

public class DownloadStatusMgr {

    public static final int TASK_STATUS_PENDING = 0x100;              // 等待中 
    public static final int TASK_STATUS_DOWNLOADING = 0x101;          // 下载中
    public static final int TASK_STATUS_PAUSED = 0x102;               // 已暂停
    public static final int TASK_STATUS_SUCCESSFUL = 0x103;           // 下载完成
    public static final int TASK_STATUS_FAILED = 0x104;               // 下载失败
    public static final int TASK_STATUS_DELETE = 0x105;               // 删除

    public static final int TASK_FAIL_REASON_NONE = -1;
    // fail reason
    public static final int TASK_FAIL_UNKNOWN = 0x300;                     //未知
    public static final int TASK_FAIL_HTTP_DATA_ERROR = 0x301;             // Http 数据异常
    public static final int TASK_FAIL_URL_ERROR = 0x302;                   // 下载地址有误
    public static final int TASK_FAIL_APK_ERROR = 0x303;                   // 文件有误
    public static final int TASK_FAIL_URL_UNREACHABLE = 0x304;             // 下载地址不可达
    public static final int TASK_FAIL_URL_UNRECOVERABLE = 0x305;           // 下载地址不可恢复
    public static final int TASK_FAIL_CONTENT_TYPE_ERROR = 0x306;          // 文件类型有误
    public static final int TASK_FAIL_GAME_NOT_EXIST = 0x308;              // 应用不存在
    public static final int TASK_FAIL_CANNOT_RESUME = 0x309;               // 任务不能继续

    // pause reason
    public static final int TASK_PAUSE_NO_NETWORK = 0x509;
    public static final int TASK_PAUSE_WAIT_WIFI = 0x510;
    public static final int TASK_PAUSE_BY_USER = 0x511;
    public static final int TASK_PAUSE_FILE_ERROR = 0x512;
    public static final int TASK_PAUSE_INSUFFICIENT_SPACE = 0x513;
    public static final int TASK_PAUSE_DEVICE_NOT_FOUND = 0x514;
    public static final int TASK_PAUSE_WIFI_INVALID = 0x515;
    public static final int TASK_PAUSE_XUNLEI_WAITING_TO_RETRY = 0x517;

    // resume reason
    public static final int TASK_RESUME_NETWORK_RECONNECT = 0x600;
    public static final int TASK_RESUME_BY_USER = 0x601;
    public static final int TASK_RESUME_DEVICE_RECOVER = 0x602;

    public static final int REASON_START = 0x700;
    public static final int USER_DELETED = 0x701;
    public static final int REASON_PENDING = 0x703;

    public static final int MAX_DOWNLOAD_TASK = 100; // 最大下载任务数
    private int mLastNewtorkType = Constant.NETWORK_NO_NET;

    private DownloadConfiguration mDownloadConfiguration;
    
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
//             sSilentInstance = new SilentDownloadStatusMgr(DownloadInfoMgr.getSilentInstance()); // cyminge modify
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
        mDownloadInfoMgr = downloadInfoMgr;
    }
    
    public DownloadInfoMgr getDownloadInfoMgr() {
        return mDownloadInfoMgr;
    }
    
    public static void init(DownloadConfiguration config) {
    	getNormalInstance().mDownloadConfiguration = config;
//    	getSilentInstance().mDownloadConfiguration = config; // cyminge modify
    }
    
    /**
     * 下载
     * 
     * @param downloadArgs
     * @param delayTime
     * @return
     */
    public long download(DownloadArgs downloadArgs, int delayTime) {
        if (!DownloadInfoMgr.isSynDB()) { // 如果下载服务没有初始化，则直接返回
            return DownloadDB.NO_DOWN_ID;
        }

        String packageName = downloadArgs.packageName;
        String localPath = null;
        if (GamesUpgradeManager.isIncreaseType(packageName)) {
        	localPath = File.separator+ packageName + ".patch" + Constant.TMP_FILE_EXT; // 增量升级
        } else {
            localPath = File.separator+ packageName + Constant.APK + Constant.TMP_FILE_EXT;
        }

        DownloadRequest request = new DownloadRequest(downloadArgs);
        request.mAllowByMobileNet = mDownloadConfiguration.isAllowByMobileNet();
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
        DownloadInfo info = DownloadInfoMgr.getDownloadInfoInAll(request.packageName);
        info = handlerExistInfo(info);
        if (null != info) {
            return info.mDownId;
        }
        
        info = new DownloadInfo(request);
        long id = DownloadDB.getInstance().insert(info); // 插入数据库
        if (DownloadDB.NO_DOWN_ID == id) {
            return DownloadDB.NO_DOWN_ID;
        }
        
        info.mDownId = id;
        mDownloadInfoMgr.addDownloadInfo(info);
        GameActionUtil.postGameAction(info.packageName, Constant.ACTION_START_DOWNLOAD); // 下载应用，通知栏提示
        delayAddInfoForAnimation(info, delayTime);
        return id;
    }

    private void delayAddInfoForAnimation(final DownloadInfo info, int delayTime) {
//        if (delayTime <= 0) {
//            enqueue(info);
//            return;
//        }
        WatchDog.postDelayed(new Runnable() {
            @Override
            public void run() {
                enqueue(info);
            }

        }, delayTime);
    }

    private void enqueue(DownloadInfo info) {
        if (Utils.isMobileNet() && (!info.mAllowByMobileNet || info.mWifiAutoDownload)) { // 移动网络下不让下载，只挂起任务
            pauseDownloadTask(info.packageName, TASK_PAUSE_WAIT_WIFI, false);
        } else {
            resumeDownloadTask(info.packageName, REASON_START);
        }
    }

    /**
     * 是否已有该任务
     * @param info
     * @return
     */
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
            switchSingleTaskStatus(pkgName, TASK_STATUS_FAILED, TASK_FAIL_APK_ERROR);
            mDownloadInfoMgr.orderChange();
        }
    }

    public void onDeleteTask(DownloadInfo info) {
        int oldStatus = info.mStatus;
        info.mStatus = TASK_STATUS_DELETE;
        ButtonStatusManager.removeDownloaded(info.packageName);
        mDownloadInfoMgr.removeDownloadInfo(info.packageName);
        Utils.delAllfiles(info.packageName);
        if (oldStatus == DownloadStatusMgr.TASK_STATUS_DOWNLOADING) {
            switchPendingToDownload();
        }
    }

    public void switchPendingToDownload() {
        ArrayList<String> packageList = getSortPkgList();
        int downloadingCount = getDownloadingCount();
        if (downloadingCount >= mDownloadConfiguration.getMaxDownloadingTask()) {
            return;
        }

        for (String pkg : packageList) {
            DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
            if (downloadingCount >= mDownloadConfiguration.getMaxDownloadingTask()) {
                return;
            }

            if (info.mStatus == TASK_STATUS_PENDING) {
                switchSingleTaskStatus(pkg, TASK_STATUS_DOWNLOADING, TASK_FAIL_REASON_NONE);
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

         if (reason != REASON_START) { // 重新下载 统计 cyminge modify
//        	 DownloadUtils.sendResumeStatis(pkgName, reason);
         }

        int targetStatus = TASK_STATUS_DOWNLOADING;
        if (getDownloadingCount() >= mDownloadConfiguration.getMaxDownloadingTask()) {
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
            if (info.isDownloading()) {
            }
        }
    }

    public void onNetworkChanged() {
        int networkType = Utils.getNetworkType();
        if (networkType == Constant.NETWORK_NO_NET) {
            switchAllRunningTask(TASK_STATUS_PAUSED, TASK_PAUSE_NO_NETWORK);
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
        int reason = TASK_FAIL_REASON_NONE;
        // if (SettingUtils.getAllowByMobileNet()) { // cyminge modify
        reason = TASK_PAUSE_NO_NETWORK;
        // } else {
        // reason = PAUSE_WAIT_WIFI;
        // }

        switchAllRunningTask(TASK_STATUS_PAUSED, reason);
    }

    private void onWifiNetworkConnect() {
        ArrayList<String> packageList = getSortPkgList();
        for (String pkg : packageList) {
            DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
            if (info.mReason == TASK_PAUSE_NO_NETWORK || info.mReason == TASK_PAUSE_WAIT_WIFI) {
                resumeDownloadTask(info.packageName, TASK_RESUME_NETWORK_RECONNECT);
            }
        }
    }

    public void onMediaChanged(boolean isEject) {
        if (isEject && GNStorageUtils.isSDCardChange()) {
            switchAllRunningTask(TASK_STATUS_PAUSED, TASK_PAUSE_DEVICE_NOT_FOUND);
            showlimitedToast(R.string.device_not_found);
        } else {
            ArrayList<String> packageList = getSortPkgList();
            for (String pkg : packageList) {
                DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
                if (shouldResumeOnMediaChanged(info)) {
                    resumeDownloadTask(info.packageName, TASK_RESUME_DEVICE_RECOVER);
                }
            }
        }
    }

    public void onSdChanged() {
        ArrayList<String> packageList = getSortPkgList();
        for (String pkg : packageList) {
            DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
//            if (info.mIsXunlei) {
//                info.mXunleiNeedRedownload = true;
//                mDownloadInfoMgr.updateDownloadInfo(info, false);
//            }
        }
    }

    protected boolean shouldResumeOnMediaChanged(DownloadInfo info) {
        return info.mReason == TASK_PAUSE_DEVICE_NOT_FOUND;
    }

    private void switchAllRunningTask(int status, int reason) {
        Set<Entry<String, DownloadInfo>> entrySet = mDownloadInfoMgr.getDownloadSet();
        for (Entry<String, DownloadInfo> entry : entrySet) {
            DownloadInfo info = entry.getValue();
            if (info.isRunning()) {
                String pkgName = info.packageName;
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
            switchAllRunningTask(TASK_STATUS_PAUSED, TASK_PAUSE_WAIT_WIFI);
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
            DownloadService.removeTask(info, targetStatus); // ?? 这里是个什么鬼
        }
        info.mLastStatus = info.mStatus;

        info.mStatus = targetStatus;
        info.mReason = reason;
        switch (targetStatus) {
        case TASK_STATUS_DOWNLOADING:
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

    protected void showToastByReason(int reason) {
        DownloadUtils.toast(reason);
    }

    protected DownloadRunnable createDownloadRunnable(DownloadInfo info) {
        return new DownloadRunnable(info);
    }
}
