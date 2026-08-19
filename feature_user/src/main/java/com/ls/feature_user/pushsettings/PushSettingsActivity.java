package com.ls.feature_user.pushsettings;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.feature_user.R;
import com.ls.feature_user.databinding.ActivityPushSettingsBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

@Route(path = ARouterPath.User.ACTIVITY_PUSHSETTINGS)
public class PushSettingsActivity extends BaseActivity<ActivityPushSettingsBinding, BaseViewModel> {

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_push_settings;
    }

    @Override
    protected int getBindingVariableId() {
        return 0;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
    }

    @Override
    protected void initData() {

    }
}