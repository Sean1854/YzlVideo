package com.ls.feature_user.user;

import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.ResUser;
import com.ls.libbase.bean.UserInfo;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.libbase.manager.UserManager;
import com.ls.network.bean.ResBase;

public class UserViewModel extends BaseViewModel {

    private MutableLiveData<String> mAvatar = new MutableLiveData<>();//头像
    private MutableLiveData<String> mNickName = new MutableLiveData<>();//用户名
    private MutableLiveData<String> mBio = new MutableLiveData<>();//个人简介
    private MutableLiveData<String> mFans = new MutableLiveData<>();//粉丝数
    private MutableLiveData<String> mFollow = new MutableLiveData<>();//关注数
    private MutableLiveData<String> mMedal = new MutableLiveData<>();//奖牌数
    private MutableLiveData<Integer> mShowLogoutBtn = new MutableLiveData<>();//是否显示退出登录
    private MutableLiveData<UserCenterAction> mAction = new MutableLiveData<>();//处理点击事件

    private final UserModel mModel;

    public UserViewModel() {
        mModel = new UserModel();
        Boolean login = mModel.isLogin();
        loadUserInfo(login);

    }

    public MutableLiveData<String> getAvatar() {
        return mAvatar;
    }

    public MutableLiveData<String> getNickName() {
        return mNickName;
    }

    public MutableLiveData<String> getBio() {
        return mBio;
    }

    public MutableLiveData<Integer> getShowLogoutBtn() {
        return mShowLogoutBtn;
    }

    public MutableLiveData<UserCenterAction> getAction() {
        return mAction;
    }

    public MutableLiveData<String> getFans() {
        return mFans;
    }

    public MutableLiveData<String> getFollow() {
        return mFollow;
    }

    public MutableLiveData<String> getMedal() {
        return mMedal;
    }

    public void loadUserInfo(Boolean login) {
        mShowLogoutBtn.setValue(login ? View.VISIBLE : View.INVISIBLE);
        if (login){
            showLoading(true);
            mModel.loadUserInfo(new ILoadUserInfoCallback() {
                @Override
                public void onLoadSuccess(ResUser user) {
                    showLoading(false);
                    updataUserInfo(user);
                }

                @Override
                public void onLoadFailure(int errorCode, String message) {
                    showLoading(false);
                    notLoginUpdateUserInfo();
                }
            });

        }else {
            notLoginUpdateUserInfo();
        }
    }

    private void notLoginUpdateUserInfo() {
        ResUser resUser = new ResUser();
        resUser.setUser(new UserInfo());
        updataUserInfo(resUser);
    }

    private void updataUserInfo(ResUser user) {
        String avatar = user.getUser().getAvatar();

        if (avatar != null && !avatar.isEmpty()) {
            mAvatar.setValue(avatar);
        } else {
            mAvatar.setValue(null);
        }

        String nickname = user.getUser().getNickname();
        if (nickname != null && !nickname.isEmpty()) {
            mNickName.setValue(nickname);
        } else {
            mNickName.setValue("请先登录");
        }

        String bio = user.getUser().getBio();
        if (bio != null && !bio.isEmpty()) {
            mBio.setValue(bio);
        } else {
            mBio.setValue("请编辑资料完善个人信息吧！");
        }


        int fans = user.getFans();
        mFans.setValue(fans + " 粉丝");

        int follow = user.getFollow();
        mFollow.setValue(follow + " 关注");

        int medal = user.getMedal();
        mMedal.setValue(medal + " 勋章");

    }

    /**
     * 编辑资料
     */
    public void onEditUserInfoClick(){
        mAction.setValue(mModel.isLogin() ? UserCenterAction.NAVIGATION_TO_EDIT_INFO : UserCenterAction.NAVIGATE_TO_LOGIN);
    }


    /**
     * 退出登录
     */
    public void onLogoutClick() {
        mAction.setValue(UserCenterAction.SHOW_LOGOUT_DIALOG);
    }

    /**
     * 收藏页
     */
    public void onCollectionClick() {
        boolean login = mModel.isLogin();
        //如果未登录 就去登录页
        mAction.setValue(login ? UserCenterAction.NAVIGATION_TO_COLLECTION : UserCenterAction.NAVIGATE_TO_LOGIN);
    }

    /**
     * 播放记录页
     */
    public void onRecordClick() {
        mAction.setValue( UserCenterAction.NAVIGATION_TO_RECORD);
    }

    /**
     * 设置页
     */
    public void onSettingsClick() {
        mAction.setValue(UserCenterAction.NAVIGATE_TO_SETTINGS);
    }
    /**
     * 设置页
     */
    public void onUserInfoMenuClick() {
        mAction.setValue(UserCenterAction.NAVIGATE_TO_USER_INFO_MENU);
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
                showLoading(false);
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                showLoading(false);
                showToast(meesage);
            }
        });
    }

    /**
     * 枚举
     */
    public enum UserCenterAction {
        SHOW_LOGOUT_DIALOG,         // 显示退出登录的弹窗
        NAVIGATION_TO_EDIT_INFO,   // 跳转到账用户信息编辑
        NAVIGATION_TO_COLLECTION,// 跳转到收藏列表页
        NAVIGATION_TO_RECORD,// 跳转到播放记录页
        NAVIGATE_TO_LOGIN,   // 跳转到登录页
        NAVIGATE_TO_SETTINGS,   // 跳转到设置页
        NAVIGATE_TO_USER_INFO_MENU   // 跳转到用户信息收公示
    }
}
