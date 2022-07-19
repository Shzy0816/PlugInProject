package com.shenyutao.pluginproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author shenyutao
 */
public class HostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        try {
            Class<?> clazz = Class.forName("com.shenyutao.pluginapp.PlugInActivity");
            clazz.getMethod("doSomething").invoke(null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkPermission() {
        if (checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
            }, 1);

        }
        return false;
    }
}