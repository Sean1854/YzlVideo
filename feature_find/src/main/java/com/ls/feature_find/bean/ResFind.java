package com.ls.feature_find.bean;

import com.ls.data_video.bean.ResFindCategory;

import java.util.List;

public class ResFind {
    private List<ResFindCategory> category;//分类数据列表
    private List<ResFindAnchor> anchor;//主题播单
    private List<ResFindTopic> topic;//话题广场

    public List<ResFindCategory> getCategory() {
        return category;
    }

    public void setCategory(List<ResFindCategory> category) {
        this.category = category;
    }

    public List<ResFindAnchor> getAnchor() {
        return anchor;
    }

    public void setAnchor(List<ResFindAnchor> anchor) {
        this.anchor = anchor;
    }

    public List<ResFindTopic> getTopic() {
        return topic;
    }

    public void setTopic(List<ResFindTopic> topic) {
        this.topic = topic;
    }


}
