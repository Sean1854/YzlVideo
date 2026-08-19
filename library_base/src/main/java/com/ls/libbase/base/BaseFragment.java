package com.ls.libbase.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.libbase.R;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.databinding.LayoutStatusViewBinding;
import com.ls.network.config.ErrorStatusConfig;

public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {

    protected VM mViewModel;
    protected V mDataBinding;
    protected LayoutStatusViewBinding mLayoutStatusViewBinding;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false);
        mDataBinding.setLifecycleOwner(this);

        VM viewModel = getViewModel();
        if (viewModel != null) {
            mViewModel = viewModel;
        }


        //在父类中使用viewmodel在xml中的变量名 关联具体的mViewModel  效果等同于mDataBinding.setViewModel(mViewModel);
        int bindingVariableId = getBindingVariableId();
        if (bindingVariableId != 0) {
            mDataBinding.setVariable(bindingVariableId, mViewModel);
            //关联完mViewModel后，实时更新数据
            mDataBinding.executePendingBindings();
        }
        //初始化arouter
        ARouter.getInstance().inject(this);

        initView();
        initData();
        initToast();
        initStatusView();
        initProgressBar();

        return mDataBinding.getRoot();
    }


    /**
     * 初始化加载样式
     */
    private void initProgressBar() {
        if (isSharedViewModel()) {
            return; // 共享宿主 ViewModel 时，加载框交由 Activity 统一显示
        }
        mProgressBar = new ProgressBar(getContext());
        if (mDataBinding.getRoot() instanceof ConstraintLayout) {
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
    }


    private void initToast() {
        if (mViewModel != null && !isSharedViewModel()) {
            mViewModel.getToastText().observe(getViewLifecycleOwner(), text -> {
                if (text !=null && !text.isEmpty()){
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            });

            //控制加载样式是否显示
            mViewModel.getShowLoading().observe(getViewLifecycleOwner(), show -> {
                mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            });
        }
    }

    /**
     * 初始化状态错误页面，使用的时候只需要设置mViewModel里的errorcode值即可
     */
    private void initStatusView() {
        //获取当前页面的根布局
        ViewGroup root = (ViewGroup) mDataBinding.getRoot();
        //查找状态错误提示布局
        mLayoutStatusViewBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.layout_status_view, null, false);
        //将布局添加到当前页面并设置宽高尺寸
        View statusViewRoot = mLayoutStatusViewBinding.getRoot();
        statusViewRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        root.addView(statusViewRoot);

        if (mViewModel != null) {
            //错误状态显示
            //getViewLifecycleOwner()：通过这个方法获取当前视图生命周期的所有者，以免造成内存泄露
            mViewModel.getErrorCode().observe(getViewLifecycleOwner(), errorCode -> {
                String content = "";
                switch (errorCode) {
                    case ErrorStatusConfig.ERROR_STATUS_NETWORK_FIAL:
                        content = "网络错误，请检查网络！";
                        break;
                    case ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN:
                        content = "请登录后再进行操作！";
                        break;
                    case ErrorStatusConfig.ERROR_STATUS_EMPTY:
                        content = "当前没有更多数据了！";
                        break;
                    case ErrorStatusConfig.ERROR_STATUS_SERVER_ERROR:
                        content = "服务器异常！";
                        break;
                    case ErrorStatusConfig.ERROR_STATUS_NORMAL:
                    default:
                        content = "";
                        break;
                }

                LayoutStatusViewBinding layoutStatusView = mLayoutStatusViewBinding;
                boolean isVisibility = errorCode != ErrorStatusConfig.ERROR_STATUS_NORMAL;
                layoutStatusView.clStatus.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
                layoutStatusView.tvLable.setText(content);

            });
        }

    }


    protected abstract VM getViewModel();

    protected abstract int getLayoutResId();

    protected abstract int getBindingVariableId();

    protected abstract void initView();//如果子类需要做一些视图上的初始化

    protected abstract void initData();//如果子类需要做一些数据上的初始化

    /**
     * 是否复用（共享）宿主 Activity 的 ViewModel。
     * 默认 false。返回 true 时，本 Fragment 不再自行创建加载框/Toast，
     * 交由宿主 Activity 统一管理，避免与 Activity 出现重复 loading / 重复 toast。
     */
    protected boolean isSharedViewModel() {
        return false;
    }
}
