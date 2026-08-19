package com.ls.libbase.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

public class Glideutils {

    /**
     * 加载普通图片
     * @param url 服务器返回的照片地址getImage
     * @param imageView 加载到哪个图片控件
     */
    public static void loadImage(String url, ImageView imageView){
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    /**
     * 加载圆形图片
     * @param url 服务器返回的照片地址getImage
     * @param imageView 加载到哪个图片控件
     */
    public static void loadCircleImage(String url, ImageView imageView){
        Glide.with(imageView.getContext())
                .load(url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//加载圆形图片
                .into(imageView);
    }
}
