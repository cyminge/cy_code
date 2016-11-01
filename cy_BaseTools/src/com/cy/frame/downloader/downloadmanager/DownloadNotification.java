package com.cy.frame.downloader.downloadmanager;

import java.util.ArrayList;

import com.cy.R;
import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.utils.Utils;

public class DownloadNotification {

    public static void updateNotification(int count, ArrayList<String> pkgList) {
//        if (count > 0) {
//            Notification notification = createNotify(count, getDetailString(pkgList));
//            NotificationUtils.show(NotificationUtils.NOTIFICATION_ID_DOWNLOAD, notification);
//        } else {
//            NotificationUtils.cancel(NotificationUtils.NOTIFICATION_ID_DOWNLOAD);
//        }
    }

//    private static Notification createNotify(int count, String detail) {
//        Notification notification = new Notification();
//        notification.when = System.currentTimeMillis();
//        notification.icon = android.R.drawable.stat_sys_download;
//        notification.flags |= Notification.FLAG_NO_CLEAR;
//
//        Context app = BaseApplication.getAppContext();
//        String title = app.getResources().getString(R.string.notification_downloading, count);
//        notification.contentView = Utils.getRemoteViews(title, detail);
//
//        Activity topActivity = WatchDog.INSTANCE.getTopActivity();
//        String downloadActivityName = DownloadActivity.class.getName(); // 此DownloadActivity跟原来的DownloadActivity不一样，  cyminge modify ???
//        Intent intent = new Intent();
//        if (topActivity == null || topActivity.getClass().getName().equals(downloadActivityName)) {
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        }
//        intent.putExtra(JsonConstant.SOURCE, StatisValue.NOTIFY);
//        intent.setClassName(app.getPackageName(), downloadActivityName);
//        notification.contentIntent = PendingIntent.getActivity(app, 0, intent, 0);
//        return notification;
//    }

    private static String getDetailString(ArrayList<String> downloadPkgList) {
        StringBuilder detailBuilder = new StringBuilder();
        String comma = Utils.getResources().getString(R.string.recom_comma);
        int size = downloadPkgList.size();
        for (int i = 0; i < size; i++) {
            String pkgName = downloadPkgList.get(i);
            DownloadInfo info = DownloadInfoMgr.getNormalInstance().getDownloadInfo(pkgName);
            if (info.isCompleted()) {
                continue;
            }

            if (i != 0) {
                detailBuilder.append(comma);
            }
            detailBuilder.append(info.mGameName);
        }
        return detailBuilder.toString();
    }
}
