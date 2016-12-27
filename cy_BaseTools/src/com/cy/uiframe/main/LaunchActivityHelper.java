package com.cy.uiframe.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.cy.constant.Constant;
import com.cy.global.WatchDog;
import com.cy.uiframe.main.impl.BaseLoadDataHelper;
import com.cy.uiframe.pulltorefresh.PullToRefreshView;

@SuppressLint("NewApi") 
public class LaunchActivityHelper implements IpullToRefreshCallBack, ILoadDataHelper {

	private Context mContext;
	private AbstractLoadDataHelper mAbstractLoadDataHelper;

	private ViewContainer mViewContainer;
	private PullToRefreshView mPullToRefreshView;
	private ContentView mContentView;

	private PullToRefreshViewHelper mViewHelper;

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

		mViewHelper = new PullToRefreshViewHelper(getRootView(), mPullToRefreshView);
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

	/**
	 * 下拉开始，获取数据
	 */
	@Override
	public void checkDataByPull() {
		mAbstractLoadDataHelper.checkDataByPull();
	}

	/**
	 * 刷新开始
	 */
	@Override
	public void onPullRefreshBegin() {

	}

	/**
	 * 刷新结束
	 */
	@Override
	public void onPullRefreshComplete() {

	}

	/**
	 * 是否响应下拉刷新动画
	 */
	@Override
	public boolean isReadyToBeginPull() {
		return mContentView.getScrollY() == 0;
	}

	@Override
	public void showNoNetworkView() {
		mViewHelper.showExceptionView(ViewHelper.TYPE_NO_NETWORK, isNeedShadowAtTopWhenUnnet());
	}

	protected boolean isNeedShadowAtTopWhenUnnet() {
		return false;
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
            	mViewHelper.startPullRefreshing();
            }
        }, Constant.SECOND_1);
	}

	@Override
	public boolean onParseData(String data) {
		return false;
	}

	@Override
	public boolean isRequestDataSucc(String data) {
		return false;
	}

	@Override
	public void showContentView() {
		
	}

	@Override
	public void showNoDataView() {
		
	}

	@Override
	public void showLoadingView() {
		
	}

	@Override
	public void showNetTimeoutOrDataErrorView() {
		
	}

	@Override
	public boolean isShowingLoadingView() {
		return false;
	}
}
