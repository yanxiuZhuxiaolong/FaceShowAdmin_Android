package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.test.yanxiu.faceshow_ui_base.imagePicker.GlideImageLoader;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.common.activity.PhotoActivity;
import com.yanxiu.gphone.faceshowadmin_android.common.bean.PhotoDeleteBean;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.request.UpLoadRequest;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.adapter.SelectedImageListAdapter;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.dialog.ClassCircleDialog;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.GetQiNiuTokenRequest;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.SendClassCircleRequest;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.GetQiNiuTokenResponse;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.RefreshClassCircle;
import com.yanxiu.gphone.faceshowadmin_android.utils.FileUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.Logger;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.permission.OnPermissionCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


/**
 * @author frc
 *         Time : 2018/1/12
 *         Function :
 */
public class SendClassCircleActivity extends FaceShowBaseActivity implements View.OnClickListener, TextWatcher {


    private static final int IMAGE_PICKER = 0x03;
    private static final int REQUEST_CODE_SELECT = 0x04;
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_IMAGE = "image";
    private static final String KEY_TYPE = "key_type";
    private static final String KEY_IMAGE = "key_image";

    private Context mContext;
    private PublicLoadLayout rootView;
    private ArrayList<String> mImagePaths;
    private EditText mContentView;
    private TextView mTitleView;
    private TextView mFunctionView;
    private TextView mBackView;
    private String mResourceIds = "";
    private InputMethodManager imm;
    private ClassCircleDialog mClassCircleDialog;
    private PopupWindow mCancelPopupWindow;
    private ImageItem mAddPicItem;

    private UUID mGetQiNiuTokenUUID;
    private UUID mSendClassCircleDataUUID;

    private SelectedImageListAdapter mSelectedImageListAdapter;
    private List<ImageItem> mSelectedImageList;
    /**
     * 是否可以发布
     */
    private boolean mCanPublish;
    private ImagePicker imagePicker;
    /**
     * 此参数设置为true时 则正在执行的七牛上传图片将被停止
     */
    private boolean mCancelQiNiuUploadPics = false;

    public static void LuanchActivity(Context context, String type, ArrayList<String> imgPaths) {
        Intent intent = new Intent(context, SendClassCircleActivity.class);
        intent.putExtra(KEY_TYPE, type);
        if (imgPaths != null && imgPaths.size() > 0) {
            intent.putStringArrayListExtra(KEY_IMAGE, imgPaths);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SendClassCircleActivity.this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_send_classcircle);
        setContentView(rootView);
        EventBus.getDefault().register(mContext);
        initView();
        listener();
        initData();
        setImagePicker();
    }


    private void setImagePicker() {
        GlideImageLoader glideImageLoader = new GlideImageLoader();
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(glideImageLoader);
        //显示拍照按钮
        imagePicker.setShowCamera(true);
        //允许裁剪（单选才有效）
        imagePicker.setCrop(false);
        //选中数量限制
        imagePicker.setSelectLimit(9);
        //裁剪框的形状
    }


    private void initView() {
        mBackView = (TextView) findViewById(R.id.title_layout_left_txt);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView = (TextView) findViewById(R.id.title_layout_title);
        mFunctionView = (TextView) findViewById(R.id.title_layout_right_txt);
        mFunctionView.setVisibility(View.VISIBLE);
        mContentView = (EditText) findViewById(R.id.et_content);
        initRecyclerView();

    }

    private void listener() {
        mBackView.setOnClickListener(this);
        mFunctionView.setOnClickListener(this);
        mContentView.addTextChangedListener(this);
    }

    private void initData() {
        mTitleView.setText(R.string.classcircle);
        mBackView.setText(R.string.cancle);
        mFunctionView.setText(R.string.send);
        mFunctionView.setEnabled(false);
        mFunctionView.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));
        mContentView.setText("");

        //生成添加图片item的数据
        mAddPicItem = new ImageItem();
        mAddPicItem.path = String.valueOf(ContextCompat.getDrawable(this, R.drawable.class_circle_add_picture));
        mAddPicItem.name = "添加图片";
    }

    private void initRecyclerView() {
        RecyclerView imageSelectedRecyclerView = (RecyclerView) findViewById(R.id.selected_images_recycler_view);
        if (mSelectedImageList == null) {
            mSelectedImageList = new ArrayList<>();
        }
        mSelectedImageList.clear();
        mSelectedImageList.add(mAddPicItem);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        imageSelectedRecyclerView.setLayoutManager(gridLayoutManager);
        mSelectedImageListAdapter = new SelectedImageListAdapter((ArrayList<ImageItem>) mSelectedImageList);
        imageSelectedRecyclerView.setAdapter(mSelectedImageListAdapter);
        mSelectedImageListAdapter.addPicClickListener(new SelectedImageListAdapter.PicClickListener() {
            @Override
            public void addPic() {
                if (mImagePaths != null && mImagePaths.size() >= 9) {
                    ToastUtil.showToast(getApplicationContext(), "一次最多上传9张图片");
                } else {
                    imagePicker.setSelectLimit(9 - (mImagePaths != null ? mImagePaths.size() : 0));
                    showDialog();
                }
            }

            @Override
            public void showBigPic(int position) {
                PhotoActivity.LaunchActivity(mContext, mImagePaths, position, mContext.hashCode(), PhotoActivity.DELETE_CAN);
            }
        });
        mSelectedImageListAdapter.setDataRemoveListener(new SelectedImageListAdapter.DataRemoveListener() {
            @Override
            public void remove(int pos) {
                mSelectedImageListAdapter.notifyItemRemoved(pos);
                mImagePaths.remove(pos);
                if (pos != mSelectedImageList.size()) {
                    mSelectedImageListAdapter.notifyItemRangeChanged(pos, mSelectedImageList.size() - pos);
                }
                if (mImagePaths.size() == 0 && TextUtils.isEmpty(mContentView.getText().toString())) {
                    openPublishButton(false);
                } else {
                    openPublishButton(true);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_left_txt:
                onBackPressed();
                break;
            case R.id.title_layout_right_txt:
                String content = mContentView.getText().toString();
                if (mImagePaths != null && mImagePaths.size() > 0) {
                    rootView.showLoadingView();
                    getQiNiuToken();
                } else {
                    if (TextUtils.isEmpty(content.trim())) {
                        ToastUtil.showToast(getApplicationContext(), "请输入要发布的内容");
                    } else {
                        rootView.showLoadingView();
                        uploadData(content, mResourceIds);
                    }
                }
                break;
            default:
                break;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mTitleView.getWindowToken(), 0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpLoadRequest.getInstense().cancle();
        EventBus.getDefault().unregister(mContext);
        if (mGetQiNiuTokenUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetQiNiuTokenUUID);
            mGetQiNiuTokenUUID = null;
        }
        if (mSendClassCircleDataUUID != null) {
            RequestBase.cancelRequestWithUUID(mSendClassCircleDataUUID);
            mSendClassCircleDataUUID = null;
        }
        if (mCancelQiNiuUploadPics = false) {
            mCancelQiNiuUploadPics = true;
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
                            Intent intent = new Intent(SendClassCircleActivity.this, ImageGridActivity.class);
                            startActivityForResult(intent, IMAGE_PICKER);
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

                            Intent intent = new Intent(SendClassCircleActivity.this, ImageGridActivity.class);
                            // 是否是直接打开相机
                            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true);
                            startActivityForResult(intent, REQUEST_CODE_SELECT);
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

    /**
     * 删除图片回调
     *
     * @param bean data
     */
    public void onEventMainThread(PhotoDeleteBean bean) {

        if (bean != null && bean.formId == mContext.hashCode()) {
            mImagePaths.remove(bean.deleteId);
            mSelectedImageList.remove(bean.deleteId);
            mSelectedImageListAdapter.update(mSelectedImageList);
            if (mImagePaths.size() == 0 && TextUtils.isEmpty(mContentView.getText().toString())) {
                openPublishButton(false);
            } else {
                openPublishButton(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mCanPublish) {
            exitDialog();
        } else {
            this.finish();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_PICKER:
                createSelectedImagesList(data, false);
                break;
            case REQUEST_CODE_SELECT:
                createSelectedImagesList(data, true);
            default:
                break;
        }

    }


    /**
     * 开发发布按钮
     *
     * @param bool 是否开启
     */
    private void openPublishButton(boolean bool) {
        if (bool) {
            mFunctionView.setEnabled(true);
            mFunctionView.setTextColor(ContextCompat.getColor(mContext, R.color.color_1da1f2));
        } else {
            mFunctionView.setEnabled(false);
            mFunctionView.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));
        }
        mCanPublish = bool;
    }

    private void createSelectedImagesList(Intent data, boolean isReSize) {
        ArrayList<ImageItem> images = null;
        try {
            images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
        } catch (Exception e) {

        }
        if (images == null) {
            return;
        }

        if (mSelectedImageList != null && mSelectedImageList.size() > 0) {
            mSelectedImageList.remove(mSelectedImageList.size() - 1);
        }

        mSelectedImageList.addAll(images);
        mSelectedImageList.add(mAddPicItem);
        mSelectedImageListAdapter.update(mSelectedImageList);
        if (mImagePaths != null) {
            mImagePaths.clear();
        } else {
            mImagePaths = new ArrayList<>();
        }
        for (int i = 0; i < mSelectedImageList.size(); i++) {
            if (i < mSelectedImageList.size() - 1) {
                mImagePaths.add(mSelectedImageList.get(i).path);
            }
        }
        if (mImagePaths.size() == 0 && TextUtils.isEmpty(mContentView.getText().toString())) {
            openPublishButton(false);
        } else {
            openPublishButton(true);
        }

    }


    private void uploadData(String content, String resourceIds) {
        SendClassCircleRequest sendClassCircleRequest = new SendClassCircleRequest();
        sendClassCircleRequest.content = content;

        sendClassCircleRequest.resourceIds = resourceIds;
        mSendClassCircleDataUUID = sendClassCircleRequest.startRequest(ClassCircleResponse.class, new HttpCallback<ClassCircleResponse>() {
            @Override
            public void onSuccess(RequestBase request, ClassCircleResponse ret) {
                mSendClassCircleDataUUID = null;
                rootView.hiddenLoadingView();
                if (ret.data != null) {
                    ToastUtil.showToast(mContext, R.string.send_success);
                    EventBus.getDefault().post(new RefreshClassCircle());
                    setResult(RESULT_OK);
                    SendClassCircleActivity.this.finish();
                } else {
                    ToastUtil.showToast(mContext, ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                mSendClassCircleDataUUID = null;
                ToastUtil.showToast(mContext, error.getMessage());
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mImagePaths == null || mImagePaths.size() == 0) {
            if (TextUtils.isEmpty(s)) {
                openPublishButton(false);
            } else {
                openPublishButton(true);
            }
        } else {
            openPublishButton(true);
        }

    }

//    private boolean textIsNull(Editable s){
//        if (TextUtils.isEmpty(s)){
//            return false;
//        }
//        s.toString().contains("/n")
//    }


    private void exitDialog() {
        showNewDialog();
        if (imm == null) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(mBackView.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
//        showCancelPopupWindow(this);

    }

    private void showCancelPopupWindow(Activity context) {
        if (mCancelPopupWindow == null) {
            View pop = LayoutInflater.from(context).inflate(R.layout.pop_ask_cancel_layout, null);
            (pop.findViewById(R.id.tv_pop_sure)).setOnClickListener(popupWindowClickListener);
            (pop.findViewById(R.id.tv_cancel)).setOnClickListener(popupWindowClickListener);
            mCancelPopupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mCancelPopupWindow.setAnimationStyle(R.style.pop_anim);
            mCancelPopupWindow.setFocusable(true);
            mCancelPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        }
        mCancelPopupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private String mQiniuToken;
    private List<String> mReSizedPicPath = new ArrayList<>();

    /**
     * 获取七牛token
     */
    private void getQiNiuToken() {
        GetQiNiuTokenRequest getQiNiuTokenRequest = new GetQiNiuTokenRequest();
        getQiNiuTokenRequest.from = "100";
        getQiNiuTokenRequest.dtype = "app";
        mGetQiNiuTokenUUID = getQiNiuTokenRequest.startRequest(GetQiNiuTokenResponse.class, new HttpCallback<GetQiNiuTokenResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetQiNiuTokenResponse ret) {
                mGetQiNiuTokenUUID = null;
                mCancelQiNiuUploadPics = false;
                if (ret != null) {
                    if (ret.getCode() == 0) {
                        mQiniuToken = ret.getData().getToken();
                        mReSizedPicPath.clear();
                        /**
                         * 使用鲁班对图片进行压缩
                         */
                        Luban.with(SendClassCircleActivity.this)
                                .load(mImagePaths)
                                .ignoreBy(100)
                                .setCompressListener(new OnCompressListener() {
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        mReSizedPicPath.add(file.getPath());
                                        if (mReSizedPicPath.size() == mImagePaths.size()) {
                                            uploadPicListByQiNiu(mQiniuToken, mReSizedPicPath);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        rootView.hiddenLoadingView();
                                        ToastUtil.showToast(getApplicationContext(), "图片上传失败");
                                    }
                                }).launch();

                    } else {
                        rootView.hiddenLoadingView();
                        ToastUtil.showToast(getApplicationContext(), ret.getError() != null ? ret.getError().getMessage() : getString(R.string.get_qiniu_token_error));
                    }

                } else {
                    rootView.hiddenLoadingView();
                    ToastUtil.showToast(getApplicationContext(), getString(R.string.get_qiniu_token_error));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mGetQiNiuTokenUUID = null;
                rootView.hiddenLoadingView();
                ToastUtil.showToast(getApplicationContext(), error.getMessage());
            }
        });
    }

    private UploadManager uploadManager = null;
    private Configuration config = new Configuration.Builder()
            // 分片上传时，每片的大小。 默认256K
            .chunkSize(2 * 1024 * 1024)
            // 启用分片上传阀值。默认512K
            .putThreshhold(4 * 1024 * 1024)
            // 链接超时。默认10秒
            .connectTimeout(10)
            // 服务器响应超时。默认60秒
            .responseTimeout(60)
            .build();

    /**
     * 通过七牛云上传图片
     *
     * @param token 七牛上传证书
     */
    private void uploadPicListByQiNiu(String token, @NonNull List<String> fileList) {
        if (uploadManager == null) {
            uploadManager = new UploadManager(config);
        }
        if (fileList != null && fileList.size() > 0) {
//            Logger.d(fileList);
            uploadPicByQiNiu(fileList, -1, null, token);
        }
    }


    private void  uploadPicByQiNiu(final List<String> filePathList, int position, String key, final String token) {
        position++;
        /**
         * 本段代码  预防
         */
        if (position > 9) {
            mCancelQiNiuUploadPics = true;
            rootView.hiddenLoadingView();
            ToastUtil.showToast(getApplicationContext(), "一次最多发布9张图片");
            return;
        }
        if (filePathList.size() <= position) {
            uploadData(mContentView.getText().toString(), mResourceIds);
        } else {
            final int finalPosition = position;
            uploadManager.put(filePathList.get(finalPosition), null, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject res) {
                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                    if (info.isOK()) {
                        try {
                            mResourceIds = mResourceIds + (TextUtils.isEmpty(mResourceIds) ? "" : ",") + res.getString("key") + "|" + FileUtils.getFileSuffix(filePathList.get(finalPosition));
//                            Logger.d(mResourceIds);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        uploadPicByQiNiu(filePathList, finalPosition, null, token);

                    } else {
                        mResourceIds = "";
                        rootView.hiddenLoadingView();
                    }

                }
            }, new UploadOptions(null, null, false, new UpProgressHandler() {
                @Override
                public void progress(String s, double v) {
                }

            }, new UpCancellationSignal() {
                @Override
                public boolean isCancelled() {
                    return mCancelQiNiuUploadPics;
                }
            }));
        }
    }

    private void dismissPopupWindow() {
        if (mCancelPopupWindow != null) {
            mCancelPopupWindow.dismiss();
        }
    }

    private AlertDialog.Builder mBuilder = null;

    private void showNewDialog() {
        if (mBuilder==null){
            mBuilder =new AlertDialog.Builder(SendClassCircleActivity.this);
            mBuilder.setMessage("退出编辑?")
                    .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            SendClassCircleActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
        }
        mBuilder.show();
    }

    View.OnClickListener popupWindowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_pop_sure:
                    dismissPopupWindow();
                    SendClassCircleActivity.this.finish();
                    break;
                case R.id.tv_cancel:
                    dismissPopupWindow();
                    break;
                default:
                    break;

            }

        }
    };


}
