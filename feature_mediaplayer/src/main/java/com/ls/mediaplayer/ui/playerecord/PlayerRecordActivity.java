package com.ls.mediaplayer.ui.playerecord;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;
import com.ls.mediaplayer.BR;
import com.ls.mediaplayer.R;
import com.ls.mediaplayer.adapter.VideoHistoryAdapter;
import com.ls.mediaplayer.databinding.ActivityPlayerRecordBinding;
import com.ls.mediaplayer.db.VideoHistory;

@Route(path = ARouterPath.Video.ACTIVITY_PLAYRECORD)
public class PlayerRecordActivity extends BaseActivity<ActivityPlayerRecordBinding,PlayerRecordViewModel> implements VideoHistoryAdapter.onVideoHistoryClick {

    private VideoHistoryAdapter mAdapter;

    @Override
    protected PlayerRecordViewModel getViewModel() {
        return new ViewModelProvider(this).get(PlayerRecordViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_player_record;
    }

    @Override
    protected void initData() {
        mViewModel.requestHistory();
        mViewModel.getDatas().observe(this,videoHistories -> {
            mAdapter.serDatas(videoHistories);
        });
        mViewModel.getSelectStatus().observe(this,isSelect ->{
            mAdapter.upIsSelect(isSelect);
        });

    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new VideoHistoryAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setVideoHistoryClick(this);

    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    public void onItemVideoHistoryClick(int id) {
        ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_VIDEODETAIL).withInt(ARouterPath.Video.KEY_VIDEO_ID,id).navigation();
    }

    @Override
    public void onIsSelectClick(VideoHistory videoHistory, boolean isSelect) {
        mViewModel.updataIsSelect(videoHistory,isSelect);
    }


}