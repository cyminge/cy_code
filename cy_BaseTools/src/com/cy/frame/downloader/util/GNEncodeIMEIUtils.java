/*******************************************************************************
 * Filename:
 * ---------
 *  GNDecodeUtils.java
 *
 * Project:
 * --------
 *   Browser
 *
 * Description:
 * ------------
 *  decode mobile IMEI
 *
 * Author:
 * -------
 *  2012.06.25 caody 
 *
 ****************************************************************************/
package com.cy.frame.downloader.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.cy.constant.Constant;

public class GNEncodeIMEIUtils {

    private static final String AES = "AES";
    private static final String IV_PARAM = "0102030405060708";
    private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
    private static final String SEED = "GIONEE2012061900";
    private static final String HEX = "0123456789ABCDEF";

    public static String encrypt(String seed, String cleartext, String ivParam) {
        try {
            byte[] rawKey = seed.getBytes(Constant.UTF_8);
            byte[] result = encrypt(rawKey, cleartext.getBytes(Constant.UTF_8), ivParam);
            return toHex(result);
        } catch (Exception e) {
            return Constant.EMPTY;
        }
    }

    private static byte[] encrypt(byte[] raw, byte[] clear, String ivParam) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(ivParam.getBytes(Constant.UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        // System.out.println("size:" + cipher.getBlockSize());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, zeroIv);
        return cipher.doFinal(clear);
    }

    private static String toHex(byte[] buf) {
        if (buf == null) {
            return Constant.EMPTY;
        }
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (byte aBuf : buf) {
            appendHex(result, aBuf);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    public static String get(String str) {
        if (str == null) {
            str = Constant.EMPTY;
        }

        String masterPassword = SEED;
        // String decode = GNDecodeUtils.decrypt(masterPassword, encryptingCode);
        // System.out.println("encryptingCode: " + encryptingCode);
        // System.out.println("decode: " + decode);
        return encrypt(masterPassword, str, IV_PARAM);
    }

    public static String decrypt(String encrypted) {
        try {
            return decrypt(SEED, encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.EMPTY;
        }
    }

    private static String decrypt(String seed, String encrypted) throws Exception {
        byte[] rawKey = seed.getBytes(Constant.UTF_8);
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result, Constant.UTF_8);
    }

    private static byte[] toByte(String hexString) {

        int len = hexString.length() / 2;
        byte[] result = new byte[len];

        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;

    }

    private static final String VIPARA = "0102030405060708";

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes(Constant.UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, zeroIv);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

}
