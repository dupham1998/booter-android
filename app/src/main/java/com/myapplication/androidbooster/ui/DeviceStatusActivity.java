package com.myapplication.androidbooster.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myapplication.androidbooster.R;
import com.myapplication.androidbooster.helper.Helper;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import static android.os.Build.*;

public class DeviceStatusActivity extends AppCompatActivity {


    private final String TAG = "about device";

    private TextView mTextViewOsVersion;
    private TextView mTextViewRootState;
    private TextView mTextViewFirmware;
    private TextView mTextViewNameDevice;
    private TextView mTextViewCpuHardware;
    private TextView mTextViewCpuModel;
    private TextView mTextViewCores;
    private TextView mTextViewFrequency;
    private TextView mTextViewRamInfo;
    private TextView mTextViewRomInfo;
    private TextView mTextViewSdcardInfo;
    private TextView mTextViewScreenResolution;
    private TextView mTextViewScreenDensity;
    private TextView mTextViewScreenTouch;
    private TextView mTextViewAccelrometerSensor;
    private TextView mTextViewMagneticSensor;
    private TextView mTextViewOrientationSensor;
    private TextView mTextViewGyroscopeSensor;
    private TextView mTextViewDistanceSensor;
    private TextView mTextViewLightSensor;
    private TextView mTextViewTemperatureSensor;
    private ProgressBar mProgressBarCpu;
    private ProgressBar mProgressBarRom;
    private ProgressBar mPrgressBarRam;

    private Map<String, String> CpuInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_status);
        findView();
        CpuInfo = getCpuInfoMap();

        setText();
    }

    private void findView(){
        mTextViewFirmware = findViewById(R.id.textview_firmware);
        mTextViewOsVersion = findViewById(R.id.textview_os_version);
        mTextViewRootState = findViewById(R.id.textview_root_state);
        mTextViewNameDevice = findViewById(R.id.textview_name_device);
        mTextViewFrequency = findViewById(R.id.textview_cpu_frequency);
        mTextViewCores = findViewById(R.id.textview_cpu_cores);
        mTextViewCpuHardware = findViewById(R.id.textview_cpu_hardware);
        mTextViewCpuModel = findViewById(R.id.textview_arch_cpu);
        mTextViewRamInfo = findViewById(R.id.textview_info_ram);
        mTextViewRomInfo = findViewById(R.id.textview_info_rom);
        mTextViewSdcardInfo = findViewById(R.id.textview_info_sdcard);
        mTextViewScreenResolution  = findViewById(R.id.textview_screen_resolution);
        mTextViewScreenDensity = findViewById(R.id.textview_screen_density);
        mTextViewScreenTouch = findViewById(R.id.textview_screen_touch);
        mTextViewAccelrometerSensor = findViewById(R.id.textview_accelerometer_sensor);
        mTextViewMagneticSensor = findViewById(R.id.textview_magnetic_field_sensor);
        mTextViewOrientationSensor = findViewById(R.id.textview_orientation_sensor);
        mTextViewGyroscopeSensor = findViewById(R.id.textview_gyroscope_sensor);
        mTextViewLightSensor = findViewById(R.id.textview_light_sensor);
        mTextViewDistanceSensor = findViewById(R.id.textview_distance_sensor);
        mTextViewTemperatureSensor = findViewById(R.id.textview_temperature_sensor);
        mPrgressBarRam = findViewById(R.id.progressbar_ram);
        mProgressBarCpu = findViewById(R.id.progressbar_cpu);
        mProgressBarRom = findViewById(R.id.progressbar_storage);
    }

    private void setText(){
        // info device
        mTextViewFirmware.setText(DISPLAY);
        mTextViewNameDevice.setText(MANUFACTURER + " " + MODEL);
        mTextViewOsVersion.setText(VERSION.RELEASE);
        mTextViewRootState.setText("Not Rooted");

        // info cpu
        if(CpuInfo.get("Processor") != null) mTextViewCpuModel.setText(CpuInfo.get("Processor"));
        if(CpuInfo.get("Hardware") != null) mTextViewCpuHardware.setText(CpuInfo.get("Hardware"));
        if(CpuInfo.get("CPU architecture") != null) mTextViewCores.setText(CpuInfo.get("CPU architecture"));
        mTextViewFrequency.setText(getFrequencyCpu());

        //storage
        long RamFree =  freeRamMemorySize(); long RamTotal = totalRamMemorySize(); long RamUsed = (RamTotal - RamFree) / RamTotal * 100;
        long RomFree = Helper.getAvailableRomSize(); long RomTotal = Helper.getTotalRomSize();
        long SdcardFree = Helper.getAvailableSdcardSize(); long SdcardTotal = Helper.getTotalSdcardSize();
        String ram = String.format(Locale.CANADA, "%d/%d", RamFree, RamTotal);
        String rom = String.format(Locale.CANADA, "%d/%d", RomFree, RomTotal);
        String sdcard = String.format(Locale.CANADA, "%d/%d", RomFree, RomTotal);
        mTextViewSdcardInfo.setText(sdcard);
        mTextViewRomInfo.setText(rom);
        mTextViewRamInfo.setText(ram);

        //screen info
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;  int height = size.y;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);

        boolean hasMultitouch = getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
        if(hasMultitouch)
            mTextViewScreenTouch.setText("Supported");
        else
            mTextViewScreenTouch.setText("Not supported");

        String resolution = String.format(Locale.CANADA, "%d*%d", width, height);
        String density = String.format(Locale.CANADA, "%d DPI", densityDpi);

        mTextViewScreenDensity.setText(density);
        mTextViewScreenResolution.setText(resolution);

        // sensor info
        SensorManager mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        Sensor sensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensorAcc == null) mTextViewAccelrometerSensor.setText("Not Supported");

        Sensor sensorMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(sensorMag == null) mTextViewMagneticSensor.setText("Not Supported");

        Sensor sensorGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(sensorGyro == null) mTextViewGyroscopeSensor.setText("Not Supported");

        Sensor sensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(sensorLight == null) mTextViewLightSensor.setText("Not Supported");

        Sensor sensorDistance = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(sensorDistance == null) mTextViewDistanceSensor.setText("Not Supported");

        Sensor sensorTemp = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        if(sensorTemp == null) mTextViewTemperatureSensor.setText("Not Supported");

        // set state info
        mPrgressBarRam.setProgress((int) RamUsed);
        mPrgressBarRam.setMax(100);
        mProgressBarRom.setProgress((int)(RomTotal - RomFree));
        mProgressBarRom.setMax((int) RomTotal);
    }

    public Map<String, String> getCpuInfoMap() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Scanner s = new Scanner(new File("/proc/cpuinfo"));
            while (s.hasNextLine()) {
                String[] vals = s.nextLine().split(": ");
                if (vals.length > 1){
                    map.put(vals[0].trim(), vals[1].trim());
                    Log.d("test", "getCpuInfoMap: " + vals[0].trim() + " " + vals[1].trim());
                }
            }
        } catch (Exception e) {Log.e("getCpuInfoMap",Log.getStackTraceString(e));}
        return map;
    }

    public String getFrequencyCpu(){
        String value = null;
        try{
            Scanner s = new Scanner(new File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"));
            value = s.nextLine();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    private long freeRamMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        return availableMegs;
    }

    private long totalRamMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.totalMem / 1048576L;
        return availableMegs;
    }
}
