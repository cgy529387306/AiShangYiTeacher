package com.weima.aishangyi.jiaoshi.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mb.android.utils.AppHelper;
import com.mb.android.utils.DialogHelper;
import com.mb.android.utils.Helper;
import com.mb.android.utils.JsonHelper;
import com.mb.android.utils.ProgressDialogHelper;
import com.mb.android.utils.ToastHelper;
import com.squareup.picasso.Picasso;
import com.weima.aishangyi.jiaoshi.R;
import com.weima.aishangyi.jiaoshi.adapter.PicAdapter;
import com.weima.aishangyi.jiaoshi.base.BaseActivity;
import com.weima.aishangyi.jiaoshi.constants.ProjectConstants;
import com.weima.aishangyi.jiaoshi.entity.ActivityDetailResp;
import com.weima.aishangyi.jiaoshi.entity.CommonEntity;
import com.weima.aishangyi.jiaoshi.entity.CurrentUser;
import com.weima.aishangyi.jiaoshi.entity.LessonBean;
import com.weima.aishangyi.jiaoshi.entity.StarPriceResp;
import com.weima.aishangyi.jiaoshi.entity.UploadResp;
import com.weima.aishangyi.jiaoshi.utils.ImageFactory;
import com.weima.aishangyi.jiaoshi.utils.InputFilterMax;
import com.weima.aishangyi.jiaoshi.utils.NavigationHelper;
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
 * 编辑课程
 */
public class EditClassActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_DATA_WAY = 0xf1;
    public static final int REQUEST_CODE_CAMERA = 0xf3;
    public static final int REQUEST_CODE_ALUBM = 0xf4;
    private ImageView imv_recover;
    private EditText edit_name,eidt_brief,edit_detail,edit_count;
    private TextView txv_one,txv_together;
    private EditText mInputView;
    private GridView mPicGridView;
    private List<String> pathList = new ArrayList<String>();
    private File mTmpFile = null;
    private ImgDel imgDel = null;
    private PicAdapter mAdaptar = null;
    private BottomMenuDialog picPhotoDialog;
    private BottomMenuDialog bottomMenuDialog;
    private String mTempFilePath = AppHelper.getBaseCachePath()
            .concat(String.valueOf(System.currentTimeMillis())).concat(".jpg");
    private long type = 1;//1:一对一  2:拼课
    private String recoverUrl="";
    private long classType = -1;
    private LessonBean lessonBean;
    private int number = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTitle("编辑课程");
        setContentView(R.layout.activity_edit_class);
        initUI();
        initData();
    }

    private void initUI() {
        classType = getIntent().getLongExtra(ProjectConstants.BundleExtra.KEY_CLASS_TYPE_ID, -1);
        edit_name = findView(R.id.edit_name);
        eidt_brief = findView(R.id.eidt_brief);
        edit_detail = findView(R.id.edit_detail);
        edit_count = findView(R.id.edit_count);
        txv_one = findView(R.id.txv_one);
        txv_together = findView(R.id.txv_together);
        imv_recover = findView(R.id.imv_recover);
        mInputView = findView(R.id.add_text_input);
        mPicGridView = findView(R.id.add_pic_grid);
        imgDel = new ImgDel();
        pathList.add(R.drawable.ic_pic_add6 + "");
        mAdaptar = new PicAdapter(EditClassActivity.this, pathList, imgDel);
        mPicGridView.setAdapter(mAdaptar);
        txv_one.setOnClickListener(this);
        txv_together.setOnClickListener(this);
        imv_recover.setOnClickListener(this);
        findView(R.id.btn_save).setOnClickListener(this);
        findView(R.id.btnReduce).setOnClickListener(this);
        findView(R.id.btnAdd).setOnClickListener(this);
        edit_count.setSelection(ProjectHelper.getCommonSeletion(edit_count.getText().toString()));
        edit_count.setFilters(new InputFilter[]{ new InputFilterMax("1", Integer.MAX_VALUE+"")});
        edit_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Helper.isNotEmpty(edit_count.getText().toString())){
                    number = Integer.parseInt(edit_count.getText().toString());
                }
            }
        });
    }

    private void initData() {
        lessonBean = (LessonBean) getIntent().getSerializableExtra(ProjectConstants.BundleExtra.KEY_CLASS);
        if (lessonBean != null){
            if (Helper.isNotEmpty(lessonBean.getIcon())){
                recoverUrl = lessonBean.getIcon();
                Picasso.with(EditClassActivity.this).load(recoverUrl).placeholder(R.drawable.ic_addcover).into(imv_recover);
            }
            edit_name.setText(ProjectHelper.getCommonText(lessonBean.getName()));
            edit_name.setSelection(ProjectHelper.getCommonSeletion(lessonBean.getName()));
            eidt_brief.setText(ProjectHelper.getCommonText(lessonBean.getLesson_brief()));
            eidt_brief.setSelection(ProjectHelper.getCommonSeletion(lessonBean.getLesson_brief()));
            edit_detail.setText(ProjectHelper.getCommonText(lessonBean.getLesson_detail()));
            edit_detail.setSelection(ProjectHelper.getCommonSeletion(lessonBean.getLesson_detail()));
            classType = lessonBean.getItem();
            type = lessonBean.getLesson_item();
            initClassType();
            if (Helper.isNotEmpty(lessonBean.getImages())){
                pathList.addAll(0, lessonBean.getImages());
                mAdaptar.updateList(pathList);
            }
        }
    }

    @Override
    public boolean onResponseSuccess(int gact, String response,
                                     Object... extras) {
        super.onResponseSuccess(gact, response, extras);
        int requestType = extras.length != 0 ? (Integer) extras[0] : 0;
        switch (requestType){
            case 1:
                //保存
                ProgressDialogHelper.dismissProgressDialog();
                CommonEntity entity = JsonHelper.fromJson(response,CommonEntity.class);
                if ("200".equals(entity.getCode())){
                    ToastHelper.showToast("保存成功");
                    LocalBroadcastManager.getInstance(EditClassActivity.this).sendBroadcast(new Intent(ProjectConstants.BroadCastAction.UPDATE_LESSON_LIST));
                    finish();
                }else{
                    ToastHelper.showToast(entity.getMessage());
                }
                break;
            default:
                break;
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
     * 图片选择
     */
    private void selectImage() {
        try {
            Intent intent = new Intent(EditClassActivity.this, MultiImageSelectorActivity.class);
            // 是否显示拍摄图片
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
            // 最大可选择图片数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 7 - pathList.size());
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
        ProgressDialogHelper.showProgressDialog(EditClassActivity.this, "上传中...");
        upload(ProjectConstants.Url.FILE_UPLOAD, "file", path, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                ProgressDialogHelper.dismissProgressDialog();
                if (responseBody != null) {
                    String response = new String(responseBody);
                    if (Helper.isNotEmpty(response)) {
                        UploadResp entity = JsonHelper.fromJson(response, UploadResp.class);
                        if (Helper.isNotEmpty(entity) && "200".equals(entity.getCode())) {
                            if (Helper.isNotEmpty(entity.getData()) && Helper.isNotEmpty(entity.getData().getUrl())) {
                                pathList.add(0, entity.getData().getUrl());
                                mAdaptar.updateList(pathList);
                            }
                        } else {
                            Log.e("=====upload===", "上次失败,path:" + path);
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                ProgressDialogHelper.dismissProgressDialog();
                Log.e("=====upload===", "上次失败,path:" + path);
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
        if (result != Activity.RESULT_OK) {
            return;
        }
        if (request == REQUEST_CODE_CAMERA) {
            handleImageCamera(data);
        } else if (request == REQUEST_CODE_ALUBM) {
            handleImageSelect(data);
        }else if (request == 1){
            // 相册
            cropPickedImage(data.getData());
        }else if (request == 2){
            // 拍照
            File file = new File(mTempFilePath);
            if (!file.exists()) {
                return;
            }
            cropPickedImage(Uri.parse("file://".concat(mTempFilePath)));
        }else if (request == 3){
            upload(ProjectConstants.Url.FILE_UPLOAD, "file", mTempFilePath, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    if (responseBody != null) {
                        String response = new String(responseBody);
                        if (Helper.isNotEmpty(response)) {
                            UploadResp entity = JsonHelper.fromJson(response, UploadResp.class);
                            if (Helper.isNotEmpty(entity) && "200".equals(entity.getCode())) {
                                if (Helper.isNotEmpty(entity.getData()) && Helper.isNotEmpty(entity.getData().getUrl())) {
                                    recoverUrl = entity.getData().getUrl();
                                    Picasso.with(EditClassActivity.this).load(recoverUrl).placeholder(R.drawable.ic_addcover).into(imv_recover);
                                }
                            } else {
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
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txv_one){
            type = 1;
            initClassType();
        }else if (id == R.id.txv_together){
            type = 2;
            initClassType();
        }else if (id == R.id.imv_recover){
            pickPhoto();
        }else if (id == R.id.btn_save){
            doSaveInfo();
        }else if (id ==R.id.btnReduce){
            if (number>1) {
                number--;
                edit_count.setText(number+"");
                edit_count.setSelection(ProjectHelper.getCommonSeletion(edit_count.getText().toString()));
            }
        }else  if (id ==R.id.btnAdd){
            if (number<Integer.MAX_VALUE){
                number++;
                edit_count.setText(number+"");
                edit_count.setSelection(ProjectHelper.getCommonSeletion(edit_count.getText().toString()));
            }else{
                ToastHelper.showToast("超过人数限制");
            }
        }
    }

    private void initClassType(){
        if (type == 1){
            txv_one.setTextColor(getResources().getColor(R.color.white));
            txv_one.setBackgroundResource(R.drawable.shape_rect_orange);
            txv_together.setTextColor(getResources().getColor(R.color.base_gray));
            txv_together.setBackgroundResource(R.drawable.shape_rect_white);
        }else{
            txv_one.setTextColor(getResources().getColor(R.color.base_gray));
            txv_one.setBackgroundResource(R.drawable.shape_rect_white);
            txv_together.setTextColor(getResources().getColor(R.color.white));
            txv_together.setBackgroundResource(R.drawable.shape_rect_orange);
        }
    }

    private void doSaveInfo(){
        String name = edit_name.getText().toString();
        String brief = eidt_brief.getText().toString();
        String detail = edit_detail.getText().toString();
        if (Helper.isEmpty(name)){
            ToastHelper.showToast("请输入课程名称");
            return;
        }
        if (Helper.isEmpty(brief)){
            ToastHelper.showToast("请输入课程简介");
            return;
        }
        if (Helper.isEmpty(detail)){
            ToastHelper.showToast("请输入课程详情");
            return;
        }
        if (Helper.isEmpty(recoverUrl)){
            ToastHelper.showToast("请上传封面图片");
            return;
        }
        if (Helper.isEmpty(pathList) || pathList.size()<=1){
            ToastHelper.showToast("请选择图片");
            return;
        }

        ProgressDialogHelper.showProgressDialog(EditClassActivity.this, "提交中...");
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        if (lessonBean != null){
            requestMap.put("id",lessonBean.getId());
        }
        requestMap.put("icon",recoverUrl);
        requestMap.put("name", name);
        requestMap.put("lesson_item", type);
        requestMap.put("item", classType);
        requestMap.put("number", number);
        requestMap.put("lesson_brief", brief);
        requestMap.put("lesson_detail", detail);
        if (Helper.isNotEmpty(pathList) && pathList.size()>1){
            pathList.remove(pathList.size()-1);
            requestMap.put("images", pathList);
        }
        post(ProjectConstants.Url.ACCOUNT_SAVE_LESSON, requestMap,1);
    }

    private class ImgDel implements PicAdapter.DelImg {

        @Override
        public void delImg(int position) {
            pathList.remove(position);
            mAdaptar.updateList(pathList);
        }

        @Override
        public void addImg() {
            if (pathList.size() < 7) {
                bottomMenuDialog = new BottomMenuDialog.Builder(EditClassActivity.this)
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
                ToastHelper.showToast("最多只能选择6张图片");
            }
        }
    }


    //begin region 上传头像
    private void pickPhoto() {
        if (picPhotoDialog == null) {
            picPhotoDialog = new BottomMenuDialog.Builder(EditClassActivity.this)
                    .addMenu("拍照", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pickImageFromCamera();
                            picPhotoDialog.dismiss();
                        }
                    }).addMenu("相册", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pickImageFromGallery();
                            picPhotoDialog.dismiss();
                        }
                    }).create();
        }
        picPhotoDialog.show();
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
            intent.putExtra("aspectX", 3);
            intent.putExtra("aspectY",2);
            intent.putExtra("outputX", 600);
            intent.putExtra("outputY", 400);
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
