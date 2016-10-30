package com.cy.frame.downloader.upgrade;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.cy.constant.Constant;
import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.frame.downloader.download.DownloadArgsFactory;
import com.cy.frame.downloader.entity.GameListData;
import com.cy.frame.downloader.statis.StatisValue;
import com.cy.frame.downloader.util.JsonConstant;
import com.cy.frame.downloader.util.JsonUtils;
import com.cy.frame.downloader.util.UrlConstant;
import com.cy.global.BaseApplication;
import com.cy.listener.GameListenerManager;
import com.cy.utils.Utils;
import com.cy.utils.sharepref.SharePrefUtil;

/**
 * 游戏更新管理
 * 
 * @author JLB6088
 * 
 */
@SuppressLint("NewApi")
public class GamesUpgradeManager {

    private static final int MIN_LEAD_PERCENT = 40;

    private static final String LEAD_PERCENT = "leadPercent";

    private static final String LEAD_PERCENT_KEY = "lead_percent";

    private static final String TAG = "GamesUpgradeManager";

    private static final String GAMES_UPGRADE_NOTIFY_KEY = "games_upgrade_notify_key";

    private static ArrayList<UpgradeAppInfo> sAppInfo = new ArrayList<UpgradeAppInfo>();

    private static final String APP_INFO = "appinfo";
    private static final String DATA = "data";

    // info key
    private static final String PACKAGE = "mPackage";
    private static final String GAME_ID = "mAppId";
    private static final String NEW_VERSIONCODE = "mNewVersionCode";
    private static final String SIZE = "mAppSize";
    private static final String NEW_VERSIONNAME = "mNewVersionName";
    private static final String DOWNLOAD_URL = "mDownLoadUrl";
    private static final String PATCH_URL = "mPatchUrl";
    private static final String PATCH_SIZE = "mPatchSize";
    private static final String UPGRADE_INFO = "mChanges";
    private static final String ICON_URL = "mIconUrl";

    private static boolean sIsInit = false;

    public static ArrayList<UpgradeAppInfo> getAppInfo() {
        init();
        return sAppInfo;
    }

    public static int getAppInfoSize() {
        init();
        return sAppInfo.size();
    }

    private static void init() {
        if (!sIsInit) {
            initAppInfo();
            checkAppInfo();
            sIsInit = true;
        }
    }

    private static void initAppInfo() {
        if (!sAppInfo.isEmpty()) {
            return;
        }
        String jsonStr = SharePrefUtil.getString(APP_INFO);
        if (jsonStr.isEmpty()) {
            return;
        }
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONArray ja = json.getJSONArray(DATA);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject object = ja.getJSONObject(i);
                UpgradeAppInfo appInfo = createUpgradeAppInfo(object);
                if (appInfo != null) {
                    sAppInfo.add(appInfo);
                }
            }
        } catch (JSONException e) {
        }
    }

    private static boolean updateAppInfo(String jsonInfo, String checkKey) {
        init();
        if (jsonInfo.isEmpty()) {
            return false;
        }
        int updateCount = 0;
        int splitCount = 0;
        boolean result = false;
        boolean isUpdate = false;
        try {
            ArrayList<UpgradeAppInfo> upList = new ArrayList<GamesUpgradeManager.UpgradeAppInfo>();
            JSONObject json = new JSONObject(jsonInfo);
            JSONArray ja = json.getJSONArray(DATA);
            saveLeadPercent(json.optString(LEAD_PERCENT));
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                UpgradeAppInfo upApp = createUpgradeAppInfo(jsonObject);
                if (upApp != null) {
                    upList.add(upApp);
                    if (isNeedAdd(upApp, checkKey)) {
                        sAppInfo.add(upApp);
                        isUpdate = true;
                    }
                    if (upApp.mIsIncreaseType) {
                        splitCount++;
                    } else {
                        updateCount++;
                    }
                    result = true;
                }
            }
            sendCheckStatis(updateCount, splitCount);
            if (checkAppInfo(upList)) {
                isUpdate = true;
            }
        } catch (Exception e) {
        }

        if (isUpdate) {
            updateLauncherIcon();
            setGamesUpgradeNotify(true);
            sort(sAppInfo);
            saveAppInfo();
        }
        return result;
    }

    private static void sendCheckStatis(int updateCount, int splitCount) {
    }

    private static void updateLauncherIcon() {
        Utils.refreshCornerIcon(sAppInfo.size());
    }

    // check local app change
    private static void checkAppInfo() {
        for (int i = 0; i < sAppInfo.size(); i++) {
            PackageInfo p = Utils.getPackageInfoByName(sAppInfo.get(i).mPackageName);
            if (p == null || p.versionCode >= sAppInfo.get(i).mNewVersionCode) {
                sAppInfo.remove(i);
                i--;
            }
        }
    }

    // check server app change
    private static boolean checkAppInfo(ArrayList<UpgradeAppInfo> upList) {
        if (upList.size() < 1) {
            return false;
        }

        boolean result = false;
        for (int i = 0; i < sAppInfo.size(); i++) {
            for (int j = 0; j < upList.size(); j++) {
                if (sAppInfo.get(i).mPackageName.equals(upList.get(j).mPackageName)) {
                    break;
                }
                if (j == upList.size() - 1) {
                    result = true;
                    sAppInfo.remove(i);
                    i--;
                }
            }
        }
        return result;
    }

    private static boolean isNeedAdd(UpgradeAppInfo upApp, String checkKey) {
        for (int i = 0; i < sAppInfo.size(); i++) {
            UpgradeAppInfo localAppInfo = sAppInfo.get(i);
            if (upApp.mPackageName.equals(localAppInfo.mPackageName)) {
                if (needRemove(upApp, localAppInfo, checkKey)) {
                    sAppInfo.remove(i);
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    private static boolean needRemove(UpgradeAppInfo upApp, UpgradeAppInfo localAppInfo, String checkKey) {
        return !localAppInfo.isEquals(upApp);
    }

    public static void updateAppInfoContent(String packageName) {
        try {
            init();
            for (int i = 0; i < sAppInfo.size(); i++) {
                if (sAppInfo.get(i).mPackageName.equals(packageName)) {
                    PackageInfo p = Utils.getPackageInfoByName(packageName);
                    if (p != null) {
                        if (p.versionCode < sAppInfo.get(i).mNewVersionCode) {
                            updateAppInfoContent(i, p.versionName);
                            return;
                        }
                    }
                    sAppInfo.remove(i);
                    saveAppInfo();
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    private static void updateAppInfoContent(int index, String versionName) {
        UpgradeAppInfo info = sAppInfo.get(index);
        if (!info.mCurVersionName.equals(versionName)) {
            info.mCurVersionName = versionName;
            String pkgName = info.mPackageName;
            if (info.mIsIncreaseType && !DownloadInfoMgr.getNormalInstance().hasDownloadInfo(pkgName)) {
                updateAppInfoType(pkgName, false);
            }
        }
    }

    public static void updateAppInfoType(String packageName, boolean isIncreaseType) {
        init();
        for (int i = 0; i < sAppInfo.size(); i++) {
            UpgradeAppInfo info = sAppInfo.get(i);
            if (info.mPackageName.equals(packageName)) {
                info.mIsIncreaseType = isIncreaseType;
                info.mPatchUrl = Constant.EMPTY;
                info.mPatchSize = Constant.ZERO;
                sort(sAppInfo);
                GameListenerManager.onEvent(GameListenerManager.UPGRADE_TYPE_CHANGE);
                saveAppInfo();
                return;
            }
        }
    }

    public static boolean check(String checkKey) {
        GameListenerManager.onEvent(GameListenerManager.UPGRADE_CHECK_START);
        String data;
        String upInfo = getInfoString();
        try {
            if (upInfo.isEmpty()) {
                return false;
            }

            data = JsonUtils.getUpgradeInfo(UrlConstant.UPGRADE_URL, upInfo);
            if (data == null || data.isEmpty() || JsonUtils.isRequestDataFail(data)) {
                return false;
            }
            if (updateAppInfo(data, checkKey)) {
                return true;
            }

            JSONObject json = new JSONObject(data);
            String succ = json.getString("success");
            if (Boolean.parseBoolean(succ)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            GameListenerManager.onEvent(GameListenerManager.UPGRADE_CHECK_FINISH);
        }

        return false;
    }

    private static void saveLeadPercent(String leadPercent) {
        if (TextUtils.isEmpty(leadPercent)) {
            SharePrefUtil.remove(LEAD_PERCENT_KEY);
        } else {
            SharePrefUtil.putString(LEAD_PERCENT_KEY, leadPercent);
        }
    }

    private static String getRandomPercent() {
        Random random = new Random();
        int percent = MIN_LEAD_PERCENT + random.nextInt(MIN_LEAD_PERCENT);
        return percent + "%";
    }

    public static String getLeadPercent() {
        return SharePrefUtil.getString(LEAD_PERCENT_KEY, getRandomPercent());
    }

    private static void sort(ArrayList<UpgradeAppInfo> games) {
        int size = games.size();
        for (int i = 0; i < size; i++) {
            UpgradeAppInfo game = games.get(i);
            if (!game.mIsIncreaseType) {
                games.remove(i);
                games.add(game);
                i--;
                size--;
            }
        }
    }

    public static void exit() {
        saveAppInfo();
    }

    private static void saveAppInfo() {
        if (!sIsInit) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (UpgradeAppInfo appInfo : sAppInfo) {
                JSONObject jsonItem = getJsonFromInfo(appInfo);
                if (jsonItem != null) {
                    jsonArray.put(jsonItem);
                }
            }
            jsonObject.putOpt(DATA, jsonArray);
            SharePrefUtil.putString(APP_INFO, jsonObject.toString());
        } catch (Exception e) {
        }
    }

    private static JSONObject getJsonFromInfo(UpgradeAppInfo appInfo) {
        try {
            JSONObject jsonItem = new JSONObject();
            jsonItem.put(PACKAGE, appInfo.mPackageName);
            jsonItem.put(GAME_ID, appInfo.mGameId);
            jsonItem.put(NEW_VERSIONCODE, appInfo.mNewVersionCode);
            jsonItem.put(NEW_VERSIONNAME, appInfo.mNewVersionName);
            jsonItem.put(SIZE, appInfo.mGameSize);
            jsonItem.put(DOWNLOAD_URL, appInfo.mDownloadUrl);
            jsonItem.put(PATCH_URL, appInfo.mPatchUrl);
            jsonItem.put(PATCH_SIZE, appInfo.mPatchSize);
            jsonItem.put(UPGRADE_INFO, appInfo.mUpgradeInfo);
            jsonItem.put(ICON_URL, appInfo.mIconUrl);
            return jsonItem;
        } catch (Exception e) {
            return null;
        }
    }

    private static String getInfoString() {
        StringBuilder sb = new StringBuilder();
        ArrayList<PackageInfo> res = getPackageInfo();
        for (PackageInfo p : res) {
            if (FilterApps.isFilterApps(p.packageName)) {
                continue;
            }

            try {
                String md5;
                File file = new File(p.applicationInfo.publicSourceDir);
                md5 = Utils.getFileMd5(file);
                sb.append(p.packageName).append(":").append(p.versionCode).append(":").append(p.versionName)
                        .append(":").append(md5).append(Constant.COMBINE_SYMBOL_1);
            } catch (Exception e) {
            }
        }
        return sb.toString();
    }

    private static ArrayList<PackageInfo> getPackageInfo() {
        ArrayList<PackageInfo> res = Utils.getPackageInfo();
        PackageInfo gspInfo = Utils.getPackageInfoByName(Constant.AMIGO_PLAY_PACKAGE_NAME);
        if (gspInfo != null) {
            res.add(gspInfo);
        }
        return res;
    }

    public static boolean hasNewVersion(String packageName) {
        init();
        PackageInfo p = Utils.getPackageInfoByName(packageName);
        if (p == null) {
            return false;
        }
        for (int i = 0; i < sAppInfo.size(); i++) {
            if (sAppInfo.get(i).mPackageName.equals(packageName)) {
                if (sAppInfo.get(i).mNewVersionCode > p.versionCode) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isUpgrade(String packageName) {
        init();
        for (int i = 0; i < sAppInfo.size(); i++) {
            if (sAppInfo.get(i).mPackageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSameSignature(String packageName, String localPath) {
        try {
            PackageManager pm = BaseApplication.getAppContext().getPackageManager();
            PackageInfo p1 = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            String apkSignatures = Utils.getPackageSignatureByPath(pm, localPath);
            if (p1.signatures[0].toCharsString().equals(apkSignatures)) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    public static boolean isIncreaseType(String packageName) {
        init();
        for (int i = 0; i < sAppInfo.size(); i++) {
            if (sAppInfo.get(i).mPackageName.equals(packageName)) {
                return sAppInfo.get(i).mIsIncreaseType;
            }
        }
        return false;
    }

    public static UpgradeAppInfo getOneAppInfo(String packageName) {
        init();
        for (int i = 0; i < sAppInfo.size(); i++) {
            if (sAppInfo.get(i).mPackageName.equals(packageName)) {
                return sAppInfo.get(i);
            }
        }
        return null;
    }

    private static UpgradeAppInfo createUpgradeAppInfo(JSONObject jsonObject) {
        try {
            String packageName = jsonObject.getString(PACKAGE);
            long gameId = Long.parseLong(jsonObject.getString(GAME_ID));
            String newVersionCode = jsonObject.getString(NEW_VERSIONCODE).trim();
            String newVersionName = jsonObject.getString(NEW_VERSIONNAME).trim();
            String gameSize = jsonObject.getString(SIZE);
            String downloadUrl = jsonObject.getString(DOWNLOAD_URL);
            String patchUrl = jsonObject.optString(PATCH_URL).trim();
            String patchSize = jsonObject.optString(PATCH_SIZE).trim();
            String upgradeInfo = jsonObject.getString(UPGRADE_INFO).trim();
            String iconUrl = jsonObject.optString(ICON_URL).trim();
            String downCount = jsonObject.optString(JsonConstant.DOWN_COUNT);

            boolean isIncreaseType = false;
            String source = StatisValue.combine(StatisValue.UPGRADE_LIST, StatisValue.UPDATE);
            if (!patchUrl.isEmpty() && Float.parseFloat(patchSize) > 0) {
                isIncreaseType = true;
                source = StatisValue.combine(StatisValue.UPGRADE_LIST, StatisValue.SPLIT);
            } else {
                patchSize = Constant.ZERO;
            }

            PackageManager pm = BaseApplication.getAppContext().getPackageManager();
            PackageInfo packageInfo = Utils.getPackageInfoByName(packageName);
            if (packageInfo != null) {
                ApplicationInfo appInfo = packageInfo.applicationInfo;
                String gameName = appInfo.loadLabel(pm).toString();
                UpgradeAppInfo upInfo = new UpgradeAppInfo(gameId, gameName, packageName, gameSize, iconUrl,
                        downloadUrl);
                upInfo.mRewardData = DownloadArgsFactory.createRewardData(jsonObject);
                upInfo.mNewVersionCode = Integer.parseInt(newVersionCode);
                upInfo.mNewVersionName = newVersionName;
                upInfo.mCurVersionName = packageInfo.versionName;
                upInfo.mUpgradeInfo = upgradeInfo;
                upInfo.mPatchUrl = patchUrl;
                upInfo.mPatchSize = patchSize;
                upInfo.mSource = source;
                upInfo.mIsIncreaseType = isIncreaseType;
                upInfo.mDownloadCount = downCount;
                return upInfo;
            }
        } catch (JSONException e) {
        }
        return null;
    }

    public static void setGamesUpgradeNotify(boolean needNotify) {
        SharePrefUtil.putBoolean(GAMES_UPGRADE_NOTIFY_KEY, needNotify);
        GameListenerManager.onEvent(GameListenerManager.GAMES_UPDATE_CHANGE);
    }

    public static boolean hasGameNeedUpgrade() {
        return SharePrefUtil.getBoolean(GAMES_UPGRADE_NOTIFY_KEY, false);
    }

    public static void refreshCornerIcon(String packageName) {
        if (!GamesUpgradeManager.hasGameNeedUpgrade()) {
            return;
        }
        if (!GamesUpgradeManager.removeAppInfo(packageName)) {
            return;
        }
        if (GamesUpgradeManager.getAppInfoSize() == 0) {
            GamesUpgradeManager.setGamesUpgradeNotify(false);
        }
        Utils.refreshCornerIcon(GamesUpgradeManager.getAppInfoSize());
    }

    private static boolean removeAppInfo(String packageName) {
        try {
            init();
            for (int i = 0; i < sAppInfo.size(); i++) {
                if (sAppInfo.get(i).mPackageName.equals(packageName)) {
                    sAppInfo.remove(i);
                    saveAppInfo();
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static void clearCornerIcon() {
        Utils.refreshCornerIcon(Constant.CLEAR_LAUNCHER_NOTIFY);
    }

    public static class UpgradeAppInfo extends GameListData {
        public int mNewVersionCode;
        public String mCurVersionName;
        public String mNewVersionName;
        public boolean mIsIncreaseType;
        public String mPatchUrl;
        public String mPatchSize;
        public String mUpgradeInfo;

        public UpgradeAppInfo(long gameId, String gameName, String packageName, String gameSize,
                String iconUrl, String downloadUrl) {
            super(gameId, gameName, packageName, gameSize, iconUrl, downloadUrl);
        }

        public boolean isEquals(UpgradeAppInfo info) {
            return info.mNewVersionCode == mNewVersionCode && info.mUpgradeInfo.equals(mUpgradeInfo)
                    && info.mIsIncreaseType == mIsIncreaseType && info.mDownloadUrl.equals(mDownloadUrl)
                    && info.mPatchUrl.equals(mPatchUrl);
        }
    }

}
