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
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.cy.constant.Constant;
import com.cy.frame.downloader.httpsutil.MyHostnameVerifier;
import com.cy.frame.downloader.httpsutil.MyTrustManager;
import com.cy.global.BaseApplication;
import com.cy.tracer.Tracer;
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
            finalUrl = finalUrl+"?puuid=95E7D365C3BC4F9F837B9E00DDBAAE9F&clientId=1b80f251f8a402bc6cc7b6fa2645ac60&serverId=0CA6E364EA9F8878D21F4900AB173737EAFF4C04EA1781EC08CB197D596D217E&client_pkg=gn.com.android.gamehall&imei=FD34645D0CF3A18C9FC4E2C49F11C510&brand=GIONEE&sp=GN3001_1.6.8.g_5.1.16_Android5.1_720*1280_U99999_wifi_FD34645D0CF3A18C9FC4E2C49F11C510&version=1.6.8.g&uname=13662882127";
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
//        params.put(JsonConstant.IMEI, Utils.getIMEI(BaseApplication.getAppContext()));
//        params.put(JsonConstant.VERSION, Utils.getVersionCode(BaseApplication.getAppContext()));
//        params.put(JsonConstant.SP, Utils.getPublicStatisArgs(false));
//        params.put(StatisKey.CLIENT_PKG, "com.cy.frame.imageloader"); // 统计用的，去掉  cyminge modify
//        params.put(StatisKey.BRAND, "");
//        if (Utils.isLogin()) {
//            params.put(ACCOUNT_USER_NAME, "cyminge");
//            params.put(JsonConstant.PUUID, UUID.randomUUID().toString());
//            params.put(JsonConstant.CLIENT_ID, TimeUtils.getClientId());
//            params.put(JsonConstant.SERVER_ID, TimeUtils.getServerId(url));
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
    
    public static String mergeRequestResult(String[] requestResults) {
        JSONObject totalObject = new JSONObject();
        for (String result : requestResults) {
            if (isRequestDataFail(result)) {
                return result;
            }

            if (!JsonUtils.hasGioneeSign(result) || isResultFailed(result)) {
                continue;
            }
            try {
                JSONObject subObject = new JSONObject(result);
                totalObject = mergeJsonObject(totalObject, subObject);
            } catch (JSONException e) {
            }
        }
        return totalObject.toString();
    }
    
    public static boolean isResultFailed(String result) {
        boolean success = false;
        try {
            success = new JSONObject(result).getBoolean("success");
        } catch (JSONException e) {
        } catch (Exception e) {
        }
        return !success;
    }

    private static JSONObject mergeJsonObject(JSONObject totalObject, JSONObject subObject) {
        try {
            Iterator<String> subKeysIter = subObject.keys();
            while (subKeysIter.hasNext()) {
                String subKeyStr = subKeysIter.next();
                Object subValue = subObject.opt(subKeyStr);
                if (totalObject.has(subKeyStr)) {
                    tryMergeValue(totalObject, subKeyStr, subValue);
                } else {
                    totalObject.put(subKeyStr, subValue);
                }
            }
        } catch (JSONException e) {
        }
        return totalObject;
    }

    private static void tryMergeValue(JSONObject totalObject, String subKeyStr, Object subValue)
            throws JSONException {
        Object totalValue = totalObject.opt(subKeyStr);
        if (totalValue != null && subValue != null && totalValue.getClass() != subValue.getClass()) {
            Tracer.e("cyTest", "Json object merge ERROR, have different type at key: \"" + subKeyStr + "\"");
            return;
        }
        if (totalValue instanceof JSONArray) {
            JSONArray jsonArray = mergeJsonArray((JSONArray) totalValue, (JSONArray) subValue);
            totalObject.put(subKeyStr, jsonArray);
        } else if (totalValue instanceof JSONObject) {
            JSONObject jsonObject = mergeJsonObject((JSONObject) totalValue, (JSONObject) subValue);
            totalObject.put(subKeyStr, jsonObject);
        }
    }

    private static JSONArray mergeJsonArray(JSONArray totalArray, JSONArray subArray) {
        int length = subArray.length();
        for (int i = 0; i < length; i++) {
            try {
                totalArray.put(subArray.get(i));
            } catch (JSONException e) {
            }
        }
        return totalArray;
    }

}
