package com.ls.feature_user.ui.resetpwd;

import com.ls.feature_user.api.UserApiServiceProvider;
import com.ls.feature_user.bean.ReqResetPwd;
import com.ls.feature_user.bean.ReqSendSmsCode;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.manager.UserManager;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import retrofit2.Call;

public class ResetPasswordModel {

    public boolean isLogin() {
        return UserManager.getInstance().isLogin();//是否登录
    }

    /**
     * 如果未登录 返回null
     *
     * @return 返回手机号
     */
    public String getMobile() {
        if (isLogin()) {
            String mobile = UserManager.getInstance().getUserInfo().getUser().getUsername();
            //长度不足时不做掩码，直接返回，防止 substring 越界闪退
            if (mobile == null || mobile.length() < 8) {
                return mobile;
            }
            //把username中间的4位替换成****

            StringBuilder builder = new StringBuilder();
            builder.append(mobile.substring(0, 3));
            builder.append("****");
            builder.append(mobile.substring(7));
            String string = builder.toString();
            return string;//返回手机号，userName就是手机号
        }
        return null;
    }

    /**
     * 发送验证码
     *
     * @param callback
     */
    public void sendSmsCode(IRequestCallback<ResBase<ResBase>> callback) {
        //获取手机号
        String mobile = UserManager.getInstance().getUserInfo().getUser().getUsername();
        ReqSendSmsCode smsCode = new ReqSendSmsCode(mobile, "resetpwd");
        Call<ResBase<ResBase>> call = UserApiServiceProvider.getApiService().sendSmsCode(smsCode);
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

    /**
     * 重置密码
     * @param password
     * @param code
     * @param callback
     */
    public void resetPassword(String password, String code, IRequestCallback<ResBase<ResBase>> callback) {
        String token = UserManager.getInstance().getToken();
        String mobile = UserManager.getInstance().getUserInfo().getUser().getUsername();
        ReqResetPwd pwd = new ReqResetPwd(password, mobile, code);
        Call<ResBase<ResBase>> call = UserApiServiceProvider.getApiService().resetPassword(token, pwd);
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
