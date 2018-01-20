package com.andy.orange.app;

import com.andy.orange.base.BaseApplication;

/**
 * Created by Andy Lau on 2017/8/10.
 */

public class AppApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
/*        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this); // 初始化 JPush*/
    }
}
