package com.cy.slide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.format.DateUtils;
import android.view.animation.DecelerateInterpolator;

import com.cy.slide.AnimationComputer.OnAnimFinishListener;
import com.cy.utils.bitmap.BitmapUtils;

public class SlideView2 extends RecyclableView implements ISlideView {
	
	public static final int ANIMATION_EFFECTS_TYPE_1 = 1;
	public static final int ANIMATION_EFFECTS_TYPE_2 = 2;
	
	
	private static final long NEXT_SWITCH_DELAY = DateUtils.SECOND_IN_MILLIS * 4;

	private SlideViewEventAdapter mSlideViewEventAdapter;
	private SlideViewHelper mSlideViewHelper;
	private AnimationComputer mAnimationComputer;
	
	private int mAnimationEffectsType;
	
	public SlideView2(Context context) {
		super(context);
	}
	
	public void init(Context context, int effectsType) {
		mSlideViewHelper = new SlideViewHelper(this, context);
		mAnimationEffectsType = effectsType;
		mAnimationComputer = new AnimationComputer(new DecelerateInterpolator(), mAnimFinishListener);
		mSlideViewEventAdapter = new SlideViewEventAdapter(context, this, true, mAnimationComputer);
	}
	
	private OnAnimFinishListener mAnimFinishListener = new OnAnimFinishListener() {

        @Override
        public void onFinish(float curPosition) {
//            postDelayed(mSwitchCommand, NEXT_SWITCH_DELAY);
        }
    };
    
    private OnAnimFinishListener mFinishListener = new OnAnimFinishListener() {
        @Override
        public void onFinish(float curPosition) {
            postDelayed(mSwitchCommand, NEXT_SWITCH_DELAY);
        }
    };

    private Runnable mSwitchCommand = new Runnable() {

        @Override
        public void run() {
//            startAnimation();
        }
    };
    
    protected void prepareAnimation() {
        removeCallbacks(mSwitchCommand);
        if (isSingle()) {
            return;
        }

        postDelayed(mSwitchCommand, NEXT_SWITCH_DELAY);
    }
    
    private int mWidth;
    private int mHeight;
    
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    	super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        mHeight = bottom - top;
        mSlideViewHelper.initSlideBitmap();
    };
    
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	if (mWidth == 0 || mHeight == 0 || mSlideViewHelper.isEmpty()) {
            return;
        }
        float xOffset = caculateXOffset();
        drawSlideItem(canvas, xOffset);
        if (!mAnimationComputer.isFinish()) {
            invalidate();
        } else {
        	mSlideViewHelper.checkAllImageDone();
        }
    }
    
    private float caculateXOffset() {
//    	if (isSingle()) {
//            return 0f;
//        }
    	
        if (mSlideViewEventAdapter.isGestureSliding()) {
            return mSlideViewEventAdapter.getOffsetX();
        }

        return mAnimationComputer.getCurrentPosition();
    }
    
    private void drawSlideItem(Canvas canvas, float xOffset) {
		drawItem(canvas, 0, mSlideViewHelper.getCurrIndex(), xOffset);
        if (isSingle()) {
            return;
        }

        if (FloatUtil.isPositive(xOffset)) { // 手指向左
            drawItem(canvas, -1, mSlideViewHelper.getPreIndex(), xOffset);
        } else if (FloatUtil.isNegative(xOffset)) { // 手指向左
            drawItem(canvas, 1, mSlideViewHelper.getNextIndex(), xOffset);
        }
	}
    
    private void drawItem(Canvas canvas, int pageOffset, int index, float xOffset) {
        Bitmap bitmap = mSlideViewHelper.getSlideBitmap(index);
        if (BitmapUtils.isBitmapEmpty(bitmap)) {
            canvas.drawBitmap(mSlideViewHelper.getDefaultBitmap(), xOffset + mWidth * pageOffset, 0,
            		BitmapUtils.HIGH_PAINT);
        } else {
            canvas.drawBitmap(bitmap, xOffset + mWidth * pageOffset, 0, BitmapUtils.HIGH_PAINT);
        }
    }
    
    public boolean isSingle() {
        return mSlideViewHelper.isSingle();
    }

	@Override
	public void onSingleTapUp() {
		mSlideViewHelper.onItemClick();
	}

	@Override
	public void onBeginScroll() {
		removeCallbacks(mSwitchCommand); // 开始滑动时需要取消定时轮播图片功能
	}

	@Override
	public int getViewWidth() {
		return mWidth;
	}
	
	public int getViewHeight() {
        return mHeight;
    }

	public int getViewPaddingBottom() {
        return 0;
    }
	
	@Override
	public boolean switchImage(boolean isLeft) {
		return mSlideViewHelper.switchImage(isLeft);
	}

	@Override
	public int getCurrentIndex() {
		return mSlideViewHelper.getCurrIndex();
	}

	@Override
	public int getCount() {
		return mSlideViewHelper.getCount();
	}

	@Override
	public void update() {
		invalidate();
	}
	
	public void resetState() {

	}

}
