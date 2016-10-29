package com.cy.animation.gallery.one;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	int mGalleryItemBackground;
	private Context mContext;
	private Integer[] mImageIds;
	private ImageView[] mImages;

	public ImageAdapter(Context c, Integer[] ImageIds) {
		mContext = c;
		mImageIds = ImageIds;
		mImages = new ImageView[mImageIds.length];
	}

	public boolean createReflectedImages() {
		final int reflectionGap = 4; // 倒转图片与原图片的间隔
		int index = 0;
		for (int imageId : mImageIds) {
			// 创建原图
			Bitmap originalImage = BitmapFactory.decodeResource(mContext.getResources(), imageId);
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();
			
			// 爲什麽單獨作為一個應用的時候，圖片放在mdpi裏面拿到的值跟現在放在hdpi裏面拿到的值是一樣的??????????
			Log.d("cyTest", "====createReflectedImages.width = "+width);
			Log.d("cyTest", "====createReflectedImages.height = "+height);

			Matrix matrix = new Matrix();
			matrix.preScale(1, -1); // 旋转180度

			//将矩阵应用到该原图之中，返回一个宽度不变，高度为原图1/2的倒影位图
			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);
			//创建一个宽度不变，高度为原图+倒影图高度的位图
			Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmapWithReflection);
			// 将原图画到画布上
			canvas.drawBitmap(originalImage, 0, 0, null);
			Paint deafaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
			// 将倒转图片画到画布上
			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

			Paint paint = new Paint();
			/**
			 * 参数一:为渐变起初点坐标x位置，
			 * 参数二:为y轴位置，
			 * 参数三和四:分辨对应渐变终点，
			 * 最后参数为平铺方式，
			 * 这里设置为镜像Gradient是基于Shader类，所以我们通过Paint的setShader方法来设置这个渐变
			 */
			LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			//设置阴影
			paint.setShader(shader);
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			//用已经定义好的画笔构建一个矩形阴影渐变效果
			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

			ImageView imageView = new ImageView(mContext);
			imageView.setImageBitmap(bitmapWithReflection);
//			imageView.setLayoutParams(new GalleryFlow.LayoutParams(90, 250));
			imageView.setLayoutParams(new GalleryFlow.LayoutParams(150, 400));
//			imageView.setScaleType(ScaleType.MATRIX);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			mImages[index++] = imageView;
		}
		return true;
	}

	private Resources getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCount() {
		return mImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return mImages[position];
	}

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}

}
