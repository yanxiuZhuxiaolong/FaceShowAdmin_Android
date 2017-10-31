package com.yanxiu.gphone.faceshowadmin_android.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.interf.MainFragmentRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.CourseArrangeBean;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.CourseBean;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.TodaySignInBean;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.main.MainFragmentRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.main.MainFragmentRequestResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.ScreenUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import com.yanxiu.gphone.faceshowadmin_android.main.MainFragmentTabAdapter;
import com.yanxiu.gphone.faceshowadmin_android.main.MainFragmentCheckInAdapter;
import com.yanxiu.gphone.faceshowadmin_android.main.MainFragmentCourseAdapter;
import com.yanxiu.gphone.faceshowadmin_android.main.MainTabBean;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * main tab
 * Created by frc on 17-10-26.
 */

public class MainFragment extends Fragment implements View.OnClickListener, MainFragmentRecyclerViewItemClickListener {
    private PublicLoadLayout mRootView;

    protected RecyclerView mTab_recyclerView;
    private MainFragmentTabAdapter mTabAdapter;

    protected RecyclerView mCheckin_recyclerView;
    private View no_checkin;
    private MainFragmentCheckInAdapter mCheckinAdapter;

    protected RecyclerView mCourse_recyclerView;
    private View no_course;
    private MainFragmentCourseAdapter mCourseAdapter;

    private CourseArrangeBean mData;
    private ArrayList mTabList = new ArrayList(5);
    private ArrayList<TodaySignInBean> mCheckInList = new ArrayList();
    private ArrayList<CourseBean> mCourseList = new ArrayList();


    private ImageView mTitle_layout_left_img;
    private TextView mTitle_layout_title;
    private View mProject_layput;
    private TextView mProject_tv;
    private TextView mClass_tv;
    private TextView mStudent_count;
    private TextView mTeacher_count;
    private TextView mCourse_count;
    private TextView mTask_count;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_main_layout);
        initData();
        initView();
        setListener();
        return mRootView;
    }

    private void initData() {
        initTabData();
        requestData();
    }

    private void initView() {
        mTitle_layout_left_img = mRootView.findViewById(R.id.title_layout_left_img);
        mTitle_layout_title = mRootView.findViewById(R.id.title_layout_title);
        mTitle_layout_left_img.setVisibility(View.VISIBLE);
        mProject_layput = mRootView.findViewById(R.id.project_layput);
        mProject_tv = mRootView.findViewById(R.id.project_tv);
        mClass_tv = mRootView.findViewById(R.id.class_tv);
        mStudent_count = mRootView.findViewById(R.id.student_count);
        mTeacher_count = mRootView.findViewById(R.id.teacher_count);
        mCourse_count = mRootView.findViewById(R.id.course_count);
        mTask_count = mRootView.findViewById(R.id.task_count);
        initTabRecyclerView();
        initCheckinRecyclerView();
        initCourseRecyclerView();
    }

    private void setListener() {
        mProject_layput.setOnClickListener(this);
        mTitle_layout_left_img.setOnClickListener(this);
    }

    private void setData() {
        if (mData != null && mData.getProjectInfo() != null) {
            mTitle_layout_title.setText(getString(R.string.tab_main));
            mProject_tv.setText(mData.getProjectInfo().getProjectName());
            setTabData();
            if (mData.getClazsInfo() != null) {
                mClass_tv.setText(mData.getClazsInfo().getClazsName());
            }
            if (mData.getClazsStatisticView() != null) {
                mStudent_count.setText(mData.getClazsStatisticView().getStudensNum());
                mTeacher_count.setText(mData.getClazsStatisticView().getMasterNum());
                mCourse_count.setText(mData.getClazsStatisticView().getCourseNum());
                mTask_count.setText(mData.getClazsStatisticView().getTaskNum());
            }
            if (mData.getClazsInfo() != null) {

            }
            setCheckinData();

            setCourseData();

        } else {
            mRootView.showOtherErrorView();
        }
    }

    private void setTabData() {
        mTabAdapter.setData(mTabList);
        mTab_recyclerView.setAdapter(mTabAdapter);
    }

    private void setCheckinData() {
        mCheckInList = mData.getTodaySignIns();
        if (mData.getTodaySignIns() == null || mCheckInList.isEmpty()) {
            no_checkin.setVisibility(View.VISIBLE);
            mCheckin_recyclerView.setVisibility(View.GONE);
        } else {
            no_checkin.setVisibility(View.GONE);
            mCheckin_recyclerView.setVisibility(View.VISIBLE);
            mCheckinAdapter.setData(mCheckInList);
            mCheckin_recyclerView.setAdapter(mTabAdapter);
        }
    }

    private void setCourseData() {
        mCourseList = mData.getTodayCourses();
        if (mData.getTodayCourses() == null || mCourseList.isEmpty()) {
            no_course.setVisibility(View.VISIBLE);
            mCourse_recyclerView.setVisibility(View.GONE);
        } else {
            no_course.setVisibility(View.GONE);
            mCourse_recyclerView.setVisibility(View.VISIBLE);
            mCourseAdapter.setData(mCourseList);
            mCourse_recyclerView.setAdapter(mTabAdapter);
        }
    }

    private void initTabRecyclerView() {
        mTab_recyclerView = mRootView.findViewById(R.id.tab_recyclerView);

        mTabAdapter = new MainFragmentTabAdapter(getActivity(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTab_recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void initCheckinRecyclerView() {
        mCheckin_recyclerView = mRootView.findViewById(R.id.checkin_recyclerView);
        no_checkin = mRootView.findViewById(R.id.no_checkin);

        mCheckinAdapter = new MainFragmentCheckInAdapter(getActivity(), this);
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCheckin_recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void initCourseRecyclerView() {
        mCourse_recyclerView = mRootView.findViewById(R.id.course_recyclerView);
        no_course = mRootView.findViewById(R.id.no_course);

        mCourseAdapter = new MainFragmentCourseAdapter(getActivity(), this);
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCourse_recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void initTabData() {
        MainTabBean mb1 = new MainTabBean();
        mb1.setImgResourcesId(R.drawable.contacts_img);
        mb1.setName("通讯录");
        MainTabBean mb2 = new MainTabBean();
        mb2.setImgResourcesId(R.drawable.notify_img);
        mb2.setName("通知管理");
        MainTabBean mb3 = new MainTabBean();
        mb3.setImgResourcesId(R.drawable.chekkin_record_img);
        mb3.setName("签到记录");
        MainTabBean mb4 = new MainTabBean();
        mb4.setImgResourcesId(R.drawable.schedule_img);
        mb4.setName("日程管理");
        MainTabBean mb5 = new MainTabBean();
        mb5.setImgResourcesId(R.drawable.resources_img);
        mb5.setName("资源管理");
        mTabList.add(mb1);
        mTabList.add(mb2);
        mTabList.add(mb3);
        mTabList.add(mb4);
        mTabList.add(mb5);
    }

    private void requestData() {
        mRootView.showLoadingView();
        MainFragmentRequest courseArrangeRequest = new MainFragmentRequest();
        courseArrangeRequest.clazsId = UserInfo.getInstance().getInfo().getClassId();
        courseArrangeRequest.clazsId = "9";
        courseArrangeRequest.startRequest(MainFragmentRequestResponse.class, new HttpCallback<MainFragmentRequestResponse>() {
            @Override
            public void onSuccess(RequestBase request, MainFragmentRequestResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData() != null) {
                        mData = ret.getData();
                        setData();
                    } else {
                        mRootView.showOtherErrorView();
                    }

                } else {
                    mRootView.showOtherErrorView();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();

            }
        });

    }


    /**
     * 首页5个tab的点击
     *
     * @param v
     * @param position
     */
    @Override
    public void onTabItemClick(View v, int position) {
        switch (position) {
            case 0:
                ToastUtil.showToast(getActivity(), "通讯录");
                break;
            case 1:
                ToastUtil.showToast(getActivity(), "通知管理");
                break;
            case 2:
                ToastUtil.showToast(getActivity(), "签到记录");
                break;
            case 3:
                ToastUtil.showToast(getActivity(), "日程管理");
                break;
            case 4:
                ToastUtil.showToast(getActivity(), "资源管理");
                break;

        }
    }

    /**
     * 签到item点击
     *
     * @param v
     * @param position
     */
    @Override
    public void onCheckInItemClick(View v, int position) {
        ToastUtil.showToast(getActivity(), "position");

    }

    /**
     * 课程item点击
     *
     * @param v
     * @param position
     */
    @Override
    public void onCourseTabItemClick(View v, int position) {
        ToastUtil.showToast(getActivity(), "position");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.project_layput:
                //TODO @凤清
                ToastUtil.showToast(getActivity(), "跳转详情");
                break;
            case R.id.title_layout_left_img:
                ToastUtil.showToast(getActivity(), "打开抽屉");
                break;
        }
    }
}
