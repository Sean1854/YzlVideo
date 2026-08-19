package com.ls.feature_user.aboutme;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;

public class AboutMeViewModel extends BaseViewModel {

    private final AboutMeModel mModel;
    private MutableLiveData<String> mVersionLable = new MutableLiveData<>();

    public AboutMeViewModel() {
        mModel = new AboutMeModel();
        int versionCode = mModel.getVersionCode();
        String versionName = mModel.getVersionName();
        mVersionLable.setValue("版本信息：V" + versionName + "-"+ versionCode);
    }

    public MutableLiveData<String> getVersionLable() {
        return mVersionLable;
    }
}
