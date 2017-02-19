package com.cy.frame.downloader.download.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

import com.cy.constant.Constant;
import com.cy.frame.downloader.core.DownloadStatusMgr;
import com.cy.frame.downloader.upgrade.GamesUpgradeManager;

/**
 * @author JLB6088
 * 
 */
public final class DownloadInfo extends DownloadRequest implements Cloneable {
	private static final SimpleDateFormat MIN_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);
	private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

	private static final int MIN_APK_SIZE = 10 * 1024;

	public long mDownId = -1; // 下载ID
	public int mStatus = DownloadStatusMgr.TASK_STATUS_PENDING; // 下载状态
	public int mReason = DownloadStatusMgr.TASK_FAIL_REASON_NONE;
	private long mCompleteTime = 0;
	private String mCompleteByMinute = Constant.EMPTY;
	private String mCompleteByDay = Constant.EMPTY;
	public long mProgress = 0;
	public long mTotalSize = -1; // 当前下载的字节数
	public boolean mNeedUpdateDB = false;
	public long mStartTime = 0;
	public String mRawDownloadUrl = Constant.EMPTY;
	public int mLastStatus = DownloadStatusMgr.TASK_STATUS_DOWNLOADING;
	public long mInitTime = 0;

	public DownloadInfo() {
	}

	public DownloadInfo(long downId, long progress, long total, int status, int reason, String pkgName) {
		mDownId = downId;
		mProgress = progress;
		mTotalSize = total;
		mStatus = status;
		mReason = reason;
		packageName = pkgName;

		if (mStatus == DownloadStatusMgr.TASK_STATUS_SUCCESSFUL && total < MIN_APK_SIZE) {
			mStatus = DownloadStatusMgr.TASK_STATUS_PAUSED;
			mReason = DownloadStatusMgr.TASK_PAUSE_WIFI_INVALID;
		}
		mLastStatus = mStatus;
	}

	public DownloadInfo(DownloadRequest request) {
		mFilePath = request.mFilePath;
		boolean increaseType = GamesUpgradeManager.isIncreaseType(request.packageName);
		if (increaseType) {
			size = GamesUpgradeManager.getOneAppInfo(request.packageName).mPatchSize;
		} else {
			size = request.size;
		}

		if (increaseType) {
			downUrl = GamesUpgradeManager.getOneAppInfo(request.packageName).mPatchUrl;
			mRawDownloadUrl = request.downUrl;
		} else {
			downUrl = request.downUrl;
			mRawDownloadUrl = request.downUrl;
		}
		gameId = request.gameId;
		gameid = request.gameid;
		name = request.name;
		packageName = request.packageName;
		mSource = request.mSource;
		mAllowByMobileNet = request.mAllowByMobileNet;
		mReserveJson = request.mReserveJson;
		mStartTime = System.currentTimeMillis();
		mInitTime = mStartTime;
		mIsSilentDownload = request.mIsSilentDownload;
		mVersionCode = request.mVersionCode;
		mWifiAutoDownload = request.mWifiAutoDownload;
	}

	public boolean isCompleted() {
		return mStatus == DownloadStatusMgr.TASK_STATUS_SUCCESSFUL;
	}

	public boolean isRunning() {
		return mStatus == DownloadStatusMgr.TASK_STATUS_DOWNLOADING
				|| mStatus == DownloadStatusMgr.TASK_STATUS_PENDING;
	}

	public boolean isDownloading() {
		return mStatus == DownloadStatusMgr.TASK_STATUS_DOWNLOADING;
	}

	public boolean isPause() {
		return mStatus == DownloadStatusMgr.TASK_STATUS_PAUSED;
	}

	public boolean isFailed() {
		return mStatus == DownloadStatusMgr.TASK_STATUS_FAILED;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeLong(mDownId);
		dest.writeInt(mStatus);
		dest.writeInt(mReason);
		dest.writeLong(getCompleteTime());
		dest.writeLong(mProgress);
		dest.writeLong(mTotalSize);
		dest.writeLong(mStartTime);
		dest.writeLong(mInitTime);
	}

	public static final Parcelable.Creator<DownloadInfo> CREATOR = new Parcelable.Creator<DownloadInfo>() {

		@Override
		public DownloadInfo createFromParcel(Parcel in) {
			return new DownloadInfo(in);
		}

		@Override
		public DownloadInfo[] newArray(int size) {
			return new DownloadInfo[size];
		}
	};

	private DownloadInfo(Parcel in) {
		super(in);
		mDownId = in.readLong();
		mStatus = in.readInt();
		mReason = in.readInt();
		setCompleteTime(in.readLong());
		mProgress = in.readLong();
		mTotalSize = in.readLong();
		mStartTime = in.readLong();
		mInitTime = in.readLong();
		mLastStatus = mStatus;
	}

	public void setCompleteTime(long completeTime) {
		mCompleteTime = completeTime;
		Date date = new Date(completeTime);
		synchronized (MIN_FORMAT) {
			mCompleteByMinute = MIN_FORMAT.format(date);
			mCompleteByDay = DAY_FORMAT.format(date);
		}
	}

	public long getCompleteTime() {
		return mCompleteTime;
	}

	public String getCompleteStr() {
		Date date = new Date();
		synchronized (MIN_FORMAT) {
			String curDay = DAY_FORMAT.format(date);
			if (curDay.equals(mCompleteByDay)) {
				return mCompleteByMinute;
			} else {
				return mCompleteByDay;
			}
		}
	}

	public boolean isNewDownload() {
		return mTotalSize == -1;
	}
}
