package com.ls.mediaplayer.ui.search;

import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.ls.data_video.bean.ResVideoDetail;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;

import java.util.List;

public class SearchViewModel extends BaseViewModel {
    private final SearchModel mModel;
    private MutableLiveData<String> mSearchKeyword = new MutableLiveData<>();//输入框内容
    private MutableLiveData<Integer> mShowCleanButton = new MutableLiveData<>(View.INVISIBLE);//是否显示，清除输入框文字图标
    private MutableLiveData<List<ResVideoDetail.ArchivesInfoBean>> mSearchData = new MutableLiveData<>();//搜索的结果数据
    public SearchViewModel() {
        mModel = new SearchModel();
    }

    /**
     * 请求搜索页面数据 keyword是搜索关键字
     */
    public void requestSearch(){
        showLoading(true);
        String keyword = mSearchKeyword.getValue();//获取输入框的内容
        mModel.requestSearch(keyword, new IRequestCallback<List<ResVideoDetail.ArchivesInfoBean>>() {
            @Override
            public void onLoadFinish(List<ResVideoDetail.ArchivesInfoBean> datas) {
                showLoading(false);
                mSearchData.setValue(datas);
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                showLoading(false);
                showToast(meesage);
            }
        });
    }

    public void upEditData(){
        mShowCleanButton.setValue(mSearchKeyword.getValue() !=null&& !mSearchKeyword.getValue().isEmpty() ? View.VISIBLE : View.INVISIBLE);
    }


    /**
     * 点击清除输入框内容
     */
    public void cleanSearchKeyword(){
        if (mSearchKeyword != null){
            mSearchKeyword.setValue("");
        }
    }

    public MutableLiveData<String> getSearchKeyword() {
        return mSearchKeyword;
    }

    public MutableLiveData<Integer> getShowCleanButton() {
        return mShowCleanButton;
    }

    public MutableLiveData<List<ResVideoDetail.ArchivesInfoBean>> getSearchData() {
        return mSearchData;
    }
}
