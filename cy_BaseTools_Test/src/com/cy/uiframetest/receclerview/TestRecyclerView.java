package com.cy.uiframetest.receclerview;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.uiframe.recyclerview.AbstractRececlerAdapter;
import com.cy.uiframe.recyclerview.AbstractRecyclerView;

public class TestRecyclerView extends AbstractRecyclerView<BaseBean> {

	public TestRecyclerView(Context context) {
		super(context);
	}
	
	public TestRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public TestRecyclerView(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	@Override
	protected AbstractRececlerAdapter<BaseBean> initAdapter() {
		return new TestRecycleViewAdapter(getContext());
	}

}
