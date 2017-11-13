package com.yanxiu.gphone.faceshowadmin_android.course.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.course.adapter.CourseResourceAdapter;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResourcesRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResourcesResponse;

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
                public void onSuccess(RequestBase request, GetCourseResourcesResponse ret) {
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

                                }
                            });
                        } else {
                            mPublicLoadLayout.showOtherErrorView("暂无课程资源");
                        }

                    } else {
                        mPublicLoadLayout.showOtherErrorView(ret.getMessage());
                    }
                }

                @Override
                public void onFail(RequestBase request, Error error) {
                    mPublicLoadLayout.hiddenOtherErrorView();
                    mPublicLoadLayout.showOtherErrorView(error.getMessage());
                }
            });

        } else {
            mPublicLoadLayout.showOtherErrorView("暂无课程资源");
        }
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
