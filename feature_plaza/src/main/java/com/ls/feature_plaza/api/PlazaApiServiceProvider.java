package com.ls.feature_plaza.api;

import com.ls.network.RetrofitProvider;

import retrofit2.Retrofit;

/**
 * plaza模块中的PlazaApiService统一在这里获取，以便统一管理
 */
public class PlazaApiServiceProvider {

    private static PlazaApiService mApiService;

    //单例
    public static PlazaApiService getApiService() {
        if (mApiService == null) {
            Retrofit retrofit = RetrofitProvider.provider();
            mApiService = retrofit.create(PlazaApiService.class);
        }
        return mApiService;
    }
}
