package com.ls.data_video.bean;

public class ReqComment {

    private int aid;
    private String content;//评论

    public ReqComment(int aid, String content) {
        this.aid = aid;
        this.content = content;
    }
}

