package com.shenyutao.pluginproject.callback;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import java.lang.reflect.Field;
import java.util.List;


public class HookHandlerCallback implements Handler.Callback {
    private final static int EXECUTE_TRANSACTION = 159;

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what == EXECUTE_TRANSACTION) {
            try {
                Object object = msg.obj;
                Class<?> clientTransactionClass = object.getClass();
                Field mActivityCallbacksField = clientTransactionClass.getDeclaredField("mActivityCallbacks");
                mActivityCallbacksField.setAccessible(true);
                List mActivityCallbacksObject = (List) mActivityCallbacksField.get(object);


                // 遍历mActivityCallbacks
                for (Object item : mActivityCallbacksObject) {
                    if ("android.app.servertransaction.LaunchActivityItem"
                            .equals(item.getClass().getName())) {
                        Field mIntentField = item.getClass().getDeclaredField("mIntent");
                        mIntentField.setAccessible(true);
                        Intent mIntent = (Intent) mIntentField.get(item);
                        Parcelable actionIntent = mIntent.getParcelableExtra("actionIntent");
                        if (actionIntent != null) {
                            mIntentField.set(item, actionIntent);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
