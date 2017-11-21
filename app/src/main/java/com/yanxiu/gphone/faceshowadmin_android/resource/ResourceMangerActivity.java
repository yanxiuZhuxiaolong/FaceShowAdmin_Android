package com.yanxiu.gphone.faceshowadmin_android.resource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.common.activity.PDFViewActivity;
import com.yanxiu.gphone.faceshowadmin_android.common.activity.WebViewActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.AttachmentInfosBean;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.ResourceDetailRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.ResourceDetailResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.ResourceRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.ResourceResponse;
import com.yanxiu.gphone.faceshowadmin_android.resource.adapter.ResourceMangerAdapter;
import com.yanxiu.gphone.faceshowadmin_android.resource.bean.ResourceBean;
import com.yanxiu.gphone.faceshowadmin_android.resource.bean.ResourceDataBean;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdate;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 资源管理页面
 */
public class ResourceMangerActivity extends FaceShowBaseActivity implements RecyclerViewItemClickListener {

    private PublicLoadLayout mRootView;
    @BindView(R.id.title_layout_left_img)
    ImageView title_layout_left_img;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
//    @BindView(R.id.title_layout_right_img)
//    ImageView title_layout_right_img;
    @BindView(R.id.title_layout_right_txt)
    TextView title_layout_signIn;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.resource_recyclerView)
    LoadMoreRecyclerView mRecyclerView;
    private ResourceMangerAdapter mAdapter;

    private ResourceDataBean mData;

    private String offset=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_resource_manage);
        setContentView(mRootView);
        ButterKnife.bind(this);
        initView();
        initListener();
        requestData(false);
    }

    private void initView() {
        titleLayoutTitle.setText(R.string.manager_resource);
        title_layout_signIn.setText(R.string.upload);
        title_layout_signIn.setTextColor(getResources().getColor(R.color.color_0065b8));
//        title_layout_right_img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selector_resource_updata_bg));
        title_layout_left_img.setVisibility(View.VISIBLE);
//        title_layout_right_img.setVisibility(View.VISIBLE);
        title_layout_signIn.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ResourceMangerAdapter(this);
        mAdapter.addItemClickListener(this);
    }

    private void initListener() {
        mAdapter.addItemClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                requestData(true);
            }
        });
        mRecyclerView.setLoadMoreEnable(true);
        mRecyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore(LoadMoreRecyclerView refreshLayout) {
                requestLoarMore();
            }

            @Override
            public void onLoadmoreComplte() {
            }
        });
    }

    private void setData() {

    }

    @OnClick({ R.id.retry_button, R.id.title_layout_left_img, R.id.title_layout_right_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.title_layout_right_img:
            case R.id.title_layout_right_txt:
                EventUpdate.onEnterSendRecourseClass(this);
                PublishResourceActivity.invoke(ResourceMangerActivity.this);
                break;
            case R.id.retry_button:
                requestData(false);
                break;
            case R.id.title_layout_left_img:
                finish();
                break;
            default:
        }
    }

    /**
     * 获取数据
     */
    private void requestData(final boolean isRefreshIng) {
        if (!isRefreshIng) {
            mRootView.showLoadingView();
        }
        offset=null;
        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        resourceRequest.startRequest(ResourceResponse.class, new HttpCallback<ResourceResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourceResponse ret) {
                mRootView.finish();
                if (isRefreshIng) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (ret != null && ret.getCode() == 0) {
                    mData = ret.getData();
                    if (mData.getResources().getElements().size()>0) {
                        mRecyclerView.setLoadMoreEnable(true);
                        mAdapter.setData(mData.getResources().getElements());
                        mRecyclerView.setAdapter(mAdapter);
                        offset=mData.getResources().getCallbacks().get(0).getCallbackValue();
                    }else {
                        mRecyclerView.setLoadMoreEnable(false);
                        ToastUtil.showToast(ResourceMangerActivity.this,"尚未上传资源");
                    }
                } else {
                    mRootView.showOtherErrorView();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();
                if (isRefreshIng) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        });

    }

    private void requestLoarMore() {
        mRootView.showLoadingView();
        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        resourceRequest.id = offset;
        resourceRequest.startRequest(ResourceResponse.class, new HttpCallback<ResourceResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourceResponse ret) {
                mRootView.finish();
                mRecyclerView.finishLoadMore();
                if (ret == null || ret.getCode() == 0) {
                    if (ret.getData().getResources().getElements() == null || ret.getData().getResources().getElements().size() == 0) {
                        ToastUtil.showToast(ResourceMangerActivity.this, "没有更多数据了");
                        mRecyclerView.setLoadMoreEnable(false);
                    } else {
                        offset=ret.getData().getResources().getCallbacks().get(0).getCallbackValue();
                        mData.getResources().getElements().addAll(ret.getData().getResources().getElements());
                        mAdapter.addData(ret.getData().getResources().getElements());
                        mRecyclerView.setLoadMoreEnable(true);
                    }
                } else {
                    mRootView.showOtherErrorView();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();
                mRecyclerView.finishLoadMore();

            }
        });

    }

    /**
     * 获取资源详情数据
     */
    private void requestDetailData(ResourceBean bean) {
        mRootView.showLoadingView();
        ResourceDetailRequest resourceRequest = new ResourceDetailRequest();
        resourceRequest.resId = bean.getResId();
        resourceRequest.startRequest(ResourceDetailResponse.class, new HttpCallback<ResourceDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourceDetailResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    AttachmentInfosBean attachmentInfosBean = ret.getData().getAi();
                    switch (attachmentInfosBean.getResType()) {
                        case "word":
                        case "doc":
                        case "docx":
                        case "xls":
                        case "xlsx":
                        case "excel":
                        case "ppt":
                        case "pptx":
                        case "pdf":
                        case "text":
                        case "txt":
                            PDFViewActivity.invoke(ResourceMangerActivity.this, attachmentInfosBean.getResName(), attachmentInfosBean.getPreviewUrl());
                            break;
                        default:
                            ToastUtil.showToast(ResourceMangerActivity.this, ret.getError().getMessage());
                            break;
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.finish();
                ToastUtil.showToast(ResourceMangerActivity.this, error.getMessage());
            }
        });

    }

    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, ResourceMangerActivity.class);
        activity.startActivity(intent);
    }

    public static void invoke(Activity activity, int result_code) {
        activity.setResult(result_code);
        activity.finish();
    }

    @Override
    public void onItemClick(View v, int position) {
        ResourceBean data = mData.getResources().getElements().get(position);
        if (TextUtils.equals(data.getType(), "1") && !TextUtils.isEmpty(data.getUrl())) {
            WebViewActivity.loadThisAct(this, data.getUrl(), data.getResName());
        } else if (TextUtils.equals(data.getType(), "0")) {
            if (data != null && data.getType() != null) {
                requestDetailData(data);
            } else {
                ToastUtil.showToast(this, "数据异常");
            }
        } else {
            ToastUtil.showToast(this, "数据异常");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PublishResourceActivity.RESULT_PUBLISH) {
            requestData(false);
        }
    }
}
