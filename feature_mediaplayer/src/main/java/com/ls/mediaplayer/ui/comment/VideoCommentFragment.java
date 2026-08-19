package com.ls.mediaplayer.ui.comment;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.manager.UserManager;
import com.ls.mediaplayer.BR;
import com.ls.mediaplayer.R;
import com.ls.mediaplayer.adapter.CommentAdapter;
import com.ls.data_video.bean.ResComment;
import com.ls.mediaplayer.databinding.FragmentVideoCommentBinding;
import com.ls.mediaplayer.ui.report.DeleteCommentDialog;
import com.ls.mediaplayer.ui.videodetail.VideoDetailViewModel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

@Route(path = ARouterPath.Video.FRAGMENT_COMMENT)
public class VideoCommentFragment extends BaseFragment<FragmentVideoCommentBinding, VideoDetailViewModel> {

    private CommentAdapter mAdapter;

    @Override
    protected VideoDetailViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(VideoDetailViewModel.class);
    }

    /**
     * 本 Fragment 通过 requireActivity() 复用 Activity 的 ViewModel，
     * 故全局 loading / toast 交由 Activity 显示，避免重复出现两个加载样式。
     */
    @Override
    protected boolean isSharedViewModel() {
        return true;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_video_comment;
    }

    @Override
    protected void initData() {
        mViewModel.mComments.observe(getViewLifecycleOwner(), Comments -> {
            mAdapter.setdata(Comments);
        });
        mViewModel.requestComment(true);
        mDataBinding.smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新结束后  释放刷新状态
                if (mDataBinding.smartRefreshLayout.isRefreshing()) {
                    mDataBinding.smartRefreshLayout.finishRefresh();
                }
                mDataBinding.smartRefreshLayout.setEnableLoadMore(true);

                mViewModel.requestComment(true);
            }
        });

        mDataBinding.smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //加载更多
                if (mDataBinding.smartRefreshLayout.isLoading()) {
                    mDataBinding.smartRefreshLayout.finishLoadMore();
                }
                mViewModel.requestComment(false);
            }
        });

        mViewModel.getIsLoadMore().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoadMore) {
                mDataBinding.smartRefreshLayout.setEnableLoadMore(isLoadMore);
            }
        });


    }

    @Override
    protected void initView() {
        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CommentAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);

        mDataBinding.etChat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    String text = mDataBinding.etChat.getText().toString().trim();
                    if (text != null && text.length() > 0) {
                        if (UserManager.getInstance().isLogin()){
                            mViewModel.sendComment(text);
                            mDataBinding.etChat.getText().clear();//清空输入框内容
                        }else {
                            mViewModel.startLogin();
                        }
                    }
                    return true;
                } else {
                        return false;
                }
            }
        });

        mAdapter.setOnItemClickListenner(new CommentAdapter.onItemClickListenner() {
            @Override
            public void onItemLongClick(ResComment comment) {
                DeleteCommentDialog deleteCommentDialog = new DeleteCommentDialog();
                deleteCommentDialog.setComment(comment);
                deleteCommentDialog.show(getChildFragmentManager(), "DeleteCommentDialog");
            }
        });




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //用系统 IME insets 精确获取键盘高度，替代 onGlobalLayout + 导航栏魔数的估算方式
        ViewCompat.setOnApplyWindowInsetsListener(mDataBinding.clComment, (v, insets) -> {
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
            Insets navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
            //键盘弹出时输入栏整体上移：键盘高度 - 底部导航栏高度（键盘已覆盖导航栏区域）
            //键盘隐藏时 imeInsets.bottom 为 0，自动归位
            int offset = Math.max(0, imeInsets.bottom - navInsets.bottom);
            v.setTranslationY(-offset);
            return insets;
        });
    }



    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    /**
     * 当切换viewpage时候刷新当前高度
     */
    public void updatasFragmentHeight() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //重新计算当前根布局的所有父布局（所有相关布局）的大小和位置
                mDataBinding.getRoot().requestLayout();
            }
        }, 1000);
    }


}