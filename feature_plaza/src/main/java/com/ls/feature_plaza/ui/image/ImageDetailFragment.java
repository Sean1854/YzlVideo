package com.ls.feature_plaza.ui.image;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.feature_plaza.R;
import com.ls.feature_plaza.databinding.ItemImageDetailBinding;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.config.ARouterPath;

@Route(path = ARouterPath.Plaza.FRAGMENT_IMAGE_DETAIL)
public class ImageDetailFragment extends BaseFragment<ItemImageDetailBinding, BaseViewModel> {

    @Autowired(name = ARouterPath.Plaza.KEY_IMAGE_URL)
    public String mImageUrl;

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return  R.layout.item_image_detail;
    }

    @Override
    protected int getBindingVariableId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mDataBinding.setImgUrl(mImageUrl);
    }
}
