package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.dialog.ClassCircleDialog;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.dialog.ChooseSexDialog;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request.PersonalDetailsRequest;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response.PersonalDetailsResponse;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.util.UserMessageUpdateManager;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.CornersImageTarget;
import com.yanxiu.gphone.faceshowadmin_android.utils.FileUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.permission.OnPermissionCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.yanxiu.gphone.faceshowadmin_android.main.adressbook.util.UserMessageUpdateManager.UPDATA_HEADIMG;
import static com.yanxiu.gphone.faceshowadmin_android.main.adressbook.util.UserMessageUpdateManager.UPDATA_SEX;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 16:52.
 * Function :
 */
public class UserMessageActivity extends FaceShowBaseActivity implements View.OnClickListener, ChooseSexDialog.OnViewClickListener, UserMessageUpdateManager.onRequestCallback {

    public static final int REQUEST_NAME=1000;
    public static final String RESULT_NAME="result_name";
    public static final int REQUEST_SCHOOL=1001;
    public static final String RESULT_SCHOOL="result_school";

    private static final int REQUEST_CODE_ALBUM = 0x000;
    private static final int REQUEST_CODE_CAMERA = 0x001;
    private static final int REQUEST_CODE_CROP = 0x002;

    private Context mContext;
    private PublicLoadLayout rootView;
    private View mBackView;
    private TextView mTitleView;
    private ImageView mHeadImgView;
    private TextView mNameView;
    private TextView mMobileView;
    private TextView mSexView;
    private TextView mStageView;
    private TextView mSubjectView;
    private TextView mSchoolView;

    private View mNameClickView;
    private View mSexClickView;
    private View mSchoolClickView;

    private ClassCircleDialog mClassCircleDialog;
    private ChooseSexDialog mSexDialog;
    private UserMessageUpdateManager mUpdateManager;

    private String mCameraPath;
    private String mCropPath;

    private PersonalDetailsResponse.PersonalDetailsData mDetailsData;

    private UUID mDetailsRequest;

    public static void LuanchActivity(Context context) {
        Intent intent = new Intent(context, UserMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_user_message);
        setContentView(rootView);
        mUpdateManager = UserMessageUpdateManager.create();
        mSexDialog = new ChooseSexDialog(mContext);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUpdateManager.destory();
        if (mDetailsRequest != null) {
            RequestBase.cancelRequestWithUUID(mDetailsRequest);
            mDetailsRequest = null;
        }
    }

    private void initView() {
        mBackView = findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView = findViewById(R.id.title_layout_title);

        mHeadImgView = findViewById(R.id.iv_head_img);
        mNameView = findViewById(R.id.tv_name);
        mMobileView = findViewById(R.id.tv_mobile);
        mSexView = findViewById(R.id.tv_sex);
        mStageView = findViewById(R.id.tv_stage);
        mSubjectView = findViewById(R.id.tv_subject);
        mSchoolView = findViewById(R.id.tv_school);

        mNameClickView=findViewById(R.id.ll_name_click);
        mSexClickView=findViewById(R.id.ll_sex_click);
        mSchoolClickView=findViewById(R.id.ll_school_click);
    }

    private void listener() {
        mBackView.setOnClickListener(this);
        rootView.setRetryButtonOnclickListener(this);
        mHeadImgView.setOnClickListener(this);
        mMobileView.setOnClickListener(this);
        mStageView.setOnClickListener(this);
        mSubjectView.setOnClickListener(this);
        mSexDialog.setClickListener(this);
        mNameClickView.setOnClickListener(this);
        mSchoolClickView.setOnClickListener(this);
        mSexClickView.setOnClickListener(this);
    }

    private void initData() {
        mTitleView.setText("我的资料");
        startUserMessageRequest();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.retry_button:
                startUserMessageRequest();
                break;
            case R.id.iv_head_img:
                showDialog();
                break;
            case R.id.ll_name_click:
                EditNameActivity.LuanchActivity(mContext,REQUEST_NAME,mDetailsData.realName);
                break;
            case R.id.ll_sex_click:
                if (mSexDialog == null) {
                    mSexDialog = new ChooseSexDialog(mContext);
                }
                mSexDialog.show();
                break;
            case R.id.ll_school_click:
                EditSchoolActivity.LuanchActivity(mContext,REQUEST_SCHOOL,mDetailsData.school);
                break;

                /*
                 * 以下暂时未开放
                 * **/
            case R.id.tv_mobile:
                break;
            case R.id.tv_stage:
                break;
            case R.id.tv_subject:
                break;
        }
    }

    private void showDialog() {
        if (mClassCircleDialog == null) {
            mClassCircleDialog = new ClassCircleDialog(mContext);
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
                            ToastUtil.showToast(mContext, R.string.no_storage_permissions);
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
                            ToastUtil.showToast(mContext, R.string.no_camera_permissions);
                        }
                    });
                }
            });
        }
        mClassCircleDialog.show();
    }

    private void startUserMessageRequest() {
        rootView.showLoadingView();
        rootView.hiddenNetErrorView();
        PersonalDetailsRequest detailsRequest = new PersonalDetailsRequest();
        detailsRequest.userId = String.valueOf(UserInfo.getInstance().getInfo().getUserId());
        mDetailsRequest = detailsRequest.startRequest(PersonalDetailsResponse.class, new HttpCallback<PersonalDetailsResponse>() {
            @Override
            public void onSuccess(RequestBase request, PersonalDetailsResponse ret) {
                rootView.hiddenLoadingView();
                if (ret != null && ret.data != null && ret.getCode() == 0) {
                    mDetailsData = ret.data;
                    setUserMessage(ret.data);
                }else {
                    rootView.showNetErrorView();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                rootView.showNetErrorView();
                ToastUtil.showToast(mContext, error.getMessage());
            }
        });
    }

    private void setUserMessage(PersonalDetailsResponse.PersonalDetailsData message) {
        mUpdateManager.initData(message.realName, String.valueOf(message.sex), message.school, message.avatar);
        Glide.with(mContext).load(message.avatar).asBitmap().placeholder(R.drawable.classcircle_headimg_small).centerCrop().into(new CornersImageTarget(mContext, mHeadImgView, 10));
        mMobileView.setText(message.mobilePhone);
        mNameView.setText(message.realName);
        mSchoolView.setText(message.school);
        mStageView.setText(message.stageName);
        mSubjectView.setText(message.subjectName);
        String sex;
        switch (message.sex) {
            case 0:
                sex = "女";
                break;
            case 1:
                sex = "男";
                break;
            default:
                sex = "未知";
                break;
        }
        mSexView.setText(sex);
    }

    @Override
    public void onChoosekMan() {
        mUpdateManager.updataSex("1", this);
    }

    @Override
    public void onChooseWoman() {
        mUpdateManager.updataSex("0", this);
    }

    @Override
    public void onHttpStart() {
        rootView.showLoadingView();
    }

    @Override
    public void onSuccess(FaceShowBaseResponse response, String updateMessage, String updataType) {
        if (response != null) {
            if (response.getCode() == 0) {
                ToastUtil.showToast(mContext, "保存成功");
                switch (updataType) {
                    case UPDATA_HEADIMG:
                        mDetailsData.avatar = updateMessage;
                        UserInfo.getInstance().getInfo().setAvatar(updateMessage);
                        SpManager.saveUserInfo(UserInfo.getInstance().getInfo());
                        break;
                    case UPDATA_SEX:
                        mDetailsData.sex = Integer.parseInt(updateMessage);
                        UserInfo.getInstance().getInfo().setSex(mDetailsData.sex);
                        String sex;
                        switch (mDetailsData.sex) {
                            case 0:
                                sex = "女";
                                break;
                            case 1:
                                sex = "男";
                                break;
                            default:
                                sex = "未知";
                                break;
                        }
                        UserInfo.getInstance().getInfo().setSexName(sex);
                        SpManager.saveUserInfo(UserInfo.getInstance().getInfo());
                        break;
                }
                setUserMessage(mDetailsData);
            } else {
                ToastUtil.showToast(mContext, response.getMessage());
            }
        }
    }

    @Override
    public void onError(Error error) {
        ToastUtil.showToast(mContext, error.getMessage());
    }

    @Override
    public void onHttpFinish() {
        rootView.hiddenLoadingView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case REQUEST_NAME:
                    if (data!=null) {
                        mDetailsData.realName = data.getStringExtra(RESULT_NAME);
                        setUserMessage(mDetailsData);
                    }
                    break;
                case REQUEST_SCHOOL:
                    if (data!=null) {
                        mDetailsData.school = data.getStringExtra(RESULT_SCHOOL);
                        setUserMessage(mDetailsData);
                    }
                    break;
                case REQUEST_CODE_ALBUM:
                    if (data != null) {
                        Uri uri = data.getData();
                        mCropPath=FileUtils.getImageCatchPath(System.currentTimeMillis()+".jpg");
                        startPhotoZoom(uri,Uri.fromFile(new File(mCropPath)));
                    }
                    break;
                case REQUEST_CODE_CAMERA:
                    if (!TextUtils.isEmpty(mCameraPath)) {
                        mCropPath=FileUtils.getImageCatchPath(System.currentTimeMillis()+".jpg");
                        try {
                            new FileInputStream(new File(mCameraPath));
                            Uri imageUri;
                            if (Build.VERSION.SDK_INT < 24) {
                                imageUri = Uri.fromFile(new File(mCameraPath));
                            } else {
                                imageUri = FileProvider.getUriForFile(UserMessageActivity.this, "com.yanxiu.gphone.faceshowadmin_android.fileprovider", new File(mCameraPath));
                            }
                            startPhotoZoom(imageUri,Uri.fromFile(new File(mCropPath)));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_CROP:
                    if (!TextUtils.isEmpty(mCropPath)) {
                        if (new File(mCropPath).exists()) {
                            mUpdateManager.updataHeadimg(mCropPath,this);
                        }
                    }
                    break;
            }
    }

    public void startPhotoZoom(Uri uri, Uri saveCroppedImageFileUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveCroppedImageFileUri);
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }

}
