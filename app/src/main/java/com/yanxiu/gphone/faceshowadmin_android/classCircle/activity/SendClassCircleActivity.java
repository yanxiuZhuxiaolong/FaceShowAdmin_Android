package com.yanxiu.gphone.faceshowadmin_android.classCircle.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.request.GetResIdRequest;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.request.SendClassCircleRequest;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.GetResIdResponse;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.RefreshClassCircle;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.UploadResResponse;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.base.UploadFileByHttp;
import com.yanxiu.gphone.faceshowadmin_android.utils.FileUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/19 12:08.
 * Function :
 */
public class SendClassCircleActivity extends FaceShowBaseActivity implements View.OnClickListener, TextWatcher {

    public static final String TYPE_TEXT="text";
    public static final String TYPE_IMAGE="image";

    private static final String KEY_TYPE="key_type";
    private static final String KEY_IMAGE="key_image";

    private Context mContext;
    private PublicLoadLayout rootView;
    private String mType;
    private ArrayList<String> mImagePaths;
    private EditText mContentView;
    private ImageView mPictureView;
    private TextView mTitleView;
    private TextView mFunctionView;
    private TextView mBackView;
    private String mResourceIds="";

    private UUID mSendDataRequest;

    public static void LuanchActivity(Context context, String type, ArrayList<String> imgPaths){
        Intent intent=new Intent(context,SendClassCircleActivity.class);
        intent.putExtra(KEY_TYPE,type);
        if (imgPaths!=null&&imgPaths.size()>0){
            intent.putStringArrayListExtra(KEY_IMAGE,imgPaths);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=SendClassCircleActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_send_classcircle);
        setContentView(rootView);
        mType=getIntent().getStringExtra(KEY_TYPE);
        if (mType.equals(TYPE_IMAGE)){
            mImagePaths=getIntent().getStringArrayListExtra(KEY_IMAGE);
        }
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendDataRequest!=null){
            RequestBase.cancelRequestWithUUID(mSendDataRequest);
            mSendDataRequest=null;
        }
     }

    private void initView() {
        mBackView = findViewById(R.id.title_layout_left_txt);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView = findViewById(R.id.title_layout_title);
        mFunctionView = findViewById(R.id.title_layout_right_txt);
        mFunctionView.setVisibility(View.VISIBLE);

        mContentView= findViewById(R.id.et_content);
        mPictureView= findViewById(R.id.iv_picture);
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
        mFunctionView.setTextColor(ContextCompat.getColor(mContext,R.color.color_0068BD));

        if (mType.equals(TYPE_TEXT)){
            mPictureView.setVisibility(View.GONE);
        }else {
            mPictureView.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mImagePaths.get(0)).into(mPictureView);
        }
        mContentView.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_layout_left_txt:
                this.finish();
                break;
            case R.id.title_layout_right_txt:
                String content=mContentView.getText().toString();
                rootView.showLoadingView();
                if (mType.equals(TYPE_IMAGE)){
                    uploadImg(content);
                }else {
                    uploadData(content,mResourceIds);
                }
                break;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTitleView.getWindowToken(), 0);
    }

    private void uploadImg(final String content){
        File file=new File(mImagePaths.get(0));
        final Map<String, String> map = new HashMap<>();
        map.put("userId", UserInfo.getInstance().getInfo().getUserId()+"");
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
                    GetResId(resResponse.name, resResponse.md5,content);
                }

                @Override
                public void onFail(String errorMessage) {
                    ToastUtil.showToast(mContext,getString(R.string.send_class_circle_fail));
                    rootView.hiddenLoadingView();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void GetResId(String fileName, String md5, final String content) {
        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("client_type", "app");
        cookies.put("passport", SpManager.getPassport());
        GetResIdRequest getResIdRequest = new GetResIdRequest();
        getResIdRequest.filename = fileName;
        getResIdRequest.md5 = md5;
        getResIdRequest.cookies = cookies;
        GetResIdRequest.Reserve reserve = new GetResIdRequest.Reserve();
        reserve.title=fileName;
        getResIdRequest.reserve = RequestBase.getGson().toJson(reserve);
        getResIdRequest.startRequest(GetResIdResponse.class, new HttpCallback<GetResIdResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetResIdResponse ret) {
                mResourceIds=ret.result.resid;
                mType=TYPE_TEXT;
                uploadData(content,ret.result.resid);
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext,error.getMessage());
            }
        });

    }

    private void uploadData(String content, String resourceIds){
        SendClassCircleRequest sendClassCircleRequest=new SendClassCircleRequest();
        sendClassCircleRequest.content=content;
        sendClassCircleRequest.resourceIds=resourceIds;
        mSendDataRequest=sendClassCircleRequest.startRequest(ClassCircleResponse.class, new HttpCallback<ClassCircleResponse>() {
            @Override
            public void onSuccess(RequestBase request, ClassCircleResponse ret) {
                rootView.hiddenLoadingView();
                if (ret.data!=null) {
                    ToastUtil.showToast(mContext, R.string.send_success);
                    mSendDataRequest = null;
                    EventBus.getDefault().post(new RefreshClassCircle());
                    SendClassCircleActivity.this.finish();
                }else {
                    ToastUtil.showToast(mContext,R.string.error_tip);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                mSendDataRequest=null;
                ToastUtil.showToast(mContext,error.getMessage());
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
        if (TextUtils.isEmpty(s)&&(mImagePaths==null||mImagePaths.size()==0)){
            mFunctionView.setEnabled(false);
            mFunctionView.setTextColor(ContextCompat.getColor(mContext,R.color.color_999999));
        }else {
            mFunctionView.setEnabled(true);
            mFunctionView.setTextColor(ContextCompat.getColor(mContext,R.color.color_0068BD));
        }
    }
}
