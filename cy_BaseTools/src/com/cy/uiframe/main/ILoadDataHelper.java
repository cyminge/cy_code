package com.cy.uiframe.main;

public interface ILoadDataHelper {
    public void showNoNetworkView();
    public boolean isRequestDataSucc(String data);
    public boolean isCacheAssociatedWithAccount();
    public String getCurrentAccount();
    public void startPullRefresh();
    public boolean onParseData(String data);
    public void showContentView();
	public void showNoDataView();
	public void showLoadingView();
	public void showNetTimeoutOrDataErrorView();
	public boolean isShowingLoadingView();
}
