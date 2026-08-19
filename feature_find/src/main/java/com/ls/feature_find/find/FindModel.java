package com.ls.feature_find.find;

import com.ls.feature_find.api.FindApiServiceProvider;
import com.ls.feature_find.bean.ResFind;
import com.ls.libbase.base.IRequestCallback;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import retrofit2.Call;

public class FindModel {

    public void loadFindData(IRequestCallback callback){
        Call<ResBase<ResFind>> call = FindApiServiceProvider.getApiService().getFindData();
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResFind>>() {
            @Override
            public void onSuccess(ResBase<ResFind> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }
}
