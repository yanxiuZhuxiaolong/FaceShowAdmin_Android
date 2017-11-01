package com.yanxiu.gphone.faceshowadmin_android.checkIn;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;

import java.util.Calendar;
import java.util.Date;

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

    private void toShowTimePickerView() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        final TimePickerView timePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvCheckInTime.setText(date.toString());
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.time_picker_layout, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        v.findViewById(R.id.submit_a_supplement).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                toSubmit();
                            }
                        });
                    }
                })
                .setType(new boolean[]{false, false, false, true, true, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
//        timePickerView.setDate(Calendar.getInstance());
        timePickerView.show();

    }

    private void toSubmit() {

    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.no_sign_in));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.signed_in));
    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_img, R.id.img_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                break;
            case R.id.title_layout_right_img:
                break;
            case R.id.img_code:
                break;
            default:
        }
    }
}
