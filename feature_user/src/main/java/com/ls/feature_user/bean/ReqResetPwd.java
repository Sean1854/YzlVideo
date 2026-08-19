package com.ls.feature_user.bean;

public class ReqResetPwd {
    private String newpassword;
    private String mobile;
    private String captcha;
    private String type;


    public ReqResetPwd(String newpassword, String mobile, String captcha) {
        this.newpassword = newpassword;
        this.mobile = mobile;
        this.captcha = captcha;
        this.type = "mobile";
    }
}
