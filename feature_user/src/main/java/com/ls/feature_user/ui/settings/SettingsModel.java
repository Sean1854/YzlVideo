package com.ls.feature_user.ui.settings;

import com.ls.feature_user.api.UserApiServiceProvider;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.UserInfo;
import com.ls.libbase.manager.UserManager;
import com.ls.libbase.utils.CacheUtils;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import retrofit2.Call;

public class SettingsModel {


    /**
     *获取缓存大小
     * @return
     */
    public String getCacheSize(){
        String cacheSize = CacheUtils.getTotalCacheSize();
        return cacheSize;
    }

    /**
     * 清理缓存
     * @return
     */
    public Boolean clearCache(){
        boolean b = CacheUtils.clearAppCache();
        boolean b1 = CacheUtils.clearExternalCache();
        return b&&b1;
    }

    /**
     * 判断是否登录
     * @return
     */
    public Boolean isLogin(){
        return UserManager.getInstance().isLogin();
    }

    public String getMobile(){
        if (isLogin()){
            UserInfo user = UserManager.getInstance().getUserInfo().getUser();
            if (user == null) {
                return null;
            }
            //账号与绑定显示的是登录用户名（手机号），不是昵称
            String username = user.getUsername();
            if (username == null || username.length() < 8) {
                //长度不足时不做掩码，直接返回，防止 substring 越界闪退
                return username;
            }
            return username.substring(0, 3) + "****" + username.substring(7);
        }
        return null;
    }


    /**
     * 退出登录
     */
    public void logout(IRequestCallback<ResBase<ResBase>> callback){
        String token = UserManager.getInstance().getToken();
        //发起请求
        Call<ResBase<ResBase>> call = UserApiServiceProvider.getApiService().logout(token);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResBase>>() {
            @Override
            public void onSuccess(ResBase<ResBase> result) {
                callback.onLoadFinish(result);
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });

    }

}
