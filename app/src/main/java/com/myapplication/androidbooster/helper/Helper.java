package com.myapplication.androidbooster.helper;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;

import com.myapplication.androidbooster.model.AppInfo;

import java.io.File;
import java.util.Locale;

import static android.content.Context.ACTIVITY_SERVICE;

public class Helper {
    public  static final int  M = 1000;

    public static String formatSize(int size){
        String result = "";

        if(size < 1024){
            result = String.format(Locale.CANADA, "%d B", size);
        }
        else if(size < M * M){
            result = String.format(Locale.CANADA, "%.2f KB", size/(float)(M));
        }
        else if(size < M * M * M){
            result = String.format(Locale.CANADA, "%.2f MB", size/(float)(M * M));
        }
        else {
            result = String.format(Locale.CANADA, "0.2f GB", size/(float)(M * M * M));
        }
        return result;
    }

    public static String formatSizeRam(int size){
        String result = "";

        if(size < 1024){
            result = String.format(Locale.CANADA, "%d KB", size);
        }
        else if(size < M * M){
            result = String.format(Locale.CANADA, "%.2f MB", size/(float)(M));
        }
        else if(size < M * M * M){
            result = String.format(Locale.CANADA, "%.2f GB", size/(float)(M * M));
        }

        return result;
    }




    public static long getAvailableRomSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize /  1048576L;
    }

    public static long getTotalRomSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize /  1048576L;
    }

    public static long getAvailableSdcardSize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize /  1048576L;
        } else {
            return 0;
        }
    }

    public static long getTotalSdcardSize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize /  1048576L;
        } else {
            return 0;
        }
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
}
