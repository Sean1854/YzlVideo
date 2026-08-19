package com.ls.mediaplayer.ui.search;

import com.ls.data_video.bean.ResVideoDetail;
import com.ls.libbase.base.IRequestCallback;
import com.ls.mediaplayer.api.MediaApiServiceProvider;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import java.util.List;

import retrofit2.Call;

public class SearchModel {

    /**
     * 请求搜索页数据
     * @param keyword 搜索关键字
     * @param callback
     */
    public void requestSearch(String keyword, IRequestCallback<List<ResVideoDetail.ArchivesInfoBean>> callback) {
        Call<ResBase<List<ResVideoDetail.ArchivesInfoBean>>> call = MediaApiServiceProvider.getApiService().search(keyword);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<List<ResVideoDetail.ArchivesInfoBean>>>() {
            @Override
            public void onSuccess(ResBase<List<ResVideoDetail.ArchivesInfoBean>> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }

}
