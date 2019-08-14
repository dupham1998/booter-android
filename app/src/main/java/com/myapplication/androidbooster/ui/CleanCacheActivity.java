package com.myapplication.androidbooster.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.androidbooster.R;
import com.myapplication.androidbooster.binding.CleanCacheAdapter;
import com.myapplication.androidbooster.helper.Helper;
import com.myapplication.androidbooster.model.AppInfo;
import com.myapplication.androidbooster.service.CleanerCacheService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CleanCacheActivity extends AppCompatActivity implements CleanerCacheService.OnActionCleanListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private List<AppInfo> apps;
    private TextView mProgressBarText;
    private CleanerCacheService mCleanerCache;
    private CleanCacheAdapter adapter;

    private View mProgressBar;
    private View mProgressBarCleaning;
    private View mProgressBarCleanDone;

    private TextView mTextViewSizeCache;
    private TextView mTextViewSizeCacheClean;
    private TextView mTextViewSizeCacheCleaned;

    private Button mButtonClean;
    private int CacheSize = 0, CacheSizeClean = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache);
        findView();

        Log.d("test", "onCreate: clean cache activity" );
        apps = new ArrayList<>();
        mCleanerCache = new CleanerCacheService(this);
        mCleanerCache.setOnActionCleanListener(this);
        mCleanerCache.scanCache();

        setUpRecycleView();

        mButtonClean.setOnClickListener(this);
    }

    public void setUpRecycleView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_clean_cache);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        adapter = new CleanCacheAdapter();
        mRecyclerView.setAdapter(adapter);

        adapter.setAppInfoList(apps);
    }


    private void findView(){
        mProgressBarText = (TextView) findViewById(R.id.textview_package_name_cache);
        mProgressBar = findViewById(R.id.progressBar_cache);
        mTextViewSizeCache = (TextView) findViewById(R.id.textview_size_clean_cache);
        mProgressBarCleaning = findViewById(R.id.progressBar_cleaning);
        mProgressBarCleanDone = findViewById(R.id.progressBar_clean_done);
        mTextViewSizeCacheClean = (TextView) findViewById(R.id.textview_size_cache_clean);
        mTextViewSizeCacheCleaned = (TextView) findViewById(R.id.textview_size_cache_cleaned);
        mButtonClean = (Button) findViewById(R.id.button_clean_cache);
    }
    @Override
    public void onScanStarted() {
        mProgressBarText.setText("Scanning start ....");
        showProgressBar(true);
    }

    @Override
    public void onScanProgressUpdate(AppInfo app) {
        Log.d("test", "onScan update ");
        mProgressBarText.setText(app.getPackageName());
        CacheSize += app.getCacheSize();
        CacheSizeClean += app.getCacheSize();
    }

    @Override
    public void onScanCompleted(List<AppInfo> result) {
        Log.d("test", "onScan completed ");
        apps = result;
        Collections.sort(apps, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo o1, AppInfo o2) {
                return (o2.getCacheSize() - o1.getCacheSize());
            }
        });
        adapter.setAppInfoList(apps);
        mTextViewSizeCache.setText(Helper.formatSize(CacheSize));
        showProgressBar(false);
    }

    @Override
    public void onCleanStarted() {
        mTextViewSizeCacheClean.setText(Helper.formatSize(CacheSize));
        showProgressBarCleaning(true);
    }

    @Override
    public void onCleanProgressUpdate(AppInfo app) {
        CacheSizeClean -= app.getCacheSize();
        mTextViewSizeCacheClean.setText(Helper.formatSize(CacheSizeClean));
    }

    @Override
    public void onCleanCompleted() {
        showProgressBarCleaning(false);
        showProgressBarCleanDone(true);
        mTextViewSizeCacheCleaned.setText("Cleaned " + Helper.formatSize(CacheSize));
    }

    public void showProgressBar(boolean show){
        if(show){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else{
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void showProgressBarCleaning(boolean show){
        if(show){
            mProgressBarCleaning.setVisibility(View.VISIBLE);
        }
        else{
            mProgressBarCleaning.setVisibility(View.GONE);
        }
    }

    public void showProgressBarCleanDone(boolean show){
        if(show){
            mProgressBarCleanDone.setVisibility(View.VISIBLE);
        }
        else{
            mProgressBarCleanDone.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mButtonClean)){
            mCleanerCache.cleanCache();
        }
    }
}

