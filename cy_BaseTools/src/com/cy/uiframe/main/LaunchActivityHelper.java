package com.cy.uiframe.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.cy.constant.Constant;
import com.cy.global.WatchDog;
import com.cy.uiframe.main.load.AbstractLoadDataHelper;
import com.cy.uiframe.main.load.ILoadDataHelper;
import com.cy.uiframe.main.load.IUrlBean;
import com.cy.uiframe.main.load.SingleUrlBean;
import com.cy.uiframe.pulltorefresh.IpullToRefreshCallBack;
import com.cy.uiframe.pulltorefresh.PullToRefreshView;
import com.cy.uiframe.pulltorefresh.PullToRefreshViewHelper;

@SuppressLint("NewApi") 
public class LaunchActivityHelper implements IpullToRefreshCallBack, ILoadDataHelper {

	private Context mContext;
	private AbstractLoadDataHelper mAbstractLoadDataHelper;

	private ViewContainer mViewContainer;
	private PullToRefreshView mPullToRefreshView;
	private ContentRootView mContentRootView;

	private PullToRefreshViewHelper mViewHelper;

	public LaunchActivityHelper(Context context, String url) {
		this(context, url, Constant.NULL_LAYOUT_ID);
	}

	public LaunchActivityHelper(Context context, String url, int layoutId) {
		IUrlBean urlBean = new SingleUrlBean(url);
		init(context, urlBean, layoutId);
	}

	public LaunchActivityHelper(Context context, IUrlBean urlBean, int layoutId) {
		init(context, urlBean, layoutId);
	}

	private void init(Context context, IUrlBean urlBean, int layoutId) {
		mContext = context;
		mAbstractLoadDataHelper = createLoadDataHelper(urlBean, this);
		initLayout(context, layoutId);
	}
	
	protected AbstractLoadDataHelper createLoadDataHelper(IUrlBean urlBean, ILoadDataHelper iLoadDataHelper) {
		return new BaseLoadDataHelper(urlBean, this);
	}

	private void initLayout(Context context, int layoutId) {
		mViewContainer = new ViewContainer(context);
		mContentRootView = new ContentRootView(context);
		
		if (layoutId != Constant.NULL_LAYOUT_ID) {
            View contentView = LayoutInflater.from(context).inflate(layoutId, mContentRootView, true); //??
            prepareView(contentView);
//            mContentRootView.addView(contentView, 0);
        }
		
		mPullToRefreshView = new PullToRefreshView(context, mContentRootView, this);
		mViewContainer.addPullView(mPullToRefreshView);
		mViewHelper = new PullToRefreshViewHelper(getRootView(), mPullToRefreshView);
	}
	
	protected void prepareView(View contentView) {
		
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
	public void onPullRefreshBegin() {

	}
	
	@Override
	public void onPullRefreshComplete() {

	}

	@Override
	public boolean isReadyToBeginPull() {
		return mContentRootView.getScrollY() == 0;
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
		return null != data && !data.isEmpty();
	}

	@Override
	public void showContentView() {
		
	}

	@Override
	public void showNoDataView() {
		mViewHelper.showExceptionView(ViewHelper.TYPE_NO_DATA, isNeedShadowAtTopWhenUnnet());
	}

	@Override
	public void showLoadingView() {
		mViewHelper.showLoadingView();
	}

	@Override
	public void showNetTimeoutOrDataErrorView() {
		mViewHelper.showExceptionView(ViewHelper.TYPE_NO_NETWORK, isNeedShadowAtTopWhenUnnet());
	}

	@Override
	public boolean isShowingLoadingView() {
		return mViewHelper.isLoading();
	}
}
