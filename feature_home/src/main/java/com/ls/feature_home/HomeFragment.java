package com.ls.feature_home;

import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_home.databinding.LayoutFragmentHomeBinding;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

import java.util.ArrayList;

@Route(path = ARouterPath.Home.homeFragment)
public class HomeFragment extends BaseFragment<LayoutFragmentHomeBinding, HomeViewModel> {

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_fragment_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        Fragment recommendFragment = (Fragment) ARouter.getInstance().build(ARouterPath.Video.FRAGMENT_VIDEO_LIST)
                .withInt(ARouterPath.Video.KEY_VIDEO_LIST_TYPE, ARouterPath.Video.VIDEO_LIST_FRAGMENT_RECOMMEND)
                .navigation();
        Fragment dialyFragment = (Fragment) ARouter.getInstance().build(ARouterPath.Video.FRAGMENT_VIDEO_LIST)
                .withInt(ARouterPath.Video.KEY_VIDEO_LIST_TYPE, ARouterPath.Video.VIDEO_LIST_FRAGMENT_DAILY).navigation();

        //存放fragment的列表
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(recommendFragment);
        fragments.add(dialyFragment);


        mDataBinding.viewPager2.setAdapter(new FragmentStateAdapter(getActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments != null ? fragments.size() : 0;
            }
        });

        mDataBinding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mDataBinding.rbRecommen.setChecked(true);
                        break;
                    case 1:
                        mDataBinding.rbDifaly.setChecked(true);
                        break;
                }

            }
        });

        mDataBinding.rgIndicator.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == mDataBinding.rbRecommen.getId()) {
                    mDataBinding.viewPager2.setCurrentItem(0);
                } else if (i == mDataBinding.rbDifaly.getId()) {
                    mDataBinding.viewPager2.setCurrentItem(1);
                }
            }
        });

    }

    @Override
    protected int getBindingVariableId() {
        return 0;
    }
}
