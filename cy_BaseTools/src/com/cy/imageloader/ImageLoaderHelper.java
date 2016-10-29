package com.cy.imageloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;

import com.cy.constant.Constant;
import com.cy.tracer.Tracer;
import com.cy.utils.StringUtil;
import com.cy.utils.storage.FileUtils;
import com.cy.utils.storage.StorageUtils;

public class ImageLoaderHelper {
    
    private static final String TAG = "ImageLoaderHelper";

    private static ArrayList<String> mInHandUrl = new ArrayList<String>(); // 正在下载中的图片
    private static final int TIME_OUT = 8000;

    /**
     * 下载网络图片
     * @param context
     * @param url
     * @return
     */
    public static File downloadImage(Context context, String url) {
        // if (!StorageUtils.isSDCardMounted()) {
        // return null;
        // }

        if (TextUtils.isEmpty(url) || !StringUtil.isUrlValid(url)) {
            return null;
        }

        // 要检测网络类型 ??????

        // 是否在运营商网络下下载图片
        // if (SettingUtils.isHidePhoto() && isSDCardStoredUrl(url)) {
        // return null;
        // }
        // if (!Utils.hasNetwork()) {
        // return null;
        // }

        if (mInHandUrl.contains(url)) {
            return null;
        }

        addInHandUrl(url);

        File downFile = new File(StorageUtils.getCacheDirectory(context) + "/" + ImageLoader.hashKeyForDisk(url) + Constant.TMP_FILE_EXT);
        if (downFile.exists()) {
            FileUtils.delete(downFile);
        }
        
//        try {
//            downFile.createNewFile();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }

        HttpURLConnection con = null;
        BufferedInputStream bis = null;
        FileOutputStream ops = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            // con.setRequestMethod("GET");
            // con.setRequestProperty("Accept-Encoding", "gzip");
            con.setConnectTimeout(TIME_OUT);
            // if ("gzip".equals(con.getContentEncoding())) {
            // bis = new BufferedInputStream(new
            // GZIPInputStream(con.getInputStream()));
            // } else {
            bis = new BufferedInputStream(con.getInputStream());
            // }
            ops = new FileOutputStream(downFile);
            byte[] buffer = new byte[8 * 1024];
            int size;
            while ((size = bis.read(buffer)) != -1) {
                ops.write(buffer, 0, size);
            }
            ops.flush();
//            File targetFile = new File(StorageUtils.getCacheDirectory(context) + "/" + ImageLoader.hashKeyForDisk(url));
//            FileUtils.renameTo(downFile, targetFile);
            return downFile;
        } catch (Exception e) {
            e.printStackTrace();
            Tracer.e(TAG, "download image error");
            return null;
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException ioe) {
            }
            try {
                if (ops != null) {
                    ops.close();
                }
            } catch (IOException ioe) {
            }
            if (con != null) {
                con.disconnect();
            }
            mInHandUrl.remove(url);
        }
    }

    private static void addInHandUrl(final String url) {
        mInHandUrl.add(url);
    }

}
