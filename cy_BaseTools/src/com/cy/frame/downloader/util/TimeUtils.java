package com.cy.frame.downloader.util;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.text.format.Time;

import com.cy.constant.Constant;
import com.cy.global.BaseApplication;
import com.cy.utils.Utils;

@SuppressLint("NewApi") 
public class TimeUtils {
    public static String getFormatSecUYmd(long second) {
        Time time = new Time();
        time.set(second * 1000);
        return time.format("%Y/%m/%d");
    }

    public static String getFormatMillUYmd(long mills) {
        Time time = new Time();
        time.set(mills);
        return time.format("%Y/%m/%d");
    }
    
    public static long getCurrentTimeSecond(){
        return System.currentTimeMillis() / 1000;
    }
    
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
    
    public static long getGMTTimeMillis() {
        Calendar calendar = Calendar.getInstance();
        long unixTime = calendar.getTimeInMillis();
        return unixTime - TimeZone.getDefault().getRawOffset();
    }
    
    public static String getClientId() {
        String uuid = UUID.randomUUID().toString();
        String accountName = "cyminge";
        int length = accountName.length();
        for (int i = 0; i < length; i++) {
            char ch = accountName.charAt(i);
            int position = (ch + i) % uuid.length();
            uuid = uuid.substring(0, position) + ch + uuid.substring(position + 1);
        }
        return Utils.getStringMd5(uuid);
    }

    public static String getServerId(String url) {
        String seed = getSeed(url);
        String srcServerId = getSrcServerId();
        String ivParam = getIvParam();
        return GNEncodeIMEIUtils.encrypt(seed, srcServerId, ivParam);
    }
    
    
    private static String getSeed(String url) {
        String urlSeed = getUrlSeed(url);
        String imeiSeed = Utils.getIMEI(BaseApplication.getAppContext());
        String accountSeed = "cyminge";
        String seed = urlSeed + imeiSeed + accountSeed;
        seed = Utils.getStringMd5(seed);
        return seed.substring(0, Math.min(16, seed.length()));
    }

    private static String getSrcServerId() {
        return Utils.getVersionCode(BaseApplication.getAppContext()) + Constant.BOTTOM_LINE + Utils.getIMEI(BaseApplication.getAppContext())
                + Constant.BOTTOM_LINE + "cyminge";
    }

    private static String getUrlSeed(String url) {
        url = url.replaceAll("/*(\\?+.*)?$", "");
        int startIndex = url.lastIndexOf("/");
        return url.substring(startIndex + 1).toUpperCase(Locale.US);
    }

    private static String getIvParam() {
        String uuid = UUID.randomUUID().toString();
        uuid = Utils.getStringMd5(uuid);
        return uuid.substring(0, Math.min(16, uuid.length()));
    }
    
    public static boolean isExceedLimitTime(long earlier, long later, long limitMillisecond) {
        return Math.abs(later - earlier) >= limitMillisecond;
    }
}
