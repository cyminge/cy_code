package com.cy.string;

import java.security.MessageDigest;

import android.util.Base64;

import com.cy.tracer.Tracer;

public class Encodes {

	private Encodes() {
	}

	// MD5加密，32位
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			Tracer.debugException(e);
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	// 可逆的加密算法
	public static String encryptmd5(String str) {
		char[] a = str.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 'l');
		}
		String s = new String(a);
		return s;
	}

	/**
	 * 将字节数组编码为字符串
	 * 
	 * @param data
	 */
	public static String encode(byte[] data) {
		if (data == null) {
			return null;
		} else {
			return Base64.encodeToString(data, Base64.NO_PADDING | Base64.NO_WRAP);
		}
	}

	/**
	 * 将base64字符串解码为字节数组
	 * 
	 * @param str
	 */
	public static byte[] decode(String str) {
		try {
			return Base64.decode(str.replace(" ", "+"), Base64.NO_PADDING | Base64.NO_WRAP);
		} catch (IllegalArgumentException e) {
			byte[] data = (new String("base64 error")).getBytes();
			return data;
		}
	}

}
