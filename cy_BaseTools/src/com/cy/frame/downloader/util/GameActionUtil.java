package com.cy.frame.downloader.util;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cy.constant.Constant;
import com.cy.threadpool.NormalThreadPool;
import com.cy.utils.Utils;

public class GameActionUtil {
    private static final int POST_GAME_ACTION_RETRY_MAX = 5;
    private static final String GAME_ACTION_KEY = "gameAction";
    private static Runnable sTask = new Runnable() {
        @Override
        public void run() {
            checkGameActionCache();
        }
    };

    public static void postGameAction(final String packageName, final int type) {
        if (!Utils.isLogin()) {
            return;
        }

        NormalThreadPool.getInstance().post(new Runnable() {
            @Override
            public void run() {
                if (!Utils.hasNetwork()) {
                    storeGameActionCache(packageName, type);
                    return;
                }
                boolean succ = doPostGameAction(type, packageName);
                if (!succ) {
                    storeGameActionCache(packageName, type);
                }
            }
        });
    }

    private static synchronized void storeGameActionCache(String packageName, int type) {
//        try {
//            JSONObject item = new JSONObject();
//            item.put(JsonConstant.PACKAGE_NAME, packageName);
//            item.put(JsonConstant.TYPE, type);
//            String cacheKey = getGameActionCacheKey();
//            JSONArray array;
//            JSONObject rootObject;
//            String cacheValue = PreferenceManager.getString(cacheKey);
//            if (TextUtils.isEmpty(cacheValue)) {
//                array = new JSONArray();
//                array.put(item);
//                rootObject = new JSONObject();
//                rootObject.put(JsonConstant.DATA, array);
//            } else {
//                rootObject = new JSONObject(cacheValue);
//                array = rootObject.getJSONArray(JsonConstant.DATA);
//                array.put(item);
//            }
//            PreferenceManager.putString(cacheKey, rootObject.toString());
//        } catch (JSONException e) {
//        }

    }

    public static void startCacheCheck() {
        NormalThreadPool.getInstance().removeCallbacks(sTask);
        NormalThreadPool.getInstance().postDelayed(sTask, Constant.SECOND_30);
    }

    private static synchronized void checkGameActionCache() {
//        if (!Utils.hasNetwork()) {
//            return;
//        }
//        final String cacheKey = getGameActionCacheKey();
//        final String cacheValue = PreferenceManager.getString(cacheKey);
//        if (TextUtils.isEmpty(cacheValue)) {
//            return;
//        }
//
//        try {
//            JSONObject rootObject = new JSONObject(cacheValue);
//            JSONArray array = rootObject.getJSONArray(JsonConstant.DATA);
//            JSONArray failItems = new JSONArray();
//            int length = array.length();
//            for (int i = 0; i < length; i++) {
//                JSONObject item = array.getJSONObject(i);
//                boolean succ = postGameActionCache(item);
//                if (!succ) {
//                    failItems.put(item);
//                }
//            }
//            if (failItems.length() == 0) {
//                PreferenceManager.remove(cacheKey);
//            } else {
//                rootObject.put(JsonConstant.DATA, failItems);
//                PreferenceManager.putString(cacheKey, rootObject.toString());
//            }
//        } catch (JSONException e) {
//        }

    }

    private static String getGameActionCacheKey() {
//        return GAME_ACTION_KEY + AccountManager.getUUID();
    	return null;
    }

    private static boolean postGameActionCache(JSONObject item) {
        try {
            String packageName = item.getString(JsonConstant.PACKAGE_NAME);
            int type = item.getInt(JsonConstant.TYPE);
            return doPostGameAction(type, packageName);
        } catch (JSONException e) {
        }
        return true;
    }

    private static boolean doPostGameAction(int type, String packageName) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(JsonConstant.TYPE, String.valueOf(type));
        map.put(JsonConstant.PACKAGE_NAME, packageName);
        String result;
        int retryCount = 0;
        boolean succ;
        do {
            retryCount++;
            result = JsonUtils.postData(UrlConstant.POST_GAME_ACTION, map);
            succ = JsonUtils.isRequestDataSuccess(result);
            if (succ && type == Constant.ACTION_INSTALL) {
                onInstallNotify(result, packageName);
            }
        } while (!succ && retryCount <= POST_GAME_ACTION_RETRY_MAX);
        return succ;
    }

    private static void onInstallNotify(String result, final String packageName) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject data = jsonObject.getJSONObject(JsonConstant.DATA);
            if (!data.optBoolean(JsonConstant.SEND_STATUS)) {
                return;
            }

            final String title = data.optString(JsonConstant.TITLE);
            final String source = data.optString(JsonConstant.SOURCE);
            NormalThreadPool.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    sendNotify(packageName, title, source);
                }
            });
        } catch (JSONException e) {
        }
    }

    private static void sendNotify(String packageName, String title, String source) {
//        String statisSource = StatisValue.combine(StatisValue.INSTALL_SEND, source);
//
//        Intent intent = new Intent();
//        Activity topActivity = GNApplication.getInstance().getTopActivity();
//        String className = MyGiftListActivity.class.getName();
//        if (topActivity == null || topActivity.getClass().getName().equals(className)) {
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        }
//        intent.putExtra(JsonConstant.SOURCE, statisSource);
//        intent.setClassName(GNApplication.getInstance().getPackageName(), className);
//
//        String content = Utils.getAppName(packageName);
//        RemoteViews contentView = Utils.getRemoteViews(title, content);
//
//        NotificationUtils.CustomNotifyParams data = new NotificationUtils.CustomNotifyParams(contentView,
//                null, intent, NotificationUtils.ACTIVITY);
//        data.setTag(packageName + title);
//        data.setSingleNotify(NotificationUtils.NOTIFICATION_ID_INSTALL);
//
//        NotificationUtils.showCustomViewNotification(data);
//        DownloadUtils.sendStatis(StatisValue.SEND_NEWS, packageName, statisSource);
    }
}
