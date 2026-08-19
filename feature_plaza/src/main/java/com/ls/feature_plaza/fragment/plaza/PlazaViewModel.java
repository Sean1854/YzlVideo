package com.ls.feature_plaza.fragment.plaza;

import androidx.lifecycle.MutableLiveData;

import com.ls.feature_plaza.bean.ResPlaza;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;

import java.util.List;

public class PlazaViewModel extends BaseViewModel implements IRequestCallback<List<ResPlaza>> {


    public MutableLiveData<List<ResPlaza>> getDatas() {
        return mDatas;
    }

    /** 跳转搜索页事件（true 表示需要跳转） */
    public final MutableLiveData<Boolean> mGoSearch = new MutableLiveData<>();

    MutableLiveData<List<ResPlaza>> mDatas = new MutableLiveData<>();
    private final PlazaModel mModel;

    public PlazaViewModel() {
        mModel = new PlazaModel();
    }


    public void requestDatas(){
        mModel.requestDatas(this);
    }

    /** 由 View 调用，发出跳转搜索页事件 */
    public void goSearch() {
        mGoSearch.setValue(true);
    }

    @Override
    public void onLoadFinish(List<ResPlaza> datas) {
        mDatas.setValue(datas);
    }

    @Override
    public void onLoadFailure(int errorCode, String meesage) {
        mErrorCode.setValue(errorCode);
    }
}
