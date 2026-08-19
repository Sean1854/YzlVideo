package com.ls.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {
    private static final String BASE_URL = "https://titok.fzqq.fun/";
    private static Retrofit mRetrofit;
    public static Retrofit provider(){

        if (mRetrofit == null){
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())// 配置 Gson 转换器
                    .client(OkhttpClientProvider.provider())
                    .build();
        }

        return mRetrofit;
    }
}
