package com.ls.feature_user.ui.editInfo;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseApplication;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.ResUser;
import com.ls.libbase.bean.UserInfo;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.libbase.manager.UserManager;
import com.ls.network.bean.ResBase;

public class EditUserInfoViewModel extends BaseViewModel {

    private final EditUserInfoModel mModel;

    private MutableLiveData<String> mNickName = new MutableLiveData<>();//当前昵称
    private MutableLiveData<String> mBio = new MutableLiveData<>();//当前简介
    private MutableLiveData<String> mAvatarUrl = new MutableLiveData<>();//当前头像url地址

    private MutableLiveData<EditUserAction> mAction = new MutableLiveData<>();//


    public EditUserInfoViewModel() {
        mModel = new EditUserInfoModel();
        refresh();//初始化页面数据
    }

    private void refresh() {
        if (mModel.isLogin()) {
            UserInfo userInfo = mModel.getUserInfo();
            mAvatarUrl.setValue(userInfo.getAvatar());
            mNickName.setValue(userInfo.getNickname());
            mBio.setValue(userInfo.getBio());
        } else {
            mAvatarUrl.setValue(null);
            mNickName.setValue(null);
            mBio.setValue(null);
        }
    }


    /**
     * @return 资料是否有变化 如果有的，在按返回键的时候，会提示是否保存
     */
    public Boolean isChange() {
        Boolean change = false;
        UserInfo userInfo = mModel.getUserInfo();

        //如果头像不为空 并且和旧的资料不一致时，表示有更改
        String avatarUrlValue = mAvatarUrl.getValue();
        if (avatarUrlValue != null && !avatarUrlValue.equals(userInfo.getAvatar())) {
            change = true;
        }

        String nickName = mNickName.getValue();
        if (nickName != null && !nickName.equals(userInfo.getNickname())) {
            change = true;
        }
        String bio = mBio.getValue();
        if (bio != null && !bio.equals(userInfo.getBio())) {
            change = true;
        }

        return change;
    }

    public void updateUserInfo() {
        if (isChange()) {
            String avatarUrl = mAvatarUrl.getValue();
            String nickname = mNickName.getValue();
            String bio = mBio.getValue();
            showLoading(true);
            mModel.updateUserInfo(avatarUrl, nickname, bio, new IRequestCallback<ResBase>() {
                @Override
                public void onLoadFinish(ResBase datas) {
                    showLoading(false);
                    showToast(datas.getMsg());
                    refresh();
                    MessageEvent.LoginStatusEvent.post(true);
                    mAction.setValue(EditUserAction.FINISH);
                }

                @Override
                public void onLoadFailure(int errorCode, String meesage) {
                    showLoading(false);
                    showToast(meesage);
                }
            });
        }
    }

    /**
     * 提交当前资料到服务器（头像/昵称/简介），不自动关闭页面，结果通过 callback 回调。
     * 用于仅更新头像等不需要走“保存后关闭”流程的场景。
     */
    public void updateUserInfo(IRequestCallback<ResBase> callback) {
        String avatarUrl = mAvatarUrl.getValue();
        String nickname = mNickName.getValue();
        String bio = mBio.getValue();
        mModel.updateUserInfo(avatarUrl, nickname, bio, new IRequestCallback<ResBase>() {
            @Override
            public void onLoadFinish(ResBase datas) {
                refresh();
                MessageEvent.LoginStatusEvent.post(true);
                callback.onLoadFinish(datas);
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }



    public MutableLiveData<String> getNickName() {
        return mNickName;
    }

    public MutableLiveData<String> getBio() {
        return mBio;
    }

    public MutableLiveData<String> getAvatarUrl() {
        return mAvatarUrl;
    }

    public MutableLiveData<EditUserAction> getAction() {
        return mAction;
    }

    /**
     * 枚举
     */
    public enum EditUserAction {
        FINISH,                      // 关闭页面
        SHOW_AVATAR_SELECT_DIALOG //显示是否保存弹窗
    }
}
