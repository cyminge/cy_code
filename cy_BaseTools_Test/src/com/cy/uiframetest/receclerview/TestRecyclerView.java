package com.cy.uiframetest.receclerview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.cy.uiframe.recyclerview.AbstractRececlerAdapter;
import com.cy.uiframe.recyclerview.AbstractRecyclerView;
import com.cy.uiframetest.bean.ChunkData;
import com.cy.uiframetest.main.ButtonStatusHandler;

@SuppressLint("ClickableViewAccessibility")
public class TestRecyclerView extends AbstractRecyclerView<ChunkData> {

	private static final String TAG = "TestRecyclerView";

	private TestRecycleViewAdapter mTestRecycleViewAdapter;
	private ButtonStatusHandler<ChunkData> mButtonStatusHandler; // 这个方法需要修改，初始化的位置也应该放到其他地方去，比如LaunchActivityHelper or Activity
	
	public TestRecyclerView(Context context) {
		this(context, null);
	}

	public TestRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public TestRecyclerView(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
		
		mButtonStatusHandler = new ButtonStatusHandler<ChunkData>((Activity)arg0, this, mTestRecycleViewAdapter); 
	}

	@Override
	protected AbstractRececlerAdapter<ChunkData> initAdapter() {
		mTestRecycleViewAdapter = new TestRecycleViewAdapter(getContext());
		return mTestRecycleViewAdapter;
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
	
	@Override
	public void exit() {
		super.exit();
		mButtonStatusHandler.deinit();
	}
	
}
