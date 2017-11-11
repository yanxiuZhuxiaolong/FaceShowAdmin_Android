package com.yanxiu.gphone.faceshowadmin_android.course.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.course.fragment.CourseResourceFragment;
import com.yanxiu.gphone.faceshowadmin_android.course.fragment.CourseTaskFragment;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.ScreenUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseDetailActivity extends FaceShowBaseActivity {
    @BindView(R.id.course_title)
    TextView mCourseTitle;
    @BindView(R.id.course_time)
    TextView mCourseTime;
    @BindView(R.id.course_teacher)
    TextView mCourseTeacher;
    @BindView(R.id.course_location)
    TextView mCourseLocation;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.img_left_back)
    ImageView mImgLeftBack;
    @BindView(R.id.tv_see_course)
    TextView mTvSeeCourse;
    private PublicLoadLayout mPublicLoadLayout;
    private UUID mGetCourseRequestUUID;
    private GetCourseResponse.DataBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPublicLoadLayout = new PublicLoadLayout(this);
        mPublicLoadLayout.setContentView(R.layout.activity_course_detail);
        setContentView(mPublicLoadLayout);
        ButterKnife.bind(this);
        getCourse();

    }

    private List<Fragment> list = new ArrayList<>();

    private void getCourse() {
        mPublicLoadLayout.showLoadingView();
        final GetCourseRequest getCourseRequest = new GetCourseRequest();
        getCourseRequest.courseId = getIntent().getStringExtra("courseId");
        mGetCourseRequestUUID = getCourseRequest.startRequest(GetCourseResponse.class, new HttpCallback<GetCourseResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetCourseResponse ret) {
                mPublicLoadLayout.hiddenOtherErrorView();
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    data = ret.getData();
                    mCourseTitle.setText(Html.fromHtml(ret.getData().getCourse().getCourseName()));
                    if (ret.getData().getCourse().getLecturerInfos() != null && ret.getData().getCourse().getLecturerInfos() != null && ret.getData().getCourse().getLecturerInfos().size() > 0) {
                        mCourseTeacher.setText(ret.getData().getCourse().getLecturerInfos().get(0).getLecturerName());
                    } else {
                        mCourseTeacher.setText("无");
                    }
                    mCourseLocation.setText(TextUtils.isEmpty(ret.getData().getCourse().getSite()) ? getString(R.string.wait_for) : ret.getData().getCourse().getSite());
                    mCourseTime.setText(StringUtils.getCourseTime(ret.getData().getCourse().getStartTime(), ret.getData().getCourse().getEndTime()));
                    CourseResourceFragment courseResourceFragment = new CourseResourceFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("courseId", getCourseRequest.courseId);
                    courseResourceFragment.setArguments(bundle1);
                    CourseTaskFragment courseTaskFragment = new CourseTaskFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", ret.getData());
                    courseTaskFragment.setArguments(bundle);
                    list.add(courseTaskFragment);
                    list.add(courseResourceFragment);
                    mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            return list.get(position);
                        }

                        @Override
                        public int getCount() {
                            return list.size();
                        }

                        @Nullable
                        @Override
                        public CharSequence getPageTitle(int position) {
                            if (position == 0) {
                                return getString(R.string.course_task);
                            } else {
                                return getString(R.string.course_resource);
                            }
                        }
                    });
                    mTabLayout.setupWithViewPager(mViewPager);
                    setUpIndicatorWidth(mTabLayout, 20, 20);
                } else {
                    mPublicLoadLayout.showOtherErrorView(ret.getMessage());
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.hiddenOtherErrorView();
            }
        });

    }

    @OnClick({R.id.img_left_back, R.id.tv_see_course})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left_back:
                this.finish();
                break;
            case R.id.tv_see_course:
                Intent intent = new Intent(CourseDetailActivity.this, CourseMessageActivity.class);
                intent.putExtra("data", data.getCourse());
                startActivity(intent);
                break;
            default:
        }
    }

    private void setUpIndicatorWidth(TabLayout tabLayout, int marginLeft, int marginRight) {
        Class<?> tabLayoutClass = tabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout layout = null;
        try {
            if (tabStrip != null) {
                layout = (LinearLayout) tabStrip.get(tabLayout);
            }
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(ScreenUtils.dpToPxInt(CourseDetailActivity.this, marginLeft));
                    params.setMarginEnd(ScreenUtils.dpToPxInt(CourseDetailActivity.this, marginRight));
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGetCourseRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetCourseRequestUUID);
        }
    }
}
