package com.example.activitytest;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cy.R;
import com.example.ProxyInstrumentation;
import com.example.global.BaseActivity;
import com.example.util.BitmapCompress;

@SuppressLint("NewApi")
public class Activity_TestOnmeasure extends BaseActivity implements OnClickListener {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_onmeasure);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        Log.e("cyTest", "Activity_A.onWindowFocusChanged");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
//        Log.e("cyTest", "Activity_A.onAttachedToWindow");
    }

//    @Override
//    public String getCallingPackage() {
//    	return super.getCallingPackage();
//    }

    @Override
    protected void onPause() {
//        Log.e("cyTest", "Activity_A.onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
//        Log.e("cyTest", "Activity_A.onStop");
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        Log.e("cyTest", "Activity_A.onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        Log.e("cyTest", "Activity_A.onDetachedFromWindow");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.e("cyTest", "Activity_A.onDestroy");
    }
}
