package com.myapplication.androidbooster.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import com.myapplication.androidbooster.model.AppInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static android.content.ContentValues.TAG;

public class CleanerCacheService {

    private Method  getPackageSizeInfoMethod;
    private Context mContext;
    private OnActionCleanListener listener;
    public CleanerCacheService(Context mContext){
        this.mContext = mContext;
        PackageManager packageManager = mContext.getPackageManager();
        try {
            getPackageSizeInfoMethod = packageManager.getClass().getMethod(
                    "getPackageSizeInfo", String.class, IPackageStatsObserver.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public interface  OnActionCleanListener{
        public void onScanStarted();
        public void onScanProgressUpdate(AppInfo app);
        public void onScanCompleted(List<AppInfo> apps);

        public void onCleanStarted();
        public void onCleanProgressUpdate(AppInfo app);
        public void onCleanCompleted();
    }

    public void setOnActionCleanListener(OnActionCleanListener listener){
        this.listener = listener;
    }

    public void scanCache (){
        new ScanTask().execute();
    }

    public void cleanCache(){
        new CleanTask().execute();
    }

    private class ScanTask extends AsyncTask<Void, AppInfo, List<AppInfo>> {

        @Override
        protected void onPreExecute() {
            if(listener != null){
                listener.onScanStarted();
            }
        }

        @Override
        protected List<AppInfo> doInBackground(Void... voids) {

            PackageManager pm = mContext.getPackageManager();
            final List<ApplicationInfo> packages = pm.getInstalledApplications(
                    PackageManager.GET_META_DATA);
            final CountDownLatch countDownLatch = new CountDownLatch(packages.size());
            final List<AppInfo> apps = new ArrayList<AppInfo>();
            try {
                for (final ApplicationInfo pkg : packages) {
                    getPackageSizeInfoMethod.invoke(pm, pkg.packageName,
                            new IPackageStatsObserver.Stub() {

                                @Override
                                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                                        throws RemoteException {
                                    synchronized (apps) {
                                      if(pStats.cacheSize > 0){
                                          Drawable icon = null;
                                          try {
                                              icon = mContext.getPackageManager().
                                                      getApplicationIcon(pkg.packageName);
                                          } catch (PackageManager.NameNotFoundException e) {
                                              e.printStackTrace();
                                          }

                                          AppInfo app = new AppInfo(pkg.packageName,
                                                  (String) mContext.getPackageManager().
                                                          getApplicationLabel(pkg),
                                                  (int) (pStats.cacheSize + pStats.externalCacheSize),
                                                  (int) (pStats.dataSize + pStats.externalDataSize),
                                                  (int) (pStats.codeSize + pStats.externalCodeSize),
                                                  icon );

                                          apps.add(app);
                                          publishProgress(app);
                                      }
                                    }

                                    synchronized (countDownLatch) {
                                        countDownLatch.countDown();
                                    }
                                }
                            }
                    );
                }

                countDownLatch.await();
            } catch (InvocationTargetException | InterruptedException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return new ArrayList<>(apps);
        }

        @Override
        protected void onProgressUpdate(AppInfo... values) {
            if(listener != null){
                listener.onScanProgressUpdate(values[0]);
            }
        }

        @Override
        protected void onPostExecute(List<AppInfo> results) {
            if(listener != null){
                listener.onScanCompleted(results);
            }
        }

    }

    private class CleanTask extends AsyncTask<Void, AppInfo, Void>{

        @Override
        protected void onPreExecute() {
            if(listener != null){
                listener.onCleanStarted();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            PackageManager pm = mContext.getPackageManager();
            final List<ApplicationInfo> packages = pm.getInstalledApplications(
                    PackageManager.GET_META_DATA);
            final CountDownLatch countDownLatch = new CountDownLatch(packages.size());
            final List<AppInfo> apps = new ArrayList<AppInfo>();
            try {
                for (final ApplicationInfo pkg : packages) {
                    getPackageSizeInfoMethod.invoke(pm, pkg.packageName,
                            new IPackageStatsObserver.Stub() {

                                @Override
                                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                                        throws RemoteException {
                                    synchronized (apps) {
                                        if(pStats.cacheSize > 0){
                                            Drawable icon = null;
                                            try {
                                                icon = mContext.getPackageManager().
                                                        getApplicationIcon(pkg.packageName);
                                            } catch (PackageManager.NameNotFoundException e) {
                                                e.printStackTrace();
                                            }

                                            AppInfo app = new AppInfo(pkg.packageName,
                                                    (String) mContext.getPackageManager().
                                                            getApplicationLabel(pkg),
                                                    (int) (pStats.cacheSize + pStats.externalCacheSize),
                                                    (int) (pStats.dataSize + pStats.externalDataSize),
                                                    (int) (pStats.codeSize + pStats.externalCodeSize),
                                                    icon );

                                            apps.add(app);
                                            publishProgress(app);
                                        }
                                    }

                                    synchronized (countDownLatch) {
                                        countDownLatch.countDown();
                                    }
                                }
                            }
                    );
                }

                countDownLatch.await();
            } catch (InvocationTargetException | InterruptedException | IllegalAccessException e) {
                e.printStackTrace();
            }

           return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            listener.onCleanCompleted();
        }

        @Override
        protected void onProgressUpdate(AppInfo... values) {
            listener.onCleanProgressUpdate(values[0]);
        }
    }
}
