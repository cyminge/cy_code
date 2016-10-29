package com.example.activitytest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cy.R;
import com.example.touchevent.TouchEventTestActivity;
import com.example.util.BitmapCompress;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class Activity_C extends Activity implements OnClickListener  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c);
		
		mHandler.sendEmptyMessageDelayed(9527, 120);
		
	}
	
	private static final int MAX_THUMB_SIZE = 32 * 1024;
	
	Handler mHandler = new Handler() {
	    public void handleMessage(android.os.Message msg) {
	        
	        
	        ImageView img_ora = (ImageView) findViewById(R.id.img_ora);
	        ImageView img_new = (ImageView) findViewById(R.id.img_new);
	        ImageView img_final = (ImageView) findViewById(R.id.img_final);
	        
	        Resources resource = getResources();
	        BitmapFactory.Options opts = new BitmapFactory.Options();
	        Bitmap rgb8888 = BitmapFactory.decodeResource(resource, R.drawable.root_bg, opts);
	        img_ora.setImageBitmap(rgb8888);
	        
	        opts.inPreferredConfig = Bitmap.Config.RGB_565;
	        Bitmap rgb565 = BitmapFactory.decodeResource(resource, R.drawable.root_bg, opts);
//	        img_new.setImageBitmap(rgb565);
	        Log.e("cyTest", "=======================================================");
	        Bitmap thumb = BitmapCompress.fullCompressBitmap(rgb565, 150, 150, MAX_THUMB_SIZE);
	        if (thumb != null) {
	            Log.e("cyTest", "thumb.name-->"+thumb.getConfig().name());
                Log.e("cyTest", "thumb.AllocationByteCount1-->"+thumb.getAllocationByteCount());
                Log.e("cyTest", "thumb.AllocationByteCount2-->"+thumb.getWidth()*thumb.getHeight()*4);
                Log.e("cyTest", "thumb.byteArray-->"+BitmapCompress.bmpToByteArray(thumb, true).length);
                img_new.setImageBitmap(thumb);
                img_final.setImageBitmap(thumb);
            }
	       
	        

	        TypedValue mTypedValue = new TypedValue();
	        resource.getValue(R.drawable.root_bg, mTypedValue, true);
	        Log.e("cyTest", "mTypedValue-->"+mTypedValue);

	        
	    };
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.btn){
//			Intent intent = new Intent();
//			intent.setClass(this, Activity_D.class);
//			startActivity(intent);
			
			Intent intent = new Intent();
			intent.setClass(this, TouchEventTestActivity.class);
			startActivity(intent);
		}
	}

}
