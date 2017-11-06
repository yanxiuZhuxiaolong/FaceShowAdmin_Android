package com.yanxiu.gphone.faceshowadmin_android.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetClassCoursesRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetClassCoursesResponse;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * course tab
 * Created by frc on 17-10-26.
 */

public class CourseFragment extends FaceShowBaseFragment {
    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.expandableListView)
    ExpandableListView mExpandableListView;
    Unbinder unbinder;
    private UUID mUUID;
    private CoursesAdapter mCoursesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setContentView(R.layout.fragment_course_layout);
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);
        mTitleLayoutTitle.setText(R.string.course);
        mTitleLayoutTitle.setVisibility(View.VISIBLE);
        mPublicLoadLayout.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCourseListData();
            }

        });
        getCourseListData();
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                return false;
            }
        });

        return mPublicLoadLayout;
    }

    private void getCourseListData() {
        mPublicLoadLayout.showLoadingView();
        GetClassCoursesRequest getClassCoursesRequest = new GetClassCoursesRequest();
        getClassCoursesRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        mUUID = getClassCoursesRequest.startRequest(GetClassCoursesResponse.class, new HttpCallback<GetClassCoursesResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetClassCoursesResponse ret) {
                mPublicLoadLayout.hiddenOtherErrorView();
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    if (ret.getData().getCourses() != null && ret.getData().getCourses().size() > 0) {
                        if (mCoursesAdapter == null) {
                            mCoursesAdapter = new CoursesAdapter(getContext(), ret.getData());
                            mExpandableListView.setGroupIndicator(null);
                            mExpandableListView.setAdapter(mCoursesAdapter);
                            int groupCount = mExpandableListView.getCount();
                            for (int i = 0; i < groupCount; i++) {
                                mExpandableListView.expandGroup(i);
                            }
                        } else {
                            mExpandableListView.notify();
                        }
                    } else {
                        mPublicLoadLayout.showOtherErrorView(getString(R.string.data_empty));
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

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mUUID != null) {
            RequestBase.cancelRequestWithUUID(mUUID);
        }
    }
}
