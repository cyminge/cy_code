package com.cy.animation.lock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.cy.R;

public class UnlockSlideTab extends View {
	
	private Context mContext;
	public boolean mDragging = true; // 
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
//    private Bitmap mLockBitmap;
    private Paint mDragPaint = new Paint();
    
    private Paint mDragPaintAlpha = new Paint();
    
    private static final int ANIMATION_SCALE_UP_DURATION = 1000;
	
    public int statusBarHeight;
	
	public UnlockSlideTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.mContext = context;
		
		mDragBitmapFrom = ((BitmapDrawable)getResources().getDrawable(R.drawable.little)).getBitmap();
		mDragBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.big)).getBitmap();
		
		mAnimationDuration = ANIMATION_SCALE_UP_DURATION; // 动画时间
		
		float scaleFactor = mDragBitmap.getWidth(); // 记录从哪开始动画
        mAnimationTo = 1.0f;
        mAnimationFrom = 1.0f*(scaleFactor-mDragBitmapFrom.getWidth())/scaleFactor;
        
        Rect frame = new Rect();
        MogooLockScreenActivity.mWindow.getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        mLastMotionX = (int) (dm.widthPixels + 0.5f)/2; // 屏幕宽（px，如：480px）
        mLastMotionY = (int) (dm.heightPixels + 0.5f)/2-statusBarHeight; // 屏幕高（px，如：800px）
    	mDragging = true; 
    	mAnimationState = ANIMATION_STATE_STARTING;
    	mAnimationType = ANIMATION_TYPE_SCALE; // 动画类型
        invalidate();
	}

	public UnlockSlideTab(Context context){
		this(context, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            break;
        case MotionEvent.ACTION_MOVE:
            break;
        case MotionEvent.ACTION_UP:
            break;
        case MotionEvent.ACTION_CANCEL:
        }
        return true;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		if(mDragging){// 只有当鼠标按下时展示动画
			if(mAnimationState == ANIMATION_STATE_STARTING){// 准备动画
				mAnimationStartTime = SystemClock.uptimeMillis();
				mAnimationState = ANIMATION_STATE_RUNNING;
			}
		} 
		
		if (mAnimationState == ANIMATION_STATE_RUNNING) {// 动画进行中
			invalidate();
            float normalized = (float) (SystemClock.uptimeMillis() - mAnimationStartTime) / mAnimationDuration; // 得到时间比值
            Log.d("cyTest", "normalized = "+normalized);
            if (normalized >= 1.0f) {
                mAnimationType = ANIMATION_TYPE_NO_SCALE;
            }
            normalized = Math.min(normalized, 1.0f);
            final float value = mAnimationFrom  + (mAnimationTo - mAnimationFrom) * normalized;
            Log.d("cyTest", "value = "+value);
            
            final int alpha = (int)(255 + (0-255)*normalized);
        	Log.d("cyTest", "alpha = "+alpha);
    		mDragPaintAlpha.setAlpha(alpha);
            
            switch (mAnimationType) {
            case ANIMATION_TYPE_SCALE:
                if(mDrawModeBitmap && mDragBitmap!=null){
                	final Bitmap dragBitmap = mDragBitmap;
                	final Bitmap dragBitmapFrom = mDragBitmapFrom;
                    canvas.save();
                    // 移动到当前点
                    canvas.translate(mLastMotionX, mLastMotionY);
                    // 画小圆
                    canvas.drawBitmap(dragBitmapFrom, -dragBitmapFrom.getWidth()/2, -dragBitmapFrom.getHeight()/2, mDragPaint);
                    canvas.scale(value, value);
                    canvas.drawBitmap(dragBitmap, -dragBitmap.getWidth()/2, -dragBitmap.getHeight()/2, mDragPaintAlpha);
                    canvas.restore();
                }
                break;
            case ANIMATION_TYPE_NO_SCALE:
      	      if(mDrawModeBitmap && mDragBitmap!=null){
      	    	  canvas.save();
    	          canvas.translate(mLastMotionX, mLastMotionY);
    	          canvas.drawBitmap(mDragBitmapFrom, -mDragBitmapFrom.getWidth()/2, -mDragBitmapFrom.getHeight()/2, mDragPaint);
//    	          canvas.drawBitmap(mDragBitmap, -mDragBitmap.getWidth()/2, -mDragBitmap.getHeight()/2, mDragPaint);
    	          canvas.restore();
    	          mAnimationState = ANIMATION_STATE_STARTING;
    	          mAnimationType = ANIMATION_TYPE_SCALE;
    	      }
      	      break;
            }
		} 
	}
}
