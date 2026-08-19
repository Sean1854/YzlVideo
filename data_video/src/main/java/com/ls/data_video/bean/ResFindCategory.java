package com.ls.data_video.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * 分类数据实体类
 */
public class ResFindCategory implements Parcelable {

    /**
     * id : 25
     * name : 广告
     * image : https://ali-img.kaiyanapp.com/23d1a1dce9756535d314aed3cf9777a0.jpeg?image_process=image/auto-orient
     * icon : http://ali-img.kaiyanapp.com/c13345e2c2e812ef4e179a4eac2b81f1.png
     * description : 为广告人的精彩创意点赞
     * url : /cms/25.html
     * fullurl : https://titok.fzqq.fun/cms/25.html
     */

    private int id;//id
    private String name;//名字
    private String image;//封面图
    private String icon;//图标
    private String description;//描述
    private String url;
    private String fullurl;

    private int people;//参与人数
    private int browse;//浏览人数
    private int collection;//分类下的总收藏（安卓端目前用不到）
    private int likes;//分类下的总点赞（安卓端目前用不到）
    public ResFindCategory() {
    }

    protected ResFindCategory(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
        icon = in.readString();
        description = in.readString();
        url = in.readString();
        fullurl = in.readString();
    }

    public static final Creator<ResFindCategory> CREATOR = new Creator<ResFindCategory>() {
        @Override
        public ResFindCategory createFromParcel(Parcel in) {
            return new ResFindCategory(in);
        }

        @Override
        public ResFindCategory[] newArray(int size) {
            return new ResFindCategory[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getBrowse() {
        return browse;
    }

    public void setBrowse(int browse) {
        this.browse = browse;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullurl() {
        return fullurl;
    }

    public void setFullurl(String fullurl) {
        this.fullurl = fullurl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(icon);
        parcel.writeString(description);
        parcel.writeString(url);
        parcel.writeString(fullurl);
    }
}
