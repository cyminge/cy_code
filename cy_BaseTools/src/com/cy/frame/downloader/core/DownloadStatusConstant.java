package com.cy.frame.downloader.core;


public class DownloadStatusConstant {

	public static final int TASK_STATUS_PENDING = 0x100; // 等待中
	public static final int TASK_STATUS_DOWNLOADING = 0x101; // 下载中
	public static final int TASK_STATUS_PAUSED = 0x102; // 已暂停
	public static final int TASK_STATUS_SUCCESSFUL = 0x103; // 下载完成
	public static final int TASK_STATUS_FAILED = 0x104; // 下载失败
	public static final int TASK_STATUS_DELETE = 0x105; // 删除

	public static final int TASK_FAIL_REASON_NONE = -1;
	// fail reason
	public static final int TASK_FAIL_UNKNOWN = 0x300; // 未知
	public static final int TASK_FAIL_HTTP_DATA_ERROR = 0x301; // Http 数据异常
	public static final int TASK_FAIL_URL_ERROR = 0x302; // 下载地址有误
	public static final int TASK_FAIL_APK_ERROR = 0x303; // 文件有误
	public static final int TASK_FAIL_URL_UNREACHABLE = 0x304; // 下载地址不可达
	public static final int TASK_FAIL_URL_UNRECOVERABLE = 0x305; // 下载地址不可恢复
	public static final int TASK_FAIL_CONTENT_TYPE_ERROR = 0x306; // 文件类型有误
	public static final int TASK_FAIL_GAME_NOT_EXIST = 0x308; // 应用不存在
	public static final int TASK_FAIL_CANNOT_RESUME = 0x309; // 任务不能继续

	// pause reason
	public static final int TASK_PAUSE_NO_NETWORK = 0x509;
	public static final int TASK_PAUSE_WAIT_WIFI = 0x510;
	public static final int TASK_PAUSE_BY_USER = 0x511;
	public static final int TASK_PAUSE_FILE_ERROR = 0x512;
	public static final int TASK_PAUSE_INSUFFICIENT_SPACE = 0x513;
	public static final int TASK_PAUSE_DEVICE_NOT_FOUND = 0x514;
	public static final int TASK_PAUSE_WIFI_INVALID = 0x515;
	public static final int TASK_PAUSE_XUNLEI_WAITING_TO_RETRY = 0x517;

	// resume reason
	public static final int TASK_RESUME_NETWORK_RECONNECT = 0x600;
	public static final int TASK_RESUME_BY_USER = 0x601;
	public static final int TASK_RESUME_DEVICE_RECOVER = 0x602;

	public static final int REASON_START = 0x700;
	public static final int USER_DELETED = 0x701;
	public static final int REASON_PENDING = 0x703;

	public static final int MAX_DOWNLOAD_TASK = 100; // 最大下载任务数
//	private int mLastNewtorkType = Constant.NETWORK_NO_NET;
//
//	private DownloadConfiguration mDownloadConfiguration;
//
//	private static DownloadStatusMgr sSilentInstance;
//	private static DownloadStatusMgr sNormalInstance;
//
//	protected DownloadInfoMgr mDownloadInfoMgr;
//
//	public static DownloadStatusMgr getNormalInstance() {
//		if (sNormalInstance == null) {
//			sNormalInstance = new DownloadStatusMgr(new DownloadInfoMgr(false));
//		}
//
//		return sNormalInstance;
//	}
//
//	public static DownloadStatusMgr getSilentInstance() {
////		if (sSilentInstance == null) {
////			sSilentInstance = new SilentDownloadStatusMgr(new DownloadInfoMgr(true)); // cyminge modify
////		}
//		return sSilentInstance;
//	}
//
//	public static DownloadStatusMgr getInstance(DownloadInfo info) {
//		if (info.mIsSilentDownload) {
//			return getSilentInstance();
//		} else {
//			return getNormalInstance();
//		}
//	}
//
//	protected DownloadStatusMgr(DownloadInfoMgr downloadInfoMgr) {
//		mDownloadInfoMgr = downloadInfoMgr;
//	}
//
//	public DownloadInfoMgr getDownloadInfoMgr() {
//		return mDownloadInfoMgr;
//	}
//
//	public static void init(DownloadConfiguration config) {
//		getNormalInstance().mDownloadConfiguration = config;
//		getSilentInstance().mDownloadConfiguration = config; // cyminge modify
//	}
//
//	/**
//	 * 下载
//	 * 
//	 * @param downloadArgs
//	 * @param delayTime
//	 * @return
//	 */
//	public long download(DownloadArgs downloadArgs, int delayTime) {
//		String packageName = downloadArgs.packageName;
//		String localPath = null;
//		if (GamesUpgradeManager.isIncreaseType(packageName)) {
//			localPath = File.separator + packageName + ".patch" + Constant.TMP_FILE_EXT; // 增量升级
//		} else {
//			localPath = File.separator + packageName + Constant.APK + Constant.TMP_FILE_EXT;
//		}
//
//		DownloadRequest request = new DownloadRequest(downloadArgs);
//		request.mAllowByMobileNet = mDownloadConfiguration.isAllowByMobileNet();
//		request.mFilePath = localPath;
//		return enqueue(request, delayTime);
//	}
//
//	/**
//	 * 入队列下载
//	 * 
//	 * @param request
//	 * @param delayTime
//	 * @return
//	 */
//	protected long enqueue(DownloadRequest request, int delayTime) {
//		DownloadInfo info = new DownloadInfo(request);
//		long id = DownloadDB.getInstance().insert(info); // 插入数据库
//		if (DownloadDB.NO_DOWN_ID == id) {
//			return DownloadDB.NO_DOWN_ID;
//		}
//
//		info.mDownId = id;
//		mDownloadInfoMgr.addDownloadInfo(info);
//		GameActionUtil.postGameAction(info.packageName, Constant.ACTION_START_DOWNLOAD); // 下载应用，通知栏提示
//		delayAddInfoForAnimation(info, delayTime);
//		return id;
//	}
//
//	private void delayAddInfoForAnimation(final DownloadInfo info, int delayTime) {
//		WatchDog.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				enqueue(info);
//			}
//
//		}, delayTime);
//	}
//
//	private void enqueue(DownloadInfo info) {
//		if (Utils.isMobileNet() && (!info.mAllowByMobileNet || info.mWifiAutoDownload)) { // 移动网络下不让下载，只挂起任务
//			pauseDownloadTask(info, TASK_PAUSE_WAIT_WIFI, false);
//		} else {
//			resumeDownloadTask(info, REASON_START);
//		}
//	}
//	
//	public void resumeDownloadTask(DownloadArgs args, int reason) {
//		if (!DownloadUtils.checkDownloadEnvironment(args)) {
//			return;
//		}
//
//		// 重新下载的统计 cyminge modify
//		if (reason != REASON_START) { 
//			// DownloadUtils.sendResumeStatis(pkgName, reason);
//		}
//
//		int targetStatus = TASK_STATUS_DOWNLOADING;
//		if (getDownloadingCount() >= mDownloadConfiguration.getMaxDownloadingTask()) {
//			targetStatus = TASK_STATUS_PENDING;
//			reason = REASON_PENDING;
//		}
//
//		switchSingleTaskStatus(args.packageName, targetStatus, reason);
//	}
//	
//	public void pauseDownloadTask(DownloadArgs args, int reason) {
//		boolean isDownloading = isDownloading(args.packageName);
//		pauseDownloadTask(args, reason, isDownloading);
//	}
//
//	public void pauseDownloadTask(DownloadArgs args, int reason, boolean isDownloading) {
//		switchSingleTaskStatus(args.packageName, TASK_STATUS_PAUSED, reason);
//		if (isDownloading) {
//			switchPendingToDownload();
//		}
//	}
//	
//	private boolean isDownloading(String pkgName) {
//		DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkgName);
//		return info != null && info.isDownloading();
////		DownloadInfo info = getDownloadInfo(pkgName);
////        return info != null && !info.isCompleted();
//	}
//	
//	/**
//	 * DownloadRunnable调用，下载被终止了
//	 * @param pkgName
//	 * @param targetStatus
//	 * @param reason
//	 */
//	public void onDownloadingExit(String pkgName, int targetStatus, int reason) {
//		if (mDownloadInfoMgr.hasDownloadInfo(pkgName)) {
//			switchSingleTaskStatus(pkgName, targetStatus, reason);
//			switchPendingToDownload();
//		}
//	}
//
//	/**
//	 * 静默安装失败
//	 * @param pkgName
//	 */
//	public void onSilentInstallingError(String pkgName) {
//		if (mDownloadInfoMgr.hasDownloadInfo(pkgName)) {
//			switchSingleTaskStatus(pkgName, TASK_STATUS_FAILED, TASK_FAIL_APK_ERROR);
//			mDownloadInfoMgr.orderChange();
//		}
//	}
//
//	/**
//	 * 开始下载的时候如果检测到有静默下载任务，则取消该任务
//	 * @param info
//	 */
//	public void onDeleteTask(DownloadInfo info) {
//		int oldStatus = info.mStatus;
//		info.mStatus = TASK_STATUS_DELETE;
//		ButtonStatusManager.removeDownloaded(info.packageName);
//		mDownloadInfoMgr.removeDownloadInfo(info.packageName);
//		Utils.delAllfiles(info.packageName);
//		if (oldStatus == DownloadStatusMgr.TASK_STATUS_DOWNLOADING) {
//			switchPendingToDownload();
//		}
//	}
//
//	public void switchPendingToDownload() {
//		ArrayList<String> packageList = getSortPkgList();
//		int downloadingCount = getDownloadingCount();
//		if (downloadingCount >= mDownloadConfiguration.getMaxDownloadingTask()) {
//			return;
//		}
//
//		for (String pkg : packageList) {
//			DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
//			if (downloadingCount >= mDownloadConfiguration.getMaxDownloadingTask()) {
//				return;
//			}
//
//			if (info.mStatus == TASK_STATUS_PENDING) {
//				switchSingleTaskStatus(pkg, TASK_STATUS_DOWNLOADING, TASK_FAIL_REASON_NONE);
//				downloadingCount++;
//			}
//		}
//	}
//
//	protected ArrayList<String> getSortPkgList() {
//		return DownloadOrderMgr.getSortPkgList();
//	}
//
//	private int getDownloadingCount() {
//		int downloadingCount = 0;
//		Set<Entry<String, DownloadInfo>> entrySet = mDownloadInfoMgr.getDownloadSet();
//		for (Entry<String, DownloadInfo> entry : entrySet) {
//			DownloadInfo info = entry.getValue();
//			if (info.isDownloading()) {
//				downloadingCount++;
//			}
//		}
//
//		return downloadingCount;
//	}
//
//	protected void showlimitedToast(int msgId) {
//		Toast.makeText(BaseApplication.getAppContext(), msgId, Toast.LENGTH_SHORT).show();
//	}
//
//	public void onNetworkChanged() {
//		int networkType = Utils.getNetworkType();
//		if (networkType == Constant.NETWORK_NO_NET) {
//			switchAllRunningTask(TASK_STATUS_PAUSED, TASK_PAUSE_NO_NETWORK);
//			if (DownloadOrderMgr.hasDownload()) {
//				Toast.makeText(BaseApplication.getAppContext(), R.string.network_off, Toast.LENGTH_SHORT).show();
//			}
//		} else if (mLastNewtorkType != networkType) {
//			if (Utils.isMobileNet(networkType)) {
//				onMobileNetworkConnect();
//			} else if (Utils.isWifiNet(networkType)) {
//				onWifiNetworkConnect();
//			}
//		}
//		mLastNewtorkType = networkType;
//	}
//
//	private void onMobileNetworkConnect() {
//		int reason = TASK_FAIL_REASON_NONE;
//		// if (SettingUtils.getAllowByMobileNet()) { // cyminge modify
//		reason = TASK_PAUSE_NO_NETWORK;
//		// } else {
//		// reason = PAUSE_WAIT_WIFI;
//		// }
//
//		switchAllRunningTask(TASK_STATUS_PAUSED, reason);
//	}
//
//	private void onWifiNetworkConnect() {
//		ArrayList<String> packageList = getSortPkgList();
//		for (String pkg : packageList) {
//			DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
//			if (info.mReason == TASK_PAUSE_NO_NETWORK || info.mReason == TASK_PAUSE_WAIT_WIFI) {
//				resumeDownloadTask(info, TASK_RESUME_NETWORK_RECONNECT);
//			}
//		}
//	}
//
//	public void onMediaChanged(boolean isEject) {
//		if (isEject && GNStorageUtils.isSDCardChange()) {
//			switchAllRunningTask(TASK_STATUS_PAUSED, TASK_PAUSE_DEVICE_NOT_FOUND);
//			showlimitedToast(R.string.device_not_found);
//		} else {
//			ArrayList<String> packageList = getSortPkgList();
//			for (String pkg : packageList) {
//				DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
//				if (shouldResumeOnMediaChanged(info)) {
//					resumeDownloadTask(info, TASK_RESUME_DEVICE_RECOVER);
//				}
//			}
//		}
//	}
//
//	protected boolean shouldResumeOnMediaChanged(DownloadInfo info) {
//		return info.mReason == TASK_PAUSE_DEVICE_NOT_FOUND;
//	}
//
//	private void switchAllRunningTask(int status, int reason) {
//		Set<Entry<String, DownloadInfo>> entrySet = mDownloadInfoMgr.getDownloadSet();
//		for (Entry<String, DownloadInfo> entry : entrySet) {
//			DownloadInfo info = entry.getValue();
//			if (info.isRunning()) {
//				String pkgName = info.packageName;
//				info.mNeedUpdateDB = true;
//				DownloadInfo targetInfo = switchTaskStatus(pkgName, status, reason);
//				mDownloadInfoMgr.updateDownloadInfo(targetInfo, false);
//			}
//		}
//
//		if (!entrySet.isEmpty()) {
//			mDownloadInfoMgr.syncToDB();
//		}
//	}
//
//	public void onAllowByMobileNetChanged(boolean enable) {
//		ArrayList<String> packageList = getSortPkgList();
//		for (String pkg : packageList) {
//			DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkg);
//			if (info != null && !info.isCompleted()) {
//				info.mAllowByMobileNet = enable;
//				info.mNeedUpdateDB = true;
//			}
//		}
//
//		if (!packageList.isEmpty()) {
//			mDownloadInfoMgr.syncToDB();
//		}
//
//		if (Utils.isMobileNet() && !enable) {
//			switchAllRunningTask(TASK_STATUS_PAUSED, TASK_PAUSE_WAIT_WIFI);
//		}
//	}
//
//	protected void switchSingleTaskStatus(String pkgName, int targetStatus, int reason) {
//		DownloadInfo info = switchTaskStatus(pkgName, targetStatus, reason);
//		if (info != null) {
//			mDownloadInfoMgr.updateDownloadInfo(info);
//		}
//	}
//
//	private DownloadInfo switchTaskStatus(String pkgName, int targetStatus, int reason) {
//		DownloadInfo info = mDownloadInfoMgr.getDownloadInfo(pkgName);
//		if (info == null || (info.mStatus == targetStatus && info.mReason == reason)) {
//			return info;
//		}
//		if (info.mStatus == TASK_STATUS_DOWNLOADING) {
//			DownloadService.removeTask(info, targetStatus); // ?? 这里是个什么鬼
//		}
//		info.mLastStatus = info.mStatus;
//
//		info.mStatus = targetStatus;
//		info.mReason = reason;
//		switch (targetStatus) {
//		case TASK_STATUS_DOWNLOADING:
//			DownloadRunnable task = createDownloadRunnable(info);
//			DownloadService.postTask(pkgName, task);
//			break;
//		case TASK_STATUS_PAUSED:
//		case TASK_STATUS_FAILED:
//			showToastByReason(reason);
//			break;
//		case TASK_STATUS_SUCCESSFUL:
//			info.setCompleteTime(System.currentTimeMillis());
//			break;
//		default:
//			break;
//		}
//		return info;
//	}
//
//	protected void showToastByReason(int reason) {
//		DownloadUtils.toast(reason);
//	}
//
//	protected DownloadRunnable createDownloadRunnable(DownloadInfo info) {
//		return new DownloadRunnable(info);
//	}
}
