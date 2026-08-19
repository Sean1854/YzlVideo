package com.ls.libbase.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ls.libbase.base.BaseApplication;

/**
 * 版本信息工具类
 */
public class VersionUtils {

    /**
     * 获取应用的版本名称
     *
     * @return 版本名称，如果获取失败则返回空字符串
     */
    public static String getVersionName() {
        try {
            Context context = BaseApplication.getContext();
            // 获取 PackageManager 实例
            PackageManager packageManager = context.getPackageManager();
            // 获取当前应用的包信息
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            // 返回版本名称
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取应用的版本代码
     *
     * @return 版本代码，如果获取失败则返回 -1
     */
    public static int getVersionCode() {
        Context context = BaseApplication.getContext();
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            // 在 Android 10 及以上版本使用 getLongVersionCode()，以下版本使用 versionCode
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                return (int) packageInfo.getLongVersionCode();
            } else {
                return packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
