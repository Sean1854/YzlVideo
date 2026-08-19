package com.ls.feature_find.categorydetail;

import com.ls.feature_find.api.FindApiServiceProvider;
import com.ls.feature_find.bean.ResCategoryDetail;
import com.ls.libbase.base.IRequestCallback;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import retrofit2.Call;

public class CategoryDetailModel {
    public void requestData(int id, IRequestCallback<ResCategoryDetail> callback){
        Call<ResBase<ResCategoryDetail>> call = FindApiServiceProvider.getApiService().getCategoryDetail(id);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResCategoryDetail>>() {
            @Override
            public void onSuccess(ResBase<ResCategoryDetail> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }
}
