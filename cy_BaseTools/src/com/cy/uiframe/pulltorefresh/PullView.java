package com.cy.uiframe.pulltorefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.cy.R;

public class PullView extends View {
    private static final int MAX_PROGRESS = 1;
    private static final float MIN_PROGRESS = 0.6f;
    private Drawable mBallDrawable;

    private int mSize;
    private Rect mContentRect = new Rect();
    private float mProgress = 0;

    @SuppressLint("NewApi") 
    public PullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBallDrawable = context.getResources().getDrawable(R.drawable.pulltorefresh_icon, null);
        mSize = context.getResources().getDimensionPixelSize(R.dimen.ball_icon_size);
    }

    public int getSize() {
        return mSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int curSize = (int) (mSize * mProgress);
        int top = (mSize - curSize) / 2;
        int left = (mSize - curSize) / 2;
        mContentRect.set(left, top, left + curSize, top + curSize);
        mBallDrawable.setBounds(mContentRect);
        mBallDrawable.draw(canvas);
    }

    @SuppressLint("NewApi") 
    public void setProgress(float progress) {
        setRotation(360 * progress);
        progress = Math.min(progress, MAX_PROGRESS);
        mProgress = MIN_PROGRESS + progress * (MAX_PROGRESS - MIN_PROGRESS);
        postInvalidate();
    }

}
