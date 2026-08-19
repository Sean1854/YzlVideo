package com.ls.libbase.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

    //错误码
    public MutableLiveData<Integer> mErrorCode = new MutableLiveData<>();

    //如果toastText发生了变化，表示需要进行toast弹窗显示
    private MutableLiveData<String> mToastText = new MutableLiveData<>();

    //是否显示加载样式
    private MutableLiveData<Boolean> mShowLoading = new MutableLiveData<>();
    //是否需要关闭当前页面
    private MutableLiveData<Boolean> mFinish = new MutableLiveData<>();

    private MutableLiveData<Boolean> mIslogin = new MutableLiveData<>();


    /**
     * 关闭页面
     */
    public void onFinishPage() {
        mFinish.setValue(true);
    }

    /**
     *跳转登录页面
     * Toast 统一在这里弹出，跳转由 BaseActivity 观察 mIslogin 处理，
     * 避免 Activity 与共享 ViewModel 的 Fragment 重复弹 Toast
     */
    public void startLogin(){
        showToast("请先登录");
        mIslogin.setValue(false);
    }

    /**
     * 显示吐司弹窗
     *
     * @param text
     */
    public void showToast(String text) {
        if (text == null || text.equals("")) {
            return;
        }
        mToastText.setValue(text);
        mToastText.setValue(null);
    }

    /**
     * 是否显示弹窗
     *
     * @param b
     */
    public void showLoading(boolean b) {
        mShowLoading.setValue(b);

    }

    public MutableLiveData<Boolean> getShowLoading() {
        return mShowLoading;
    }

    public MutableLiveData<Boolean> getFinish() {
        return mFinish;
    }

    public MutableLiveData<String> getToastText() {
        return mToastText;
    }

    public MutableLiveData<Integer> getErrorCode() {
        return mErrorCode;
    }

    public MutableLiveData<Boolean> getIslogin() {
        return mIslogin;
    }

    public void setIslogin(boolean islogin) {
        mIslogin.setValue(islogin);
    }

}
