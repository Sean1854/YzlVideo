package com.ls.feature_user.ui.settings;

import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.libbase.manager.UserManager;
import com.ls.libbase.ui.dialog.YesOrNoDialog;
import com.ls.network.bean.ResBase;

public class SettingsViewModel extends BaseViewModel {

    //手机号
    private MutableLiveData<String> mMobile = new MutableLiveData<>();
    //缓存大小
    private MutableLiveData<String> mCacheSize = new MutableLiveData<>();

    //是否显示退出登录的按钮
    private MutableLiveData<Integer> mExitLoginBtnVisibility = new MutableLiveData<>();
    private MutableLiveData<SettingsAction> mAction = new MutableLiveData<>();

    private final SettingsModel mModel;

    public SettingsViewModel() {
        mModel = new SettingsModel();
        refreshCache();

    }

    /**
     * 显示缓存大小
     */
    public void refreshCache() {
        mMobile.setValue(mModel.getMobile());
        mExitLoginBtnVisibility.setValue(mModel.isLogin() ? View.VISIBLE : View.INVISIBLE);
        String cacheSize = mModel.getCacheSize();
        mCacheSize.setValue(cacheSize);
    }

    public void clearCache(){
        showLoading(true);
        Boolean b = mModel.clearCache();//调用model里的清理缓存方法
        if (b){
            showLoading(false);
            refreshCache();
            showToast("清理缓存成功！");
        }else {
            showLoading(false);
            showToast("清理缓存失败，请前往应用设置手动清理！");
        }
    }



    /**
     * 点击账号与绑定
     */
    public void onAccountBindClick() {
        if (mModel.isLogin()){
            mAction.setValue(SettingsAction.NAVIGATION_TO_ACCOUNT);
        }else {
            mAction.setValue(SettingsAction.NAVIGATE_TO_LOGIN);
        }
    }

    /**
     * 点击设置密码
     */
    public void onPasswordSettingClick() {
        if (mModel.isLogin()){
            mAction.setValue(SettingsAction.NAVIGATION_TO_PASSWORD);
        }else {
            mAction.setValue(SettingsAction.NAVIGATE_TO_LOGIN);
        }
    }

    /**
     * 点击推送设置
     */
    public void onPushSettingClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_PUSH_SETTING);
    }

    /**
     * 点击播放设置
     */
    public void onPlaySettingClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_PLAY_SETTING);
    }

    /**
     * 点击清除缓存
     */
    public void onClearCacheClick() {
        mAction.setValue(SettingsAction.SHOW_CLEAR_CACHE_DIALOG);
    }

    /**
     * 点击用户协议
     */
    public void onUserAgreementClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_USER_AGREEMENT);
    }

    /**
     * 点击隐私政策概要
     */
    public void onSimplePrivacyPolicyClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_SIMPLE_PRIVACY_POLICY);
    }

    /**
     * 点击隐私政策
     */
    public void onPrivacyPolicyClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_PRIVACY_POLICY);
    }

    /**
     * 点击隐私权限设置
     */
    public void onPermissionSettingsClick() {

    }

    /**
     * 点击个人信息收集清单
     */
    public void onUserInfoMenusClick() {
    mAction.setValue(SettingsAction.NAVIGATE_TO_USER_INFO_MENU);
    }

    /**
     * 点击关于我们
     */
    public void onAboutUsClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_ABOUT_US);
    }

    /**
     * 点击退出登录
     */
    public void onLogoutClick() {
        //显示弹窗
        mAction.setValue(SettingsAction.SHOW_LOGOUT_DIALOG);
    }

    /**
     * 退出登录
     * 1.清除本地文件的用户数据
     * 2.更新Userfragment的页面数据
     */
    public void logout(){
        showLoading(true);
        mModel.logout(new IRequestCallback<ResBase<ResBase>>() {
            @Override
            public void onLoadFinish(ResBase<ResBase> datas) {
                //向外部发送退出登录的状态
                MessageEvent.LoginStatusEvent.post(false);
                showToast(datas.getMsg());
                //清除本地数据
                UserManager.getInstance().logout();
                refreshCache();
                showLoading(false);
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
            showLoading(false);
            showToast(meesage);
            }
        });
    }


    public MutableLiveData<String> getMobile() {
        return mMobile;
    }

    public MutableLiveData<String> getCacheSize() {
        return mCacheSize;
    }

    public MutableLiveData<Integer> getShowExitLoginBtn() {
        return mExitLoginBtnVisibility;
    }

    public MutableLiveData<SettingsAction> getAction() {
        return mAction;
    }

    /**
     * 枚举
     */
    public enum SettingsAction {
        FINISH,                      // 关闭页面
        SHOW_LOGOUT_DIALOG,         // 显示退出登录的弹窗
        NAVIGATION_TO_ACCOUNT,   // 跳转到账号与绑定
        NAVIGATION_TO_PASSWORD,// 跳转到设置密码页
        NAVIGATE_TO_PUSH_SETTING,   // 跳转到推送设置
        NAVIGATE_TO_PLAY_SETTING,   // 跳转到播放设置
        SHOW_CLEAR_CACHE_DIALOG,    // 显示清除缓存对话框
        NAVIGATE_TO_USER_AGREEMENT, // 跳转到用户协议
        NAVIGATE_TO_SIMPLE_PRIVACY_POLICY, // 跳转到概要隐私政策
        NAVIGATE_TO_PRIVACY_POLICY, // 跳转到隐私政策
        NAVIGATE_TO_PERMISSION_SETTING, // 跳转到权限设置
        NAVIGATE_TO_USER_INFO_MENU, // 跳转到用户信息清单
        NAVIGATE_TO_ABOUT_US,       // 跳转到关于我们
        NAVIGATE_TO_LOGIN   // 跳转到登录页
    }

}
