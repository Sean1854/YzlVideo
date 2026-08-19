package com.ls.feature_plaza.ui.image;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_plaza.BR;
import com.ls.feature_plaza.R;
import com.ls.feature_plaza.bean.ResPlaza;
import com.ls.feature_plaza.databinding.ActivityImageBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;


@Route(path = ARouterPath.Plaza.IMAGE_ACTIVITY)
public class ImageActivity extends BaseActivity<ActivityImageBinding, ImageViewModel> {

    @Autowired(name = ARouterPath.Plaza.KEY_IMAGE_DATA)
    public ResPlaza.PlazaDetail mDetail;

    @Override
    protected ImageViewModel getViewModel() {
        return new ViewModelProvider(this).get(ImageViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        initViewPager();

    }

    private void initViewPager() {

        //直接关联数据到pager列表
        List<String> images = mDetail.getImages();

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            String url = images.get(i);
            Fragment fragment = (Fragment) ARouter.getInstance().build(ARouterPath.Plaza.FRAGMENT_IMAGE_DETAIL)
                    .withString(ARouterPath.Plaza.KEY_IMAGE_URL, url).navigation();
            fragments.add(fragment);
        }

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

        mDataBinding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                String format = String.format("%s/%s", position + 1, fragments.size());
                mDataBinding.tvTitle.setText(format);
            }
        });
    }

    @Override
    protected void initData() {
        //将上个页面接受到的数据传到viewModel,直接关联UI
        mViewModel.updateData(mDetail);
    }

    @Override
    public void finish() {
        super.finish();
        //指定退出时 从顶部到底部过渡的动画
        overridePendingTransition(0, R.anim.anim_activity_top2bottom);
    }
}
