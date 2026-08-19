package com.ls.feature_user.user;

import com.ls.libbase.bean.ResUser;

public interface ILoadUserInfoCallback {
    /**
     * 用戶信息加载成功
     *
     * @param user
     */
    void onLoadSuccess(ResUser user);
    /**
     * 加载失败
     *
     * @param errorCode
     * @param message
     */
    void onLoadFailure(int errorCode, String message);
}
