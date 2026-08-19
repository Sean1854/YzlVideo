package com.ls.feature_plaza.ui.image;

import androidx.lifecycle.MutableLiveData;

import com.ls.feature_plaza.bean.ResPlaza;
import com.ls.libbase.base.BaseViewModel;

public class ImageViewModel extends BaseViewModel {

    MutableLiveData<ResPlaza.PlazaDetail> mData = new MutableLiveData<>();

    public void updateData(ResPlaza.PlazaDetail detail){
        mData.setValue(detail);
    }

    public MutableLiveData<ResPlaza.PlazaDetail> getData() {
        return mData;
    }
}
