package com.cy.uiframe.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class AbstractRootView extends FrameLayout {
	
	private Context mContext;

	public AbstractRootView(Context context) {
		this(context, null);
	}
	
	public AbstractRootView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		
		initLayout(context);
	}
	
	private void initLayout(Context context) {
		
	}
	
	

}
