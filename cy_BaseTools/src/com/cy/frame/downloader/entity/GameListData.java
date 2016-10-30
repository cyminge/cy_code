package com.cy.frame.downloader.entity;

import com.cy.constant.Constant;


public class GameListData extends ListData {

    public String mResume;
    public int mSubsript;
    public boolean mHasGift;
    public float mScore;
    public String mAdId;
    public String mTitle = Constant.EMPTY;
    public String mCategory = Constant.EMPTY;
    public long mTime;
    public int mTestLeftCount;
    public int mVipLevel;
    public String mOpenTestType = Constant.EMPTY;

    public GameListData(long gameId, String gameName, String packageName, String gameSize, String iconUrl,
            String downloadUrl) {
        super(gameId, gameName, packageName, gameSize, iconUrl, downloadUrl);
    }

}
