package com.yanxiu.gphone.faceshowadmin_android.main.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.FSAApplication;
import com.yanxiu.gphone.faceshowadmin_android.MainActivity;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.activity.CheckInDetailActivity;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.activity.CheckInNotesActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.interf.MainFragmentRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.main.FullyLinearLayoutManager;
import com.yanxiu.gphone.faceshowadmin_android.main.MainFragmentCheckInAdapter;
import com.yanxiu.gphone.faceshowadmin_android.main.MainFragmentCourseAdapter;
import com.yanxiu.gphone.faceshowadmin_android.main.MainFragmentTabAdapter;
import com.yanxiu.gphone.faceshowadmin_android.main.MainTabBean;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.activity.AdressBookActivity;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.CourseArrangeBean;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.CourseBean;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.TodaySignInBean;
import com.yanxiu.gphone.faceshowadmin_android.main.ui.activity.MainDetailActivity;
import com.yanxiu.gphone.faceshowadmin_android.net.main.MainFragmentRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.main.MainFragmentRequestResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.schedule.ScheduleRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.schedule.ScheduleResponse;
import com.yanxiu.gphone.faceshowadmin_android.notice.NoticeManageActivity;
import com.yanxiu.gphone.faceshowadmin_android.resource.ResourceMangerActivity;
import com.yanxiu.gphone.faceshowadmin_android.schedule.PublishScheduleActivity;
import com.yanxiu.gphone.faceshowadmin_android.schedule.ScheduleManageActivity;
import com.yanxiu.gphone.faceshowadmin_android.schedule.bean.ScheduleBean;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdate;
import com.yanxiu.gphone.faceshowadmin_android.utils.TextTypefaceUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

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

    private MainFragmentRequest mCourseArrangeRequest;

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
        mTitle_layout_left_img.setVisibility(View.VISIBLE);
        mTitle_layout_left_img.setImageResource(R.drawable.selector_main_leftdrawer);
        mTitle_layout_title = mRootView.findViewById(R.id.title_layout_title);
        mTitle_layout_title.setText(getString(R.string.yanxiubao));
        mProject_layput = mRootView.findViewById(R.id.project_layput);
        mProject_tv = mRootView.findViewById(R.id.project_tv);
        mClass_tv = mRootView.findViewById(R.id.class_tv);
        mStudent_count = mRootView.findViewById(R.id.student_count);
        mTeacher_count = mRootView.findViewById(R.id.teacher_count);
        mCourse_count = mRootView.findViewById(R.id.course_count);
        TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_LIGHT, mCourse_count);
        mTask_count = mRootView.findViewById(R.id.task_count);
        TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_LIGHT, mTask_count);
        initTabRecyclerView();
        initCheckinRecyclerView();
        initCourseRecyclerView();
    }

    private void setListener() {
        mProject_layput.setOnClickListener(this);
        mTitle_layout_left_img.setOnClickListener(this);
        mRootView.setRetryButtonOnclickListener(this);
    }

    private void setData() {
        if (mData != null && mData.getProjectInfo() != null) {
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
            mCheckin_recyclerView.setAdapter(mCheckinAdapter);
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
            mCourse_recyclerView.setAdapter(mCourseAdapter);
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
        mCourseArrangeRequest = new MainFragmentRequest();
        mCourseArrangeRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        mCourseArrangeRequest.startRequest(MainFragmentRequestResponse.class, new HttpCallback<MainFragmentRequestResponse>() {
            @Override
            public void onSuccess(RequestBase request, MainFragmentRequestResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData() != null) {
                        mData = ret.getData();
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).setLeftDrawer();//给抽屉刷新数据
                        }
                        if (isAdded() && getActivity() != null) // 防止Fragment被移除
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
                EventUpdate.onEnterAdressBook(getContext());
                AdressBookActivity.LuanchActivity(getContext());
                break;
            case 1:
                EventUpdate.onEnterNotify(getContext());
                NoticeManageActivity.invoke(getActivity());
                break;
            case 2:
                EventUpdate.onEnterCheckKinRecord(getContext());
                CheckInNotesActivity.toThisAct(getActivity());
                break;
            case 3:
                requestScheduleData();
                break;
            case 4:
                EventUpdate.onEnterResource(getContext());
                ResourceMangerActivity.invoke(getActivity());
                break;
            default:

        }
    }

    private static final int REQUEST_CODE_TO_SIGN_IN = 0x100;

    /**
     * 签到item点击
     *
     * @param v
     * @param position
     */
    @Override
    public void onCheckInItemClick(View v, int position) {
        Intent intent = new Intent(getContext(), CheckInDetailActivity.class);
        intent.putExtra("stepId", String.valueOf(mData.getTodaySignIns().get(position).getStepId()));
        startActivityForResult(intent, REQUEST_CODE_TO_SIGN_IN);
    }

    /**
     * 课程item点击
     *
     * @param v
     * @param position
     */
    @Override
    public void onCourseTabItemClick(View v, int position) {
        ToastUtil.showToast(getActivity(), "课程item点击 position = " + position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.project_layput:
                EventUpdate.onSeeClassDetail(getContext());
                MainDetailActivity.invoke(getActivity(), mData);
                break;
            case R.id.title_layout_left_img:
                EventUpdate.onShowDrawerLeft(getContext());
                ((MainActivity) getActivity()).openLeftDrawer();
                break;
            case R.id.retry_button:
                requestData();
                break;
            default:
        }
    }

    public CourseArrangeBean getmData() {
        return mData;
    }

    /**
     * 获取日程信息
     */
    private void requestScheduleData() {
        mRootView.showLoadingView();
        ScheduleRequest scheduleRequest = new ScheduleRequest();
        scheduleRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        scheduleRequest.startRequest(ScheduleResponse.class, new HttpCallback<ScheduleResponse>() {
            @Override
            public void onSuccess(RequestBase request, ScheduleResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0 && ret.getSchedule() != null) {
                    ScheduleBean bean = ret.getSchedule();
                    EventUpdate.onEnterSchedule(getContext());
                    ScheduleManageActivity.invoke(getActivity(), bean);
                } else {
                    PublishScheduleActivity.invoke(getActivity());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.finish();
                if (error != null) {
                    ToastUtil.showToast(FSAApplication.getInstance().getApplicationContext(), error.getMessage());
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        if (mCourseArrangeRequest != null)
            mCourseArrangeRequest.cancelRequest();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_TO_SIGN_IN==requestCode){
                if (resultCode==RESULT_OK){
                    requestData();
                }
        }
    }
}
