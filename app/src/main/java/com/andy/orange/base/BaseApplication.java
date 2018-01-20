package com.andy.orange.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Andy Lau on 2017/8/10.
 */

public class BaseApplication extends Application {

    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        baseApplication=this;
        super.onCreate();
    }

    public static Context getAppContext(){
        return baseApplication;

    }

    public static Resources getAppResources(){
        return baseApplication.getResources();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
