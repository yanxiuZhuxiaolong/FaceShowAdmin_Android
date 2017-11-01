package com.yanxiu.gphone.faceshowadmin_android.main.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.main.adapter.MainDetailPagerAdapter;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.CourseArrangeBean;
import com.yanxiu.gphone.faceshowadmin_android.main.ui.fragment.ClassDetailFragment;
import com.yanxiu.gphone.faceshowadmin_android.main.ui.fragment.ProjectDetailFragment;
import com.yanxiu.gphone.faceshowadmin_android.utils.ScreenUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lufengqing on 2017/10/31.
 */

public class MainDetailActivity extends FaceShowBaseActivity {
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    private Context mContext;
    private PublicLoadLayout rootView;
    private Unbinder unbinder;
    @BindView(R.id.tablayout_course)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private ArrayList<Fragment> mTabContents;
    private List<String> mTitleList;
    private ProjectDetailFragment mProjectDetailFragment;
    private ClassDetailFragment mClassDetailFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_main_detail);
        setContentView(rootView);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleLayoutTitle.setText(R.string.main_detail_title);
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        initFragments();
        MainDetailPagerAdapter mAdapter = new MainDetailPagerAdapter(getSupportFragmentManager(), mTabContents);
        mAdapter.setTitleList(mTitleList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void initFragments() {
        mTabContents = new ArrayList<>();
        CourseArrangeBean courseArrangeBean = (CourseArrangeBean) getIntent().getSerializableExtra("COURSE_ARRANGE");
        mProjectDetailFragment = new ProjectDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("projectName", courseArrangeBean.getProjectInfo().getProjectName());
        bundle.putString("startTime", courseArrangeBean.getProjectInfo().getStartTime());
        bundle.putString("endTime", courseArrangeBean.getProjectInfo().getEndTime());
        bundle.putString("description", courseArrangeBean.getProjectInfo().getDescription());
        mProjectDetailFragment.setArguments(bundle);
        mClassDetailFragment = new ClassDetailFragment();
        bundle = new Bundle();
        bundle.putString("className", courseArrangeBean.getClazsInfo().getClazsName());
        bundle.putString("startTime", courseArrangeBean.getClazsInfo().getStartTime().split(" ")[0]);
        bundle.putString("endTime", courseArrangeBean.getClazsInfo().getEndTime().split(" ")[0]);
        bundle.putString("description", courseArrangeBean.getClazsInfo().getDescription());
        bundle.putString("studensNum", courseArrangeBean.getClazsStatisticView().getStudensNum());
        bundle.putString("masterNum", courseArrangeBean.getClazsStatisticView().getMasterNum());
        mClassDetailFragment.setArguments(bundle);
        mTabContents.add(mProjectDetailFragment);
        mTabContents.add(mClassDetailFragment);
        mTitleList = new ArrayList<>();
        mTitleList.add(getResources().getString(R.string.project_detail));
        mTitleList.add(getResources().getString(R.string.class_detail));
        setUpIndicatorWidth(mTabLayout, 44, 44);
    }

    public static void invoke(Context context, CourseArrangeBean courseArrangeBean) {
        Intent intent = new Intent(context, MainDetailActivity.class);
        intent.putExtra("COURSE_ARRANGE", courseArrangeBean);
        context.startActivity(intent);
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
                    params.setMarginStart(ScreenUtils.dpToPxInt(this, marginLeft));
                    params.setMarginEnd(ScreenUtils.dpToPxInt(this, marginRight));
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
        finish();
    }
}
