package com.cy.uiframe.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.cy.constant.Constant;
import com.cy.global.WatchDog;
import com.cy.uiframe.main.impl.BaseLoadDataHelper;
import com.cy.uiframe.pulltorefresh.PullToRefreshView;

@SuppressLint("NewApi") 
public class LaunchActivityHelper implements IViewHelper, ILoadDataHelper {

	private Context mContext;
	private AbstractLoadDataHelper mAbstractLoadDataHelper;

	private ViewContainer mViewContainer;
	private PullToRefreshView mPullToRefreshView;
	private ContentView mContentView;

	private ViewHelper mViewHelper;

	protected LaunchActivityHelper(Context context, String url) {
		this(context, url, Constant.NULL_LAYOUT_ID);
	}

	protected LaunchActivityHelper(Context context, String url, int layoutId) {
		IUrlBean urlBean = new SingleUrlBean(url);
		init(context, urlBean, layoutId);
	}

	protected LaunchActivityHelper(Context context, IUrlBean urlBean, int layoutId) {
		init(context, urlBean, layoutId);
	}

	private void init(Context context, IUrlBean urlBean, int layoutId) {
		mContext = context;
		mAbstractLoadDataHelper = new BaseLoadDataHelper(urlBean, this);
		initLayout(context);
	}

	private void initLayout(Context context) {
		mViewContainer = new ViewContainer(context);
		mContentView = new ContentView(context);
		mPullToRefreshView = new PullToRefreshView(context, mContentView, this);
		mViewContainer.addPullView(mPullToRefreshView);

		mViewHelper = new ViewHelper(getRootView(), mContentView);
	}

	public View getRootView() {
		return mViewContainer.getRootView();
	}

	public void initLoad() {
		mAbstractLoadDataHelper.initLoad();
	}

	public void exit() {
		mAbstractLoadDataHelper.exit();
	}

	@Override
	public void checkDataByPull() {
		mAbstractLoadDataHelper.checkDataByPull();
	}

	@Override
	public void pullRefreshBegin() {

	}

	@Override
	public void pullRefreshComplete() {

	}

	@Override
	public boolean isReadyForPullStart() {
		return mContentView.getScrollY() == 0;
	}

	@Override
	public void showNoNetworkView() {
		mViewHelper.showExceptionView(ViewHelper.TYPE_NO_NETWORK, isNeedShadowAtTopWhenUnnet());
	}

	protected boolean isNeedShadowAtTopWhenUnnet() {
		return false;
	}

	@Override
	public void onCheckDataByLoading() {
		mViewHelper.showLoadingView();
	}

	@Override
	public void onLoadDataError() {
		if (mViewHelper.isLoading()) {
			showNoNetworkView();
		} else {
			mViewHelper.showContent();
		}
	}

	@Override
	public boolean isRequestDataSuccess(String data) {
		if(null == data || data.isEmpty()) {
			return false;
		}
		
		return hasSign(data);
	}
	
	/**
	 * 需要重写
	 * @param data
	 * @return
	 */
	protected boolean hasSign(String data) {
		return true;
	}

	@Override
	public boolean isCacheAssociatedWithAccount() {
		return false;
	}

	@Override
	public String getCurrentAccount() {
		return null;
	}

	@Override
	public void startPullRefresh() {
		WatchDog.postDelayed(new Runnable() {
            @Override
            public void run() {
            	mPullToRefreshView.startPullRefreshing();
            }
        }, Constant.SECOND_1);
	}

	@Override
	public boolean onParseData(String data) {
		return false;
	}
}
