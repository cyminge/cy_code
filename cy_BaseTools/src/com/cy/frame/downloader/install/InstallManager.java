package com.cy.frame.downloader.install;

import java.util.concurrent.ConcurrentSkipListSet;

import android.annotation.SuppressLint;

import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.utils.Utils;

@SuppressLint("NewApi") 
public class InstallManager {
    private static ConcurrentSkipListSet<String> sInstallingSet = new ConcurrentSkipListSet<String>();

    public static void addInstallingGame(String gamePackage) {
        sInstallingSet.add(gamePackage);
        ButtonStatusManager.onInstalledChange(gamePackage, false);
    }

    public static void removeInstallingGame(String gamePackage) {
        sInstallingSet.remove(gamePackage);
        ButtonStatusManager.onInstalledChange(gamePackage, true);
    }

    public static boolean isInstalling(String gamePackage) {
        return sInstallingSet.contains(gamePackage);
    }

    public static void installedDone(String packageName) {
        String fileName = packageName + Constant.APK;
        if (Utils.isFileExisting(fileName)) {
//            GameActionUtil.postGameAction(packageName, Constant.ACTION_INSTALL); // cyminge modify

            DownloadInfoMgr downloadInfoMgr = getDownloadInfoMgr(packageName);
            if (null != downloadInfoMgr) {
                ButtonStatusManager.removeDownloaded(packageName);
                downloadInfoMgr.removeDownloadInfo(packageName);
                Utils.deleteFile(fileName);
            }
        }
    }

    private static DownloadInfoMgr getDownloadInfoMgr(String packageName) {
        DownloadInfoMgr downloadInfoMgr = DownloadInfoMgr.getSilentInstance();
        if (downloadInfoMgr.hasDownloadInfo(packageName)) {
            return downloadInfoMgr;
        }

        return DownloadInfoMgr.getNormalInstance();

    }

}
