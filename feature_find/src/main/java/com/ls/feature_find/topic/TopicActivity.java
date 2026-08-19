package com.ls.feature_find.topic;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.feature_find.R;
import com.ls.feature_find.adapter.TopicAdapter;
import com.ls.feature_find.databinding.ActivityTopicBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

@Route(path = ARouterPath.Find.ACTIVITY_TOPIC)
public class TopicActivity extends BaseActivity<ActivityTopicBinding,TopicViewModel> {


    private TopicAdapter mAdapter;

    @Override
    protected TopicViewModel getViewModel() {
        return new ViewModelProvider(this).get(TopicViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_topic;
    }

    @Override
    protected void initData() {
        mViewModel.requestTopicData();
        mViewModel.getResTopic().observe(this,resTopics -> {
            mAdapter.setData(resTopics);
        });
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TopicAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int getBindingVariableId() {
        return 0;
    }
}