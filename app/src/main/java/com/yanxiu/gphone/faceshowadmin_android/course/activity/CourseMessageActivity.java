package com.yanxiu.gphone.faceshowadmin_android.course.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.course.fragment.CourseBriefIntroductionFragment;
import com.yanxiu.gphone.faceshowadmin_android.course.fragment.LectureInfoListFragment;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdata;
import com.yanxiu.gphone.faceshowadmin_android.utils.ScreenUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author frc
 */
public class CourseMessageActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_message);
        ButterKnife.bind(this);
        mTitleLayoutTitle.setText(R.string.course_message);
        mTitleLayoutTitle.setVisibility(View.VISIBLE);
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);

        GetCourseResponse.CourseBean course = (GetCourseResponse.CourseBean) getIntent().getSerializableExtra("data");
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", course);
        LectureInfoListFragment lectureInfoListFragment = new LectureInfoListFragment();
        lectureInfoListFragment.setArguments(bundle);
        CourseBriefIntroductionFragment courseBriefIntroductionFragment = new CourseBriefIntroductionFragment();
        courseBriefIntroductionFragment.setArguments(bundle);
        mFragmentList.add(lectureInfoListFragment);
        mFragmentList.add(courseBriefIntroductionFragment);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.course_lecture);
                    case 1:
                        return getString(R.string.lecture_brief);
                    default:
                        return null;
                }
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    EventUpdata.onSeeCourseProfile(CourseMessageActivity.this);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
        setUpIndicatorWidth(mTabLayout, 20, 20);

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
                    params.setMarginStart(ScreenUtils.dpToPxInt(CourseMessageActivity.this, marginLeft));
                    params.setMarginEnd(ScreenUtils.dpToPxInt(CourseMessageActivity.this, marginRight));
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.title_layout_left_img)
    public void onViewClicked() {
        CourseMessageActivity.this.finish();
    }
}
