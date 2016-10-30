package com.cy.frame.downloader.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.frame.downloader.core.DownloadStatusMgr;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.install.InstallManager;
import com.cy.global.BaseApplication;
import com.cy.global.InitialWatchDog;
import com.cy.threadpool.NomalThreadPool;
import com.cy.utils.Utils;
import com.cy.utils.storage.GNStorageUtils;

@SuppressLint("NewApi") public class GameInstaller {
    private static final String TAG = "GameInstaller";
    
    public static final int REQUEST_CODE_FOR_SELF_UPGRADE = 100;
    public static final int REQUEST_CODE_FOR_CONTENT_UPGRADE = REQUEST_CODE_FOR_SELF_UPGRADE + 1;
    protected static final int ACCOUNT_REQUEST_CODE = REQUEST_CODE_FOR_CONTENT_UPGRADE + 1;

    private static final int INSTALL_REPLACE_EXISTING = 0x00000002;
    private static final int INSTALL_SUCCEEDED = 1;
    public static final int APPLY_PATCH_SUCCESS = 0;
    public static final String INSTALL_JUMP_ACTION = "gamehall.intent.action.INSTALL_JUMP";
    public static final String TYPE_APPLICATION = "application/vnd.android.package-archive";

    public static void silentInstall(final Context context, final DownloadArgs downloadArgs,
            final String fileName, final boolean fromActivity) {
        silentInstall(context, GNStorageUtils.getHomeDirAbsolute(), downloadArgs, fileName, fromActivity);
    }

    public static void silentInstall(final Context context, final String homeDir,
            final DownloadArgs downloadArgs, final String fileName, final boolean fromActivity) {
    	NomalThreadPool.getInstance().post(new Runnable() {
            @Override
            public void run() {
                DownloadArgs args = fromActivity ? (DownloadArgs) downloadArgs.clone() : downloadArgs;
                systemSilentInstall(context, homeDir, args, fileName, fromActivity);
            }
        });
    }

    private static boolean hasSignature(String localPath) {
        PackageManager pm = BaseApplication.getAppContext().getPackageManager();
        return Utils.getPackageSignatureByPath(pm, localPath) != null;
    }

    public static void popupInstall(Context context, String pkgName) {
        popupInstall(context, GNStorageUtils.getHomeDirAbsolute(), pkgName);
    }

    public static void popupInstall(Context context, String homeDir, String pkgName) {
        String apkDownloadDir = homeDir;
        if (apkDownloadDir == null) {
            Toast.makeText(context, "sdcard error", Toast.LENGTH_SHORT);
            return;
        }
        String apkAbsolutePath = apkDownloadDir + File.separator + pkgName + Constant.APK;
        startSystemInstaller(context, apkAbsolutePath, pkgName);
        InstallManager.removeInstallingGame(pkgName);
    }

    public static void startSystemInstaller(Context context, String filePath, String packageName) {
        startSystemInstaller(context, filePath, packageName, REQUEST_CODE_FOR_CONTENT_UPGRADE);
    }

    public static void startSystemInstaller(Context context, String filePath, String packageName, int code) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        if (Utils.isClientSelf(packageName)) {
            startSystemInstallerForGameHall(file, code);
        } else {
            startSystemInstallerForGames(context, file);
        }
    }

    private static void startSystemInstallerForGameHall(File file, int code) {
        Activity activity = InitialWatchDog.INSTANCE.getTopActivity();
        if (activity == null) {
            return;
        }
//        GNApplication.getInstance().unregisterReceiver();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), TYPE_APPLICATION);
//        GameActivityUtils.startActivityForResult(activity, intent, code);
    }

    private static void startSystemInstallerForGames(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(Uri.fromFile(file), TYPE_APPLICATION);
        context.startActivity(intent);
    }

    private static void installNotify(Context context, DownloadArgs args, boolean setFlag) {
        if (args.mIsSilentDownload) {  // || args.mSource.contains(StatisValue.UPDATE_SILENT)
            return;
        }

        try {
            String gameName = getGameName(args.mPackageName, args.mGameName);
            String pkgName = args.mPackageName;
            String infoStr = context.getString(R.string.installed);
            Intent intent = new Intent();
            intent.setAction(INSTALL_JUMP_ACTION);
            intent.putExtra(Constant.PACKAGE, pkgName);
            String contentStr;
            if (Constant.AMIGO_PLAY_PACKAGE_NAME.equals(pkgName)) {
                contentStr = context.getString(R.string.click_open_recharge);
            } else {
                contentStr = context.getString(R.string.click_open_game);
            }
            // cyminge modify
//            RemoteViews contentView = Utils.getRemoteViews(gameName + infoStr, contentStr);
//            CustomNotifyParams data = new CustomNotifyParams(contentView, null, intent,
//                    NotificationUtils.BROADCAST);
//            data.setTag(pkgName);
//            data.setSingleNotify(NotificationUtils.NOTIFICATION_ID_INSTALL);
//            NotificationUtils.showCustomViewNotification(data);
        } catch (Exception e) {
        }
    }

    private static String getGameName(String packageName, String gameName) {
        if (gameName.isEmpty()) {
            try {
                PackageManager pm = BaseApplication.getAppContext().getPackageManager();
                PackageInfo p = Utils.getPackageInfoByName(packageName);
                return p.applicationInfo.loadLabel(pm).toString();
            } catch (Exception e) {
                return packageName;
            }
        }
        return gameName;
    }

    public static int applyPatch(String homeDir, String packageName) { // cyminge modify
        int result = -1;
//        try {
//            String oldPath = Utils.getPackageInfoByName(packageName).applicationInfo.publicSourceDir;
//            String newPath = homeDir + File.separator + packageName + Constant.APK;
//            String patchPath = homeDir + File.separator + packageName + ".patch";
//            result = ApkCreater.applyPatch(oldPath, newPath, patchPath);
//            if (Utils.getPackageInfoByPath(newPath) == null) {
//                result = -1;
//            }
//            Utils.deleteFile(homeDir, packageName + ".patch");
//            ButtonStatusManager.addDownloaded(packageName);
//            return result;
//        } catch (Exception e) {
//            return result;
//        } catch (UnsatisfiedLinkError e) {
//            return result;
//        }
        return result;
    }

    public static boolean manualInstall(final Context context, final DownloadArgs downloadArgs) {
        String fileName = downloadArgs.mPackageName + Constant.APK;
        if (Utils.isSamePackage(fileName, downloadArgs.mPackageName)) {
            InstallManager.addInstallingGame(downloadArgs.mPackageName);
            if (Utils.permitSilentInstall(downloadArgs.mPackageName)) {
                GameInstaller.silentInstall(context, downloadArgs, fileName, true);
            } else {
                GameInstaller.popupInstall(context, downloadArgs.mPackageName);
            }
            return true;
        } else {
            ButtonStatusManager.removeDownloaded(downloadArgs.mPackageName);
            DownloadInfoMgr.getNormalInstance().removeDownloadInfo(downloadArgs.mPackageName);
            Utils.deleteFile(fileName);
            Toast.makeText(context, R.string.file_error, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 静默安装
     * @param context
     * @param homeDir
     * @param downloadArgs
     * @param fileName
     * @param fromActivity
     */
    public static synchronized void systemSilentInstall(final Context context, final String homeDir,
            final DownloadArgs downloadArgs, final String fileName, final boolean fromActivity) {
//        String apkAbsolutePath = homeDir + File.separator + fileName;
//        String pkgName = downloadArgs.mPackageName;
//
//        try {
//            int installFlags = 0;
//            installFlags |= INSTALL_REPLACE_EXISTING;
//            PackageManager pm = context.getPackageManager();
//            IPackageInstallObserver observer = new GamePackageInstallObserver(context, homeDir, downloadArgs,
//                    fileName, fromActivity);
//            File file = new File(apkAbsolutePath);
//            Uri packageUri = Uri.fromFile(file);
//            pm.installPackage(packageUri, observer, 0, pkgName);
//        } catch (Exception e) {
//            silentInstallFail(context, homeDir, downloadArgs, pkgName, apkAbsolutePath);
//        }
    }

    private static void silentInstallFail(final Context context, final String homeDir,
            final DownloadArgs downloadArgs, String pkgName, String apkAbsolutePath) {
        if (downloadArgs.mIsSilentDownload) {
            DownloadStatusMgr.getSilentInstance().onSilentInstallingError(pkgName);
        } else if (hasSignature(apkAbsolutePath)) {
            popupInstall(context, homeDir, pkgName);
        } else {
            InstallManager.removeInstallingGame(pkgName);
            ButtonStatusManager.removeDownloaded(pkgName);
            Utils.delAllfiles(pkgName);
            DownloadStatusMgr.getNormalInstance().onSilentInstallingError(pkgName);
        }
    };

//    private static class GamePackageInstallObserver extends IPackageInstallObserver.Stub {
//        private Context mContext;
//        private String mHomeDir;
//        private String mFileName;
//        private DownloadArgs mDownloadArgs;
//        private boolean mFromActivity;
//
//        public GamePackageInstallObserver(final Context context, final String homeDir,
//                final DownloadArgs downloadArgs, final String fileName, final boolean fromActivity) {
//            mContext = context;
//            mHomeDir = homeDir;
//            mDownloadArgs = downloadArgs;
//            mFileName = fileName;
//            mFromActivity = fromActivity;
//        }
//
//        @Override
//        public synchronized void packageInstalled(String pkgName, int returnCode) {
//            String apkAbsolutePath = mHomeDir + File.separator + mFileName;
//
//            if (returnCode == INSTALL_SUCCEEDED) {
//                installSuccess(mContext, mFromActivity, mDownloadArgs, pkgName);
//            } else {
//                silentInstallFail(mContext, mHomeDir, mDownloadArgs, pkgName, apkAbsolutePath);
//            }
//        }
//
//        private void installSuccess(final Context context, final boolean fromActivity, DownloadArgs args,
//                String pkgName) {
//            InstallManager.removeInstallingGame(pkgName);
//            GamesUpgradeManager.updateAppInfoContent(pkgName);
//            ButtonStatusManager.onInstalledChange(pkgName, false);
//            installNotify(context, args, !fromActivity);
//            DownloadUtils.sendStatis(StatisValue.INSTALL_SUCC, pkgName, StatisValue.SILENT_INSTALL);
//            if (!args.mIsSilentDownload && !args.mSource.contains(StatisValue.UPDATE_SILENT)) {
//                DownloadUtils.sendStatis(StatisValue.SEND_NEWS, pkgName, StatisValue.INSTALL_FINISH);
//            }
//        }
//    }
}
