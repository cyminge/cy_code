package com.example.activitytest;

import java.io.File;
import java.io.FileFilter;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cy.R;
import com.example.bean.Car;
import com.example.global.BaseActivity;
import com.example.global.BaseApplication;
import com.example.touchevent.TouchEventTestActivity;

@SuppressLint({ "ResourceAsColor", "InlinedApi" }) 
public class MainActivity extends BaseActivity implements OnClickListener {

    private LinearLayout root;
    private Button btn;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;

    private MyHandler mHandler;

    private ImageView mAnimationView;

    private static final long ANIM_DURATION = 1500;

    private static final float SCALE_PIVOT_Y = 0.5f;
    private static final float SCALE_PIVOT_X = 0.5f;
    private static final float SCALE_FROM_X = 1.0f;
    private static final float SCALE_TO_X = 0.5f;
    private static final float SCALE_FROM_Y = 1.0f;
    private static final float SCALE_TO_Y = 0.5f;

    private static final float ALPHA_FROM = 1.0f;
    private static final float ALPHA_TO = 0.2f;

    private static final float TRANSLATE_FROM_X = 0.0f;
    private static final float TRANSLATE_FROM_Y = 0.0f;

    public void startAnim() {

        final int deltaX = -200;
        final int deltaY = -500;

//        Log.e("cyTest", "deltaX = " + deltaX + ", deltaY" + deltaY);

        Animation alphaAnimation = new AlphaAnimation(ALPHA_FROM, ALPHA_TO);
        Animation scaleAnimation = new ScaleAnimation(SCALE_FROM_X, SCALE_TO_X, SCALE_FROM_Y, SCALE_TO_Y,
                Animation.RELATIVE_TO_SELF, SCALE_PIVOT_X, Animation.RELATIVE_TO_SELF, SCALE_PIVOT_Y);
        Animation translateAnimation = new TranslateAnimation(TRANSLATE_FROM_X, deltaX, TRANSLATE_FROM_Y,
                deltaY);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(ANIM_DURATION);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        mAnimationView.startAnimation(animationSet);
        
    }
    
    private void getCPUCounts() {
    	Log.e("cyTest", "cpu-1 : "+Runtime.getRuntime().availableProcessors());
    	Log.e("cyTest", "cpu-2 : "+getNumCores());
    }
    
  //CPU个数
    private int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if(Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }      
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Print exception
            e.printStackTrace();
            //Default to return 1 core
            return 1;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	setIntent(intent);
    	Log.e("cyTest", "--> MainActivity.onNewIntent from :"+getIntent().getStringExtra("from"));
    	
    }
    
    public boolean isAppForeground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if(null == am.getRunningTasks(1)) {
			return false;
		}
		RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
		String topAppPackage = foregroundTaskInfo.topActivity.getPackageName();
		Log.e("cyTeset", "--------------------topAppPackage:"+topAppPackage);
		if (topAppPackage != null && topAppPackage.contentEquals(context.getPackageName())) {
			return true;
		}
		return false;
	}
    
    @Override
    protected void onResume() {
    	super.onResume();
//    	Log.e("cyTest", "MainActivity.onResume 来自游戏大厅 :" +ProxyInstrumentation.startFromOgc());
//    	Log.e("cyTest", "MainActivity.onResume getCallingPackage:"+getCallingPackage());
    }
    
//    @Override
//    public String getCallingPackage() {
//    	return super.getCallingPackage();
//    }
    
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.e("aaa", "--onCreate--");
//        String s = null;
//        s.length();
        
//        Log.e("cyTest", "--> MainActivity.onCreate cyminge :"+getIntent().getStringExtra("cyminge"));
        if(null != getIntent()) {
        	Log.e("cyTest", "--> MainActivity.onCreate from :"+getIntent().getStringExtra("from"));
        }
        setContentView(R.layout.activity_main);
        mHandler = new MyHandler(MainActivity.this);
        getResources(StoreSize.getCurrFontScale(this));
        root = (LinearLayout) findViewById(R.id.root);
        btn = (Button) findViewById(R.id.btn);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);

//        ProgressBar progressBar = new ProgressBar(this);
//        progressBar.setProgressDrawable(getResources().getDrawable(
//                R.drawable.activation_code_receive_progress));
//        progressBar.setIndeterminate(false);
//        root.addView(progressBar, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        /*
         * <animated-rotate
         * xmlns:android="http://schemas.android.com/apk/res/android"
         * android:pivotX="50%" android:pivotY="50%" android:fromDegrees="0"
         * android:toDegrees="360">
         */
        
        ImageView iv = new ImageView(this);
        iv.setBackgroundResource(R.drawable.activation_code_receive_progress);

        RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);// 设置动画持续时间
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new LinearInterpolator());//不停顿  
        iv.setAnimation(animation);
        animation.startNow();
        root.addView(iv, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

//        if (null == mAnimationView) {
//            mAnimationView = new ImageView(this);
//            mAnimationView.setBackgroundResource(R.drawable.activation_code_gift_icon);
//        }
//
//        root.addView(mAnimationView);
        
        CheckBox mCheckBox = new CheckBox(this);
        mCheckBox.setPadding(0, 0,
                0, 0);
        mCheckBox.setButtonDrawable(getResources().getDrawable(R.drawable.check_box_on));
//        mCheckBox.setText("版本不在提示");
//        mCheckBox.setTextColor(Color.RED);
//        mCheckBox.setTextSize(20);
//        mCheckBox.setCompoundDrawablePadding(18);
//        mCheckBox.setCompoundDrawablesWithIntrinsicBounds(50, 0,
//                50, 0);
        root.setBackgroundColor(android.R.color.white);
        
//        root.addView(mCheckBox,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        
        TextView tv = new TextView(this);
        tv.setText("版本不在提示");
        tv.setTextColor(Color.RED);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        
        LinearLayout checkLayout = new LinearLayout(this);
        checkLayout.setOrientation(LinearLayout.HORIZONTAL);
        checkLayout.addView(mCheckBox,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 0, 0, 0);
        tv.setLayoutParams(lp);
        checkLayout.addView(tv);
        
        root.addView(checkLayout,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        
        EditText et = new EditText(this);
        try {
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            field.set(et, Color.rgb(0, 0, 0));
        } catch (Exception ignored) {
            ignored.printStackTrace();
//            Log.e("cyTest", "ignored");
        }

        // 测试文字的放大缩小
        float btnSize = btn.getTextSize();
        StoreSize.setSize(this, btnSize);
//        Log.e("cyTest", "--->" + Environment.getExternalStorageDirectory());
        My3GInfo aa = new My3GInfo(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

//        Log.e("cyTest", "width:" + width);
//        Log.e("cyTest", "height:" + height);
//        Log.e("cyTest", "density:" + dm.density);
        // 测试文字的放大缩小 end

        // 测试WeakReference
        softref = new SoftReference<Object>(s1);
        weakref = new WeakReference<Object>(s2);
        // 测试WeakReference end

        // Log.e("cyTest", "主线程名"+getMainLooper().getThread().getName());

        
        mHandler.sendEmptyMessageDelayed(22, 50);
        
//        getCPUCounts();
        
        Log.e("cyTest", "-->getFilesDir = " + this.getFilesDir());
        Log.e("cyTest", "-->getCacheDir = " + this.getCacheDir());
        Log.e("cyTest", "-->getExternalCacheDir = " + this.getExternalCacheDir());
        Log.e("cyTest", "-->getExternalFilesDir = " + this.getExternalFilesDir("cy"));
        Log.e("cyTest", "-->Environment.getExternalStorageState = " + Environment.getExternalStorageState());
        Log.e("cyTest", "-->Environment.getDataDirectory = " + Environment.getDataDirectory());
        Log.e("cyTest",
                "-->Environment.getDownloadCacheDirectory = " + Environment.getDownloadCacheDirectory());
        Log.e("cyTest",
                "-->Environment.getExternalStorageDirectory = " + Environment.getExternalStorageDirectory());
        Log.e("cyTest", "-->Environment.getRootDirectory = " + Environment.getRootDirectory());
        for (File file : this.getExternalCacheDirs()) {
            Log.e("cyTest", "-->getExternalCacheDirs = " + file.getPath());
        }
        
    }

    Object s1 = new String("abcdef");
    Object s2 = new String("123456");
    SoftReference<Object> softref;
    WeakReference<Object> weakref;

    boolean flag = false;

    // public void testWeakReference(View view) {
    // flag = !flag;
    // while(flag) {
    // // s1 = null;
    // // s2 = null;
    // System.gc();
    // // System.gc();
    // // try {
    // // Thread.sleep(10);
    // // } catch (InterruptedException e) {
    // // e.printStackTrace();
    // // }
    // Log.e("cyTest", "----softref=" + softref.get());
    // Log.e("cyTest", "----weakref=" + weakref.get());
    // }
    //
    // }

    public void testWeakReference(View view) {
        Car car = new Car(22000, "silver");
        WeakReference<Car> weakCar = new WeakReference<Car>(car);

        int i = 0;

        while (true) {
            // car = null;
            // System.gc();
            if (weakCar.get() != null) {
                i++;
                Log.e("cyTest", "Object is alive for " + i + " loops - " + weakCar);
            } else {
                Log.e("cyTest", "Object has been collected.");
                break;
            }
        }
    }
    
    /**
     * 测试子线程post消息到主线程执行时，主线程的运行机制
     * @param view
     */
    public void testSingleThreadRun(View view) {
    	Intent intent = new Intent();
    	intent.setClass(this, Activity_TestSingleThreadRun.class);
    	startActivity(intent);
	}

    /**
     * 测试AlertDialog传入应用context时是否会报错
     * @param view
     */
    public void testDialog(View view) {
    	Intent intent = new Intent();
    	intent.setClass(this, Activity_TestDialogWithContext.class);
    	startActivity(intent);
    	
    	
    }
    
    public void testToGameHallHome(View view) {
    	Intent intent = new Intent();
    	intent.setAction("gn.com.android.gamehall.action.HOME");
    	intent.putExtra("from", "测试包");
    	intent.putExtra("source", "测试包");
    	intent.putExtra("packageName", "com.cy.test");
    	startActivity(intent);
    }
    
    public void testToGameHallSearch(View view) {
    	Intent intent = new Intent();
    	intent.setAction("gn.com.android.gamehall.action.external.JUMP");
    	String value = "from:globalSearch2,source:globalSearch2,searchKeyWord:大话西游,action:gn.com.android.gamehall.action.SEARCH";
    	intent.putExtra("jumpParams", value);
    	startActivity(intent);
    }
    
    public void testToGameHallTagList(View view) {
    	Intent intent = new Intent();
    	intent.setAction("gn.com.android.gamehall.action.external.JUMP");
    	String value = "from:globalSearch2,source:globalSearch2,contentId:163,viewType:TagGameListView,title:腾讯游戏,action:gn.com.android.gamehall.action.TAG_GAME_LIST";
    	intent.putExtra("jumpParams", value);
    	startActivity(intent);
    }
    
    public void testToGameHallEventDetail(View view) {
    	Intent intent = new Intent();
    	intent.setAction("gn.com.android.gamehall.action.EVENT_DETAIL");
    	intent.putExtra("from", "test");
    	intent.putExtra("source", "test");
    	intent.putExtra("contentId", "1765");
    	startActivity(intent);
    	
//    	from  ： （必填）
//    	source： （必填）
//    	contentId：活动ID（必填）
//    	gameId: 游戏id （必填）
//    	target_packagename ：(选填)
//    	ad_id （用于统计，选填）
//    	"action":"gn.com.android.gamehall.action.EVENT_DETAIL" 
    }

    public Resources getResources(final float fontScale) {
        Resources res = super.getResources();
        Configuration config = res.getConfiguration();
        // config.setToDefaults();
        config.fontScale = fontScale;
//        Log.e("cyTest", "-->res.getDisplayMetrics() = " + res.getDisplayMetrics());
        DisplayMetrics dm = res.getDisplayMetrics();
        dm.scaledDensity = 1;
        res.updateConfiguration(config, dm);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                float size = StoreSize.getSize();
                if (0 == size) {
                    size = btn.getTextSize();
                }
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, size * fontScale);
                btn_1.setTextSize(TypedValue.COMPLEX_UNIT_PX, size * fontScale);
                btn_2.setTextSize(TypedValue.COMPLEX_UNIT_PX, size * fontScale);
                btn_3.setTextSize(TypedValue.COMPLEX_UNIT_PX, size * fontScale);
            }
        });
        StoreSize.setCurrFontScale(this, fontScale);
        return res;
    }

    private static class MyHandler extends Handler {

        WeakReference<MainActivity> mMainActivityWR;

        public MyHandler(MainActivity mainActivity) {
            super();
            mMainActivityWR = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainActivity mainActivity = mMainActivityWR.get();

            switch (msg.what) {
            case 11:

                break;
            case 22 :
            	Intent intent = new Intent();
            	intent.setClass(mainActivity, Activity_A.class);
            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	if(null != mainActivity.getIntent()) {
//                	intent.putExtra("cyminge", mainActivity.getIntent().getStringExtra("cyminge"));
                }
            	mainActivity.startActivity(intent);
//            	mainActivity.finish();
//            	mainActivity.isAppForeground(mainActivity);
//            	mainActivity.mHandler.sendEmptyMessageDelayed(22, 2000);
            	break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn) {
//             Intent intent = new Intent();
//             intent.setClass(this, Activity_A.class);
//             startActivity(intent);
        	
        	Intent intent = new Intent();
			intent.setClass(this, Activity_TestOnmeasure.class);
			startActivity(intent);

//            Intent intent = new Intent();
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setAction("gn.com.android.gamehall.action.HOME");
//            intent.putExtra("tabIndex", 3);
//            startActivity(intent);
        } else if (view.getId() == R.id.btn_1) {
            // getResources(1.0f);
            startAnim();
        } else if (view.getId() == R.id.btn_2) {
            getResources(1.25f);
        } else if (view.getId() == R.id.btn_3) {
            getResources(1.36f);
        }
    }
    
    int counts;
    long timeStart;

    @Override
    protected void onPause() {
        timeStart = System.currentTimeMillis();
        super.onPause();
        if (isBack) {
            // finish();
            // return ;
        }
        // for (int i = 0; i < 100000000; i++) {
        // counts++;
        // }
        // for (int i = 0; i < 100000000; i++) {
        // counts++;
        // }
        // for (int i = 0; i < 100000000; i++) {
        // counts++;
        // }
        // for (int i = 0; i < 100000000; i++) {
        // counts++;
        // }
        // Log.e("cyTest", "Activity_B.onPause.counts-->" + counts);
//        Log.e("cyTest", "onpause time --> " + (System.currentTimeMillis() - timeStart));

        // for (int i = 0; i < 100000000; i++) {
        // counts++;
        // }
        // Log.e("cyTest", "Activity_B.onPause.counts-->" + counts);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Intent intent = new Intent();
        // intent.setClass(this, MainActivity.class);
        // startActivity(intent);

        moveTaskToBack(true);
        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.e("cyTest", "onStop time --> " + (System.currentTimeMillis() - timeStart));
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("cyTest", "onDestroy time --> onDestroy");
//        BaseApplication.getInstance().exit();
    }

    private boolean isBack = false;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isBack = true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected boolean canAsBootActivity() {
        return true;
    }
    
}
