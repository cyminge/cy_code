package com.cy.frame.downloader.downloadmanager;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.frame.downloader.core.DownloadRunnable;
import com.cy.frame.downloader.download.DownloadUtils;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.threadpool.NormalThreadPool;

@SuppressLint("HandlerLeak")
public class DownloadService extends Service {

    public static final String ACTION_SEVICE_START = "game.intent.action.DOWNLOAD_SERVICE"; // 服务action

    public static final String ACTION_START_DOWNLOAD_SERVICE_CMD = "ACTION_START_DOWNLOAD_SERVICE_CMD";

    static final int HANDLER_MAIN_INITIAL = 1;

    private static HashMap<String, DownloadRunnable> mDownloadingTask = new HashMap<String, DownloadRunnable>(); // 下载任务集合
    private final DownloadBinder mBinder = new DownloadBinder();
    private volatile MainHandler mMainHandler = new MainHandler();
    private boolean mIsQuitting = false;

    private final class MainHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (mIsQuitting) {
                return;
            }

            switch (msg.what) {
            case HANDLER_MAIN_INITIAL:
                
                DownloadUtils.printLastFailLog();
                
                DownloadInfoMgr.getNormalInstance().initDownloadTask();
                DownloadInfoMgr.getSilentInstance().initDownloadTask();
                break;

            default:
                break;
            }
        }
    }

    public class DownloadBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void startDownloadService(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_START_DOWNLOAD_SERVICE_CMD);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = null;
        if (null != intent) {
            action = intent.getAction();
        } else {
            action = ACTION_START_DOWNLOAD_SERVICE_CMD;
        }
        if (null == action) {
            return START_REDELIVER_INTENT;
        }

        if (ACTION_START_DOWNLOAD_SERVICE_CMD.contentEquals(action)) {
            mMainHandler.sendEmptyMessage(HANDLER_MAIN_INITIAL);
        }

        return START_REDELIVER_INTENT;
    }

    public static synchronized void postTask(String pkgName, DownloadRunnable downloadRunnable) {
        mDownloadingTask.put(pkgName, downloadRunnable);
        NormalThreadPool.getInstance().post(downloadRunnable);
    }

    public static synchronized void removeTask(DownloadInfo info, int targetStatus) {
        DownloadRunnable removeTask = mDownloadingTask.remove(info.mPackageName);
        if (removeTask != null) {
            removeTask.exit(targetStatus);
        }

        // if (info.mIsXunlei && targetStatus ==
        // DownloadStatusMgr.TASK_STATUS_PAUSED) {
        // XunleiManager.getInstance().pauseDownload(info);
        // }
    }

    public static synchronized boolean isTaskEmpty() {
        return mDownloadingTask.isEmpty();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsQuitting = true;
    }

}
