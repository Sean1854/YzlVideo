package com.ls.mediaplayer.ui.categorylist;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.data_video.bean.ResCategoryVideoDetail;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.list.BaseListFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.mediaplayer.adapter.CategoryVideosAdapter;

import java.util.List;

@Route(path = ARouterPath.Video.FRAGMENT_CATEGORY_LIST)
public class CategoryListFragment extends BaseListFragment<ResCategoryVideoDetail> implements CategoryVideosAdapter.onCategoryClick {
    @Autowired(name = ARouterPath.Video.KEY_CATEGORY_TYPE)
    public int mType;//区分是热门发布，还是最新发布
    @Autowired(name = ARouterPath.Video.KEY_CATEGORY_ID)
    public int mChannelId;//分类id
    private CategoryVideosAdapter mAdpater;

    @Override
    protected void initData() {
        //把页面type和channelid传到model，准备发起数据请求
        CategoryListViewModel viewModel = (CategoryListViewModel) mViewModel;
        viewModel.setArgments(mType, mChannelId);
        super.initData();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected CategoryListViewModel getViewModel() {
        return new ViewModelProvider(this).get(CategoryListViewModel.class);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mAdpater = new CategoryVideosAdapter(this);
        return mAdpater;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    protected void onDatasRequestSuccess(List list) {
        mAdpater.setData(list);
    }

    //点击回调
    @Override
    public void onItemCategoryClick(int id) {
        ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_VIDEODETAIL).withInt(ARouterPath.Video.KEY_VIDEO_ID,id).navigation();
    }
}
