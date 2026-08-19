package com.ls.mediaplayer.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VideoHistoryDao {
    @Insert
    void insertVideoHistory(VideoHistory videoHistory);//插入数据

    /**
     *通过用户id和视频id查询是否有浏览记录。
     * @param userId 用户id
     * @param videoId 视频id
     * @return
     */
    @Query("SELECT * FROM video_history WHERE userId= :userId AND videoId = :videoId LIMIT 1")
    VideoHistory getVideoHistoryByUserAndVideo(String userId,int videoId);

    /**
     * 根据视频id和用户id更新浏览时间
     *
     * @param userId
     * @param videoId
     * @param newViewTime
     */
    @Query("UPDATE video_history SET viewTime = :newViewTime WHERE userId = :userId AND videoId = :videoId")
    void updateViewTime(String userId, int videoId, long newViewTime);

    /**
     * 查询当前用户的浏览记录
     *
     * @param userId
     * @return DESC（降序排序） 最新浏览的排在首位
     */
    @Query("SELECT * FROM video_history WHERE userId = :userId ORDER BY viewTime DESC")
    List<VideoHistory> getVideoHistorys(String userId);

    /**
     * 通过userid、videoid删除记录
     *
     * @param userId
     * @param videoId
     * @return
     */
    @Query("SELECT * FROM video_history WHERE userId = :userId AND videoId = :videoId")
    int deleteVideoHistory(String userId, int videoId);

    /**
     * 通过userid、videoid列表删除多条记录
     *
     * @param userId
     * @param videoIds 视频id列表
     * @return
     */
    @Query("DELETE FROM video_history WHERE userId = :userId AND videoId IN (:videoIds)")
    int deleteVideoHistoryByIds(String userId, List<Integer> videoIds);


}
