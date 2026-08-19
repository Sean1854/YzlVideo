package com.ls.feature_plaza.fragment.plaza;

import com.ls.feature_plaza.api.PlazaApiServiceProvider;
import com.ls.feature_plaza.bean.ResPlaza;
import com.ls.libbase.base.IRequestCallback;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import java.util.List;

import retrofit2.Call;

public class PlazaModel {

    public void requestDatas(IRequestCallback<List<ResPlaza>> callback) {
        Call<ResBase<List<ResPlaza>>> call = PlazaApiServiceProvider.getApiService().getPlaza();
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<List<ResPlaza>>>() {
            @Override
            public void onSuccess(ResBase<List<ResPlaza>> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }

}
