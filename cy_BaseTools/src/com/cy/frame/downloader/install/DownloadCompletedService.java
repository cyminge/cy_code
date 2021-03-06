package com.cy.frame.downloader.install;

import java.util.concurrent.ConcurrentSkipListSet;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.cy.constant.Constant;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.manager.DownloadDB;
import com.cy.frame.downloader.util.GameInstaller;
import com.cy.threadpool.NormalThreadPool;
import com.cy.utils.Utils;

@SuppressLint("NewApi") 
public abstract class DownloadCompletedService extends Service {
    private ConcurrentSkipListSet<Long> mDownIdSet = new ConcurrentSkipListSet<Long>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long downId = getDownId(intent);
        if (downId != DownloadDB.NO_DOWN_ID && !mDownIdSet.contains(downId) && mDownIdSet.add(downId)) {
            NormalThreadPool.getInstance().post(getHandleRunnable(downId));
        } else {
            tryStopService();
        }
        return START_REDELIVER_INTENT;
    }

    protected abstract long getDownId(Intent intent);

    protected abstract Runnable getHandleRunnable(long downId);

    private void tryStopService() {
        if (mDownIdSet.isEmpty()) {
            DownloadCompletedService.this.stopSelf();
        }
    }

    protected void clearUp(long downId) {
        mDownIdSet.remove(downId);
        tryStopService();
    }

    protected void startInstall(Context context, String homeDir, DownloadInfo downloadInfo) {
        String packageName = downloadInfo.packageName;
        InstallManager.addInstallingGame(packageName);
        String fileName = packageName + Constant.APK;
        if (Utils.permitSilentInstall(packageName)) {
            GameInstaller.silentInstall(context, homeDir, downloadInfo, fileName, false);
        } else {
            GameInstaller.popupInstall(context, homeDir, packageName);
        }
    }

}
