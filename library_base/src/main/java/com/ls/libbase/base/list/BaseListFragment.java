package com.ls.libbase.base.list;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.libbase.R;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.databinding.LayoutListSmartrefreshlayoutBinding;
import com.ls.network.bean.ResList;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

public abstract class BaseListFragment<T> extends BaseFragment<LayoutListSmartrefreshlayoutBinding,BaseListViewModel> {

    private RecyclerView.Adapter mAdapter;




    @Override
    protected int getLayoutResId() {
        return R.layout.layout_list_smartrefreshlayout;
    }

    @Override
    protected int getBindingVariableId() {
        return 0;
    }

    @Override
    protected void initView() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        mDataBinding.recyclerView.setLayoutManager(layoutManager);


        //初始化适配器
        mAdapter = getAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);

        mDataBinding.smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //已经是最后一页则不再发起无效请求，直接结束加载更多
                if (Boolean.FALSE.equals(mViewModel.getIsLoadMore().getValue())){
                    refreshLayout.finishLoadMore();
                    return;
                }
                //下拉触发加载更多
//                mViewModel.requestData(mPageType,false);
//                onLoadMoreDatas(false);
                mViewModel.requestDatas(false);
            }
        });

        mDataBinding.smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新
//                mViewModel.requestData(mPageType,true);
                mViewModel.requestDatas(true);
            }
        });
    }

    @Override
    protected void initData() {
        mViewModel.requestDatas(true);


        mViewModel.getDatas().observe(getViewLifecycleOwner(), new Observer<ResList<T>>() {
            @Override
            public void onChanged(ResList<T> videoResList) {
                SmartRefreshLayout smartRefreshLayout = mDataBinding.smartRefreshLayout;
                //记录本次是否处于加载更多场景，finishLoadMore 之后 isLoading 会变为 false
                boolean wasLoading = smartRefreshLayout.isLoading();
                if (smartRefreshLayout.isRefreshing()){
                    //正在刷新，如果监测到getDatas（）的变化说明数据加载完成，停止刷新
                    smartRefreshLayout.finishRefresh();
                }

                if (wasLoading){
                    //正在下拉加载更多数据，如果监测到getDatas（）的变化说明数据加载完成，停止加载
                    smartRefreshLayout.finishLoadMore();
                }

                List<T> list = videoResList.getList();
                onDatasRequestSuccess(list);
            }
        });

        mViewModel.getIsLoadMore().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                //不允许下拉加载更多数据，在viewmodel里代表数据已经全部加载完服务器没有数据了
                mDataBinding.smartRefreshLayout.setEnableLoadMore(aBoolean);

                //仅在加载更多过程中到达末页时提示一次（首次加载/刷新不弹）
                if (!aBoolean && !mViewModel.isFirstLoad()){
                    Toast.makeText(getContext(),"没有更多视频了！",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 由子类提供adapter
     *
     * @return
     */
    protected abstract RecyclerView.Adapter getAdapter();


    /**
     * 由子类提供LayoutManager
     *
     * @return
     */
    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract void onDatasRequestSuccess(List<T> list);


}
