package com.cy.frame.downloader.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.cy.constant.Constant;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.downloadmanager.DownloadDB;
import com.cy.frame.downloader.statis.StatisValue;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager;
import com.cy.frame.downloader.util.JsonConstant;
import com.cy.frame.downloader.util.JsonUtils;
import com.cy.frame.downloader.util.UrlConstant;
import com.cy.global.BaseApplication;
import com.cy.utils.Utils;

public class DownloadUrlUtils {

	/**
     * 当DownloadInfo不包含下载地址的时候，重新从网络获取下载地址
     * @param info
     * @return
     */
    public static int getDownloadUrl(DownloadInfo info) {
//        if (null != info.mRawDownloadUrl && !info.mRawDownloadUrl.contains(HostsProperties.getOfficialDownloadUrlMark())) { // 如果地址非官方的时候，直接就返回，不然还需要经过转换
        if (null != info.mRawDownloadUrl && !info.mRawDownloadUrl.contains("gionee.com")) { // 如果地址非官方的时候，直接就返回，不然还需要经过转换
            return DownloadStatusMgr.REASON_NONE;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put(JsonConstant.GAME_ID, info.gameId);
        if (GamesUpgradeManager.isIncreaseType(info.packageName)) {
            map.put(JsonConstant.TYPE, StatisValue.SPLIT);
            map.put(JsonConstant.MD5, getPkgMd5(info.packageName));
        }

        String data = JsonUtils.postData(UrlConstant.GET_DOWNLOAD_URL, map);
        if (JsonUtils.hasGioneeSign(data)) {
            parseDownloadUrl(info, data);
        }
        return DownloadStatusMgr.REASON_NONE;
    }

    private static void parseDownloadUrl(final DownloadInfo info, final String data) {
        try {
            String jsonData = new JSONObject(data).getString(JsonConstant.DATA);
            JSONObject json = new JSONObject(jsonData);
            String gameUrl = json.optString(JsonConstant.DOWN_URL);
            if (!TextUtils.isEmpty(gameUrl) && !gameUrl.equals(info.mRawDownloadUrl)) {
                info.mRawDownloadUrl = gameUrl;
                info.downUrl = gameUrl;
                DownloadDB.getInstance().update(info);
            }
        } catch (JSONException e) {
        }
    }

    private static String getPkgMd5(String pkgName) {
        PackageManager packageManger = BaseApplication.getAppContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManger.getPackageInfo(pkgName, 0);
            File file = new File(packageInfo.applicationInfo.sourceDir);
            return Utils.getFileMd5(file);
        } catch (NameNotFoundException e) {
        }
        return Constant.EMPTY;
    }
}
