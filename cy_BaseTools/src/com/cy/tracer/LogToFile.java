package com.cy.tracer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Environment;

import com.cy.string.StringUtils;
import com.cy.utils.StringUtil;

public class LogToFile {
	private OutputStreamWriter osw = null;

	public LogToFile(String fileName) {
		try {
			osw = new OutputStreamWriter(new FileOutputStream(fileName, true));
		} catch (FileNotFoundException e) {
			Tracer.debugException(e);
		}
	}

	public void log(String logString) {
		if (osw != null) {
			try {
				osw.append(logString);
				osw.append("\r\n");
				osw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Tracer.debugException(e);
			}
		}
	}

	public void log(String tag, String logString) {
		Tracer.i(tag, logString);
		if (osw != null) {
			try {
				osw.append(logString);
				osw.append("\r\n");
				osw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Tracer.debugException(e);
			}
		}
	}

	public void close() {
		if (null != osw) {
			try {
				osw.flush();
				osw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			osw = null;
		}
	}

	public static String getDefLogPath() {
		try {
			File file = Environment.getExternalStorageDirectory();
			String dir = file.getPath();
			if (StringUtils.isEmpty(dir)) {
				dir = "";
			}
			return (dir + "/" + StringUtil.formatCurrDay() + ".log");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "wkl_def.log";
	}
	
}
