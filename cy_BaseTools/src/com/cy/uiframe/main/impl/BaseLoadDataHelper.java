package com.cy.uiframe.main.impl;

import com.cy.uiframe.main.AbstractLoadDataHelper;
import com.cy.uiframe.main.ILoadDataHelper;
import com.cy.uiframe.main.IUrlBean;

public class BaseLoadDataHelper extends AbstractLoadDataHelper {
	
	private ILoadDataHelper mILoadDataHelper;

	public BaseLoadDataHelper(IUrlBean urlBean, ILoadDataHelper loadDataHelper) {
		super(urlBean);
		mILoadDataHelper = loadDataHelper;
	}

	@Override
	protected void showNoNetworkView() {
		mILoadDataHelper.showNoNetworkView();
	}

	@Override
	protected void onCheckDataByLoading() {
		mILoadDataHelper.onCheckDataByLoading();
	}

	@Override
	protected void onLoadDataError() {
		mILoadDataHelper.onLoadDataError();
	}

	@Override
	protected boolean isRequestDataSuccess(String result) {
		return mILoadDataHelper.isRequestDataSuccess(result);
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
	protected boolean onParseData(String data) {
		return mILoadDataHelper.onParseData(data);
	}

}
