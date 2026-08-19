package com.ls.video;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ls.libbase.utils.StatusBarUtils;

public class SplashActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mJumpRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 立即显示布局，文字随布局同步渲染，空窗期由 SplashTheme 白底衔接，无灰白突变
        setContentView(R.layout.activity_splash);
        //沉浸式状态栏：内容延伸到状态栏下方，与主页面保持一致
        StatusBarUtils.setImmerseStatusBar(this);

        mJumpRunnable = () -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        };
        // 最短等待300ms，保证闪屏视觉，不需要强制久等
        mHandler.postDelayed(mJumpRunnable,300);
    }

    // 拦截返回键，启动页不能返回
    @Override
    public void onBackPressed() {
        // 什么都不做，禁止返回
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁时移除任务，防止内存泄漏！非常关键
        if(mJumpRunnable != null){
            mHandler.removeCallbacks(mJumpRunnable);
        }
    }
}
