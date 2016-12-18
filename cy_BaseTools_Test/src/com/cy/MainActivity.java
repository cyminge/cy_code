package com.cy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cy.downloadtest.DownloadActivity;
import com.cy.global.BaseActivity;
import com.cy.imageloadertest.ImageLoaderActivity;
import com.cy.slide.SlideViewActivity;
import com.cy.test.R;
import com.cy.uiframetest.UIFrameActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }

    public void showImageLoader(View view) {
        Intent intent = new Intent();
        intent.setClass(this, ImageLoaderActivity.class);
        startActivity(intent);
    }

    public void showDownload(View view) {
        Intent intent = new Intent();
        intent.setClass(this, DownloadActivity.class);
        startActivity(intent);
    }
    
    public void showSlideView(View view) {
        Intent intent = new Intent();
        intent.setClass(this, SlideViewActivity.class);
        startActivity(intent);
    }
    
    public void showRecycleView(View view) {
    	Intent intent = new Intent();
    	intent.setClass(this, UIFrameActivity.class);
    	startActivity(intent);
    }

}
