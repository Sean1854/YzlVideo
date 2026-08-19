package com.ls.mediaplayer.ui.videolist;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.data_video.bean.ResVideo;
import com.ls.libbase.base.list.BaseListFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.mediaplayer.adapter.VideoApadter;

import java.util.List;

@Route(path = ARouterPath.Video.FRAGMENT_VIDEO_LIST)
public class VideoListFragment extends BaseListFragment<ResVideo> {
    @Autowired(name = ARouterPath.Video.KEY_VIDEO_LIST_TYPE)
    protected int mPageType;

    @Autowired(name = ARouterPath.Video.KEY_VIDEO_LIST_STYLE)
    protected boolean mStyle;//是否需要把列表的文字改为白色
    private VideoApadter mAdapter;


    @Override
    protected VideolistViewModel getViewModel() {
        return new ViewModelProvider(this).get(VideolistViewModel.class);
    }

    @Override
    protected void initData() {
        VideolistViewModel viewModel = (VideolistViewModel) mViewModel;
        viewModel.setPageType(mPageType);

        super.initData();


    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mAdapter = new VideoApadter(new VideoApadter.ItemClickListenner() {
            @Override
            public void onVideoClickId(int id) {
                //把适配器传递的点击视频id携带数据跳转到VideoDeatailActivity
                ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_VIDEODETAIL).
                        withInt(ARouterPath.Video.KEY_VIDEO_ID,id).
                        navigation();
            }
        });
        return mAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    protected void onDatasRequestSuccess(List<ResVideo> list) {
        mAdapter.setvideo(list);
        if (mStyle){
            mAdapter.setWhite(true);
        }


    }


    @Override
    protected void initView() {
        super.initView();


    }

    @Override
    protected int getBindingVariableId() {
        return 0;
    }
}
