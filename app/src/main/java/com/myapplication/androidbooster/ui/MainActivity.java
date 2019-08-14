package com.myapplication.androidbooster.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.myapplication.androidbooster.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonJunkFiles;
    private Button mButtonCpuCooler;
    private Button mButtonAppManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();

        mButtonCpuCooler.setOnClickListener(this);
        mButtonAppManager.setOnClickListener(this);
        mButtonJunkFiles.setOnClickListener(this);
    }

    private void findView(){
        mButtonJunkFiles = (Button) findViewById(R.id.button_junk_file);
        mButtonAppManager = (Button) findViewById(R.id.button_app_manager);
        mButtonCpuCooler = (Button) findViewById(R.id.button_cpu_cooler);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mButtonAppManager)){
            Intent myIntent = new Intent(MainActivity.this, AppManagerActivity.class);
            this.startActivity(myIntent);
        }
        if(v.equals(mButtonCpuCooler)){
            Intent myIntent = new Intent(MainActivity.this, CleanRamActivity.class);
            this.startActivity(myIntent);
        }
        if(v.equals(mButtonJunkFiles)){
            Toast.makeText(this, "click junk file", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(MainActivity.this, CleanCacheActivity.class);
            this.startActivity(myIntent);
        }
    }
}
