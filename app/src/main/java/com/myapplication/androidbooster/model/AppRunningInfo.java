package com.myapplication.androidbooster.model;

import android.graphics.drawable.Drawable;

public class AppRunningInfo extends AppInfo{
    private int pid;
    private int ramSize;

    public AppRunningInfo(String packageName, String appName, Drawable icon, int pid, int ramSize) {
        super(packageName, appName, icon);
        this.pid = pid;
        this.ramSize = ramSize;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getRamSize() {
        return ramSize;
    }

    public void setRamSize(int ramSize) {
        this.ramSize = ramSize;
    }
}
