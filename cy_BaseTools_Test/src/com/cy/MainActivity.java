package com.cy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cy.downloadtest.DownloadActivity;
import com.cy.global.BaseActivity;
import com.cy.imageloadertest.ImageLoaderActivity;
import com.cy.test.R;

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

}
