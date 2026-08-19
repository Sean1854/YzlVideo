package com.ls.feature_user.ui.login;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ls.feature_user.bean.ResLogin;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.ResUser;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.network.bean.ResBase;

public class LoginViewModel extends BaseViewModel {
    private static final String TAG = "LoginViewModel";
    private final loginModel mModel;

    private MutableLiveData<String> mUserMobile = new MutableLiveData<>();//用户输入的手机号
    private MutableLiveData<String> mCode = new MutableLiveData<>();//用户输入的验证码
    private MutableLiveData<Boolean> mLoginSuccess = new MutableLiveData<>(false);//是否登录成功

    private MutableLiveData<Boolean> mIsEnableLogin = new MutableLiveData<>(false);//登录按钮是否可用

    private MutableLiveData<Boolean> mCheckAgreement = new MutableLiveData<>(false);//登是否勾选协议

    private MutableLiveData<String> mGetVerticalCodeText = new MutableLiveData<>("获取验证码");//获取验证码控件的显示文本
    private MutableLiveData<Boolean> mIsEnableSendCode = new MutableLiveData<>(true);//获取验证码控件是否可用
    private CountDownTimer mDownTimer;//倒计时

    public LoginViewModel() {
        mModel = new loginModel();
    }

    /**
     * 登录的方法，在databinding中触发
     */
    public void login(){
        Boolean check = mCheckAgreement.getValue();
        if (check == false){
            Log.i(TAG, "login: 请勾选用户协议");
            showToast("请勾选用户协议");
            return;
        }

        String mobile = mUserMobile.getValue();//手机号
        String code = mCode.getValue();//验证码
        if (mobile == null || code == null){
            mIsEnableLogin.setValue(false);
            return;
        }


        //发起验证码登录
        mModel.mobileLogin(mobile, code, new IRequestCallback<ResBase<ResLogin>>() {
            @Override
            public void onLoadFinish(ResBase<ResLogin> datas) {
                showLoading(false);
                showToast(datas.getMsg());

                int id = datas.getData().getId();
                getUserInfo(String.valueOf(id));
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                showToast(meesage);
                showLoading(false);
            }
        });

    }

    private  void getUserInfo(String id) {
        showLoading(true);
       mModel.getUserInfo(id, new IRequestCallback<ResBase<ResUser>>() {
           @Override
           public void onLoadFinish(ResBase<ResUser> datas) {
               showLoading(false);
               mIsEnableLogin.setValue(true);
               //发送已登录的状态
               MessageEvent.LoginStatusEvent.post(true);
               mLoginSuccess.setValue(true);
           }

           @Override
           public void onLoadFailure(int errorCode, String meesage) {
               showLoading(false);
                showToast(meesage);
           }
       });
    }


    /**
     * 点击按钮发送验证码，开始倒计时
     */
    public void sendCode(){

        // 先把值取出来存临时变量
        String mobile = mUserMobile.getValue();
        // 先判空，再取长度，避免null调用length
        if (mobile == null || mobile.length() != 11){
            showToast("手机号格式不正确，请重新填写");
            return;
        }

        if (mDownTimer != null){
            mDownTimer.cancel();
        }

        mIsEnableSendCode.setValue(false);

        mDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long time) {
                int seconds = (int) (time / 1000);
                mGetVerticalCodeText.setValue(seconds + "秒");
            }

            @Override
            public void onFinish() {
                mGetVerticalCodeText.setValue("获取验证码");
                mIsEnableSendCode.setValue(true);
            }
        }.start();


        //发起请求
        showLoading(true);//显示加载控件
        mModel.sendSmsCode(mobile, new IRequestCallback<ResBase<ResBase>>() {
            @Override
            public void onLoadFinish(ResBase<ResBase> datas) {
                showToast(datas.getMsg());
                showLoading(false);
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                showToast(meesage);
                showLoading(false);
            }
        });
    }


    /**
     * 更新登录按钮的状态
     */
    public void upDataEnable(){
        String mobile = mUserMobile.getValue();
        String code = mCode.getValue();
        if (mobile == null || code == null){
            return;
        }

        boolean isEnable = mobile.length() == 11 && code.length() == 4;
        mIsEnableLogin.setValue(isEnable);

    }

    public MutableLiveData<Boolean> getIsEnableLogin() {
        return mIsEnableLogin;
    }

    public MutableLiveData<String> getUserMobile() {
        return mUserMobile;
    }

    public MutableLiveData<String> getCode() {
        return mCode;
    }

    public MutableLiveData<Boolean> getCheckAgreement() {
        return mCheckAgreement;
    }

    public MutableLiveData<String> getGetVerticalCodeText() {
        return mGetVerticalCodeText;
    }

    public MutableLiveData<Boolean> getIsEnableSendCode() {
        return mIsEnableSendCode;
    }

    public MutableLiveData<Boolean> getLoginSuccess() {
        return mLoginSuccess;
    }
}
