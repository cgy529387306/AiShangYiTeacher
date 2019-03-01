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
import android.widget.TextView;
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
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.entity.UploadResp;
import com.weima.aishangyi.jiaoshi.utils.ImageFactory;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
import com.weima.aishangyi.jiaoshi.widget.BottomMenuDialog;
import com.weima.aishangyi.jiaoshi.widget.CircleTransform;

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
 * 作者：cgy on 16/12/6 23:24
 * 邮箱：593960111@qq.com
 * 宣传简介
 */
public class CampaignActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_DATA_WAY = 0xf1;
    public static final int REQUEST_CODE_CAMERA = 0xf3;
    public static final int REQUEST_CODE_ALUBM = 0xf4;
    private EditText mInputView;
    private GridView mPicGridView;
    private List<String> pathList = new ArrayList<String>();
    private File mTmpFile = null;
    private ImgDel imgDel = null;
    private PicAdapter mAdaptar = null;
    private BottomMenuDialog bottomMenuDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("宣传简介");
        setRightButton("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveInfo();
            }
        });
        setContentView(R.layout.activity_campaign);
        initUI();
        initData();
    }

    private void initUI() {
        mInputView = findView(R.id.add_text_input);
        mPicGridView = findView(R.id.add_pic_grid);
        imgDel = new ImgDel();
        pathList.add(R.drawable.ic_pic_add + "");
        mAdaptar = new PicAdapter(CampaignActivity.this, pathList, imgDel);
        mPicGridView.setAdapter(mAdaptar);
    }

    private void initData() {
        CurrentUser currentUser = CurrentUser.getInstance();
        if (Helper.isNotEmpty(currentUser)){
            mInputView.setText(currentUser.getDetail()==null?"":currentUser.getDetail());
            mInputView.setSelection(currentUser.getDetail()==null?0:currentUser.getDetail().length());
        }
        if (Helper.isNotEmpty(currentUser.getAlbum())){
            pathList.addAll(0, currentUser.getAlbum());
            mAdaptar.updateList(pathList);
        }
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        ProgressDialogHelper.dismissProgressDialog();
        CommonEntity entity = JsonHelper.fromJson(response,CommonEntity.class);
        if ("200".equals(entity.getCode())){
            LocalBroadcastManager.getInstance(CampaignActivity.this).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_USER_INFO));
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

    private void doSaveInfo() {
        String detail = mInputView.getText().toString();
        if (Helper.isEmpty(detail)){
            ToastHelper.showToast("请输入宣传简介");
            return;
        }
        ProgressDialogHelper.showProgressDialog(CampaignActivity.this, "保存中...");
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("detail", detail);
        if (Helper.isNotEmpty(pathList) && pathList.size()>1){
            pathList.remove(pathList.size() - 1);
            requestMap.put("album", pathList);
        }
        post(ProjectConstants.Url.ACCOUNT_EDIT_INFO, requestMap);
    }

    /**
     * 图片选择
     */
    private void selectImage() {
        try {
            Intent intent = new Intent(CampaignActivity.this, MultiImageSelectorActivity.class);
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

    private void uploadPhoto(final String path){
        ProgressDialogHelper.showProgressDialog(CampaignActivity.this,"上传中...");
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
                bottomMenuDialog = new BottomMenuDialog.Builder(CampaignActivity.this)
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
