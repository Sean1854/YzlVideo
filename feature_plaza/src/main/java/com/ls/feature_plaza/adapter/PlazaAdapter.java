package com.ls.feature_plaza.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ls.feature_plaza.R;
import com.ls.feature_plaza.bean.PlazaXBannerData;
import com.ls.feature_plaza.bean.ResPlaza;
import com.ls.feature_plaza.databinding.ItemBannerBinding;
import com.ls.feature_plaza.databinding.ItemImageBinding;
import com.ls.libbase.utils.Glideutils;
import com.stx.xhb.androidx.XBanner;

import java.util.ArrayList;
import java.util.List;

public class PlazaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_BANNER = 1;//banner广告
    private static final int ITEM_TYPE_IMAGE = 2;//普通的item类型
    private List<ResPlaza.PlazaDetail> mLists;
    private ArrayList<PlazaXBannerData> mBannerDatas;
    private PlazaItemListener mListener;
    private ResPlaza mBannerData;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (ITEM_TYPE_BANNER == viewType) {
            ItemBannerBinding bannerBinding = ItemBannerBinding.inflate(layoutInflater, parent, false);
            BannerViewHolder viewHolder = new BannerViewHolder(bannerBinding);
            return viewHolder;
        } else {
            ItemImageBinding imageBinding = ItemImageBinding.inflate(layoutInflater, parent, false);
            ImageViewHolder viewHolder = new ImageViewHolder(imageBinding);
            return viewHolder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == ITEM_TYPE_BANNER){
            //加载头部广告数据
            BannerViewHolder viewHolder = (BannerViewHolder) holder;
            XBanner xbanner = viewHolder.bannerBinding.xbanner;
            //设置占位图
            xbanner.setBannerPlaceholderImg(R.mipmap.ic_launcher, ImageView.ScaleType.CENTER_CROP);
            //设置一屏多页广告
            xbanner.setIsClipChildrenMode(true);
            //设置banner的自定义布局和banner的数据源
            xbanner.setBannerData(R.layout.item_banner_child, mBannerDatas);

            //把自定义布局的xbanner和数据源绑定
            xbanner.loadImage(new XBanner.XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {
                    ImageView imageview = view.findViewById(R.id.image_wiew);
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    TextView tvLabel = view.findViewById(R.id.tv_label);

                    PlazaXBannerData data = mBannerDatas.get(position);
                    //用Glide进行广告图片装载
                    Glideutils.loadImage(data.getXBannerUrl(),imageview);

                    tvTitle.setText(data.getXBannerTitle());
                    tvLabel.setText(data.getDescription());
                }
            });

            xbanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    viewHolder.bannerBinding.tvIndicator.setText(String.valueOf(position + 1));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }else{
            ImageViewHolder ViewHolder = (ImageViewHolder) holder;
            ResPlaza.PlazaDetail detail = mLists.get(position-1);
            ViewHolder.binding.setData(detail);
        }

    }

    @Override
    public int getItemCount() {
        //一共有多少条数据
        int count = 0;
        if (mBannerDatas != null && mBannerDatas.size()>0){
            count += 1;
        }
        if (mLists != null && mLists.size()>0){
            count += mLists.size();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_BANNER : ITEM_TYPE_IMAGE;
    }

    public void setDatas(List<ResPlaza> datas) {
        if (datas != null && datas.size() >= 2){
            //获取广告数据
            mBannerData = datas.get(0);
            mBannerDatas = converXBannerDatas(mBannerData);


            ResPlaza imagData = datas.get(1);//普通的item数据

            mLists = imagData.getLists();

            //刷新数据（很重要）
            notifyDataSetChanged();
        }
    }

    private ArrayList<PlazaXBannerData> converXBannerDatas(ResPlaza data) {
        List<ResPlaza.PlazaDetail> lists = data.getLists();
        if (lists != null && lists.size()>0){
            ArrayList<PlazaXBannerData> xBannerData = new ArrayList<>();
            for (int i = 0; i < lists.size(); i++) {
                ResPlaza.PlazaDetail detail = lists.get(i);
                xBannerData.add(new PlazaXBannerData(detail.getImage(),
                        detail.getName(), detail.getDescription()));
            }
            return xBannerData;
        }

        return null;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        private final ItemBannerBinding bannerBinding;
        //banner广告的viewholder
        public BannerViewHolder(@NonNull ItemBannerBinding itemView) {
            super(itemView.getRoot());
            this.bannerBinding = itemView;
            //给广告设置点击事件
            bannerBinding.getRoot().setOnClickListener(view -> {
                if (mListener != null){
                    int position = bannerBinding.xbanner.getBannerCurrentItem();
                    List<ResPlaza.PlazaDetail> lists = mBannerData.getLists();
                    ResPlaza.PlazaDetail detail = lists.get(position);
                    mListener.onBannerClick(detail);
                }
            });

            bannerBinding.xbanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
                @Override
                public void onItemClick(XBanner banner, Object model, View view, int position) {
                    List<ResPlaza.PlazaDetail> lists = mBannerData.getLists();
                    ResPlaza.PlazaDetail detail = lists.get(position);
                    mListener.onBannerClick(detail);
                }
            });
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ItemImageBinding binding;
        public ImageViewHolder(@NonNull ItemImageBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            binding.getRoot().setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getLayoutPosition();
                    ResPlaza.PlazaDetail plazaDetail = mLists.get(position - 1);
                    mListener.onImageClick(plazaDetail);
                }
            });

        }
    }

    public interface PlazaItemListener {
        /**
         * 头部广告点击
         *
         * @param detail
         */
        void onBannerClick(ResPlaza.PlazaDetail detail);

        /**
         * 底部图片点击
         *
         * @param detail
         */
        void onImageClick(ResPlaza.PlazaDetail detail);
    }


    public void setListener(PlazaItemListener listenner) {
        mListener = listenner;
    }




}
