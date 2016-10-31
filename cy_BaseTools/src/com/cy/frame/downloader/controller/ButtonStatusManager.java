package com.cy.frame.downloader.controller;

import java.util.ArrayList;

import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.frame.downloader.core.DownloadStatusMgr;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.entity.ListData;
import com.cy.frame.downloader.install.InstallManager;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager;
import com.cy.global.WatchDog;
import com.cy.utils.Utils;

public class ButtonStatusManager {
    public static final int BUTTON_STATUS_DOWNLOAD = 0x0;
    public static final int BUTTON_STATUS_RUNNING = 0x1;
    public static final int BUTTON_STATUS_UPGRADE = 0x2;
    public static final int BUTTON_STATUS_OPEN = 0x3;
    public static final int BUTTON_STATUS_INSTALLING = 0x4;
    public static final int BUTTON_STATUS_DISABLE = 0x5;
    public static final int BUTTON_STATUS_PAUSE = 0x6;
    public static final int BUTTON_STATUS_INSTALL = 0x7;
    public static final int BUTTON_STATUS_FAILED = 0x8;
    public static final int BUTTON_STATUS_REWARD_DOWNLOAD = 0x9;
    public static final int BUTTON_STATUS_REWARD_UPGRADE = 0xa;
    public static final int BUTTON_STATUS_GAME_SUBSCRIBE = 0xb;
    public static final int BUTTON_STATUS_GAME_SUBSCRIBED = 0xc;
    public static final int BUTTON_STATUS_OPEN_TEST_DOWNLOAD = 0xd;
    private static final float MAX_PROGRESS = 0.99f;
    private static final int FLAG_OPEN_TEST_TEST = 1 << 3;

    private static ArrayList<String> sInstalledApp = new ArrayList<String>();
    private static ArrayList<String> sDownloadedAPK = new ArrayList<String>();

    static {
        initDownloadedAPK(); // 初始化已下载到的apk
        initInstalledApp(); // 初始化所有已安装的应用
    }

    /**
     * 获取按钮状态
     * @param args
     * @return
     */
    public static synchronized int getButtonStatus(DownloadArgs args) {
        int status = getButtonStatus(args.mPackageName);
        return convertStatus(args, status);
    }

    /**
     * 获取按钮状态
     * @param pkgName
     * @return
     */
    public static synchronized int getButtonStatus(String pkgName) {
        DownloadInfo info = getDownloadInfoMgr().getDownloadInfo(pkgName);
        if (info != null) {
            if (info.isRunning()) {
                return BUTTON_STATUS_RUNNING;
            }
            if (isFailed(info.mStatus)) {
                return BUTTON_STATUS_FAILED;
            }

            if (isPause(info.mStatus)) {
                return BUTTON_STATUS_PAUSE;
            }
        }

        if (InstallManager.isInstalling(pkgName)) {
            return BUTTON_STATUS_INSTALLING;
        }

        if (GamesUpgradeManager.isUpgrade(pkgName)) {
            return BUTTON_STATUS_UPGRADE;
        }

        if (Utils.isSameClient(pkgName)) {
            return BUTTON_STATUS_DISABLE;
        }

        if (isInstalled(pkgName)) {
            return BUTTON_STATUS_OPEN;
        }

        if (isDownloaded(pkgName)) {
            return BUTTON_STATUS_INSTALL;
        }

        return BUTTON_STATUS_DOWNLOAD;
    }

    private static int convertStatus(DownloadArgs args, int status) {
        if (args.mRewardData != null) {
            if (BUTTON_STATUS_DOWNLOAD == status) {
                status = BUTTON_STATUS_REWARD_DOWNLOAD;
            } else if (BUTTON_STATUS_UPGRADE == status) {
                status = BUTTON_STATUS_REWARD_UPGRADE;
            }
        }
        return status;
    }

    /**
     * 测试环境下，有奖下载的状态都要转为下载状态
     * @param args
     * @param status
     * @return
     */
    public static boolean isOpenTest(DownloadArgs args, int status) {
        if ((BUTTON_STATUS_REWARD_DOWNLOAD == status || BUTTON_STATUS_REWARD_UPGRADE == status)
                && args.mRewardData != null) {
            int rewardStatisId = args.mRewardData.mRewardStatisId;
            if ((rewardStatisId & FLAG_OPEN_TEST_TEST) != 0) {
                return true;
            }
        }
        return false;
    }

    private static DownloadInfoMgr getDownloadInfoMgr() {
        return DownloadInfoMgr.getNormalInstance();
    }

    private static boolean isPause(int status) {
        return status == DownloadStatusMgr.TASK_STATUS_PAUSED;
    }

    private static boolean isFailed(int status) {
        return status == DownloadStatusMgr.TASK_STATUS_FAILED;
    }

    private static synchronized void initDownloadedAPK() {
        sDownloadedAPK.addAll(Utils.getDownloadedNames());
    }

    public static synchronized void addDownloaded(String pkgName) {
        sDownloadedAPK.add(pkgName);
    }

    public static synchronized void removeDownloaded(String pkgName) {
        sDownloadedAPK.remove(pkgName);
    }

    public static synchronized void removeAllDownloaded() {
        for (String pkgName : sDownloadedAPK) {
            getDownloadInfoMgr().removeDownloadInfo(pkgName);
        }
        sDownloadedAPK.clear();
    }

    public static synchronized boolean isDownloaded(String pkgName) {
        return sDownloadedAPK.contains(pkgName);
    }
    
    public static synchronized boolean isInstalled(String pkgName) {
        return sInstalledApp.contains(pkgName);
    }

    public static synchronized void initInstalledApp() {
        sInstalledApp.addAll(Utils.getPackageNames());
        addPackage(Utils.getClientPkg());
    }

    

    /**
     * 应用状态有更新
     * @param pkgName
     * @param isRemoved
     */
    public static void onInstalledChange(String pkgName, boolean isRemoved) {
        if (isRemoved) {
            removePackage(pkgName);
        } else {
            addPackage(pkgName);
        }

        int size = WatchDog.INSTANCE.getSize();
        if (size <= 0) {
            return;
        }
        getDownloadInfoMgr().notifyChange();
    }

    private static synchronized void addPackage(String pkgName) {
        if (isInstalled(pkgName)) {
            return;
        }

        sInstalledApp.add(pkgName);
    }

    private static synchronized void removePackage(String pkgName) {
        if (Utils.getPackageInfoByName(pkgName) != null) {
            return;
        }
        sInstalledApp.remove(pkgName);
    }

    /**
     * 获取下载进度
     * @param data
     * @return
     */
    public static float getProgress(ListData data) {
        return getProgress(data.mStatus, data);
    }

    /**
     * 获取下载进度
     * @param status
     * @param args
     * @return
     */
    public static float getProgress(int status, DownloadArgs args) {
        switch (status) {
            case BUTTON_STATUS_RUNNING:
            case BUTTON_STATUS_PAUSE:
            case BUTTON_STATUS_FAILED:
                DownloadInfo info = DownloadInfoMgr.getNormalInstance().getDownloadInfo(args.mPackageName);
                if (info == null) {
                    return 0;
                }
                long total;
                if (info.mTotalSize > 0) {
                    total = info.mTotalSize;
                } else {
                    total = Utils.getPkgTotalByte(args.mPackageName, args.mGameSize);
                }
                float progress = ((float) info.mProgress) / total;
                progress = Math.max(0, progress);
                progress = Math.min(MAX_PROGRESS, progress);
                return progress;
            default:
                return 0;
        }
    }

}

