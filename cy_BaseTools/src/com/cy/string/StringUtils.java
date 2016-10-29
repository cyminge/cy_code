package com.cy.string;

import java.util.regex.Pattern;

import android.annotation.SuppressLint;

public class StringUtils {
	/***
	 * Returns true if the string is null or 0-length.
	 * @param str the string to be examined
	 * @return true if str is null or zero length
	 */
	public static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 去除电话号码 前缀和-
	 * 
	 * @return 处理后的电话号码
	 */
	public static String formatNumber(String number) {
		String returnStr;
		if (number == null || number.length() == 0) {
			return null;
		}
		returnStr = number.replaceAll("-", "");
		if (returnStr.startsWith("+86")) {
			return returnStr.substring(3, number.length());
		} else if (returnStr.startsWith("17951")) {
			return returnStr.substring(5, number.length());
		} else if (returnStr.startsWith("12593")) {
			return returnStr.substring(5, number.length());
		} else if (returnStr.startsWith("17911")) {
			return returnStr.substring(5, number.length());
		} else if (returnStr.startsWith("17900")) {
			return returnStr.substring(5, number.length());
		} else {
			return returnStr;
		}
	}

	/**
	 * 处理首字母
	 * 
	 * @return 字符串的首字母，不是A~Z范围的返回#
	 */
	@SuppressLint("DefaultLocale")
	public static String formatAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

}
