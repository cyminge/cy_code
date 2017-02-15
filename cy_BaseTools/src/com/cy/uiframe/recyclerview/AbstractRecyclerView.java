package com.cy.uiframe.recyclerview;

import java.util.ArrayList;

import com.cy.uiframe.main.LaunchActivityHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("NewApi") 
public abstract class AbstractRecyclerView<T> extends RecyclerView {

	protected LaunchActivityHelper<T> mLaunchActivityHelper;
	
	protected AbstractRececlerAdapter<T> mAdapter;

	public AbstractRecyclerView(Context context) {
		this(context, null);
	}

	public AbstractRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public AbstractRecyclerView(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);

		mAdapter = initAdapter();
		setAdapter(mAdapter);
	}

	protected abstract AbstractRececlerAdapter<T> initAdapter();

	public void addHeaderView(View view) {
		mAdapter.addHeaderView(view);
	}

	public void addFootView(View view) {
		mAdapter.addFooterView(view);
	}
	
	public void setLaunchActivityHelper(LaunchActivityHelper<T> launchActivityHelper) {
		mLaunchActivityHelper = launchActivityHelper;
	}
	
	public void updateList(final ArrayList<T> list, final int exceptionType) {
        if (list == null || list.isEmpty()) {
//            onDataException(exceptionType); //??
            return;
        }

        if (mLaunchActivityHelper.isFirstPageData()) {
            mAdapter.updateData(list);
        } else {
            mAdapter.addData(list);
        }
        requestLayout();
        mAdapter.notifyDataSetChanged();
    }
	
	@Override
	public boolean canScrollHorizontally(int direction) {
		boolean flag = super.canScrollHorizontally(direction);
		Log.e("cyTest", "canScrollHorizontally: "+flag);
		return false;
	}
	
	public int getHeaderViewsCount() {
		return mAdapter.getHeaderViewsCount();
	}
	
	public void exit() {
		
	}
	
	// @Override
	// public void setLayoutManager(LayoutManager layout) {
	// super.setLayoutManager(layout);
	// }
	//
	// @Override
	// public void setAdapter(Adapter adapter) {
	// super.setAdapter(adapter);
	// }
	//
	// @Override
	// public void setItemAnimator(ItemAnimator animator) {
	// super.setItemAnimator(animator);
	// }
	//
	// @Override
	// public void addItemDecoration(ItemDecoration decor) {
	// super.addItemDecoration(decor);
	// }

}
