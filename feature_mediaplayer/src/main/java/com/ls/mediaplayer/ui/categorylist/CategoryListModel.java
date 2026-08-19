package com.ls.mediaplayer.ui.categorylist;

import com.ls.data_video.bean.ResCategoryVideoDetail;
import com.ls.libbase.base.list.BaseListModel;
import com.ls.libbase.manager.UserManager;
import com.ls.mediaplayer.api.MediaApiServiceProvider;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;
import com.ls.network.bean.ResList;

import retrofit2.Call;

public class CategoryListModel extends BaseListModel {

    private int mType;
    private int mChannelId;

    @Override
    public void requestDatas(boolean isFirst) {
        if (isFirst) {
            mPage = 1;
        } else {
            mPage++;
        }
        String token = UserManager.getInstance().getToken();

        Call<ResBase<ResList<ResCategoryVideoDetail>>> call = MediaApiServiceProvider.getApiService().getCategoryLists(token, mType, mChannelId, mPage, mLimit);
        ApiCall.enqueueLists(call, new ApiCall.ApiListsCallback() {
            @Override
            public void onSuccess(ResList result) {
                mListenner.onLoadFinish(isFirst,result);
            }

            @Override
            public void onError(int errorCode, String meesage) {
                mListenner.onLoadFailure(errorCode);
            }
        });

    }


    public void setArgments(int type, int id) {
        mType = type;
        mChannelId = id;
    }




}
