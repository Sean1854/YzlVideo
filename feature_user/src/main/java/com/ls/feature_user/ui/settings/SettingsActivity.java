package com.ls.feature_user.ui.settings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.config.UserConfig;
import com.ls.feature_user.databinding.ActivitySettingsBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.ui.dialog.YesOrNoDialog;
import com.ls.libbase.utils.StatusBarUtils;

@Route(path = ARouterPath.User.ACTIVITY_SETTINGS)
public class SettingsActivity extends BaseActivity<ActivitySettingsBinding, SettingsViewModel> {


    @Override
    protected SettingsViewModel getViewModel() {
        return new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        mViewModel.getAction().observe(this, new Observer<SettingsViewModel.SettingsAction>() {
            @Override
            public void onChanged(SettingsViewModel.SettingsAction settingsAction) {
                if (settingsAction == null) {
                    return;
                }
                switch (settingsAction){
                    case NAVIGATE_TO_LOGIN://跳转登录页面
                        mViewModel.showToast("请先登录后才能操作！");
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_LOGIN).navigation();
                        break;
                    case NAVIGATION_TO_ACCOUNT://设置账户
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_ACCOUNT).navigation();
                        break;
                    case NAVIGATION_TO_PASSWORD://设置密码
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_RESETPWD).navigation();
                        break;
                    case SHOW_CLEAR_CACHE_DIALOG://显示清理缓存弹窗
                        showClearCacheDialog();
                        break;
                    case NAVIGATE_TO_PUSH_SETTING://推送设置
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_PUSHSETTINGS).navigation();
                        break;
                    case NAVIGATE_TO_PLAY_SETTING://播放设置
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_PLAYTTINGS).navigation();
                        break;
                    case NAVIGATE_TO_USER_AGREEMENT://用户协议
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT).withInt(UserConfig.AgreementType.KEY_AGREEMENT,UserConfig.AgreementType.VALUE_AGREEMENT).navigation();
                        break;
                    case NAVIGATE_TO_SIMPLE_PRIVACY_POLICY://隐私政策概要
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT).withInt(UserConfig.AgreementType.KEY_AGREEMENT,UserConfig.AgreementType.VALUE_SIMPLE_PRIVATE).navigation();
                        break;
                    case NAVIGATE_TO_PRIVACY_POLICY://隐私政策
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT).withInt(UserConfig.AgreementType.KEY_AGREEMENT,UserConfig.AgreementType.VALUE_PRIVATE).navigation();
                        break;
                    case NAVIGATE_TO_USER_INFO_MENU://用户清单
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT).withInt(UserConfig.AgreementType.KEY_AGREEMENT,UserConfig.AgreementType.VALUE_USER_INFO).navigation();
                        break;
                    case NAVIGATE_TO_ABOUT_US://关于我们
                        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_ABOUTME).navigation();
                        break;
                    case SHOW_LOGOUT_DIALOG://点击退出登录，显示确认弹窗
                        showLogoutDialog();
                        break;
                }
                //消费后复位，防止 Activity 重建时 LiveData 粘性重放导致重复跳转/重复弹 Toast
                mViewModel.getAction().setValue(null);
            }
        });


    }

    private void showClearCacheDialog() {
        YesOrNoDialog.showDialog(this, "清除缓存", "是否清除当前APP相关缓存", new YesOrNoDialog.Callback() {
            @Override
            public void onConfirm() {
              mViewModel.clearCache();
            }
        });
    }

    private void showLogoutDialog() {
        YesOrNoDialog.showDialog(this, "退出登录", "是否退出当前APP的登录", new YesOrNoDialog.Callback() {
            @Override
            public void onConfirm() {
                //点击确认，退出登录
                mViewModel.logout();

            }
        });
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }
}
