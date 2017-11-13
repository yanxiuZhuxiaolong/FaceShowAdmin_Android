package com.yanxiu.gphone.faceshowadmin_android.course.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.Constants;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.common.activity.PDFViewActivity;
import com.yanxiu.gphone.faceshowadmin_android.common.activity.WebViewActivity;
import com.yanxiu.gphone.faceshowadmin_android.course.adapter.CourseResourceAdapter;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResourcesRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResourcesResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.AttachmentInfosBean;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.ResourceDetailRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.resource.ResourceDetailResponse;
import com.yanxiu.gphone.faceshowadmin_android.resource.ResourceMangerActivity;
import com.yanxiu.gphone.faceshowadmin_android.resource.bean.ResourceBean;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author frc on 17-11-8.
 */

public class CourseResourceFragment extends FaceShowBaseFragment {
    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private UUID mGetCourseResourcesRequestUUID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setErrorLayoutFullScreen();
        mPublicLoadLayout.setContentView(inflater.inflate(R.layout.fragment_course_task_layout, container, false));
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);
        getCourseResources();
        return mPublicLoadLayout;
    }

    private void getCourseResources() {
        String courseId = (getArguments() != null ? (String) getArguments().get("courseId") : null);
        if (courseId != null) {
            mPublicLoadLayout.showLoadingView();
            GetCourseResourcesRequest getCourseResourcesRequest = new GetCourseResourcesRequest();
            getCourseResourcesRequest.courseId = courseId;
            mGetCourseResourcesRequestUUID = getCourseResourcesRequest.startRequest(GetCourseResourcesResponse.class, new HttpCallback<GetCourseResourcesResponse>() {
                @Override
                public void onSuccess(RequestBase request, final GetCourseResourcesResponse ret) {
                    mPublicLoadLayout.hiddenOtherErrorView();
                    if (ResponseConfig.SUCCESS == ret.getCode()) {
                        if (ret.getData() != null && ret.getData().getResources() != null && ret.getData().getResources().getElements() != null && ret.getData().getResources().getElements().size() > 0) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            CourseResourceAdapter courseResourceAdapter = new CourseResourceAdapter(ret.getData().getResources().getElements());
                            mRecyclerView.setAdapter(courseResourceAdapter);
                            courseResourceAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    GetCourseResourcesResponse.ElementsBean data = ret.getData().getResources().getElements().get(position);
                                    if (!TextUtils.isEmpty(data.getUrl())) {
                                        WebViewActivity.loadThisAct(getContext(), data.getUrl(), data.getResName());
                                    } else {
                                        if (data != null && data.getType() != null) {
                                            requestDetailData(data);
                                        } else {
                                        ToastUtil.showToast(getContext(), "数据异常");
                                        }
                                    }
                                }
                            });
                        } else {
                            mPublicLoadLayout.showOtherErrorView("暂无课程资源");
                        }

                    } else

                    {
                        mPublicLoadLayout.showOtherErrorView(ret.getMessage());
                    }
                }

                @Override
                public void onFail(RequestBase request, Error error) {
                    mPublicLoadLayout.hiddenOtherErrorView();
                    mPublicLoadLayout.showOtherErrorView(error.getMessage());
                }
            });

        } else

        {
            mPublicLoadLayout.showOtherErrorView("暂无课程资源");
        }

    }

    /**
     * 获取资源详情数据
     */

    private void requestDetailData(GetCourseResourcesResponse.ElementsBean bean) {
        mPublicLoadLayout.showLoadingView();
        ResourceDetailRequest resourceRequest = new ResourceDetailRequest();
        resourceRequest.resId = String.valueOf(bean.getResId());
        resourceRequest.startRequest(ResourceDetailResponse.class, new HttpCallback<ResourceDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourceDetailResponse ret) {
                mPublicLoadLayout.finish();
                if (ret != null && ret.getCode() == 0) {
                    AttachmentInfosBean attachmentInfosBean = ret.getData().getAi();
                    if (attachmentInfosBean.getResType().equals(Constants.EXCEL) || attachmentInfosBean.getResType().equals(Constants.PDF)
                            || attachmentInfosBean.getResType().equals(Constants.PPT) || attachmentInfosBean.getResType().equals(Constants.TEXT)
                            || attachmentInfosBean.getResType().equals(Constants.WORD)) {

                        PDFViewActivity.invoke(getActivity(), attachmentInfosBean.getResName(), attachmentInfosBean.getPreviewUrl());
                    } else {
                        ToastUtil.showToast(getContext(), ret.getError().getMessage());
                    }
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.finish();
                ToastUtil.showToast(getContext(), error.getMessage());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mGetCourseResourcesRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetCourseResourcesRequestUUID);
        }
    }
}
