package com.cy.animation.lock;

import com.cy.R;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Window;

public class MogooLockScreenActivity extends Activity {
    
	public static Window mWindow;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindowManager();
        
        mWindow = getWindow();
        
//        setContentView(new UnlockSlideTab(this));
        setContentView(R.layout.unlock_main);
    }
}