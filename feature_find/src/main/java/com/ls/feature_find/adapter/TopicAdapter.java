package com.ls.feature_find.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.feature_find.bean.ResThemeData;
import com.ls.feature_find.bean.ResTopic;
import com.ls.feature_find.databinding.ItemAnchorInfoBinding;
import com.ls.feature_find.databinding.ItemTopicInfoBinding;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {


    private List<ResTopic> mData;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTopicInfoBinding binding = ItemTopicInfoBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResTopic topic = mData.get(position);
        holder.binding.setTopicinfo(topic);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0: mData.size();
    }


    public void setData( List<ResTopic> topics){
        mData = topics;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private final ItemTopicInfoBinding binding;

        public ViewHolder(@NonNull ItemTopicInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
