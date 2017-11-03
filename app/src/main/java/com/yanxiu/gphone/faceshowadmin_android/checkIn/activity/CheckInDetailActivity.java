package com.yanxiu.gphone.faceshowadmin_android.checkIn.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.fragment.NoSignInFragment;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.fragment.SignedInFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created  by fengrongcheng
 */
public class CheckInDetailActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_right_img)
    ImageView titleLayoutRightImg;
    @BindView(R.id.tv_check_in_title)
    TextView tvCheckInTitle;
    @BindView(R.id.tv_check_in_time)
    TextView tvCheckInTime;
    @BindView(R.id.tv_attendance_number)
    TextView tvAttendanceNumber;
    @BindView(R.id.tv_check_in_percentage)
    TextView tvCheckInPercentage;
    @BindView(R.id.bottom_of_head)
    RelativeLayout bottomOfHead;
    @BindView(R.id.img_code)
    ImageView imgCode;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_detail);
        ButterKnife.bind(this);
        initTitle();
        initHead();
        initTabLayout();
        final List<Fragment> list = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putString("stepId", getIntent().getStringExtra("stepId"));
        SignedInFragment signedInFragment = new SignedInFragment();
        signedInFragment.setArguments(bundle);
        NoSignInFragment noSignInFragment = new NoSignInFragment();
        noSignInFragment.setArguments(bundle);
        list.add(noSignInFragment);
        list.add(signedInFragment);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
                    return getString(R.string.no_sign_in);
                } else {
                    return getString(R.string.signed_in);
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager, true);
    }

    private void initTitle() {
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutTitle.setText(R.string.check_in_detail);
    }

    private void initHead() {
        tvCheckInTitle.setText(getIntent().getStringExtra("title"));
        tvCheckInTime.setText(getIntent().getStringExtra("time"));
        tvAttendanceNumber.setText(Html.fromHtml(getString(R.string.attendance_number, getIntent().getStringExtra("proportion"))));
        tvCheckInPercentage.setText(Html.fromHtml(getString(R.string.check_in_percentage_new, getIntent().getStringExtra("percentage"))));

    }


    private void initTabLayout() {
        tabLayout.setTabTextColors(R.color.color_333333, R.color.color_999999);
    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_img, R.id.img_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.title_layout_right_img:
                break;
            case R.id.img_code:
                Intent intent = new Intent(CheckInDetailActivity.this, QrCodeShowActivity.class);
                intent.putExtra("stepId", getIntent().getStringExtra("stepId"));
                intent.putExtra("qrCodeRefreshRate", getIntent().getIntExtra("qrCodeRefreshRate", 0));
                startActivity(intent);
                break;
            default:
        }
    }
}
