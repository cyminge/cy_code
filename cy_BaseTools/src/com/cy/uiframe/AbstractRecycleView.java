package com.cy.uiframe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class AbstractRecycleView extends RecyclerView {

	public AbstractRecycleView(Context context) {
		this(context, null);
	}

	public AbstractRecycleView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public AbstractRecycleView(Context arg0, AttributeSet arg1, int arg2) {
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
