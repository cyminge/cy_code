package com.cy.frame.downloader.entity;

import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.download.entity.DownloadArgs;

public class ListData extends DownloadArgs {
    public int mStatus;

    public ListData(long gameId, String gameName, String packageName, String gameSize, String iconUrl,
            String downloadUrl) {
        super(Constant.EMPTY, gameId, gameName, downloadUrl, packageName, gameSize, iconUrl);
        this.mStatus = ButtonStatusManager.BUTTON_STATUS_DOWNLOAD;
    }

    public boolean isEquals(ListData listData) {
        return mPackageName.equals(listData.mPackageName);
    }
}
