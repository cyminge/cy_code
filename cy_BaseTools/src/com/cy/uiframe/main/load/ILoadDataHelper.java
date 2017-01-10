package com.cy.uiframe.main.load;

public interface ILoadDataHelper {
	/**
	 * 数据解析
	 * @param data
	 * @return
	 */
    public boolean parseData(String data);
    /**
	 * 缓存数据是否关联账号信息
	 * @return
	 */
    public boolean isCacheAssociatedWithAccount();
    /**
	 * 获取当前账号的UUID或者账号的唯一标示
	 * 
	 * @return
	 */
    public String getCurrentAccount();
    /**
	 * 是否正在展示loadingView
	 * @return
	 */
    public boolean isShowingLoadingView();
    /**
	 * 需要加载新的数据
	 */
    public void startPullRefresh();
    /**
	 * 显示主体内容view
	 */
    public void showContentView();
    /**
	 * 显示无网络view
	 */
    public void showNoNetworkView();
    /**
     * 数据的主体部分解析出错，数据有误，需要展示给用户??
     */
	public void showNoDataView();
	/**
	 * 展示进度条 or others
	 */
	public void showLoadingView();
	/**
	 * 数据加载超时或者服务器数据有误
	 */
	public void showNetTimeoutOrDataErrorView();
   
}
