package com.cy.uiframetest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.cy.test.R;
import com.cy.uiframe.AbstractRecycleView;

public class UIFrameActivity extends Activity {

	private AbstractRecycleView mAbstractRecycleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_uiframe_main);
		mAbstractRecycleView = (AbstractRecycleView) findViewById(R.id.recycle_view);
		
		List<BaseBean> list = new ArrayList<BaseBean>();
		BaseBean bean1 = new BaseBean("敏哥", "30", TestRecycleViewAdapter.LIST_TYPE_FIRST);
		BaseBean bean2 = new BaseBean("芬姐", "29", TestRecycleViewAdapter.LIST_TYPE_SECOND);
		list.add(bean1);
		list.add(bean2);
		
		TestRecycleViewAdapter adapter = new TestRecycleViewAdapter(this, list);
		mAbstractRecycleView.setAdapter(adapter);
	}
}
