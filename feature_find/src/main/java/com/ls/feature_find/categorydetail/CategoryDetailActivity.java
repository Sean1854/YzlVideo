package com.ls.feature_find.categorydetail;

import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_find.BR;
import com.ls.feature_find.R;
import com.ls.data_video.bean.ResFindCategory;
import com.ls.feature_find.databinding.ActivityCategoryDetailBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

import java.util.ArrayList;

@Route(path = ARouterPath.Find.ACTIVITY_CATEGORY_DETAIL)
public class CategoryDetailActivity extends BaseActivity<ActivityCategoryDetailBinding,CategoryDetailViewModel> {
    @Autowired(name = ARouterPath.Find.KEY_CATEGORY_DATA)
    public ResFindCategory mCategory;//分类的详情数据

    @Override
    protected CategoryDetailViewModel getViewModel() {
        return new ViewModelProvider(this).get(CategoryDetailViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_category_detail;
    }

    @Override
    protected void initData() {
        mViewModel.requestData(mCategory.getId());

    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        initViewPage();
    }

    private void initViewPage() {
        //热门页面
        Fragment recommendFragment = (Fragment) ARouter.getInstance().build(ARouterPath.Video.FRAGMENT_CATEGORY_LIST).
                withInt(ARouterPath.Video.KEY_CATEGORY_TYPE, ARouterPath.Video.CATEGORY_VIDEO_RECOMMEND)
                .withInt(ARouterPath.Video.KEY_CATEGORY_ID, mCategory.getId()).navigation();
        //最新发布页面
        Fragment publishFragment = (Fragment) ARouter.getInstance().build(ARouterPath.Video.FRAGMENT_CATEGORY_LIST).
                withInt(ARouterPath.Video.KEY_CATEGORY_TYPE, ARouterPath.Video.CATEGORY_VIDEO_NEWPUBLISH)
                .withInt(ARouterPath.Video.KEY_CATEGORY_ID, mCategory.getId()).navigation();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(recommendFragment);
        fragments.add(publishFragment);

        mDataBinding.viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments == null ? 0 : fragments.size();
            }
        });

        mDataBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == mDataBinding.rbRecommend.getId()){
                    mDataBinding.barrier1.setVisibility(View.VISIBLE);
                    mDataBinding.barrier2.setVisibility(View.GONE);
                    mDataBinding.viewPager2.setCurrentItem(ARouterPath.Video.CATEGORY_VIDEO_RECOMMEND);
                } else if (i == mDataBinding.rbNew.getId()) {
                    mDataBinding.barrier1.setVisibility(View.GONE);
                    mDataBinding.barrier2.setVisibility(View.VISIBLE);
                    mDataBinding.viewPager2.setCurrentItem(ARouterPath.Video.CATEGORY_VIDEO_NEWPUBLISH);
                }
            }
        });
        mDataBinding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == ARouterPath.Video.CATEGORY_VIDEO_RECOMMEND){
                    mDataBinding.rbRecommend.setChecked(true);
                    mDataBinding.rbNew.setChecked(false);
                }else {
                    mDataBinding.rbNew.setChecked(true);
                    mDataBinding.rbRecommend.setChecked(false);
                }
            }
        });





    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }
}
