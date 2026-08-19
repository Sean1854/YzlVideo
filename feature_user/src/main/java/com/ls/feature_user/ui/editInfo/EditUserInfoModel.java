package com.ls.feature_user.ui.editInfo;

import com.ls.feature_user.api.UserApiServiceProvider;
import com.ls.feature_user.bean.ReqUpdateUserProfile;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.ResUser;
import com.ls.libbase.bean.UserInfo;
import com.ls.libbase.manager.UserManager;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import retrofit2.Call;

public class EditUserInfoModel {

    /**
     * 获取用户信息
     * @return
     */
    public UserInfo getUserInfo(){
        UserInfo userInfo = null;
        if (isLogin()) {
            userInfo = UserManager.getInstance().getUserInfo().getUser();
        }
        return userInfo;
    }


    /**
     * 是否登录
     * @return
     */
    public Boolean isLogin(){
        return UserManager.getInstance().isLogin();
    }


    /**
     * 更新用户信息
     * @param avatar
     * @param nickname
     * @param bio
     * @param callback
     */
    public void updateUserInfo(String avatar, String nickname, String bio, IRequestCallback<ResBase> callback){
        if (isLogin()){
            String token = UserManager.getInstance().getToken();
            ReqUpdateUserProfile userProfile = new ReqUpdateUserProfile();
            userProfile.setAvatar(avatar);
            userProfile.setNickname(nickname);
            userProfile.setBio(bio);
            Call<ResBase<ResUser>> call = UserApiServiceProvider.getApiService().updateUserProfile(token, userProfile);
            ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResUser>>() {
                @Override
                public void onSuccess(ResBase<ResUser> result) {
                    UserManager.getInstance().updateUserInfo(avatar, nickname, bio);
                    callback.onLoadFinish(result);
                }

                @Override
                public void onError(int errorCode, String meesage) {
                    callback.onLoadFailure(errorCode, meesage);
                }
            });
        }
    }

}
