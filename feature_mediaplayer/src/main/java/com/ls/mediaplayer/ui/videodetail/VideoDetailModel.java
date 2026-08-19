package com.ls.mediaplayer.ui.videodetail;

import android.util.Log;

import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.base.BaseApplication;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.base.list.IListenner;
import com.ls.libbase.manager.UserManager;
import com.ls.mediaplayer.api.MediaApiServiceProvider;
import com.ls.data_video.bean.ReqComment;
import com.ls.data_video.bean.ReqDeleteComment;
import com.ls.data_video.bean.ReqVideoOperation;
import com.ls.data_video.bean.ResComment;
import com.ls.data_video.bean.ResLike;
import com.ls.data_video.bean.ResSendComment;
import com.ls.data_video.bean.ResVideoDetail;
import com.ls.mediaplayer.db.VideoHistory;
import com.ls.mediaplayer.db.VideoHistoryRepository;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;
import com.ls.network.bean.ResList;
import com.ls.network.config.ErrorStatusConfig;

import retrofit2.Call;

public class VideoDetailModel {
    private static final String TAG = "VideoDetailModel";


    private int mPage = 1;

    /**
     * 请求服务器，获取视频详情
     */
    public void requestDetail(int id, IRequestCallback<ResVideoDetail> callback){
        //获取用户token
        String token = UserManager.getInstance().getToken();
        Call<ResBase<ResVideoDetail>> call = MediaApiServiceProvider.getApiService().getVideoDetail(token,id);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResVideoDetail>>() {
            @Override
            public void onSuccess(ResBase<ResVideoDetail> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode,meesage);
            }
        });
    }

    /**
     * 是否登录
     * @return
     */
    public Boolean isLogin(){
        return UserManager.getInstance().isLogin();
    }


    /**
     * 点赞
     * @param likeId
     * @param callback
     */
    public void requeslike(int likeId,IRequestCallback<ResLike> callback){
        if (!isLogin()){
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN,"用户未登录");
            return;
        }
        String token = UserManager.getInstance().getToken();
        Call<ResLike> call = MediaApiServiceProvider.getApiService().requesLikes(token, new ReqVideoOperation(likeId, "like"));
        ApiCall.enqueueCommon(call, new ApiCall.ApiCallback<ResLike>() {
            @Override
            public void onSuccess(ResLike result) {
                if (result != null && result.getCode() == 1001) {
                    callback.onLoadFinish(result);
                } else {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_SERVER_ERROR, result.getMsg());
                }
            }

            @Override
            public void onError(int errorCode, String meesage) {
            callback.onLoadFailure(errorCode,meesage);
            }
        });
    }

    /**
     * 取消点赞
     *
     * @param id 需要取消点赞的id
     */
    public void requestCancelLike(int id, IRequestCallback<ResLike> callback) {

        if (!isLogin()) {
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN, "用户未登录");
            return;
        }

        UserManager userManager = UserManager.getInstance();
        String token = userManager.getToken();

        Call<ResLike> call = MediaApiServiceProvider.getApiService().requestCancelLike(token, new ReqVideoOperation(id));
        ApiCall.enqueueCommon(call, new ApiCall.ApiCallback<ResLike>() {
            @Override
            public void onSuccess(ResLike result) {

                if (result != null && result.getCode() == 1) {
                    callback.onLoadFinish(result);
                } else {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_SERVER_ERROR, result.getMsg());
                }
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }

    /**
     * 收藏
     */
    public void requestCollection(int id,IRequestCallback<ResLike> callback){
        if (!isLogin()){
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN,"用户未登录");
            return;
        }
        String token = UserManager.getInstance().getToken();
        Call<ResLike> call = MediaApiServiceProvider.getApiService().requestCollection(token, new ReqVideoOperation("archives",id));
        ApiCall.enqueueCommon(call, new ApiCall.ApiCallback<ResLike>() {
            @Override
            public void onSuccess(ResLike result) {
                if (result != null && result.getCode() == 1) {
                    callback.onLoadFinish(result);
                } else {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_SERVER_ERROR, result.getMsg());
                }
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }


    /**
     * 取消收藏
     *
     * @param id 需要取消收藏的id
     */
    public void requestCancelCollection(int id, IRequestCallback<ResLike> callback) {

        if (!isLogin()) {
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN, "用户未登录");
            return;
        }

        UserManager userManager = UserManager.getInstance();
        String token = userManager.getToken();

        Call<ResLike> call = MediaApiServiceProvider.getApiService().cancelCollection(token, new ReqVideoOperation(id));
        ApiCall.enqueueCommon(call, new ApiCall.ApiCallback<ResLike>() {
            @Override
            public void onSuccess(ResLike result) {

                if (result != null && result.getCode() == 1) {
                    callback.onLoadFinish(result);
                } else {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_SERVER_ERROR, result.getMsg());
                }
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }

    /**
     * 发送评论
     * @param aid 要评论的视频id
     * @param message 评论内容
     * @param callback 数据回传给viewmodel
     */
    public void sendComment(int aid,String message,IRequestCallback<ResSendComment> callback) {
        String token = UserManager.getInstance().getToken();
        Call<ResBase<ResSendComment>> call = MediaApiServiceProvider.getApiService().sendComment(token, new ReqComment(aid, message));
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResSendComment>>() {
            @Override
            public void onSuccess(ResBase<ResSendComment> result) {
                Log.i(TAG, "onSuccess:发送评论 " + result.getData().getComment().getContent());
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode,meesage);
            }
        });
    }

    /**
     * 请求评论列表
     */
    public void requestComment(int id, Boolean isFirst,IListenner<ResComment> listenner){
        if (isFirst){
            mPage = 1;
        }else {
            mPage ++ ;
        }
        Call<ResBase<ResList<ResComment>>> call = MediaApiServiceProvider.getApiService().requestComments(id, mPage);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResList<ResComment>>>() {
            @Override
            public void onSuccess(ResBase<ResList<ResComment>> result) {
                listenner.onLoadFinish(isFirst,result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
            listenner.onLoadFailure(errorCode);
            }
        });
    }


    /**
     * 删除评论
     * @param id 需要删除的评论id
     */
    public void deleteComment(int id, IRequestCallback<ResBase> callback) {

        UserManager userManager = UserManager.getInstance();
        String token = userManager.getToken();


        Call<ResBase<ReqComment>> call = MediaApiServiceProvider.getApiService()
                .deleteComment(token, new ReqDeleteComment(id));

        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ReqComment>>() {
            @Override
            public void onSuccess(ResBase<ReqComment> result) {
                callback.onLoadFinish(result);
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }

    public void insertHistory(ResVideoDetail.ArchivesInfoBean videoInfo) {
        VideoHistoryRepository repository = new VideoHistoryRepository(BaseApplication.getContext());
        String userId = "0";

        if (isLogin()){
            userId = UserManager.getInstance().getUserInfo().getUser().getId();
        }
        VideoHistory videoHistory = repository.generateVideoHistory(userId, videoInfo.getId(), videoInfo.getTitle(), videoInfo.getChannel().getName(), videoInfo.getDuration(), videoInfo.getImage());
        repository.insert(videoHistory);
    }
}
