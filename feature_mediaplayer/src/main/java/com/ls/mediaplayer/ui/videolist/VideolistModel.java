package com.ls.mediaplayer.ui.videolist;


import com.ls.libbase.base.list.BaseListModel;
import com.ls.libbase.config.ARouterPath;
import com.ls.mediaplayer.api.MediaApiService;
import com.ls.mediaplayer.api.MediaApiServiceProvider;
import com.ls.data_video.bean.ResVideo;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;
import com.ls.network.bean.ResList;

import retrofit2.Call;

public class VideolistModel extends BaseListModel {

    private int mPageType;


    @Override
    public void requestDatas(boolean isFirst) {
        if (isFirst) {
            mPage = 1;
        } else {
            mPage++;
        }

        MediaApiService apiService = MediaApiServiceProvider.getApiService();
        Call<ResBase<ResList<ResVideo>>> call;
        if (mPageType == ARouterPath.Video.VIDEO_LIST_FRAGMENT_RECOMMEND){
            call = apiService.getRecommend(mPage, mLimit);
        }else {
            call = apiService.getDaily(mPage, mLimit);
        }

        ApiCall.enqueueLists(call, new ApiCall.ApiListsCallback() {
            @Override
            public void onSuccess(ResList result) {
                mListenner.onLoadFinish(isFirst,result);
            }

            @Override
            public void onError(int errorCode, String message) {
                mListenner.onLoadFailure(errorCode);
            }
        });
    }

    public void setPageType(int pageType) {
        mPageType = pageType;
    }
}
