package com.cy.uiframe.main;

public interface ILoadDataHelper {
//	public void initLoad();
//    public void exit();
    public void showNoNetworkView();
    public void onCheckDataByLoading();
    public void onNetworkError();
    public boolean isRequestDataSuccess();
    public boolean isCacheAssociatedWithAccount();
    public String getCurrentAccount();
    public String startPullRefresh();
    public boolean onParseData();
}
