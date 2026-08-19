package com.ls.feature_user.ui.resetpwd;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.network.bean.ResBase;

public class ResetPasswordViewModel extends BaseViewModel {

    private static final String TAG = "ResetPasswordViewModel";
    private final ResetPasswordModel mModel;
    private MutableLiveData<String> mMobile = new MutableLiveData<>();
    private MutableLiveData<String> mCode = new MutableLiveData<>();//用户输入的验证码
    private MutableLiveData<Boolean> mIsEnableLogin = new MutableLiveData<>(false);//登录按钮是否可用。默认不可用
    private MutableLiveData<String> mGetVerticalCodeText = new MutableLiveData<>("获取验证码");//获取验证码控件的显示文本
    private MutableLiveData<Boolean> mIsEnableSendCode = new MutableLiveData<>(true);//获取验证码控件是否可用
    private MutableLiveData<Boolean> mLoginSuccess = new MutableLiveData<>(false);//登录状态

    private MutableLiveData<String> mPassword1 = new MutableLiveData<>();
    private MutableLiveData<String> mPassword2 = new MutableLiveData<>();
    private CountDownTimer mDownTimer;//获取验证码的倒计时


    public ResetPasswordViewModel() {
        mModel = new ResetPasswordModel();

        mMobile.setValue(mModel.getMobile());
    }


    public MutableLiveData<String> getMobile() {
        return mMobile;
    }

    /**
     * 发送验证码
     */
    public void sendCode() {

        String mobile = mMobile.getValue();
        if (mobile == null || mobile.length() != 11) {
            Log.i(TAG, "sendCode: 手机号不符合规则！");
            showToast("请输入正确的手机号码！");
            return;
        }

        if (mDownTimer != null) {
            mDownTimer.cancel();//防止重复点击时 未停止之前的计时
        }
        //禁用发送按钮
        mIsEnableSendCode.setValue(false);

        mDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                //每1秒会被触发 把毫秒转为秒
                int seconds = (int) (millisUntilFinished / 1000);
                mGetVerticalCodeText.setValue(seconds + "s");//更新倒计时的显示
            }

            @Override
            public void onFinish() {
                //倒计时完成后
                mGetVerticalCodeText.setValue("获取验证码");
                //60s后允许发送验证码
                mIsEnableSendCode.setValue(true);
            }
        }.start();

        //发起请求，让服务端发送验证码
        Log.i(TAG, "sendCode: ");
        showLoading(true);
        //在这发起获取验证码请求
        mModel.sendSmsCode(new IRequestCallback<ResBase<ResBase>>() {
            @Override
            public void onLoadFinish(ResBase<ResBase> datas) {
                showToast(datas.getMsg());
                showLoading(false);
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showToast(message);
                showLoading(false);
            }
        });
    }

    /**
     * 重置密码
     */
    public void resetPassword() {
        String password1 = mPassword1.getValue();
        String password2 = mPassword2.getValue();
        String code = mCode.getValue();

        if (code == null || code.isEmpty()) {
            showToast("验证码不能为空");
            return;
        }

        if (password1 == null || password1.isEmpty()) {
            showToast("密码不能为空");
            return;
        }

        if (password2 == null || password2.isEmpty()) {
            showToast("请确认密码");
            return;
        }

        if (!password1.equals(password2)) {
            showToast("两次输入的密码不一致！");
            return;
        }

        showLoading(true);
        mModel.resetPassword(password1, code, new IRequestCallback<ResBase<ResBase>>() {
            @Override
            public void onLoadFinish(ResBase<ResBase> datas) {
                showToast(datas.getMsg());
                showLoading(false);
                onFinishPage();//关闭页面
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showToast(message);
                showLoading(false);
            }
        });
    }

    public MutableLiveData<String> getCode() {
        return mCode;
    }

    public MutableLiveData<Boolean> getIsEnableLogin() {
        return mIsEnableLogin;
    }

    public MutableLiveData<String> getGetVerticalCodeText() {
        return mGetVerticalCodeText;
    }

    public MutableLiveData<Boolean> getIsEnableSendCode() {
        return mIsEnableSendCode;
    }

    public MutableLiveData<String> getPassword1() {
        return mPassword1;
    }

    public MutableLiveData<String> getPassword2() {
        return mPassword2;
    }

    /**
     * 是否允许点击重置密码的按钮
     */
    public void updateEnableResetBtnStatus() {
        String password1 = mPassword1.getValue();
        String password2 = mPassword2.getValue();
        String code = mCode.getValue();

        if (code == null || password1 == null || password2 == null) {
            return;
        }
        //如果验证码不是4位数，并且两次输入的密码不一致，就不让点击重置按钮
        boolean isEnable = code.length() == 4 && password1.equals(password2);
        mIsEnableLogin.setValue(isEnable);
    }
}
