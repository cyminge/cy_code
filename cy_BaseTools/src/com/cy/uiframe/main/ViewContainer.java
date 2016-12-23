package com.cy.uiframe.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.cy.R;

public class ViewContainer {
	private FrameLayout mRootView;
	
	public ViewContainer(Context context) {
		this(context, R.layout.uiframe_load_helper_view);
	}
	
	public ViewContainer(Context context, int rootLayoutId) {
		mRootView = (FrameLayout) (LayoutInflater.from(context).inflate(rootLayoutId, null));
	}
	
	public FrameLayout getRootView() {
		return mRootView;
	}
	
	protected void addPullView(View pullView) {
		mRootView.addView(pullView, 0);
    }
	
}
