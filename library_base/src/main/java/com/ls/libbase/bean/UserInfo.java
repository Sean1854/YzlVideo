package com.ls.libbase.bean;

public class UserInfo {

    /**
     * id : 13
     * nickname : 199****4306
     * bio :
     * avatar : https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg
     * status : normal
     * username : 19959114306
     * url : /u/13
     */

    private String id; //用户id
    private String nickname;//昵称
    private String bio;//签名
    private String avatar;//头像
    private String status;//状态
    private String username;//用户名

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
