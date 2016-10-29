package com.cy.animation.magnifier;

import com.cy.R;
import com.cy.utils.LogUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Path.Direction;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GradientDrawable extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        setContentView(new SampleView(this));
        
        LogUtils.debug("cyTest", "========GradientDrawable.onCreate()========");
        
    }
    
    class SampleView extends View{
    	private Path mPath = new Path();
    	private Matrix matrix = new Matrix();
    	private Bitmap bitmap;
    	//放大镜的半径
    	private static final int RADIUS = 100;
    	//放大倍数
    	private static final int FACTOR = 2;
    	private int mCurrentX, mCurrentY;

    	public SampleView(Context context) {
    		super(context);
    		mPath.addCircle(RADIUS, RADIUS, RADIUS, Direction.CW); // 顺时针画个圆
    		matrix.setScale(FACTOR, FACTOR);
    		
    		bitmap = BitmapFactory.decodeResource(GradientDrawable.this.getResources(), R.drawable.c);
    		
    		LogUtils.debug("cyTest", "========GradientDrawable.SampleView========");
    	}	
    	
    	@Override
    	public boolean onTouchEvent(MotionEvent event) {
    		mCurrentX = (int) event.getX();
    		mCurrentY = (int) event.getY();
    		
    		invalidate();
    		return true;
    	}
    	
    	@Override
    	public void onDraw(Canvas canvas) {
    		super.onDraw(canvas);
    		Log.d("cyTest", "======onDraw()========");
    		//底图
    		canvas.drawBitmap(bitmap, 0, 0, null);
    		//剪切
    		canvas.translate(mCurrentX - RADIUS, mCurrentY - RADIUS);
    		canvas.clipPath(mPath);	// 剪切图片
////    		//画放大后的图
    		canvas.translate(RADIUS-mCurrentX*FACTOR, RADIUS-mCurrentY*FACTOR);
    		canvas.drawBitmap(bitmap, matrix, null);		
    	}
    }
}