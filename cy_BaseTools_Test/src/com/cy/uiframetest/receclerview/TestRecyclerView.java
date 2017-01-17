package com.cy.uiframetest.receclerview;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.uiframe.recyclerview.AbstractRececlerAdapter;
import com.cy.uiframe.recyclerview.AbstractRecyclerView;
import com.cy.uiframetest.bean.ChunkData;

public class TestRecyclerView extends AbstractRecyclerView<ChunkData> {

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
	protected AbstractRececlerAdapter<ChunkData> initAdapter() {
		return new TestRecycleViewAdapter(getContext());
	}

}
