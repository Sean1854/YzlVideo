// 包路径：基础库弹窗组件
package com.ls.libbase.ui.dialog;

// 系统弹窗基础类
import android.app.Dialog;
// 弹窗显示/关闭监听接口
import android.content.DialogInterface;
// Fragment存储临时数据容器
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
// 弹窗对齐方式常量（居中、底部、顶部等）
import android.view.Gravity;
// 布局加载器，用来Inflate xml布局
import android.view.LayoutInflater;
// 视图点击事件父类
import android.view.View;
// 窗口属性管理器，控制弹窗宽高、位置、透明度
import android.view.WindowManager;

// 非空注解，标记返回值一定不为null
import androidx.annotation.NonNull;
// 可为空注解，标记参数/返回值可能为null
import androidx.annotation.Nullable;
// 兼容版AlertDialog弹窗
import androidx.appcompat.app.AlertDialog;
// DialogFragment：把弹窗封装成Fragment，拥有Fragment完整生命周期
import androidx.fragment.app.DialogFragment;
// 页面Activity基类，用于获取FragmentManager展示弹窗
import androidx.fragment.app.FragmentActivity;

// DataBinding自动生成的弹窗布局绑定类
import com.ls.libbase.databinding.LayoutDialogYesOrNoBinding;

/**
 * 通用确认/取消双按钮弹窗
 * 用途：清除缓存、退出登录、二次确认等场景
 */
public class YesOrNoDialog extends DialogFragment {

    // Bundle存储key：弹窗标题
    private static final String KEY_TITLE = "KEY_TITLE";
    // Bundle存储key：弹窗正文内容
    private static final String KEY_CONTEXT = "KEY_CONTEXT";
    // 外部传入的确认按钮回调接口
    private final Callback mCallback;

    /**
     * 构造方法：传入确认回调
     *
     * @param callback 点击确认后的回调
     */
    public YesOrNoDialog(Callback callback) {
        mCallback = callback;
    }

    /**
     * 对外静态快捷展示方法（Activity直接调用）
     * @param activity 当前页面Activity
     * @param title 弹窗标题文字
     * @param context 弹窗提示正文
     * @param callback 确认按钮回调
     */
    public static void showDialog(FragmentActivity activity,String title, String context, Callback callback){
        // 调用newInstance创建弹窗实例
        YesOrNoDialog yesOrNoDialog = YesOrNoDialog.newInstance(title, context, callback);
        // 展示弹窗，tag用于唯一标识弹窗
        yesOrNoDialog.show(activity.getSupportFragmentManager(),"yesorno");
    }

    /**
     * 创建弹窗实例，把标题、内容存入Bundle参数
     * @param title 标题
     * @param context 正文
     * @param callback 确认回调
     * @return 弹窗对象
     */
    public static YesOrNoDialog newInstance(String title, String context, Callback callback) {
        // 创建Bundle容器，存放页面文字数据（Fragment重建时数据不会丢失）
        Bundle args = new Bundle();
        // 存入标题字符串
        args.putString(KEY_TITLE, title);
        // 存入正文字符串
        args.putString(KEY_CONTEXT, context);
        // 创建弹窗对象，传入回调
        YesOrNoDialog fragment = new YesOrNoDialog(callback);
        // 将Bundle绑定给Fragment，后续onCreateDialog中读取
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Fragment生命周期方法：创建弹窗Dialog对象
     * @param savedInstanceState 页面旋转/销毁重建缓存（此处未使用）
     * @return 最终展示的AlertDialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 取出newInstance存入的Bundle参数
        Bundle bundle = getArguments();
        // 根据key读取标题文本
        String title = bundle.getString(KEY_TITLE);
        // 根据key读取正文文本
        String context = bundle.getString(KEY_CONTEXT);

        // 构建AlertDialog构造器
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // 获取布局加载器
        LayoutInflater inflater = LayoutInflater.from(getContext());
        // inflate自定义弹窗xml布局，生成Binding对象
        LayoutDialogYesOrNoBinding binding = LayoutDialogYesOrNoBinding.inflate(inflater);

        // 给布局内标题TextView赋值
        binding.tvTitle.setText(title);
        // 给布局内内容TextView赋值
        binding.tvContent.setText(context);

        // 取消按钮点击监听
        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 关闭弹窗
                dismiss();
            }
        });

        // 确认按钮点击监听
        binding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 执行外部传入的确认回调逻辑
                mCallback.onConfirm();
                // 关闭弹窗
                dismiss();
            }
        });

        // 将自定义布局绑定到弹窗构造器
        builder.setView(binding.getRoot());
        // 创建最终弹窗实例
        AlertDialog alertDialog = builder.create();

        // 弹窗渲染完成后的监听，用来修改窗口宽高、位置
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                // 获取弹窗窗口属性对象
                WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                // 设置弹窗宽度为屏幕宽度90%
                layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
                // 设置弹窗居中显示
                layoutParams.gravity = Gravity.CENTER;

                // 透明背景，保留圆角，无黑色底边
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // 将修改后的属性应用到弹窗窗口
                alertDialog.getWindow().setAttributes(layoutParams);
            }
        });
        // 返回自定义弹窗给系统渲染展示
        return alertDialog;
    }

    /**
     * 回调接口：外部页面实现，接收确认按钮点击事件
     */
    public interface Callback {
        // 用户点击确认时执行
        void onConfirm();
    }
}