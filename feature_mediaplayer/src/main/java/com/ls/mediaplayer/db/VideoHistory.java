package com.ls.mediaplayer.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "video_history")
public class VideoHistory {
    @PrimaryKey(autoGenerate = true)//主键，自增id
    int id;
    private int videoId;//视频Id
    private String userId;//用户id
    private String title;//视频标题
    private String cover;//视频封面
    private String tag;//视频标题
    private String duration;//视频时长
    private long viewTime;//浏览时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getViewTime() {
        return viewTime;
    }

    public void setViewTime(long viewTime) {
        this.viewTime = viewTime;
    }
}
