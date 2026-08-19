package com.ls.feature_user.ui.account;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.databinding.ActivityAccountBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

@Route(path = ARouterPath.User.ACTIVITY_ACCOUNT)
public class AccountActivity extends BaseActivity<ActivityAccountBinding,AccountViewModel> {


    @Override
    protected AccountViewModel getViewModel() {
        return new ViewModelProvider(this).get(AccountViewModel.class) ;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_account;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
    }

    @Override
    protected void initData() {

    }
}