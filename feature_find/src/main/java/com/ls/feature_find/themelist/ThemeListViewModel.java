package com.ls.feature_find.themelist;

import androidx.lifecycle.MutableLiveData;

import com.ls.feature_find.bean.ResThemeData;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;

import java.util.List;

public class ThemeListViewModel extends BaseViewModel {

    private final ThemeListModel mModel;

    private MutableLiveData<List<ResThemeData>> mThemeData = new MutableLiveData<>();//主题歌的详情数据

    public ThemeListViewModel() {
        mModel = new ThemeListModel();
    }

    public void requestData(){
        showLoading(true);
        mModel.requestdata(new IRequestCallback<List<ResThemeData>>() {
            @Override
            public void onLoadFinish(List<ResThemeData> datas) {
                showLoading(false);
                mThemeData.setValue(datas);
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                showLoading(false);
                showToast(meesage);
            }
        });
    }

    public MutableLiveData<List<ResThemeData>> getThemeData() {
        return mThemeData;
    }
}
