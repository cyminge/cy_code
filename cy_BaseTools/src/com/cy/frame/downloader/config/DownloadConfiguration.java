package com.cy.frame.downloader.config;

import java.io.File;

import com.cy.utils.storage.GNStorageUtils;

/**
 * 下载配置项
 * @author zf
 *
 */
public final class DownloadConfiguration {

	public static final int MAX_DOWNLOADING_TASK = 2; // 最大正在下载任务数
	
	private final String mHomePath;
	private final boolean mIsAllowByMobileNet;
	private final int mMaxDownloadingTask;
	
	public String getHomePath() {
		return mHomePath;
	}
	
	public boolean isAllowByMobileNet() {
		return mIsAllowByMobileNet;
	}
	
	public int getMaxDownloadingTask() {
		return mMaxDownloadingTask;
	}
	
	private DownloadConfiguration(final Builder builder) {
		mHomePath = builder.mHomePath;
		mIsAllowByMobileNet = builder.mIsAllowByMobileNet;
		mMaxDownloadingTask = builder.mMaxDownloadingTask;
	}
	
	public static class Builder {
		private String mHomePath = GNStorageUtils.getHomeDirAbsolute()+File.separator;
		private boolean mIsAllowByMobileNet = true;
		private int mMaxDownloadingTask = MAX_DOWNLOADING_TASK;
		
		public Builder appFilePath(String filePath) {
			mHomePath = filePath;
			return this;
		}
		
		public Builder isAllowByMobileNet(boolean allow) {
			mIsAllowByMobileNet = allow;
			return this;
		}
		
		public Builder maxDownloadingTask(int maxTaskCounts) {
			mMaxDownloadingTask = maxTaskCounts;
			return this;
		}
		
		public DownloadConfiguration build() {
			return new DownloadConfiguration(this);
		}
		
	}
	
}
