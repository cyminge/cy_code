package com.cy.uiframetest.main;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;

import com.cy.test.R;
import com.cy.uiframe.main.LaunchActivityHelper;
import com.cy.uiframe.main.load.AbstractLoadDataHelper;
import com.cy.uiframe.main.load.ILoadDataHelper;
import com.cy.uiframe.main.load.IUrlBean;
import com.cy.uiframe.recyclerview.AbstractRecyclerView;
import com.cy.uiframetest.receclerview.BaseBean;

@SuppressWarnings("unchecked")
public class UIFrameLaunchActivityHelper extends LaunchActivityHelper {
	
	private AbstractRecyclerView<BaseBean> mAbstractRecyclerView;

	public UIFrameLaunchActivityHelper(Context context, String url, int layoutId) {
		super(context, url, layoutId);
	}
	
	@Override
	protected AbstractLoadDataHelper createLoadDataHelper(IUrlBean urlBean, ILoadDataHelper loadDataHelper) {
		return new UIFrameLoadDataHelper(urlBean, loadDataHelper);
	}
	
	@Override
	protected void prepareView(View contentView) {
		mAbstractRecyclerView = (AbstractRecyclerView<BaseBean>) contentView.findViewById(R.id.recycle_view);
		View headerView = prepareHeaderView();
		if(null != headerView) {
			mAbstractRecyclerView.addHeaderView(headerView);
		}
	}

	protected View prepareHeaderView() {
		return null;
	}
	
	@Override
	public boolean onParseData(String data) {
		 JSONObject sourceObject = new JSONObject(data);
		 JSONObject dataOjb =  sourceObject.getJSONObject("data");
		
//		try {
//            String str = new JSONObject(data).getString("data");
//            mTabView = Utils.getInflater().inflate(R.layout.tab_viewpager_content, null);
//            mViewPageHelper = new ViewPagerHelper(mActivity, mTabView, data);
//            addView(mTabView);
//
//            return true;
//        } catch (Exception e) {
//            ExceptionSendUtils.sendDataErrorInfo(TAG, jsonData, e);
//            return false;
//        }
		return false;
	}
	
}
