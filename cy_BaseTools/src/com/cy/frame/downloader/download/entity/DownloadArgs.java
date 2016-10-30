package com.cy.frame.downloader.download.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.cy.constant.Constant;
import com.cy.frame.downloader.entity.IconData;
import com.cy.frame.downloader.statis.StatisValue;
import com.cy.frame.downloader.util.JsonConstant;
import com.cy.utils.Utils;

public class DownloadArgs extends IconData implements Cloneable {

    public String mDownloadCount = Constant.EMPTY;

    public static class RewardData {
        public int mTypeCount = Constant.NONE;
        public String mRemindDes = Constant.EMPTY;
        public int mRewardStatisId = Constant.NONE;
    }

    public String mSource = Constant.EMPTY;
    public long mGameId = -1;
    public String mGameName = Constant.EMPTY;
    public String mDownloadUrl = Constant.EMPTY;
    public String mPackageName = Constant.EMPTY;
    public String mGameSize = Constant.EMPTY;
    public RewardData mRewardData = null;
    public boolean mIsSilentDownload = false;
    public boolean mAutoDownload = false;
    public int mVersionCode = -1;

    protected DownloadArgs() {

    }

    private DownloadArgs(String source, long gameId, String downloadUrl, String packageName, String gameSize) {
        mSource = source;
        mGameId = gameId;
        mDownloadUrl = downloadUrl;
        mPackageName = packageName;
        mGameSize = Utils.trimSize(gameSize);
    }

    public DownloadArgs(String source, long gameId, String gameName, String downloadUrl, String packageName,
            String gameSize, String iconUrl) {
        this(source, gameId, downloadUrl, packageName, gameSize);
        mGameName = gameName;
        mIconUrl = iconUrl;
    }

    public DownloadArgs(long gameId, String downloadUrl, String packageName, String gameSize, int versionCode) {
        this(StatisValue.WASH_GAME, gameId, downloadUrl, packageName, gameSize);
        this.mVersionCode = versionCode;
        this.mIsSilentDownload = true;
    }

    public DownloadArgs(JSONObject json) throws JSONException {
        mSource = json.optString(JsonConstant.SOURCE);
        mGameId = Long.parseLong(json.getString(JsonConstant.GAME_ID));
        mGameName = json.getString(JsonConstant.GAME_NAME);
        mDownloadUrl = json.getString(JsonConstant.DOWN_URL);
        mPackageName = json.getString(JsonConstant.PACKAGE_NAME);
        mGameSize = Utils.trimSize(json.getString(JsonConstant.GAME_SIZE));
        mIconUrl = json.getString(JsonConstant.ICON_URL);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public boolean isInvalid() {
        return Utils.isUrlInvalid(mDownloadUrl) || isIconUrlInvalid(mIconUrl);
    }

    private boolean isIconUrlInvalid(String mIconUrl) {
        if (mIconUrl == null ) { // || GamesUpgradeManager.isUpgrade(mIconUrl)
            return false;
        } else {
            return Utils.isUrlInvalid(mIconUrl);
        }
    }

}
