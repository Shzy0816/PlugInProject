package com.shenyutao.pluginproject;

import android.app.Application;

import com.shenyutao.pluginproject.plugincore.PlugInManager;

/**
 * @author shenyutao
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PlugInManager.getInstance(this);
    }
}
