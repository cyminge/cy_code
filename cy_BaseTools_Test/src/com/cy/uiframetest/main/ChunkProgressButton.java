package com.cy.uiframetest.main;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.ui.ProgressButton;
import com.cy.test.R;

public class ChunkProgressButton extends ProgressButton {

    public ChunkProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getBackgroundId(DownloadArgs args, int status) {
        status = ButtonStatusManager.convertOpenTestStatus(args, status);
        int resId;
        switch (status) {
            case ButtonStatusManager.BUTTON_STATUS_INSTALLING:
            case ButtonStatusManager.BUTTON_STATUS_INSTALL:
            case ButtonStatusManager.BUTTON_STATUS_DOWNLOAD:
            case ButtonStatusManager.BUTTON_STATUS_OPEN:
                resId = R.drawable.download_green_chunk;
                break;
            case ButtonStatusManager.BUTTON_STATUS_GAME_SUBSCRIBE:
            case ButtonStatusManager.BUTTON_STATUS_GAME_SUBSCRIBE_DOWNLOAD:
                resId = R.drawable.download_blue_chunk;
                break;
            case ButtonStatusManager.BUTTON_STATUS_RUNNING:
            case ButtonStatusManager.BUTTON_STATUS_PAUSE:
            case ButtonStatusManager.BUTTON_STATUS_FAILED:
            case ButtonStatusManager.BUTTON_STATUS_GAME_SUBSCRIBED:
            case ButtonStatusManager.BUTTON_STATUS_DISABLE:
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

    @Override
    protected int getTextColor(int status) {
        return R.color.white;
    }
}
