package com.weima.aishangyi.jiaoshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.kevin.crop.UCrop;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mb.android.utils.AppHelper;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.squareup.picasso.Picasso;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.entity.UploadResp;
import com.weima.aishangyi.jiaoshi.utils.PathHelper;
import com.weima.aishangyi.jiaoshi.widget.BottomMenuDialog;
import com.weima.aishangyi.jiaoshi.widget.CircleTransform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * 封面设置
 */
public class CoverSettingActivity extends BaseActivity {
    private ImageView image_cover,video_cover;
    private BottomMenuDialog bottomMenuDialog;
    private String mTempFilePath;
    private Uri mDestinationUri;
    private String imageUrl = "";
    private String videoUrl = "";
    private int type=0;//0：图片  1：视频
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("封面设置");
        setRightButton("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveInfo();
            }
        });
        setContentView(R.layout.activity_cover_setting);
        initUI();
        initData();
    }


    private void initUI() {
        mTempFilePath = AppHelper.getBaseCachePath().concat(String.valueOf(System.currentTimeMillis())).concat(".jpg");
        mDestinationUri = Uri.fromFile(new File(mTempFilePath));
        image_cover = findView(R.id.image_cover);
        video_cover = findView(R.id.video_cover);
        image_cover.setOnClickListener(mClickListener);
        video_cover.setOnClickListener(mClickListener);
    }

    private void initData() {
        CurrentUser currentUser = CurrentUser.getInstance();
        if (Helper.isNotEmpty(currentUser) && Helper.isNotEmpty(currentUser.getImage_cover())){
            imageUrl = currentUser.getImage_cover();
            Picasso.with(CoverSettingActivity.this).load(imageUrl).placeholder(R.drawable.img_default).into(image_cover);
        }
        if (Helper.isNotEmpty(currentUser) && Helper.isNotEmpty(currentUser.getVideo_cover())){
            videoUrl = currentUser.getVideo_cover();
            Picasso.with(CoverSettingActivity.this).load(videoUrl).placeholder(R.drawable.img_default).into(video_cover);
        }
    }

    private void doSaveInfo() {
        if (Helper.isEmpty(imageUrl) && Helper.isEmpty(videoUrl)){
            ToastHelper.showToast("请设置视频封面或图片封面");
            return;
        }
        ProgressDialogHelper.showProgressDialog(CoverSettingActivity.this, "保存中...");
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        if (Helper.isNotEmpty(imageUrl)){
            requestMap.put("image_cover", imageUrl);
        }
        if (Helper.isNotEmpty(videoUrl)){
            requestMap.put("video_cover", videoUrl);
        }
        post(ProjectConstants.Url.ACCOUNT_EDIT_INFO, requestMap);
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        ProgressDialogHelper.dismissProgressDialog();
        CommonEntity entity = JsonHelper.fromJson(response,CommonEntity.class);
        if ("200".equals(entity.getCode())){
            LocalBroadcastManager.getInstance(CoverSettingActivity.this).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_USER_INFO));
            ToastHelper.showToast("保存成功");
            finish();
        }else{
            ToastHelper.showToast(entity.getMessage());
        }
        return true;
    }

    @Override
    public boolean onResponseError(int gact, String response,
                                   VolleyError error, Object... extras) {
        ProgressDialogHelper.dismissProgressDialog();
        ToastHelper.showToast("保存失败");
        return true;
    }

    /**
     * 点击事件
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_cover:
                    type = 0;
                    pickPhoto();
                    break;
                case R.id.video_cover:
                    type = 1;
                    pickPhoto();
                    break;
                default:
                    break;
            }
        }
    };



    //begin region 上传头像
    private void pickPhoto() {
        if (bottomMenuDialog == null) {
            bottomMenuDialog = new BottomMenuDialog.Builder(CoverSettingActivity.this)
                    .addMenu("拍照", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pickImageFromCamera();
                            bottomMenuDialog.dismiss();
                        }
                    }).addMenu("相册", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pickImageFromGallery();
                            bottomMenuDialog.dismiss();
                        }
                    }).create();
        }
        bottomMenuDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1: // 相册
                startCropActivity(data.getData());
                break;
            case 2:  // 拍照
                File file = new File(mTempFilePath);
                if (!file.exists()) {
                    return;
                }
                startCropActivity(Uri.fromFile(file));
                break;
            case UCrop.REQUEST_CROP:    // 裁剪图片结果
                handleCropResult(data);
                break;
            case UCrop.RESULT_ERROR:    // 裁剪图片错误
                handleCropError(data);
                break;
            default:
                break;
        }
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri) {
            try {
                String path = PathHelper.getRealFilePath(CoverSettingActivity.this,resultUri);
                upload(ProjectConstants.Url.FILE_UPLOAD, "file", path, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        if (responseBody != null){
                            String response = new String(responseBody);
                            if (Helper.isNotEmpty(response)){
                                UploadResp entity = JsonHelper.fromJson(response, UploadResp.class);
                                if (Helper.isNotEmpty(entity) && "200".equals(entity.getCode())){
                                    if (Helper.isNotEmpty(entity.getData()) && Helper.isNotEmpty(entity.getData().getUrl())){
                                        if (type == 0){
                                            imageUrl = entity.getData().getUrl();
                                            Picasso.with(CoverSettingActivity.this).load(imageUrl).placeholder(R.drawable.img_default).into(image_cover);
                                        }else{
                                            videoUrl = entity.getData().getUrl();
                                            Picasso.with(CoverSettingActivity.this).load(videoUrl).placeholder(R.drawable.img_default).into(video_cover);
                                        }
                                    }
                                }else{
                                    ToastHelper.showToast("上传失败！");
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        ToastHelper.showToast("上传失败！");
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(CoverSettingActivity.this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result) {
        deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e("cropError", "handleCropError: ", cropError);
            Toast.makeText(CoverSettingActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CoverSettingActivity.this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除拍照临时文件
     */
    private void deleteTempPhotoFile() {
        File tempFile = new File(mTempFilePath);
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }

    /**
     * 相册图片选取
     */
    private void pickImageFromGallery() {
        try {
            Intent intentFromGallery = new Intent();
            // 设置文件类型
            intentFromGallery.setType("image/*");
            intentFromGallery.setAction(Intent.ACTION_PICK);
            startActivityForResult(intentFromGallery, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍照获取图片
     */
    private void pickImageFromCamera() {
        try {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            // 图片缓存的地址
            mTempFilePath = AppHelper.getBaseCachePath()
                    .concat(String.valueOf(System.currentTimeMillis()))
                    .concat(".jpg");
            File file = new File(mTempFilePath);
            Uri uri = Uri.fromFile(file);
            // 设置图片的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startCropActivity(Uri uri) {
        UCrop.of(uri, mDestinationUri)
                .withAspectRatio(25, 14)
                .withMaxResultSize(750, 420)
                .withTargetActivity(CropActivity.class)
                .start(CoverSettingActivity.this);
    }

    //end region 上传头像


}
