package com.ls.feature_find.find;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_find.BR;
import com.ls.feature_find.R;
import com.ls.feature_find.adapter.AnchorAdapter;
import com.ls.feature_find.adapter.CategoryAdapter;
import com.ls.feature_find.bean.ResFindAnchor;
import com.ls.data_video.bean.ResFindCategory;
import com.ls.feature_find.databinding.LayoutFragmentFindBinding;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

import java.util.List;

@Route(path = ARouterPath.Find.findFragment)
public class FindFragment extends BaseFragment<LayoutFragmentFindBinding,FindViewModel> {



    private CategoryAdapter mCategoryAdapter;
    private AnchorAdapter mAnchorAdapter;

    @Override
    protected FindViewModel getViewModel() {
        return new ViewModelProvider(this).get(FindViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_fragment_find;
    }

    @Override
    protected void initData() {
        mViewModel.loadFindData();//发起数据请求

        mViewModel.getCategory().observe(getViewLifecycleOwner(), new Observer<List<ResFindCategory>>() {
            @Override
            public void onChanged(List<ResFindCategory> category) {
                mCategoryAdapter.setdatas(category);
            }
        });

        mViewModel.getAnchor().observe(getViewLifecycleOwner(), new Observer<List<ResFindAnchor>>() {
            @Override
            public void onChanged(List<ResFindAnchor> anchors) {
                mAnchorAdapter.setdatas(anchors);
            }
        });

    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        //分类
        mDataBinding.rvCategory.setLayoutManager(layoutManager);
        mCategoryAdapter = new CategoryAdapter();
        mDataBinding.rvCategory.setAdapter(mCategoryAdapter);

        //主题清单
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//指定recycleView的布局为横向
        mDataBinding.rvAnchor.setLayoutManager(linearLayoutManager);
        mAnchorAdapter = new AnchorAdapter();
        mDataBinding.rvAnchor.setAdapter(mAnchorAdapter);

        //点击主题歌单跳转
        mAnchorAdapter.setOnItemClick(new AnchorAdapter.OnItemClickListener() {
            @Override
            public void onThemeListClick() {
                ARouter.getInstance().build(ARouterPath.Find.ACTIVITY_THEME_LIST).navigation();
            }
        });

        mViewModel.getAction().observe(this, action -> {
            if (action == FindViewModel.FindAction.NAVIGATION_TO_THEME_LIST) {
                //跳转到主题播单
                ARouter.getInstance().build(ARouterPath.Find.ACTIVITY_THEME_LIST).navigation();
            } else if (action == FindViewModel.FindAction.NAVIGATION_TO_TOPIC) {
                //跳转到话题广场
                ARouter.getInstance().build(ARouterPath.Find.ACTIVITY_TOPIC).navigation();
            } else if (action == FindViewModel.FindAction.NAVIGATION_TO_SEARCH) {
                ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_SEARCH).navigation();
            }
        });


        //跳转分类详情
        mCategoryAdapter.setListenner(category -> {
            ARouter.getInstance().build(ARouterPath.Find.ACTIVITY_CATEGORY_DETAIL).
                    withParcelable(ARouterPath.Find.KEY_CATEGORY_DATA,category).navigation();
        });

    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }
}
