package com.yanxiu.gphone.faceshowadmin_android.task.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.task.fragment.NoSubmitFragment;
import com.yanxiu.gphone.faceshowadmin_android.task.fragment.SubmittedFragment;
import com.yanxiu.gphone.faceshowadmin_android.utils.ScreenUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubmitDetailActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_detail);
        ButterKnife.bind(this);
        initTitle();
        initTabLayout();
    }

    private void initTitle() {
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
        mTitleLayoutTitle.setText(Html.fromHtml(getString(R.string.submit_person_number_title, getIntent().getIntExtra("submitNum", 0), getIntent().getIntExtra("totalNum", 0))));
        mTitleLayoutTitle.setTextSize(18);
        mTitleLayoutTitle.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.title_layout_left_img)
    public void onViewClicked() {
        this.finish();
    }

    private List<Fragment> list = new ArrayList<>();

    private void initTabLayout() {

        Bundle bundle = new Bundle();
        bundle.putString("stepId", getIntent().getStringExtra("stepId"));
        SubmittedFragment submittedFragment = new SubmittedFragment();
        submittedFragment.setArguments(bundle);
        NoSubmitFragment noSubmitFragment = new NoSubmitFragment();
        noSubmitFragment.setArguments(bundle);
        list.add(submittedFragment);
        list.add(noSubmitFragment);
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
                    return getString(R.string.submitted);
                } else {
                    return getString(R.string.no_submit);
                }
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
                    params.setMarginStart(ScreenUtils.dpToPxInt(SubmitDetailActivity.this, marginLeft));
                    params.setMarginEnd(ScreenUtils.dpToPxInt(SubmitDetailActivity.this, marginRight));
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
