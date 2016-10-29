package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TestActivityJumpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("cyTest", "--> 到了这里没？");
        if(null != intent) {
            Log.e("cyTest", "--> "+intent.getStringExtra("cyTest"));
        }
    }

}
