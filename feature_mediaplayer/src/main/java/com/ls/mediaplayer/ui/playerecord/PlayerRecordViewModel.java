package com.ls.mediaplayer.ui.playerecord;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.mediaplayer.db.VideoHistory;
import com.ls.mediaplayer.db.VideoHistoryRepository;

import java.util.HashMap;
import java.util.List;

public class PlayerRecordViewModel extends BaseViewModel {

    private final PlayerRecordModel mModel;
    private MutableLiveData<List<VideoHistory>> mDatas = new MutableLiveData<>();//历史浏览记录数据
    private MutableLiveData<Boolean> mSelectStatus = new MutableLiveData<>(false);//当前是否处于勾选删除的状态

    private HashMap<VideoHistory,Boolean> mSelectDelDatas ;

    public PlayerRecordViewModel() {
        mModel = new PlayerRecordModel();
    }

    /**
     * 浏览记录
     */
    public void requestHistory(){
        showLoading(true);
        mModel.requestHistory(new IRequestCallback<List<VideoHistory>>() {
            @Override
            public void onLoadFinish(List<VideoHistory> datas) {
                mDatas.setValue(datas);
                showLoading(false);
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
            showToast(meesage);
            showLoading(false);
            }
        });
    }

    /**
     * 多选操作
     */
    public void onSelectClick(){
        if (!mSelectStatus.getValue()){
            mSelectStatus.setValue(true);
            //点击编辑后就开始初始化
            mSelectDelDatas = new HashMap<>();
        }else {
            mSelectStatus.setValue(false);
            //未勾选任何记录时直接退出编辑模式，不发起删除请求
            if (mSelectDelDatas == null || mSelectDelDatas.isEmpty()){
                mSelectDelDatas = null;
                return;
            }
            mModel.deleterById(mSelectDelDatas, new IRequestCallback<String>() {
                @Override
                public void onLoadFinish(String datas) {
                    showToast(datas);
                    //删除后重新加载
                    requestHistory();
                }

                @Override
                public void onLoadFailure(int errorCode, String meesage) {
                    showToast(meesage);
                }
            });
            mSelectDelDatas = null;
        }
    }

    public MutableLiveData<List<VideoHistory>> getDatas() {
        return mDatas;
    }

    public MutableLiveData<Boolean> getSelectStatus() {
        return mSelectStatus;
    }

    public void updataIsSelect(VideoHistory videoHistory, boolean isSelect) {
        if (mSelectDelDatas == null) {
            return;//非编辑模式下不处理
        }
        if (mSelectDelDatas.containsKey(videoHistory) && !isSelect){
            //如果hasmap里有相同的key并且是没有勾选就移除
            mSelectDelDatas.remove(videoHistory);
        }else {
            //如果hasmap里没有相同的key，处于勾选状态 添加到列表
            mSelectDelDatas.put(videoHistory,isSelect);
        }
    }
}
