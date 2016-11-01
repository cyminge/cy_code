package com.cy.imageloadertest;

import android.os.Bundle;
import android.widget.GridView;

import com.cy.global.BaseActivity;
import com.cy.imageloader.ImageLoader;
import com.cy.imageloader.listener.PauseOnScrollListener;
import com.cy.test.R;


public class ImageLoaderActivity extends BaseActivity {
    
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageloader_main);
        
        mImageLoader = ImageLoader.INSTANCE;
        
        GridView photo_wall = (GridView) findViewById(R.id.photo_wall);
        PhotoWallAdapter adapter = new PhotoWallAdapter(this, mImageLoader);
        photo_wall.setAdapter(adapter);
        photo_wall.setOnScrollListener(new PauseOnScrollListener(mImageLoader, true, true));
        
    }
    
}
