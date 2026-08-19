package com.ls.feature_find.topic;

import com.ls.feature_find.api.FindApiServiceProvider;
import com.ls.feature_find.bean.ResTopic;
import com.ls.libbase.base.IRequestCallback;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import java.util.List;

import retrofit2.Call;

public class TopicModel {

    public void requestTopicData(IRequestCallback<List<ResTopic>> callback){
        Call<ResBase<List<ResTopic>>> call = FindApiServiceProvider.getApiService().getTopic();
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<List<ResTopic>>>() {
            @Override
            public void onSuccess(ResBase<List<ResTopic>> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }
}
