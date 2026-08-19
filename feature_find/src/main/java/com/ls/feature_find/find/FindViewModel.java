package com.ls.feature_find.find;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ls.feature_find.bean.ResFind;
import com.ls.feature_find.bean.ResFindAnchor;
import com.ls.data_video.bean.ResFindCategory;
import com.ls.feature_find.bean.ResFindTopic;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;

import java.util.List;

public class FindViewModel extends BaseViewModel implements IRequestCallback<ResFind> {
    private static final String TAG = "FindViewModel";
    private MutableLiveData<List<ResFindCategory>> mCategory = new MutableLiveData<>();
    private MutableLiveData<List<ResFindAnchor>> mAnchor = new MutableLiveData<>();
    private MutableLiveData<List<ResFindTopic>> mTopic = new MutableLiveData<>();
    private MutableLiveData<FindAction> mAction = new MutableLiveData<>();//跳转事件

    public MutableLiveData<List<ResFindTopic>> getTopic() {
        return mTopic;
    }

    public MutableLiveData<List<ResFindAnchor>> getAnchor() {
        return mAnchor;
    }

    public MutableLiveData<List<ResFindCategory>> getCategory() {
        return mCategory;
    }

    private final FindModel mModel;

    public FindViewModel() {
        mModel = new FindModel();
    }


    public void startThemeListActivity() {
        //跳转到主题播单
        mAction.setValue(FindAction.NAVIGATION_TO_THEME_LIST);
    }


    public void startTopicActivity() {
        //跳转到话题广场
        mAction.setValue(FindAction.NAVIGATION_TO_TOPIC);
    }


    public void startSearchActivity(){
        //跳转搜索页
        mAction.setValue(FindAction.NAVIGATION_TO_SEARCH);
    }

    public void loadFindData(){
        mModel.loadFindData(this);
    }

    @Override
    public void onLoadFinish(ResFind datas) {

        Log.i(TAG, "onLoadFinish: datas的数据有多少条：" + datas.getAnchor().size());
        mCategory.setValue(datas.getCategory());
        mAnchor.setValue(datas.getAnchor());
        mTopic.setValue(datas.getTopic());
    }

    @Override
    public void onLoadFailure(int errorCode, String meesage) {
        showToast(meesage);
    }

    public MutableLiveData<FindAction> getAction() {
        return mAction;
    }

    /**
     * 枚举
     */
    public enum FindAction {
        NAVIGATION_TO_THEME_LIST,   // 跳转到主题播单
        NAVIGATION_TO_TOPIC,// 跳转到话题广场
        NAVIGATION_TO_SEARCH,// 跳转到搜索页

    }

}
