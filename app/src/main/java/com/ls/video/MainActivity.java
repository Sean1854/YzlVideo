package com.ls.video;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.video.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding,MainViewModel> {

    private long lastBackPressedTime = 0;
    private static final int DOUBLE_PRESS_INTERVAL = 2000;
    private Toast exitToast;


    @Override
    protected MainViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        Fragment homeFragment = (Fragment) ARouter.getInstance().build(ARouterPath.Home.homeFragment).navigation();
        Fragment findFragment = (Fragment) ARouter.getInstance().build(ARouterPath.Find.findFragment).navigation();
        Fragment plazaFragment = (Fragment) ARouter.getInstance().build(ARouterPath.Plaza.plazaFragment).navigation();
        Fragment userFragment = (Fragment) ARouter.getInstance().build(ARouterPath.User.userFragment).navigation();
        replaceFragment(homeFragment);
        // 创建并注册返回键回调
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastBackPressedTime < DOUBLE_PRESS_INTERVAL) {
                    // 双击退出
                    if (exitToast != null) {
                        exitToast.cancel();
                    }

                    // 优雅退出应用
                    finishAffinity();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask();
                    }
                    System.exit(0);
                } else {
                    // 第一次点击
                    lastBackPressedTime = currentTime;
                    showExitToast();

                    // 添加延迟重置逻辑（可选）
                    getWindow().getDecorView().postDelayed(() -> {
                        // 重置时间，防止用户等待过久后再次点击无效
                        lastBackPressedTime = 0;
                    }, DOUBLE_PRESS_INTERVAL + 500); // 比双击间隔多500ms
                }
            }
        });

        mDataBinding.rgButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_home){
                    replaceFragment(homeFragment);
                } else if (i == R.id.rb_find) {
                    replaceFragment(findFragment);
                }else if (i == R.id.rb_plaza) {
                    replaceFragment(plazaFragment);
                }else if (i == R.id.rb_mine) {
                    replaceFragment(userFragment);
                }
            }
        });




    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fcv, fragment).commit();
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    private void showExitToast() {
        if (exitToast != null) {
            exitToast.cancel();
        }
        exitToast = Toast.makeText(
                this,
                R.string.press_again_to_exit,
                Toast.LENGTH_SHORT
        );
        exitToast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理资源
        if (exitToast != null) {
            exitToast.cancel();
            exitToast = null;
        }
    }
}