package com.yanxiu.gphone.faceshowadmin_android.task;

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
import com.yanxiu.gphone.faceshowadmin_android.utils.ScreenUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoteDetailActivity extends FaceShowBaseActivity {

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
        mTitleLayoutTitle.setText(Html.fromHtml(getString(R.string.vote_person_number_title, getIntent().getIntExtra("submitNum", 0), getIntent().getIntExtra("totalNum", 0))));
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
        VotedFragment votedFragment = new VotedFragment();
        votedFragment.setArguments(bundle);
        NoVoteFragment noVoteFragment = new NoVoteFragment();
        noVoteFragment.setArguments(bundle);
        list.add(votedFragment);
        list.add(noVoteFragment);
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
                    return getString(R.string.voted);
                } else {
                    return getString(R.string.no_vote);
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
                    params.setMarginStart(ScreenUtils.dpToPxInt(VoteDetailActivity.this, marginLeft));
                    params.setMarginEnd(ScreenUtils.dpToPxInt(VoteDetailActivity.this, marginRight));
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
