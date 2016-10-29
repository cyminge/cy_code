package com.cy.edittext;

import com.cy.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class Mogoo_PasswordEditText extends EditText {
	private static final int MAX_LEN = 4;
	private Bitmap mPoint;
	private Paint mPaint;
	private int mPointWidth;
	private int mPointHeight;
	private int mSpan;
	
	public Mogoo_PasswordEditText(Context context) {
		this(context,null);
	}
	
	public Mogoo_PasswordEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		Resources res = context.getResources();
		BitmapDrawable drawable = (BitmapDrawable)res.getDrawable(R.drawable.mogoo_password_point);
		if(drawable != null)
		{
			mPoint = drawable.getBitmap();
			mPointWidth = mPoint.getWidth();
			mPointHeight = mPoint.getHeight();
			
		}
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mSpan = res.getDimensionPixelSize(R.dimen.lockscreen_password_span);
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		//super.onDraw(canvas);
		CharSequence text = this.getText();
		if(text != null && text.length() > 0)
		{
			int dy = (this.getMeasuredHeight() - mPointHeight)/2;
			for(int index = 0; index < text.length(); index++)
			{
				int width = (this.getMeasuredWidth() - (MAX_LEN - 1)*mSpan)/MAX_LEN;
				int dx = (width - mPointWidth)/2 + (width + mSpan)*index;
				canvas.drawBitmap(mPoint, dx, dy, mPaint);
			}
			
		}
	}
}
