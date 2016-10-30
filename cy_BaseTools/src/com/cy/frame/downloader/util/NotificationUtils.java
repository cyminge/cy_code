package com.cy.frame.downloader.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.RemoteViews;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.global.BaseApplication;
import com.cy.utils.Utils;

public class NotificationUtils {

    public static final int BROADCAST = 0;
    public static final int ACTIVITY = 1;
    private static final int SERVICE = 2;

    public static final int NOTIFICATION_ID_SELF_UPGRADE = 8888;
    public static final int NOTIFICATION_ID_DOWNLOAD = NOTIFICATION_ID_SELF_UPGRADE + 1;
    public static final int NOTIFICATION_ID_GAMES_UPGRADE = NOTIFICATION_ID_DOWNLOAD + 1;
    private static final int NOTIFICATION_ID_FEEDBACK = NOTIFICATION_ID_GAMES_UPGRADE + 1;
    public static final int NOTIFICATION_ID_INSTALL = NOTIFICATION_ID_FEEDBACK + 1;

    public static void cancel(int id) {
        NotificationManager manager = getNotificationManager();
        manager.cancel(id);
    }

    public static void cancelWithEmptyTag(int id) {
        cancel(Constant.EMPTY, id);
    }

    private static void cancel(String tag, int id) {
        NotificationManager manager = getNotificationManager();
        manager.cancel(tag, id);
    }

    public static void show(int id, Notification notification) {
        show(null, id, notification);
    }

    private static void show(AbstractNotifyParams data, Notification notification) {
        show(data.mTag, data.mId, notification);
    }

    private static void show(String tag, int id, Notification notification) {
        try {
            NotificationManager manager = getNotificationManager();
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;
            setLollipopIcon(notification);
            manager.notify(tag, id, notification);
        } catch (Exception e) {
        }
    }

    private static void setLollipopIcon(Notification notification) {
//        if (Utils.isLollipop()) {
//            notification.icon = R.drawable.ic_launcher_notification;
//
//            Bitmap bigIcon = BitmapUtil.decodeResource(Utils.getResources(), R.drawable.ic_launcher);
//            notification.largeIcon = bigIcon;
//        }
    }

    public static void showCommonNotification(SysNotifyParams data) {
//        Context context = BaseApplication.getAppContext();
//        Builder builder = new Notification.Builder(context).setContentTitle(data.mTitle)
//                .setContentText(data.mContent).setContentIntent(data.mPendingIntent)
//                .setSmallIcon(R.drawable.ic_launcher).setDefaults(data.mDefaultValue).setTicker(data.mTitle);
//
//        setLollipopIcon(builder);
//        Notification notification = builder.getNotification();
//        notification.flags |= data.mFlags;
//        show(data, notification);
    }

    public static void setLollipopIcon(Builder builder) {
//        if (Utils.isLollipop()) {
//            builder.setSmallIcon(R.drawable.ic_launcher_notification);
//            Bitmap bigIcon = BitmapUtil.decodeResource(Utils.getResources(), R.drawable.ic_launcher);
//            builder.setLargeIcon(bigIcon);
//        }
    }

    @SuppressLint("NewApi")
    public static void showCustomViewNotification(CustomNotifyParams data) {
        Notification notification = new Notification();
        notification.contentView = data.mSmallView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification.bigContentView = data.mBigView;
        }
        notification.flags |= data.mFlags;
        notification.defaults |= data.mDefaultValue;
        notification.icon = Utils.getGameHallIconId();
        notification.contentIntent = data.mPendingIntent;
        show(data, notification);
    }

    private static NotificationManager getNotificationManager() {
        return (NotificationManager) BaseApplication.getAppContext().getSystemService(
                Context.NOTIFICATION_SERVICE);
    }

    public static class SysNotifyParams extends AbstractNotifyParams {
        protected String mTitle;
        protected String mContent;

        public SysNotifyParams(Intent intent, String title, String content, int type) {
            super(intent, type);
            mTitle = title;
            mContent = content;
        }

    }

    public static class CustomNotifyParams extends AbstractNotifyParams {
        protected RemoteViews mBigView;
        protected RemoteViews mSmallView;

        public CustomNotifyParams(RemoteViews smallView, RemoteViews bigView, Intent intent, int type) {
            super(intent, type);
            mSmallView = smallView;
            mBigView = bigView;
        }
    }

    private abstract static class AbstractNotifyParams {

        protected PendingIntent mPendingIntent;
        protected String mTag = Constant.EMPTY;
        protected int mId = this.hashCode();
        protected int mDefaultValue = Notification.DEFAULT_SOUND;
        protected int mFlags = Notification.FLAG_AUTO_CANCEL;

        private AbstractNotifyParams(Intent intent, int type) {
            mPendingIntent = getPendingIntent(intent, type);
        }

        private PendingIntent getPendingIntent(Intent intent, int type) {
            PendingIntent pendintIntent;
            Context context = BaseApplication.getAppContext();
            int requestCode = intent.hashCode();
            int flags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (type == BROADCAST) {
                pendintIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags);
            } else if (type == SERVICE) {
                pendintIntent = PendingIntent.getService(context, requestCode, intent, flags);
            } else {
                pendintIntent = PendingIntent.getActivity(context, requestCode, intent, flags);
            }
            return pendintIntent;
        }

        public void setSingleNotify(int id) {
            mId = id;
        }

        public void setTag(String tag) {
            mTag = tag;
        }
    }
}
