package com.ls.feature_user.ui.editInfo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.api.UserApiServiceProvider;
import com.ls.feature_user.bean.ResUpload;
import com.ls.feature_user.databinding.ActivityEditUserInfoBinding;
import com.ls.feature_user.ui.camera.CameraActivity;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.ResUser;
import com.ls.libbase.bean.UserInfo;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.libbase.manager.UserManager;
import com.ls.libbase.ui.dialog.YesOrNoDialog;
import com.ls.libbase.utils.StatusBarUtils;
import com.ls.network.bean.ResBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Route(path = ARouterPath.User.ACTIVITY_EDITUSERINFO)
public class EditUserInfoActivity extends BaseActivity<ActivityEditUserInfoBinding,EditUserInfoViewModel> {

    /** 拍照结果：CameraActivity 回传图片绝对路径 */
    private final ActivityResultLauncher<Intent> mCameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String path = result.getData().getStringExtra(CameraActivity.EXTRA_AVATAR_FILE);
                    if (path != null) {
                        uploadAvatar(Uri.fromFile(new File(path)));
                    }
                }
            });

    /** 相册选择结果：返回图片 Uri */
    private final ActivityResultLauncher<String> mAlbumLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    uploadAvatar(uri);
                }
            });

    @Override
    protected EditUserInfoViewModel getViewModel() {
        return new ViewModelProvider(this).get(EditUserInfoViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_user_info;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        mDataBinding.tvEditAvatar.setOnClickListener(view -> {
            showAvatarSelectDialog();
        });
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    public void finish() {
        //资料发生变化，退出需要特殊处理
        if (mViewModel.isChange()) {
            YesOrNoDialog.showDialog(this, "提示", "是否保存更新？", new YesOrNoDialog.Callback() {
                @Override
                public void onConfirm() {
                    //提交保存，提交成功后会再执行super.finish
                    mViewModel.updateUserInfo();
                }
            });
        } else {
            super.finish();//一定要调用父类的finish，否则就是调用当前类中的finish
        }
    }

    public void showAvatarSelectDialog(){
        // 创建并显示 DialogFragment 弹窗（沿用底部滑入样式）
        PictureSelectDialog dialog = PictureSelectDialog.newInstance();
        dialog.setOnPictureSelectListener(new PictureSelectDialog.OnPictureSelectListener() {
            @Override
            public void onTakePhoto() {
                // 跳转自定义相机页拍照（通过 ARouter 构造 Intent，交给 launcher 启动）
                Intent cameraIntent = new Intent(EditUserInfoActivity.this, CameraActivity.class);
                mCameraLauncher.launch(cameraIntent);
            }

            @Override
            public void onPickFromAlbum() {
                // 打开系统相册选择图片（不裁剪，原图上传）
                mAlbumLauncher.launch("image/*");
            }
        });
        dialog.show(getSupportFragmentManager(), "PictureSelectDialog");
    }

    /**
     * 统一上传头像：将图片压缩后通过 uploadFile 接口上传，
     * 成功则更新本地缓存与 UI，失败 Toast 提示。
     */
    private void uploadAvatar(Uri uri) {
        new Thread(() -> {
            try {
                File compressed = compressImage(uri);
                if (compressed == null) {
                    runOnUiThread(() -> Toast.makeText(this, "图片处理失败", Toast.LENGTH_SHORT).show());
                    return;
                }
                RequestBody requestFile = RequestBody.create(
                        MediaType.parse("image/*"), compressed);
                MultipartBody.Part body = MultipartBody.Part.createFormData(
                        "file", compressed.getName(), requestFile);

                String token = UserManager.getInstance().getToken();
                UserApiServiceProvider.getApiService()
                        .uploadFile(token, body)
                        .enqueue(new Callback<ResBase<ResUpload>>() {
                            @Override
                            public void onResponse(Call<ResBase<ResUpload>> call,
                                                   Response<ResBase<ResUpload>> response) {
                                ResBase<ResUpload> res = response.body();
                                if (res != null && res.getCode() == 1 && res.getData() != null) {
                                    String fullUrl = res.getData().getFullurl();
                                    persistAvatar(fullUrl);
                                    // 通知用户页刷新（头像/资料）
                                    MessageEvent.LoginStatusEvent.post(true);
                                } else {
                                    String msg = res != null ? res.getMsg() : "上传失败";
                                    runOnUiThread(() ->
                                            Toast.makeText(EditUserInfoActivity.this, msg, Toast.LENGTH_SHORT).show());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResBase<ResUpload>> call, Throwable t) {
                                runOnUiThread(() ->
                                        Toast.makeText(EditUserInfoActivity.this, "网络错误，上传失败", Toast.LENGTH_SHORT).show());
                            }
                        });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(EditUserInfoActivity.this, "图片处理异常", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    /** 将图片压缩到合适尺寸/质量，避免 OOM 与大流量 */
    private File compressImage(Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            if (is == null) return null;
            Bitmap src = BitmapFactory.decodeStream(is);
            is.close();
            if (src == null) return null;

            // 读取 EXIF 方向，将 Bitmap 转正（预览用 ImageView 自动处理，
            // 但 decodeStream 不会读 EXIF，直接压缩上传会导致方向错误）
            Bitmap oriented = src;
            try {
                InputStream exifIs = getContentResolver().openInputStream(uri);
                if (exifIs != null) {
                    ExifInterface exif = new ExifInterface(exifIs);
                    int rotation = exif.getRotationDegrees();
                    if (rotation != 0) {
                        Matrix m = new Matrix();
                        m.postRotate(rotation);
                        oriented = Bitmap.createBitmap(src, 0, 0,
                                src.getWidth(), src.getHeight(), m, true);
                        if (oriented != src) src.recycle();
                    }
                    exifIs.close();
                }
            } catch (Exception ignore) {
                // EXIF 读取失败则保持原样
            }

            // 缩放到最长边不超过 1080
            int maxSide = 1080;
            int w = oriented.getWidth();
            int h = oriented.getHeight();
            float scale = Math.min(1f, maxSide / (float) Math.max(w, h));
            Bitmap out = oriented;
            if (scale < 1f) {
                out = Bitmap.createScaledBitmap(oriented,
                        Math.round(w * scale), Math.round(h * scale), true);
                if (out != oriented) oriented.recycle();
            }

            File file = new File(getExternalFilesDir(null),
                    "avatar_upload_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.flush();
            fos.close();
            if (out != oriented) out.recycle();
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    /** 更新头像：先刷新界面，再把头像关系提交到服务器（与昵称/简介一并保存） */
    private void persistAvatar(String fullUrl) {
        // 更新 LiveData，让 ViewModel 拿到最新头像地址
        mViewModel.getAvatarUrl().setValue(fullUrl);
        mViewModel.updateUserInfo(new IRequestCallback<ResBase>() {
            @Override
            public void onLoadFinish(ResBase datas) {
                runOnUiThread(() ->
                        Toast.makeText(EditUserInfoActivity.this, "头像已更新", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onLoadFailure(int errorCode, String meesage) {
                runOnUiThread(() ->
                        Toast.makeText(EditUserInfoActivity.this, meesage, Toast.LENGTH_SHORT).show());
            }
        });
    }
}
