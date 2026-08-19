package com.ls.mediaplayer.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ls.libbase.base.IRequestCallback;
import com.ls.network.config.ErrorStatusConfig;

import java.util.Date;
import java.util.List;

/**
 * 操作视频浏览记录数据库的类
 */
public class VideoHistoryRepository {
    private static final String TAG = "VideoHistoryRepository";

    private final AppDatabase mAppDatabase;
    private Handler mainHandler = new Handler(Looper.getMainLooper());


    public VideoHistoryRepository(Context context) {
        //初始化room数据库， "Video_history_database"是数据库的名字
        mAppDatabase = Room.databaseBuilder(context, AppDatabase.class, "Video_history_database").build();
    }

    /**
     * 生成一条浏览记录
     *
     * @param userId
     * @param videoId
     * @param title
     * @param tag
     * @param duration
     * @return
     */
    public VideoHistory generateVideoHistory(String userId, int videoId, String title, String tag, String duration, String cover) {

        //获取当前的时间戳
        long viewTime = new Date().getTime();
        VideoHistory videoHistory = new VideoHistory();
        videoHistory.setUserId(userId);
        videoHistory.setVideoId(videoId);
        videoHistory.setTitle(title);
        videoHistory.setTag(tag);
        videoHistory.setDuration(duration);
        videoHistory.setViewTime(viewTime);
        videoHistory.setCover(cover);

        return videoHistory;
    }

    /**
     * 插入浏览记录
     *
     * @param videoHistory
     */
    public void insert(VideoHistory videoHistory) {
        new Thread(() -> {
            //判断当前的浏览记录是否已存在 未存在返回null
            VideoHistory existing = mAppDatabase.videoHistoryDao().getVideoHistoryByUserAndVideo(videoHistory.getUserId(), videoHistory.getVideoId());

            if (existing != null) {
                //当前的浏览记录已存在 更新浏览时间
                mAppDatabase.videoHistoryDao().updateViewTime(videoHistory.getUserId(), videoHistory.getVideoId(), videoHistory.getViewTime());
                Log.i(TAG, "insert: 视频标题 =" + videoHistory.getTag() + " 时间已更新");
                return;
            }
            //不存在，插入新数据
            mAppDatabase.videoHistoryDao().insertVideoHistory(videoHistory);
            Log.i(TAG, "insert: 视频标题 =" + videoHistory.getTag() + " 已插入");
        }).start();
    }


    /**
     * 通过用户Id查询浏览记录
     *
     * @param userId
     */
    public void query(String userId, IRequestCallback<List<VideoHistory>> callback) {
        new Thread(() -> {
            List<VideoHistory> videoHistorys = mAppDatabase.videoHistoryDao().getVideoHistorys(userId);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (videoHistorys == null) {
                        callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_EMPTY, "没有历史浏览记录！");
                    } else {
                        callback.onLoadFinish(videoHistorys);
                    }
                }
            });
        }).start();
    }


    /**
     *删除单条历史记录
     * @param userId
     * @param videoId
     * @param callback
     */
    public void delete(String userId, int videoId, IRequestCallback<String> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = mAppDatabase.videoHistoryDao().deleteVideoHistory(userId, videoId);
                mainHandler.post(() -> {
                    if (i > 0){
                        callback.onLoadFinish("已删除" + i + "条历史浏览记录");
                    }else {
                        callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_EMPTY,"删除失败！");
                    }
                });
            }
        }).start();
    }

    /**
     *删除多条历史记录
     * @param userId
     * @param videoId
     * @param callback
     */
    public void deleteByIds(String userId, List<Integer> videoId, IRequestCallback<String> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = mAppDatabase.videoHistoryDao().deleteVideoHistoryByIds(userId, videoId);
                mainHandler.post(() -> {
                    if (i > 0){
                        callback.onLoadFinish("已删除" + i + "条历史浏览记录");
                    }else {
                        callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_EMPTY,"删除失败！");
                    }
                });
            }
        }).start();
    }

}
