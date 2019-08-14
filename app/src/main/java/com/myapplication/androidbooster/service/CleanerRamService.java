package com.myapplication.androidbooster.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;

import com.myapplication.androidbooster.model.AppRunningInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CleanerRamService {
    private Context mContext;
    private OnActionCleanRamListener listener;

    public CleanerRamService(Context context){
        this.mContext = context;
    }

    public interface  OnActionCleanRamListener{
        public void onScanStarted();
        public void onScanProgressUpdate(AppRunningInfo app);
        public void onScanCompleted(List<AppRunningInfo> apps);

        public void onCleanStarted();
        public void onCleanProgressUpdate(AppRunningInfo app);
        public void onCleanCompleted();
    }

    public void scan(){
        new ScanTask().execute();
    }

    public void setOnActionCleanRamListener(OnActionCleanRamListener listener){
        this.listener = listener;
    }
    public class ScanTask extends AsyncTask<Void, AppRunningInfo, List<AppRunningInfo>>{

        @Override
        protected void onPreExecute() {
            if(listener != null){
                listener.onScanStarted();
            }
        }

        @Override
        protected List<AppRunningInfo> doInBackground(Void... voids) {

            ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            final List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                    .getRunningServices(Integer.MAX_VALUE);
            final List<AppRunningInfo> apps = new ArrayList<AppRunningInfo>();
            final CountDownLatch countDownLatch = new CountDownLatch(serviceList.size());

            for(ActivityManager.RunningServiceInfo service : serviceList) {

                        int[] _pid = new int[1];
                        _pid[0] = service.pid;
                        Debug.MemoryInfo[] processMemInfo = activityManager.getProcessMemoryInfo(_pid);

                        if (processMemInfo[0].getTotalPss() > 0) {

                            try {
                                PackageManager pm = mContext.getPackageManager();
                                ApplicationInfo applicationInfo = pm.getApplicationInfo(service.process, 0);

                                String packageName = service.process;
                                int pid = service.pid;
                                int ram = processMemInfo[0].getTotalPss();
                                String applicationName = (String) pm.getApplicationLabel(applicationInfo);
                                Drawable icon = mContext.getPackageManager().getApplicationIcon(service.process);

                                AppRunningInfo app = new AppRunningInfo(packageName, (String) pm.getApplicationLabel(applicationInfo), icon, pid, ram);
                                apps.add(app);
                                publishProgress(app);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    }

            return new ArrayList<>(apps);
        }

        @Override
        protected void onProgressUpdate(AppRunningInfo... values) {
            if(listener != null)  listener.onScanProgressUpdate(values[0]);
        }

        @Override
        protected void onPostExecute(List<AppRunningInfo> result) {
            if(listener != null) listener.onScanCompleted(result);
        }

    }

    public class CleanTask extends AsyncTask<List<AppRunningInfo>, AppRunningInfo, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(List<AppRunningInfo>... lists) {
            return null;
        }


        @Override
        protected void onProgressUpdate(AppRunningInfo... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }
}
