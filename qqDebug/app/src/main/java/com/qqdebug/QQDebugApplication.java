package com.qqdebug;

import android.app.Application;

/**
 * @Desc
 * @time 2017/9/13 16:51
 * @Author lixiangxiang
 */

public class QQDebugApplication extends Application {

    private static QQDebugApplication app;

    public static QQDebugApplication getInstance(){
         return  app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
