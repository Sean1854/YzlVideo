package com.ls.feature_plaza.api;

import com.ls.feature_plaza.bean.ResPlaza;
import com.ls.network.bean.ResBase;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 这里存放plaza模块的api
 */
public interface PlazaApiService {

    /**
     * 广场首页数据
     *
     * @return 服务端返回的数据类型
     */
    @GET("addons/cms/api.eye/square")
    Call<ResBase<List<ResPlaza>>> getPlaza();


}
