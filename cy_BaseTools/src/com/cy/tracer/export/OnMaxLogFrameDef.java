package com.cy.tracer.export;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Base64;
import android.util.Log;

import com.cy.tracer.LogToFile;
import com.cy.tracer.ReportDoneEvent;

import de.greenrobot.event.EventBus;

public class OnMaxLogFrameDef implements IOnMaxLogFrame {
	private static final String THIS_FILE = "OnMaxLogFrameDef";

	@Override
	/**
	 * 将log保存到本地文件
	 */
	public int saveLogToFile(final String jsonString) {
		String logPath = LogToFile.getDefLogPath();
		int result = -1;
		if (logPath != null) {
			FileWriter fos = null;
			try {
				fos = new FileWriter(logPath, true);
				fos.append(jsonString);
				fos.flush();
				result = 0;
				Log.d(THIS_FILE, "flushTrace done");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null)
						fos.close();
					Log.d(THIS_FILE, "flushTrace close");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public String getLogPath() {
		return LogToFile.getDefLogPath();
	}

	@Override
	public int reportToServer(final String jsonString) {
		return reportToServer("auto", "log" + System.currentTimeMillis(), "", jsonString);
	}

	@Override
	public int reportToServer(final String reportType, final String logName, final String logType, final String jsonString) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				URL url = null;

				try {
					final StringBuilder urlString = new StringBuilder("http://webs.veclinknet.com:80/cgi-bin/accordServer");

					String userInfo = "&userinfo=" + "|"
							+ "|" + "|"
							+ "|" + "|";
					String requestString = "type=app_log" + userInfo + "&content=" + jsonString;

					Log.d(THIS_FILE, "reportToServer" + requestString);

					urlString.append("?c=" + Base64.encodeToString(requestString.getBytes(), Base64.NO_PADDING | Base64.NO_WRAP));

					url = new URL(urlString.toString());
					HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
					httpURLConnection.setDoInput(true);
					httpURLConnection.setUseCaches(false);
					httpURLConnection.setRequestMethod("GET");
					httpURLConnection.setConnectTimeout(12 * 1000);

					int reportResult = httpURLConnection.getResponseCode();

					boolean success = false;
					if (200 == reportResult) {
						success = true;
						Log.d(THIS_FILE, "report done");
					} else {
						Log.d(THIS_FILE, "report failed " + reportResult);
					}

					httpURLConnection.disconnect();

					EventBus.getDefault().post(new ReportDoneEvent(success, reportResult));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		return 0;
	}
}
