package com.cy.frame.downloader.install;

import java.util.HashMap;

import android.content.Intent;

import com.cy.constant.Constant;
import com.cy.frame.downloader.core.DownloadRunnable;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.manager.DownloadDB;

/**
 * 本应用下载文件的安装服务
 * @author zf
 *
 */
public class SelfDownloadService extends DownloadCompletedService {
    private HashMap<Long, DownloadInfo> mInfoMap = new HashMap<Long, DownloadInfo>();
    private String mHomeDir = Constant.EMPTY;

    @Override
    protected long getDownId(Intent intent) {
        try {
            DownloadInfo downloadInfo = intent.getParcelableExtra(DownloadRunnable.EXTRA_DOWNLOAD_INFO);
            mInfoMap.put(downloadInfo.mDownId, downloadInfo);
            mHomeDir = intent.getStringExtra(DownloadRunnable.EXTRA_DOWNLOAD_HOME_DIR);
            return downloadInfo.mDownId;
        } catch (Exception e) {
            return DownloadDB.NO_DOWN_ID;
        }
    }

    @Override
    protected Runnable getHandleRunnable(final long downId) {
        return new Runnable() {
            @Override
            public void run() {
                DownloadInfo downloadInfo = mInfoMap.remove(downId);
                startInstall(SelfDownloadService.this, mHomeDir, downloadInfo);
                clearUp(downId);
            }
        };
    }

}
