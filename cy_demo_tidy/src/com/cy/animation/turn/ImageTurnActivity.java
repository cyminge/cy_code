package com.cy.animation.turn;

import com.cy.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class ImageTurnActivity extends Activity implements OnClickListener {
	
	private static final String TAG = "ImageTurn";
	
	/**
	 * 仿照系统的范例，修改成点击图片播放动画更新图片。 apiDemos范例： com.example.android.apis.animation
	 * Transition3d
	 */
	private static final int[] PHOTOS_RESOURCES = new int[] { R.drawable.photo, R.drawable.ic_launcher, R.drawable.huimei, R.drawable.cyminge, R.drawable.cyminge, R.drawable.cyminge };
	private ImageView mImageview;
	private ViewGroup mLayout;
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.turn_animation_main);

		mImageview = (ImageView) findViewById(R.id.imageView1);
		mImageview.setOnClickListener(this);
		mLayout = (ViewGroup) findViewById(R.id.image_layout);// ViewGroup是所有layout的父类
		mImageview.setImageResource(PHOTOS_RESOURCES[0]);

		/**
		 * 下面这句应该是用来压缩大图的，没加上这句运行正常
		 */
		// Since we are caching large views, we want to keep their cache
		// between each animation
		mLayout.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imageView1) {
			// Pre-load the image then start the animation
			// mPosition++; 
			applyRotation(0, 90);
//			mImageview.setImageResource(PHOTOS_RESOURCES[mPosition]);
		}
	}

	/**
	 * Setup a new 3D rotation on the container view.
	 * 
	 * @param position
	 *            the item that was clicked to show a picture, or -1 to show the list
	 * @param start
	 *            the start angle at which the rotation must begin
	 * @param end
	 *            the end angle of the rotation
	 */
	private void applyRotation(float start, float end) {
		// Find the center of the container
		final float centerX = mLayout.getWidth() / 2.0f;
		final float centerY = mLayout.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true); // 310.0f 这个参数是旋转的时候往Z轴方向进入的深度
		rotation.setDuration(3000);
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

				mLayout.post(mAaction);// 刷新layout这个View
			}
		});

		// 启动动画
		mLayout.startAnimation(rotation);
	}

	Runnable mAaction = new Runnable() {
		@Override
		public void run() {
			final float centerX = mLayout.getWidth() / 2.0f;
			final float centerY = mLayout.getHeight() / 2.0f;
			Rotate3dAnimation rotation;
			// mImageView.requestFocus();
			
			rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false); // 180,  360  改为这两个参数可以实现360翻转。。

			rotation.setDuration(3000);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mLayout.startAnimation(rotation);
		}
	};
}
