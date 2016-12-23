package com.cy.uiframe.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.uiframe.main.ContentView;
import com.cy.uiframe.main.IViewHelper;

public class PullToRefreshView extends PullToRefreshBase<ContentView> {
	
	private IViewHelper mViewHelper;

	private OnRefreshListener<ContentView> mOnRefreshListener = new OnRefreshListener<ContentView>() {
		@Override
		public void onRefresh(PullToRefreshBase<ContentView> refreshView) {
			// if (Utils.hasNetwork()) {
			// AbstractGameView gameView = refreshView.getRefreshableView();
			// gameView.checkDataByPull();
			// } else {
			// ToastUtils.showLimited(R.string.str_no_net_msg);
			// onRefreshComplete();
			// }
			mViewHelper.checkDataByPull();
		}

	};

	public PullToRefreshView(Context context, ContentView contentView, IViewHelper viewHelper) {
		super(context, null, contentView);
		setOnRefreshListener(mOnRefreshListener);
		mViewHelper = viewHelper;
	}

	public ContentView getGameView() {
		return getRefreshableView();
	}

	@Override
	protected ContentView createRefreshableView(Context context, AttributeSet attrs) {
		return mRefreshableView;
	}

	@Override
	protected void pullRefreshBegin() {
		// mRefreshableView.pullRefreshBegin();
		mViewHelper.pullRefreshBegin();
	}

	@Override
	protected void pullRefreshComplete() {
		// mRefreshableView.pullRefreshComplete();
		mViewHelper.pullRefreshComplete();
	}

	@Override
	protected boolean isReadyForPullStart() {
		return mPullRefreshEnable && mViewHelper.isReadyForPullStart();
	}

}
