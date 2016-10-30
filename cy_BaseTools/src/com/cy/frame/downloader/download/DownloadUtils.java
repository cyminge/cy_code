package com.cy.frame.downloader.download;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import android.util.Log;
import android.widget.Toast;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.controller.SingleDownloadManager;
import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.frame.downloader.core.DownloadStatusMgr;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.statis.StatisValue;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager.UpgradeAppInfo;
import com.cy.frame.downloader.util.GameActionUtil;
import com.cy.frame.downloader.util.GameInstaller;
import com.cy.global.BaseApplication;
import com.cy.global.InitialWatchDog;
import com.cy.utils.Utils;
import com.cy.utils.sharepref.SharePrefUtil;
import com.cy.utils.storage.FileUtils;

public class DownloadUtils {
    private static final String TAG = "DownloadUtils";
    public static final int MIN_APK_SIZE = 18 * 1024;
    private static final String SIZE_DECIMAL_FORMAT = ".00";
    private static final String LAST_FAIL_REASON = "lastFailReason";

    public static String formatSize(long size) {
        DecimalFormat df = new DecimalFormat(SIZE_DECIMAL_FORMAT);
        if (size < Constant.KB) {
            if (size == 0) {
                return "0.00B";
            }
            if (size >= 1000) {
                return "1K";
            }
            return size + "B";
        }
        if (size < Constant.MB) {
            double num = ((double) size) / Constant.KB;
            if (num < 100.0) {
                return df.format(num) + "K";
            }
            if (num > 1000.0) {
                return "1.00M";
            }
            return (int) num + "K";
        }
        double num = ((double) size) / Constant.MB;
        return df.format(num) + Constant.M;
    }

    public static String reasonToString(int reason) {
        switch (reason) {
        case DownloadStatusMgr.FAIL_HTTP_DATA_ERROR:
            return StatisValue.ERR_HTTP;
        case DownloadStatusMgr.FAIL_URL_ERROR:
            return StatisValue.ERR_URL_ERROR;
        case DownloadStatusMgr.FAIL_URL_UNREACHABLE:
        case DownloadStatusMgr.FAIL_CANNOT_RESUME:
            return StatisValue.ERR_URL_UNREACHABLE;
        case DownloadStatusMgr.FAIL_URL_UNRECOVERABLE:
            return StatisValue.ERR_URL_UNRECOVERABLE;
        case DownloadStatusMgr.FAIL_APK_ERROR:
            return StatisValue.ERR_PACKAGE_NAME;
        case DownloadStatusMgr.FAIL_CONTENT_TYPE_ERROR:
            return StatisValue.ERR_CONTENT_TYPE_ERROR;
        case DownloadStatusMgr.FAIL_GAME_NOT_EXIST:
            return StatisValue.ERR_GAME_NOT_EXIST;

        case DownloadStatusMgr.PAUSE_NO_NETWORK:
            return StatisValue.NET;
        case DownloadStatusMgr.PAUSE_WAIT_WIFI:
            return StatisValue.DOWNLOAD_WAIT_WLAN;
        case DownloadStatusMgr.PAUSE_DEVICE_NOT_FOUND:
            return StatisValue.ERR_NO_DEVICE;
        case DownloadStatusMgr.PAUSE_FILE_ERROR:
            return StatisValue.ERR_FILE_ERROR;
        case DownloadStatusMgr.PAUSE_INSUFFICIENT_SPACE:
            return StatisValue.ERR_NO_SPACE;
        case DownloadStatusMgr.PAUSE_WIFI_INVALID:
            return StatisValue.ERR_WIFI_INVALID;

        case DownloadStatusMgr.RESUME_DEVICE_RECOVER:
            return StatisValue.DOWNLOAD_DEVICE_RECOVER;
        case DownloadStatusMgr.RESUME_NETWORK_RECONNECT:
            if (Utils.isMobileNet()) {
                return StatisValue.DOWNLOAD_GPRS;
            } else {
                return StatisValue.DOWNLOAD_WLAN;
            }

        case DownloadStatusMgr.PAUSE_BY_USER:
        case DownloadStatusMgr.RESUME_BY_USER:
            return StatisValue.DOWNLOAD_UESER;

        case DownloadStatusMgr.FAIL_UNKNOWN:
        default:
            return StatisValue.ERR_UNKNOWN;
        }
    }

    public static void toast(int reason) {
        if (InitialWatchDog.INSTANCE.getTopActivity() == null) {
            return;
        }

        int failedResid = -1;
        switch (reason) {
        case DownloadStatusMgr.PAUSE_FILE_ERROR:
            failedResid = R.string.file_write_error;
            break;
        case DownloadStatusMgr.PAUSE_DEVICE_NOT_FOUND:
            failedResid = R.string.sdcard_error;
            break;
        case DownloadStatusMgr.PAUSE_INSUFFICIENT_SPACE:
            failedResid = R.string.sdcard_low_space;
            break;
        case DownloadStatusMgr.FAIL_URL_ERROR:
        case DownloadStatusMgr.FAIL_URL_UNREACHABLE:
        case DownloadStatusMgr.FAIL_URL_UNRECOVERABLE:
        case DownloadStatusMgr.FAIL_HTTP_DATA_ERROR:
        case DownloadStatusMgr.FAIL_CONTENT_TYPE_ERROR:
        case DownloadStatusMgr.PAUSE_XUNLEI_WAITING_TO_RETRY:
            failedResid = R.string.connect_server_fail;
            break;
        case DownloadStatusMgr.USER_DELETED:
            failedResid = R.string.download_deleted;
            break;
        case DownloadStatusMgr.PAUSE_WIFI_INVALID:
            failedResid = R.string.no_net_msg;
            break;
        case DownloadStatusMgr.FAIL_APK_ERROR:
            failedResid = R.string.apk_error_retry;
            break;
        case DownloadStatusMgr.FAIL_UNKNOWN:
            failedResid = R.string.unknown_fail;
            break;
        case DownloadStatusMgr.FAIL_GAME_NOT_EXIST:
            failedResid = R.string.game_not_exist;
            break;
        case DownloadStatusMgr.FAIL_CANNOT_RESUME:
            failedResid = R.string.cannot_resume;
            break;
        default:
            break;
        }

        if (failedResid != -1) {
            Toast.makeText(BaseApplication.getAppContext(), failedResid, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isFileInvilid(String homeDir, DownloadInfo info) {
        String packageName = info.mPackageName;
        String fileName = getFileName(packageName);
        Utils.renameFile(homeDir, fileName + Constant.TMP_FILE_EXT, fileName);

        boolean isError = false;
        if (!Utils.isFileExisting(homeDir, fileName)) {
            isError = true;
            if (GamesUpgradeManager.isIncreaseType(packageName)) {
                GamesUpgradeManager.updateAppInfoType(packageName, false);
            } else if (info.mFilePath != null && info.mFilePath.contains(".patch" + Constant.TMP_FILE_EXT)) {
                ButtonStatusManager.removeDownloaded(packageName);
                DownloadInfoMgr.getNormalInstance().removeDownloadInfo(packageName);
                isError = false;
            }
        } else if (GamesUpgradeManager.isIncreaseType(packageName)) {
            if (GameInstaller.APPLY_PATCH_SUCCESS != GameInstaller.applyPatch(homeDir, packageName)) {
                GamesUpgradeManager.updateAppInfoType(packageName, false);
                ButtonStatusManager.removeDownloaded(packageName);
                DownloadInfoMgr.getNormalInstance().removeDownloadInfo(packageName);
                downloadAPK(true, packageName);
                isError = true;
            }
        } else if (!Utils.isSamePackage(homeDir, fileName, packageName)) {
            isError = true;
        }
        if (isError) {
            Utils.delAllfiles(packageName);
            recoveryDownloadUrl(info);
        } else {
            FileUtils.notifyApkAdd(fileName);
        }
        return isError;
    }

    private static void recoveryDownloadUrl(DownloadInfo info) {
        if (info.mRawDownloadUrl != null && !info.mRawDownloadUrl.equals(info.mDownloadUrl)) {
            info.mDownloadUrl = info.mRawDownloadUrl;
        }
    }

    private static void downloadAPK(boolean isApplyFail, String packageName) {
        // if (GNApplication.getInstance().hasBooted()) { // cyminge modify
        UpgradeAppInfo info = GamesUpgradeManager.getOneAppInfo(packageName);
        if (info != null) {
            info.mSource = StatisValue.SPLIT_ERROR;
            SingleDownloadManager singleTask = new SingleDownloadManager();
            singleTask.execute(info, null, isApplyFail);
        }
        // }
    }

    private static String getFileName(String packageName) {
        if (GamesUpgradeManager.isIncreaseType(packageName)) {
            return packageName + ".patch";
        } else {
            return packageName + Constant.APK;
        }
    }

    private static final String HTTP_WORK_VERIFICATION = "http://www.baidu.com/";

    public static boolean isWifiInvalid() {
        if (!Utils.isWifiNet()) {
            return false;
        }
        HttpURLConnection conn = null;
        try {
            URL url = new URL(HTTP_WORK_VERIFICATION);
            conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setConnectTimeout(Constant.SECOND_10);
            conn.setReadTimeout(Constant.SECOND_10);
            int responseCode = conn.getResponseCode();
            Log.e("TAG", "responseCode = " + responseCode);
            return responseCode == HttpURLConnection.HTTP_MOVED_TEMP;
        } catch (IOException e) {
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }

    public static String getPkgNameAppendStartTime(String pkgName) {
        DownloadInfo info = DownloadInfoMgr.getDownloadInfoInAll(pkgName);
        if (info == null || info.mStartTime == 0) {
            return pkgName;
        } else {
            return StatisValue.combinePkgName(info.mPackageName, info.mStartTime + Constant.EMPTY);
        }
    }

    public static String statusToString(int status) {
        switch (status) {
        case DownloadStatusMgr.TASK_STATUS_DOWNLOADING:
        case DownloadStatusMgr.TASK_STATUS_PENDING:
            return StatisValue.DOWNLOAD_RESUME;
        case DownloadStatusMgr.TASK_STATUS_FAILED:
            return StatisValue.DOWNLOAD_FAIL;
        case DownloadStatusMgr.TASK_STATUS_PAUSED:
            return StatisValue.DOWNLOAD_PAUSE;
        case DownloadStatusMgr.TASK_STATUS_SUCCESSFUL:
            return StatisValue.DOWNLOAD_SUCCESS;
        default:
            return StatisValue.START_DOWNLOAD;
        }
    }

    public static void saveLastFailLog(String reason) {
        SharePrefUtil.putString(LAST_FAIL_REASON, reason);
    }

    public static void printLastFailLog() {
        String reason = SharePrefUtil.getString(LAST_FAIL_REASON);
        Log.e(TAG, reason);
    }

    public static void onDownloadSucc(DownloadInfo info) {
        if (info == null) {
            return;
        }

        GameActionUtil.postGameAction(info.mPackageName, Constant.ACTION_SUCCESS_DOWNLOAD);
        if (Constant.AMIGO_PLAY_PACKAGE_NAME.equals(info.mPackageName)) {
            // StatisHelper.get().send(StatisValue.DOWNLOAD_AMIGO_PLAY_SUCCESS,
            // Constant.AMIGO_PLAY_PACKAGE_NAME, info.mSource);
        }
    }
}
