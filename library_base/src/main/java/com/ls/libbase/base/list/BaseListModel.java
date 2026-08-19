package com.ls.libbase.base.list;

public abstract class BaseListModel {
    protected final int mLimit = 10;//一页10条数据
    protected int mPage = 1;//请求页数
    protected IListenner mListenner;


    public void setListenner(IListenner listenner) {
        this.mListenner = listenner;
    }
    public abstract void requestDatas(boolean isFirst);
}
