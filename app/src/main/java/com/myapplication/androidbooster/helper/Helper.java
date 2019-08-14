package com.myapplication.androidbooster.helper;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.myapplication.androidbooster.model.AppInfo;

import java.util.Locale;

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
}
