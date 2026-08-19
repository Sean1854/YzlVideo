package com.ls.mediaplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.libbase.manager.UserManager;
import com.ls.data_video.bean.ResComment;
import com.ls.mediaplayer.databinding.ItemConmentBinding;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHoldel> {

    private List<ResComment> comments;
    private onItemClickListenner mOnItemClickListenner;

    @NonNull
    @Override
    public CommentHoldel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemConmentBinding binding = ItemConmentBinding.inflate(inflater, parent,false);
        return new CommentHoldel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHoldel holder, int position) {
        ResComment resComment = comments.get(position);
        holder.binding.setContents(resComment);


    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
    }

    public void setdata(List<ResComment> comments){
        this.comments = comments;
        notifyDataSetChanged();
    }

    public class CommentHoldel extends RecyclerView.ViewHolder{

        private final com.ls.mediaplayer.databinding.ItemConmentBinding binding;

        public CommentHoldel(@NonNull ItemConmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            //给评论设置长按事件监听，弹出删除评论的dialog
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //用户Id
                    String id = UserManager.getInstance().getUserInfo().getUser().getId();

                    ResComment resComment = comments.get(getLayoutPosition());

                    //评论的作者Id
                    int commentId = resComment.getUser_id();

                    //两者Id相同才会弹出删除评论的弹窗
                    if (id.equals(String.valueOf(commentId))){
                        mOnItemClickListenner.onItemLongClick(resComment);
                        return false;
                    }
                    return false;
                }
            });
        }
    }


    public void setOnItemClickListenner(onItemClickListenner listenner) {
        mOnItemClickListenner = listenner;
    }

    public interface onItemClickListenner {
        void onItemLongClick(ResComment comment);

    }

}
