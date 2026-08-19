package com.ls.libbase.base.list;


import com.ls.network.bean.ResList;

public interface IListenner<T> {
    /**
     * 网络请求成功
     * @param videos
     */
    void onLoadFinish(boolean isFirst,ResList<T> videos);

    /**
     * 网络请求失败
     * @param statusCode 失败返回的请求码（自己定义）
     *
     */
    void onLoadFailure(int statusCode);
}
