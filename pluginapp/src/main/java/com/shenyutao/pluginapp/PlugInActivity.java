package com.shenyutao.pluginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

/**
 * @author shenyutao
 */
public class PlugInActivity extends AppCompatActivity {

    private static final String TAG = "PlugInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug_in);
    }

    public static void doSomething(){
        Log.i(TAG, "doSomething: I am from plugin");
    }
}