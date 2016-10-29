package com.cy.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.res.AssetManager;
import android.util.Log;

/**
 * DehooConfig.txt文件解析类
 * @author zhanmin
 */
public class TVDVBConfig {
	
	private static final String TAG = "cyTest";

	private static TVDVBConfig mTVDVBConfig;
	private Map<String,String> configInfo = new HashMap<String, String>();
	
	private TVDVBConfig(AssetManager assetManager){
		configInfo = load(assetManager,"strings.txt");
	}
	
	private TVDVBConfig(AssetManager assetManager, String configFileName){
		configInfo = load(assetManager,configFileName);
	}
	
	public static TVDVBConfig getInstance(AssetManager assetManager){
		if(mTVDVBConfig==null){
			synchronized (TVDVBConfig.class) {
				if(mTVDVBConfig==null){
					mTVDVBConfig = new TVDVBConfig(assetManager);
				}
			}
		}
		return mTVDVBConfig;
	}
	
	/**
	 * 加载配置文件
	 * @param assetManager
	 * @param configFileName
	 * @return
	 */
	private Map<String,String> load(AssetManager assetManager, String configFileName){
		InputStream is;
		StringBuffer result= new StringBuffer();
		try {
			is = assetManager.open(configFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line = "";
			while((line = br.readLine())!=null){
				result.append(line+"\n");
			}
		} catch (IOException e) {
			Log.e(TAG, "Config File Load Error:"+e.getMessage());
			e.printStackTrace();
		}
		return resolveStringXML(result.toString());
	}
	
	/**
	 * 解析配置文件
	 * @param fileContent
	 * @return
	 */
	private Map<String,String> resolveStringXML(String fileContent) {
		Map<String,String> configInfo = new HashMap<String, String>();
		if(null != fileContent){
//			Log.v(TAG, "fileContent:"+fileContent);
			String [] contentLines = fileContent.split("\n");
			for (String line : contentLines) {
				line = line.replace(" ", "");
				line = line.replace("\t", "");
//				String str = line.substring(line.indexOf("\""), line.lastIndexOf("\""));
//				System.out.println(str);
				if(!line.startsWith("<string")){
					continue;
				}
				
				String[] all = line.split("\""); 
				Log.e("cyTest", all[1]);
				System.out.println(all[1]);
				
//				if(line.indexOf("#")==0){
//					continue;
//				}
//				String str = line.substring(line.indexOf("\""), line.lastIndexOf("\""));
//				System.out.println(str);
				int keyValueIndex = line.indexOf("=");
				String key = null;
				String value=null;
				if(keyValueIndex!=-1){
					key = line.substring(0,keyValueIndex);
					value = line.substring(keyValueIndex+1,line.length());
				}
				configInfo.put(key, value);
			}
			return configInfo;
		}else{
			return null;
		}
	}
	
	/**
	 * 解析配置文件
	 * @param fileContent
	 * @return
	 */
	private Map<String,String> resolveConfig(String fileContent) {
		Map<String,String> configInfo = new HashMap<String, String>();
		if(fileContent!=null){
			Log.v(TAG, "fileContent:"+fileContent);
			String [] contentLines = fileContent.split("\n");
			for (String line : contentLines) {
				line = line.replace(" ", "");
				line = line.replace("\t", "");
				if(line.indexOf("#")==0){
					continue;
				}
				int keyValueIndex = line.indexOf("=");
				String key = null;
				String value=null;
				if(keyValueIndex!=-1){
					key = line.substring(0,keyValueIndex);
					value = line.substring(keyValueIndex+1,line.length());
				}
				configInfo.put(key, value);
			}
			return configInfo;
		}else{
			return null;
		}
	}
	
	/**
	 * 是否打开DEBUG开关
	 * @return
	 */
	public boolean isDebug(){
		if(this.configInfo.get("DEBUG")==null||configInfo.get("DEBUG").equals("")){
			return false;
		}else{
			return Boolean.parseBoolean(this.configInfo.get("DEBUG"));
//			return Boolean.valueOf(this.configInfo.get("DEBUG")).booleanValue();
		}
	}
	
	/**
	 * 是否打开测试开关
	 * @return
	 */
	public boolean isVerbose(){
		if(this.configInfo.get("VERBOSE")==null||configInfo.get("VERBOSE").equals("")){
			return false;
		}else{
			return Boolean.parseBoolean(this.configInfo.get("VERBOSE"));
//			return Boolean.valueOf(this.configInfo.get("DEBUG")).booleanValue();
		}
	}
	
}
