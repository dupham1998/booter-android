package com.myapplication.androidbooster.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.androidbooster.R;
import com.myapplication.androidbooster.binding.CleanRamAdapter;
import com.myapplication.androidbooster.model.AppRunningInfo;
import com.myapplication.androidbooster.service.CleanerRamService;

import java.util.ArrayList;
import java.util.List;

public class CleanRamActivity  extends AppCompatActivity implements CleanerRamService.OnActionCleanRamListener {

    private RecyclerView mRecyclerView;
    private List<AppRunningInfo> apps;
    private CleanerRamService mCleanerRamService;
    private CleanRamAdapter adapter;
    private View mProgressBarScanning;
    private TextView mTextViewPackageScan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_ram);
        findView();

        apps = new ArrayList<>();
        mCleanerRamService = new CleanerRamService(this);
        mCleanerRamService.setOnActionCleanRamListener(this);
        mCleanerRamService.scan();

        setUpRecyclerView();
    }

    public void setUpRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_clean_ram);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        adapter  = new CleanRamAdapter();
        mRecyclerView.setAdapter(adapter);

        adapter.setApps(apps);
    }

    public void findView(){
        mProgressBarScanning = findViewById(R.id.progressBar_ram_scan);
        mTextViewPackageScan = (TextView) findViewById(R.id.textview_size_ram);
    }
    @Override
    public void onScanStarted() {
        showProgressScan(true);
    }

    @Override
    public void onScanProgressUpdate(AppRunningInfo app) {
        mTextViewPackageScan.setText(app.getAppName());
    }

    @Override
    public void onScanCompleted(List<AppRunningInfo> result) {
        apps = result;
        adapter.setApps(apps);
        showProgressScan(false);
    }

    @Override
    public void onCleanStarted() {

    }

    @Override
    public void onCleanProgressUpdate(AppRunningInfo app) {

    }

    @Override
    public void onCleanCompleted() {

    }

    public void showProgressScan(boolean show){
        if(show){
            mProgressBarScanning.setVisibility(View.VISIBLE);
        }
        else {
            mProgressBarScanning.setVisibility(View.GONE);
        }
    }
}
