package com.example.activitytest;

import java.io.File;
import java.util.UUID;

import android.media.MediaRecorder;

public class AudioManager {

	private MediaRecorder mMediaRecorder;
	private String mStoreDir;
	private String mCurrentFilePath;

	private static AudioManager mAudioInstance; // 单例

	public boolean mIsPrepared = false;

	private AudioManager(String dir) {
		this.mStoreDir = dir;
	}

	public interface AudioStateChangeListener {
		void wellPrepared();
	}

	public AudioStateChangeListener mAudioStateChangeListener;

	public void setOnAudioStateChangeListener(AudioStateChangeListener listener) {
		mAudioStateChangeListener = listener;
	}

	public static AudioManager getInstance(String dir) {
		if (mAudioInstance == null) {
			synchronized (AudioManager.class) {
				if (mAudioInstance == null) {
					mAudioInstance = new AudioManager(dir);
				}
			}
		}
		return mAudioInstance;
	}

	/**
	 * 准备录音
	 */
	public void prepareAudio() {
		try {
			mIsPrepared = false;
			File fileDir = new File(mStoreDir);
			if (!fileDir.exists())
				fileDir.mkdirs();
			String fileName = generateFileName();
			File file = new File(fileDir, fileName);

			mCurrentFilePath = file.getAbsolutePath();
			mMediaRecorder = new MediaRecorder();
			// 设置输出文件
			mMediaRecorder.setOutputFile(file.getAbsolutePath());
			// 设置音频源
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置音频格式
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			// 设置音频编码
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			mMediaRecorder.prepare();
			mMediaRecorder.start();
			// 准备结束
			mIsPrepared = true;
			//
			if (mAudioStateChangeListener != null) {
				mAudioStateChangeListener.wellPrepared();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 随机生成文件名称
	 * 
	 * @return
	 */
	private String generateFileName() {
		return UUID.randomUUID().toString() + ".amr";
	}

	public int getVoiceLevel(int maxLevel) {
		if (mIsPrepared) {
			try {
				// 振幅范围mediaRecorder.getMaxAmplitude():1-32767
				return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
			} catch (Exception e) {
			}
		}
		return 1;
	}

	public void release() {
		mMediaRecorder.stop();
		mMediaRecorder.release();
		mMediaRecorder = null;

	}

	public void cancel() {
		release();
		if (mCurrentFilePath != null) {
			File file = new File(mCurrentFilePath);
			file.delete();
			mCurrentFilePath = null;
		}
	}

	public String getCurrentPath() {
		return mCurrentFilePath;
	}
}
