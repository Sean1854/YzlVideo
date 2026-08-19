package com.ls.data_video.bean;

import java.util.Objects;

/**
 * 分类详情当中的视频列表实体类，虽然也是代表视频的实体类，但
 * 这个实体类和ResVideoDetail.ArchivesInfoBean不相同，所以不和ArchivesInfoBean共用
 */
public class ResCategoryVideoDetail {

    /**
     * id : 145
     * title : 全民灌肠计划，这支广告玩的有点大
     * image : http://ali-img.kaiyanapp.com/035d50b81f26550ad6b6a984cfbf3cb4.jpeg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp
     * video_file : http://t-cdn.kaiyanapp.com/1682609003870_e0a45647.mp4
     * description : Travis Barker 这位全身遍布纹身的帅酷鼓手凭借着自己职业的态度、技术和天分，已成为摇滚舞台上最具影响力的鼓手之一，被誉为世界级朋克鼓王。在这支短片中，他说出了成功的秘诀「灌肠」，「全民灌肠计划」正在进行中，你还在等什么呢。From Liquid Death
     * comments : 0
     * likes : 1
     * collection : 0
     * createtime : 1732528037
     * user_id : 1
     * author : admin
     * avatar : https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg
     * islike : 0
     * iscollection : 0
     */

    private int id;
    private String title;
    private String image;
    private String video_file;
    private String description;
    private int comments;
    private int likes;
    private int collection;
    private int createtime;
    private int user_id;
    private String author;
    private String avatar;
    private int islike;
    private int iscollection;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo_file() {
        return video_file;
    }

    public void setVideo_file(String video_file) {
        this.video_file = video_file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStrComments() {
        return String.valueOf(comments);
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getStrLikes() {
        return String.valueOf(likes);
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public int getLikes() {
        return likes;
    }

    public int getCollection() {
        return collection;
    }

    public String getStrCollection() {
        return String.valueOf(collection);
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 等于1表示已点赞，0表示为点赞
     *
     * @return 是否点赞
     */
    public boolean getIslike() {
        return islike == 1;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    /**
     * @return 是否收藏
     */
    public boolean getIscollection() {
        return iscollection == 1;
    }

    public void setIscollection(int iscollection) {
        this.iscollection = iscollection;
    }

    //只要id一致，就认定为是同一条视频
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResCategoryVideoDetail that = (ResCategoryVideoDetail) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
