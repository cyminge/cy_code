package com.cy.animation.lock.copy;

import android.app.Activity;
import android.os.Bundle;

public class MogooLockScreenActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindowManager();
        setContentView(new UnlockSlideTab(this));
    }
}