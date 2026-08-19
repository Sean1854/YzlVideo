package com.ls.feature_user.user;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.databinding.LayoutFragmentUserBinding;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.libbase.ui.dialog.YesOrNoDialog;
import com.ls.libbase.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@Route(path = ARouterPath.User.userFragment)
public class UserFragment extends BaseFragment<LayoutFragmentUserBinding,UserViewModel> {
    private static final String TAG = "UserFragment";

    @Override
    protected UserViewModel getViewModel() {
        return new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_fragment_user;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2Views(mDataBinding.getRoot(),mDataBinding.ivSettings,mDataBinding.ivQualifications);
        mViewModel.getAction().observe(getViewLifecycleOwner(), new Observer<UserViewModel.UserCenterAction>() {
            @Override
            public void onChanged(UserViewModel.UserCenterAction mAction) {
                if (mAction == null) {
                    return;
                }
                switch (mAction){
                    case NAVIGATE_TO_LOGIN://跳转登录页面
                        mViewModel.showToast("请先登录后才能操作！");
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_LOGIN).navigation();
                        break;
                    case SHOW_LOGOUT_DIALOG://退出登录弹窗
                        showLogoutDialog();
                        break;
                    case NAVIGATE_TO_SETTINGS://跳转设置页面
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_SETTINGS).navigation();
                        break;
                    case NAVIGATION_TO_EDIT_INFO://跳转用户信息编辑
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_EDITUSERINFO).navigation();
                        break;
                    case NAVIGATION_TO_RECORD://浏览记录
                        ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_PLAYRECORD).navigation();
                        break;


                }
                //消费后复位，防止视图重建时 LiveData 粘性重放导致重复跳转/重复弹 Toast
                mViewModel.getAction().setValue(null);
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
        Log.i(TAG, "onMessageEvent: isLogin" + event.getLogin());
        mViewModel.loadUserInfo(event.getLogin());

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

    private void showLogoutDialog() {
        YesOrNoDialog.showDialog(getActivity(), "退出登录", "是否退出当前APP的登录", new YesOrNoDialog.Callback() {
            @Override
            public void onConfirm() {
                //点击确认，退出登录
                mViewModel.logout();

            }
        });
    }
}
