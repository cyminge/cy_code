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
public class Activity_A extends BaseActivity implements OnClickListener {

    private static int counts = 0;
    
    @Override
    protected void onRestart() {
        super.onRestart();
        if(null == getIntent()) {
        	
        }
//        Log.e("cyTest", "--> Activity_A.onNewIntent.from :"+GNGameHallSDK.isFromGNGameHall(this, getIntent(), getPackageName()));
//        Log.e("cyTest", "--> Activity_A.onRestart.getIntent :"+getIntent().getStringExtra("cyminge"));
//        Log.e("", msg)
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.e("cyTest", "--> Activity_A.onNewIntent.from :"+getIntent().getStringExtra("from"));
    }
    
    @Override
    protected void onStart() {
//        Log.e("cyTest", "Activity_A.onStart");
        super.onStart();
        
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("cyTest", "Activity_A.onResume");
        counts++;
        
//        Log.e("cyTest", "Activity_A.onResume 来自游戏大厅 :" +ProxyInstrumentation.startFromOgc());
//        Log.e("cyTest", "Activity_A.counts = " + counts);
//        Log.e("cyTest", "Activity_A.onResume getCallingPackage:"+getCallingPackage());
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
//        Log.e("cyTest", "Activity_A.onCreate");

        
        if (null != savedInstanceState) {
//            Log.e("cyTest", "onCreate.savedInstanceState is not null");
//            Log.e("cyTest", "aa:" + savedInstanceState.get("aa"));
        }
        
        if(null != getIntent()) {
//        	Log.e("cyTest", "--> Activity_A.onCreate.cyminge :"+getIntent().getStringExtra("cyminge"));
        }
        
        Locale locale = getResources().getConfiguration().locale;
        String country = locale.getCountry();
        country = country.trim().toLowerCase();
        // Log.e("cyTest", "country:"+country);
        // MediaManager.playSound("/sdcard/MyVoiceForder/Record/aa1f7285-8c79-4020-9173-e4af00bdfbb1.amr",
        // new OnCompletionListener() {
        //
        // @Override
        // public void onCompletion(MediaPlayer mp) {
        // Log.e("cyTest", "说完了");
        // }
        // });

        // 测试图片的压缩
        ImageView img_ora = (ImageView) findViewById(R.id.img_ora);
        ImageView img_new = (ImageView) findViewById(R.id.img_new);
        img_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});

        Resources resource = getResources();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap rgb8888 = BitmapFactory.decodeResource(resource, R.drawable.root_bg, opts);
        // img_ora.setImageBitmap(rgb8888);

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.root_bg, newOpts);
        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = BitmapCompress.computeSampleSize(newOpts, 250, 250);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.root_bg, newOpts);
        img_ora.setImageBitmap(bitmap);

        // opts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap rgb565 = BitmapFactory.decodeResource(resource, R.drawable.root_bg, opts);
        // img_new.setImageBitmap(rgb565);
//        Log.e("cyTest", "=======================================================");
        big(bitmap, img_new);

        // 测试图片的压缩 end

//        Log.e("cyTest", "-->getFilesDir = " + this.getFilesDir());
//        Log.e("cyTest", "-->getCacheDir = " + this.getCacheDir());
//        Log.e("cyTest", "-->getExternalCacheDir = " + this.getExternalCacheDir());
//        Log.e("cyTest", "-->getExternalFilesDir = " + this.getExternalFilesDir("cy"));
//        Log.e("cyTest", "-->Environment.getExternalStorageState = " + Environment.getExternalStorageState());
//        Log.e("cyTest", "-->Environment.getDataDirectory = " + Environment.getDataDirectory());
//        Log.e("cyTest",
//                "-->Environment.getDownloadCacheDirectory = " + Environment.getDownloadCacheDirectory());
//        Log.e("cyTest",
//                "-->Environment.getExternalStorageDirectory = " + Environment.getExternalStorageDirectory());
//        Log.e("cyTest", "-->Environment.getRootDirectory = " + Environment.getRootDirectory());
//        for (File file : this.getExternalCacheDirs()) {
//            Log.e("cyTest", "-->getExternalCacheDirs = " + file.getPath());
//        }

    }

    private int displayWidth;
    private int displayHeight;
    private float scaleWidth = 1;
    private float scaleHeight = 1;

    /* 图片放大的method */
    private void big(Bitmap bmp, ImageView img_new) {
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        /* 设置图片放大的比例 */
        double scale = 2;
        /* 计算这次要放大的比例 */
        scaleWidth = (float) (scaleWidth * scale);

        scaleHeight = (float) (scaleHeight * scale);

        /* 产生reSize后的Bitmap对象 */
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);

        img_new.setImageBitmap(resizeBmp);

        // if (id == 0) {
        // /* 如果是第一次按，就删除原来设置的ImageView */
        // layout1.removeView(mImageView);
        // } else {
        // /* 如果不是第一次按，就删除上次放大缩小所产生的ImageView */
        // layout1.removeView((ImageView) findViewById(id));
        // }
        // /* 产生新的ImageView，放入reSize的Bitmap对象，再放入Layout中 */
        // id++;
        // ImageView imageView = new ImageView(EX04_23.this);
        // imageView.setId(id);
        // imageView.setImageBitmap(resizeBmp);
        // layout1.addView(imageView);
        // setContentView(layout1);
        //
        // /* 如果再放大会超过屏幕大小，就把Button disable */
        // if (scaleWidth * scale * bmpWidth > displayWidth ||
        //
        // scaleHeight * scale * bmpHeight > displayHeight)
        //
        // {
        // mButton02.setEnabled(false);
        // }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn) {
//            Log.e("cyTest", "aaaa == " + getResources().getResourceEntryName(R.id.btn));

            Intent intent = new Intent();
            intent.setClass(this, Activity_B.class);
            startActivity(intent);
        } else if(view.getId() == R.id.shape_test) {
        	
        }
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
        outState.putInt("aa", 11);
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
