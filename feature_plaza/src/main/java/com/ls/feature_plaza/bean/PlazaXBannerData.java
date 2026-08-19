package com.ls.feature_plaza.bean;

import com.stx.xhb.androidx.entity.BaseBannerInfo;

public class PlazaXBannerData implements BaseBannerInfo {

    private String imgUrl;//广告图片的地址
    private String title;//广告的标题
    private String description;//广告的详情介绍

    public PlazaXBannerData(String imgUrl, String title, String description) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getXBannerUrl() {
        return imgUrl;
    }

    @Override
    public String getXBannerTitle() {
        return title;
    }
}
