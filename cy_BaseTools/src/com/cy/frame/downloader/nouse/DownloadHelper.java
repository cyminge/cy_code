package com.cy.frame.downloader.nouse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.controller.DownloadClickHelper;
import com.cy.frame.downloader.controller.DownloadClickHelper.DownloadClickCallback;
import com.cy.frame.downloader.controller.SingleDownloadManager.SingleDownloadListener;
import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.frame.downloader.core.DownloadInfoMgr.DownloadChangeListener;
import com.cy.frame.downloader.download.DownloadArgsFactory;
import com.cy.frame.downloader.download.DownloadArgsFactory.DownloadArgsListener;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.ui.IProgressButton;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager.UpgradeAppInfo;
import com.cy.listener.GameListener;
import com.cy.listener.GameListenerManager;
import com.cy.threadpool.NormalThreadPool;
import com.cy.utils.Utils;

@SuppressLint("NewApi") public class DownloadHelper {

    private View mDownloadView;
    private DownloadArgs mDownloadArgs;
    private Button mDownButton;
    private ImageView mBar;
    private RelativeLayout mDownloadPanel;
    private String mFileName;
    private DownloadArgsFactory mDownloadArgsFactory;
    protected Activity mActivity;

    private DownloadInfoMgr mDownloadInfoMgr;
    private DownloadClickHelper mClickHelper;

    protected boolean mIsExit = false;

    private DownloadChangeListener mDownloadListener = new DownloadChangeListener() {

        @Override
        public void onDownloadChange() {
            update(false);
        }
    };

    protected DownloadArgsListener mListener = new DownloadArgsListener() {

        @Override
        public void onGetDownloadArgs(DownloadArgs downloadArgs) {
            mDownloadArgs = downloadArgs;
            initFileName();
            if (needShowView()) {
                showView();
            }
            GameListenerManager.onEvent(GameListenerManager.DOWNLOADARGS_DONE);
        }
    };

    private GameListener mUpgradeInfoListener = new GameListener() {

        @Override
        public void onEvent(int key, Object... params) {
            if (isViewShow()) {
                switch (key) {
                    case GameListenerManager.UPGRADE_TYPE_CHANGE:
                        update(true);
                        break;
                    default:
                        break;
                }
            }
        }

    };

    private SingleDownloadListener mLocalUpgradeListener = new SingleDownloadListener() {

        @Override
        public void onStartDownload(Long downId, DownloadArgs downloadArgs) {
//            RewardDialogMgr.onStartDownload(mActivity, downloadArgs); // cyminge modify
//            sendStartDownloadStatis();
        }

        @Override
        public void onResetDownload(DownloadArgs downloadArgs) {
            onStatusChange(getCurrentStatus());
        }

        @Override
        public void onInstalling(DownloadArgs downloadArgs) {
            setBtnText(ButtonStatusManager.BUTTON_STATUS_INSTALLING, R.string.game_installing);
        }
    };

    protected void sendStartDownloadStatis() {

    }

    public DownloadHelper(Activity activity, String gameId, View view) {
        this(activity, gameId, Constant.EMPTY, view);
    }

    public DownloadHelper(Activity activity, String gameId, String gamePkgName, View view) {
        this.mActivity = activity;
        this.mDownloadView = view;
        this.mDownloadInfoMgr = DownloadInfoMgr.getNormalInstance();

        initView(view);
        mDownloadArgsFactory = createDownloadArgsFoctory(activity, gameId, gamePkgName);

        int[] keys = {GameListenerManager.UPGRADE_TYPE_CHANGE};
        GameListenerManager.addListener(mUpgradeInfoListener, keys);
    }

    protected DownloadArgsFactory createDownloadArgsFoctory(Activity activity, String gameId,
            String gamePkgName) {
        return new DownloadArgsFactory(activity, gameId, gamePkgName, mListener);
    }

    private void initView(View view) {
        mBar = (ImageView) view.findViewById(R.id.progress);
//        mDownloadPanel = (RelativeLayout) view.findViewById(R.id.default_panel); // cyminge modify
//        mDownButton = (Button) view.findViewById(R.id.btn_download);
//        mDownButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                onClickDownload();
//            }
//        });
    }

    public String getGameName() {
        return mDownloadArgs == null ? Constant.EMPTY : mDownloadArgs.name;
    }

    public void onDestroy() {
        mIsExit = true;
        if (mDownloadArgsFactory != null) {
            mDownloadArgsFactory.onDestroy();
        }
        mDownloadInfoMgr.removeChangeListener(mDownloadListener);
        GameListenerManager.removeListener(mUpgradeInfoListener);
    }

    public void onResume() {
        if (isViewShow()) {
        	NormalThreadPool.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    final int status = getCurrentStatus();
                    boolean fileMatch = Utils.isSamePackage(mFileName, mDownloadArgs.packageName);
                    onResumeCheck(status, fileMatch);
                }
            });
        }
    }

    private void onResumeCheck(final int status, final boolean fileMatch) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (apkHasDeleted(status, fileMatch)) {
                    ButtonStatusManager.removeDownloaded(mDownloadArgs.packageName);
                    mDownloadInfoMgr.removeDownloadInfo(mDownloadArgs.packageName);
                    onStatusChange(status);
                }

                if (apkHasAdded(status, fileMatch)) {
                    ButtonStatusManager.addDownloaded(mDownloadArgs.packageName);
                    onStatusChange(status);
                }
            }
        });
    }

    private boolean apkHasDeleted(int status, boolean fileMatch) {
        return status == ButtonStatusManager.BUTTON_STATUS_INSTALL && !fileMatch;
    }

    private boolean apkHasAdded(int status, boolean fileMatch) {
        return status == ButtonStatusManager.BUTTON_STATUS_DOWNLOAD && fileMatch;
    }

    private void initFileName() {
        mFileName = mDownloadArgs.packageName + Constant.APK;
    }

    public void getDownloadArgs() {
        if (isViewShow()) {
            return;
        }
        mDownloadArgsFactory.getDownloadArgs();
    }

    public boolean isViewShow() {
        return mDownloadView.getVisibility() == View.VISIBLE;
    }

    private boolean needShowView() {
        return !Utils.isSameClient(mDownloadArgs.packageName) || isUpgrade();
    }

    private void showView() {
        if (mActivity.isFinishing()) {
            return;
        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDownloadView.setVisibility(View.VISIBLE);
                setDownloadView(mDownloadView, mDownloadArgs);
                onResume();
                onStatusChange(getCurrentStatus());
                mDownloadInfoMgr.addChangeListener(mDownloadListener);
                onDownloadShowed();
            }
        });
    }

    protected void onDownloadShowed() {
        GameListenerManager.onEvent(GameListenerManager.GAME_DETAIL_DOWNLOAD_SHOWED);
    }

    protected void setDownloadView(View downloadVew, DownloadArgs downloadArgs) {
    }

    private void onClickDownload() {
        if (mClickHelper == null) {
            
            mClickHelper = new DownloadClickHelper(new DownloadClickCallback() {
                @Override
                public void onResumeDownload() {
                    handleDownloadChange(R.string.pause, ButtonStatusManager.BUTTON_STATUS_RUNNING);
                }

                @Override
                public void onPauseDownload() {
                    handleDownloadChange(R.string.resume, ButtonStatusManager.BUTTON_STATUS_PAUSE);
                }

                @Override
                public void onInstall(boolean result, IProgressButton button, DownloadArgs data) {
                    manualInstall(result);
                }

                @Override
                public SingleDownloadListener getSingleDownloadListener(IProgressButton button, DownloadArgs data) {
                    return mLocalUpgradeListener;
                }
            });
                 
        }
        mClickHelper.clickHandle(mDownloadArgs);
    }

    private void manualInstall(boolean result) {
        if (result) {
            setBtnText(ButtonStatusManager.BUTTON_STATUS_INSTALLING, R.string.game_installing);
        } else {
            setBtnText(ButtonStatusManager.BUTTON_STATUS_DOWNLOAD, R.string.download);
        }
    }

    private void update(final boolean reset) {
        if (mActivity.isFinishing() || !mActivity.hasWindowFocus()) {
            return;
        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (reset) {
                    resetProgress();
                }
                onStatusChange(getCurrentStatus());
            }
        });
    }

    private void onStatusChange(int status) {
        if (ButtonStatusManager.isOpenTest(mDownloadArgs, status)) {
            status = ButtonStatusManager.BUTTON_STATUS_DOWNLOAD;
        }
        
        switch (status) {
            case ButtonStatusManager.BUTTON_STATUS_DOWNLOAD:
                setBtnText(status, R.string.download, getAppendSize());
                break;
            case ButtonStatusManager.BUTTON_STATUS_RUNNING:
                handleDownloadChange(R.string.pause, status);
                break;
            case ButtonStatusManager.BUTTON_STATUS_PAUSE:
                handleDownloadChange(R.string.resume, status);
                break;
            case ButtonStatusManager.BUTTON_STATUS_INSTALL:
                if (Utils.isSamePackage(mFileName, mDownloadArgs.packageName)) {
                    setBtnText(ButtonStatusManager.BUTTON_STATUS_INSTALL, R.string.install);
                } else {
                    ButtonStatusManager.removeDownloaded(mDownloadArgs.packageName);
                    mDownloadInfoMgr.removeDownloadInfo(mDownloadArgs.packageName);
                    Utils.deleteFile(mFileName);
                    setBtnText(status, R.string.download, getAppendSize());
                }
                break;
            case ButtonStatusManager.BUTTON_STATUS_INSTALLING:
                setBtnText(status, R.string.game_installing);
                break;
            case ButtonStatusManager.BUTTON_STATUS_OPEN:
                setBtnText(status, R.string.launch);
                break;
            case ButtonStatusManager.BUTTON_STATUS_REWARD_DOWNLOAD:
                setBtnText(status, R.string.reward_download, getAppendSize());
                break;
            case ButtonStatusManager.BUTTON_STATUS_REWARD_UPGRADE:
                setBtnText(status, R.string.reward_upgrade, getAppendSize());
                break;
            case ButtonStatusManager.BUTTON_STATUS_UPGRADE:
                UpgradeAppInfo info = GamesUpgradeManager.getOneAppInfo(mDownloadArgs.packageName);
                if (isIncreaseUpgrade()) {
                    setBtnText(status, getIncreaseUpdateResId(), getPatchSize(info));
                } else {
                    setBtnText(status, R.string.onekey_update, getAppendSize());
                }
                break;
            case ButtonStatusManager.BUTTON_STATUS_FAILED:
                setBtnText(status, R.string.retry);
                break;
            case ButtonStatusManager.BUTTON_STATUS_DISABLE:
                mDownloadView.setVisibility(View.GONE);
                break;
            default:
                setBtnText(status, R.string.download, getAppendSize());
                break;
        }

    }

    protected int getIncreaseUpdateResId() {
        return R.string.increase_update;
    }

    protected String getAppendSize() {
        return mDownloadArgs.size + Constant.M;
    }

    protected String getPatchSize(UpgradeAppInfo info) {
        return info.mPatchSize + Constant.M;
    }

    private void setBtnText(int downloadStatus, int resId, String appendStr) {
        int progressResId;
        boolean enabled = true;

        if (downloadStatus != ButtonStatusManager.BUTTON_STATUS_RUNNING
                && downloadStatus != ButtonStatusManager.BUTTON_STATUS_PAUSE) {
            mDownloadPanel.setBackgroundResource(0);
        } else {
            mDownloadPanel.setBackgroundResource(getDownloadPanelBgId());
        }

        switch (downloadStatus) {
            case ButtonStatusManager.BUTTON_STATUS_RUNNING:
                progressResId = getRunnningProgressResId();// R.drawable.progress_bar_downloading;
                break;
            case ButtonStatusManager.BUTTON_STATUS_PAUSE:
                progressResId = getPauseProgressResId();// R.drawable.progress_bar_pause;
                break;
            case ButtonStatusManager.BUTTON_STATUS_REWARD_DOWNLOAD:
            case ButtonStatusManager.BUTTON_STATUS_REWARD_UPGRADE:
                progressResId = getRewardProgressResId();// R.drawable.progress_bar_reward_download;
                break;
            case ButtonStatusManager.BUTTON_STATUS_INSTALLING:
                progressResId = getNormalProgressResId();// R.drawable.progress_bar_normal;
                enabled = false;
                break;
            case ButtonStatusManager.BUTTON_STATUS_OPEN:
            case ButtonStatusManager.BUTTON_STATUS_INSTALL:
            case ButtonStatusManager.BUTTON_STATUS_DOWNLOAD:
            case ButtonStatusManager.BUTTON_STATUS_UPGRADE:
            case ButtonStatusManager.BUTTON_STATUS_DISABLE:
            case ButtonStatusManager.BUTTON_STATUS_FAILED:
            default:
                progressResId = getNormalProgressResId();// R.drawable.progress_bar_normal;
                break;
        }
        mBar.setBackgroundResource(progressResId);
        mDownButton.setEnabled(enabled);

        if (appendStr.isEmpty()) {
            mDownButton.setText(resId);
        } else {
            String displayStr = mActivity.getString(resId) + " " + appendStr;
            mDownButton.setText(displayStr);
        }
    }

    protected int getDownloadPanelBgId() {
        return R.drawable.progress_bar_background;
    }

    protected int getRunnningProgressResId() {
        return R.drawable.progress_bar_downloading;
    }

    protected int getPauseProgressResId() {
        return R.drawable.progress_bar_pause;
    }

    protected int getRewardProgressResId() {
        return R.drawable.progress_bar_reward_download;
    }

    protected int getNormalProgressResId() {
        return R.drawable.progress_bar_normal;
    }

    private void setBtnText(int downloadStatus, int resId) {
        setBtnText(downloadStatus, resId, Constant.EMPTY);
    }

    private void handleDownloadChange(int resId, int downloadStatus) {
        DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(mDownloadArgs.packageName);
        if (info == null) {
            return;
        }
        onDownloadRunning(info.mProgress, info.mTotalSize, resId, downloadStatus);
    }

    private void onDownloadRunning(long progress, long total, int resId, int downloadStatus) {
        if (total < 0) {
            total = Utils.getPkgTotalByte(mDownloadArgs.packageName, mDownloadArgs.size);
        }

        String percentText = Utils.getPercentText(progress, total);
        setBtnText(downloadStatus, resId, percentText);
        updateProgress(progress, total);
    }

    private void updateProgress(long progress, long max) {
        if (max <= 0) {
            setProgressLevel(0);
        } else {
            long level = (progress * Constant.MAX_PROGRESS_LEVEL) / max;
            setProgressLevel((int) level);
        }
    }

    private void setProgressLevel(int level) {
        mBar.getBackground().setLevel(level);
    }

    private void resetProgress() {
        setProgressLevel(0);
    }

    private boolean isUpgrade() {
        return GamesUpgradeManager.hasNewVersion(mDownloadArgs.packageName);
    }

    private boolean isIncreaseUpgrade() {
        return GamesUpgradeManager.isIncreaseType(mDownloadArgs.packageName);
    }

    public String getGamePackage() {
        if (mDownloadArgs == null) {
            return Constant.EMPTY;
        }

        return mDownloadArgs.packageName;
    }

    public void startDownload(long gameId) {
        if (mDownloadArgs == null || gameId == -1) {
            return;
        }

        onClickDownload();
    }

    private int getCurrentStatus() {
        if (mDownloadArgs == null) {
            return ButtonStatusManager.BUTTON_STATUS_RUNNING;
        }

        return ButtonStatusManager.getButtonStatus(mDownloadArgs);
    }

    protected void hideDownloadPanel() {
        mDownloadPanel.setVisibility(View.GONE);
    }
}
