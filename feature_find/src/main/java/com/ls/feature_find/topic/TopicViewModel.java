package com.ls.feature_find.topic;

import androidx.lifecycle.MutableLiveData;

import com.ls.feature_find.bean.ResTopic;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;

import java.util.List;

public class TopicViewModel extends BaseViewModel {

    private MutableLiveData<List<ResTopic>> mResTopic = new MutableLiveData<>();

    private final TopicModel mModel;

    public TopicViewModel() {
        mModel = new TopicModel();
    }

    public void requestTopicData(){
        showLoading(true);
        mModel.requestTopicData(new IRequestCallback<List<ResTopic>>() {
            @Override
            public void onLoadFinish(List<ResTopic> datas) {
                showLoading(false);
                mResTopic.setValue(datas);
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                showLoading(false);
                showToast(meesage);
            }
        });
    }

    public MutableLiveData<List<ResTopic>> getResTopic() {
        return mResTopic;
    }
}
