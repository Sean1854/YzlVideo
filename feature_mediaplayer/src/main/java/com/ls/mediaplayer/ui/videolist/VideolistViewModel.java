package com.ls.mediaplayer.ui.videolist;


import com.ls.libbase.base.list.BaseListViewModel;
import com.ls.data_video.bean.ResVideo;

public class VideolistViewModel extends BaseListViewModel<ResVideo,VideolistModel>{



    private int mPageType;



    public VideolistViewModel() {
        super(new VideolistModel());
    }



    public void setPageType(int pageType) {
        mPageType = pageType;
        mModel.setPageType(mPageType);
    }
}
