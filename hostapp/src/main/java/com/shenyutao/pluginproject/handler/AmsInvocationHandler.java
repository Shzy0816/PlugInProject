package com.shenyutao.pluginproject.handler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.shenyutao.pluginproject.RegisterActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author shenyutao
 */
public class AmsInvocationHandler implements InvocationHandler {
    private static final String TAG = "AmsInvocationHandler";
    private Context context;
    private Object subject;

    public AmsInvocationHandler(Context context, Object subject) {
        this.context = context;
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if("startActivity".equals(method.getName())){
            Log.d(TAG, "invoke: ");
            //替换要启动但是未注册的Activity
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if(arg instanceof Intent){
                    Intent intentNew = new Intent();
                    intentNew.setClass(context, RegisterActivity.class);
                }
            }
        }
        return method.invoke(subject,args);
    }
}
