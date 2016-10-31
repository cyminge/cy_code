package com.cy.imageloader.listener;

import android.graphics.Bitmap;
import android.view.View;

public interface ImageLoadingListener {
	
	void onLoadingComplete(String imageUri, View view, Bitmap loadedImage);
}

