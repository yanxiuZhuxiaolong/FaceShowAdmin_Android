package com.yanxiu.gphone.faceshowadmin_android.notice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.dialog.ClassCircleDialog;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.base.UploadFileByHttp;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.GetResIdRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.GetResIdResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeRequestResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeSaveRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeSaveResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.UploadResResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdate;
import com.yanxiu.gphone.faceshowadmin_android.utils.FileUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.permission.OnPermissionCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lufengqing on 2017/11/2.
 */

public class NoticePostActivity extends FaceShowBaseActivity {
    private static final int REQUEST_CODE_ALBUM = 0x000;
    private static final int REQUEST_CODE_CAMERA = 0x001;
    private static final int REQUEST_CODE_CROP = 0x002;

    public static final String TYPE_TEXT = "text";
    public static final String TYPE_IMAGE = "image";
    private String mType = TYPE_TEXT;
    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_right_txt)
    TextView titleLayoutRightTxt;
    @BindView(R.id.notice_edit_title)
    EditText noticeEditTitle;
    @BindView(R.id.notice_edit_content)
    EditText noticeEditContent;
    @BindView(R.id.edit_word_num)
    TextView editWordNum;
    @BindView(R.id.notice_pic_add)
    ImageView noticePicAdd;
    @BindView(R.id.notice_pic)
    ImageView noticePic;
    @BindView(R.id.notice_pic_del)
    ImageView noticePicDel;
    private Context mContext;
    private PublicLoadLayout mRootView;
    private Unbinder unbinder;
    private boolean isPostSuccess;
    private String mNoticeTitle;
    private String mNoticeContent;
    private String mAttachUrl;
    private ClassCircleDialog mClassCircleDialog;
    private String mCameraPath;
    private String mImagePaths;
    private String mCropPath;
    private String mResourceIds;
    private NoticeSaveResponse.DataBean mReturnNoticeBean;
    private long mNoticeCreateTime;
    private File portraitFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mRootView = new PublicLoadLayout(mContext);
        mRootView.setContentView(R.layout.activity_notice_post_edit);
        setContentView(mRootView);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleLayoutTitle.setText(R.string.notice_post_title);
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutRightTxt.setVisibility(View.VISIBLE);
        titleLayoutRightTxt.setText(R.string.notice_submit);
        titleLayoutRightTxt.setEnabled(false);
        titleLayoutRightTxt.setTextColor(getResources().getColor(R.color.color_999999));
        InputFilter[] filters1 = {new InputFilter.LengthFilter(20)};
        noticeEditTitle.setFilters(filters1);
        InputFilter[] filters = {new InputFilter.LengthFilter(200)};
        noticeEditContent.setFilters(filters);
        noticeEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setTexCountTips(s.toString());
            }
        });
        noticeEditContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && KeyEvent.ACTION_DOWN == event.getAction())) {

                }
                return false;
            }
        });
    }

    private void setTexCountTips(String s) {
        if (s.trim().length() < 1 || noticeEditTitle.getText().toString().length() < 1) {
            titleLayoutRightTxt.setEnabled(false);
            titleLayoutRightTxt.setTextColor(getResources().getColor(R.color.color_999999));
            editWordNum.setTextColor(getResources().getColor(R.color.color_999999));
        } else {
            titleLayoutRightTxt.setEnabled(true);
            titleLayoutRightTxt.setTextColor(getResources().getColor(R.color.color_0068BD));
            editWordNum.setTextColor(getResources().getColor(R.color.color_0068BD));
        }
        editWordNum.setText(s.toString().length() + "");
    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_txt, R.id.notice_pic_add, R.id.notice_pic_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                finish();
                break;
            case R.id.title_layout_right_txt:

                mNoticeContent = noticeEditContent.getText().toString();
                mNoticeTitle = noticeEditTitle.getText().toString();
                submitNotice();
                break;
            case R.id.notice_pic_add:
                EventUpdate.onSendNotifyAddPicture(mContext);
                showDialog();
                break;
            case R.id.notice_pic_del:
                mType = TYPE_TEXT;
                mResourceIds = null;
                noticePic.setVisibility(View.GONE);
                noticePicDel.setVisibility(View.GONE);
                noticePicAdd.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showDialog() {
        if (mClassCircleDialog == null) {
            mClassCircleDialog = new ClassCircleDialog(this);
            mClassCircleDialog.setClickListener(new ClassCircleDialog.OnViewClickListener() {
                @Override
                public void onAlbumClick() {
                    FaceShowBaseActivity.requestWriteAndReadPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, REQUEST_CODE_ALBUM);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            ToastUtil.showToast(NoticePostActivity.this, R.string.no_storage_permissions);
                        }
                    });
                }

                @Override
                public void onCameraClick() {
                    FaceShowBaseActivity.requestCameraPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                            mCameraPath = FileUtils.getImageCatchPath(System.currentTimeMillis() + ".jpg");
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            File file = new File(mCameraPath);
                            if (file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Uri uri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(intent, REQUEST_CODE_CAMERA);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            ToastUtil.showToast(NoticePostActivity.this, R.string.no_camera_permissions);
                        }
                    });
                }
            });
        }
        mClassCircleDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ALBUM:
                if (data != null) {
                    Uri uri = data.getData();
//                    mCropPath=FileUtil.getImageCatchPath(System.currentTimeMillis()+".jpg");
//                    startCropImg(uri,mCropPath);
                    String path = FileUtils.getRealFilePath(this, uri);
                    mImagePaths = path;
                    mType = TYPE_IMAGE;
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (!TextUtils.isEmpty(mCameraPath)) {
//                    mCropPath=FileUtil.getImageCatchPath(System.currentTimeMillis()+".jpg");
//                    startCropImg(Uri.fromFile(new File(mCameraPath)),mCropPath);
                    File file = new File(mCameraPath);
                    Uri imageUri;
                    if (Build.VERSION.SDK_INT < 24) {
                        imageUri = Uri.fromFile(file);
                    } else {
                        imageUri = FileProvider.getUriForFile(NoticePostActivity.this, "com.yanxiu.gphone.faceshowadmin_android.fileprovider", file);
                    }
//                    startPhotoZoom(imageUri, Uri.fromFile(createCroppedImageFile()));
                    mImagePaths = mCameraPath;
                    mType = TYPE_IMAGE;
                }
                break;
            case REQUEST_CODE_CROP:
//                if (!TextUtils.isEmpty(mCropPath)) {
//                    if (new File(mCropPath).exists()) {
//                        mImagePaths = mCropPath;
//                        mType = TYPE_IMAGE;
//                    }
//                }
                if (portraitFile != null) {
                    mImagePaths = portraitFile.getAbsolutePath();
                    mType = TYPE_IMAGE;
                }
                break;
        }
        if (mImagePaths != null) {
            noticePic.setVisibility(View.VISIBLE);
            noticePicDel.setVisibility(View.VISIBLE);
            noticePicAdd.setVisibility(View.GONE);
            Glide.with(mContext).load(mImagePaths).into(noticePic);
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri, Uri saveCroppedImageFileUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
//        if(intent.resolveActivity(getPackageManager())==null){
//            ToastMaster.showToast("该手机不支持裁剪");
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveCroppedImageFileUri);
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    /**
     * 创建保存裁剪后图片的文件
     *
     * @return
     */
    private File createCroppedImageFile() {
        mCropPath = FileUtils.getImageCatchPath(System.currentTimeMillis() + ".jpg");

//        File dir = new File(Environment.getExternalStorageDirectory() + "/yanxiu/portrait/");
//        if (!dir.exists())
//            dir.mkdir();
        portraitFile = new File(mCropPath);
        try {
            portraitFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "裁剪文件异常", Toast.LENGTH_SHORT).show();
        }
        return portraitFile;
    }

    private void submitNotice() {
        mRootView.showLoadingView();
        if (mType.equals(TYPE_IMAGE)) {
            uploadImg();
        } else {
            mResourceIds = null;
            submitPostRequest();
        }
    }

    private void uploadImg() {
        File file = new File(mImagePaths);
        final Map<String, String> map = new HashMap<>();
        map.put("userId", UserInfo.getInstance().getInfo().getUserId() + "");
        map.put("name", file.getName());
        map.put("lastModifiedDate", String.valueOf(System.currentTimeMillis()));
        map.put("size", String.valueOf(FileUtils.getFileSize(file.getPath())));
        map.put("md5", FileUtils.MD5Helper(UserInfo.getInstance().getInfo().getUserId() +
                file.getName() + "jpg" + String.valueOf(System.currentTimeMillis())
                + String.valueOf(FileUtils.getFileSize(file.getPath()))));
        map.put("type", "image/jpg");
        map.put("chunkSize", String.valueOf(FileUtils.getFileSize(file.getPath())));

        UploadFileByHttp uploadFileByHttp = new UploadFileByHttp();
        try {
            uploadFileByHttp.uploadForm(map, "file", file, file.getName(), "http://newupload.yanxiu.com/fileUpload", new UploadFileByHttp.UpLoadFileByHttpCallBack() {
                @Override
                public void onSuccess(String responseStr) {
                    UploadResResponse resResponse = RequestBase.getGson().fromJson(responseStr, UploadResResponse.class);
                    GetResId(resResponse.name, resResponse.md5);
                }

                @Override
                public void onFail(String errorMessage) {
                    ToastUtil.showToast(mContext, getString(R.string.send_class_circle_fail));
                    mRootView.hiddenLoadingView();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void GetResId(final String fileName, final String md5) {
        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("client_type", "app");
        cookies.put("passport", SpManager.getPassport());
        GetResIdRequest getResIdRequest = new GetResIdRequest();
        getResIdRequest.filename = fileName;
        getResIdRequest.md5 = md5;
        getResIdRequest.cookies = cookies;
        GetResIdRequest.Reserve reserve = new GetResIdRequest.Reserve();
        reserve.title = fileName;
        getResIdRequest.reserve = RequestBase.getGson().toJson(reserve);
        getResIdRequest.startRequest(GetResIdResponse.class, new HttpCallback<GetResIdResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetResIdResponse ret) {
                mResourceIds = ret.result.resid;
                mAttachUrl = "http://upload.ugc.yanxiu.com/img/" + md5 + ".jpg?from=6&resId=" + mResourceIds;
                submitPostRequest();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                ToastUtil.showToast(mContext, error.getMessage());
            }
        });

    }


    private void submitPostRequest() {
        mRootView.showLoadingView();
        NoticeSaveRequest noticeSaveRequest = new NoticeSaveRequest();
        noticeSaveRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        noticeSaveRequest.title = mNoticeTitle;
        noticeSaveRequest.content = mNoticeContent.replace("/n", "//n");
        if (mType.equals(TYPE_IMAGE)) {
            noticeSaveRequest.url = mAttachUrl;
        }
        noticeSaveRequest.startRequest(NoticeSaveResponse.class, new HttpCallback<NoticeSaveResponse>() {
            @Override
            public void onSuccess(RequestBase request, NoticeSaveResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    ToastUtil.showToast(NoticePostActivity.this, "发布成功");
                    isPostSuccess = true;
                    mReturnNoticeBean = ret.getData();
                    mNoticeCreateTime = ret.getCurrentTime();
                    postFinish();
                } else {
                    ToastUtil.showToast(NoticePostActivity.this, ret.getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                ToastUtil.showToast(NoticePostActivity.this, error.getMessage());
            }
        });
    }

    private void postFinish() {
        NoticeRequestResponse.DataBean.NoticeInfosBean.ElementsBean bean = new NoticeRequestResponse.DataBean.NoticeInfosBean.ElementsBean();
        bean.setId(mReturnNoticeBean.getId());
        bean.setTitle(mReturnNoticeBean.getTitle());
        bean.setContent(mReturnNoticeBean.getContent());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        bean.setCreateTime(format.format(mNoticeCreateTime));
        bean.setNoticeReadNumSum(mReturnNoticeBean.getReadNum());
        bean.setAttachUrl(mAttachUrl);
        Intent intent = new Intent();
        intent.putExtra("isPostSuccess", isPostSuccess);
        intent.putExtra("noticeBean", bean);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, NoticePostActivity.class);
        context.startActivity(intent);
    }
}
