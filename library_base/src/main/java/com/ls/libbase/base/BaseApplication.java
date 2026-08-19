package com.ls.libbase.base;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.BuildConfig;
import com.alibaba.android.arouter.launcher.ARouter;

import me.jessyan.autosize.AutoSizeConfig;

public class BaseApplication extends Application {
    private static Application instance;
    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        // 只在 debug 模式下开启日志和调试模式
        if (BuildConfig.DEBUG) {
            ARouter.openLog();  // 打印日志
            ARouter.openDebug(); // 开启调试模式（必须在初始化之前调用）
        }

        // 初始化 ARouter
        ARouter.init(this);

        //AutoSiza的参数初始化
        AutoSizeConfig.getInstance().setCustomFragment(true);
    }

    /**
     * 使用Application生成了一个全局可用的context
     * 注意不要滥用，否则会产生下面的问题
     * 1、把Application当成是某个Activity上下文，与ui更新关联在一起，会引发错误
     * 2、更容易获取到context，会增加项目耦合性
     *
     * @return
     */
    public static Context getContext() {

        return instance.getApplicationContext();
    }
}
