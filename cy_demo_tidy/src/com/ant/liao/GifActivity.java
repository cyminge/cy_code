package com.ant.liao;

import android.app.Activity;
import android.os.Bundle;

import com.cy.R;

public class GifActivity extends Activity {

	private GifView mGifView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.gif_view_main);
		
		mGifView = (GifView) findViewById(R.id.gif_view);
		mGifView.setGifImage(R.drawable.buzhuangbi);
		
	}
	
}
