package com.ls.mediaplayer.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.data_video.bean.ResVideoDetail;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;
import com.ls.mediaplayer.BR;
import com.ls.mediaplayer.R;
import com.ls.mediaplayer.adapter.SearchAdapter;
import com.ls.mediaplayer.databinding.ActivitySearchBinding;

import java.util.List;

@Route(path = ARouterPath.Video.ACTIVITY_SEARCH)
public class SearchActivity extends BaseActivity<ActivitySearchBinding,SearchViewModel> {

    private SearchAdapter mAdapter;

    @Override
    protected SearchViewModel getViewModel() {
        return new ViewModelProvider(this).get(SearchViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData() {

        mViewModel.getSearchData().observe(this, new Observer<List<ResVideoDetail.ArchivesInfoBean>>() {
            @Override
            public void onChanged(List<ResVideoDetail.ArchivesInfoBean> archivesInfoBeans) {
                mAdapter.setData(archivesInfoBeans);
            }
        });

        mDataBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND){
                    //发送搜索关键字查询视频
                    mViewModel.requestSearch();
                    // 新增：点击键盘搜索键后自动收起键盘
                    hideKeyboard(mDataBinding.etSearch);
                    return true;
                }
                return false;
            }
        });

        mViewModel.getSearchKeyword().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //更新清除按钮状态
                mViewModel.upEditData();
            }
        });

    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);

        mAdapter.setItemSearchClick(new SearchAdapter.onSearchClick() {
            @Override
            public void onItemSearchClick(int id) {
                //把适配器传递的点击视频id携带数据跳转到VideoDeatailActivity
                ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_VIDEODETAIL).
                        withInt(ARouterPath.Video.KEY_VIDEO_ID,id).
                        navigation();
            }
        });

    }

    // ========== 新增：页面可见时自动弹出键盘 ==========
    @Override
    protected void onResume() {
        super.onResume();
        // 延迟执行，确保布局渲染完成后再唤起键盘，避免失效
        mDataBinding.etSearch.post(new Runnable() {
            @Override
            public void run() {
                showKeyboard(mDataBinding.etSearch);
            }
        });
    }

    // ========== 新增：唤起软键盘方法 ==========
    private void showKeyboard(View view) {
        if (view == null) return;
        // 先请求焦点
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    // ========== 新增：隐藏软键盘方法 ==========
    private void hideKeyboard(View view) {
        if (view == null) return;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }
}