package com.ls.feature_plaza.fragment.plaza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.data_video.bean.ResFindCategory;
import com.ls.feature_plaza.R;
import com.ls.feature_plaza.adapter.PlazaAdapter;
import com.ls.feature_plaza.bean.ResPlaza;
import com.ls.feature_plaza.databinding.LayoutFragmentPlazaBinding;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

@Route(path = ARouterPath.Plaza.plazaFragment)
public class PlazaFragment extends BaseFragment<LayoutFragmentPlazaBinding,PlazaViewModel> {


    private PlazaAdapter mAdapter;

    @Override
    protected PlazaViewModel getViewModel() {
        return new ViewModelProvider(this).get(PlazaViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_fragment_plaza;
    }

    @Override
    protected void initData() {
        mViewModel.requestDatas();
        mViewModel.getDatas().observe(getViewLifecycleOwner(), new Observer<List<ResPlaza>>() {
            @Override
            public void onChanged(List<ResPlaza> datas) {
                //判断是否正在刷新
                if (mDataBinding.layoutRecycler.smartRefreshLayout.isRefreshing()){
                    //如果正在刷新，则马上停止
                    mDataBinding.layoutRecycler.smartRefreshLayout.finishRefresh();
                }
                mAdapter.setDatas(datas);
            }
        });

        // 观察「跳转搜索页」事件，由 View 层执行 UI 跳转（MVVM）
        mViewModel.mGoSearch.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean go) {
                if (Boolean.TRUE.equals(go)) {
                    ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_SEARCH).navigation();
                    mViewModel.mGoSearch.setValue(false); // 复位，避免重复触发
                }
            }
        });



    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        RecyclerView recyclerView = mDataBinding.layoutRecycler.recyclerView;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0){
                    return 2;//如果是第一行，网格布局里item占两个格子
                }else {
                    return 1;//其他的item占一个格子
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);//为recycleview添加布局管理器
        mAdapter = new PlazaAdapter();
        recyclerView.setAdapter(mAdapter);
        SmartRefreshLayout smartRefreshLayout = mDataBinding.layoutRecycler.smartRefreshLayout;
        smartRefreshLayout.setEnableLoadMore(false);//把加载更多的功能关闭
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mViewModel.requestDatas();
                //添加刷新的监听
            }
        });

        mAdapter.setListener(new PlazaAdapter.PlazaItemListener() {
            @Override
            public void onBannerClick(ResPlaza.PlazaDetail detail) {
                ResFindCategory category = new ResFindCategory();
                category.setId(detail.getId());
                category.setName(detail.getName());
                category.setImage(detail.getImage());
                category.setDescription(detail.getDescription());
                category.setIcon(detail.getIcon());
                category.setFullurl(detail.getFullurl());
                category.setUrl(detail.getUrl());
                //跳转到分类详情，并且传递对应的分类id
                ARouter.getInstance().build(ARouterPath.Find.ACTIVITY_CATEGORY_DETAIL)
                        .withParcelable(ARouterPath.Find.KEY_CATEGORY_DATA, category)
                        .navigation();
            }

            @Override
            public void onImageClick(ResPlaza.PlazaDetail detail) {
                ARouter.getInstance().build(ARouterPath.Plaza.IMAGE_ACTIVITY)
                        .withParcelable(ARouterPath.Plaza.KEY_IMAGE_DATA, detail)
                        .navigation();
                //指定页面打开时 从底部弹出的过渡动画
                getActivity().overridePendingTransition(R.anim.anim_activity_bottom2top, 0);
            }
        });

        // 左上角搜索图标：点击只通知 ViewModel，跳转由观察者执行（MVVM）
        mDataBinding.ivSearch.setOnClickListener(v -> mViewModel.goSearch());



}

    @Override
    protected int getBindingVariableId() {
        return 0;
    }
}
