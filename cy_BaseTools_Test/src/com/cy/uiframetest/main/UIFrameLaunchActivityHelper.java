package com.cy.uiframetest.main;

import android.content.Context;
import android.view.View;

import com.cy.test.R;
import com.cy.uiframe.main.LaunchActivityHelper;
import com.cy.uiframe.main.load.AbstractLoadDataHelper;
import com.cy.uiframe.main.load.ILoadDataHelper;
import com.cy.uiframe.main.load.IUrlBean;
import com.cy.uiframe.recyclerview.AbstractRecyclerView;

public class UIFrameLaunchActivityHelper extends LaunchActivityHelper {
	
	private AbstractRecyclerView mAbstractRecyclerView;

	public UIFrameLaunchActivityHelper(Context context, String url, int layoutId) {
		super(context, url, layoutId);
	}
	
	@Override
	protected AbstractLoadDataHelper createLoadDataHelper(IUrlBean urlBean, ILoadDataHelper iLoadDataHelper) {
		return super.createLoadDataHelper(urlBean, iLoadDataHelper);
	}
	
	@Override
	protected void prepareView(View contentView) {
		super.prepareView(contentView);
		
//		mAbstractRecyclerView = (AbstractRecyclerView) contentView.findViewById(R.id.recycle_view);
	}

}
