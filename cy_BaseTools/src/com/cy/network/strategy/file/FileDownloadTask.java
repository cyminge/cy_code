package com.cy.network.strategy.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;

import com.cy.tracer.Tracer;

public class FileDownloadTask extends AsyncTask<String, Integer, Integer> {
	private ArrayList<IFileNetIOListener> observers = new ArrayList<IFileNetIOListener>();
	
	public void addFileIOListener(IFileNetIOListener listener) {
		observers.add(listener);
	}
	public void removeFileIOListener(IFileNetIOListener listener) {
		observers.remove(listener);
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		for (IFileNetIOListener observer : observers) {
			observer.onFileNetTaskDone("" + result);
		}
		observers.clear();
		super.onPostExecute(result);
	}
	
	@Override  
    protected void onPreExecute() {  
        super.onPreExecute();  
    }  

	
	@Override
	protected void onCancelled() {
		observers.clear();
		super.onCancelled();
	}

	@Override
	protected Integer doInBackground(String... params) {
		if (params.length != 2) {
			Tracer.d("FileDownloadTask", "doInBackground parameter number not match ");
			return -1;
		}
		String urlString = params[0];
		String destPath = params[1];
		Tracer.i("FileDownloadTask", "doInBackground url is : " + urlString);
		
		HttpURLConnection conn = null;
		FileOutputStream fos = null;
		InputStream in = null;
		int result = -1;
		
		try {  
            URL url = new URL(urlString);  
            conn = (HttpURLConnection) url.openConnection();  
//            conn.setRequestMethod("GET");  
//            conn.setDoOutput(true);  
            conn.setDoInput(true);
            conn.connect();
            
            if((result = conn.getResponseCode()) == 200) {
            	 //get download file length
                int lenghtOfFile = conn.getContentLength();
                  
                fos = new FileOutputStream(new File(destPath));  
                in = conn.getInputStream();  

               //download buffer
                byte[] buffer = new byte[1024];  
                int readed = 0;  
                long total = 0;  
                  
                while ((readed = in.read(buffer)) > 0) {  
                    total += readed;
                    fos.write(buffer, 0, readed);
                    
                    publishProgress((int)((total*100)/lenghtOfFile));
                }  
                fos.close();
            }
        } catch (Exception e) {  
            Tracer.d("FileDownloadTask", ""+e);  
        } finally {
        	try {
        		if(fos!=null) fos.close();
        		if(in != null) in.close();
				if(conn != null) conn.disconnect();
			} catch (IOException e) {
				Tracer.debugException(e);
			}
        }
		return result;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		int progress = values[0];
		super.onProgressUpdate(values);
		for (IFileNetIOListener observer : observers) {
			observer.onFileNetProgress(progress);
		}
	}
}
