package com.ls.feature_user.ui.account;

import com.ls.libbase.manager.UserManager;

public class AccountModel {

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
}
