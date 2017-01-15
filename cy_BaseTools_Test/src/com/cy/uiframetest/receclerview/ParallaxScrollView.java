package com.cy.uiframetest.receclerview;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class ParallaxScrollView extends HorizontalScrollView {

    private ArrayList<ParallaxScrollView> mBackLayers;
    private float mRatio;

    public ParallaxScrollView addBackLayer(ParallaxScrollView psv, float ratio) {
        if (null == mBackLayers) {
            mBackLayers = new ArrayList<ParallaxScrollView>();
        }
        if (null == psv) {
            mBackLayers.clear();
        } else {
            mBackLayers.add(psv);
            psv.setScrollRatio(ratio);
        }
        return this;
    }

    public interface OnScrollChangedListener {
        public void onScrollChanged(int x);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mBackLayers) {
            for (ParallaxScrollView psv : mBackLayers) {
                psv.scrollTo(l, 0);
            }
        }

        if (null != mOnScrollChangedListener) {
            mOnScrollChangedListener.onScrollChanged(l);
        }
    }

    private OnScrollChangedListener mOnScrollChangedListener;

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        mOnScrollChangedListener = onScrollChangedListener;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo((int) (x * mRatio), y);
    }

    public void setScrollRatio(float ratio) {
        mRatio = ratio;
    }

    private void init() {
        setScrollRatio(1f);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParallaxScrollView(Context context) {
        super(context);
        init();
    }

    public static class OnTouchDispatcher implements View.OnTouchListener {
        private final View mTarget;
        private final boolean mConsume;

        public OnTouchDispatcher(View receiver, boolean consume) {
            mTarget = receiver;
            mConsume = consume;
        }

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            if (null != mTarget) {
                mTarget.dispatchTouchEvent(e);
            }
            return mConsume;
        }
    }
}
