package com.shenyutao.pluginproject.plugincore;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * @author shenyutao
 */
public class PlugInManager {
    private final static String TAG = "PlugInManager";
    private static PlugInManager instance;
    private Context context;

    private PlugInManager(Context context) {
        this.context = context;
    }

    public static PlugInManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PlugInManager.class) {
                if (instance == null) {
                    instance = new PlugInManager(context);
                    instance.init();
                }
            }
        }
        return instance;
    }

    private void init() {
        try {
            loadApk();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 加载插件的apk文件，并且合并dexElements
     */
    private void loadApk() throws Exception {
        Log.i(TAG, "init: " );
        //加载插件的apk
        String pluginApkPath = context.getExternalFilesDir(null).getAbsolutePath() + "/pluginapp-debug.apk";
        String cachePath = context.getDir("cache_plugin", Context.MODE_PRIVATE).getAbsolutePath();
        DexClassLoader dexClassLoader = new DexClassLoader(pluginApkPath, cachePath, null, context.getClassLoader());

        //从BaseDexClassLoader类中获取DexPathList对象，这是一个类不是列表
        Class<?> baseDexClassLoader = dexClassLoader.getClass().getSuperclass();
        Field pathListField = baseDexClassLoader.getDeclaredField("pathList");
        pathListField.setAccessible(true);

        //获取plugin（插件）的DexElements  获取DexPathList中的DexElements属性
        Object pluginPathListObject = pathListField.get(dexClassLoader);
        Class<?> pathListClass = pluginPathListObject.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        Object pluginDexElements = dexElementsField.get(pluginPathListObject);

        //获取host（宿主）的DexElements
        ClassLoader classLoader = context.getClassLoader();
        Object hostPathListObject = pathListField.get(classLoader);
        Object hostDexElements = dexElementsField.get(hostPathListObject);

        //合并
        int pluginDexElementsLength = Array.getLength(pluginDexElements);
        int hostDexElementsLength = Array.getLength(hostDexElements);
        int newDexElementsLength = pluginDexElementsLength + hostDexElementsLength;

        Object newDexElements = Array.newInstance(hostDexElements.getClass().getComponentType(),newDexElementsLength);
        for (int i = 0; i < newDexElementsLength; i++) {
            //plugin
            if( i < pluginDexElementsLength){
                Array.set(newDexElements,i,Array.get(pluginDexElements,i));
            }

            //host
            else{
                Array.set(newDexElements,i,Array.get(hostDexElements,i-pluginDexElementsLength));
            }
        }

        dexElementsField.set(hostPathListObject,newDexElements);
        Log.i(TAG, "init: " );

    }
}
