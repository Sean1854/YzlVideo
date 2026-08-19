package com.ls.video;

import android.app.Application;

import com.alibaba.android.arouter.BuildConfig;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.libbase.base.BaseApplication;

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // 只在 debug 模式下开启日志和调试模式
        if (BuildConfig.DEBUG) {
            ARouter.openLog();  // 打印日志
            ARouter.openDebug(); // 开启调试模式（必须在初始化之前调用）
        }

        // 初始化 ARouter
        ARouter.init(this);
    }

}
