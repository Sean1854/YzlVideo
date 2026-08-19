package com.ls.mediaplayer.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.data_video.bean.ResVideoDetail;
import com.ls.libbase.utils.TimeUtils;
import com.ls.mediaplayer.databinding.ItemSearchBinding;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<ResVideoDetail.ArchivesInfoBean> mDatas;
    private onSearchClick mListenner;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSearchBinding binding = ItemSearchBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResVideoDetail.ArchivesInfoBean search = mDatas.get(position);
        holder.binding.setSearch(search);
        String tag = search.getTag();
        holder.binding.setTag("#" + tag);
        int createtime = search.getCreatetime();
        holder.binding.setTime(TimeUtils.convertTimestampToDate(createtime));

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    public void setData(List<ResVideoDetail.ArchivesInfoBean> search) {
        mDatas = search;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final com.ls.mediaplayer.databinding.ItemSearchBinding binding;

        public ViewHolder(@NonNull ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> {
                int position = getLayoutPosition();
                ResVideoDetail.ArchivesInfoBean bean = mDatas.get(position);
                mListenner.onItemSearchClick(bean.getId());
            });
        }
    }

    public interface onSearchClick{
        void onItemSearchClick(int id);
    }

    public void setItemSearchClick(onSearchClick listenner){
        mListenner = listenner;
    }
}
