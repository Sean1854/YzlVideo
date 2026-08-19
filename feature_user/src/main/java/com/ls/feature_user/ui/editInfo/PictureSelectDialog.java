package com.ls.feature_user.ui.editInfo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ls.feature_user.R;
import com.ls.feature_user.databinding.DialogLayoutPictureSelectBinding;


public class PictureSelectDialog extends DialogFragment {

    private DialogLayoutPictureSelectBinding binding;
    private OnPictureSelectListener mListener;

    public static PictureSelectDialog newInstance() {
        return new PictureSelectDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 构造 Dialog，沿用你原来的底部滑入样式与点击逻辑
        Dialog dialog = new Dialog(requireActivity());
        // 移除系统默认标题栏
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // DataBinding 加载布局
        binding = DialogLayoutPictureSelectBinding.inflate(LayoutInflater.from(getContext()));
        dialog.setContentView(binding.getRoot());

        // ========== 核心：底部弹窗+宽度占满配置（保留你的底部滑入） ==========
        Window window = dialog.getWindow();
        if (window != null) {
            // 背景透明，保证圆角生效、无默认白边
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams params = window.getAttributes();
            // 固定在屏幕底部
            params.gravity = Gravity.BOTTOM;
            // 宽度占满屏幕
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            // 高度自适应内容
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            // 设置底部滑入滑出动画
            window.setWindowAnimations(R.style.BottomDialogAnimation);
        }

        // 初始化点击事件
        initClickListener();

        return dialog;
    }

    private void initClickListener() {
        // 拍摄照片
        binding.tvTakePhoto.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onTakePhoto();
            }
            dismiss();
        });

        // 从相册获取
        binding.tvPickAlbum.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onPickFromAlbum();
            }
            dismiss();
        });
    }

    /**
     * 对外设置回调监听
     */
    public void setOnPictureSelectListener(OnPictureSelectListener listener) {
        this.mListener = listener;
    }

    /**
     * 回调接口
     */
    public interface OnPictureSelectListener {
        void onTakePhoto();
        void onPickFromAlbum();
    }
}
