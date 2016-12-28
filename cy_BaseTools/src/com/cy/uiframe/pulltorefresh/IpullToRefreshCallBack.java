package com.cy.uiframe.pulltorefresh;

public interface IpullToRefreshCallBack {
	/**
	 * 下拉开始，获取数据
	 */
	public void checkDataByPull();
	/**
	 * 刷新开始
	 */
	public void onPullRefreshBegin();
	/**
	 * 刷新结束
	 */
	public void onPullRefreshComplete();
	/**
	 * 是否响应下拉刷新动画
	 */
	public boolean isReadyToBeginPull();
}
