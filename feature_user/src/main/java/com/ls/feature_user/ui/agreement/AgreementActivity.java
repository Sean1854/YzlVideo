package com.ls.feature_user.ui.agreement;


import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.config.UserConfig;
import com.ls.feature_user.databinding.ActivityAgreementBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;


/**
 * 用户隐私协议页面
 */
@Route(path = ARouterPath.User.ACTIVITY_AGREEMENT)
public class AgreementActivity extends BaseActivity<ActivityAgreementBinding, BaseViewModel> {
    private  final String URL = "https://titok.fzqq.fun/";

    private final String PRIVATE_URL = URL + "agreement.html";//隐私政策、隐私概要
    private final String AGREEMENT_URL = URL + "UserAgreement.html";//用户协议
    private final String USER_INFO_URL = URL + "userinfomenus.html";//隐私政策、隐私概要


    @Autowired(name = UserConfig.AgreementType.KEY_AGREEMENT)
    public int mType;//定义跳转页面时的值显示不同用户协议

    @Override
    protected BaseViewModel getViewModel() {
        return new ViewModelProvider(this).get(BaseViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initData() {
        mViewModel.showLoading(true);
        mDataBinding.wvAgreement.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //网页加载结束触发
                mViewModel.showLoading(false);
            }
        });
        String loadUrl = "";
        switch (mType){
            case UserConfig.AgreementType.VALUE_AGREEMENT://用户协议
                mDataBinding.tvTitle.setText("用户协议");
                loadUrl = AGREEMENT_URL;
                break;
            case UserConfig.AgreementType.VALUE_SIMPLE_PRIVATE://隐私政策
                mDataBinding.tvTitle.setText("隐私政策");
                loadUrl = PRIVATE_URL;
                break;
            case UserConfig.AgreementType.VALUE_PRIVATE://隐私政策概要
                mDataBinding.tvTitle.setText("隐私政策概要");
                loadUrl = PRIVATE_URL;
                break;
            case UserConfig.AgreementType.VALUE_USER_INFO://用户信息收集清单
                mDataBinding.tvTitle.setText("用户信息收集清单");
                loadUrl = USER_INFO_URL;
                break;
        }
        mDataBinding.wvAgreement.loadUrl(loadUrl);
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }
}