package com.ls.feature_find.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.feature_find.bean.ResFindAnchor;
import com.ls.feature_find.databinding.ItemAnchorBinding;

import java.util.List;

public class AnchorAdapter extends RecyclerView.Adapter<AnchorAdapter.ViewHolder> {

    private List<ResFindAnchor> mDatas;
    private OnItemClickListener onItemClick;

    public void setdatas(List<ResFindAnchor> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnchorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAnchorBinding categoryBinding =ItemAnchorBinding.inflate(inflater,parent,false);
        return new AnchorAdapter.ViewHolder(categoryBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull AnchorAdapter.ViewHolder holder, int position) {
        ResFindAnchor anchor = mDatas.get(position);
        holder.binding.setData(anchor);


    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0:mDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ItemAnchorBinding binding;

        public ViewHolder(@NonNull ItemAnchorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> {
            onItemClick.onThemeListClick();
            });

        }
    }
    public void setOnItemClick(OnItemClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClickListener {
        void onThemeListClick();
    }

}
