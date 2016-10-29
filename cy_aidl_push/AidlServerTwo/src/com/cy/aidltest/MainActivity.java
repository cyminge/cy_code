package com.cy.aidltest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.cy.aidlservertwotest.R;

/**
 * aidl服务端启动
 * @author zhanmin
 *
 */
public class MainActivity extends Activity {
	TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}