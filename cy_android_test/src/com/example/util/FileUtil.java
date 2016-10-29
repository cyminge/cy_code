package com.example.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static boolean setLastModified(File file, long time) {
        return file.setLastModified(time);
    }

    public static boolean renameTo(File file, File aimFile) {
        return file.renameTo(aimFile);
    }

    public static boolean mkdirs(File file) {
        return file.mkdirs();
    }

    public static boolean createNewFile(File file) throws IOException {
        return file.createNewFile();
    }





}

