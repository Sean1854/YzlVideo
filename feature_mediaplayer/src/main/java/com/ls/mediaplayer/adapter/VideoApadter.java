package com.ls.mediaplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.data_video.bean.ResVideo;
import com.ls.mediaplayer.databinding.ItemVideoBinding;

import java.util.List;

public class VideoApadter extends RecyclerView.Adapter<VideoApadter.ViewHolder> {

    private final ItemClickListenner clickListenner;
    private  List<ResVideo> mvideos;
    private Boolean mStyle;//是否文字颜色设置为白色

    public VideoApadter(ItemClickListenner clickListenner) {
        this.clickListenner = clickListenner;
    }

    public void setvideo(List<ResVideo> videos) {
        this.mvideos = videos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemVideoBinding binding = ItemVideoBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResVideo resvideo = mvideos.get(position);
        holder.binding.setVideo(resvideo);
        holder.binding.executePendingBindings();
        holder.binding.setColor(mStyle);

//        Glideutils.loadImage(resvideo.getImage(), holder.binding.ivBackground);
//        Glideutils.loadCircleImage(resvideo.getAvatar(),holder.binding.ivAvatar);

    }

    @Override
    public int getItemCount() {
        return mvideos == null ? 0 : mvideos.size();
    }

    public void setWhite(Boolean mStyle) {
        this.mStyle = mStyle;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ItemVideoBinding binding;

        public ViewHolder(ItemVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获取点击的item
                    int position = getLayoutPosition();
                    ResVideo video = mvideos.get(position);
                    clickListenner.onVideoClickId(video.getId());
                }
            });
        }
    }


    /**
     * 把点击的item视频id，传给VideoListFragment
     */
    public interface ItemClickListenner{
        void onVideoClickId(int id);
    }
}
