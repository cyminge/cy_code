package com.cy.uiframe.main.load;


/**
 * 父类的方法有的可能用不着，可能需要拓展和重写。可以重新定义对外的接口
 * @author JLB6088
 *
 */
public class BaseLoadDataHelper extends AbstractLoadDataHelper {
	
	private ILoadDataHelper mILoadDataHelper;

	public BaseLoadDataHelper(IUrlBean urlBean, ILoadDataHelper loadDataHelper) {
		super(urlBean);
		mILoadDataHelper = loadDataHelper;
	}

	@Override
	protected boolean isCacheAssociatedWithAccount() {
		return mILoadDataHelper.isCacheAssociatedWithAccount();
	}

	@Override
	protected String getCurrentAccount() {
		return mILoadDataHelper.getCurrentAccount();
	}

	@Override
	protected void startPullRefresh() {
		mILoadDataHelper.startPullRefresh();
	}

	@Override
	protected boolean parseData(String data) {
		return mILoadDataHelper.parseData(data);
	}

	@Override
	protected void showContentView() {
		mILoadDataHelper.showContentView();
	}

	@Override
	protected void showNoDataView() {
		mILoadDataHelper.showNoDataView();
	}

	@Override
	protected void showNoNetworkView() {
		mILoadDataHelper.showNoNetworkView();
	}

	@Override
	protected void showLoadingView() {
		mILoadDataHelper.showLoadingView();
	}
	
	@Override
	protected void showNetTimeoutOrDataErrorView() {
		mILoadDataHelper.showNetTimeoutOrDataErrorView();
	}

	@Override
	protected boolean isShowingLoadingView() {
		return mILoadDataHelper.isShowingLoadingView();
	}

	
}
