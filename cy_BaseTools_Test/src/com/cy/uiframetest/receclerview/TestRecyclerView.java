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
		// switch (event.getAction()) {
		//
		// case MotionEvent.ACTION_DOWN:
		// Log.i(TAG, "CustomLayout-onTouchEvent-ACTION_DOWN");
		// break;
		// case MotionEvent.ACTION_UP:
		// Log.i(TAG, "CustomLayout-onTouchEvent-ACTION_UP");
		// break;
		// default:
		// break;
		//
		// }
		Log.e("cyTest", "TestRecyclerView -- onTouchEvent");
		return super.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		float nowX = ev.getX();
		float nowY = ev.getY();
		int action = ev.getAction();
		if (mLaunchActivityHelper.isRecyclerViewNotConsumerEvent(nowX, nowY, action)) {
			return false;
		}
		boolean event = super.onInterceptTouchEvent(ev);
		Log.e("cyTest", "TestRecyclerView -- onInterceptTouchEvent : "+event);
		return event;
	}

}
