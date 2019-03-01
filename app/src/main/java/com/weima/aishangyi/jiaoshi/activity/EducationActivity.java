package com.weima.aishangyi.jiaoshi.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.squareup.picasso.Picasso;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.PicAdapter;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.CertifyInfoResp;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.entity.UploadResp;
import com.weima.aishangyi.jiaoshi.utils.ImageFactory;
import com.weima.aishangyi.jiaoshi.utils.ProjectHelper;
import com.weima.aishangyi.jiaoshi.widget.BottomMenuDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.nereo.multi_image_selector.utils.FileUtils;

/**
 * 学历认证
 */
public class EducationActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_DATA_WAY = 0xf1;
    public static final int REQUEST_CODE_CAMERA = 0xf3;
    public static final int REQUEST_CODE_ALUBM = 0xf4;
    private GridView mPicGridView;
    private List<String> pathList = new ArrayList<String>();
    private File mTmpFile = null;
    private ImgDel imgDel = null;
    private PicAdapter mAdaptar = null;
    private BottomMenuDialog bottomMenuDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("学历认证");
        setContentView(R.layout.activity_education);
        initUI();
        initData();
    }

    private void initUI() {
        mPicGridView = findView(R.id.add_pic_grid);
        imgDel = new ImgDel();
        pathList.add(R.drawable.ic_pic_add + "");
        mAdaptar = new PicAdapter(EducationActivity.this, pathList, imgDel);
        mPicGridView.setAdapter(mAdaptar);
        findView(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSaveInfo();
            }
        });
    }

    private void initData(){
        int type = getIntent().getIntExtra("type",0);
        if (type==1){
            CertifyInfoResp.CertifyInfo certifyInfo = (CertifyInfoResp.CertifyInfo) getIntent().getSerializableExtra(ProjectConstants.BundleExtra.KEY_CERTIFY_INFO);
            if (Helper.isNotEmpty(certifyInfo) && Helper.isNotEmpty(certifyInfo.getEducation())){
                pathList.addAll(0,certifyInfo.getEducation());
                mAdaptar.updateList(pathList);
            }
        }
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        ProgressDialogHelper.dismissProgressDialog();
        CommonEntity entity = JsonHelper.fromJson(response,CommonEntity.class);
        if ("200".equals(entity.getCode())){
            CurrentUser.getInstance().setEdCertification(1);
            LocalBroadcastManager.getInstance(EducationActivity.this).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_USER_INFO));
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

    private void doSaveInfo(){
        if (Helper.isNotEmpty(pathList) && pathList.size()>1){
            pathList.remove(pathList.size() - 1);
            ProgressDialogHelper.showProgressDialog(EducationActivity.this, "提交中...");
            HashMap<String, Object> requestMap = new HashMap<String, Object>();
            requestMap.put("education",pathList);
            post(ProjectConstants.Url.ACCOUNT_INFO_CERTIFY, requestMap);
        }else{
            ToastHelper.showToast("请上传学历学位证书！");
        }
    }

    /**
     * 图片选择
     */
    private void selectImage() {
        try {
            Intent intent = new Intent(EducationActivity.this, MultiImageSelectorActivity.class);
            // 是否显示拍摄图片
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
            // 最大可选择图片数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 10 - pathList.size());
            // 选择模式
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
            startActivityForResult(intent, REQUEST_CODE_ALUBM);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = FileUtils.createTmpFile(this);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
        } else {
            Toast.makeText(this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理图片选择结果
     *
     * @param data
     */
    private void handleImageSelect(Intent data) {
        if (null == data) {
            return;
        }
        List<String> selecImags = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
        if (null == selecImags || 0 == selecImags.size()) {
            return;
        }
        for (String path:selecImags) {
            uploadPhoto(path);
        }
    }

    private void uploadPhoto(final String path){
        ProgressDialogHelper.showProgressDialog(EducationActivity.this,"上传中...");
        upload(ProjectConstants.Url.FILE_UPLOAD, "file", path, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                ProgressDialogHelper.dismissProgressDialog();
                if (responseBody != null){
                    String response = new String(responseBody);
                    if (Helper.isNotEmpty(response)){
                        UploadResp entity = JsonHelper.fromJson(response, UploadResp.class);
                        if (Helper.isNotEmpty(entity) && "200".equals(entity.getCode())){
                            if (Helper.isNotEmpty(entity.getData()) && Helper.isNotEmpty(entity.getData().getUrl())){
                                pathList.add(0, entity.getData().getUrl());
                                mAdaptar.updateList(pathList);
                            }
                        }else{
                            Log.e("=====upload===","上次失败,path:"+path);
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                ProgressDialogHelper.dismissProgressDialog();
                Log.e("=====upload===","上次失败,path:"+path);
            }
        });
    }

    private void handleImageCamera(Intent data) {
        try {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Uri uri = data.getData();
                    File pic = new File(new URI(uri.toString()));
                    uploadPhoto(pic.getPath());
                }
            } else {
                Bitmap bm = ImageFactory.getImageThumbnail(mTmpFile.getPath(), 1024 * 1024);
                if (Helper.isNotEmpty(bm)){
                    try {
                        FileOutputStream fos = new FileOutputStream(new File(mTmpFile.getPath()));
                        fos.write(ImageFactory.bitmapToBytes(bm, Bitmap.CompressFormat.JPEG));
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uploadPhoto(mTmpFile.getPath());
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (request == REQUEST_CODE_CAMERA) {
            handleImageCamera(data);
        } else if (request == REQUEST_CODE_ALUBM) {
            handleImageSelect(data);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    private class ImgDel implements PicAdapter.DelImg {

        @Override
        public void delImg(int position) {
            pathList.remove(position);
            mAdaptar.updateList(pathList);
        }

        @Override
        public void addImg() {
            if (pathList.size() < 10) {
                bottomMenuDialog = new BottomMenuDialog.Builder(EducationActivity.this)
                        .addMenu("拍照", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showCameraAction();
                                bottomMenuDialog.dismiss();
                            }
                        }).addMenu("相册", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectImage();
                                bottomMenuDialog.dismiss();
                            }
                        }).create();
                bottomMenuDialog.show();
            } else {
                ToastHelper.showToast("最多只能选择9张图片");
            }
        }
    }
}
