package com.cy.uiframe.main;

public interface ILoadDataHelper {
//	public void initLoad();
//    public void exit();
    public void showNoNetworkView();
    public void onCheckDataByLoading();
    public void onLoadDataError();
    public boolean isRequestDataSuccess(String data);
    public boolean isCacheAssociatedWithAccount();
    public String getCurrentAccount();
    public void startPullRefresh();
    public boolean onParseData(String data);
}
