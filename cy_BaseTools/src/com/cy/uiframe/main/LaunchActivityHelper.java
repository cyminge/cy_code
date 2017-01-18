package com.cy.uiframe.main;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.cy.constant.Constant;
import com.cy.global.WatchDog;
import com.cy.uiframe.main.load.AbstractLoadDataHelper;
import com.cy.uiframe.main.load.BaseLoadDataHelper;
import com.cy.uiframe.main.load.ILoadDataHelper;
import com.cy.uiframe.main.load.IUrlBean;
import com.cy.uiframe.main.parse.Parser;
import com.cy.uiframe.main.parse.Parser.ParserCallBack;
import com.cy.uiframe.pulltorefresh.IpullToRefreshCallBack;
import com.cy.uiframe.pulltorefresh.PullToRefreshView;
import com.cy.uiframe.pulltorefresh.PullToRefreshViewHelper;

@SuppressLint("NewApi") 
public class LaunchActivityHelper<T> implements IpullToRefreshCallBack, ILoadDataHelper, ParserCallBack<T> {

	protected Context mContext;
	private AbstractLoadDataHelper mAbstractLoadDataHelper;

	private ViewContainer mViewContainer;
	private PullToRefreshView mPullToRefreshView;
	private ContentRootView mContentRootView;

	private PullToRefreshViewHelper mViewHelper;
	
	protected Parser<T> mParser;

//	public LaunchActivityHelper(Context context, String url) { //?? 按照现在的思路，数据有效性的检查放到IUrlBean中，这个构造函数可以去掉，不再提供默认的IUrlBean。
//		this(context, url, Constant.NULL_LAYOUT_ID);
//	}

//	public LaunchActivityHelper(Context context, String url, int layoutId) {   //?? 按照现在的思路，数据有效性的检查放到IUrlBean中，这个构造函数可以去掉，不再提供默认的IUrlBean。
//		IUrlBean urlBean = new SingleUrlBean(url);
//		init(context, urlBean, layoutId);
//	}
	
	/**
	 * layout's parent View is FrameLaout. 
	 * if your layout tag is not merge, you should override isAttachToRoot()
	 * @param context
	 * @param urlBean
	 */
	public LaunchActivityHelper(Context context, IUrlBean urlBean) {
		this(context, urlBean, Constant.NULL_LAYOUT_ID);
	}

	/**
	 * layout's parent View is FrameLaout. 
	 * if your layout tag is not merge, you should override isAttachToRoot()
	 * @param context
	 * @param urlBean
	 * @param layoutId
	 */
	public LaunchActivityHelper(Context context, IUrlBean urlBean, int layoutId) {
		init(context, urlBean, layoutId);
	}

	private void init(Context context, IUrlBean urlBean, int layoutId) {
		mContext = context;
		mParser = createParser();
		mAbstractLoadDataHelper = createLoadDataHelper(urlBean, this);
		initLayout(context, layoutId);
	}
	
	/**
	 * 创建加载数据的Helper类
	 * @param urlBean
	 * @param iLoadDataHelper
	 * @return
	 */
	protected AbstractLoadDataHelper createLoadDataHelper(IUrlBean urlBean, ILoadDataHelper iLoadDataHelper) {
		return new BaseLoadDataHelper(urlBean, this);
	}
	
	protected Parser<T> createParser() {
		return new Parser<>(this);
	}
	
	@Override
	public void onParse(ArrayList<T> list, int exceptionType) {
		
	}
	
	public boolean isLastPage() {
		return mParser.isLastPage();
	}

	public boolean isFirstPageData() {
		return mParser.isFirstPageData();
	}

	private void initLayout(Context context, int layoutId) {
		mViewContainer = new ViewContainer(context);
		mContentRootView = new ContentRootView(context);
		
		if (layoutId != Constant.NULL_LAYOUT_ID) {
			View contentView = null;
			if(isAttachToRoot()) {
				contentView = LayoutInflater.from(context).inflate(layoutId, mContentRootView, true);
			} else {
				contentView = LayoutInflater.from(context).inflate(layoutId, null);
	            mContentRootView.addView(contentView, 0);
			}
			
            prepareView(contentView);
        }
		
		mPullToRefreshView = new PullToRefreshView(context, mContentRootView, this);
		mViewHelper = new PullToRefreshViewHelper(getRootView(), mPullToRefreshView);
		mViewContainer.addPullView(mPullToRefreshView);
	}
	
	protected boolean isAttachToRoot() {
		return true;
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
		Log.e("cyTest", "mContentRootView.getScrollY():"+mContentRootView.getScrollY());
		return mContentRootView.getScrollY() == 0;
	}

	@Override
	public void showNoNetworkView() {
		mViewHelper.showExceptionView(ViewHelper.TYPE_NO_NETWORK, isNeedShadowAtTopWhenUnnet());
	}

	protected boolean isNeedShadowAtTopWhenUnnet() {
		return false;
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
	public boolean parseData(String data) {
		return mParser.parse(data);
	}

	@Override
	public void showContentView() {
		mViewHelper.showContent();
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
