package com.ls.mediaplayer.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.data_video.bean.ResCategoryVideoDetail;
import com.ls.mediaplayer.databinding.ItemCategoryVideoBinding;

import java.util.List;

public class CategoryVideosAdapter extends RecyclerView.Adapter<CategoryVideosAdapter.ViewHolder> {

    private final onCategoryClick mLinnean;

    public CategoryVideosAdapter(onCategoryClick click) {
        mLinnean = click;
    }

    private List<ResCategoryVideoDetail> mData;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCategoryVideoBinding binding = ItemCategoryVideoBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResCategoryVideoDetail videoDetail = mData.get(position);
        holder.binding.setData(videoDetail);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(List<ResCategoryVideoDetail> data){
        mData = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final com.ls.mediaplayer.databinding.ItemCategoryVideoBinding binding;

        public ViewHolder(@NonNull ItemCategoryVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> {
                int position = getLayoutPosition();
                int id = mData.get(position).getId();
                mLinnean.onItemCategoryClick(id);
            });
        }
    }

    public interface onCategoryClick{
        void onItemCategoryClick(int id);
    }

}
