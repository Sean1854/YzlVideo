package com.ls.feature_user.bean;

/**
 * 发起获取验证码请求的请求体
 */
public class ReqSendSmsCode {

    private String mobile;//手机号
    private String event;//事件名称  一般使用mobilelogin就可以

    public ReqSendSmsCode(String mobile, String event) {
        this.mobile = mobile;
        this.event = event;
    }
}
