package com.cy.imageloader.ui;

import com.cy.imageloader.listener.ImageLoadingListener;

import android.widget.ImageView;

public class ImageViewAware {
	
	public ImageView mImageView;
	public int mPosition = -1;
	public ImageLoadingListener mImageLoadingListener;

	public ImageViewAware(ImageView imageView) {
		this(imageView, -1, null);
	}
	
	public ImageViewAware(ImageView imageView, int position, ImageLoadingListener imageLoadingListener) {
		mImageView = imageView;
		mPosition = position;
		mImageLoadingListener = imageLoadingListener;
	}
	
	
	
}
