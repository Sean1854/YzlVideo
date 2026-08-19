package com.ls.feature_user.ui.login;

import com.ls.feature_user.api.UserApiServiceProvider;
import com.ls.feature_user.bean.ReqMobileLogin;
import com.ls.feature_user.bean.ReqSendSmsCode;
import com.ls.feature_user.bean.ResLogin;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.ResUser;
import com.ls.libbase.manager.UserManager;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import retrofit2.Call;

public class loginModel {


    /**
     * 发送验证码
     * @param mobile 手机号
     * @param callback 接口回调
     */
    public void sendSmsCode(String mobile, IRequestCallback<ResBase<ResBase>> callback){
    ReqSendSmsCode sendSmsCode = new ReqSendSmsCode(mobile, "mobilelogin");
    Call<ResBase<ResBase>> call = UserApiServiceProvider.getApiService().sendSmsCode(sendSmsCode);
    ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResBase>>(){
        @Override
        public void onSuccess(ResBase<ResBase> result) {
            callback.onLoadFinish(result);
        }

        @Override
        public void onError(int errorCode, String meesage) {
            callback.onLoadFailure(errorCode,meesage);
        }
    });
}


    /**
     * 使用验证码登录
     * @param mobile
     * @param code
     * @param callback
     */
    public void mobileLogin(String mobile, String code, IRequestCallback<ResBase<ResLogin>> callback){
    ReqMobileLogin login = new ReqMobileLogin(mobile,code);
    Call<ResBase<ResLogin>> call = UserApiServiceProvider.getApiService().mobileLogin(login);
    ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResLogin>>() {
        @Override
        public void onSuccess(ResBase<ResLogin> result) {
            callback.onLoadFinish(result);

            //保存用户的token
            String token = result.getData().getToken();
            UserManager.getInstance().saveToken(token);

        }

        @Override
        public void onError(int errorCode, String meesage) {
            callback.onLoadFailure(errorCode,meesage);
        }
    });

}

public void getUserInfo(String userId, IRequestCallback<ResBase<ResUser>> callback){
    Call<ResBase<ResUser>> call = UserApiServiceProvider.getApiService().getUserInfo(userId, "archives");
    ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResUser>>() {
        @Override
        public void onSuccess(ResBase<ResUser> result) {

            ResUser user = result.getData();
            if (user != null){
                callback.onLoadFinish(result);
                UserManager.getInstance().saveUserInfo(result.getData());
            }
        }

        @Override
        public void onError(int errorCode, String meesage) {
        callback.onLoadFailure(errorCode,meesage);
        }
    });
}

}
