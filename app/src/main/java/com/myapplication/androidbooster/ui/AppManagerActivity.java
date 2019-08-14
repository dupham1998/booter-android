package com.myapplication.androidbooster.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.androidbooster.R;
import com.myapplication.androidbooster.binding.AppManagerAdapter;
import com.myapplication.androidbooster.helper.Helper;
import com.myapplication.androidbooster.model.AppInfo;
import com.myapplication.androidbooster.service.ScanApp;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener, ScanApp.OnActionListener {
    private RecyclerView mRecyclerView;
    private List<AppInfo> appInfoList;
    private List<AppInfo> appUninstall;
    private PackageManager pm;
    private Button mButtonUninstall;
    private Method getPackageSizeInfo;
    private  AppManagerAdapter adapter;
    private int SizeUninstall = 0;
    private AppInfo appInfo;
    private boolean flagUninstall = false;
    private RadioGroup mRadioGroup;
    private View mProgressBar;
    private TextView mProgressBarText;
    private ScanApp mScan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        findView();
        pm = getPackageManager();
        appUninstall = new ArrayList<>();
        appInfoList = new ArrayList<>();

        mScan = new ScanApp(this);
        mScan.setOnActionListener(this);
        mScan.scan();

        setUpRecycleView();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton_name:
                        SizeUninstall = 0;
                        mButtonUninstall.setText("Uninstall");
                        Collections.sort(appInfoList, new Comparator<AppInfo>() {
                            @Override
                            public int compare(AppInfo o1, AppInfo o2) {
                                return o1.getAppName().toLowerCase().compareTo(o2.getAppName().toLowerCase());
                            }
                        });
                        adapter.setAppInfoList(appInfoList);
                        break;
                    case R.id.radioButton_size:
                        SizeUninstall = 0;
                        mButtonUninstall.setText("Uninstall");
                        Collections.sort(appInfoList, new Comparator<AppInfo>() {
                            @Override
                            public int compare(AppInfo o1, AppInfo o2) {
                                return (o2.getCacheSize() - o1.getCacheSize());
                            }
                        });
                        adapter.setAppInfoList(appInfoList);
                        break;
                }
            }
        });
    }

    private void findView(){
        mButtonUninstall = (Button) findViewById(R.id.button_uninstall);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group_sort);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBarText = (TextView) findViewById(R.id.textview_progressbar);
    }

    private void setUpRecycleView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_app_manager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        adapter = new AppManagerAdapter(this);
        mRecyclerView.setAdapter(adapter);

        adapter.setAppInfoList(appInfoList);
        adapter.setOnItemClickListener(new AppManagerAdapter.OnItemClickListener() {
            @Override
            public void checkBoxItem(AppInfo app) {
                SizeUninstall +=  app.getCacheSize() + app.getDataSize() + app.getAppSize();
                mButtonUninstall.setText("Uninstall " + Helper.formatSize(SizeUninstall));
                appUninstall.add(app);
            }

            @Override
            public void unCheckBoxItem(AppInfo app) {
                SizeUninstall -=  app.getCacheSize() + app.getDataSize() + app.getAppSize();
                if(SizeUninstall == 0) mButtonUninstall.setText("Uninstall");
                else mButtonUninstall.setText("Uninstall " + Helper.formatSize(SizeUninstall));
                appUninstall.remove(app);
            }

            @Override
            public void onItemClick(AppInfo app) {
                flagUninstall = true;
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + app.getPackageName()));
                startActivity(intent);
                appInfo = app;
            }
        });
        mButtonUninstall.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.equals(mButtonUninstall)){
            for(AppInfo app : appUninstall){
                appInfo = app;
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + app.getPackageName()));
                startActivity(intent);
            }
        }
    }

    private boolean isPackageInstalled(String packagename) {
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(appUninstall != null ) {
            for (AppInfo app : appUninstall) {
                if (!isPackageInstalled(app.getPackageName()))
                    appInfoList.remove(app);
            }
            adapter.setAppInfoList(appInfoList);
            mButtonUninstall.setText("Uninstall");
        }
        if(flagUninstall){
            if(!isPackageInstalled(appInfo.getPackageName()))
                appInfoList.remove(appInfo);
            adapter.setAppInfoList(appInfoList);
            flagUninstall = false;
        }
    }


    @Override
    public void onScanStarted() {
        mProgressBarText.setText("scanning start ....");
        showProgressBar(true);
    }

    @Override
    public void onScanProgressUpdate(AppInfo app) {
        mProgressBarText.setText(app.getPackageName());
    }

    @Override
    public void onScanCompeleted(List<AppInfo> apps) {
        appInfoList = apps;
        adapter.setAppInfoList(appInfoList);
        showProgressBar(false);
    }

    public void showProgressBar(boolean show){
        if(show){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
