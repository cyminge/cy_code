package com.cy.animation.turn;

import com.cy.utils.LogUtils;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.graphics.Camera;
import android.graphics.Matrix;

/**
 * An animation that rotates the view on the Y axis between two specified
 * angles. This animation also adds a translation on the Z axis (depth) to
 * improve the effect.
 */
public class Rotate3dAnimation extends Animation {
	
	private static final String TAG = "ImageTurn.Rotate3dAnimation";
	
	private final float mFromDegrees;
	private final float mToDegrees;
	private final float mCenterX;
	private final float mCenterY;
	private final float mDepthZ;
	private final boolean mReverse;
	private Camera mCamera;

	/**
	 * Creates a new 3D rotation on the Y axis. The rotation is defined by its
	 * start angle and its end angle. Both angles are in degrees. The rotation
	 * is performed around a center point on the 2D space, definied by a pair of
	 * X and Y coordinates, called centerX and centerY. When the animation
	 * starts, a translation on the Z axis (depth) is performed. The length of
	 * the translation can be specified, as well as whether the translation
	 * should be reversed in time.
	 * 
	 * @param fromDegrees
	 *            the start angle of the 3D rotation
	 * @param toDegrees
	 *            the end angle of the 3D rotation
	 * @param centerX
	 *            the X center of the 3D rotation
	 * @param centerY
	 *            the Y center of the 3D rotation
	 * @param reverse
	 *            true if the translation should be reversed, false otherwise
	 */

	public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY, float depthZ, boolean reverse) {
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
		mCenterX = centerX;
		mCenterY = centerY;
		mDepthZ = depthZ;
		mReverse = reverse;
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		
		LogUtils.debug(TAG, "=====initialize()======");
		
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		
		LogUtils.debug(TAG, "=====applyTransformation()======");
		LogUtils.debug(TAG, "=====interpolatedTime = "+interpolatedTime);
		
		final float fromDegrees = mFromDegrees;
		float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Camera camera = mCamera;

		final Matrix matrix = t.getMatrix();
		camera.save();
		if (mReverse) {
			camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
		} else {
			camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
		}
		camera.rotateY(degrees);
		camera.getMatrix(matrix);
		camera.restore();

//		由于缩放是以(0,0)为中心的,所以为了把界面的中心与(0,0)对齐,就要preTranslate(-centerX, -centerY),
//		完成后,调用postTranslate(centerX, centerY),再把图片移回来,这样看到的动画效果就是activity的界面图片从中心不停的缩放了
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
//		matrix.preRotate(degrees, centerX, centerY);
	}
}