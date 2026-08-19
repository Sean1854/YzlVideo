package com.ls.feature_user.bean;

public class ResLogin {


    /**
     * token : 28876270-8f8c-4372-8b28-d7c55c0a4b94
     * id : 11
     */

    private String token;//用户身份标识

    private int id;//当前登录的用户id

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ResLogin{" +
                "token='" + token + '\'' +
                ", id=" + id +
                '}';
    }
}
