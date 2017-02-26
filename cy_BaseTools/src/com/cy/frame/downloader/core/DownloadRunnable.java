package com.cy.frame.downloader.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import android.content.Context;
import android.content.Intent;

import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.download.DownloadUtils;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.frame.downloader.downloadmanager.DownloadDB;
import com.cy.frame.downloader.downloadmanager.DownloadService;
import com.cy.frame.downloader.install.SelfDownloadService;
import com.cy.frame.downloader.util.DifferenceUtils;
import com.cy.global.BaseApplication;
import com.cy.utils.Utils;
import com.cy.utils.storage.FileUtils;
import com.cy.utils.storage.GNStorageUtils;

public class DownloadRunnable implements Runnable {
    private static final String TAG = "DownloadRunnable";

    // 读取流异常信息
    private static final String STREAM_IS_CLOSED = "BufferedInputStream is closed";
    private static final String STREAM_UNEXPECTED_END = "unexpected end of stream";

    // HttpURLConnection contentType html 类型 ， 这是客户端下载，如果类型为html则表示有异常
    private static final String CONTENT_TYPE_HTML = "text/html";

    private static final String LINE = "\n\t"; // 用于打印日志
    private static final int END_OF_FILE = -1; // 文件读到末尾的返回值
    private static final int NO_DATA = -2; // 写入文件的异常值
    private static final int BUFFER_SIZE = 10 * 1024;

    private static final int READ_TIMEOUT = 8000; // 读取流的超时时间
    private static final int CONNECT_TIMEOUT = 10000; // 重连超时时间

    /**
     * 安装提供的参数
     */
    public static final String EXTRA_DOWNLOAD_INFO = "downloadInfo";
    public static final String EXTRA_DOWNLOAD_HOME_DIR = "downloadHomeDir";

    private DownloadInfo mDownloadInfo;
    private File mDownloadFile;

    // 异常重连参数
    private static final int START_DOWNLOAD_TIMEOUT_RETRY_COUNT = 5;
    private static final int START_DOWNLOAD_RETRY_COUNT = 10;
    private static final int RETRY_COUNT = 4;
    private static final int RETRY_WAIT_TIME = 1000;
    private static final int MAX_SOCKET_TIMEOUT = 33;
    private int mTimeoutCount = 0;
    private int mHttpRetryCount = 0;
    private boolean mSizeRetryed = false;
    // 异常重连参数 end

    private boolean mExit = false;
    private String mHomeDir = Constant.EMPTY;

    public DownloadRunnable(DownloadInfo downloadInfo) {
        mDownloadInfo = downloadInfo;
    }

    @Override
    public void run() {
        if (!Utils.hasNetwork()) {
            pauseTask(DownloadStatusMgr.TASK_PAUSE_NO_NETWORK);
            return;
        }

        if (mDownloadInfo.isNewDownload()) {
            DownloadUrlUtils.getDownloadUrl(mDownloadInfo); // 如果是新下载，从服务器获取下载url
        }

        initFile();
        startDownload();
    }

    private void initFile() {
        mHomeDir = GNStorageUtils.getHomeDirAbsolute();
        if (null == mHomeDir) {
            pauseTask(DownloadStatusMgr.TASK_PAUSE_DEVICE_NOT_FOUND);
            return;
        }
        mDownloadFile = new File(mHomeDir + mDownloadInfo.mFilePath);
        mDownloadFile = new File(mDownloadInfo.mFilePath);
        if (!mDownloadFile.getParentFile().exists()) {
            FileUtils.mkdirs(mDownloadFile.getParentFile());
        }
        
        if (mDownloadFile.exists()) {
            long fileLength = mDownloadFile.length();
            if (fileLength > mDownloadInfo.mTotalSize) {
                mDownloadInfo.mProgress = 0;
                Utils.delAllfiles(mDownloadInfo.packageName);
            } else if (mDownloadInfo.mProgress != fileLength) {
                mDownloadInfo.mProgress = fileLength;
            }
        } else if (mDownloadInfo.mProgress > 0) {
            mDownloadInfo.mTotalSize = -1;
            mDownloadInfo.mProgress = 0;
        }
    }

    /**
     * 这里开始真正的下载apk
     */
    private void startDownload() {
        if (mExit) {
            return;
        }

        int exceptionType = DownloadStatusMgr.TASK_FAIL_REASON_NONE;
        HttpURLConnection connect = null;
        InputStream inputStream = null;
        BufferedInputStream bis = null;
        try {
            connect = initConnection();
            int code = connect.getResponseCode();
            // 当请求带有时setRequestProperty("Range", "bytes=" + mDownloadInfo.mProgress + "-");会返回 HTTP_PARTIAL
            if (code != HttpURLConnection.HTTP_OK && code != HttpURLConnection.HTTP_PARTIAL) {
                exceptionType = DownloadStatusMgr.TASK_FAIL_URL_UNREACHABLE;
                mHttpRetryCount++;
                retryDownload(exceptionType);
                return;
            }
            
            String contentType = connect.getContentType();
            if (null != contentType && contentType.equals(CONTENT_TYPE_HTML)) {
                onTaskFail(DownloadStatusMgr.TASK_FAIL_CONTENT_TYPE_ERROR);
                return;
            }
            
            int contentLength = connect.getContentLength();
            if (!checkContentLength(contentLength)) {
            	pauseTask(DownloadStatusMgr.TASK_PAUSE_WIFI_INVALID);
                return;
            }
            
            if (mDownloadInfo.isNewDownload()) {
                long totalSize = contentLength + mDownloadInfo.mProgress;
                float gameSize = Float.valueOf(Utils.trimSize(mDownloadInfo.size)) * Constant.MB;
                // 这里的逻辑是不是有问题?? 上传的包如果没有及时被客户端获取，不存在应该也是正常得吧??
                if (Math.abs(gameSize - totalSize) > Constant.MB && !mSizeRetryed) {
                    mSizeRetryed = true;
                    retryDownload(DownloadStatusMgr.TASK_FAIL_APK_ERROR);
                    return;
                }
                mDownloadInfo.mTotalSize = totalSize;
                mDownloadInfo.downUrl = connect.getURL().toString(); // 这是重定向后的地址的么?? 跟重新获取的那个url有什么区别
                if (!mExit) {
                    getDownloadInfoMgr().updateDownloadInfo(mDownloadInfo);
                }
            }
            inputStream = connect.getInputStream();
            bis = new BufferedInputStream(inputStream);

            writeFile(mDownloadFile, bis);
        } catch (MalformedURLException e) {
            onUrlError(DownloadStatusMgr.TASK_FAIL_URL_ERROR);
        } catch (SocketTimeoutException e) {
            mTimeoutCount++;
            exceptionType = DownloadStatusMgr.TASK_FAIL_URL_UNRECOVERABLE;
        } catch (IOException e) {
            mHttpRetryCount++;
            exceptionType = DownloadStatusMgr.TASK_FAIL_URL_UNRECOVERABLE;
        } catch (Exception e) {
            failLogFile(getExceptionInfo(e));
            mHttpRetryCount++;
            exceptionType = DownloadStatusMgr.TASK_FAIL_UNKNOWN;
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
            if (connect != null) {
                connect.disconnect();
            }
        }

        if (exceptionType != DownloadStatusMgr.TASK_FAIL_REASON_NONE) {
            retryDownload(exceptionType);
        }
    }

    protected DownloadInfoMgr getDownloadInfoMgr() {
        return DownloadInfoMgr.getNormalInstance();
    }

    protected DownloadStatusMgr getDownloadStatusMgr() {
        return DownloadStatusMgr.getNormalInstance();
    }

    private void retryDownload(int exceptionType) {
        if (!Utils.hasNetwork() || mTimeoutCount > START_DOWNLOAD_TIMEOUT_RETRY_COUNT) {
            pauseTask(DownloadStatusMgr.TASK_PAUSE_NO_NETWORK);
            return;
        }

        if (mHttpRetryCount > START_DOWNLOAD_RETRY_COUNT) {
            onTaskFail(exceptionType);
            return;
        }

        startDownload();
    }

    private HttpURLConnection initConnection() throws IOException {
        HttpURLConnection connection;
        URL url = new URL(mDownloadInfo.downUrl);
        connection = (HttpURLConnection) url.openConnection();
        if (mDownloadInfo.mProgress > 0) {
            connection.setRequestProperty("Range", "bytes=" + mDownloadInfo.mProgress + "-"); // 设置当前已下载的字节数
        }
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Language", "zh-CN");
        connection.setRequestProperty("Referer", url.toString());
        connection.setRequestProperty("Charset", Constant.UTF_8);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Accept-Encoding", "identity");
        return connection;
    }

    private void writeFile(File file, BufferedInputStream bis) throws IOException {
        RandomAccessFile out = null;
        try {
            out = new RandomAccessFile(file, "rw");
            out.seek(mDownloadInfo.mProgress >= 0 ? mDownloadInfo.mProgress : 0);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = NO_DATA;
            while (!mExit) {
                if (!file.exists()) {
                    pauseTask(DownloadStatusMgr.TASK_PAUSE_FILE_ERROR);
                    break;
                }
                if (len != NO_DATA) {
                    out.write(buffer, 0, len);
                    mDownloadInfo.mProgress += len;
                    if (mExit) {
                        break;
                    } else {
                        getDownloadInfoMgr().updateProgress(mDownloadInfo); // 更新进度
                    }
                }

                len = fillBuffer(bis, buffer);
                if (len == END_OF_FILE) {
                    if (DownloadUtils.isFileInvilid(mHomeDir, mDownloadInfo)) {
                        onTaskFail(DownloadStatusMgr.TASK_FAIL_APK_ERROR);
                    } else {
                        onTaskSucc();
                    }
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            pauseTask(DownloadStatusMgr.TASK_PAUSE_FILE_ERROR);
        } catch (IOException e) {
            if (Utils.isSDCardLowSpace()) {
                pauseTask(DownloadStatusMgr.TASK_PAUSE_INSUFFICIENT_SPACE);
            } else {
                pauseTask(DownloadStatusMgr.TASK_PAUSE_FILE_ERROR);
            }
        } catch (Exception e) {
            onTaskFail(DownloadStatusMgr.TASK_FAIL_UNKNOWN);
            failLogFile(getExceptionInfo(e));
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private int fillBuffer(BufferedInputStream bis, byte[] buffer) throws InterruptedException {
        int len = NO_DATA;
        String msg = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            try {
                len = bis.read(buffer);
                mTimeoutCount = 0;
                break;
            } catch (SocketTimeoutException e) {
                mTimeoutCount++;
            } catch (IOException e) {
                msg = e.getLocalizedMessage();
                synchronized (mDownloadInfo) {
                    mDownloadInfo.wait(RETRY_WAIT_TIME);
                }
            }
        }
        if (needReconnect(msg, mTimeoutCount)) {
            reconnect();
        }
        return len;
    }

    private boolean needReconnect(String msg, int timeoutCount) {
        if (mExit) {
            return false;
        }

        if (timeoutCount > MAX_SOCKET_TIMEOUT) {
            if (!Utils.hasNetwork()) {
                pauseTask(DownloadStatusMgr.TASK_PAUSE_NO_NETWORK);
                return false;
            } else {
                return true;
            }
        }

        return msg != null && (msg.contains(STREAM_IS_CLOSED) || msg.contains(STREAM_UNEXPECTED_END)) // 读取流异常
                && Utils.hasNetwork();
    }

    private void reconnect() {
        if (!DifferenceUtils.isFlowTipConfirmed()) {
            pauseTask(DownloadStatusMgr.TASK_PAUSE_NO_NETWORK);
            return;
        }

        mExit = true;
        DownloadService.postTask(mDownloadInfo.packageName, new DownloadRunnable(mDownloadInfo));
    }

    private boolean checkContentLength(int contentLength) {
    	// 以前的逻辑中有这个方法 DownloadUtils.isWifiInvalid()用于判断wifi是否被重定向了。不过应该从返回的大小跟已存在的文件大小就可以判断是否异常了。
        if (contentLength >= DownloadUtils.MIN_APK_SIZE) {
        	return true;
        }
        
        if(mDownloadInfo.mTotalSize > DownloadUtils.MIN_APK_SIZE && mDownloadInfo.mTotalSize - mDownloadInfo.mProgress < DownloadUtils.MIN_APK_SIZE) {
        	return true;
        }
        
        return false;
    }

    protected void onTaskSucc() {
        handelSelfDownload(mDownloadInfo);

        DownloadUtils.onDownloadSucc(mDownloadInfo);
        onExit(DownloadStatusMgr.TASK_STATUS_SUCCESSFUL, DownloadStatusMgr.TASK_FAIL_REASON_NONE);
    }

    protected void handelSelfDownload(DownloadInfo downloadInfo) {
        startInstall(downloadInfo);
        ButtonStatusManager.addDownloaded(mDownloadInfo.packageName);
    }

    protected void startInstall(DownloadInfo downloadInfo) {
        Context context = BaseApplication.getAppContext();
        Intent installService = new Intent(context, SelfDownloadService.class);
        installService.putExtra(DownloadRunnable.EXTRA_DOWNLOAD_INFO, downloadInfo);
        installService.putExtra(DownloadRunnable.EXTRA_DOWNLOAD_HOME_DIR, mHomeDir);
        installService.setPackage(Utils.getClientPkg());
        context.startService(installService);
    }

    protected void onTaskFail(int reason) {
        onExit(DownloadStatusMgr.TASK_STATUS_FAILED, reason);
    }

    private void onUrlError(int reason) {
        onExit(DownloadStatusMgr.TASK_STATUS_FAILED, reason);
    }

    private void pauseTask(int reason) {
        onExit(DownloadStatusMgr.TASK_STATUS_PAUSED, reason);
    }

    protected void onExit(int status, int reason) {
        if (reason != DownloadStatusMgr.TASK_FAIL_UNKNOWN && reason != DownloadStatusMgr.TASK_FAIL_REASON_NONE) {
            failLogFile(DownloadUtils.reasonToString(reason));
        }

        if (mExit) {
            return;
        }
        
        mExit = true;
        getDownloadStatusMgr().onDownloadingExit(mDownloadInfo.packageName, status, reason);
        DownloadDB.getInstance().tryCloseDB();
    }

    private void failLogFile(String reason) {
        reason = reason.replace(LINE, Constant.EMPTY);
        String log = mDownloadInfo.packageName + Constant.COMBINE_SYMBOL_2 + mDownloadInfo.downUrl
                + Constant.COMBINE_SYMBOL_2 + System.currentTimeMillis() + Constant.COMBINE_SYMBOL_2 + reason;
        DownloadUtils.saveLastFailLog(log);
    }

    private static String getExceptionInfo(Exception ex) {
        Writer errorWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(errorWriter);
        ex.printStackTrace(pw);
        pw.close();
        return errorWriter.toString();
    }
    
    public void exit(int targetStatus) {
        mExit = true;
        mDownloadInfo.mStatus = targetStatus;
        synchronized (mDownloadInfo) {
            mDownloadInfo.notifyAll(); //?? 这个唤醒的是什么鬼
        }
    }

}
