package com.ls.feature_user.user;

import com.ls.feature_user.api.UserApiServiceProvider;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.ResUser;
import com.ls.libbase.manager.UserManager;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;
import com.ls.network.config.ErrorStatusConfig;

import retrofit2.Call;

public class UserModel {

    public Boolean isLogin(){
        return UserManager.getInstance().isLogin();//是否登录
    }
    public void loadUserInfo(ILoadUserInfoCallback callback) {
        if (isLogin()){

            ResUser user = UserManager.getInstance().getUserInfo();
            if (user != null){
                callback.onLoadSuccess(user);
            }else {
                callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN,"请先登录");
            }
        }else {
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN,"请先登录");
        }
    }

    /**
     * 退出登录
     */
    public void logout(IRequestCallback<ResBase<ResBase>> callback){
        String token = UserManager.getInstance().getToken();
        //发起请求
        Call<ResBase<ResBase>> call = UserApiServiceProvider.getApiService().logout(token);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResBase>>() {
            @Override
            public void onSuccess(ResBase<ResBase> result) {
                callback.onLoadFinish(result);
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });

    }
}
