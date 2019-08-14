package com.myapplication.androidbooster.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.myapplication.androidbooster.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonJunkFiles;
    private Button mButtonCpuCooler;
    private Button mButtonAppManager;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        setUpActionBar();


        mButtonCpuCooler.setOnClickListener(this);
        mButtonAppManager.setOnClickListener(this);
        mButtonJunkFiles.setOnClickListener(this);
    }

    private void findView(){
        mButtonJunkFiles = (Button) findViewById(R.id.button_junk_file);
        mButtonAppManager = (Button) findViewById(R.id.button_app_manager);
        mButtonCpuCooler = (Button) findViewById(R.id.button_cpu_cooler);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
    }

    public void setUpActionBar(){
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_device_status:
                        Toast.makeText(getApplication(), "navigation device status", Toast.LENGTH_SHORT).show();
                    case R.id.nav_about:
                        Toast.makeText(getApplication(), "navigation settings", Toast.LENGTH_SHORT).show();
                    case R.id.nav_setting:
                        Toast.makeText(getApplication(), "navigation about", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
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
