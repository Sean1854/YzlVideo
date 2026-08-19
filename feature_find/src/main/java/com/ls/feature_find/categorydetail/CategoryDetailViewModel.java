package com.ls.feature_find.categorydetail;

import androidx.lifecycle.MutableLiveData;

import com.ls.feature_find.bean.ResCategoryDetail;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;

public class CategoryDetailViewModel extends BaseViewModel {

    private final CategoryDetailModel mModel;

    private MutableLiveData<ResCategoryDetail> mCategory = new MutableLiveData<>();
    private MutableLiveData<String> mPeople = new MutableLiveData<>();//浏览人数

    public CategoryDetailViewModel() {
        mModel = new CategoryDetailModel();
    }

    /**
     * 请求分类详情数据
     *
     * @param id
     */
    public void requestData(int id) {
        showLoading(true);
        mModel.requestData(id, new IRequestCallback<ResCategoryDetail>() {
            @Override
            public void onLoadFinish(ResCategoryDetail datas) {
                showLoading(false);
                mCategory.setValue(datas);
                mPeople.setValue(String.format("%s万人参与·%s万人浏览",datas.getInfo().getPeople(),datas.getInfo().getBrowse()));
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                showLoading(false);
            }
        });
    }

    public MutableLiveData<ResCategoryDetail> getCategory() {
        return mCategory;
    }

    public MutableLiveData<String> getPeople() {
        return mPeople;
    }
}
