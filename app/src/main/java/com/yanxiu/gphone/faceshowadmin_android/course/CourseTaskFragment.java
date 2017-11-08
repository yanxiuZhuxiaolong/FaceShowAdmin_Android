package com.yanxiu.gphone.faceshowadmin_android.course;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author frc on 17-11-8.
 */

public class CourseTaskFragment extends FaceShowBaseFragment {
    PublicLoadLayout mPublicLoadLayout;
    GetCourseResponse.DataBean data;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setErrorLayoutFullScreen();
        mPublicLoadLayout.setContentView(inflater.inflate(R.layout.fragment_course_task_layout, container, false));
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);

        data = (getArguments() != null ? (GetCourseResponse.DataBean) getArguments().get("data") : null);
        if (data == null) {
            mPublicLoadLayout.showOtherErrorView("暂无课程任务");
        } else {
            if (data.getInteractSteps() != null && data.getInteractSteps().size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                CourseTaskAdapter courseTaskAdapter = new CourseTaskAdapter(data.getInteractSteps());
                mRecyclerView.setAdapter(courseTaskAdapter);
                courseTaskAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {

                    }
                });
            } else {
                mPublicLoadLayout.showOtherErrorView("暂无课程任务");
            }
        }
        return mPublicLoadLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
