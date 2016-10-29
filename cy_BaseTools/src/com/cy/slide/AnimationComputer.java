package com.cy.slide;

import android.view.animation.Interpolator;

public class AnimationComputer {

    public interface OnAnimFinishListener {
        public void onFinish(float curPosition);
    }

    private long mStartTime;

    private float mStartPosition;
    private float mEndPosition;
    private float mAnimationTime;
    private static final float ENDING_RATE = 0.95f;
    private float mPrePosition;

    private Interpolator mInterpolator;

    private float mStartValue;
    private float mEndValue;
    private static final float START_CONSTANT = 0;
    private static final float END_CONSTANT = 1.0f;

    private boolean mIsFinish;
    private OnAnimFinishListener mOnFinishListener;

    public AnimationComputer(Interpolator interpolator) {
        this(interpolator, null);
    }

    public AnimationComputer(Interpolator interpolator, OnAnimFinishListener onFinishListener) {
        this.mInterpolator = interpolator;
        mIsFinish = true;
        mOnFinishListener = onFinishListener;
    }

    public void start(float startPosition, float endPosition, float animationTimeMs) {
        mStartTime = System.currentTimeMillis();
        mStartPosition = startPosition;
        mEndPosition = endPosition;
        mAnimationTime = animationTimeMs;
        mPrePosition = mStartPosition;

        mStartValue = mInterpolator.getInterpolation(START_CONSTANT);
        mEndValue = mInterpolator.getInterpolation(END_CONSTANT);

        mIsFinish = false;
    }

    public float getCurrentPosition() {
        if (mIsFinish) {
            return mEndPosition;
        }
        long curTime = System.currentTimeMillis();
        long gapTime = Math.abs(curTime - mStartTime);

        if (isEnding(gapTime, mAnimationTime)) {
            mIsFinish = true;
            float retValue = mEndPosition;
            if (mOnFinishListener != null) {
                mOnFinishListener.onFinish(mEndPosition);
            }
            return retValue;
        } else {
            float curInput = START_CONSTANT + (gapTime / mAnimationTime) * (END_CONSTANT - START_CONSTANT);
            float curValue = mInterpolator.getInterpolation(curInput);
            float curPosition = mStartPosition
                    + ((curValue - mStartValue) / (mEndValue - mStartValue) * (mEndPosition - mStartPosition));
            if (Math.abs(curPosition - mEndPosition) < 0.05) {
                curPosition = mEndPosition;
            }
            return curPosition;
        }
    }

    public float getDeltaPosition() {
        if (mIsFinish) {
            return 0;
        }
        float curPosition = getCurrentPosition();
        float deltaPosition = curPosition - mPrePosition;
        mPrePosition = curPosition;
        return deltaPosition;
    }

    private boolean isEnding(float gapTime, float animationTime) {
        if (animationTime == 0) {
            return true;
        }
        return gapTime / animationTime > ENDING_RATE;
    }

    public boolean isFinish() {
        return mIsFinish;
    }

    public void forceFinish() {
        mIsFinish = true;
        if (mOnFinishListener != null) {
            mOnFinishListener.onFinish(mPrePosition);
        }
    }

    public void setEndPosition(int endPosition) {
        mEndPosition = endPosition;
    }
}
