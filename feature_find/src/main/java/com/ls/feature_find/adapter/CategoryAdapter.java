package com.ls.feature_find.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.data_video.bean.ResFindCategory;
import com.ls.feature_find.databinding.ItemCategoryBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<ResFindCategory> mDatas;
    private CategoryListenner mListenner;

    public void setdatas(List<ResFindCategory> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCategoryBinding categoryBinding =ItemCategoryBinding.inflate(inflater,parent,false);
        return new ViewHolder(categoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResFindCategory category = mDatas.get(position);
        holder.binding.setData(category);

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0:mDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ItemCategoryBinding binding;

        public ViewHolder(@NonNull ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> {
                int position = getLayoutPosition();
                ResFindCategory category = mDatas.get(position);
                mListenner.onCategroyItemClick(category);
            });

        }
    }

    public interface CategoryListenner{
        void onCategroyItemClick(ResFindCategory category);
    }

    public void  setListenner(CategoryListenner categoryListenner){
        mListenner = categoryListenner;
    }
}
