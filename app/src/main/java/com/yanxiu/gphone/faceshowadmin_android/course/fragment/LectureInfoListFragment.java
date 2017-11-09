package com.yanxiu.gphone.faceshowadmin_android.course.fragment;

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
import com.yanxiu.gphone.faceshowadmin_android.course.adapter.LectureInfoAdapter;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author frc  on 17-11-9.
 */

public class LectureInfoListFragment extends FaceShowBaseFragment {
    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setContentView(R.layout.fragment_lecture_info_list_layout);
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);

        GetCourseResponse.CourseBean course = getArguments() != null ? (GetCourseResponse.CourseBean) getArguments().get("data") : null;
        if (course != null && course.getLecturerInfos() != null && course.getLecturerInfos().size() > 0) {
            LectureInfoAdapter lectureInfoAdapter = new LectureInfoAdapter(course.getLecturerInfos());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(lectureInfoAdapter);
        } else {
            mPublicLoadLayout.showOtherErrorView(getString(R.string.no_lecture));
        }

        return mPublicLoadLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
