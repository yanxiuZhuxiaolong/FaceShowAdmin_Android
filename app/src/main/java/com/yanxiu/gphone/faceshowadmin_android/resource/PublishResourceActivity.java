package com.yanxiu.gphone.faceshowadmin_android.resource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.CreateResourceRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.CreateResourceResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.ResourcePublishRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.ResourcePublishResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdate;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布资源页面
 */
public class PublishResourceActivity extends FaceShowBaseActivity {

    public static final int RESULT_PUBLISH = 0x1011;

    private PublicLoadLayout mRootView;

    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_right_txt)
    TextView title_layout_right_txt;
    @BindView(R.id.title_layout_left_img)
    ImageView title_layout_left_img;

    @BindView(R.id.resource_edit_title)
    EditText mResourceEditTitle;
    @BindView(R.id.resource_edit_content)
    EditText mResourceEditContent;

    private String mResourceName;
    private String mResourceUrl;
    private String mResourceIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_resource_publish);
        setContentView(mRootView);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleLayoutTitle.setText(R.string.publish_resource);
        title_layout_right_txt.setText(R.string.notice_submit);
        title_layout_right_txt.setEnabled(false);
//        title_layout_left_img.setImageResource(R.color.color_96bde4);
        titleLayoutTitle.setVisibility(View.VISIBLE);
        title_layout_right_txt.setVisibility(View.VISIBLE);
        title_layout_left_img.setVisibility(View.VISIBLE);
        mResourceEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mResourceName = mResourceEditTitle.getText().toString();
                canPublish();
            }
        });
        mResourceEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mResourceUrl = mResourceEditContent.getText().toString();
                canPublish();
            }
        });
    }

    private void canPublish() {
        if (!TextUtils.isEmpty(mResourceName) && !TextUtils.isEmpty(mResourceUrl) && mResourceName.length() > 0 && mResourceUrl.length() > 0) {
            title_layout_right_txt.setEnabled(true);
        } else {
            title_layout_right_txt.setEnabled(false);
        }
    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                finish();
                break;
            case R.id.title_layout_right_txt:
                EventUpdate.onSendRecourse(this);
                GetResId();
                break;
        }
    }

    private void GetResId() {
        mRootView.showLoadingView();
        CreateResourceRequest createResourceRequest = new CreateResourceRequest();
        createResourceRequest.filename = mResourceName;
        createResourceRequest.createtime = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());
        CreateResourceRequest.Reserve reserve = new CreateResourceRequest.Reserve();
        reserve.externalUrl = mResourceUrl;
        createResourceRequest.reserve = RequestBase.getGson().toJson(reserve);
        createResourceRequest.startRequest(CreateResourceResponse.class, new HttpCallback<CreateResourceResponse>() {
            @Override
            public void onSuccess(RequestBase request, CreateResourceResponse ret) {
                if (ret.getCode() == 0 && ret.getDesc().equals("success")) {
                    mResourceIds = ret.getResid();
                    submitPostRequest();
                } else {
                    mRootView.hiddenLoadingView();
                    ToastUtil.showToast(PublishResourceActivity.this, getString(R.string.send_class_circle_fail));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                ToastUtil.showToast(PublishResourceActivity.this, error.getMessage());
            }
        });

    }

    private void submitPostRequest() {
        ResourcePublishRequest resourcePublishRequest = new ResourcePublishRequest();
        resourcePublishRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        resourcePublishRequest.resId = mResourceIds;
        resourcePublishRequest.resName = mResourceName;
        resourcePublishRequest.url = mResourceUrl;
        resourcePublishRequest.startRequest(ResourcePublishResponse.class, new HttpCallback<ResourcePublishResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourcePublishResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    ToastUtil.showToast(PublishResourceActivity.this, "发布成功");
                    ResourceMangerActivity.invoke(PublishResourceActivity.this, RESULT_PUBLISH);
                    finish();
                } else {
                    ToastUtil.showToast(PublishResourceActivity.this, getString(R.string.send_class_circle_fail));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                ToastUtil.showToast(PublishResourceActivity.this, getString(R.string.send_class_circle_fail));
            }
        });
    }

    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, PublishResourceActivity.class);
        activity.startActivityForResult(intent, RESULT_PUBLISH);
    }

}
