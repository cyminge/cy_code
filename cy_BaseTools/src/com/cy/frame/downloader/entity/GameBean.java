package com.cy.frame.downloader.entity;

import com.cy.frame.downloader.download.entity.DownloadArgs;

public class GameBean extends DownloadArgs {
	
	public String ad_id;
	public String resume;
	public int hot; // 熱度 mSubsript
	public float score;  // 評分
	public String category; // 分類
	
	public String img;  // 遊戲圖標
	public String link;  // 下載鏈接地址
	public int commonGift;
	public int vipGift;
	public String freedl;
	public int attach;
	public String viewType;
	
	public boolean hasGift() {
		return attach == 1;
	}
}
