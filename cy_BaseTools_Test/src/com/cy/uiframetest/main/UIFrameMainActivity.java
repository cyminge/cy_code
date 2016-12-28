package com.cy.uiframetest.main;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cy.global.BaseActivity;
import com.cy.test.R;

public class UIFrameMainActivity extends BaseActivity {

	private UIFrameLaunchActivityHelper mLaunchActivityHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String url = "http://game.gionee.com/api/activity/getTabs";
		mLaunchActivityHelper = new UIFrameLaunchActivityHelper(this, url, R.layout.activity_uiframe_main);
		setContentView(mLaunchActivityHelper.getRootView(), new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		mLaunchActivityHelper.initLoad();
	}

}
