package com.cy.uiframe.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.uiframe.main.ContentRootView;

public class PullToRefreshView extends PullToRefreshBase<ContentRootView> {
	
	private IpullToRefreshCallBack mCallBack;

	private OnRefreshListener<ContentRootView> mOnRefreshListener = new OnRefreshListener<ContentRootView>() {
		@Override
		public void onRefresh(PullToRefreshBase<ContentRootView> refreshView) {
			mCallBack.checkDataByPull();
		}

	};

	public PullToRefreshView(Context context, ContentRootView contentView, IpullToRefreshCallBack callback) {
		super(context, null, contentView);
		setOnRefreshListener(mOnRefreshListener);
		mCallBack = callback;
	}

	public ContentRootView getContentView() {
		return getRefreshableView();
	}

	@Override
	protected ContentRootView createRefreshableView(Context context, AttributeSet attrs) {
		return getContentView();
	}

	@Override
	protected void onPullRefreshBegin() {
		mCallBack.onPullRefreshBegin();
	}

	@Override
	protected void onPullRefreshComplete() {
		mCallBack.onPullRefreshComplete();
	}

	@Override
	protected boolean isReadyToBeginPull() {
		return mPullRefreshEnable && mCallBack.isReadyToBeginPull();
	}

}
