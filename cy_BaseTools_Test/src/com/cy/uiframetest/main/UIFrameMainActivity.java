package com.cy.uiframetest.main;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cy.global.BaseActivity;
import com.cy.test.R;
import com.cy.uiframe.main.load.IUrlBean;
import com.cy.uiframe.main.load.MultiUrlBean;

/**
 * 问题点： 1. 如何控制下拉方式，如果要换一种刷新机制怎么做到可配置 
 * 2. 添加尾布局 
 * 3. 命名还需要进行优化
 * 4.SlideView控件的结构还需要优化 
 * 5. 现有的框架是否支持不同数据结构
 * 6. 感觉代码好僵硬啊啊啊啊啊 啊啊 啊啊啊啊 怎么做好模块啊啊啊 啊啊啊
 * @author zf
 * 
 */
public class UIFrameMainActivity extends BaseActivity {

	private static final String TAG = "UIFrameMainActivity";

	private static final String LIST_FIRST_PAGE_URL = "http://game.gionee.com/api/Local_Home/newRecomendFirstPageList";
	public static final String SLIDE_ITEMS_URL = "http://game.gionee.com/Api/Local_Home/slideAd";

	private static final String[] URLS = { LIST_FIRST_PAGE_URL, SLIDE_ITEMS_URL };

	private UIFrameLaunchActivityHelper mLaunchActivityHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLaunchActivityHelper = new UIFrameLaunchActivityHelper(this, createUrlBean(), R.layout.activity_uiframe_main);
		setContentView(mLaunchActivityHelper.getRootView(), new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		mLaunchActivityHelper.initLoad();
	}
	
	private IUrlBean createUrlBean() {
		return new MultiUrlBean(URLS);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "MainActivity-dispatchTouchEvent-ACTION_DOWN");
			break;
		case MotionEvent.ACTION_UP:
			Log.i(TAG, "MainActivity-dispatchTouchEvent-ACTION_UP");
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "MainActivity-onTouchEvent-ACTION_DOWN");
			break;
		case MotionEvent.ACTION_UP:
			Log.i(TAG, "MainActivity-onTouchEvent-ACTION_UP");
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

}
