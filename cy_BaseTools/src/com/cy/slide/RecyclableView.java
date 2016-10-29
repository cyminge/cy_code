package com.cy.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class RecyclableView extends View {
    private boolean mIsExit;

    public RecyclableView(Context context) {
        super(context);
    }

    public RecyclableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void exit() {
        mIsExit = true;
    }
    
    public boolean isExit() {
        return mIsExit;
    }
}
