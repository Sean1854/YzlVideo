package com.ls.data_video.bean;

import com.ls.libbase.bean.UserInfo;

public class ResComment {

    /**
     * id : 19
     * user_id : 11
     * pid : 0
     * content : fhbvf
     * comments : 0
     * createtime : 1739797279
     * user : {"id":11,"nickname":"186****6510","avatar":"https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg","bio":"","email":"","url":"/u/11"}
     * create_date : 2周前
     */

    private int id;
    private int user_id;
    private int pid;
    private String content;
    private int comments;
    private int createtime;
    private UserInfo user;//使用统一的user实体类
    private String create_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

}
