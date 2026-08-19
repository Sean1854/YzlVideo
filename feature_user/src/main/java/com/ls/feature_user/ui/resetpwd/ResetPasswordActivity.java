package com.ls.feature_user.ui.resetpwd;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.databinding.ActivityResetPasswordBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

@Route(path = ARouterPath.User.ACTIVITY_RESETPWD)
public class ResetPasswordActivity extends BaseActivity<ActivityResetPasswordBinding, ResetPasswordViewModel> {

    @Override
    protected ResetPasswordViewModel getViewModel() {
        return new ViewModelProvider(this).get(ResetPasswordViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());

        mViewModel.getCode().observe(this, s -> {
            mViewModel.updateEnableResetBtnStatus();
        });
        mViewModel.getPassword1().observe(this, s -> {
            mViewModel.updateEnableResetBtnStatus();
        });
        mViewModel.getPassword2().observe(this, s -> {
            mViewModel.updateEnableResetBtnStatus();
        });
    }

    @Override
    protected void initData() {

    }
}