package com.cy.uiframe.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.uiframe.main.ContentView;
import com.cy.uiframe.main.IpullToRefreshCallBack;

public class PullToRefreshView extends PullToRefreshBase<ContentView> {
	
	private IpullToRefreshCallBack mCallBack;

	private OnRefreshListener<ContentView> mOnRefreshListener = new OnRefreshListener<ContentView>() {
		@Override
		public void onRefresh(PullToRefreshBase<ContentView> refreshView) {
			mCallBack.checkDataByPull();
		}

	};

	public PullToRefreshView(Context context, ContentView contentView, IpullToRefreshCallBack viewHelper) {
		super(context, null, contentView);
		setOnRefreshListener(mOnRefreshListener);
		mCallBack = viewHelper;
	}

	public ContentView getContentView() {
		return getRefreshableView();
	}

	@Override
	protected ContentView createRefreshableView(Context context, AttributeSet attrs) {
		return getContentView();
	}

	@Override
	protected void onPullRefreshBegin() {
		mCallBack.onPullRefreshBegin();
	}

	@Override
	protected void onPullRefreshComplete() {
		// mRefreshableView.pullRefreshComplete();
		mCallBack.onPullRefreshComplete();
	}

	@Override
	protected boolean isReadyToBeginPull() {
		return mPullRefreshEnable && mCallBack.isReadyToBeginPull();
	}

}
