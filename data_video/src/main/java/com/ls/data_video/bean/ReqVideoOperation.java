package com.ls.data_video.bean;

public class ReqVideoOperation {
    private int id;
    private String type;
    private int aid;
    private String content;//评论

    //点赞
    public ReqVideoOperation(int id, String type) {
        this.id = id;
        this.type = type;
    }

    //取消点赞、取消收藏、评论
    public ReqVideoOperation(int aid) {
        this.aid = aid;
    }


    /**
     * 收藏
     *
     * @param type 类型默认传 archives
     * @param aid
     */
    public ReqVideoOperation(String type, int aid) {
        this.type = type;
        this.aid = aid;
    }

    /**
     * 设置评论内容
     *
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
}
