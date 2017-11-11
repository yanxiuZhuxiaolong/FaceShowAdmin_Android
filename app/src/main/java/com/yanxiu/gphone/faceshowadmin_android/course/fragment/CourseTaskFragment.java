package com.yanxiu.gphone.faceshowadmin_android.course.fragment;

import android.content.Intent;
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
import com.yanxiu.gphone.faceshowadmin_android.checkIn.activity.CheckInDetailActivity;
import com.yanxiu.gphone.faceshowadmin_android.course.activity.CourseCommentActivity;
import com.yanxiu.gphone.faceshowadmin_android.course.adapter.CourseTaskAdapter;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResponse;
import com.yanxiu.gphone.faceshowadmin_android.task.activity.QuestionnaireActivity;
import com.yanxiu.gphone.faceshowadmin_android.task.activity.VoteActivity;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

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
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
                        GetCourseResponse.InteractStepsBean interactStep = data.getInteractSteps().get(position);
                        Intent intent = null;
                        switch (interactStep.getInteractType()) {
                            case 6:
                                intent = new Intent(getContext(), CheckInDetailActivity.class);
                                break;
                            case 5:
                                intent = new Intent(getContext(), QuestionnaireActivity.class);
                                break;
                            case 4:
                                intent = new Intent(getContext(), CourseCommentActivity.class);
                                break;
                            case 3:
                                intent = new Intent(getContext(), VoteActivity.class);
                            default:
                        }
                        if (intent == null) {
                            ToastUtil.showToast(getContext(), "未知类型");
                            return;
                        }
                        intent.putExtra("stepId", String.valueOf(interactStep.getStepId()));
                        if (getActivity() != null) {
                            getActivity().startActivity(intent);
                        }
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
