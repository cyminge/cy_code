package com.cy.network.strategy.file;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;

import com.cy.tracer.Tracer;

public class FileUploadTask extends AsyncTask<String, Integer, String> {
	public static final String ERROR = "error";
	public static final String SUCC = "success";

	private ArrayList<IFileNetIOListener> observers = new ArrayList<IFileNetIOListener>();

	public void addFileIOListener(IFileNetIOListener listener) {
		observers.add(listener);
	}

	public void removeFileIOListener(IFileNetIOListener listener) {
		observers.remove(listener);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	private FileInputStream fileInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private InputStream inputStream = null;
	private HttpURLConnection httpURLConnection;

	private void releaseIOHandle() {
		try {
			if (fileInputStream != null)
				fileInputStream.close();
			if (dataOutputStream != null)
				dataOutputStream.close();
			if (inputStream != null)
				inputStream.close();
			if(httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		} catch (IOException e) {
			Tracer.debugException(e);
		}
	}

	@Override
	/**
	 * params[0] : local filePath
	 * params[1] : url
	 */
	protected String doInBackground(String... params) {
		if (params.length != 2) {
			Tracer.d("FileUploadTask",
					"doInBackground parameter number not match ");
			return ERROR;
		}

		String filePath = params[0];
		String uploadUrl = params[1];

		String mResult = "";
		String mResponseString = "";
		try {
			fileInputStream = new FileInputStream(filePath);
			int total = fileInputStream.available();

			Tracer.i("UploadFileTask", "doInBackground file size = " + total);

			URL url = new URL(uploadUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setDefaultUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setFixedLengthStreamingMode(total);
			httpURLConnection.setConnectTimeout(6 * 1000);
			httpURLConnection.setRequestProperty("pix", "150,80,50,36,18");
			httpURLConnection.setRequestProperty("Content-Type", "image/jpg");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Length", "" + total);
			httpURLConnection.setRequestProperty("Connection", "close");

			dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());

			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			int length = 0;
			while ((count = fileInputStream.read(buffer)) > 0) {
				dataOutputStream.write(buffer, 0, count);
				length += count;
				publishProgress((int) ((length / (float) total) * 100));
			}
			fileInputStream.close();
			dataOutputStream.flush();

			inputStream = httpURLConnection.getInputStream();

			if (200 == httpURLConnection.getResponseCode()) {
				mResult = SUCC;
			} else {
				mResult = ERROR;
			}

			InputStreamReader isr = new InputStreamReader(inputStream, "utf-8");
			BufferedReader br = new BufferedReader(isr);

			String bufString = null;
			while ((bufString = br.readLine()) != null) {
				mResponseString += bufString;
			}
			if (!mResponseString.trim().equals("")) {
				mResult = mResponseString;
			}
			// Tracer.i("UploadFileTask", "end : " + mResponseString);
			return mResult;
		} catch (Exception e) {
			Tracer.debugException(e);
			return mResult;
		} finally {
			releaseIOHandle();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		int progress = values[0];
		for (IFileNetIOListener observer : observers) {
			observer.onFileNetProgress(progress);
		}
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		for (IFileNetIOListener observer : observers) {
			observer.onFileNetTaskDone(result);
		}
		observers.clear();
		super.onPostExecute(result);
	}

	@Override
	protected void onCancelled() {
		Tracer.d("cyTest", "==== 取消上传 ！！！ ======");
		releaseIOHandle();
		observers.clear();
		super.onCancelled();
	}
}
