package com.yanxiu.gphone.faceshowadmin_android.checkIn.activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.fragment.NoSignInFragment;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.fragment.SignedInFragment;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.DeleteCheckInRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.DeleteCheckInResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetCheckInDetailRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetCheckInDetailResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.DateFormatUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdata;
import com.yanxiu.gphone.faceshowadmin_android.utils.ScreenUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
    @BindView(R.id.title_layout_right_txt)
    TextView mTitleLayoutRightTxt;
    private PublicLoadLayout mPublicLoadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPublicLoadLayout = new PublicLoadLayout(this);
        mPublicLoadLayout.setContentView(R.layout.activity_check_in_detail);
        setContentView(mPublicLoadLayout);
        ButterKnife.bind(this);
        initTitle();
        initHead();
        initTabLayout();

    }

    private void initTitle() {
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutTitle.setText(R.string.check_in_detail);
        mTitleLayoutRightTxt.setVisibility(View.VISIBLE);
        mTitleLayoutRightTxt.setText("删除");

    }

    private void initHead() {
        if (TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            getCheckInDetail();
        } else {
            tvCheckInTitle.setText(getIntent().getStringExtra("title"));
            tvCheckInTime.setText(getIntent().getStringExtra("time"));
            tvAttendanceNumber.setText(Html.fromHtml(getString(R.string.attendance_number, getIntent().getStringExtra("proportion"))));
            tvCheckInPercentage.setText(Html.fromHtml(getString(R.string.check_in_percentage_new, getIntent().getStringExtra("percentage"))));
        }
    }

    private List<Fragment> list = new ArrayList<>();


    private void initTabLayout() {

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
        tabLayout.setupWithViewPager(viewPager);
        setUpIndicatorWidth(tabLayout, 20, 20);
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
                    params.setMarginStart(ScreenUtils.dpToPxInt(CheckInDetailActivity.this, marginLeft));
                    params.setMarginEnd(ScreenUtils.dpToPxInt(CheckInDetailActivity.this, marginRight));
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_img, R.id.img_code, R.id.title_layout_right_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                onBackPressed();
                break;
            case R.id.title_layout_right_img:
                break;
            case R.id.img_code:
                Intent intent = new Intent(CheckInDetailActivity.this, QrCodeShowActivity.class);
                intent.putExtra("stepId", getIntent().getStringExtra("stepId"));
                intent.putExtra("qrCodeRefreshRate", getIntent().getIntExtra("qrCodeRefreshRate", 0));
                startActivity(intent);
                break;
            case R.id.title_layout_right_txt:
                toDeleteThisSignIn();
                EventUpdata.onDeleteSignIn(this);
                break;
            default:
        }
    }

    private void toDeleteThisSignIn() {
        mPublicLoadLayout.showLoadingView();
        DeleteCheckInRequest deleteCheckInRequest = new DeleteCheckInRequest();
        deleteCheckInRequest.stepId = getIntent().getStringExtra("stepId");
        deleteCheckInRequest.startRequest(DeleteCheckInResponse.class, new HttpCallback<DeleteCheckInResponse>() {
            @Override
            public void onSuccess(RequestBase request, DeleteCheckInResponse ret) {
                mPublicLoadLayout.finish();
                if (ResponseConfig.SUCCESS == ret.getCode()) {
                    setResult(RESULT_OK);
                    CheckInDetailActivity.this.finish();
                } else {
                    ToastUtil.showToast(getApplicationContext(), ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.finish();
                ToastUtil.showToast(getApplicationContext(), error.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (((NoSignInFragment) list.get(0)).mNeedToRefreshParentActivity) {
            this.setResult(RESULT_OK);
        }
        this.finish();
        super.onBackPressed();
    }

    public void getCheckInDetail() {
        GetCheckInDetailRequest getCheckInDetailRequest = new GetCheckInDetailRequest();
        getCheckInDetailRequest.stepId = getIntent().getStringExtra("stepId");
        getCheckInDetailRequest.startRequest(GetCheckInDetailResponse.class, new HttpCallback<GetCheckInDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetCheckInDetailResponse ret) {
                if (ret.getCode() == ResponseConfig.SUCCESS && ret.getData() != null && ret.getData().getSignIn() != null) {
                    tvCheckInTitle.setText(ret.getData().getSignIn().getTitle());
                    final String time;
                    if (ret.getData().getSignIn().getStartTime() == null || ret.getData().getSignIn().getEndTime() == null) {
                        time = "";
                    } else {
                        String translationTime = DateFormatUtil.translationBetweenTwoFormat(ret.getData().getSignIn().getStartTime(), DateFormatUtil.FORMAT_ONE, DateFormatUtil.FORMAT_TWO);
                        String[] startTimes = translationTime.split(" ");
                        String[] startTime = startTimes[1].split(":");
                        String[] endTime = ret.getData().getSignIn().getEndTime().split(" ")[1].split(":");
                        time = startTimes[0] + " " + startTime[0] + ":" + startTime[1] + "-" + endTime[0] + ":" + endTime[1];
                    }
                    tvCheckInTime.setText(time);
                    final String proportion = getString(R.string.has_check_in_proportion, ret.getData().getSignIn().getSignInUserNum(), ret.getData().getSignIn().getTotalUserNum());
                    tvAttendanceNumber.setText(Html.fromHtml(getString(R.string.attendance_number, proportion)));
                    tvCheckInPercentage.setText(Html.fromHtml(getString(R.string.check_in_percentage_new, getPercent(ret.getData().getSignIn().getSignInUserNum(), ret.getData().getSignIn().getTotalUserNum()))));
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                ToastUtil.showToast(getApplicationContext(), error.getMessage());
            }
        });
    }

    String getPercent(int y, int z) {
        if (y == 0) {
            return "0%";
        } else {
            String baifenbi;// 接受百分比的值
            double baiy = y * 1.0;
            double baiz = z * 1.0;
            double fen = baiy / baiz;
            // NumberFormat nf = NumberFormat.getPercentInstance(); 注释掉的也是一种方法
            // nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位
            DecimalFormat df1 = new DecimalFormat("##%"); // ##.00%
            // 百分比格式，后面不足2位的用0补齐
            // baifenbi=nf.format(fen);
            baifenbi = df1.format(fen);
            System.out.println(baifenbi);
            return baifenbi;
        }
    }

}
