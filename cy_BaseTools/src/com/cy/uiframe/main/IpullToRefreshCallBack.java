package com.cy.uiframe.main;

public interface IpullToRefreshCallBack {
	public void checkDataByPull();
	public void onPullRefreshBegin();
	public void onPullRefreshComplete();
	public boolean isReadyToBeginPull();
}
