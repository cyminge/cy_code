package com.cy.frame.downloader.controller;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.widget.Toast;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.frame.downloader.core.DownloadStatusMgr;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.downloadmanager.DownloadDB;
import com.cy.frame.downloader.install.InstallManager;
import com.cy.frame.downloader.statis.StatisValue;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager.UpgradeAppInfo;
import com.cy.frame.downloader.util.GameInstaller;
import com.cy.utils.Utils;
import com.cy.utils.storage.GNStorageUtils;

public abstract class StartDownloadManager {

    public interface InstallListener {
        public void onInstalling(DownloadArgs downloadArgs);
    }

    protected Context mContext;
    private int mDelayTime = 0;

    /**
     * 是否需要下载
     * @return
     */
    protected abstract boolean isDownloadable();
    protected abstract boolean confirmDownload();
    protected abstract void onResetDownload();
    protected abstract void startDownload();

    protected StartDownloadManager() {
    	this(0);
    }

    public StartDownloadManager(int delayTime) {
        mDelayTime = delayTime;
    }

    protected void execute() {
        check();
    }

    protected void check() {
        if (!isDownloadable()) {
            onResetDownload();
            return;
        }
        userConfirm();
    }

    private void userConfirm() {
        if (!confirmDownload()) {
            onResetDownload();
            return;
        }

        startDownload();
    }

    /**
     * 开始下载
     * @param downloadArgs
     * @return
     */
    protected long download(DownloadArgs downloadArgs) {
        if (!GNStorageUtils.isSDCardMounted()) {
            showLimitedToast(R.string.sdcard_error);
            return DownloadDB.NO_DOWN_ID;
        }
        
        if (DownloadOrderMgr.getDownloadCount() >= DownloadStatusMgr.MAX_DOWNLOAD_TASK) { // 下载任务数已经最大 ??
            showLimitedToast(R.string.max_download_task);
            return DownloadDB.NO_DOWN_ID;
        }
        
        String packageName = downloadArgs.packageName;

        long downId = DownloadDB.NO_DOWN_ID;
        DownloadInfo info = getDownloadInfo(packageName);
        try {
            if (null != info) {
                downId = info.mDownId;
            } else {
                Utils.delAllfiles(packageName);
                downloadArgs.mSource = resetDownloadSource(downloadArgs);
                downId = getDownloadStatusMgr().download(downloadArgs, mDelayTime);
            }
        } catch (Exception e) {
            Log.e("TAG", e.getLocalizedMessage(), e);
            showLimitedToast(R.string.download_manager_error);
        }
        return downId;
    }

    private String resetDownloadSource(DownloadArgs downloadArgs) {
//        if (!SettingUtils.getAllowByMobileNet() && Utils.isMobileNet()) {
//            return StatisValue.combine(downloadArgs.mSource, StatisValue.WLAN_AUTO);
//        }

        String source = downloadArgs.mSource;
        if(source == null) {
        	return "";
        }
        
        if (source.contains(StatisValue.SPLIT) || source.contains(StatisValue.UPDATE)) {
            return source;
        }

        return Utils.combineGameUpgradeSource(downloadArgs.packageName, downloadArgs.mSource);
    }

    protected DownloadStatusMgr getDownloadStatusMgr() {
        return DownloadStatusMgr.getNormalInstance();
    }
    
    protected DownloadInfoMgr getDownloadInfoMgr() {
        return getDownloadStatusMgr().getDownloadInfoMgr();
    }

    protected final DownloadInfo getDownloadInfo(String mPackageName) {
        return getDownloadInfoMgr().getDownloadInfo(mPackageName);
    }

    protected void showLimitedToast(int msgId) {
        Toast.makeText(mContext, msgId, Toast.LENGTH_SHORT).show();
    }

    protected void runInstall(DownloadArgs args, InstallListener listener) {
        InstallManager.addInstallingGame(args.packageName);
        if (Utils.permitSilentInstall(args.packageName)) {
            GameInstaller.silentInstall(mContext, args, args.packageName + Constant.APK, false);
            if (listener != null) {
                listener.onInstalling(args);
            }
        } else {
            popupInstall(args);
            onResetDownload();
        }
    }

    protected void popupInstall(final DownloadArgs args) {
        GameInstaller.popupInstall(mContext, args.packageName);
    }

    protected boolean hasLocalApk(DownloadArgs downloadArgs) {
        String gamePackage = downloadArgs.packageName;
        String fileName = gamePackage + Constant.APK;
        boolean hasAPK = Utils.isFileExisting(fileName) && Utils.isSamePackage(fileName, gamePackage);
        if (hasAPK) {
            UpgradeAppInfo upInfo = GamesUpgradeManager.getOneAppInfo(gamePackage);
            if (upInfo != null) {
                String filePath = GNStorageUtils.getHomeDirAbsolute() + File.separator + fileName;
                PackageInfo p = Utils.getPackageInfoByPath(filePath);
                if (p != null && p.versionCode < upInfo.mNewVersionCode) {
                    removeDownload(gamePackage);
                    Utils.delAllfiles(gamePackage);
                    return false;
                }
            }
        }
        return hasAPK;
    }

    protected void removeDownload(String gamePackage) {
        ButtonStatusManager.removeDownloaded(gamePackage);
        getDownloadInfoMgr().removeDownloadInfo(gamePackage);
    }

}
