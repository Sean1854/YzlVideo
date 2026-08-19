package com.ls.mediaplayer.ui.categorylist;

import com.ls.data_video.bean.ResCategoryVideoDetail;
import com.ls.libbase.base.list.BaseListModel;
import com.ls.libbase.base.list.BaseListViewModel;

public class CategoryListViewModel extends BaseListViewModel<ResCategoryVideoDetail,CategoryListModel> {
    public CategoryListViewModel() {
        super(new CategoryListModel());
    }

    public void setArgments(int type,int id){
        mModel.setArgments(type,id);
    }


}
