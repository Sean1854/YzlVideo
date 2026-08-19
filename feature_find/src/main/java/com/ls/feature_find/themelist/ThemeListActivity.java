package com.ls.feature_find.themelist;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_find.BR;
import com.ls.feature_find.R;
import com.ls.feature_find.adapter.ThemeListAdapter;
import com.ls.feature_find.databinding.ActivityThemeListBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

@Route(path = ARouterPath.Find.ACTIVITY_THEME_LIST)
public class ThemeListActivity extends BaseActivity<ActivityThemeListBinding,ThemeListViewModel> {

    private ThemeListAdapter mAdapter;

    @Override
    protected ThemeListViewModel getViewModel() {
        return new ViewModelProvider(this).get(ThemeListViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_theme_list;
    }

    @Override
    protected void initData() {
        mViewModel.requestData();
        mViewModel.getThemeData().observe(this,resThemeData -> {
            mAdapter.setData(resThemeData);
        });
        mAdapter.setOnItemClickListener(new ThemeListAdapter.OnItemClickListener() {
            @Override
            public void onVideoClick(int videoId) {
                ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_VIDEODETAIL)
                        .withInt(ARouterPath.Video.KEY_VIDEO_ID,videoId)
                        .navigation();
            }
        });
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ThemeListAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);


    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }
}
