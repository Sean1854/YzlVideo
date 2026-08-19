package com.ls.feature_plaza.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.feature_plaza.R;
import com.ls.feature_plaza.databinding.ItemImageDetailBinding;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final List<String> mImgUrls;

    public ImageAdapter(List<String> urls) {
        this.mImgUrls = urls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_image_detail,
                null, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String url = mImgUrls.get(position);
        holder.binding.setImgUrl(url);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mImgUrls == null ? 0 : mImgUrls.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        private final ItemImageDetailBinding binding;

        public ImageViewHolder(@NonNull ItemImageDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}