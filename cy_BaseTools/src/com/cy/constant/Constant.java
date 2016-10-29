package com.cy.constant;

public class Constant {
    
    public static final int DEFAULT_NUM = -1;
    public static final int ZERO_NUM = 0;
    public static final String EMPTY_STRING = "";
    
    public static final String DEFAULT_VERSION = "-1";
    public static final String APK = ".apk";
    public static final String TMP_FILE_EXT = ".gntmp"; // ??
    
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    
    // size
    public static final String ZERO = "0";
    public static final String M = "M";
    public static final int KB = 1024;
    public static final int MB = KB * KB;
    public static final int LOW_SPACE_SIZE = 5 * MB;
    public static final int MIN_SPACE_REQUIRED = 15 * MB;
    
    // time
    public static final int NO_COOLING = 0;
    
    public static final int SECOND_0 = 0;
    public static final int SECOND_1 = 1000;
    public static final int SECOND_2 = 2000;
    public static final int SECOND_3 = 3000;
    public static final int SECOND_5 = 5000;
    public static final int SECOND_10 = 10000;
    public static final int SECOND_20 = 20000;
    public static final int SECOND_30 = 30000;
    
    public static final int MILLIS_30 = 30;
    public static final int MILLIS_100 = 100;
    public static final int MILLIS_200 = 200;
    public static final int MILLIS_300 = 300;
    public static final int MILLIS_500 = 500;
    public static final int MILLIS_600 = 600;
    public static final int MILLIS_700 = 700;

    public static final int MINUTE_1 = SECOND_1 * 60;
    public static final int MINUTE_2 = MINUTE_1 * 2;
    public static final int MINUTE_3 = MINUTE_1 * 3;
    public static final int MINUTE_5 = MINUTE_1 * 5;
    public static final int MINUTE_30 = MINUTE_1 * 30;
    
    public static final int HOUR_1 = MINUTE_1 * 60;
    public static final int HOUR_2 = HOUR_1 * 2;
    public static final int HOUR_3 = HOUR_1 * 3;
    public static final int HOUR_8 = HOUR_1 * 8;

    public static final long DAY_1 = HOUR_1 * 24;
    public static final long DAY_2 = DAY_1 * 2;
    public static final long DAY_3 = DAY_1 * 3;
    public static final long DAY_7 = DAY_1 * 7;

}
