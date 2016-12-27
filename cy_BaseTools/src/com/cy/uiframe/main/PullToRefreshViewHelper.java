package com.cy.uiframe.main;

import android.view.View;

import com.cy.uiframe.pulltorefresh.PullToRefreshBase;

public class PullToRefreshViewHelper extends ViewHelper {

    protected PullToRefreshBase<?> mPullView;

    public PullToRefreshViewHelper(View rootView, PullToRefreshBase<?> pullView) {
        super(rootView, pullView);
        mPullView = pullView;
    }

    @Override
    public void showContent() {
        super.showContent();
        mPullView.onRefreshComplete();
    }

    @Override
    public void showLoadingView() {
        super.showLoadingView();
        mPullView.onRefreshComplete();
    }
    
    @Override
    public void showExceptionView(int type, boolean isNeedShadowAtTopWhenUnnet) {
    	super.showExceptionView(type, isNeedShadowAtTopWhenUnnet);
    	mPullView.onRefreshComplete();
    }

    @Override
    public void hideAllView() {
        super.hideAllView();
        mPullView.onRefreshComplete();
    }

    public void startPullRefreshing() {
        mPullView.startPullRefreshing();
    }

}
