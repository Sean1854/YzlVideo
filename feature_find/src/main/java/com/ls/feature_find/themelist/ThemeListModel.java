package com.ls.feature_find.themelist;

import com.ls.feature_find.api.FindApiServiceProvider;
import com.ls.feature_find.bean.ResThemeData;
import com.ls.libbase.base.IRequestCallback;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import java.util.List;

import retrofit2.Call;

public class ThemeListModel {

    /**
     * 主题歌单详情数据
     * @param callback
     */
    public void requestdata(IRequestCallback<List<ResThemeData>> callback){
        Call<ResBase<List<ResThemeData>>> call = FindApiServiceProvider.getApiService().getAnchor();
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<List<ResThemeData>>>() {
            @Override
            public void onSuccess(ResBase<List<ResThemeData>> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }

}
