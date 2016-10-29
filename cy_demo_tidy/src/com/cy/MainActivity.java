package com.cy;

import net.androgames.blog.sample.customdialog.CustomDialogActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ant.liao.GifActivity;
import com.cy.animation.gallery.one.GalleryOneActivity;
import com.cy.animation.gallery.two.GalleryTwoActivity;
import com.cy.animation.lock.MogooLockScreenActivity;
import com.cy.animation.magnifier.GradientDrawable;
import com.cy.animation.turn.ImageTurnActivity;
import com.cy.edittext.EditTextActivity;
import com.cy.jackson.JacksonMainActivity;
import com.cy.popupwindow.RollActivity;

public class MainActivity extends Activity implements OnClickListener{

	private Intent mIntent;
	
	private Button mJacksonBtn;
	private Button mEditTextBtn;
	private Button mTurnAnimationBtn;
	private Button mGalleryOneBtn;
	private Button mGalleryTwoBtn;
	private Button mPopupwindowBtn;
	private Button mMagnifierBtn;
	private Button mLock;
	private Button mDialog;
	private Button mGif;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mJacksonBtn = (Button) findViewById(R.id.one_btn_for_jackson);
		mEditTextBtn = (Button) findViewById(R.id.two_btn_for_edittext);
		mTurnAnimationBtn = (Button) findViewById(R.id.three_btn_for_turn_animation);
		mGalleryOneBtn = (Button) findViewById(R.id.four_btn_for_gallery_one);
		mGalleryTwoBtn = (Button) findViewById(R.id.five_btn_for_gallery_two);
		mPopupwindowBtn = (Button) findViewById(R.id.six_btn_for_popupwindow);
		mMagnifierBtn = (Button) findViewById(R.id.seven_btn_for_magnifier);
		mLock = (Button) findViewById(R.id.eight_btn_for_lock);
		mDialog = (Button) findViewById(R.id.nine_btn_for_dialog);
		mGif = (Button) findViewById(R.id.ten_btn_for_gif);
		
		
		mJacksonBtn.setOnClickListener(this);
		mEditTextBtn.setOnClickListener(this);
		mTurnAnimationBtn.setOnClickListener(this);
		mGalleryOneBtn.setOnClickListener(this);
		mGalleryTwoBtn.setOnClickListener(this);
		mPopupwindowBtn.setOnClickListener(this);
		mMagnifierBtn.setOnClickListener(this);
		mLock.setOnClickListener(this);
		mDialog.setOnClickListener(this);
		mGif.setOnClickListener(this);
		
//		DehooAnimationUtils.slideAnimation(this, mJacksonBtn, MessageModel.DVB_ANIMATION_TYPE_SLIDE_OUT_TOP);
	}

	/**
	 * 控件点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.one_btn_for_jackson :
			mIntent = new Intent();
			mIntent.setClass(MainActivity.this, JacksonMainActivity.class);
			startActivity(mIntent);
			break;
		case R.id.two_btn_for_edittext :
			mIntent = new Intent();
			mIntent.setClass(MainActivity.this, EditTextActivity.class);
			startActivity(mIntent);
			break;
		case R.id.three_btn_for_turn_animation :
			mIntent = new Intent();
			mIntent.setClass(MainActivity.this, ImageTurnActivity.class);
			startActivity(mIntent);
			break;
		case R.id.four_btn_for_gallery_one : 
			mIntent = new Intent();
			mIntent.setClass(MainActivity.this, GalleryOneActivity.class);
			startActivity(mIntent);
			break;
		case R.id.five_btn_for_gallery_two :
			mIntent = new Intent();
			mIntent.setClass(MainActivity.this, GalleryTwoActivity.class);
			startActivity(mIntent);
			break;
		case R.id.six_btn_for_popupwindow :
			mIntent = new Intent();
			mIntent.setClass(MainActivity.this, RollActivity.class);
			startActivity(mIntent);
			break;
		case R.id.seven_btn_for_magnifier :
			mIntent = new Intent();
			mIntent.setClass(MainActivity.this, GradientDrawable.class);
			startActivity(mIntent);
			break;
		case R.id.eight_btn_for_lock :
			mIntent = new Intent();
			mIntent.setClass(MainActivity.this, MogooLockScreenActivity.class);
			startActivity(mIntent);
			break;
		case R.id.nine_btn_for_dialog :
			mIntent = new Intent();
			mIntent.setClass(MainActivity.this, CustomDialogActivity.class);
			startActivity(mIntent);
			break;
		case R.id.ten_btn_for_gif :
			mIntent = new Intent();
			mIntent.setClass(MainActivity.this, GifActivity.class);
			startActivity(mIntent);
			break;
		}
		
	}

}
