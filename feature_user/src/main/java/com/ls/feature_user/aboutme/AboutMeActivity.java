package com.ls.feature_user.aboutme;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.config.ARouterPath;

@Route(path = ARouterPath.User.ACTIVITY_ABOUTME)
public class AboutMeActivity extends BaseActivity {


    @Override
    protected BaseViewModel getViewModel() {
        return new ViewModelProvider(this).get(AboutMeViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about_me;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getBindingVariableId() {
        return BR.ViewModel;
    }
}