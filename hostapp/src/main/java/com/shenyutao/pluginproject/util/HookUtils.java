package com.shenyutao.pluginproject.util;

import android.app.ActivityManager;
import android.content.Context;

import com.shenyutao.pluginproject.handler.AmsInvocationHandler;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;


/**
 * @author shenyutao
 */
public class HookUtils {
    /**
     * hook AMS对象
     * 对AMS对象的startActivity方法进行拦截
     */
    public static void hookAMS(Context context) throws Exception {
        //1.获取ATMS对象
        //1.1获取静态属性ActivityTaskManager.IActivityTaskManagerSingleton的值
        //它是Singleton类型

        Class<?> activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
        Field iActivityTaskManagerSingletonField = activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
        iActivityTaskManagerSingletonField.setAccessible(true);
        Object iActivityTaskManagerSingletonObject = iActivityTaskManagerSingletonField.get(null);


        //获取Singleton的mInstance属性值
        Class<?> singletonClazz = Class.forName("android.util.Singleton");
        Field mInstanceField = singletonClazz.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        Object amsSubject = mInstanceField.get(iActivityTaskManagerSingletonObject);

        //2.对AMS对象进行代理
        Class<?> iActivityTaskManagerInterface = Class.forName("android.app.IActivityTaskManager");
        AmsInvocationHandler amsInvocationHandler = new AmsInvocationHandler(context,amsSubject);
        Object amsProxy = Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{iActivityTaskManagerInterface},
                amsInvocationHandler
        );

        mInstanceField.set(iActivityTaskManagerSingletonObject,amsProxy);

        //3.InvocationHandler对AMS的startActivity方法进行拦截
    }


}
