package com.zjl.mywechat;

import android.app.Application;
import android.content.Context;

/**
 * Created by dllo on 16/9/20.
 * 注意!!!!写完Application之后一定要注册
 */
public class MyApp extends Application {
    private static Context mcontext;
    @Override
    public void onCreate() {
        super.onCreate();
        mcontext = this;

    }

    public static Context getMcontext() {
        return mcontext;
    }

}
