package com.ls.mediaplayer.api;

import com.ls.network.RetrofitProvider;

import retrofit2.Retrofit;

/**
 * home模块中的HomeApiService统一在这里获取，以便统一管理
 */
public class MediaApiServiceProvider {

    private static MediaApiService mApiService;

    //单例
    public static MediaApiService getApiService() {
        if (mApiService == null) {
            Retrofit retrofit = RetrofitProvider.provider();
            mApiService = retrofit.create(MediaApiService.class);
        }
        return mApiService;
    }
}
