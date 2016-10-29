package com.cy.popupwindow;

import com.cy.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class RollActivity extends Activity {
	private View view;
	private Button btn;
	private PopupWindow mPopupWindow;
	private View[] btns;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow_main);
        
        btn=(Button) this.findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow(btn);
			}
        	
        });
        
        initPopupWindow(R.layout.popwindow);

    }
    
	private void initPopupWindow(int resId){
		LayoutInflater mLayoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
	    view = mLayoutInflater.inflate(resId, null);
	        
		mPopupWindow = new PopupWindow(view, 400,LayoutParams.WRAP_CONTENT);
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_frame)); // 必须要设置，否则返回键无法返回
		mPopupWindow.setOutsideTouchable(true);
		
		//�Զ��嶯��
//		mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
		//ʹ��ϵͳ����
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		
		
		mPopupWindow.update();
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		
		btns=new View[3];
		btns[0]=view.findViewById(R.id.btn_0);
		btns[1]=view.findViewById(R.id.btn_1);
		btns[2]=view.findViewById(R.id.btn_2);
		btns[0].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
				mPopupWindow.dismiss();
			}
		});
		btns[1].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
				mPopupWindow.dismiss();
			}
		});
		btns[2].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
				mPopupWindow.dismiss();
			}
		});
	}
	private void showPopupWindow(View view) {
		Log.d("cyTest", "====showPopupWindow====");
		if(!mPopupWindow.isShowing()){
			Log.d("cyTest", "====showPopupWindow 111====");
//			mPopupWindow.showAsDropDown(view,0,0); // 设置显示在当前view下方
			mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0); // 设置居中
		}
	}
	public Bitmap setRotate(int resId) {
		Matrix mFgMatrix = new Matrix();
		Bitmap mFgBitmap = BitmapFactory.decodeResource(getResources(), resId);
		mFgMatrix.setRotate(180f);
		return mFgBitmap=Bitmap.createBitmap(mFgBitmap, 0, 0, 
				mFgBitmap.getWidth(), mFgBitmap.getHeight(), mFgMatrix, true);
	}
}