package com.cy.account;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import android.os.Environment;

import com.cy.utils.StringUtil;

public class FileManager {
	private static Context mContext = null;
	private static FileManager instance = null;

	public static void prepare(Context context) {
		mContext = context;
		instance = new FileManager();
	}

	private FileManager() {
	}
	
	public static FileManager getInstance() {
		return instance;
	}
	
	private static HashMap<OBJTYPE, String> objMap = new HashMap<OBJTYPE, String>();
	private static HashMap<DIRTYPE, String> dirMap = new HashMap<DIRTYPE, String>();
	
	static {
		objMap.put(OBJTYPE.USER, "user");
		objMap.put(OBJTYPE.GROUP, "group");

		dirMap.put(DIRTYPE.USER_AVATAR_IMAGE, "portrait");
		dirMap.put(DIRTYPE.GROUP_AVATAR_IMAGE, "portrait");
		dirMap.put(DIRTYPE.CHAT_IMAGE, "chat_image");
		dirMap.put(DIRTYPE.CHAT_VOICE, "chat_voice");
		dirMap.put(DIRTYPE.CACHE, "cache");
	}

//	public static String getChatVoice(OBJTYPE type, String fileName) {
//		String baseDir = DirectoryManager.getBaseDir(mContext, dirMap.get(DIRTYPE.CHAT_VOICE));
//		return populatePath(baseDir, objMap.get(type) + fileName);
//	}
//
//	public static String getChatImage(OBJTYPE type, String fileName) {
//		String baseDir = DirectoryManager.getBaseDir(mContext, dirMap.get(DIRTYPE.CHAT_IMAGE));
//		return populatePath(baseDir, objMap.get(type) + fileName);
//	}
//
//	public static String getPortrait(OBJTYPE type, String fileName) {
//		String baseDir = DirectoryManager.getBaseDir(mContext, dirMap.get(DIRTYPE.USER_AVATAR_IMAGE));
//		return populatePath(baseDir, objMap.get(type) + fileName);
//	}
//	
//	public static String getCacheFile(OBJTYPE type, String fileName) {
//		String baseDir = DirectoryManager.getBaseDir(mContext, dirMap.get(DIRTYPE.CACHE));
//		return populatePath(baseDir, objMap.get(type) + fileName);
//	}
//	
//	public static String getAvatarFile(OBJTYPE type, String fileName) {
//		String baseDir = DirectoryManager.getBaseDir(mContext, dirMap.get(DIRTYPE.USER_AVATAR_IMAGE));
//		return populatePath(baseDir, objMap.get(type) + fileName);
//	}

	public static String getLogPath() {
		File file = Environment.getExternalStorageDirectory();
		String dir = file.getPath();
//		String dir = DirectoryManager.getBaseDir(mContext, dirMap.get(DIRTYPE.CATCHER)); // 异常处理时调用会导致死循环
		return populatePath(dir, StringUtil.formatCurrDay() + ".log");
	}

	public static String populatePath(String... spliteds) {
		if (spliteds.length <= 0) {
			return "";
		}
		String fullPath = spliteds[0];
		for (int i = 1; i < spliteds.length; i++) {
			fullPath += "/";
			fullPath += spliteds[i];
		}
		return fullPath;
	}
}
