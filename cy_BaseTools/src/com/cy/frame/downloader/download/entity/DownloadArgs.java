package com.cy.frame.downloader.download.entity;

import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.utils.Utils;

public class DownloadArgs implements Cloneable {

	public int mStatus = ButtonStatusManager.BUTTON_STATUS_DOWNLOAD;

	public String mSource;
	public boolean mIsSilentDownload = false;
	public boolean mWifiAutoDownload = false;
	public int mVersionCode = -1;

	public String downloadCount; // 下載數
	public String gameId; // 遊戲ID
	public long gameid;
	public String name; // 遊戲名稱
	public String packageName; // package 要改成packageName
	public String size; // 遊戲大小 213.00M
	public String downUrl; // 下载url
	public RewardData reward; // 有獎下載參數

	public class RewardData {
		public String remindDes;
		public int rewardTypeCount;
		public int rewardStatisId;
	}
	
	protected DownloadArgs() {

    }

	public DownloadArgs(String source, long gameId, String gameName, String downloadUrl, String packageName,
			String gameSize) {
		mSource = source;
		gameid = gameId;
		this.gameId = String.valueOf(gameid);
		name = gameName;
		downUrl = downloadUrl;
		this.packageName = packageName;
		name = gameName;
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
		return Utils.isUrlInvalid(downUrl);
	}

}
