package com.weima.aishangyi.jiaoshi.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.weima.aishangyi.jiaoshi.entity.CertifyInfoResp;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.entity.UploadResp;
import com.weima.aishangyi.jiaoshi.utils.PathHelper;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;
import com.weima.aishangyi.jiaoshi.widget.BottomMenuDialog;
import com.weima.aishangyi.jiaoshi.widget.CircleTransform;

import java.io.File;
import java.util.HashMap;

/**
 * 身份认证
 */
public class IdentityActivity extends BaseActivity {
    private EditText edit_name,edit_idcard;
    private ImageView imv_facephoto,imv_idcardphoto;
    private Button btn_commit;
    private String mTempFilePath;
    private Uri mDestinationUri;
    private String facePhotoUrl = "";
    private String idPhotoUrl = "";
    private BottomMenuDialog bottomMenuDialog;
    private int type = 0;//0手持身份证 1身份证正面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("身份认证");
        setContentView(R.layout.activity_identity);
        initUI();
        initData();
    }

    private void initUI() {
        mTempFilePath = AppHelper.getBaseCachePath().concat(String.valueOf(System.currentTimeMillis())).concat(".jpg");
        mDestinationUri = Uri.fromFile(new File(mTempFilePath));
        btn_commit = findView(R.id.btn_commit);
        edit_name = findView(R.id.edit_name);
        edit_idcard = findView(R.id.edit_idcard);
        imv_facephoto = findView(R.id.imv_facephoto);
        imv_idcardphoto = findView(R.id.imv_idcardphoto);
        imv_facephoto.setOnClickListener(mClickListener);
        imv_idcardphoto.setOnClickListener(mClickListener);
        btn_commit.setOnClickListener(mClickListener);
    }

    private void initData(){
        int type = getIntent().getIntExtra("type",0);
        if (type==1){
            CertifyInfoResp.CertifyInfo certifyInfo = (CertifyInfoResp.CertifyInfo) getIntent().getSerializableExtra(ProjectConstants.BundleExtra.KEY_CERTIFY_INFO);
            if (Helper.isNotEmpty(certifyInfo)){
                edit_name.setText(ProjectHelper.getCommonText(certifyInfo.getName()));
                edit_name.setSelection(ProjectHelper.getCommonSeletion(certifyInfo.getName()));
                edit_idcard.setText(ProjectHelper.getCommonText(certifyInfo.getIdCard()));
                edit_idcard.setSelection(ProjectHelper.getCommonSeletion(certifyInfo.getIdCard()));
                if (Helper.isNotEmpty(certifyInfo.getFront_card())){
                    facePhotoUrl = certifyInfo.getFront_card();
                    Picasso.with(IdentityActivity.this).load(facePhotoUrl).placeholder(R.drawable.ic_selfie).into(imv_facephoto);
                }
                if (Helper.isNotEmpty(certifyInfo.getBack_card())){
                    idPhotoUrl = certifyInfo.getBack_card();
                    Picasso.with(IdentityActivity.this).load(idPhotoUrl).placeholder(R.drawable.ic_facephoto).into(imv_idcardphoto);
                }
            }
        }
    }


    /**
     * 点击事件
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imv_facephoto://手持身份证照片
                    type = 0;
                    pickPhoto();
                    break;
                case R.id.imv_idcardphoto://身份证正面照片
                    type = 1;
                    pickPhoto();
                    break;
                case R.id.btn_commit://
                    doSaveInfo();
                    break;
                default:
                    break;
            }
        }
    };


    private void doSaveInfo(){
        String name = edit_name.getText().toString();
        String idCard = edit_idcard.getText().toString();
        if (Helper.isEmpty(name)){
            ToastHelper.showToast("请输入真实姓名");
            return;
        }
        if (Helper.isEmpty(idCard)){
            ToastHelper.showToast("身份证号码");
            return;
        }
        if (!ProjectHelper.isIdcard(idCard)){
            ToastHelper.showToast("身份证号码输入有误");
            return;
        }
        if (Helper.isEmpty(facePhotoUrl)){
            ToastHelper.showToast("请添加手持身份证照片");
            return;
        }
        if (Helper.isEmpty(idPhotoUrl)){
            ToastHelper.showToast("请添加身份证正面照片");
            return;
        }
        ProgressDialogHelper.showProgressDialog(IdentityActivity.this, "提交中...");
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("name", name);
        requestMap.put("idCard", idCard);
        requestMap.put("front_card", facePhotoUrl);
        requestMap.put("back_card", idPhotoUrl);
        post(ProjectConstants.Url.ACCOUNT_INFO_CERTIFY, requestMap);
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        ProgressDialogHelper.dismissProgressDialog();
        CommonEntity entity = JsonHelper.fromJson(response,CommonEntity.class);
        if ("200".equals(entity.getCode())){
            CurrentUser.getInstance().setIdCertification(1);
            LocalBroadcastManager.getInstance(IdentityActivity.this).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_USER_INFO));
            ToastHelper.showToast("已提交审核，请耐心等待...");
            setResult(RESULT_OK);
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
        ToastHelper.showToast("提交失败");
        return true;
    }


    //begin region 上传头像
    private void pickPhoto() {
        if (bottomMenuDialog == null) {
            bottomMenuDialog = new BottomMenuDialog.Builder(IdentityActivity.this)
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
                String path = PathHelper.getRealFilePath(IdentityActivity.this,resultUri);
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
                                            facePhotoUrl = entity.getData().getUrl();
                                            Picasso.with(IdentityActivity.this).load(facePhotoUrl).placeholder(R.drawable.ic_selfie).into(imv_facephoto);
                                        }else{
                                            idPhotoUrl = entity.getData().getUrl();
                                            Picasso.with(IdentityActivity.this).load(idPhotoUrl).placeholder(R.drawable.ic_facephoto).into(imv_idcardphoto);
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
            Toast.makeText(IdentityActivity.this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(IdentityActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(IdentityActivity.this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
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
                .start(IdentityActivity.this);
    }

    //end region 上传头像

}
