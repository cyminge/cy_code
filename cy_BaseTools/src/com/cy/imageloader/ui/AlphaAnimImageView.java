package com.cy.imageloader.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.cy.R;
import com.cy.constant.Constant;

@SuppressLint("NewApi") public class AlphaAnimImageView extends SecurityImageView {

    private static final String ALPHA_PROPERTY = "alpha";

    private boolean mIsCheck = false;
    private ImageView mBackground;
    private ObjectAnimator mContentAnimator;
    private ObjectAnimator mBackgroundAnimator;

    public AlphaAnimImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnimator();
    }

    private void initBackgroundView() {
        if (mIsCheck) {
            return;
        }

        mIsCheck = true;
        ViewParent parent = getParent();
        mBackground = (ImageView) ((View) parent).findViewById(R.id.alpha_anim_bg);
        if (mBackground != null) {
            initBackgroundAnimator();
        }
    }

    @Override
    protected void setDefaultImage() {
    }

    private void initAnimator() {
        mContentAnimator = ObjectAnimator.ofFloat(this, ALPHA_PROPERTY, 0f, 1f).setDuration(
                Constant.MILLIS_500);
        mContentAnimator.setInterpolator(new LinearInterpolator());
    }

    private void initBackgroundAnimator() {
        mBackgroundAnimator = ObjectAnimator.ofFloat(mBackground, ALPHA_PROPERTY, 1f, 0f).setDuration(
                Constant.MILLIS_500);
        mBackgroundAnimator.setInterpolator(new LinearInterpolator());
        mBackgroundAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                mBackground.setVisibility(View.GONE);
            }

        });
    }

    @Override
    public void setImageResource(int resId) {
        initBackgroundView();
        if (mBackground == null) {
            super.setImageResource(resId);
        } else {
            mBackground.setImageResource(resId);
            mBackground.setAlpha(1f);
            mBackground.setVisibility(View.VISIBLE);
            setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        setImageBitmap(bm, true);
    }

    public void setImageBitmap(Bitmap bm, boolean alphaAnim) {
        super.setImageBitmap(bm);
        setVisibility(View.VISIBLE);
        if (alphaAnim) {
            startAnim();
        }
    }

    private void startAnim() {
        mContentAnimator.start();
        if (mBackgroundAnimator != null) {
            mBackgroundAnimator.start();
        }
    }
}
