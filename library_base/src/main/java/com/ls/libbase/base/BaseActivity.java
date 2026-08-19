package com.ls.libbase.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

public abstract class BaseActivity<V extends ViewDataBinding,VM extends BaseViewModel> extends AppCompatActivity {

    protected VM mViewModel;
    protected V mDataBinding;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        initDataBinding();

//        EdgeToEdge.enable(this);
//        ViewCompat.setOnApplyWindowInsetsListener(mDataBinding.getRoot(),(v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left,systemBars.top,systemBars.right,systemBars.bottom);
//            return insets;
//        });

        ARouter.getInstance().inject(this);
        StatusBarUtils.setImmerseStatusBar(this);


        initView();
        initData();
        initProgressBar();
        if (mViewModel != null){

            mViewModel.getIslogin().observe(this,islogin -> {
                //只负责跳转登录页，Toast 由 BaseViewModel.startLogin() 统一弹出
                if (islogin != null && !islogin){
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_LOGIN).navigation();
                    //立即复位，防止 Activity 重建/观察者重注册时 LiveData 粘性重放导致重复跳转
                    mViewModel.setIslogin(true);
                }
            });
        }

    }

    private void initDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutResId());
        mDataBinding.setLifecycleOwner(this);
        mDataBinding.setVariable(getBindingVariableId(),mViewModel);
        mDataBinding.executePendingBindings();
    }

    private void initViewModel() {
        //从子类获取到的viewModel 赋值给mViewModel
        mViewModel = getViewModel();

        if (mViewModel != null) {
            //控制是否显示弹窗信息
            mViewModel.getToastText().observe(this, text -> {
                if (text !=null && !text.isEmpty()){
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
            });
            //控制加载样式是否显示
            mViewModel.getShowLoading().observe(this, show -> {
                mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            });

            mViewModel.getFinish().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isFish) {
                    finish();
                }
            });
        }


    }

    /**
     * 初始化加载样式
     */
    private void initProgressBar() {
        mProgressBar = new ProgressBar(this);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setVisibility(View.GONE);//默认不可见
        ConstraintLayout constraintLayout = (ConstraintLayout) mDataBinding.getRoot();
        constraintLayout.addView(mProgressBar);
    }

    protected abstract VM getViewModel();

    protected abstract int getLayoutResId();
    protected abstract void initData();
    protected abstract void initView();
    protected abstract int getBindingVariableId();
}
