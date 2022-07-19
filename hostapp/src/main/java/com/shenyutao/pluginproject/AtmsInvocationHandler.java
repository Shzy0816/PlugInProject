package com.shenyutao.pluginproject;

import android.content.Context;
import android.content.Intent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * @author shzy
 */
public class AtmsInvocationHandler implements InvocationHandler {
    private String mInterceptMethodName = "startActivity";
    private Context context;
    private Object object;

    public AtmsInvocationHandler(Context context, Object object){
        this.context = context;
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(mInterceptMethodName.equals(method.getName())){
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if(arg instanceof Intent){
                    Intent newIntent = new Intent(context,RegisterActivity.class);
                    newIntent.putExtra("actionIntent",(Intent)arg);
                    args[i] = newIntent;
                    break;
                }
            }
        }
        return method.invoke(object,args);
    }
}
