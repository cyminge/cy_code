package com.cy.animation.lock.copy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cy.R;

public class UnlockSlideTab extends View {
	
	private Context mContext;
	private boolean mDragging = false;
	private float mLastMotionX; // 记录点击坐标点
	private float mLastMotionY;
	private float mMoveX; // 记录移动坐标点
	private float mMoveY;

	private static final int ANIMATION_STATE_STARTING = 1; // 动画状态
	private static final int ANIMATION_STATE_RUNNING = 2;
	private static final int ANIMATION_STATE_DONE = 3;
	private static final int ANIMATION_TYPE_SCALE = 1; // 动画类型
	private static final int ANIMATION_TYPE_NO_SCALE = 2;
	private float mAnimationFrom; // 动画起始位置
	private float mAnimationTo; // 动画目标位置
	private int mAnimationDuration; // 动画持续时间
	private long mAnimationStartTime; // 动画开始时间
	private int mAnimationType;
	private int mAnimationState;
	private boolean mDrawModeBitmap=true;
    private Bitmap mDragBitmap; // 所画圆圈
    private Bitmap mDragBitmapFrom;
    private Bitmap mLockBitmap;
    private Paint mDragPaint = new Paint();
    
    private Paint mDragPaintAlpha = new Paint();
    
    private static final int ANIMATION_SCALE_UP_DURATION = 400;

	
	public UnlockSlideTab(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UnlockSlideTab(Context context){
		super(context);
		this.mContext = context;
		
		mDragBitmapFrom = ((BitmapDrawable)getResources().getDrawable(R.drawable.little)).getBitmap();
		mDragBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.big)).getBitmap();
		mLockBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.lock)).getBitmap();
		
		mAnimationDuration = ANIMATION_SCALE_UP_DURATION; // 动画时间
		
		float scaleFactor = mDragBitmap.getWidth(); // 记录从哪开始动画
        mAnimationTo = 1.0f;
        mAnimationFrom = 1.0f*(scaleFactor-mDragBitmapFrom.getWidth())/scaleFactor;
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();

        switch (action) {
        case MotionEvent.ACTION_DOWN:
        	mLastMotionX = x; 
        	mLastMotionY = y;
        	mDragging = true; 
        	mAnimationState = ANIMATION_STATE_STARTING;
        	mAnimationType = ANIMATION_TYPE_SCALE; // 动画类型
            invalidate();
            break;
        case MotionEvent.ACTION_MOVE:
        	invalidate();
        	Log.d("cyTest", "onTouchEvent.step(2)");
        	// 手指移动时改变大小圆环的的颜色和状态，放在后面做
        	mMoveX = x;
        	mMoveY = y;
        	
        	if(Math.sqrt((mMoveX - mLastMotionX)*(mMoveX - mLastMotionX)+(mMoveY - mLastMotionY)*(mMoveY - mLastMotionY)) > mDragBitmap.getWidth()/2){
        		mDragPaintAlpha.setAlpha(0);
        	} else if(Math.sqrt((mMoveX - mLastMotionX)*(mMoveX - mLastMotionX)+(mMoveY - mLastMotionY)*(mMoveY - mLastMotionY)) < mDragBitmapFrom.getWidth()/2){
        		mDragPaintAlpha.setAlpha(255);
        	} else {
        		int alpha = 255*(int)(Math.sqrt((mMoveX-mLastMotionX)*(mMoveX-mLastMotionX)+(mMoveY-mLastMotionY)*(mMoveY-mLastMotionY)))/(mDragBitmap.getWidth()-mDragBitmapFrom.getWidth());
            	Log.d("cyTest", "alpha = "+alpha);
        		mDragPaintAlpha.setAlpha(255-alpha);
        	}
        	
        	mDragging = false; 
        	mAnimationState = ANIMATION_STATE_RUNNING;
        	
            
            break;
        case MotionEvent.ACTION_UP:
        	
        	mAnimationState = ANIMATION_STATE_DONE;
        	
        	mDragPaintAlpha.setAlpha(255);

        	invalidate();
            break;
        case MotionEvent.ACTION_CANCEL:
        }

        return true;
    
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		Log.d("cyTest", "draw.step(1)");

		if(mDragging){// 只有当鼠标按下时展示动画
			if(mAnimationState == ANIMATION_STATE_STARTING){// 准备动画
				Log.d("cyTest", "draw.step(2)");
				mAnimationStartTime = SystemClock.uptimeMillis();
				mAnimationState = ANIMATION_STATE_RUNNING;
			}
		} 
		
		if (mAnimationState == ANIMATION_STATE_RUNNING) {// 动画进行中
			invalidate();
			Log.d("cyTest", "draw.step(2)");
            float normalized = (float) (SystemClock.uptimeMillis() - mAnimationStartTime) / mAnimationDuration; // 得到时间比值
            Log.d("cyTest", "normalized = "+normalized);
            if (normalized >= 1.0f) {
                mAnimationType = ANIMATION_TYPE_NO_SCALE;
            }
            normalized = Math.min(normalized, 1.0f);
            final float value = mAnimationFrom  + (mAnimationTo - mAnimationFrom) * normalized;
            Log.d("cyTest", "value = "+value);
            switch (mAnimationType) {
            case ANIMATION_TYPE_SCALE:
                if(mDrawModeBitmap && mDragBitmap!=null){
                	Log.d("cyTest", "draw.step(3)");
                	final Bitmap dragBitmap = mDragBitmap;
                	final Bitmap dragBitmapFrom = mDragBitmapFrom;
                	final Bitmap lockBitmap = mLockBitmap;
                    canvas.save();
                    canvas.translate(mLastMotionX, mLastMotionY);
                    canvas.drawBitmap(lockBitmap, -lockBitmap.getWidth()/2, -lockBitmap.getHeight()/2, mDragPaint);
                    canvas.drawBitmap(dragBitmapFrom, -dragBitmapFrom.getWidth()/2, -dragBitmapFrom.getHeight()/2, mDragPaint);
//                    canvas.translate(-(dragBitmap.getWidth() * (1.0f - value)) / 2, -(dragBitmap.getHeight() * (1.0f - value)) / 2);
                    canvas.scale(value, value);
                    canvas.drawBitmap(dragBitmap, -dragBitmap.getWidth()/2, -dragBitmap.getHeight()/2, mDragPaint);
                    canvas.restore();
                }
                break;
            case ANIMATION_TYPE_NO_SCALE:
      	      if(mDrawModeBitmap && mDragBitmap!=null){
      	    	  Log.d("cyTest", "draw.step(4)");
      	    	  canvas.save();
    	          canvas.translate(mLastMotionX, mLastMotionY);
    	          canvas.drawBitmap(mLockBitmap, -mLockBitmap.getWidth()/2, -mLockBitmap.getHeight()/2, mDragPaint);
    	          canvas.drawBitmap(mDragBitmapFrom, -mDragBitmapFrom.getWidth()/2, -mDragBitmapFrom.getHeight()/2, mDragPaintAlpha);
    	          canvas.drawBitmap(mDragBitmap, -mDragBitmap.getWidth()/2, -mDragBitmap.getHeight()/2, mDragPaint);
    	          canvas.restore();
    	      }
      	      break;
            }
		} 
	}
}
