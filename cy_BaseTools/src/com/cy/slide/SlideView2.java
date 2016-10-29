package com.cy.slide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.format.DateUtils;
import android.view.animation.DecelerateInterpolator;

import com.cy.slide.AnimationComputer.OnAnimFinishListener;
import com.cy.utils.bitmap.BitmapUtils;

public class SlideView2 extends RecyclableView implements ISlideView {

	private static final long NEXT_SWITCH_DELAY = DateUtils.SECOND_IN_MILLIS * 4;

	private SlideViewEventAdapter mSlideViewEventAdapter;
	private AnimationComputer mAnimationComputer;
	private SlideViewHelper mSlideViewHelper;

	public SlideView2(Context context) {
		super(context);

		init(context);
	}

	private void init(Context context) {
		mSlideViewHelper = new SlideViewHelper(this, context);
		mAnimationComputer = new AnimationComputer(new DecelerateInterpolator(), mAnimFinishListener);
		mSlideViewEventAdapter = new SlideViewEventAdapter(context, this, true, mAnimationComputer);
	}

	private OnAnimFinishListener mAnimFinishListener = new OnAnimFinishListener() {

		@Override
		public void onFinish(float curPosition) {
			// postDelayed(mSwitchCommand, NEXT_SWITCH_DELAY);
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
			// startAnimation();
		}
	};

	private int mWidth;
	private int mHeight;

	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		synchronized (this) {
			mWidth = right - left;
			mHeight = bottom - top;
			mSlideViewHelper.initSlideBitmap();
		}
	};

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mWidth == 0 || mHeight == 0 || mSlideViewHelper.isEmpty()) {
			return;
		}
		float xOffset = caculateXOffset();
		drawSlideView(canvas, xOffset);
		if (mAnimationComputer != null && !mAnimationComputer.isFinish()) {
			invalidate();
		} else {
			mSlideViewHelper.checkAllImageDone();
		}
	}

	private float caculateXOffset() {
		if (isSingle()) {
			return 0f;
		}

		if (mSlideViewEventAdapter.isGestureSliding()) {
			return mSlideViewEventAdapter.getOffsetX();
		}

		return mAnimationComputer.getCurrentPosition();
	}

	public boolean isSingle() {
		synchronized (this) {
			return mSlideViewHelper.isSingle();
		}
	}

	private void drawSlideView(Canvas canvas, float xOffset) {
		drawSlideItem(canvas, 0, mSlideViewHelper.getCurrIndex(), xOffset); 

		if (FloatUtil.isPositive(xOffset)) { // 手指向左
			drawSlideItem(canvas, -1, mSlideViewHelper.getPreIndex(), xOffset);
		} else if (FloatUtil.isNegative(xOffset)) { // 手指向左
			drawSlideItem(canvas, 1, mSlideViewHelper.getNextIndex(), xOffset);
		}
	}

	private void drawSlideItem(Canvas canvas, int pageOffset, int index, float xOffset) {
		Bitmap bitmap = mSlideViewHelper.getSlideBitmap(index);
		if (BitmapUtils.isBitmapEmpty(bitmap)) {
			bitmap = mSlideViewHelper.getDefaultBitmap();
		}

		canvas.drawBitmap(bitmap, pageOffset * mWidth + xOffset, 0, BitmapUtils.HIGH_PAINT);
	}

	@Override
	public void onSingleTapUp() {
		mSlideViewHelper.onItemClick();
	}

	@Override
	public void onBeginScroll() {
		removeCallbacks(mSwitchCommand);
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
		return false;
	}

	@Override
	public int getCurrentIndex() {
		return 0;
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public void update() {
		invalidate();
	}
	
	public void resetState() {

	}

}
