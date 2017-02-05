package com.cy.frame.downloader.upgrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.cy.constant.Constant;
import com.cy.frame.downloader.util.JsonConstant;
import com.cy.frame.downloader.util.JsonUtils;
import com.cy.frame.downloader.util.TimeUtils;
import com.cy.frame.downloader.util.UrlConstant;
import com.cy.global.WatchDog;
import com.cy.utils.Utils;
import com.cy.utils.sharepref.SharePrefUtil;

/**
 * 有些应用在列表中需要屏蔽掉。这个是获取需要屏蔽掉的应用。用于做差异化展示或者其他情况。
 * @author JLB6088
 *
 */
public class FilterApps {
    private static final String SYSTEM_SPECIAL_PRELOAD_APP = "com.happyelements.AndroidAnimal.jinli"; // 系统预装应用
    private static final String FILTER_APPS_CACHE_TIME = "filter_apps_cache_time";
    private static final String FILTER_APPS = "filter_apps";
    private static ArrayList<String> sFilterApps = new ArrayList<String>();

    public static void init() {
        String cacheData = SharePrefUtil.getString(FILTER_APPS);
        parseFilterData(cacheData);
    }

    public static void startCheck() {
        if (!isDataExpired()) {
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        String data = JsonUtils.postData(UrlConstant.FILTER_APPS_URL, map);
        parseFilterData(data);
    }

    private static void parseFilterData(String data) {
        sFilterApps.clear();

        try {
            JSONObject object = new JSONObject(data);
            JSONObject json = object.getJSONObject(JsonConstant.DATA);

            JSONArray jsonArray = json.getJSONArray(JsonConstant.ITEMS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                String pkgName = jsonItem.getString(JsonConstant.PACKAGE_NAME);
                sFilterApps.add(pkgName);
            }

            SharePrefUtil.putString(FILTER_APPS, data);
            SharePrefUtil.putLong(FILTER_APPS_CACHE_TIME, System.currentTimeMillis());
        } catch (JSONException e) {
        }
    }

    private static boolean isDataExpired() {
        long cacheTime = SharePrefUtil.getLong(FILTER_APPS_CACHE_TIME, 0);
        return TimeUtils.isExceedLimitTime(cacheTime, System.currentTimeMillis(), Constant.DAY_7);
    }

    public static boolean isFilterApps(String pkgName) {
        return sFilterApps.contains(pkgName);
    }

    public static String getInstalledGamePkgNames() {
        return getInstalledGamePkgNames(false);
    }

    public static String getInstalledGamePkgNames(boolean strictMode) {
        StringBuilder sb = new StringBuilder();
        ArrayList<PackageInfo> infos = Utils.getPackageInfo();
        for (PackageInfo info : infos) {
            String packageName = info.packageName;
            boolean packageError = checkPackageError(strictMode, packageName);
            if (sFilterApps.contains(packageName) || packageError) {
                continue;
            }
            if (SYSTEM_SPECIAL_PRELOAD_APP.equals(packageName)
                    && TextUtils.isEmpty(Utils.getAppName(packageName))) {
                continue;
            }
            sb.append(packageName).append(Constant.COMBINE_SYMBOL_1);
        }

        return sb.toString();
    }

    private static boolean checkPackageError(boolean strictMode, String packageName) {
        if (!strictMode) {
            return false;
        }

        Context context = WatchDog.getContext();
        PackageManager pm = context.getPackageManager();
        boolean packageError = false;
        try {
            pm.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            packageError = true;
        } catch (Exception e) {
            packageError = true;
        }
        return packageError;
    }

    public static void addGamesParam(Map<String, String> params) {
        String installedGamePkgNames = getInstalledGamePkgNames();
        params.put(JsonConstant.PARAM_APP_LIST, installedGamePkgNames);
    }

}
