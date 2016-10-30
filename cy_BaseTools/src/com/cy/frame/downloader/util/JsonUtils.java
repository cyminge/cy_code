package com.cy.frame.downloader.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.cy.constant.Constant;
import com.cy.frame.downloader.httpsutil.MyHostnameVerifier;
import com.cy.frame.downloader.httpsutil.MyTrustManager;
import com.cy.global.BaseApplication;
import com.cy.utils.Utils;

@SuppressLint("NewApi") 
public class JsonUtils {
    
    private static final String JSON_SIGN = "GioneeGameHall";
    private static final String GAMEHALL_FAIL = "gamehall_fail";
    public static final String DATA = "data";
    
    public static boolean isValueEmpty(String value) {
        return TextUtils.isEmpty(value) || Constant.NULL.equals(value);
    }
    
    public static boolean getBoolean(String value) {
        return (value != null && (value.equals(Constant.TRUE_SYMBOL) || value.equals(Constant.TRUE)));
    }
    
    public static long optTime(JSONObject json, String timeKey) {
        return json.optLong(timeKey) * Constant.SECOND_1;
    }
    
    public static String getUpgradeInfo(String actionUrl, String dataString) {
//        StringBuilder data = new StringBuilder(dataString);
//        data.append(FrameworkUtil.getAndroidVersion().substring("Android".length()))
//                .append(Constant.COMBINE_SYMBOL_1)
//                .append(Utils.getPhonePixels()[0] + "*" + Utils.getPhonePixels()[1]);

        Map<String, String> map = new HashMap<String, String>();
//        map.put("apk_info", data.toString());
//
//        String phoneRAM = Utils.getPhoneRAM();
//        map.put("phone_ram", phoneRAM);
        return postData(actionUrl, map);
    }
	
	public static boolean isRequestDataFail(String data) {
        return GAMEHALL_FAIL.equals(data);
    }
	
    public static boolean isRequestDataSuccess(String data) {
        if (data == null) {
            return false;
        }
        return data.isEmpty() || hasGioneeSign(data);
    }
    
    public static boolean hasGioneeSign(String jsonInfo) {
        if (jsonInfo == null) {
            return false;
        }
        return jsonInfo.contains(JSON_SIGN);
    }
    
    public static String postData(String actionUrl, Map<String, String> params) {
        if (!Utils.hasNetwork()) {
            return GAMEHALL_FAIL;
        }
        HttpURLConnection conn = null;
        OutputStream op = null;
        try {
            String finalUrl = UrlTranslator.translateUrl(actionUrl);
            URL url = new URL(finalUrl);
            byte[] data = getRequestData(params, finalUrl).getBytes(Constant.UTF_8);
            if (url.getProtocol().equalsIgnoreCase(Constant.HTTPS)) {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, new TrustManager[] {new MyTrustManager()}, new SecureRandom()); // 
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier()); // 忽略hostname 的验证。（仅仅用于测试阶段，不建议用于发布后的产品中。）
                conn = (HttpsURLConnection) url.openConnection();
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }

            conn.setConnectTimeout(Constant.SECOND_10);
            conn.setReadTimeout(Constant.SECOND_10);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.setRequestProperty("Accept-Encoding", "gzip");

            op = conn.getOutputStream();
            op.write(data);

            int response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                return getConnectResult(conn);
            }
        } catch (Exception e) {
            Log.e("cyTest", "postData error");
            e.printStackTrace();
        } finally {
            try {
                if (op != null) {
                    op.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
            }
        }
        return GAMEHALL_FAIL;
    }
    
    private static String getRequestData(Map<String, String> params, String url) {
        addCommonParam(params, url);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=").append(Utils.getUTF8Code(entry.getValue()))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (Exception e) {
        }
        
        return stringBuffer.toString();
    }
    
    public static final String ACCOUNT_USER_NAME = "uname";
    
    private static void addCommonParam(Map<String, String> params, String url) {
        params.put(JsonConstant.IMEI, Utils.getIMEI(BaseApplication.getAppContext()));
        params.put(JsonConstant.VERSION, Utils.getVersionCode(BaseApplication.getAppContext()));
        params.put(JsonConstant.SP, Utils.getPublicStatisArgs(false));
//        params.put(StatisKey.CLIENT_PKG, "com.cy.frame.imageloader"); // 统计用的，去掉  cyminge modify
//        params.put(StatisKey.BRAND, "");
//        if (Utils.isLogin()) {
            params.put(ACCOUNT_USER_NAME, "cyminge");
            params.put(JsonConstant.PUUID, UUID.randomUUID().toString());
            params.put(JsonConstant.CLIENT_ID, TimeUtils.getClientId());
            params.put(JsonConstant.SERVER_ID, TimeUtils.getServerId(url));
//        }
    }
    
    private static String getConnectResult(URLConnection conn) {
        InputStream finalInputStream = null;
        try {
            InputStream is = conn.getInputStream();
            if ("gzip".equals(conn.getContentEncoding())) {
                finalInputStream = new BufferedInputStream(new GZIPInputStream(is));
            } else {
                finalInputStream = is;
            }
            StringBuilder res = new StringBuilder();
            int ch;
            while ((ch = finalInputStream.read()) != -1) {
                res.append((char) ch);
            }
            return res.toString();
        } catch (Exception e) {
            Log.e("cyTest", "getConnectResult error == " + e.getClass().getName() + " " + e.getMessage());
        } finally {
            try {
                if (finalInputStream != null) {
                    finalInputStream.close();
                }
            } catch (IOException e) {
            }
        }
        return GAMEHALL_FAIL;
    }

}
