package com.ls.feature_user.bean;

/**
 * 修改用户信息的body
 */
public class ReqUpdateUserProfile {

    private String avatar;
    private String nickname;
    private String bio;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
