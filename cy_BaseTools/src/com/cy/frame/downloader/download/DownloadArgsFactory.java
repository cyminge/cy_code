package com.cy.frame.downloader.download;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.cy.constant.Constant;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.statis.StatisValue;
import com.cy.frame.downloader.util.JsonConstant;
import com.cy.frame.downloader.util.JsonUtils;
import com.cy.frame.downloader.util.UrlConstant;
import com.cy.global.InitialWatchDog;
import com.cy.threadpool.NomalThreadPool;
import com.cy.utils.Utils;

@SuppressLint("NewApi")
public class DownloadArgsFactory {

    public static interface DownloadArgsListener {
        public void onGetDownloadArgs(DownloadArgs downloadArgs);
    }

    private static final String GAME_ID = "game_id";
    private String mGameId;
    private String mGamePkgName;
    private String mSource;
    private String mFrom = StatisValue.FROM_GN;
    private DownloadArgsListener mListener;
    private boolean mThreadStop = true;

    private Runnable mDownloadArgsRunnable = new Runnable() {
        public void run() {
            while (!mThreadStop) {
                String downloadArgs = postData();
                if (needWait(downloadArgs)) {
                    blockThread();
                } else {
                    mThreadStop = true;
                    parseDownloadArgs(downloadArgs);
                }
            }
        }
    };

    public DownloadArgsFactory(String gameId, DownloadArgsListener listener, String source) {
        this(null, gameId, Constant.EMPTY, listener);
        mSource = source;
    }

    public DownloadArgsFactory(Activity activity, String gameId, DownloadArgsListener listener) {
        this(activity, gameId, Constant.EMPTY, listener);
    }

    public DownloadArgsFactory(Activity activity, String gameId, String gamePkgName,
            DownloadArgsListener listener) {
        mGameId = gameId;
        mGamePkgName = gamePkgName;
        mListener = listener;
        init(activity);
        startGet();
    }

    private void init(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = activity.getIntent();
        initSource(activity, intent);
        initFrom(intent);
    }

    private void initSource(Activity activity, Intent intent) {
        // String curSource = ((GNBaseActivity) activity).getSource();
        // String preString = intent.getStringExtra(JsonConstant.SOURCE);
        // if (TextUtils.isEmpty(preString)) {
        // preString = StatisSourceManager.getInstance().getPreSource();
        // }
        mSource = StatisValue.combine("", "");
    }

    private void initFrom(Intent intent) {
        String from = intent.getStringExtra(JsonConstant.FROM);
        if (from == null || from.isEmpty() || !StatisValue.FROM_BAIDU.equals(from)) {
            mFrom = StatisValue.FROM_GN;
        } else {
            mFrom = from;
        }
    }

    public void getDownloadArgs() {
        if (mThreadStop) {
            startGet();
        }
    }

    private void startGet() {
        if (Utils.hasNetwork()) {
            mThreadStop = false;
            NomalThreadPool.getInstance().post(mDownloadArgsRunnable);
        }
    }

    public void onDestroy() {
        stopThread();
    }

    private void stopThread() {
        synchronized (this) {
            mThreadStop = true;
            notify();
        }
    }

    private void blockThread() {
        Activity activity = InitialWatchDog.INSTANCE.getTopActivity();
        if (activity != null && activity.isFinishing()) {
            return;
        }

        synchronized (this) {
            try {
                wait(Constant.SECOND_1);
            } catch (InterruptedException e) {
            }
        }
    }

    private boolean needWait(String downloadArgs) {
        return JsonUtils.isRequestDataFail(downloadArgs);
    }

    private void parseDownloadArgs(String downloadArgs) {
        if (JsonUtils.isRequestDataSuccess(downloadArgs)) {
            try {
                DownloadArgs args = createDownloadArgs(downloadArgs);
                if (mListener != null) {
                    mListener.onGetDownloadArgs(args);
                }
            } catch (Exception e) {
                Log.e("cyTest", "解析下载文件信息出错");
                e.printStackTrace();
            }
        }
    }

    protected String postData() {
        return JsonUtils.postData(UrlConstant.DOWNLOAD_ARGS, getPostMap()); // 这是个什么鬼？
    }

    protected Map<String, String> getPostMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GAME_ID, mGameId);
        map.put(JsonConstant.FROM, mFrom);
        if (TextUtils.isEmpty(mGameId)) {
            map.put(JsonConstant.PACKAGE_NAME, mGamePkgName);
        }
        return map;
    }

    protected DownloadArgs createDownloadArgs(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);
        DownloadArgs downloadArgs = createDownloadArgs(json, mSource);
        if (downloadArgs.isInvalid()) {
            throw new JSONException("args invalid");
        }
        return downloadArgs;
    }

    public static DownloadArgs createDownloadArgs(JSONObject json, String source) throws JSONException {
        String gameName = json.getString(JsonConstant.GAME_NAME);
        String gameId = json.getString(JsonConstant.GAME_ID);
        String gameUrl = json.getString(JsonConstant.DOWN_URL);
        String gamePackage = json.getString(JsonConstant.PACKAGE_NAME);
        String gameSize = json.getString(JsonConstant.FILE_SIZE);
        String gameIconUrl = json.getString(JsonConstant.ICON_URL);
        String downloadCount = json.optString(JsonConstant.DOWN_COUNT);
        DownloadArgs downloadArgs = new DownloadArgs(source, Long.parseLong(gameId), gameName, gameUrl,
                gamePackage, gameSize, gameIconUrl);
        downloadArgs.mDownloadCount = downloadCount;
        downloadArgs.mRewardData = createRewardData(json);
        return downloadArgs;
    }

    public static DownloadArgs.RewardData createRewardData(JSONObject json) {
        try {
            JSONObject rewardObject = json.getJSONObject(JsonConstant.REWARD);
            DownloadArgs.RewardData data = new DownloadArgs.RewardData();
            data.mTypeCount = rewardObject.getInt(JsonConstant.REWARD_TYPE_COUNT);
            data.mRemindDes = rewardObject.getString(JsonConstant.REWARD_REMIND_DES)
                    .replaceAll("\\\\n", "\n");
            data.mRewardStatisId = rewardObject.getInt(JsonConstant.REWARD_STATIS_ID);
            return data;
        } catch (JSONException e) {
        }
        return null;
    }
}
