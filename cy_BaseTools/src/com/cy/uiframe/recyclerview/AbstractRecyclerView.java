package com.cy.uiframe.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class AbstractRecyclerView extends RecyclerView {

	public AbstractRecyclerView(Context context) {
		this(context, null);
	}

	public AbstractRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public AbstractRecyclerView(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

//	@Override
//	public void setLayoutManager(LayoutManager layout) {
//		super.setLayoutManager(layout);
//	}
//
//	@Override
//	public void setAdapter(Adapter adapter) {
//		super.setAdapter(adapter);
//	}
//	
//	@Override
//	public void setItemAnimator(ItemAnimator animator) {
//		super.setItemAnimator(animator);
//	}
//
//	@Override
//	public void addItemDecoration(ItemDecoration decor) {
//		super.addItemDecoration(decor);
//	}
	
}
