package com.weima.aishangyi.jiaoshi.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.volley.VolleyError;
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
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.widget.BottomMenuDialog;

import java.io.File;
import java.util.HashMap;

/**
 * 个人资料
 *
 * @author cgy
 */
public class PersonInfoActivity extends BaseActivity {
    private ImageView imv_user_avater;
    private BottomMenuDialog bottomMenuDialog;
    private String mTempFilePath = AppHelper.getBaseCachePath()
            .concat(String.valueOf(System.currentTimeMillis())).concat(".jpg");
    private String imageUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("个人信息");
        setContentView(R.layout.activity_person_info);
        initUI();
        initData();
    }

    private void initUI() {
        imv_user_avater = findView(R.id.imv_user_avater);//头像
        findView(R.id.btn_avatar).setOnClickListener(mClickListener);
        findView(R.id.btn_coversetting).setOnClickListener(mClickListener);
        findView(R.id.btn_campaign).setOnClickListener(mClickListener);
        findView(R.id.btn_personablum).setOnClickListener(mClickListener);
    }

    private void initData() {
        CurrentUser currentUser = CurrentUser.getInstance();
        if (Helper.isNotEmpty(currentUser) && Helper.isNotEmpty(currentUser.getIcon())){
            Picasso.with(PersonInfoActivity.this).load(currentUser.getIcon()).placeholder(R.drawable.ic_avatar_default).into(imv_user_avater);
        }
    }

    private void doSaveInfo() {
        ProgressDialogHelper.showProgressDialog(PersonInfoActivity.this, "上传中...");
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("icon", imageUrl);
        post(ProjectConstants.Url.ACCOUNT_EDIT_INFO, requestMap);
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        ProgressDialogHelper.dismissProgressDialog();
        CommonEntity entity = JsonHelper.fromJson(response,CommonEntity.class);
        if ("200".equals(entity.getCode())){
            LocalBroadcastManager.getInstance(PersonInfoActivity.this).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_USER_INFO));
            ToastHelper.showToast("上传成功");
        }else{
            ToastHelper.showToast(entity.getMessage());
        }
        return true;
    }

    @Override
    public boolean onResponseError(int gact, String response,
                                   VolleyError error, Object... extras) {
        ProgressDialogHelper.dismissProgressDialog();
        ToastHelper.showToast("上传失败");
        return true;
    }

    /**
     * 点击事件
     */
    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_avatar://头像
                    pickPhoto();
                    break;
                case R.id.btn_coversetting://封面设置
                    NavigationHelper.startActivity(PersonInfoActivity.this, CoverSettingActivity.class, null, false);
                    break;
                case R.id.btn_personablum://个人相册
                    NavigationHelper.startActivity(PersonInfoActivity.this, PersonAlbumActivity.class, null, false);
                    break;
                case R.id.btn_campaign://宣传简介
                    NavigationHelper.startActivity(PersonInfoActivity.this, CampaignActivity.class, null, false);
                    break;
                default:
                    break;
            }
        }
    };

    //begin region 上传头像
    private void pickPhoto() {
        if (bottomMenuDialog == null) {
            bottomMenuDialog = new BottomMenuDialog.Builder(PersonInfoActivity.this)
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
            case 1:
                // 相册
                cropPickedImage(data.getData());
                break;
            case 2:
                // 拍照
                File file = new File(mTempFilePath);
                if (!file.exists()) {
                    return;
                }
                cropPickedImage(Uri.parse("file://".concat(mTempFilePath)));
                break;
            case 3:
                upload(ProjectConstants.Url.FILE_UPLOAD, "file", mTempFilePath, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        if (responseBody != null){
                            String response = new String(responseBody);
                            if (Helper.isNotEmpty(response)){
                                UploadResp entity = JsonHelper.fromJson(response, UploadResp.class);
                                if (Helper.isNotEmpty(entity) && "200".equals(entity.getCode())){
                                    if (Helper.isNotEmpty(entity.getData()) && Helper.isNotEmpty(entity.getData().getUrl())){
                                        imageUrl = entity.getData().getUrl();
                                        Picasso.with(PersonInfoActivity.this).load(imageUrl).placeholder(R.drawable.ic_avatar_default).into(imv_user_avater);
                                        doSaveInfo();
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
                break;
            default:
                break;
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
     * 裁剪选取的图片
     *
     * @param uri
     */
    private void cropPickedImage(Uri uri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");// 可裁剪
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("scale", true);

            // 图片缓存的地址
            mTempFilePath = AppHelper.getBaseCachePath()
                    .concat(String.valueOf(System.currentTimeMillis()))
                    .concat(".jpg");
            File file = new File(mTempFilePath);
            Uri uriCache = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCache);
            intent.putExtra("outputFormat", "JPEG");// 图片格式
            intent.putExtra("noFaceDetection", true);// 取消人脸识别
            intent.putExtra("return-data", false);
            startActivityForResult(intent, 3);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    //end region 上传头像


}
