package com.ls.feature_user.ui.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.ls.feature_user.databinding.ActivityCameraBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.config.ARouterPath;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自定义相机页：前后摄像头切换 + 拍摄后预览确认。
 * 通过 setResult 回传拍摄图片的绝对路径（EXTRA_AVATAR_FILE）。
 */
@Route(path = ARouterPath.User.ACTIVITY_CAMERA)
public class CameraActivity extends BaseActivity<ActivityCameraBinding, BaseViewModel> {

    public static final String EXTRA_AVATAR_FILE = "extra_avatar_file";

    private static final int REQ_CAMERA_PERMISSION = 1001;

    private ImageCapture mImageCapture;
    private ProcessCameraProvider mCameraProvider;
    private ExecutorService mCameraExecutor;
    private int mFacing = CameraSelector.LENS_FACING_BACK;
    private File mPhotoFile;

    /** 运行时相机权限申请 */
    private final ActivityResultLauncher<String> mPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (Boolean.TRUE.equals(granted)) {
                    startCamera();
                } else {
                    Toast.makeText(this, "需要相机权限才能拍照", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

    @Override
    protected void initView() {
        mDataBinding.btnClose.setOnClickListener(v -> finish());
        mDataBinding.btnSwitch.setOnClickListener(v -> switchCamera());
        mDataBinding.btnShutter.setOnClickListener(v -> takePhoto());
        mDataBinding.btnRetake.setOnClickListener(v -> retake());
        mDataBinding.btnConfirm.setOnClickListener(v -> confirmPhoto());
    }

    @Override
    protected void initData() {
        mCameraExecutor = Executors.newSingleThreadExecutor();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            mPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> future =
                ProcessCameraProvider.getInstance(this);
        future.addListener(() -> {
            try {
                mCameraProvider = future.get();
                bindCamera(mCameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "相机初始化失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCamera(@NonNull ProcessCameraProvider provider) {
        provider.unbindAll();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(mDataBinding.previewView.getSurfaceProvider());

        mImageCapture = new ImageCapture.Builder()
                .setTargetRotation(mDataBinding.previewView.getDisplay().getRotation())
                .build();

        CameraSelector selector = new CameraSelector.Builder()
                .requireLensFacing(mFacing)
                .build();
        try {
            provider.bindToLifecycle(this, selector, preview, mImageCapture);
        } catch (Exception e) {
            Toast.makeText(this, "该摄像头不可用", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchCamera() {
        mFacing = (mFacing == CameraSelector.LENS_FACING_BACK)
                ? CameraSelector.LENS_FACING_FRONT
                : CameraSelector.LENS_FACING_BACK;
        if (mCameraProvider != null) {
            bindCamera(mCameraProvider);
        }
    }

    private void takePhoto() {
        if (mImageCapture == null) return;
        mPhotoFile = new File(getExternalFilesDir(null),
                "avatar_" + System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions options =
                new ImageCapture.OutputFileOptions.Builder(mPhotoFile).build();

        mImageCapture.takePicture(options, mCameraExecutor,
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        runOnUiThread(() -> showPreview());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        runOnUiThread(() ->
                                Toast.makeText(CameraActivity.this, "拍摄失败", Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void showPreview() {
        mDataBinding.previewView.setVisibility(android.view.View.GONE);
        mDataBinding.btnSwitch.setVisibility(android.view.View.GONE);
        mDataBinding.layoutCapture.setVisibility(android.view.View.GONE);
        mDataBinding.ivPreview.setVisibility(android.view.View.VISIBLE);
        mDataBinding.layoutConfirm.setVisibility(android.view.View.VISIBLE);
        mDataBinding.ivPreview.setImageURI(Uri.fromFile(mPhotoFile));
    }

    private void retake() {
        if (mPhotoFile != null && mPhotoFile.exists()) {
            mPhotoFile.delete();
            mPhotoFile = null;
        }
        mDataBinding.ivPreview.setVisibility(android.view.View.GONE);
        mDataBinding.layoutConfirm.setVisibility(android.view.View.GONE);
        mDataBinding.previewView.setVisibility(android.view.View.VISIBLE);
        mDataBinding.btnSwitch.setVisibility(android.view.View.VISIBLE);
        mDataBinding.layoutCapture.setVisibility(android.view.View.VISIBLE);
    }

    private void confirmPhoto() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            finish();
            return;
        }
        Intent result = new Intent();
        result.putExtra(EXTRA_AVATAR_FILE, mPhotoFile.getAbsolutePath());
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraExecutor != null) {
            mCameraExecutor.shutdown();
        }
    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return com.ls.feature_user.R.layout.activity_camera;
    }

    @Override
    protected int getBindingVariableId() {
        return 0;
    }
}
