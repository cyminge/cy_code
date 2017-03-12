package com.cy.frame.downloader.core;

import java.util.ArrayList;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.download.entity.DownloadRequest;
import com.cy.frame.downloader.statis.StatisValue;
import com.cy.utils.Utils;

public class SilentDownloadStatusMgr extends DownloadManager {

    public SilentDownloadStatusMgr(boolean isSilent) {
        super(isSilent);
    }

    @Override
    protected long enqueue(DownloadRequest request, int delayTime) {
        request.mAllowByMobileNet = false;
        request.mIsSilentDownload = true;
        return super.enqueue(request, delayTime);
    }

    @Override
    protected ArrayList<String> getSortPkgList() {
        return mDownloadInfoMgr.getAllDownloadPkg();
    }

    @Override
    protected void showlimitedToast(int msgId) {
    }

    @Override
    protected void showToastByReason(int reason) {
    }

    @Override
    public void onAllowByMobileNetChanged(boolean enable) {
    }

    @Override
    protected DownloadRunnable createDownloadRunnable(DownloadInfo info) {
//        return new SilentDownloadRunnable(info);
    	return null;
    }

    @Override
    protected boolean shouldResumeOnMediaChanged(DownloadInfo info) {
//        return super.shouldResumeOnMediaChanged(info) || PAUSE_FILE_ERROR == info.mReason;
    	return false;
    }

    @Override
    public void onSilentInstallingError(String pkgName) {
        super.onSilentInstallingError(pkgName);
        String source = StatisValue.WASH_INSTALL_FAIL_UNKNOWN;
        PackageInfo oldPkgInfo = Utils.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
        PackageInfo filePkgInfo = getApkFilePkgInfo(pkgName);

        if (null == oldPkgInfo) {
            source = StatisValue.WASH_INSTALL_FAIL_UNINSTALLED;
        } else if (null == filePkgInfo) {
            source = StatisValue.WASH_INSTALL_FAIL_FILE;
        } else if (filePkgInfo.versionCode < oldPkgInfo.versionCode) {
            source = StatisValue.WASH_INSTALL_FAIL_VERSION;
        } else if (isSignDiffer(oldPkgInfo, filePkgInfo)) {
            source = StatisValue.WASH_INSTALL_FAIL_SIGNATURE;
        }
//        DownloadUtils.sendStatis(StatisValue.WASH_INSTALL_FAIL, pkgName, source);
    }

    private PackageInfo getApkFilePkgInfo(String pkgName) {
//        String filePath = StorageUtils.getHomeDirAbsolute() + File.separator + pkgName + Constant.APK;
//        return Utils.getPackageInfoByPath(filePath, PackageManager.GET_SIGNATURES);
    	return null;
    }

    private boolean isSignDiffer(PackageInfo oldPkgInfo, PackageInfo filePkgInfo) {
//        String oldSignMd5 = WashUtils.getSignMd5(oldPkgInfo);
//        String fileSignMd5 = WashUtils.getSignMd5(filePkgInfo);
//        return !oldSignMd5.equals(fileSignMd5);
    	return false;
    }

}
