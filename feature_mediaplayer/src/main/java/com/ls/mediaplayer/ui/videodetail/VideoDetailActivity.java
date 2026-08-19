package com.ls.mediaplayer.ui.videodetail;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.libbase.utils.StatusBarUtils;
import com.ls.mediaplayer.BR;
import com.ls.mediaplayer.R;
import com.ls.data_video.bean.ResVideoDetail;
import com.ls.mediaplayer.databinding.ActivityVideoDetailBinding;
import com.ls.mediaplayer.db.VideoHistoryRepository;
import com.ls.mediaplayer.ui.comment.VideoCommentFragment;
import com.ls.mediaplayer.ui.introduce.IntroduceFragment;
import com.ls.mediaplayer.ui.player.MediaPlayerManager;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

@Route(path = ARouterPath.Video.ACTIVITY_VIDEODETAIL)
public class VideoDetailActivity extends BaseActivity<ActivityVideoDetailBinding,VideoDetailViewModel> {


    @Autowired(name = ARouterPath.Video.KEY_VIDEO_ID)
    public int mVideoId;//接收跳转时，携带的视频Id
    private IntroduceFragment mIntroduceFragment;
    private VideoCommentFragment mCommentFragment;
    private MediaPlayerManager mPlayerManager;

    /**
     * 适用于以下启动模式：singleTop SingleTask SingleInstance
     * 如果启动一个已经存在的Activity实例，那么这个方法会被触发
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mVideoId = intent.getIntExtra(ARouterPath.Video.KEY_VIDEO_ID,0);
        mViewModel.requestDetail(mVideoId);
        //滚动到最顶上
        mDataBinding.nsl.scrollTo(0,0);
    }

    @Override
    protected VideoDetailViewModel getViewModel() {
        return new ViewModelProvider(this).get(VideoDetailViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initData() {
        mViewModel.requestDetail(mVideoId);

        mViewModel.getArchivesInfo().observe(this,archivesInfoBean -> {
            //如果有数据加载证明已经跳转视频详情页面，开始播放
            String url = archivesInfoBean.getVideo_file();
            mPlayerManager.play(url);
        });

    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        initviewpage();

        initTab();
        mDataBinding.ivBack.setOnClickListener(view -> finish());

        initPlayer();


    }


    private void initPlayer(){
        mPlayerManager = MediaPlayerManager.getInstance(this);
        mPlayerManager.setPlayerView(mDataBinding.playView);
    }

    private void initTab() {
        mDataBinding.tabLayout.setViewPager(mDataBinding.viewPager);
        ArrayList titles = new ArrayList();
        titles.add("简介");
        titles.add("评论");
        mDataBinding.tabLayout.setAdapter(new TabFlowAdapter(titles));
    }

    private void initviewpage() {
        mIntroduceFragment = (IntroduceFragment) ARouter.getInstance().build(ARouterPath.Video.FRAGMENT_INTRODUCE).navigation();
        mCommentFragment = (VideoCommentFragment) ARouter.getInstance().build(ARouterPath.Video.FRAGMENT_COMMENT).navigation();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(mIntroduceFragment);
        fragments.add(mCommentFragment);

        mDataBinding.viewPager.setAdapter(new FragmentStateAdapter(this) {
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


        mDataBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0){
                    mIntroduceFragment.updatasFragmentHeight();
                }else {
                    mCommentFragment.updatasFragmentHeight();
                }
            }
        });
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    /**
     * 接受是否登录的状态，更新页面
     * @param event
     */
    @Subscribe(sticky = true)//表示接受粘性事件
    public void onMessageEvent(MessageEvent.LoginStatusEvent event){
        mViewModel.requestDetail(mVideoId);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);//在当前页面注册Eventbus
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerManager.release();
    }
}
