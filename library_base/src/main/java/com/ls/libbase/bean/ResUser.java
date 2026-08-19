package com.ls.libbase.bean;

public class ResUser {


    /**
     * user : {"id":13,"nickname":"199****4306","bio":"","avatar":"https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg","status":"normal","username":"19959114306","url":"/u/13"}
     * fans : 126
     * follow : 18
     * medal : 0
     */

    private UserInfo user;//其他用户信息
    private int fans;//粉丝数
    private int follow;//关注数
    private int medal;//奖牌数

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getMedal() {
        return medal;
    }

    public void setMedal(int medal) {
        this.medal = medal;
    }

}
