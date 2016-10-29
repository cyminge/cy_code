package com.cy.animation;

import com.cy.R;
import com.cy.config.MessageModel;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Animation.AnimationListener;

/**
 *  动画工具类
 * @author zhanmin
 */
public class DehooAnimationUtils {
	
	/**
	 * 3D翻转动画
	 * @param start
	 * 			开始旋转的角度
	 * @param end
	 * 			结束旋转的角度
	 * @param view
	 * 			要旋转的View
	 */
	public static void apply3DRotation(float start, float end, final View view) {
		// Find the center of the container
		final float centerX = view.getWidth() / 2.0f;
		final float centerY = view.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true); // 310.0f 这个参数是旋转的时候往Z轴方向进入的深度
		rotation.setDuration(200);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());

		// 监听动画
		rotation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				view.post(new Runnable() {
					@Override
					public void run() {
						final float centerX = view.getWidth() / 2.0f;
						final float centerY = view.getHeight() / 2.0f;
						Rotate3dAnimation rotation;
						// mImageView.requestFocus();
						rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false); // 180,  360  改为这两个参数可以实现360翻转。。
						rotation.setDuration(200);
						rotation.setFillAfter(true);
						rotation.setInterpolator(new DecelerateInterpolator());
						view.startAnimation(rotation);
					}
				});
				
			}
		});
		// 启动动画
		view.startAnimation(rotation);
	}
	
	/**
	 * View的渐入渐出动画
	 * @param context 调用此方法的上下文
	 * @param view 产生动画的View
	 * @param animationType 动画类型，指左进、左出等 
	 * 			调用MessageModel的对应变量即可
	 */
	public static void slideAnimation(Context context, final View view , int animationType){
		Animation animation = null;
		switch (animationType) {
		case MessageModel.DVB_ANIMATION_TYPE_SLIDE_IN_LEFT :
			animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
			animation.setDuration(MessageModel.DVB_VIEW_SLIDE_ANIMATION_MILLIS);
			view.startAnimation(animation);
			break;
		case MessageModel.DVB_ANIMATION_TYPE_SLIDE_IN_RIGHT :
			animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
			animation.setDuration(MessageModel.DVB_VIEW_SLIDE_ANIMATION_MILLIS);
			view.startAnimation(animation);
			break;
		case MessageModel.DVB_ANIMATION_TYPE_SLIDE_IN_TOP :
			animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
			animation.setDuration(MessageModel.DVB_VIEW_SLIDE_ANIMATION_MILLIS);
			view.startAnimation(animation);
			break;
		case MessageModel.DVB_ANIMATION_TYPE_SLIDE_IN_BOTTOM :
			animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
			animation.setDuration(MessageModel.DVB_VIEW_SLIDE_ANIMATION_MILLIS);
			view.startAnimation(animation);
			break;
		case MessageModel.DVB_ANIMATION_TYPE_SLIDE_OUT_LEFT :
			animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
			view.startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					view.setVisibility(View.INVISIBLE);
				}
			});
			break;
		case MessageModel.DVB_ANIMATION_TYPE_SLIDE_OUT_RIGHT :
			animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
			view.startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					view.setVisibility(View.INVISIBLE);
				}
			});
			break;
		case MessageModel.DVB_ANIMATION_TYPE_SLIDE_OUT_TOP :
			animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_out_top);
			view.startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					view.setVisibility(View.INVISIBLE);
				}
			});
			break;
		case MessageModel.DVB_ANIMATION_TYPE_SLIDE_OUT_BOTTOM :
			animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom);
			view.startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					view.setVisibility(View.INVISIBLE);
				}
			});
			break;
		}
		
	}
	
	/**
	 * 旋转动画
	 * @param fromDegrees 起始角度
	 * @param toDegrees 结束角度
	 */
	public static void myAnim(Context context, float fromDegrees, float toDegrees, AnimationListener listener){
		RotateAnimation anim = null;
		//动画声明
		anim=new RotateAnimation(fromDegrees, toDegrees,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		//设置动画执行时间
		anim.setDuration(1000);
		//设置动画执行次数
		anim.setRepeatCount(0);
		//设置动画渲染器
		anim.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.accelerate_interpolator));
		//设置动画结束时停止在结束的位置
		anim.setFillAfter(true);
		//动画监听
		anim.setAnimationListener(listener);
	}
	
}
