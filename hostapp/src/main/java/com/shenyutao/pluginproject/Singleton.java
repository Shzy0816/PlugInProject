package com.shenyutao.pluginproject;

import android.os.Handler;

public class Singleton {
    private static volatile Singleton mInstance = null;
    private Singleton(){}
    public static Singleton getInstance(){
        if(mInstance == null){
            synchronized (Singleton.class){
                if(mInstance == null){
                    mInstance = new Singleton();
                }
            }
        }
        return mInstance;

        Handler
    }
}
