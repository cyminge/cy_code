package com.cy.frame.downloader.controller;

import java.io.File;

import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.downloadmanager.DownloadDB;
import com.cy.frame.downloader.install.InstallManager;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager.UpgradeAppInfo;
import com.cy.global.BaseApplication;
import com.cy.global.WatchDog;
import com.cy.utils.Utils;
import com.cy.utils.storage.GNStorageUtils;

/**
 * 单个文件下载管理器
 * @author JLB6088
 *
 */
public class SingleDownloadManager extends StartDownloadManager {

    private static final int LOCALAPK_CHECK_OK = 101;
    private static final int LOCALAPK_CHECK_SIGNFAIL = 102;

    private DownloadArgs mDownloadArgs;
    private SingleDownloadListener mListener;

    /**
     * 单个文件下载回调
     * @author zf
     *
     */
    public interface SingleDownloadListener extends InstallListener {
        public void onStartDownload(Long downId, DownloadArgs downloadArgs);
        public void onResetDownload(DownloadArgs downloadArgs);
    }

    public SingleDownloadManager() {
        super();
    }

    public SingleDownloadManager(int delayTime) {
        super(delayTime);
    }

    private class SignCheckTask extends AsyncTask<Object, Object, Integer> {

        @Override
        protected Integer doInBackground(Object... params) {
            String gamePackage = mDownloadArgs.packageName;
            if (Utils.permitSilentInstall(gamePackage)) {
                InstallManager.addInstallingGame(gamePackage);
            }

            if (checkSignature(gamePackage)) {
                return LOCALAPK_CHECK_OK;
            } else {
                return LOCALAPK_CHECK_SIGNFAIL;
            }
        }

        @Override
        protected void onPostExecute(Integer it) {
            switch (it.intValue()) {
                case LOCALAPK_CHECK_OK:
                    runInstall(mDownloadArgs, mListener);
                    break;
                case LOCALAPK_CHECK_SIGNFAIL:
                    InstallManager.removeInstallingGame(mDownloadArgs.packageName);
                    Toast.makeText(mContext, R.string.nosame_sign, Toast.LENGTH_SHORT).show();
                    onResetDownload();
                    break;
                default:
                    break;
            }

        }
    }

    public void execute(DownloadArgs downloadArgs, SingleDownloadListener listener, boolean isApplyFail) {
        mDownloadArgs = downloadArgs;
        mListener = listener;
        mContext = WatchDog.INSTANCE.getTopActivity();
        if (isApplyFail) { // 提示错误
            applyFail(mDownloadArgs);
            return;
        }

        if (InstallManager.isInstalling(mDownloadArgs.packageName)) { // 如果正在安装
            return;
        }

        if (shouldCheckSign()) { 
            new SignCheckTask().execute();
            return;
        }

        super.execute();
    }

    /**
     * 检查是否有新版本的签名
     * @return
     */
    private boolean shouldCheckSign() {
        UpgradeAppInfo upgradeAppInfo = GamesUpgradeManager.getOneAppInfo(mDownloadArgs.packageName);
        return GamesUpgradeManager.hasNewVersion(mDownloadArgs.packageName) && hasLocalUpgradeApk(upgradeAppInfo);
    }

    /**
     * 检查本地是否有已经下载好的apk
     * @param info
     * @return
     */
    private boolean hasLocalUpgradeApk(UpgradeAppInfo info) {
        String path = GNStorageUtils.getHomeDirAbsolute() + File.separator + info.packageName + Constant.APK;
        PackageInfo p = Utils.getPackageInfoByPath(path);
        if (p != null && p.packageName.equals(info.packageName)) {
            if (p.versionCode >= info.mNewVersionCode) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查签名
     * @param packageName
     * @return
     */
    private boolean checkSignature(String packageName) {
        String localPath = GNStorageUtils.getHomeDirAbsolute() + File.separator + packageName + Constant.APK;
        return GamesUpgradeManager.isSameSignature(packageName, localPath);
    }

    /**
     * 检查是否需要下载
     */
    @Override
    protected boolean isDownloadable() {

        if (hasLocalApk(mDownloadArgs)) { // 本地是否有apk
            ButtonStatusManager.addDownloaded(mDownloadArgs.packageName);
            runInstall(mDownloadArgs, mListener);
            return false;
        }

        if (mDownloadArgs.isInvalid()) { // 下载地址不正确
            Toast.makeText(mContext, R.string.down_url_error, Toast.LENGTH_SHORT).show();
            return false;
        }

        return checkNetWork() && checkSDCard(mDownloadArgs);
    }

    private static boolean checkSDCard(DownloadArgs args) {
        if (args == null) {
            return false;
        }

        UpgradeAppInfo upgradeAppInfo = GamesUpgradeManager.getOneAppInfo(args.packageName);
        if (upgradeAppInfo != null && upgradeAppInfo.mIsIncreaseType) {
            return checkSDCard(upgradeAppInfo.mPatchSize, args.packageName);
        } else {
            return checkSDCard(args.size, args.packageName);
        }
    }

    private static boolean checkSDCard(String fileSize, String packageName) {
        switch (Utils.getSDCardState(fileSize)) {
            case Constant.SD_NOT_MOUNTED:
                Toast.makeText(BaseApplication.getAppContext(), R.string.sdcard_error, Toast.LENGTH_SHORT).show();
                return false;
            case Constant.SD_LOW_SPACE:
                Toast.makeText(BaseApplication.getAppContext(), "磁盘空间不足", Toast.LENGTH_SHORT).show();
                return false;
            case Constant.SD_ENOUGH_ROOM:
            default:
                return true;
        }
    }

    @Override
    protected boolean confirmDownload() {
        return isSysMatched(mDownloadArgs);
    }

    @Override
    protected void onResetDownload() {
    	mListener.onResetDownload(mDownloadArgs);
    }

    /**
     * 开始下载
     */
    @Override
    protected void startDownload() {
        if (Utils.needShowMobileHint()) {
//            BgDialogActivity.show(BgDialogActivity.DIALOG_MOBILE_HINT); // cyminge modify
        }
        long downId = download(mDownloadArgs); // 这里开始下载
        if (DownloadDB.NO_DOWN_ID == downId) {
        	mListener.onResetDownload(mDownloadArgs);
        } else {
            mListener.onStartDownload(downId, mDownloadArgs);
        }
    }

}
