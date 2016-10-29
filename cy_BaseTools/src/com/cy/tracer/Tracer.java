package com.cy.tracer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.cy.tracer.export.IOnMaxLogFrame;
import com.cy.tracer.export.OnMaxLogFrameDef;
import com.cy.utils.StringUtil;
import com.cy.utils.storage.StorageUtils;

import de.greenrobot.event.EventBus;

/**
 * mDebugLevel 用于修改上报阀值 mGlobType取值如下 GLOB_DEVLOP = 0; // 开发时，log打印到logcat
 * GLOB_DEBUG = 1; // 内部测试，log既打到logcat，也上报日志 GLOB_RELS = 2; // release版本，只上报
 * 
 * @author liuzf1986
 * @time 2014-4-26
 */
public class Tracer {
    public static final String PERSI_BRC = "com.veclink.log.persistent";

    private static int MAX_CACHE_COUNT = 512; // 打印缓冲最大条数
    private static int MAX_CACHE_CAPACITY = (MAX_CACHE_COUNT + 64) * 512; // 每条打印预留512大小，并预留异常打印空间

    public static enum EXCEP_TYPE {
        UNCAUGHTED, CAUGHTED
    }

    private static IOnMaxLogFrame mOnMaxLogFrame = null;

    /* 控制常量 */
    private static final String LOG_TYPE_VERBOSE = "verbose";
    private static final int LOG_LEVEL_VERBOSE = 0;
    private static final String LOG_TYPE_DEBUG = "debug";
    private static final int LOG_LEVEL_DEBUG = 1;
    private static final String LOG_TYPE_INFO = "info";
    private static final int LOG_LEVEL_INFO = 2;
    private static final String LOG_TYPE_WARN = "warn";
    private static final int LOG_LEVEL_WARN = 3;
    private static final String LOG_TYPE_ERROR = "error";
    private static final int LOG_LEVEL_ERROR = 4;
    private static final String LOG_TYPE_FATLE = "fatal"; /* LOG头独有 */
    private static final int LOG_LEVEL_FATAL = 5;

    private static final String LOG_TYPE_EXCEP = "exception"; /* LOG信息条独有 */
    private static final int LOG_LEVEL_EXECP = 6;
    private static final String LOG_TYPE_UNCATCHED = "uncatched"; /* LOG信息条独有 */
    private static final int LOG_LEVEL_UNCATCHED = 7;

    private static final String REPORT_TYPE_AUTO = "auto";
    private static final String REPORT_TYPE_MANUAL = "manual";

    /* 控制变量 */
    private static int mErrorLevel = LOG_LEVEL_INFO; // 一帧log的最高等级
    private static boolean mLogcat = true; // logcat默认打开log
    private static boolean mSaveFile = false; // 默认关闭log输出到文件
    private static boolean mReport = false; // 上报默认关闭
    private static int mSaveLevel = LOG_LEVEL_WARN; // 默认记录警告级别以上log
    private static int mPersistTriggerLevel = LOG_LEVEL_UNCATCHED; // 默认当出现未捕获异常时，log持久化

    // private static int mDebugLevel = 8; // 不上报日志
    // private static int mDebugLevel = LOG_LEVEL_ERROR; // 上报日志等级：Error

    /* tag to split tag and concrete message */
    private static String SPLITER = ":\t\t";
    /* Context */
    private static Context context = null;
    /* this log message */
    // private static JSONObject logJson = null;
    /* Log message array */
    // private static JSONArray logItemsArray = null;
    /* Global log level */
    private static String LOG_TYPE = "log_type";
    /* Log message */
    private static String LOG_CONTENT = "message";
    /* device information, each log frame will have one device information */
    private static String DEVICE = "device";
    /* Log items */
    private static String LOG_ITEMS = "logs";
    /* Log item */
    private static String LOG_ITEM = "log";
    /* Runtime info */
    private static String RUN_TIME = "run_time";
    /* log extern info */
    private static String LOG_EXTERN = "log_extern";
    /* 将log信息写入本地文件的缓冲，默认缓冲大小为20k */
    private static StringBuffer logStringBuilder = null;
    private static int logStringCount = 0;

    private static WifiConnectChangedReceiver wifiReceiver = null;
    private static PersistentReceiver mPersistentReceiver;

    public static Boolean isUpLoad = true;
    public static Boolean isSaveFile = true;

    private static boolean isReady() {
        if ((context != null) && logStringBuilder != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从assets/debug.properties中加载日志配置 log.logcat mLogcat log.report mReport
     * log.save.level mSaveLevel log.pers.trigger mPersistTriggerLevel
     * 
     * @param context
     */
    public static void loadConfiguration(Context context) {
        Properties properties = new Properties();
        try {
            properties.load(context.getAssets().open("log.properties"));

            /* 读取logcat开关 */
            String value = properties.getProperty("log.logcat");
            if (value != null) {
                if (value.equalsIgnoreCase("true")) {
                    mLogcat = true;
                } else if (value.equalsIgnoreCase("false")) {
                    mLogcat = false;
                }
            }

            /* 读取文件保存开关 */
            value = properties.getProperty("log.savefile");
            if (value != null) {
                if (value.equalsIgnoreCase("true")) {
                    mSaveFile = true;
                } else if (value.equalsIgnoreCase("false")) {
                    mSaveFile = false;
                }
            }

            /* 读取上报开关 */
            value = properties.getProperty("log.report");
            if (value != null) {
                if (value.equalsIgnoreCase("true")) {
                    mReport = true;
                } else if (value.equalsIgnoreCase("false")) {
                    mReport = false;
                }
            }

            /* 读取保存最小等级 */
            value = properties.getProperty("log.save.level");
            if (value != null) {
                try {
                    int logLevel = Integer.parseInt(value);
                    if (logLevel >= 0) {
                        mSaveLevel = logLevel;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /* 读取触发持久化最小等级 */
            value = properties.getProperty("log.pers.trigger");
            if (value != null) {
                try {
                    int logLevel = Integer.parseInt(value);
                    if (logLevel >= 0) {
                        mPersistTriggerLevel = logLevel;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Tracer", "logcat: " + mLogcat + mReport + ", save level :" + mSaveLevel + ", report :"
                + ", trigger persistent level :" + mPersistTriggerLevel);
    }

    public static void init(Context context) {
        init(context, null);
    }

    /**
     * 初始化
     * 
     * @param context
     * @param onMaxLogFrame
     */
    public static void init(Context context, IOnMaxLogFrame onMaxLogFrame) {
        Tracer.context = context.getApplicationContext();

        loadConfiguration(context);
        resetCache();

        if (onMaxLogFrame == null) {
            if (mSaveLevel > 0) {
                onMaxLogFrame = new OnMaxLogFrameDef();
            } else {
                onMaxLogFrame = new IOnMaxLogFrame() {
                    @Override
                    public int reportToServer(String jsonString) {
                        return 0;
                    }

                    @Override
                    public int reportToServer(String reportType, String logName, String logType,
                            String jsonString) {
                        return 0;
                    }

                    @Override
                    public String getLogPath() {
                        return LogToFile.getDefLogPath();
                    }

                    @Override
                    public int saveLogToFile(String jsonString) {
                        return 0;
                    }
                };
            }
        }

        mOnMaxLogFrame = onMaxLogFrame;
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        wifiReceiver = new WifiConnectChangedReceiver();
        context.getApplicationContext().registerReceiver(wifiReceiver, filter);

        mPersistentReceiver = new PersistentReceiver();
        filter = new IntentFilter(PERSI_BRC);
        context.getApplicationContext().registerReceiver(mPersistentReceiver, filter);

        Log.d("Tracer", "init done !");
    }

    public static void deInit() {
        context.getApplicationContext().unregisterReceiver(wifiReceiver);
        logStringBuilder = null;
        logStringCount = 0;
    }

    static public class WifiConnectChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    State state = networkInfo.getState();
                    boolean isConnected = (state == State.CONNECTED);
                    if (isConnected) {
                        if (isReady()) {
                            if (logStringCount > 0) {
                                // flushToServer(); // 立刻上传
                            }
                        }
                    }
                }
            }
        }
    }

    static public class PersistentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (PERSI_BRC.equals(intent.getAction())) {
                if (!persisteLog(true, REPORT_TYPE_MANUAL, "logup" + System.currentTimeMillis())) {
                    if (persisting.get()) {
                        EventBus.getDefault().post(new ReportDoneEvent(false, -2)); // 正在上传
                    } else {
                        EventBus.getDefault().post(new ReportDoneEvent(false, -3));
                    }
                }
            }
        }
    }

    /**
     * 考虑代码的执行效率，每个代码里面都加入重复的判断逻辑，不使用函数调用方式
     * 
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (mLogcat) {
            if (null == msg) {
                msg = "null";
            }
            Log.v(tag, msg);
        }

        if (LOG_LEVEL_VERBOSE >= mSaveLevel) {
            appendSimpleLog('V', tag, msg);
            /* 达到记录点 */
            if (LOG_LEVEL_VERBOSE >= mPersistTriggerLevel || logStringCount >= MAX_CACHE_COUNT) {
                persisteLog(false);
            }
        }
    }

    public static void v(String tag, String message, Object... args) {
        if (null == message) {
            return;
        }

        message = String.format(message, args);

        if (mLogcat) {
            Log.v(tag, message);
        }

        if (LOG_LEVEL_VERBOSE >= mSaveLevel) {
            appendSimpleLog('V', tag, message);
            if (mErrorLevel < LOG_LEVEL_VERBOSE)
                mErrorLevel = LOG_LEVEL_VERBOSE;
            /* 达到记录点 */
            if (LOG_LEVEL_VERBOSE >= mPersistTriggerLevel || logStringCount >= MAX_CACHE_COUNT) {
                persisteLog(false);
            }

        }
    }

    public static void d(String tag, String msg) {
        if (mLogcat) {
            if (null == msg) {
                msg = "null";
            }
            Log.d(tag, msg);
        }

        if (LOG_LEVEL_DEBUG >= mSaveLevel) {
            appendSimpleLog('D', tag, msg);
            if (mErrorLevel < LOG_LEVEL_DEBUG) {
                mErrorLevel = LOG_LEVEL_DEBUG;
            }
            /* 达到记录点 */
            if (LOG_LEVEL_DEBUG >= mPersistTriggerLevel || logStringCount >= MAX_CACHE_COUNT) {
                persisteLog(false);
            }
        }
    }

    public static void d(String tag, String message, Object... args) {
        if (null == message) {
            return;
        }

        message = String.format(message, args);

        if (mLogcat) {
            Log.d(tag, message);
        }

        if (LOG_LEVEL_DEBUG >= mSaveLevel) {
            appendSimpleLog('D', tag, message);
            if (mErrorLevel < LOG_LEVEL_DEBUG)
                mErrorLevel = LOG_LEVEL_DEBUG;
            /* 达到记录点 */
            if (LOG_LEVEL_DEBUG >= mPersistTriggerLevel || logStringCount >= MAX_CACHE_COUNT) {
                persisteLog(false);
            }

        }
    }

    public static void i(String tag, String msg) {
        if (mLogcat) {
            if (null == msg) {
                msg = "null";
            }
            Log.i(tag, msg);
        }

        if (LOG_LEVEL_INFO >= mSaveLevel) {
            appendSimpleLog('I', tag, msg);
            if (mErrorLevel < LOG_LEVEL_INFO)
                mErrorLevel = LOG_LEVEL_INFO;
            /* 达到记录点 */
            if (LOG_LEVEL_INFO >= mPersistTriggerLevel || logStringCount >= MAX_CACHE_COUNT) {
                persisteLog(false);
            }
        }
    }

    public static void i(String tag, String message, Object... args) {
        if (null == message) {
            return;
        }

        message = String.format(message, args);

        if (mLogcat) {
            Log.i(tag, message);
        }

        if (LOG_LEVEL_INFO >= mSaveLevel) {
            appendSimpleLog('I', tag, message);
            if (mErrorLevel < LOG_LEVEL_INFO)
                mErrorLevel = LOG_LEVEL_INFO;
            /* 达到记录点 */
            if (LOG_LEVEL_INFO >= mPersistTriggerLevel || logStringCount >= MAX_CACHE_COUNT) {
                persisteLog(false);
            }

        }
    }

    public static void w(String tag, String msg) {
        if (mLogcat) {
            if (null == msg) {
                msg = "null";
            }
            Log.w(tag, msg);
        }

        if (LOG_LEVEL_WARN >= mSaveLevel) {
            appendSimpleLog('W', tag, msg);
            if (mErrorLevel < LOG_LEVEL_WARN)
                mErrorLevel = LOG_LEVEL_WARN;
            /* 达到记录点 */
            if (LOG_LEVEL_WARN >= mPersistTriggerLevel || logStringCount >= MAX_CACHE_COUNT) {
                persisteLog(false);
            }
        }
    }

    public static void w(String tag, String message, Object... args) {
        if (null == message) {
            return;
        }

        message = String.format(message, args);

        if (mLogcat) {
            Log.w(tag, message);
        }

        if (LOG_LEVEL_WARN >= mSaveLevel) {
            appendSimpleLog('W', tag, message);
            if (mErrorLevel < LOG_LEVEL_WARN)
                mErrorLevel = LOG_LEVEL_WARN;
            /* 达到记录点 */
            if (LOG_LEVEL_WARN >= mPersistTriggerLevel || logStringCount >= MAX_CACHE_COUNT) {
                persisteLog(false);
            }

        }
    }

    public static void e(String tag, String msg) {
        if (mLogcat) {
            if (null == msg) {
                msg = "null";
            }
            Log.e(tag, msg);
        }

        if (LOG_LEVEL_ERROR >= mSaveLevel) {
            appendSimpleLog('E', tag, msg);
            if (mErrorLevel < LOG_LEVEL_ERROR)
                mErrorLevel = LOG_LEVEL_ERROR;
            /* 达到记录点 */
            if (LOG_LEVEL_ERROR >= mPersistTriggerLevel || logStringCount >= MAX_CACHE_COUNT) {
                persisteLog(false);
            }

        }
    }

    public static void e(String tag, String message, Object... args) {
        if (null == message) {
            return;
        }

        message = String.format(message, args);

        if (mLogcat) {
            Log.e(tag, message);
        }

        if (LOG_LEVEL_ERROR >= mSaveLevel) {
            appendSimpleLog('E', tag, message);
            if (mErrorLevel < LOG_LEVEL_ERROR)
                mErrorLevel = LOG_LEVEL_ERROR;
            /* 达到记录点 */
            if (LOG_LEVEL_ERROR >= mPersistTriggerLevel || logStringCount >= MAX_CACHE_COUNT) {
                persisteLog(false);
            }

        }
    }

    /**
     * 供JNI崩溃调用
     */
    public static void onNativeCrash() {
        Tracer.e("media", "JNI crashed");
    }

    public static void e(String tag, String msg, Throwable throwable) {
        Tracer.printException(null, throwable, "" + tag + ":" + msg, Tracer.EXCEP_TYPE.CAUGHTED);
    }

    public static void debugException(Throwable throwable) {
        Tracer.printException(null, throwable, null, Tracer.EXCEP_TYPE.CAUGHTED);
    }

    public static void debugException(Throwable throwable, String exterInfo) {
        Tracer.printException(null, throwable, exterInfo, Tracer.EXCEP_TYPE.CAUGHTED);
    }

    /**
     * 打印异常信息(无自定义信息)
     * 
     * @param thread
     * @param throwable
     * @param type
     * @return 是否上报成功
     */
    public static boolean printException(Thread thread, Throwable throwable, EXCEP_TYPE type) {
        return printException(thread, throwable, null, type);
    }

    /**
     * 打印异常信息
     * 
     * @param thread
     * @param throwable
     * @param exterInfo
     * @param type
     * @return 是否上报成功
     */
    public static boolean printException(Thread thread, Throwable throwable, String exterInfo, EXCEP_TYPE type) {
        if (mLogcat) {
            throwable.printStackTrace();
        }

        int logLevel = LOG_LEVEL_EXECP;
        String exceptionType = LOG_TYPE_EXCEP;
        if (EXCEP_TYPE.UNCAUGHTED == type) {
            logLevel = LOG_LEVEL_UNCATCHED;
            exceptionType = LOG_TYPE_UNCATCHED;
        }

        if (logLevel >= mSaveLevel) {
            if (mErrorLevel < logLevel)
                mErrorLevel = logLevel;

            String exceptionInfo = "printStackTrace err:";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = null;
            try {
                ps = new PrintStream(baos);
                throwable.printStackTrace(ps);
                ps.flush();
                baos.flush();
                exceptionInfo = baos.toString(); // 输出异常打印信息
            } catch (Exception e) {
                e.printStackTrace();
                exceptionInfo += e.toString();
            } finally {
                if (null != ps) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        exceptionInfo += "\nPrintStream close exception:" + e.toString();
                    }
                }
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    exceptionInfo += "\nByteArrayOutputStream close exception:" + e.toString();
                }
            }

            String threadName = "Unknown thread:";
            try {
                if (null != thread) {
                    threadName = thread.getName();
                } else {
                    threadName += "null";
                }
            } catch (Exception e) {
                e.printStackTrace();
                threadName += e.toString();
            }

            appendSimpleLog('T', threadName, "**********" + exceptionType + " exception **********" + "\n"
                    + exceptionInfo);
            /* 达到记录点 */
            // if (logLevel >= mPersistTriggerLevel || logStringCount >=
            // MAX_CACHE_COUNT) { // 有异常打印都立刻保存 20150120
            return persisteLog(true);
            // }
        }
        return false;
    }

    // 获取设备详情
    private static JSONObject getDeviceInformation() {
        if (Tracer.context == null) {
            return null;
        }

        DeviceInfo deviceInfo = new DeviceInfo();
        JSONObject devInfoJson = new JSONObject();
        deviceInfo.parseDeviceInfo(Tracer.context, devInfoJson);
        return devInfoJson;
    }

    private static String convertLevel(int level) {
        String levelString = null;
        switch (level) {
        case LOG_LEVEL_VERBOSE:
            levelString = LOG_TYPE_VERBOSE;
            break;
        case LOG_LEVEL_INFO:
            levelString = LOG_TYPE_INFO;
            break;
        case LOG_LEVEL_DEBUG:
            levelString = LOG_TYPE_DEBUG;
            break;
        case LOG_LEVEL_WARN:
            levelString = LOG_TYPE_WARN;
            break;
        case LOG_LEVEL_ERROR:
            levelString = LOG_TYPE_ERROR;
            break;
        case LOG_LEVEL_FATAL:
            levelString = LOG_TYPE_EXCEP;
            break;
        case LOG_LEVEL_UNCATCHED:
            levelString = LOG_TYPE_UNCATCHED;
            break;
        default:
            levelString = "unkown";
            break;
        }
        return levelString;
    }

    /**
     * 重置log缓冲
     */
    private static StringBuffer resetCache() {
        StringBuffer oldStringBuffer = logStringBuilder;
        logStringBuilder = new StringBuffer(MAX_CACHE_CAPACITY);
        logStringCount = 0;
        mErrorLevel = LOG_LEVEL_INFO;
        return oldStringBuffer;
    }

    // 简单防止多线程重复调用上报和保存文件
    private static AtomicBoolean persisting = new AtomicBoolean(false);

    /**
     * 将log日志持久化，并清空缓冲数据
     * 
     * @param report
     *            是否上报服务器
     * @param reportType
     *            上报类型
     * @param logName
     * @return true 开始上传；false 没有可上传的内容
     */
    public static synchronized boolean persisteLog(boolean report, String reportType, String logName) {
        boolean ret = false;
        if (persisting.get()) {
            return false;
        }

        persisting.set(true);
        /* 将cache交换出来，不上锁 */
        int errLevel = mErrorLevel;
        StringBuffer persBuffer = resetCache();
        if (persBuffer != null && persBuffer.length() > 10 && mOnMaxLogFrame != null) {
            if (mSaveFile || mReport) {
                String logStr = persBuffer.toString();

                if (mSaveFile) {
                    Log.d("Tracer", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx " + errLevel);
                    mOnMaxLogFrame.saveLogToFile(logStr);
                }

                if (report && mReport) {
                    mOnMaxLogFrame.reportToServer(reportType, logName, convertLevel(errLevel), logStr);
                    ret = true;
                }
            }
        } else {
            Log.d("Tracer", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx no up log! buf:" + persBuffer.toString());
        }
        persisting.set(false);

        return ret;
    }

    public static boolean persisteLog(boolean report) {
        return persisteLog(report, REPORT_TYPE_AUTO, "log" + System.currentTimeMillis());
    }

    /**
     * 将log添加到文件缓冲
     * 
     * @param level
     *            log级别简写
     * @param tag
     * @param msg
     */
    public static void appendSimpleLog(char level, String tag, String msg) {
        String timeStr = new SimpleDateFormat("HH:mm:ss").format(new Date());
        Log.e("cyTest", "33 logStringBuilder = "+logStringBuilder);
        logStringBuilder.append(String.format("%s %c\\%-20s\t%s\n", timeStr, level, tag, msg));
        logStringCount++;
        // if (logStringCount > MAX_FILEBUF_CAP) {
        // persisteLog();
        // }
    }

    /**
     * 把日志写入本地存储文件
     */
    // public static synchronized void flushToFile() {
    // String logs = logStringBuilder.toString();
    // mOnMaxLogFrame.saveLogToFile(logs);
    // logStringBuilder = new StringBuffer();
    // logStringCount = 0;
    // }

    /**
     * 把日志上传服务器
     */
    // public static void flushToServer() {
    // Log.d("Tracer", "flushToServer");
    // JSONArray toFlashArray = null;
    // if (logItemsArray.length() <= 0 || !mReport) {
    // Log.d("Tracer", "skipped !!");
    // return;
    // }
    //
    // try {
    // synchronized (logItemsArray) {
    // toFlashArray = Tracer.logItemsArray;
    // Tracer.logItemsArray = new JSONArray();
    // }
    //
    // final JSONObject logJson = new JSONObject();
    // logJson.put(LOG_TYPE, convertLevel(mErrorLevel));
    // logJson.put(DEVICE, getDeviceInformation()); // 获取设备详情
    // logJson.put(LOG_ITEMS, toFlashArray);
    //
    // String logString = logJson.toString();
    //
    // if (mOnMaxLogFrame != null) {
    // mOnMaxLogFrame.reportToServer(logString);
    // Log.d("Tracer", "flushToServer done !!");
    // }
    //
    // toFlashArray = null;
    // mErrorLevel = LOG_LEVEL_INFO;
    //
    // } catch (JSONException e) {
    // e.printStackTrace();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // if (null != toFlashArray) {
    // Tracer.logItemsArray.put(toFlashArray); // 保存上传失败的数据
    // }
    // }

    /*
     * public static void writeThowableInfo(Throwable throwable) { StringBuffer
     * excep = new StringBuffer(); excep.append(">>> " + throwable.getMessage()
     * + "\n"); StackTraceElement[] stackFrames = throwable.getStackTrace(); for
     * (int j = stackFrames.length - 1; j >= 0; j--) { String error =
     * stackFrames[j].getFileName() + ":" + stackFrames[j].getClassName() + "."
     * + stackFrames[j].getMethodName() + ":" + stackFrames[j].getLineNumber() +
     * "\n"; excep.append(error); } writeToFile(excep.toString()); }
     * 
     * private static final String PUSH_TAG = "cyTest";
     * 
     * public static void writeToFile(String logString) { Tracer.e(PUSH_TAG,
     * logString); // String logPath = mOnMaxLogFrame.getLogPath(); String str =
     * StringUtil.formatCurrDate("HH:mm:ss ") + logString + "\n"; String logPath
     * = getLogPath(); if (logPath != null) { FileWriter fos = null; try { if
     * (isSaveFile) { fos = new FileWriter(logPath, true); fos.write(str);
     * fos.flush(); // Tracer.v("cyTest", "flushTrace done"); } } catch
     * (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e)
     * { e.printStackTrace(); } finally { try { if (fos != null) fos.close(); //
     * Tracer.v("cyTest", "flushTrace close"); } catch (IOException e) {
     * e.printStackTrace(); } } } }
     */

    public static String getLogPath() {
        String dir = StorageUtils.getOwnCacheDirectory(context, "trace").getPath();
        return populatePath(dir, StringUtil.formatCurrDate("yyyy_MM_dd") + ".log");
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

    /*************************** sip 点对点专用 start *****************************/
    private static void showToast(final Context context, final String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void asyncReportSipX(final String urlString, final String logFilePath, final boolean toast) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                OutputStream output = null;
                DataInputStream in = null;

                try {
                    url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setChunkedStreamingMode(1024 * 1024);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("connection", "Keep-Alive");
                    conn.setRequestProperty("Charsert", "UTF-8");

                    File file = new File(logFilePath);

                    output = new DataOutputStream(conn.getOutputStream());
                    in = new DataInputStream(new FileInputStream(file));

                    String contentTag = "content=";
                    output.write(contentTag.getBytes());

                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        output.write(bufferOut, 0, bytes);
                    }
                    in.close();
                    output.flush();
                    output.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    Log.i("DialFragment", "上报成功");
                    if (toast) {
                        showToast(Tracer.context, "log report success");
                    }
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (toast) {
                        showToast(Tracer.context, "log report exeption : " + e.getMessage());
                    }
                } finally {
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
    /*************************** sip 点对点专用 end *****************************/
}
