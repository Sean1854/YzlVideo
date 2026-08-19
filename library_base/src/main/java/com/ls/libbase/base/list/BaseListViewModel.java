package com.ls.libbase.base.list;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;
import com.ls.network.bean.ResList;
import com.ls.network.config.ErrorStatusConfig;

import java.util.List;

public class BaseListViewModel<T , V extends BaseListModel> extends BaseViewModel implements IListenner<T>  {
    public BaseListViewModel(V model) {
        this.mModel = model;
        mModel.setListenner(this);
    }

    public V mModel;
    public MutableLiveData<ResList<T>> mDatas = new MutableLiveData<>();
    public MutableLiveData<Boolean> mIsLoadMore = new MutableLiveData<>(true);//是否继续加载更多

    /**
     * 是否为首次加载/刷新场景。加载更多场景为 false，用于区分 Toast 提示，避免首屏/刷新误弹。
     */
    private boolean mIsFirstLoad = true;

    public boolean isFirstLoad() {
        return mIsFirstLoad;
    }

    public void requestDatas(boolean isFirst){
        if (isFirst){
            mIsFirstLoad = true;
            mIsLoadMore.setValue(true);
            mModel.requestDatas(true);
        } else {
            mIsFirstLoad = false;
            //已经是最后一页，不再发起无效请求
            if (Boolean.FALSE.equals(mIsLoadMore.getValue())){
                return;
            }
            mModel.requestDatas(false);
        }
    }

    public MutableLiveData<ResList<T>> getDatas() {
        return mDatas;
    }

    public MutableLiveData<Boolean> getIsLoadMore() {
        return mIsLoadMore;
    }

    @Override
    public void onLoadFinish(boolean isFirst, ResList<T> datas) {
        if (isFirst){
            //第一次加载的情况
            mDatas.setValue(datas);
        }else {
            //分页加载情况
            ResList<T> value = mDatas.getValue();//如果isFirst为false，mDatas里面本来就有值
            List<T> list = value.getList();
            list.addAll(datas.getList());
            mDatas.setValue(value);
        }

        int count = mDatas.getValue().getCount();
        if (mDatas.getValue().getList().size() >= count){
            mIsLoadMore.setValue(false);
        }
    }

    @Override
    public void onLoadFailure(int statusCode) {
        mErrorCode.setValue(statusCode);

        if (statusCode == ErrorStatusConfig.ERROR_STATUS_EMPTY){
            mIsLoadMore.setValue(false);
        }
    }
}
