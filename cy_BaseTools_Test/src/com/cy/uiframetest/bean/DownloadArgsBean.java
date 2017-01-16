package com.cy.uiframetest.bean;

import com.cy.frame.downloader.controller.ButtonStatusManager;

public class DownloadArgsBean implements Cloneable {
	
	public int mStatus = ButtonStatusManager.BUTTON_STATUS_DOWNLOAD;
	
	public String mSource;
	public boolean mIsSilentDownload = false;
	public boolean mWifiAutoDownload = false;
	public int mVersionCode = -1;
	
	public String downloadCount;
	public String gameId;
	public long gameid;
	public String name;
	public String packageName; // package 要改成packageName
	public String size;
	public RewardData reward;
		
	public class RewardData {
		public String remindDes;
		public int rewardTypeCount;
		public int rewardStatisId;
	}
	
}
