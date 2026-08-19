package com.ls.mediaplayer.ui.report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.ls.mediaplayer.R;
import com.ls.mediaplayer.databinding.LayoutCommentReportBinding;
import com.ls.mediaplayer.ui.videodetail.VideoDetailViewModel;

public class CommentReportPopupWindow extends PopupWindow {


    private LayoutCommentReportBinding binding;
    private OnPopupInteractionListener listener;

    public CommentReportPopupWindow(AppCompatActivity activity) {
        super(activity);
        init(activity);
    }

    private void init(AppCompatActivity activity){
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.layout_comment_report, null, false);
        VideoDetailViewModel viewModel = new ViewModelProvider(activity).get(VideoDetailViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(activity);

        //设置popupwindow内容视图
        setContentView(binding.getRoot());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //这行代码的作用是让 PopupWindow 可以获取焦点。当 PopupWindow 可获取焦点时，它能够接收用户的输入事件，
        setFocusable(true);
        //允许用户在 PopupWindow 外部触摸屏幕，触摸外部后直接关闭弹窗
        setOutsideTouchable(true);
        //当软键盘弹出时，PopupWindow 的大小会进行调整，以确保 PopupWindow 内的内容能完整显示，避免被软键盘遮挡。
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //避免默认背景导致PopupWindow大小受限，影响占满屏幕效果
        setBackgroundDrawable(null);

        binding.etChat.setOnEditorActionListener((v, actionId, event) -> {
            //如果用户按下输入法上的发送
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String text = binding.etChat.getText().toString().trim();
                if (text != null && text.length() > 0) {

                    if (listener != null){
                        listener.onSendMessage(binding.etChat.getText().toString());
                    }

                    binding.etChat.getText().clear(); // 清空输入框
                }
                return true;
            } else {
                return false;
            }

        });
    }


    /**
     * 显示PopupWindow
     */
    public void showPopup(View anchor) {
        showAtLocation(anchor, android.view.Gravity.BOTTOM, 0, 0);
    }

    /**
     * 设置监听器
     */
    public void setOnPopupInteractionListener(OnPopupInteractionListener listener) {
        this.listener = listener;
    }

    /**
     * 交互回调接口
     */
    public interface OnPopupInteractionListener {
//        void onLikeClicked();
//
//        void onCollectionClicked();

        void onSendMessage(String message);
    }


}
