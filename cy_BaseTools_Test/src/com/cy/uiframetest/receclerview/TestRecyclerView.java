package com.cy.uiframetest.receclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import com.cy.uiframe.recyclerview.AbstractRececlerAdapter;
import com.cy.uiframe.recyclerview.AbstractRecyclerView;
import com.cy.uiframetest.bean.ChunkData;

@SuppressLint("ClickableViewAccessibility")
public class TestRecyclerView extends AbstractRecyclerView<ChunkData> {

	private static final String TAG = "TestRecyclerView";

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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e("cyTest", "TestRecyclerView -- onTouchEvent");
		return super.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean event = super.onInterceptTouchEvent(ev);
		Log.e("cyTest", "TestRecyclerView -- onInterceptTouchEvent : "+event);
		return event;
	}
	
}
