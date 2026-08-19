package com.ls.mediaplayer.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.libbase.utils.TimeUtils;
import com.ls.mediaplayer.databinding.ItemVideoHistoryBinding;
import com.ls.mediaplayer.db.VideoHistory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VideoHistoryAdapter extends RecyclerView.Adapter<VideoHistoryAdapter.ViewHolder> {


    private List<VideoHistory> mDatas;
    private onVideoHistoryClick mListenner;
    private Boolean mIsSelect;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemVideoHistoryBinding binding = ItemVideoHistoryBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoHistory history = mDatas.get(position);
        holder.binding.setHistroy(history);
        //tag需要加入一个#号
        long viewTime = history.getViewTime();
        Instant instant = Instant.ofEpochMilli(viewTime);
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = localDate.format(formatter);
        holder.binding.setTag("上次浏览时间："+dateStr);
        holder.binding.setIsSelectStatus(mIsSelect);
        //复用时先摘掉监听再重置勾选，避免上次编辑模式残留的勾选状态错位
        holder.binding.cbSelect.setOnCheckedChangeListener(null);
        holder.binding.cbSelect.setChecked(false);
        holder.binding.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mListenner != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mListenner.onIsSelectClick(mDatas.get(pos), isChecked);
                }
            }
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void serDatas(List<VideoHistory> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void upIsSelect(Boolean isSelect) {
        mIsSelect = isSelect;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final com.ls.mediaplayer.databinding.ItemVideoHistoryBinding binding;

        public ViewHolder(@NonNull ItemVideoHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION || mListenner == null) {
                    return;
                }
                if (Boolean.TRUE.equals(mIsSelect)) {
                    //编辑模式下点击条目 = 切换勾选状态，禁止跳转视频详情
                    binding.cbSelect.toggle();
                } else {
                    VideoHistory videoHistory = mDatas.get(position);
                    mListenner.onItemVideoHistoryClick(videoHistory.getVideoId());
                }
            });
        }
    }


    public interface onVideoHistoryClick{
        void onItemVideoHistoryClick(int id);

        void onIsSelectClick(VideoHistory videoHistory,boolean isSelect);
    }

    public void setVideoHistoryClick(onVideoHistoryClick listenner){
        mListenner = listenner;
    }
}
