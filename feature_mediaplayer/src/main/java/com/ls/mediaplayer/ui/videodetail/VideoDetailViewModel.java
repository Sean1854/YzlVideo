package com.ls.mediaplayer.ui.videodetail;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.base.list.IListenner;
import com.ls.data_video.bean.ResComment;
import com.ls.data_video.bean.ResLike;
import com.ls.data_video.bean.ResSendComment;
import com.ls.data_video.bean.ResVideoDetail;
import com.ls.network.bean.ResBase;
import com.ls.network.bean.ResList;
import com.ls.network.config.ErrorStatusConfig;

import java.util.List;

public class VideoDetailViewModel extends BaseViewModel {
    private static final String TAG = "VideoDetailViewModel";
    private MutableLiveData<String> mChannel = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsLikes = new MutableLiveData<>(false);//是否点赞
    private MutableLiveData<Boolean> mIsCollection = new MutableLiveData<>(false);//是否收藏
    private MutableLiveData<Boolean> mIsLoadMore = new MutableLiveData<>(false);//是否允许加载更多


    //视频详情数据
    public MutableLiveData<ResVideoDetail.ArchivesInfoBean> mArchivesInfo = new MutableLiveData<>();

    //评论详情数据
    public MutableLiveData<List<ResComment>> mComments = new MutableLiveData<>();

    private final VideoDetailModel mModel;

    public VideoDetailViewModel() {
        mModel = new VideoDetailModel();
    }

    public MutableLiveData<ResVideoDetail.ArchivesInfoBean> getArchivesInfo() {
        return mArchivesInfo;
    }

    public MutableLiveData<String> getChannel() {
        return mChannel;
    }

    public MutableLiveData<Boolean> getIsLikes() {
        return mIsLikes;
    }

    public MutableLiveData<Boolean> getIsCollection() {
        return mIsCollection;
    }

    public MutableLiveData<Boolean> getIsLoadMore() {
        return mIsLoadMore;
    }

    public MutableLiveData<List<ResComment>> getComments() {
        return mComments;
    }

    public void requestDetail(int id) {
        showLoading(true);
        mModel.requestDetail(id, new IRequestCallback<ResVideoDetail>() {
            @Override
            public void onLoadFinish(ResVideoDetail datas) {
                showLoading(false);
                ResVideoDetail.ArchivesInfoBean archivesInfo = datas.getArchivesInfo();
                mArchivesInfo.setValue(archivesInfo);
                String channel = archivesInfo.getChannel().getName();
                mChannel.setValue("#" + channel);
                mIsLikes.setValue(archivesInfo.getIslike() == 1);//是否有点赞 1表示已点赞
                mIsCollection.setValue(archivesInfo.getIscollection() == 1);//是否有收藏 1表示已收藏

                //插入浏览记录到数据库
                mModel.insertHistory(datas.getArchivesInfo());

            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                showLoading(false);
                showToast(meesage);
            }
        });
    }

    /**
     * 点赞和取消点赞功能
     */
    public void onLikeClick() {
        showLoading(true);
        Boolean login = mModel.isLogin();
        ResVideoDetail.ArchivesInfoBean info = mArchivesInfo.getValue();
        int likeId = info.getId();
        if (login) {
            //是否点赞
            Boolean isLike = mIsLikes.getValue();
            if (!isLike) {
                mModel.requeslike(likeId, new IRequestCallback<ResLike>() {
                    @Override
                    public void onLoadFinish(ResLike datas) {
                        showLoading(false);
                        mIsLikes.setValue(true);
                        info.setIslike(1);
                        info.setLikes(info.getLikes() + 1);
                        mArchivesInfo.setValue(info);
                    }

                    @Override
                    public void onLoadFailure(int errorCode, String meesage) {
                        showLoading(false);
                        showToast(meesage);
                    }
                });
            } else {
                mModel.requestCancelLike(likeId, new IRequestCallback<ResLike>() {
                    @Override
                    public void onLoadFinish(ResLike datas) {
                        showLoading(false);
                        mIsLikes.setValue(false);
                        info.setIslike(0);
                        info.setLikes(info.getLikes() - 1);
                        mArchivesInfo.setValue(info);
                    }

                    @Override
                    public void onLoadFailure(int errorCode, String meesage) {
                        showLoading(false);
                        showToast(meesage);
                    }
                });
            }

        } else {
            showLoading(false);
            startLogin();
        }
    }

    public void onCollectionClick() {
        showLoading(true);
        Boolean login = mModel.isLogin();
        ResVideoDetail.ArchivesInfoBean info = mArchivesInfo.getValue();
        int videoId = info.getId();
        if (login) {
            //是否收藏
            Boolean isCollection = mIsCollection.getValue();
            if (!isCollection) {//收藏
                mModel.requestCollection(videoId, new IRequestCallback<ResLike>() {
                    @Override
                    public void onLoadFinish(ResLike datas) {
                        showLoading(false);
                        mIsCollection.setValue(true);
                        info.setIscollection(1);
                        info.setCollection(info.getCollection() + 1);
                        mArchivesInfo.setValue(info);
                    }

                    @Override
                    public void onLoadFailure(int errorCode, String meesage) {
                        showLoading(false);
                        showToast(meesage);
                    }
                });
            } else {//取消收藏
                mModel.requestCancelCollection(videoId, new IRequestCallback<ResLike>() {
                    @Override
                    public void onLoadFinish(ResLike datas) {
                        showLoading(false);
                        mIsCollection.setValue(false);
                        info.setIscollection(0);
                        info.setCollection(info.getCollection() - 1);
                        mArchivesInfo.setValue(info);
                    }

                    @Override
                    public void onLoadFailure(int errorCode, String meesage) {
                        showLoading(false);
                        showToast(meesage);
                    }
                });
            }
        } else {
            showLoading(false);
            startLogin();
        }
    }


    /**
     * 发送评论
     *
     * @param message
     */
    public void sendComment(String message) {
        showLoading(true);
        int aid = mArchivesInfo.getValue().getId();
        mModel.sendComment(aid, message, new IRequestCallback<ResSendComment>() {
            @Override
            public void onLoadFinish(ResSendComment datas) {
                showLoading(false);
                showToast("发送成功");
                List<ResComment> list = mComments.getValue();
                if (list != null) {
                    list.add(0, datas.getComment());
                    mComments.setValue(list);
                }
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                showLoading(false);
                showToast(meesage);
            }
        });
    }

    /**
     * 请求评论列表
     */
    public void requestComment(Boolean isFirst) {
        int id = mArchivesInfo.getValue().getId();
        mModel.requestComment(id, isFirst, new IListenner<ResComment>() {
            @Override
            public void onLoadFinish(boolean isFirst, ResList<ResComment> videos) {
                List<ResComment> list = videos.getList();

                mIsLoadMore.setValue(list != null && list.size() >= 10);//只有当返回数据大于10条才能加载更多评论
                if (isFirst) {
                    mComments.setValue(videos.getList());
                } else {
                    List<ResComment> comments = mComments.getValue();
                    comments.addAll(list);
                    mComments.setValue(comments);
                }
            }

            @Override
            public void onLoadFailure(int statusCode) {
                if (statusCode == ErrorStatusConfig.ERROR_STATUS_EMPTY) {
                    showToast("没有更多数据了!");
                    mIsLoadMore.setValue(false);
                }
            }
        });
    }

    /**
     * 删除评论
     *
     * @param mComment
     */
    public void deleteComment(ResComment mComment) {
        showLoading(true);
        mModel.deleteComment(mComment.getId(), new IRequestCallback<ResBase>() {
            @Override
            public void onLoadFinish(ResBase datas) {
                showLoading(false);
                showToast(datas.getMsg());
                List<ResComment> comments = mComments.getValue();
                comments.remove(mComment);
                mComments.setValue(comments);
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                showLoading(false);
                showToast(meesage);

            }
        });

    }
}
