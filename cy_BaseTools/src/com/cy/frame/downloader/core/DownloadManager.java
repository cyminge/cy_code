package com.cy.frame.downloader.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import android.widget.Toast;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.config.DownloadConfiguration;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.controller.DownloadOrderMgr;
import com.cy.frame.downloader.download.DownloadUtils;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.download.entity.DownloadRequest;
import com.cy.frame.downloader.manager.DownloadDB;
import com.cy.frame.downloader.manager.DownloadService;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager;
import com.cy.frame.downloader.util.GameActionUtil;
import com.cy.global.BaseApplication;
import com.cy.global.WatchDog;
import com.cy.utils.Utils;
import com.cy.utils.storage.GNStorageUtils;

public class DownloadManager {
	private int mLastNewtorkType = Constant.NETWORK_NO_NET;

	private DownloadConfiguration mDownloadConfiguration;

	private static DownloadManager sSilentInstance;
	private static DownloadManager sNormalInstance;

	protected DownloadInfoMgr mDownloadInfoMgr;
	
    private ArrayList<DownloadChangeListener> mDownloadListeners = new ArrayList<DownloadChangeListener>();
    private ArrayList<StatusChangeListener> mStatusListeners = new ArrayList<StatusChangeListener>();
    private long mLastNotifyTime = 0;
    public static final int NOTIFY_PROGRESS_DELAY_TIME = 1500;
    
    public interface DownloadChangeListener {
        public void onDownloadChange();
    }

    public interface StatusChangeListener {
        public void onStatusChange();
    }
    
	public static DownloadManager getNormalInstance() {
		if (sNormalInstance == null) {
			sNormalInstance = new DownloadManager(false);
		}

		return sNormalInstance;
	}

	public static DownloadManager getSilentInstance() {
		if (sSilentInstance == null) {
			sSilentInstance = new SilentDownloadStatusMgr(true); 
		}
		return sSilentInstance;
	}

	public static DownloadManager getInstance(DownloadInfo info) {
		if (info.mIsSilentDownload) {
			return getSilentInstance();
		} else {
			return getNormalInstance();
		}
	}

	protected DownloadManager(boolean isSilent) {
		mDownloadInfoMgr = new DownloadInfoMgr(this, isSilent);
	}

	public DownloadInfoMgr getDownloadInfoMgr() {
		return mDownloadInfoMgr;
	}

	public static void init(DownloadConfiguration config) {
		getNormalInstance().mDownloadConfiguration = config;
		getSilentInstance().mDownloadConfiguration = config; // cyminge modify
	}
	
	public synchronized void clearListener() {
        mDownloadListeners.clear();
        mStatusListeners.clear();
    }

    public synchronized void addChangeListener(DownloadChangeListener listener) {
        if (listener == null || mDownloadListeners.contains(listener)) {
            return;
        }
        mDownloadListeners.add(listener);
    }

    public synchronized void removeChangeListener(DownloadChangeListener listener) {
        mDownloadListeners.remove(listener);
    }

    public synchronized void addChangeListener(StatusChangeListener listener) {
        if (listener == null || mStatusListeners.contains(listener)) {
            return;
        }
        mStatusListeners.add(listener);
    }

    public synchronized void removeChangeListener(StatusChangeListener listener) {
        mStatusListeners.remove(listener);
    }
	
	public void notifyChange() {
        notifyChange(true);
    }

    public void notifyChange(boolean statusChange) {
        mLastNotifyTime = System.currentTimeMillis();
        synchronized (DownloadManager.this) {
            for (DownloadChangeListener listener : mDownloadListeners) {
                listener.onDownloadChange();
            }
            if (statusChange) {
                for (StatusChangeListener listener : mStatusListeners) {
                    listener.onStatusChange();
                }
            }
        }
    }
    
    public void initDownloadTask() {
    	mDownloadInfoMgr.initDownloadInfo();
    }
    
    public static DownloadInfo getDownloadInfoInAll(String pkgName) {
        DownloadInfo info = getNormalInstance().mDownloadInfoMgr.getDownloadInfo(pkgName);
        if (null == info) {
            info = getSilentInstance().mDownloadInfoMgr.getDownloadInfo(pkgName);
        }
        return info;
    }
    
    private Runnable mOnProgressChangeRunnable = new Runnable() {

        @Override
        public void run() {
        	notifyChange(false);
        	mDownloadInfoMgr.syncToDB();
        }
    };
    
    public void updateProgress(DownloadInfo info) {
        if (!mDownloadInfoMgr.hasDownloadInfo(info.packageName)) {
            return;
        }
        mDownloadInfoMgr.putToMap(info);

        long delay = (NOTIFY_PROGRESS_DELAY_TIME - (System.currentTimeMillis() - mLastNotifyTime));
        delay = Math.max(0, delay);
        delay = Math.min(NOTIFY_PROGRESS_DELAY_TIME, delay);
        
        WatchDog.getLoopHandler().removeCallbacks(mOnProgressChangeRunnable);
        WatchDog.getLoopHandler().postDelayed(mOnProgressChangeRunnable, delay);
    }

    public void updateProgressNoDelay(DownloadInfo info) {
        if (!mDownloadInfoMgr.hasDownloadInfo(info.packageName)) {
            return;
        }
        mDownloadInfoMgr.putToMap(info);
        mDownloadInfoMgr.syncToDB();
        notifyChange(false);
    }

    public void updateDownloadInfo(DownloadInfo info) {
    	mDownloadInfoMgr.updateDownloadInfo(info, true);
    }
    
    public DownloadInfo getDownloadInfo(String pkgName) {
        return mDownloadInfoMgr.getDownloadInfo(pkgName);
    }
    
    public void removeDownloadInfo(String pkgName) {
    	mDownloadInfoMgr.removeDownloadInfo(pkgName);
    }
    
    public boolean hasDownloadInfo(String pkgName) {
        return mDownloadInfoMgr.hasDownloadInfo(pkgName);
    }
    
	/**
	 * 下载
	 * 
	 * @param downloadArgs
	 * @param delayTime
	 * @return
	 */
	public long download(DownloadArgs downloadArgs, int delayTime) {
		String packageName = downloadArgs.packageName;
		String localPath = null;
		if (GamesUpgradeManager.isIncreaseType(packageName)) {
			localPath = File.separator + packageName + ".patch" + Constant.TMP_FILE_EXT; // 增量升级
		} else {
			localPath = File.separator + packageName + Constant.APK + Constant.TMP_FILE_EXT;
		}

		DownloadRequest request = new DownloadRequest(downloadArgs);
		request.mAllowByMobileNet = mDownloadConfiguration.isAllowByMobileNet();
		request.mFilePath = localPath;
		return enqueue(request, delayTime);
	}

	/**
	 * 入队列下载
	 * 
	 * @param request
	 * @param delayTime
	 * @return
	 */
	protected long enqueue(DownloadRequest request, int delayTime) {
		DownloadInfo info = new DownloadInfo(request);
		long id = DownloadDB.getInstance().insert(info); // 插入数据库
		if (DownloadDB.NO_DOWN_ID == id) {
			return DownloadDB.NO_DOWN_ID;
		}

		info.mDownId = id;
		mDownloadInfoMgr.addDownloadInfo(info);
		GameActionUtil.postGameAction(info.packageName, Constant.ACTION_START_DOWNLOAD); // 下载应用，通知栏提示
		delayAddInfoForAnimation(info, delayTime);
		return id;
	}

	private void delayAddInfoForAnimation(final DownloadInfo info, int delayTime) {
		WatchDog.postDelayed(new Runnable() {
			@Override
			public void run() {
				enqueue(info);
			}

		}, delayTime);
	}

	private void enqueue(DownloadInfo info) {
		if (Utils.isMobileNet() && (!info.mAllowByMobileNet || info.mWifiAutoDownload)) { // 移动网络下不让下载，只挂起任务
			pauseDownloadTask(info, DownloadStatusConstant.TASK_PAUSE_WAIT_WIFI, false);
		} else {
			resumeDownloadTask(info, DownloadStatusConstant.REASON_START);
		}
	}
	
	public void resumeDownloadTask(DownloadArgs args, int reason) {
		if (!DownloadUtils.checkDownloadEnvironment(args)) {
			return;
		}

		// 重新下载的统计 cyminge modify
		if (reason != DownloadStatusConstant.REASON_START) { 
			// DownloadUtils.sendResumeStatis(pkgName, reason);
		}

		int targetStatus = DownloadStatusConstant.TASK_STATUS_DOWNLOADING;
		if (getDownloadingCount() >= mDownloadConfiguration.getMaxDownloadingTask()) {
			targetStatus = DownloadStatusConstant.TASK_STATUS_PENDING;
			reason = DownloadStatusConstant.REASON_PENDING;
		}

		switchSingleTaskStatus(args.packageName, targetStatus, reason);
	}
	
	public void pauseDownloadTask(DownloadArgs args, int reason) {
		boolean isDownloading = isDownloading(args.packageName);
		pauseDownloadTask(args, reason, isDownloading);
	}

	public void pauseDownloadTask(DownloadArgs args, int reason, boolean isDownloading) {
		switchSingleTaskStatus(args.packageName, DownloadStatusConstant.TASK_STATUS_PAUSED, reason);
		if (isDownloading) {
			switchPendingToDownload();
		}
	}
	
	private boolean isDownloading(String pkgName) {
		DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkgName);
		return info != null && info.isDownloading();
//		DownloadInfo info = getDownloadInfo(pkgName);
//        return info != null && !info.isCompleted();
	}
	
	/**
	 * DownloadRunnable调用，下载被终止了
	 * @param pkgName
	 * @param targetStatus
	 * @param reason
	 */
	public void onDownloadingExit(String pkgName, int targetStatus, int reason) {
		if (mDownloadInfoMgr.hasDownloadInfo(pkgName)) {
			switchSingleTaskStatus(pkgName, targetStatus, reason);
			switchPendingToDownload();
		}
	}

	/**
	 * 静默安装失败
	 * @param pkgName
	 */
	public void onSilentInstallingError(String pkgName) {
		if (mDownloadInfoMgr.hasDownloadInfo(pkgName)) {
			switchSingleTaskStatus(pkgName, DownloadStatusConstant.TASK_STATUS_FAILED, DownloadStatusConstant.TASK_FAIL_APK_ERROR);
			mDownloadInfoMgr.orderChange();
		}
	}

	/**
	 * 开始下载的时候如果检测到有静默下载任务，则取消该任务
	 * @param info
	 */
	public void onDeleteTask(DownloadInfo info) {
		int oldStatus = info.mStatus;
		info.mStatus = DownloadStatusConstant.TASK_STATUS_DELETE;
		ButtonStatusManager.removeDownloaded(info.packageName);
		mDownloadInfoMgr.removeDownloadInfo(info.packageName);
		DownloadUtils.delAllfiles(info.packageName);
		if (oldStatus == DownloadStatusConstant.TASK_STATUS_DOWNLOADING) {
			switchPendingToDownload();
		}
	}

	public void switchPendingToDownload() {
		ArrayList<String> packageList = getSortPkgList();
		int downloadingCount = getDownloadingCount();
		if (downloadingCount >= mDownloadConfiguration.getMaxDownloadingTask()) {
			return;
		}

		for (String pkg : packageList) {
			DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
			if (downloadingCount >= mDownloadConfiguration.getMaxDownloadingTask()) {
				return;
			}

			if (info.mStatus == DownloadStatusConstant.TASK_STATUS_PENDING) {
				switchSingleTaskStatus(pkg, DownloadStatusConstant.TASK_STATUS_DOWNLOADING, DownloadStatusConstant.TASK_FAIL_REASON_NONE);
				downloadingCount++;
			}
		}
	}

	protected ArrayList<String> getSortPkgList() {
		return DownloadOrderMgr.getSortPkgList();
	}

	private int getDownloadingCount() {
		int downloadingCount = 0;
		Set<Entry<String, DownloadInfo>> entrySet = mDownloadInfoMgr.getDownloadSet();
		for (Entry<String, DownloadInfo> entry : entrySet) {
			DownloadInfo info = entry.getValue();
			if (info.isDownloading()) {
				downloadingCount++;
			}
		}

		return downloadingCount;
	}

	protected void showlimitedToast(int msgId) {
		Toast.makeText(BaseApplication.getAppContext(), msgId, Toast.LENGTH_SHORT).show();
	}

	public void onNetworkChanged() {
		int networkType = Utils.getNetworkType();
		if (networkType == Constant.NETWORK_NO_NET) {
			switchAllRunningTask(DownloadStatusConstant.TASK_STATUS_PAUSED, DownloadStatusConstant.TASK_PAUSE_NO_NETWORK);
			if (DownloadOrderMgr.hasDownload()) {
				Toast.makeText(BaseApplication.getAppContext(), R.string.network_off, Toast.LENGTH_SHORT).show();
			}
		} else if (mLastNewtorkType != networkType) {
			if (Utils.isMobileNet(networkType)) {
				onMobileNetworkConnect();
			} else if (Utils.isWifiNet(networkType)) {
				onWifiNetworkConnect();
			}
		}
		mLastNewtorkType = networkType;
	}

	private void onMobileNetworkConnect() {
		int reason = DownloadStatusConstant.TASK_FAIL_REASON_NONE;
		// if (SettingUtils.getAllowByMobileNet()) { // cyminge modify
		reason = DownloadStatusConstant.TASK_PAUSE_NO_NETWORK;
		// } else {
		// reason = PAUSE_WAIT_WIFI;
		// }

		switchAllRunningTask(DownloadStatusConstant.TASK_STATUS_PAUSED, reason);
	}

	private void onWifiNetworkConnect() {
		ArrayList<String> packageList = getSortPkgList();
		for (String pkg : packageList) {
			DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
			if (info.mReason == DownloadStatusConstant.TASK_PAUSE_NO_NETWORK || info.mReason == DownloadStatusConstant.TASK_PAUSE_WAIT_WIFI) {
				resumeDownloadTask(info, DownloadStatusConstant.TASK_RESUME_NETWORK_RECONNECT);
			}
		}
	}

	public void onMediaChanged(boolean isEject) {
		if (isEject && GNStorageUtils.isSDCardChange()) {
			switchAllRunningTask(DownloadStatusConstant.TASK_STATUS_PAUSED, DownloadStatusConstant.TASK_PAUSE_DEVICE_NOT_FOUND);
			showlimitedToast(R.string.device_not_found);
		} else {
			ArrayList<String> packageList = getSortPkgList();
			for (String pkg : packageList) {
				DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
				if (shouldResumeOnMediaChanged(info)) {
					resumeDownloadTask(info, DownloadStatusConstant.TASK_RESUME_DEVICE_RECOVER);
				}
			}
		}
	}

	protected boolean shouldResumeOnMediaChanged(DownloadInfo info) {
		return info.mReason == DownloadStatusConstant.TASK_PAUSE_DEVICE_NOT_FOUND;
	}

	private void switchAllRunningTask(int status, int reason) {
		Set<Entry<String, DownloadInfo>> entrySet = mDownloadInfoMgr.getDownloadSet();
		for (Entry<String, DownloadInfo> entry : entrySet) {
			DownloadInfo info = entry.getValue();
			if (info.isRunning()) {
				String pkgName = info.packageName;
				info.mNeedUpdateDB = true;
				DownloadInfo targetInfo = switchTaskStatus(pkgName, status, reason);
				mDownloadInfoMgr.updateDownloadInfo(targetInfo, false);
			}
		}

		if (!entrySet.isEmpty()) {
			mDownloadInfoMgr.syncToDB();
		}
	}

	public void onAllowByMobileNetChanged(boolean enable) {
		ArrayList<String> packageList = getSortPkgList();
		for (String pkg : packageList) {
			DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
			if (info != null && !info.isCompleted()) {
				info.mAllowByMobileNet = enable;
				info.mNeedUpdateDB = true;
			}
		}

		if (!packageList.isEmpty()) {
			mDownloadInfoMgr.syncToDB();
		}

		if (Utils.isMobileNet() && !enable) {
			switchAllRunningTask(DownloadStatusConstant.TASK_STATUS_PAUSED, DownloadStatusConstant.TASK_PAUSE_WAIT_WIFI);
		}
	}

	protected void switchSingleTaskStatus(String pkgName, int targetStatus, int reason) {
		DownloadInfo info = switchTaskStatus(pkgName, targetStatus, reason);
		if (info != null) {
			mDownloadInfoMgr.updateDownloadInfo(info);
		}
	}

	private DownloadInfo switchTaskStatus(String pkgName, int targetStatus, int reason) {
		DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkgName);
		if (info == null || (info.mStatus == targetStatus && info.mReason == reason)) {
			return info;
		}
		if (info.mStatus == DownloadStatusConstant.TASK_STATUS_DOWNLOADING) {
			DownloadService.removeTask(info, targetStatus); // ?? 这里是个什么鬼
		}
		info.mLastStatus = info.mStatus;

		info.mStatus = targetStatus;
		info.mReason = reason;
		switch (targetStatus) {
		case DownloadStatusConstant.TASK_STATUS_DOWNLOADING:
			DownloadRunnable task = createDownloadRunnable(info);
			DownloadService.postTask(pkgName, task);
			break;
		case DownloadStatusConstant.TASK_STATUS_PAUSED:
		case DownloadStatusConstant.TASK_STATUS_FAILED:
			showToastByReason(reason);
			break;
		case DownloadStatusConstant.TASK_STATUS_SUCCESSFUL:
			info.setCompleteTime(System.currentTimeMillis());
			break;
		default:
			break;
		}
		return info;
	}

	protected void showToastByReason(int reason) {
		DownloadUtils.toast(reason);
	}

	protected DownloadRunnable createDownloadRunnable(DownloadInfo info) {
		return new DownloadRunnable(info);
	}
}
