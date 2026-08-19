package com.ls.feature_user.aboutme;

import com.ls.libbase.utils.VersionUtils;

public class AboutMeModel {

    /**
     * 获取版本名称
     * @return
     */
    public String getVersionName() {
        String versionName = VersionUtils.getVersionName();
        return versionName;
    }

    /**
     * 获取版本号
     * @return 失败返回-1
     */
    public int getVersionCode() {
        int versionCode = VersionUtils.getVersionCode();
        return versionCode;
    }


}
