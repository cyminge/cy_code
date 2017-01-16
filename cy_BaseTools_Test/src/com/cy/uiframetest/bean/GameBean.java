package com.cy.uiframetest.bean;

public class GameBean extends DownloadArgsBean {
	
	public String ad_id;
	public String resume;
	public int hot; // mSubsript
	public float score;
	public String category;
	
	public String img;
	public String link;
	public int commonGift;
	public int vipGift;
	public String freedl;
	public int attach;
	public String viewType;
	
	public boolean hasGift() {
		return attach == 1;
	}
}
