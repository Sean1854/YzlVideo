package com.ls.mediaplayer.ui.introduce;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.manager.UserManager;
import com.ls.mediaplayer.BR;
import com.ls.mediaplayer.R;
import com.ls.data_video.bean.ResVideoDetail;
import com.ls.mediaplayer.databinding.FragmentIntroduceBinding;
import com.ls.mediaplayer.ui.report.CommentReportPopupWindow;
import com.ls.mediaplayer.ui.videodetail.VideoDetailViewModel;
import com.ls.mediaplayer.ui.videolist.VideoListFragment;

@Route(path = ARouterPath.Video.FRAGMENT_INTRODUCE)
public class IntroduceFragment extends BaseFragment<FragmentIntroduceBinding, VideoDetailViewModel> {


    private CommentReportPopupWindow mPopupWindow;

    @Override
    protected VideoDetailViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(VideoDetailViewModel.class);
    }

    /**
     * 本 Fragment 通过 requireActivity() 复用 Activity 的 ViewModel，
     * 故全局 loading / toast 交由 Activity 显示，避免与 Activity 重复弹 Toast。
     */
    @Override
    protected boolean isSharedViewModel() {
        return true;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_introduce;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        //给viewpage设置视频列表fragment
        VideoListFragment recomenrFragment = (VideoListFragment) ARouter.getInstance().build(ARouterPath.Video.FRAGMENT_VIDEO_LIST).
                withInt(ARouterPath.Video.KEY_VIDEO_LIST_TYPE, ARouterPath.Video.VIDEO_LIST_FRAGMENT_RECOMMEND)
                .withBoolean(ARouterPath.Video.KEY_VIDEO_LIST_STYLE,true)
                .navigation();

        getChildFragmentManager().beginTransaction().add(mDataBinding.fcv.getId(),recomenrFragment).commit();
        mDataBinding.ivComments.setOnClickListener(click ->{
            if (UserManager.getInstance().isLogin()){
                showCommentPopupWindow();
            }else {
                mViewModel.startLogin();
            }
        });
    }

    /**
     * 显示评论弹窗
     */
    private void showCommentPopupWindow() {
        if (mPopupWindow == null){
            mPopupWindow = new CommentReportPopupWindow((AppCompatActivity) getActivity());
            mPopupWindow.setOnPopupInteractionListener(new CommentReportPopupWindow.OnPopupInteractionListener() {
                @Override
                public void onSendMessage(String message) {
                    mViewModel.sendComment(message);
                }
            });
        }
        mPopupWindow.showPopup(mDataBinding.getRoot());
    }

    @Override
    protected void initData() {

    }

    /**
     * 当切换viewpage时候刷新当前高度
     */
    public void updatasFragmentHeight(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //重新计算当前根布局的所有父布局（所有相关布局）的大小和位置
                mDataBinding.getRoot().requestLayout();

            }
        },500);
    }


}