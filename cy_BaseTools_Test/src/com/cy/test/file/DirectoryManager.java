package com.cy.test.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import android.content.Context;
import android.os.Environment;

import com.cy.tracer.Tracer;


public class DirectoryManager {
	
	static final String STR_ASSETFILENAME = "directory";
	static final String STR_PATH_DEFAULT = "";
	//static final String STR_FIELD_APP = "main";
	//static final String STR_ROOT_APP = "movenow";
	
	static final String STR_VALUE_SDCARD = "@SD";
	static final String STR_VALUE_DATA = "@DATA";
	static final String STR_VALUE_ROOT = "@ROOT";
	
	static String m_strRootExtern = "";
	static String m_strRootData = "";
	static String m_strRoot = "";
	
	static String getExtenRoot() {
		if (null != m_strRootExtern && m_strRootExtern.length() > 0) {
		} else if (!IsExistDir(m_strRootExtern)) {
			File file = Environment.getExternalStorageDirectory();
			m_strRootExtern = file.getPath();
		}
		
		return m_strRootExtern;
	}
	
	static String getDataRoot(Context context) {
		if (null != m_strRootData && m_strRootData.length() > 0) {
		}
		else {
			//File file = Environment.getDataDirectory();
			File file = context.getFilesDir();
			m_strRootData = file.getPath();
		}
		
		return m_strRootData;
	}
	
	static String getRoot() {
		if (null != m_strRoot && m_strRoot.length() > 0) {
		}
		else {
			File file = Environment.getRootDirectory();
			m_strRoot = file.getPath();
		}
		
		return m_strRoot;
	}
	
	static String getBaseRoot(Context context) {
		String strRoot = null;
		
		strRoot = getExtenRoot();
		strRoot = getDataRoot(context);
		strRoot = getRoot();
		
		return strRoot;
	}
	
	
	static Properties loadConfig(Context context) {
		Properties properties = null;
		
		try {
			InputStream input = context.getAssets().open(STR_ASSETFILENAME);
			properties = new Properties();
			properties.load(input);
			
		} catch (UnsupportedEncodingException e) {
			properties = null;
			Tracer.debugException(e);
		} catch (IOException e) {
			properties = null;
			Tracer.debugException(e);
		}
		
		return properties;
	}
	
	public static String getBaseDir(Context context, String field) {
		String path = new String();
		Properties properties = loadConfig(context);
		
		path = String.format("%s", properties.getProperty(field, STR_PATH_DEFAULT));
		
		path = getRootPath(path, context);
		
		MakeDir(path);
		
//		Tracer.v("DirectoryManager:", path);
		return path;
	}
	
	/*static public String getAppDir(Context context, String field, int userid) {
		String fileName = new String();
		Properties properties = loadConfig(context);
		
		fileName = String.format("/%s/%s", 
				properties.getProperty(STR_FIELD_APP, STR_ROOT_APP), 
				properties.getProperty(field, STR_PATH_DEFAULT));
		
		Tracer.d("DirectoryManager:", fileName);
		return fileName;
	}*/
	
	static String getRootPath(String str, Context context) {
		getBaseRoot(context);
		str = str.replace(STR_VALUE_SDCARD, m_strRootExtern);
		str = str.replace(STR_VALUE_DATA, m_strRootData);
		str = str.replace(STR_VALUE_ROOT, m_strRoot);
		return str;
	}
	
	static public boolean IsExistDir(String path) {
		File file = new File(path);
		return file.exists();
	}
	
	static public boolean MakeDir(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return file.mkdirs();
		}
		
		return true;
	}
}
