package com.cy.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

import com.cy.tracer.export.IOnMaxLogFrame;
import com.cy.utils.storage.StorageUtils;

public class OnMaxLogFrame implements IOnMaxLogFrame {
	private static final String THIS_FILE = "OnMaxLogFrame";

	private static final String LOG_FILE = "log.txt";
	private static final String LOG_FILE_EXTERN = "ptt_log.txt";

	private static final int LOG_FILE_SIZE_MAX = 10 * 1024 * 1024;
	private static final int LOG_FILE_EXTERN_SIZE_MAX = 50 * 1024 * 1024;

	private Context mContext;

	public OnMaxLogFrame(Context context) {
		this.mContext = context;
	}

	@Override
	@SuppressWarnings("deprecation")
	/**
	 * 将log保存到本地文件，不保存到t卡，直接保存到软件目录
	 */
	public int saveLogToFile(final String jsonString) {
//		String logPath = FileManager.getLogPath();
		FileOutputStream fos = null;
//		FileWriter writer = null;
		try {
			fos = mContext.openFileOutput(LOG_FILE, Context.MODE_APPEND | Context.MODE_WORLD_READABLE);
			if (fos.getChannel().size() > LOG_FILE_SIZE_MAX) {
				fos.close();
				bakLogFile();
				mContext.deleteFile(LOG_FILE);
				fos = mContext.openFileOutput(LOG_FILE, Context.MODE_APPEND | Context.MODE_WORLD_READABLE);
			}
			fos.write(jsonString.getBytes());
			fos.flush();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return 1;
	}

	private void bakLogFile() {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			File dir = Environment.getExternalStorageDirectory();
			String filePath = dir.getAbsolutePath() + "/" + LOG_FILE_EXTERN;
			boolean isAppend = true;
			File fileBak = new File(filePath);
			if (fileBak.exists()) {
				if (fileBak.length() > LOG_FILE_EXTERN_SIZE_MAX) {
					isAppend = false; // 重写
				}
			} else {
				fileBak.createNewFile();
			}
//			File file = mContext.getFileStreamPath(LOG_FILE);
//			if (file.renameTo(fileBak)) {
//				Log.d(THIS_FILE, "back up log file done!");
//			} else {
//				Log.d(THIS_FILE, "back up log file failed!");
//			}
			fis = mContext.openFileInput(LOG_FILE);
			fos = new FileOutputStream(fileBak, isAppend);

			byte[] buffer = new byte[4 * 1024];
			int byteread = 0;
			int bytesum = 0;
			while ((byteread = fis.read(buffer)) != -1) {
				bytesum += byteread; // 字节数 文件大小   
				fos.write(buffer, 0, byteread);
			}
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getLogPath() {
		return StorageUtils.getCacheDirectory(mContext).getPath();
	}

	@Override
	public int reportToServer(final String reportType, final String logName, final String logType, final String jsonString) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				reportPost(reportType, logName, logType, jsonString);
			}
		}).start();

		return 0;
	}

	@Override
	public int reportToServer(final String jsonString) {
		return reportToServer("auto", "log" + System.currentTimeMillis(), "", jsonString);
	}

	/**
	 * 上传实现
	 * 
	 * @param jsonString
	 */
	private void reportGet(final String logType, final String jsonString) {
//		URL url = null;
//
//		try {
//			final StringBuilder urlString = new StringBuilder(HostProperties.getHost(HostProperties.REPORT_HOST));
//
//			String userInfo = "&userinfo=" + SipLoginAccountInfo.getUin() + "|" + SipLoginAccountInfo.getUserName()
//					+ "|" + SipLoginAccountInfo.getUserAvatar() + "|" + SipLoginAccountInfo.getPhone()
//					+ "|" + SipLoginAccountInfo.getDefaultGid() + "|";
//			String requestString = "type=app_log" + userInfo + "&content=" + jsonString;
//
//			Log.d(THIS_FILE, "reportToServer" + requestString);
//
//			urlString.append("?c=" + Base64.encodeToString(requestString.getBytes(), Base64.NO_PADDING | Base64.NO_WRAP));
//
//			url = new URL(urlString.toString());
//			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//			httpURLConnection.setDoInput(true);
//			httpURLConnection.setUseCaches(false);
//			httpURLConnection.setRequestMethod("GET");
//			httpURLConnection.setConnectTimeout(12 * 1000);
//
//			int reportResult = httpURLConnection.getResponseCode();
//
//			boolean success = false;
//			if (200 == reportResult) {
//				success = true;
//				Log.d(THIS_FILE, "report done");
//			} else {
//				Log.d(THIS_FILE, "report failed " + reportResult);
//			}
//
//			httpURLConnection.disconnect();
//
//			EventBus.getDefault().post(new ReportDoneEvent(success, reportResult));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private void reportPost(final String reportType, final String logName, final String logType, final String jsonString) {
//		URL url = null;
//
//		try {
//			final String urlString = HostProperties.getHost(HostProperties.REPORT_HOST);
//
//			String userInfo = "uin=" + SipLoginAccountInfo.getUin() + "&userName=" + URLEncoder.encode(SipLoginAccountInfo.getUserName(), "UTF-8")
//					+ "&phone=" + SipLoginAccountInfo.getPhone() + "&package=" + mContext.getPackageName()
//					+ "&version=" + GlobalDefine.getClientVersion(mContext) + "&sdk_ver=" + Build.VERSION.SDK_INT
//					+ "&report=" + reportType + "&log_type=" + logType + "&log_name=" + logName;
//			String requestString = userInfo + "&content=" + URLEncoder.encode(jsonString, "UTF-8");
//
//			Log.d(THIS_FILE, "reportToServer:" + requestString);
////			urlString.append("?" + Base64.encodeToString(requestString.getBytes(), Base64.NO_PADDING | Base64.NO_WRAP));
//
//			url = new URL(urlString.toString() + "?t=" + System.currentTimeMillis());
//			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//			httpURLConnection.setDoOutput(true);
//			httpURLConnection.setDoInput(true);
//			httpURLConnection.setUseCaches(false);
//			httpURLConnection.setRequestMethod("POST");
//			httpURLConnection.setConnectTimeout(12 * 1000);
//
//			httpURLConnection.connect();
//			DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
////			out.writeBytes(Base64.encodeToString(requestString.getBytes(), Base64.NO_PADDING | Base64.NO_WRAP));
//			out.writeBytes(requestString);
//			out.flush();
//			out.close(); // flush and close
////			InputStream input = httpURLConnection.getInputStream();
////			byte[] buf = new byte[10 * 1024];
////			int len = input.read(buf);
////			String deg = buf.toString();
//
//			int reportResult = httpURLConnection.getResponseCode();
//
//			boolean success = false;
//			if (200 == reportResult) {
//				success = true;
//				Log.d(THIS_FILE, "report done");
//			} else {
//				Log.d(THIS_FILE, "report failed " + reportResult);
//			}
//
//			httpURLConnection.disconnect();
//
//			EventBus.getDefault().post(new ReportDoneEvent(success, reportResult));
//
//			return;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		EventBus.getDefault().post(new ReportDoneEvent(false, -1)); // 出错
	}

}
