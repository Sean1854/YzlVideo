package com.ls.mediaplayer.ui.playerecord;

import com.ls.libbase.base.BaseApplication;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.manager.UserManager;
import com.ls.mediaplayer.db.VideoHistory;
import com.ls.mediaplayer.db.VideoHistoryRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PlayerRecordModel {


    /**
     * 查询浏览记录
     * @param callback 返回给外部viewModel
     */
    public void requestHistory(IRequestCallback<List<VideoHistory>> callback) {
        String userId = "0";
        if (isLogin()) {
            userId = UserManager.getInstance().getUserInfo().getUser().getId();
        }
        VideoHistoryRepository repository = new VideoHistoryRepository(BaseApplication.getContext());
        repository.query(userId, callback);
    }


    /**
     * 是否登录
     * @return
     */
    public Boolean isLogin() {
        return UserManager.getInstance().isLogin();
    }

    public void deleterById(HashMap<VideoHistory,Boolean> selectDelDatas, IRequestCallback<String> callback){
        String userId = "0";
        if (isLogin()) {
            userId = UserManager.getInstance().getUserInfo().getUser().getId();
        }
        VideoHistoryRepository repository = new VideoHistoryRepository(BaseApplication.getContext());
        List<Integer> videoIds = new ArrayList<>();
        for (VideoHistory history : selectDelDatas.keySet()) {
            videoIds.add(history.getVideoId());
        }
        repository.deleteByIds(userId,videoIds,callback);
    }

}
