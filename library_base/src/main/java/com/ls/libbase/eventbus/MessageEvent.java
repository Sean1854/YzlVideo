package com.ls.libbase.eventbus;

import org.greenrobot.eventbus.EventBus;

public class MessageEvent {
    public static class LoginStatusEvent{
        private Boolean isLogin;//是否登录

        public LoginStatusEvent(Boolean isLogin) {
            this.isLogin = isLogin;
        }

        public Boolean getLogin() {
            return isLogin;
        }

        public static void post(Boolean isLogin){
            //发送粘性事件，确定页面被加载时再处理事件
            EventBus.getDefault().postSticky(new LoginStatusEvent(isLogin));
        }

    }
}
