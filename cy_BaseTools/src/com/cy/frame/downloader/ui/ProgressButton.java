package com.cy.frame.downloader.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.utils.Utils;

public class ProgressButton extends RelativeLayout implements IProgressButton {
    private View mProgressView;
    private TextView mTextView;

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        initView();
    }

    private void initView() {
        mProgressView = findViewById(R.id.progress);
        mTextView = (TextView) findViewById(R.id.download_text);
    }

    @Override
    public void setText(CharSequence charSequence) {
        mTextView.setText(charSequence);
    }

    @Override
    public void setButton(DownloadArgs args, int status, float progress) {
        setProgress(status, progress);
        setBackground(args, status);
        setText(args, status, progress);
        setTextColor(status);
        setEnabled(ButtonStatusManager.BUTTON_STATUS_DISABLE != status);
    }

    private void setProgress(int status, float progress) {
        mProgressView.setBackgroundResource(getProgressBg(status));
        Drawable background = mProgressView.getBackground();
        if (background != null) {
            background.setLevel((int) (progress * Constant.MAX_PROGRESS_LEVEL));
        }
    }

    protected int getProgressBg(int status) {
        switch (status) {
            case ButtonStatusManager.BUTTON_STATUS_RUNNING:
                return R.drawable.download_green_progress;
            case ButtonStatusManager.BUTTON_STATUS_PAUSE:
                return R.drawable.download_orange_progress;
            default:
                return 0;
        }
    }

    private void setBackground(DownloadArgs args, int status) {
        int resId;
        resId = getBackgroundId(args, status);
        setBackgroundResource(resId);
    }

    protected int getBackgroundId(DownloadArgs args, int status) {
        if (ButtonStatusManager.isOpenTest(args, status)) {
            status = ButtonStatusManager.BUTTON_STATUS_DOWNLOAD;
        }
        int resId;
        switch (status) {
            case ButtonStatusManager.BUTTON_STATUS_DOWNLOAD:
            case ButtonStatusManager.BUTTON_STATUS_OPEN:
            case ButtonStatusManager.BUTTON_STATUS_INSTALLING:
            case ButtonStatusManager.BUTTON_STATUS_INSTALL:
            case ButtonStatusManager.BUTTON_STATUS_FAILED:
                resId = R.drawable.download_green_chunk;
                break;
            case ButtonStatusManager.BUTTON_STATUS_GAME_SUBSCRIBE:
                resId = R.drawable.download_green_blue;
                break;
            case ButtonStatusManager.BUTTON_STATUS_RUNNING:
            case ButtonStatusManager.BUTTON_STATUS_DISABLE:
            case ButtonStatusManager.BUTTON_STATUS_PAUSE:
            case ButtonStatusManager.BUTTON_STATUS_GAME_SUBSCRIBED:
                resId = R.drawable.download_gray_chunk;
                break;
            case ButtonStatusManager.BUTTON_STATUS_UPGRADE:
                resId = R.drawable.download_orange_chunk;
                break;
            case ButtonStatusManager.BUTTON_STATUS_REWARD_DOWNLOAD:
            case ButtonStatusManager.BUTTON_STATUS_REWARD_UPGRADE:
                resId = R.drawable.download_red_chunk;
                break;
            default:
                resId = 0;
                break;
        }
        return resId;
    }

    private void setText(DownloadArgs args, int status, float progress) {
        if (ButtonStatusManager.isOpenTest(args, status)) {
            status = ButtonStatusManager.BUTTON_STATUS_DOWNLOAD;
        }
        String textStr = (Utils.getDownloadBtnText())[status];
        if (ButtonStatusManager.BUTTON_STATUS_RUNNING == status
                || ButtonStatusManager.BUTTON_STATUS_PAUSE == status) {
            String percent = ((int) (progress * 100)) + "%";
            textStr += percent;
        }
        setText(textStr);
    }

    private void setTextColor(int status) {
        int resId;
        resId = getTextColor(status);
        mTextView.setTextColor(Utils.getResources().getColor(resId));
    }

    protected int getTextColor(int status) {
        return R.color.white;
    }

}
