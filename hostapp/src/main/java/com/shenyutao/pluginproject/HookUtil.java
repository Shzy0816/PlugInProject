package com.shenyutao.pluginproject;

import android.app.Activity;
import android.content.Context;

import com.shenyutao.pluginproject.callback.HookHandlerCallback;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.logging.Handler;

/**
 * @author Shzy
 */
public class HookUtil {
    private static HookUtil mInstance = null;

    public static HookUtil getInstance() {
        if(mInstance == null){
            synchronized (HookUtil.class){
                if(mInstance == null){
                    mInstance = new HookUtil();
                    mInstance.init();
                }
            }
        }
        return mInstance;
    }

    private void init() {
    }

    public void HookATMS(Context context) throws Exception {
        // 通过反射获取到ActivityTaskManagerClass类对象，该类被隐藏了，所以只能用Class.forName这种形式获取
        Class<?> ActivityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
        // 获取到ActivityTaskManagerClass类对象的成员变量域iActivityTaskManagerSingletonField
        Field iActivityTaskManagerSingletonField = ActivityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
        // 通过iActivityTaskManagerSingletonField获取到iActivityTaskManagerSingletonField成员变量实例
        // 因为该实例是静态变量，所以get里面直接传入bull即可
        iActivityTaskManagerSingletonField.setAccessible(true);
        Object iActivityTaskManagerSingletonObject = iActivityTaskManagerSingletonField.get(null);


        // 通过Class.forName形式加载SingleTon类对象,该类被隐藏了，所以只能这么用
        Class<?> singletonClass = Class.forName("android.util.Singleton");
        // 获取singletonClass类对象中的mInstance属性
        Field mInstanceField = singletonClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        // 获取到mInstance实例，该实例是一个IActivityTaskManager类型变量,
        // IActivityTaskManager是ATMS的远程代理，通过它可以调用ATMS的同名方法
        // 我们可以直接将IActivityTaskManager看成是ATMS
        Object mInstanceObject = mInstanceField.get(iActivityTaskManagerSingletonObject);


        // 加载IActivityTaskManager类对象
        Class<?> iActivityTaskManagerInterface = Class.forName("android.app.IActivityTaskManager");

        // 为mInstanceObject创建动态代理，拦截其startActivity方法，并修改其Intent参数
        Object mInstanceProxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{iActivityTaskManagerInterface},
                new AtmsInvocationHandler(context,mInstanceObject));

        // 将mInstance对象替换成其代理对象
        mInstanceField.set(mInstanceObject,mInstanceProxy);
    }


    /**
     * 获取到特定消息中的Intent，进行处理
     * 将Intent中的对象替换回原来的Activity
     */
    public void HookHandler() throws Exception {

        // 加载ActivityThread类
        Class<?> ActivityThreadClass = Class.forName("android.app.ActivityThread");

        // 获取到ActivityThread类中的CurrentActivityThread属性
        // 这是一个静态属性，代表当前的ActivityThread
        Field sCurrentActivityThreadField = ActivityThreadClass.getDeclaredField("sCurrentActivityThread");
        sCurrentActivityThreadField.setAccessible(true);

        // 获取到当前ActivityThread实例，sCurrentActivityThread
        Object sCurrentActivityThreadObject = sCurrentActivityThreadField.get(null);

        // 获取到ActivityThread中的mH(handler)属性
        Field mHField = ActivityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Object mHObject = mHField.get(sCurrentActivityThreadObject);

        // 获取到mH（handler）的mCallback属性并未其赋值
        Field mCallBackField = Handler.class.getField("mCallback");
        mCallBackField.setAccessible(true);
        Object mCallBackObject = mCallBackField.get(mHObject);
        mCallBackField.set(mCallBackField,new HookHandlerCallback());

        // 1.2 获取ActivityThread对象的mH属性

        // 在CallBack中替换Activity
    }
}
