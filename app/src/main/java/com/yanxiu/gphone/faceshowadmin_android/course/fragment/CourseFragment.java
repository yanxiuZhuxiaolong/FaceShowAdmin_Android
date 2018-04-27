package com.yanxiu.gphone.faceshowadmin_android.course.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.MainActivity;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.course.adapter.CoursesAdapter;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetClassCoursesRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetClassCoursesResponse;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private UUID mUUID;
    private CoursesAdapter mCoursesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setContentView(R.layout.fragment_course_layout);
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
//        mTitleLayoutLeftImg.setImageResource(R.drawable.selector_main_leftdrawer);
        mTitleLayoutTitle.setText(R.string.course);
        mTitleLayoutTitle.setVisibility(View.VISIBLE);
        mPublicLoadLayout.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCourseListData();
            }

        });
        mPublicLoadLayout.showLoadingView();
        getCourseListData();
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCourseListData();
            }
        });
        return mPublicLoadLayout;
    }

    private void getCourseListData() {

        GetClassCoursesRequest getClassCoursesRequest = new GetClassCoursesRequest();
        getClassCoursesRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        mUUID = getClassCoursesRequest.startRequest(GetClassCoursesResponse.class, new HttpCallback<GetClassCoursesResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetClassCoursesResponse ret) {
                mPublicLoadLayout.finish();
                if (mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
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
                            mCoursesAdapter.update(ret.getData());
                        }
                    } else {
                        mPublicLoadLayout.showOtherErrorView(getString(R.string.no_course));
                    }
                } else {
                    mPublicLoadLayout.showOtherErrorView(ret.getMessage());

                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.finish();
                if (mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mPublicLoadLayout.showNetErrorView();
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

    @OnClick(R.id.title_layout_left_img)
    public void onViewClicked() {
        getActivity().finish();
    }
}
