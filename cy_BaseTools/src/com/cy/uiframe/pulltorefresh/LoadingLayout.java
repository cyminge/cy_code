package com.cy.uiframe.pulltorefresh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.global.WatchDog;

public class LoadingLayout extends LinearLayout {
    private RelativeLayout mInnerLayout;
    private final PullView mRefreshingIcon;
    private int mContentSize;
    private Animation mAnim;
    
    private Context mContext;

    public LoadingLayout(Context context) {
        super(context);
        
        mContext = context;
        
        LayoutInflater.from(mContext).inflate(R.layout.pull_to_refresh_header, this);

        mInnerLayout = (RelativeLayout) findViewById(R.id.fl_inner);
        mRefreshingIcon = (PullView) mInnerLayout.findViewById(R.id.pull_to_refresh_icon);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mInnerLayout.getLayoutParams();
        lp.gravity = Gravity.TOP;

        mContentSize = context.getResources().getDimensionPixelSize(R.dimen.refreshing_content_height);
        mAnim = createRotateAnim();
        reset();
    }
    
    public static RotateAnimation createRotateAnim() {
        RotateAnimation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, Constant.ANIM_CENTER,
                Animation.RELATIVE_TO_SELF, Constant.ANIM_CENTER);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Constant.ANIM_CYCLE);
        anim.setFillAfter(true);
        anim.setDuration(Constant.ANIM_DURATION);
        return anim;
    }

    public final void setHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = height;
        requestLayout();
    }

    public final void setWidth(int width) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = width;
        requestLayout();
    }

    public final int getRefreshingIconSize() {
        return mRefreshingIcon.getSize();
    }

    public final int getContentSize() {
        return mContentSize;
    }

    public void offsetYChange(float y) {
        y = Math.abs(y);

        float progress = y / mContentSize;
        mRefreshingIcon.setProgress(progress);
    }

    public final void pullToRefresh() {
        checkContentSize();
        mRefreshingIcon.clearAnimation();
    }

    @SuppressLint("NewApi") 
    private void checkContentSize() {
        Activity activity = WatchDog.INSTANCE.getTopActivity(); 
        if (activity == null || !isLollipop()) {
            return;
        }

        Window window = activity.getWindow();
        int chosenStatusBarolor = mContext.getResources().getColor(R.color.chosen_status_bar_color);
        int color = window.getStatusBarColor();

        if (chosenStatusBarolor == color) {
            mContentSize = mContext.getResources().getDimensionPixelSize(R.dimen.refreshing_content_height)
                    + mContext.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
        } else {
            mContentSize = mContext.getResources().getDimensionPixelSize(R.dimen.refreshing_content_height);
        }
    }
    
    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    }
    
    public final void refreshing() {
        mRefreshingIcon.clearAnimation();
        mRefreshingIcon.startAnimation(mAnim);
    }

    public final void releaseToRefresh() {
    }

    public final void reset() {
        mRefreshingIcon.clearAnimation();
    }

}
