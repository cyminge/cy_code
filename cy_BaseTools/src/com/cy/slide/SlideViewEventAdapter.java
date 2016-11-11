package com.cy.slide;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import com.cy.constant.Constant;

/**
 * 处理slideView手势滑动
 * @author zf
 *
 */
public class SlideViewEventAdapter {
    private float mOffsetX = 0;
    private GestureDetector mGestureDetector;
    private AnimationComputer mAnimComputer;
    private ISlideView mTargetView;
    private boolean mIsGestureSliding = false; // 是否手指滑动
    private boolean mIsCycled = false; // 是否轮播
    private boolean mBeginScroll = false;
    private boolean mGestureDetectorEnabled = true; // 手势检测器

    public SlideViewEventAdapter(Context context, ISlideView view) {
        this(context, view, false, null);
    }

    public SlideViewEventAdapter(Context context, ISlideView view, boolean isCycle, AnimationComputer animComputer) {
        mTargetView = view;
        mIsCycled = isCycle;
        if (null != animComputer) {
            mAnimComputer = animComputer;
        } else {
        	mAnimComputer = new AnimationComputer(new DecelerateInterpolator());
        }

        initGestureDetector(context);
    }

    private void initGestureDetector(Context context) {
        mGestureDetector = new GestureDetector(context, new OnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
            	Log.e("cyTest", "onSingleTapUp");
                mIsGestureSliding = false;
                mTargetView.onSingleTapUp();
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }
            
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (isSingle()) {
                    return false;
                }
                
                if (mGestureDetectorEnabled && !mBeginScroll) {
                    mBeginScroll = true;
                    mIsGestureSliding = true;
                    mTargetView.onBeginScroll();
                }

                if (mIsCycled) {
                    distanceX = onNoCycle(distanceX);
                }
                
                mOffsetX -= distanceX;
                mTargetView.update();
                
                return false;
            }

            private float onNoCycle(float distanceX) { // 为毛向右移，distanceX < 0 ????
                if ((mTargetView.getCurrentIndex() == 0 && mOffsetX > 0 && distanceX < 0)
                        || (mTargetView.getCurrentIndex() == (mTargetView.getCount() - 1) && mOffsetX < 0 && distanceX > 0)) {
                	Log.e("cyTest", "mOffsetX = "+mOffsetX+", distanceX = "+distanceX);
                    distanceX /= 2;
                }
                return distanceX;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (isSingle()) {
                    return false;
                }

                if (velocityX > 0 && mOffsetX > 0) { // 从左向右滑动
                    startSwitch(true);
                    return true;
                } else if (velocityX < 0 && mOffsetX < 0) {
                    startSwitch(false);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                if (isSingle()) {
                    return false;
                }

                mBeginScroll = false;
                mOffsetX = 0;
                return false;
            }
        });
    }
    
    private boolean isSingle() {
        return mTargetView.getCount() == 1;
    }

    public void setGestureDetectorEnabled(boolean enable) {
        mGestureDetectorEnabled = enable;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        
        if (event.getAction() == MotionEvent.ACTION_UP) {
            startMove();
        }
        
        return true;
    }
    
    private void startMove() {
        float rightDivide = mTargetView.getViewWidth() / 3.0f;
        float leftDivide = -rightDivide;
        if (mOffsetX < leftDivide) {
            startSwitch(false);
        } else if (mOffsetX > rightDivide) {
            startSwitch(true);
        } else {
            startRebound();
        }
    }
    
    private void startSwitch(boolean isLeft) {
        boolean switchSucc = mTargetView.switchImage(isLeft);
        if (switchSucc) {
            if (isLeft) {
                mOffsetX -= mTargetView.getViewWidth();
            } else {
                mOffsetX += mTargetView.getViewWidth();
            }
        }
        startRebound();
    }

    private void startRebound() {
        float animationTime = Math.abs(mOffsetX) / mTargetView.getViewWidth() * Constant.MILLIS_300;
        mAnimComputer.start(mOffsetX, 0, animationTime);
        postRebound();
    }
    
    private void postRebound() {
        if (mAnimComputer.isFinish()) {
            mIsGestureSliding = false;
            mOffsetX = 0;
            return;
        }

        mTargetView.postDelayed(new Runnable() {
            @Override
            public void run() {
                float deltaPosition = mAnimComputer.getDeltaPosition();
                mOffsetX += deltaPosition;
                mTargetView.update();
                postRebound();
            }
        }, Constant.MILLIS_30);
    }

    public float getOffsetX() {
        return mOffsetX;
    }

    public boolean isGestureSliding() {
        return mIsGestureSliding;
    }

}
