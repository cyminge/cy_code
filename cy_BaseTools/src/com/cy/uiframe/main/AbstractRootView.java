package com.cy.uiframe.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class AbstractRootView extends FrameLayout {

	public AbstractRootView(Context context) {
		this(context, null);
	}
	
	public AbstractRootView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

}
