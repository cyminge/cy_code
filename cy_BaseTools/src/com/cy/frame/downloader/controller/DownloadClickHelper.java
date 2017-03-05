package com.cy.frame.downloader.controller;

import com.cy.frame.downloader.controller.SingleDownloadManager.SingleDownloadListener;
import com.cy.frame.downloader.core.DownloadStatusMgr;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.ui.IProgressButton;
import com.cy.frame.downloader.util.GameInstaller;
import com.cy.global.BaseApplication;
import com.cy.utils.Utils;

/**
 * 下载按钮点击
 * @author JLB6088
 *
 */
public class DownloadClickHelper {

    private DownloadClickCallback mDownloadClickCallback;
    private DownloadStatusMgr mDownloadStatusMgr;

    public DownloadClickHelper() {
        this(null);
    }
    
    public DownloadClickHelper(DownloadClickCallback downloadClickCallback) {
        mDownloadClickCallback = downloadClickCallback;
        mDownloadStatusMgr = DownloadStatusMgr.getNormalInstance();
    }

    /**
     * 下载按钮点击事件回调
     * @author JLB6088
     *
     */
    public interface DownloadClickCallback {
        public void onPauseDownload();
        public void onResumeDownload();
        public void onInstall(boolean result, IProgressButton button, DownloadArgs data);
        public SingleDownloadListener getSingleDownloadListener(IProgressButton button, DownloadArgs data);
    }
    

    public SingleDownloadListener getSingleDownloadListener(IProgressButton button, DownloadArgs data) {
        if(null != mDownloadClickCallback) {
            return mDownloadClickCallback.getSingleDownloadListener(button, data);
        }
        
        return null;
    }

    public void clickHandle(DownloadArgs args) {
        clickHandle(null, args);
    }

    /**
     * 进度条按钮点击事件
     * @param button
     * @param args
     */
    public void clickHandle(IProgressButton button, DownloadArgs args) {
        if (null == args) {
            return;
        }

        int status = getCurrentStatus(args);
        switch (status) {
            case ButtonStatusManager.BUTTON_STATUS_RUNNING:
                pauseDownloadTask(args);
                if(null != mDownloadClickCallback) {
                    mDownloadClickCallback.onPauseDownload();
                }
                break;
            case ButtonStatusManager.BUTTON_STATUS_PAUSE:
            case ButtonStatusManager.BUTTON_STATUS_FAILED:
                resumeDownloadTask(args);
                if(null != mDownloadClickCallback) {
                    mDownloadClickCallback.onResumeDownload();
                }
                break;
            case ButtonStatusManager.BUTTON_STATUS_INSTALL:
                if(null != mDownloadClickCallback) {
                    mDownloadClickCallback.onInstall(startInstall(args), button, args);
                }
                break;
            case ButtonStatusManager.BUTTON_STATUS_DOWNLOAD:
            case ButtonStatusManager.BUTTON_STATUS_UPGRADE:
                startDownload(args, false, button);
                showGiftToast(args);
                break;
            case ButtonStatusManager.BUTTON_STATUS_REWARD_DOWNLOAD:
            case ButtonStatusManager.BUTTON_STATUS_REWARD_UPGRADE:
                startRewardDownload(args, button);
                break;
            case ButtonStatusManager.BUTTON_STATUS_OPEN:
                launchGame(args.packageName);
                break;
            default:
                break;
        }
    }
    
    protected void launchGame(String packageName) {
    	Utils.launchGame(packageName);
    }

    private int getCurrentStatus(DownloadArgs args) {
        if (args == null) {
            return ButtonStatusManager.BUTTON_STATUS_RUNNING; //??
        }

        return ButtonStatusManager.getButtonStatus(args);
    }

    /**
     * 安装应用
     * @param args
     * @return
     */
    private boolean startInstall(DownloadArgs args) {
        return GameInstaller.manualInstall(BaseApplication.getAppContext(), args);
    }

    /**
     * 开始下载
     * @param args
     * @param isApplyFail
     * @param button
     */
    private void startDownload(DownloadArgs args, boolean isApplyFail, IProgressButton button) {
        SingleDownloadManager singleTask = new SingleDownloadManager();
        singleTask.execute(args, getSingleDownloadListener(button, args), isApplyFail);
    }

    /**
     * 有奖下载
     * @param args
     * @param button
     */
    private void startRewardDownload(final DownloadArgs args, final IProgressButton button) {
    	startDownload(args, false, button);
//        RewardDialogMgr.sendRewardClickStatis(args);
//        RewardDialogMgr.startRewardDownload(args, mActivity, new RewardDialogMgr.DownloadCallback() {
//            @Override
//            public void postDownload() {
//                startDownload(args, false, button);
//                if (!Utils.isLogin()) {
//                    showGiftToast(args);
//                }
//            }
//        });
    }

    /**
     * 这个是什么功能??
     * @param args
     */
    private void showGiftToast(DownloadArgs args) {
//    	if(args.reward != null && !TextUtils.isEmpty(args.reward.remindDes)) {
//		Toast.makeText(WatchDog.getContext(), "显示礼物提示", Toast.LENGTH_SHORT).show();
//    	}
    }

    /**
     * 暂停任务
     * @param args
     */
    private void pauseDownloadTask(DownloadArgs args) {
        mDownloadStatusMgr.pauseDownloadTask(args, DownloadStatusMgr.TASK_PAUSE_BY_USER);
    }

    /**
     * 重启任务
     * @param args
     */
    private void resumeDownloadTask(DownloadArgs args) {
        mDownloadStatusMgr.resumeDownloadTask(args, DownloadStatusMgr.TASK_RESUME_BY_USER);
    }

}
