package com.ls.libbase.base;

/**
 * model和ViewModel通讯的接口回调
 *
 * @param <T>
 */
public interface IRequestCallback<T> {

    void onLoadFinish(T datas);

    void onLoadFailure(int errorCode, String meesage);
}
