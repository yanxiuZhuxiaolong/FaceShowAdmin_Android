package com.yanxiu.gphone.faceshowadmin_android.schedule;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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
import com.yanxiu.gphone.faceshowadmin_android.net.notice.UploadResResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.schedule.SchedulePublishRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.schedule.SchedulePublishResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.FileUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.permission.OnPermissionCallback;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布日程页面
 */
public class PublishScheduleActivity extends FaceShowBaseActivity {

    private PublicLoadLayout mRootView;

    private static final int REQUEST_CODE_ALBUM = 0x000;
    private static final int REQUEST_CODE_CAMERA = 0x001;
    private static final int REQUEST_CODE_CROP = 0x002;


    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_right_txt)
    TextView title_layout_right_txt;
    @BindView(R.id.title_layout_left_img)
    ImageView title_layout_left_img;

    @BindView(R.id.schedule_edit_title)
    EditText scheduleEditTitle;
    @BindView(R.id.schedule_pic_add)
    ImageView schedulePicAdd;
    @BindView(R.id.schedule_pic)
    ImageView schedulePic;
    @BindView(R.id.schedule_pic_del)
    ImageView schedulePicDel;

    private String mScheduleTitle;
    private String mAttachUrl;
    private ClassCircleDialog mClassCircleDialog;
    private String mCameraPath;
    private String mImagePaths;
    private String mCropPath;
    private String mResourceIds;
    private File portraitFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_schedule_publish);
        setContentView(mRootView);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleLayoutTitle.setText(R.string.publish_schedule);
        title_layout_right_txt.setText(R.string.notice_submit);
        title_layout_right_txt.setEnabled(false);
        title_layout_left_img.setImageResource(R.color.color_96bde4);
        titleLayoutTitle.setVisibility(View.VISIBLE);
        title_layout_right_txt.setVisibility(View.VISIBLE);
        title_layout_left_img.setVisibility(View.VISIBLE);
        scheduleEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                canPublish();
            }
        });
    }

    private void canPublish() {
        if (scheduleEditTitle.getText().toString().length() > 0 && !TextUtils.isEmpty(mImagePaths)) {
            title_layout_right_txt.setEnabled(true);
        } else {
            title_layout_right_txt.setEnabled(false);
        }
    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_txt, R.id.schedule_pic_add, R.id.schedule_pic_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                finish();
                break;
            case R.id.title_layout_right_txt:
                mScheduleTitle = scheduleEditTitle.getText().toString();
                submitSchedule();
                break;
            case R.id.schedule_pic_add:
                showDialog();
                break;
            case R.id.schedule_pic_del:
//                mResourceIds = null;
                mImagePaths = null;
                schedulePic.setVisibility(View.GONE);
                schedulePicDel.setVisibility(View.GONE);
                schedulePicAdd.setVisibility(View.VISIBLE);
                canPublish();
                break;
        }
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
                        imageUri = FileProvider.getUriForFile(PublishScheduleActivity.this, "com.yanxiu.gphone.faceshowadmin_android.fileprovider", file);
                    }
//                    startPhotoZoom(imageUri, Uri.fromFile(createCroppedImageFile()));
                    mImagePaths = mCameraPath;
//                    mType = TYPE_IMAGE;
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
                }
                break;
        }
        if (mImagePaths != null) {
            canPublish();
            schedulePic.setVisibility(View.VISIBLE);
            schedulePicDel.setVisibility(View.VISIBLE);
            schedulePicAdd.setVisibility(View.GONE);
            Glide.with(PublishScheduleActivity.this).load(mImagePaths).into(schedulePic);
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
                            ToastUtil.showToast(PublishScheduleActivity.this, R.string.no_storage_permissions);
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
                            ToastUtil.showToast(PublishScheduleActivity.this, R.string.no_camera_permissions);
                        }
                    });
                }
            });
        }
        mClassCircleDialog.show();
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri, Uri saveCroppedImageFileUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
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
        portraitFile = new File(mCropPath);
        try {
            portraitFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "裁剪文件异常", Toast.LENGTH_SHORT).show();
        }
        return portraitFile;
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
                    ToastUtil.showToast(PublishScheduleActivity.this, getString(R.string.send_class_circle_fail));
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
                ToastUtil.showToast(PublishScheduleActivity.this, error.getMessage());
            }
        });

    }

    private void submitSchedule() {
        mRootView.showLoadingView();
//        if (mType.equals(TYPE_IMAGE)) {
        uploadImg();
//        } else {
//            mResourceIds = null;
//            submitPostRequest();
//        }
    }

    private void submitPostRequest() {
        mRootView.showLoadingView();
        SchedulePublishRequest schedulePublishRequest = new SchedulePublishRequest();
        schedulePublishRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        schedulePublishRequest.subject = mScheduleTitle;
        schedulePublishRequest.imageUrl = mAttachUrl;
//        if (mType.equals(TYPE_IMAGE)) {
//            scheduleSaveRequest.url = mAttachUrl;
//        }
        schedulePublishRequest.startRequest(SchedulePublishResponse.class, new HttpCallback<SchedulePublishResponse>() {
            @Override
            public void onSuccess(RequestBase request, SchedulePublishResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    ToastUtil.showToast(PublishScheduleActivity.this, "发布成功");
                    ScheduleManageActivity.invoke(PublishScheduleActivity.this);
                    finish();
                } else {
                    ToastUtil.showToast(PublishScheduleActivity.this, getString(R.string.send_class_circle_fail));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                ToastUtil.showToast(PublishScheduleActivity.this, getString(R.string.send_class_circle_fail));
            }
        });
    }

    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, PublishScheduleActivity.class);
//        intent.putExtra(FROM, FROM_LEFTDRAWER);
        activity.startActivity(intent);
    }

}
