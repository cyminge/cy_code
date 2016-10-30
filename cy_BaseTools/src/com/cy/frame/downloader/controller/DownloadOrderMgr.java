package com.cy.frame.downloader.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

import android.content.res.Resources;

import com.cy.R;
import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.downloadmanager.DownloadNotification;
import com.cy.utils.Utils;

/**
 * 下载文件的排序管理，主要用于界面UI展示
 * @author zf
 *
 */
public class DownloadOrderMgr {

    private static int sDownloadCount = 0;
    private static int sCompletedCount = 0;
    private static ArrayList<String> sSortPkgList = new ArrayList<String>();
    private static DownloadInfoComparator sInfoComparator = new DownloadInfoComparator();

    private static boolean sIsOrderChange = false;

    public static boolean hasDownload() {
        return sDownloadCount > 0;
    }

    public static int getDownloadCount() {
        return sDownloadCount;
    }

    public static boolean isFirstItem(int position) {
        return position == 0 || position == sDownloadCount;
    }

    private static boolean isCompleted(final int position) {
        return sCompletedCount > 0 && position >= sDownloadCount;
    }

    public static ArrayList<String> getSortPkgList() {
        synchronized (sSortPkgList) {
            return new ArrayList<String>(sSortPkgList);
        }
    }

    public static boolean isOrderChange() {
        boolean ret = sIsOrderChange;
        sIsOrderChange = false;
        return ret;
    }

    public static void onOrderChange(ConcurrentHashMap<String, DownloadInfo> sDownloadMap) {
        sIsOrderChange = true;
        ArrayList<DownloadInfo> infos = new ArrayList<DownloadInfo>(sDownloadMap.values());
        Collections.sort(infos, sInfoComparator);

        synchronized (sSortPkgList) {
            sSortPkgList.clear();
            for (DownloadInfo info : infos) {
                sSortPkgList.add(info.mPackageName);
            }
            computeDownloadCount(sSortPkgList);
            DownloadNotification.updateNotification(sDownloadCount, sSortPkgList);
        }
    }

    private static void computeDownloadCount(ArrayList<String> sortPkgList) {
        sCompletedCount = 0;
        sDownloadCount = 0;
        for (String pkg : sortPkgList) {
            if (DownloadInfoMgr.getNormalInstance().getDownloadInfo(pkg).isCompleted()) {
                sCompletedCount++;
            } else {
                sDownloadCount++;
            }
        }
    }

    /**
     * 
     * @param position
     * @return
     */
    public static String getFirstItemTitle(int position) {
        Resources resources = Utils.getResources();
        if (isCompleted(position)) {
            return resources.getString(R.string.downloaded_count, sCompletedCount);
        } else {
            return resources.getString(R.string.downloading_count, sDownloadCount);
        }
    }
    
    private static class DownloadInfoComparator implements Comparator<DownloadInfo>, Serializable {
        private static final long serialVersionUID = -8029527994878717503L;

        @Override
        public int compare(DownloadInfo info1, DownloadInfo info2) {
            boolean completed1 = info1.isCompleted();
            boolean completed2 = info2.isCompleted();
            if (completed1 != completed2) {
                return completed1 ? 1 : -1;
            } else if (completed1) {
                long completeTime1 = info1.getCompleteTime();
                long completeTime2 = info2.getCompleteTime();
                if (completeTime2 == completeTime1) {
                    return 0;
                } else if (completeTime2 > completeTime1) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                long time1 = info1.mInitTime;
                long time2 = info2.mInitTime;
                if (time2 == time1) {
                    return 0;
                } else if (time2 < time1) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    }

}
