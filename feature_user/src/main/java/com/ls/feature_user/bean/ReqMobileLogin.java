package com.ls.feature_user.bean;

public class ReqMobileLogin {
    private String mobile;//手机号
    private String captcha;//验证码

    public ReqMobileLogin(String mobile, String captcha) {
        this.mobile = mobile;
        this.captcha = captcha;
    }
}
