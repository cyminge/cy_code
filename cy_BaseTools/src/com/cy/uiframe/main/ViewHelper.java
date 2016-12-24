package com.cy.uiframe.main;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cy.R;
import com.cy.utils.Utils;

public class ViewHelper {
    public static final int TYPE_NO_EXCEPTION = -1;
    public static final int TYPE_NO_NETWORK = 0;
    public static final int TYPE_NO_DATA = 1;
    public static final int TYPE_LIST_BOTTOM = 2;
    public static final int TYPE_NO_UPGRADE = 3;
    public static final int TYPE_NO_ACTION = 4;
    public static final int TYPE_NOT_ENOUGH_DATA = 5;
    private static final int DEFAULT_ICON_ID = 0;

    private TextView mExceptionDescriptionView;
    protected View mProgressBar;
    protected View mUnnetworkLayout;
    protected View mUnnetworkShadow;
    protected View mContentView;
    protected View mShadowView;
    protected ImageView mExceptionIcon;
    private boolean mInTask = false;

    public ViewHelper(View rootView, View contentView) {
        mProgressBar = rootView.findViewById(R.id.page_loading);
        mShadowView = rootView.findViewById(R.id.helper_shadow);
        mUnnetworkLayout = rootView.findViewById(R.id.rl_unnetwork);
        mExceptionIcon = (ImageView) rootView.findViewById(R.id.unnetwork_icon);
        mExceptionDescriptionView = (TextView) rootView.findViewById(R.id.exception_description);
        mUnnetworkShadow = rootView.findViewById(R.id.unnetwork_shadow);
        mContentView = contentView;
    }

    public void showContent() {
        setContentViewVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mUnnetworkLayout.setVisibility(View.GONE);
    }

    public void showLoadingView() {
        setContentViewVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mUnnetworkLayout.setVisibility(View.GONE);
    }

    public void hideShadowView() {
        mShadowView.setVisibility(View.GONE);
    }

    public boolean isLoading() {
        return mProgressBar.getVisibility() == View.VISIBLE;
    }

    public boolean isShowException() {
        return mUnnetworkLayout.getVisibility() == View.VISIBLE;
    }

    public void showExceptionView(int type, boolean isNeedShadowAtTopWhenUnnet) {
        int resId = R.string.str_no_net_msg;
        int iconId = R.drawable.uiframe_no_content;
        if (type == TYPE_NO_DATA) {
            resId = getNoDataStr();
            iconId = R.drawable.uiframe_no_content;
        } else if (type == TYPE_NO_UPGRADE) {
            resId = R.string.str_no_upgrade;
            iconId = R.drawable.uiframe_no_content;
        }
        showExceptionView(Utils.getResources().getString(resId), iconId, isNeedShadowAtTopWhenUnnet);
    }

    protected int getNoDataStr() {
        return R.string.uiframe_no_data;
    }

    public void showExceptionView(String tips, int iconId, boolean isNeedShadowAtTopWhenUnnet) {
        setContentViewVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mUnnetworkLayout.setVisibility(View.VISIBLE);
        if (iconId != DEFAULT_ICON_ID && !mInTask) {
            mExceptionIcon.setBackgroundResource(iconId);
        }
        mExceptionDescriptionView.setText(tips);
        mUnnetworkShadow.setVisibility(isNeedShadowAtTopWhenUnnet? View.VISIBLE : View.GONE);
    }

    public void hideAllView() {
        mProgressBar.setVisibility(View.GONE);
        mUnnetworkLayout.setVisibility(View.GONE);
        setContentViewVisibility(View.GONE);
    }

    protected void setContentViewVisibility(int visibilityId) {
        mContentView.setVisibility(visibilityId);
    }

    public void setTaskException() {
        mExceptionIcon.setBackgroundResource(R.drawable.uiframe_no_content_task);
        mExceptionDescriptionView.setTextColor(Color.WHITE);
        mInTask = true;
    }

}
