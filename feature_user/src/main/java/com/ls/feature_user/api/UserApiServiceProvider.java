package com.ls.feature_user.api;

import com.ls.network.RetrofitProvider;

import retrofit2.Retrofit;

/**
 * user模块中的userApiService统一在这里获取，以便统一管理
 */
public class UserApiServiceProvider {

    private static UserApiService mApiService;

    //单例
    public static UserApiService getApiService() {
        if (mApiService == null) {
            Retrofit retrofit = RetrofitProvider.provider();
            mApiService = retrofit.create(UserApiService.class);
        }
        return mApiService;
    }
}
