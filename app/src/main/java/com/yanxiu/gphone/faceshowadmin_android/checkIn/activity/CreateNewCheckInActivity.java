package com.yanxiu.gphone.faceshowadmin_android.checkIn.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.suke.widget.SwitchButton;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.CreateNewCheckInRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.CreateNewCheckInResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by fengrongcheng
 */
public class CreateNewCheckInActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_right_txt)
    TextView titleLayoutRightTxt;
    @BindView(R.id.check_in_name)
    EditText checkInName;
    @BindView(R.id.rl_check_in_time)
    RelativeLayout rlCheckInTime;
    @BindView(R.id.rl_check_in_start_time)
    RelativeLayout rlCheckInStartTime;
    @BindView(R.id.rl_check_in_end_time)
    RelativeLayout rlCheckInEndTime;
    @BindView(R.id.rl_useful)
    RelativeLayout rlUseful;
    @BindView(R.id.rl_use_code)
    RelativeLayout rlUseCode;
    @BindView(R.id.switch_btn_useful)
    SwitchButton switchBtnUseful;
    @BindView(R.id.switch_btn_use_code)
    SwitchButton switchBtnUseCode;
    @BindView(R.id.tv_check_in_time)
    TextView tvCheckInTime;
    @BindView(R.id.tv_check_in_start_time)
    TextView tvCheckInStartTime;
    @BindView(R.id.tv_check_in_end_time)
    TextView tvCheckInEndTime;
    @BindView(R.id.check_in_success_toast)
    EditText checkInSuccessToast;

    private PublicLoadLayout publicLoadLayout;
    private Date startTime;
    private UUID requestUUID;
    InputMethodManager imm = null;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            changeCommitButtonColor();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicLoadLayout = new PublicLoadLayout(this);
        publicLoadLayout.setContentView(R.layout.activity_create__new__check_in);
        setContentView(publicLoadLayout);
        ButterKnife.bind(this);
        initTitle();
        checkInName.addTextChangedListener(textWatcher);
        checkInSuccessToast.addTextChangedListener(textWatcher);
        tvCheckInEndTime.addTextChangedListener(textWatcher);
        tvCheckInStartTime.addTextChangedListener(textWatcher);
        tvCheckInTime.addTextChangedListener(textWatcher);
        imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    }

    private void initTitle() {
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutTitle.setText(R.string.create_new_check_in);
        titleLayoutTitle.setVisibility(View.VISIBLE);
        titleLayoutRightTxt.setText(R.string.commit_new_check_in);
        titleLayoutRightTxt.setTextColor(Color.parseColor("#CCCCCC"));
        titleLayoutRightTxt.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_txt, R.id.rl_check_in_time, R.id.rl_check_in_start_time, R.id.rl_check_in_end_time})
    public void onViewClicked(View view) {
        if (imm == null) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                CreateNewCheckInActivity.this.finish();
                break;
            case R.id.title_layout_right_txt:
                if (canCommit()) {
                    toCommit();
                }
                break;
            case R.id.rl_check_in_time:
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                toSelectedData();
                break;
            case R.id.rl_check_in_start_time:
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                toSelectedStartTime();
                break;
            case R.id.rl_check_in_end_time:
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                toSelectedEndTime();
                break;
            default:
        }


    }

    private void toSelectedData() {
        TimePickerView timePickerView = new TimePickerView.Builder(CreateNewCheckInActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvCheckInTime.setText(getDay(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        timePickerView.show();
    }

    private void toSelectedStartTime() {
        TimePickerView timePickerView = new TimePickerView.Builder(CreateNewCheckInActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                startTime = date;
                tvCheckInStartTime.setText(getTime(date));
            }
        })
                .setType(new boolean[]{false, false, false, true, true, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        timePickerView.show();
    }

    private void toSelectedEndTime() {
        TimePickerView timePickerView = new TimePickerView.Builder(CreateNewCheckInActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (date.compareTo(startTime) > 0) {
                    tvCheckInEndTime.setText(getTime(date));
                } else {
                    ToastUtil.showToast(getApplicationContext(), getString(R.string.end_time_must_be_greater_than_start_time));
                }
            }
        })
                .setType(new boolean[]{false, false, false, true, true, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        timePickerView.show();
    }

    private String getDay(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd ", Locale.getDefault());
        return format.format(date);
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return format.format(date);
    }

    private void toCommit() {
        publicLoadLayout.showLoadingView();
        CreateNewCheckInRequest createNewCheckInRequest = new CreateNewCheckInRequest();
        createNewCheckInRequest.title = checkInName.getText().toString();
        createNewCheckInRequest.startTime = tvCheckInTime.getText().toString() + " " + tvCheckInStartTime.getText().toString();
        createNewCheckInRequest.endTime = tvCheckInTime.getText().toString() + " " + tvCheckInEndTime.getText().toString();
        createNewCheckInRequest.successPrompt = checkInSuccessToast.getText().toString();
        createNewCheckInRequest.antiCheat = switchBtnUseful.isChecked() ? "1" : "0";
        createNewCheckInRequest.qrcodeRefreshRate = switchBtnUseCode.isChecked() ? "1" : "0";
        createNewCheckInRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        requestUUID = createNewCheckInRequest.startRequest(CreateNewCheckInResponse.class, new HttpCallback<CreateNewCheckInResponse>() {
            @Override
            public void onSuccess(RequestBase request, CreateNewCheckInResponse ret) {
                publicLoadLayout.hiddenLoadingView();
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    CreateNewCheckInActivity.this.setResult(RESULT_OK);
                    CreateNewCheckInActivity.this.finish();
                    ToastUtil.showToast(getApplicationContext(), ret.getMessage());
                } else {
                    ToastUtil.showToast(getApplicationContext(), ret.getMessage());
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                publicLoadLayout.hiddenLoadingView();
                ToastUtil.showToast(getApplicationContext(), error.getMessage());

            }
        });
    }

    private boolean canCommit() {
        if (TextUtils.isEmpty(checkInName.getText().toString())) {
            ToastUtil.showToast(getApplicationContext(), getString(R.string.check_in_title_can_not_be_null));
            return false;

        } else if (TextUtils.isEmpty(checkInSuccessToast.getText().toString())) {
            ToastUtil.showToast(getApplicationContext(), getString(R.string.check_in_success_toast_can_not_be_null));
            return false;

        } else if (TextUtils.isEmpty(tvCheckInTime.getText().toString())) {
            ToastUtil.showToast(getApplicationContext(), getString(R.string.check_in_time_can_not_be_null));
            return false;

        } else if (TextUtils.isEmpty(tvCheckInStartTime.getText().toString())) {
            ToastUtil.showToast(getApplicationContext(), getString(R.string.check_in_start_time_can_not_be_null));
            return false;

        } else if (TextUtils.isEmpty(tvCheckInEndTime.getText().toString())) {
            ToastUtil.showToast(getApplicationContext(), getString(R.string.check_in_end_title_can_not_be_null));
            return false;
        } else {
            return true;
        }
    }

    private boolean canCommitWithOutToast() {
        return !TextUtils.isEmpty(checkInName.getText().toString()) && !TextUtils.isEmpty(checkInSuccessToast.getText().toString())
                && !TextUtils.isEmpty(tvCheckInTime.getText().toString())
                && !TextUtils.isEmpty(tvCheckInStartTime.getText().toString())
                && !TextUtils.isEmpty(tvCheckInEndTime.getText().toString());
    }

    private void changeCommitButtonColor() {
        if (canCommitWithOutToast()) {
            titleLayoutRightTxt.setTextColor(Color.parseColor("#0088BD"));
        } else {
            titleLayoutRightTxt.setTextColor(Color.parseColor("#CCCCCC"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestUUID != null) {
            {
                RequestBase.cancelRequestWithUUID(requestUUID);
            }
        }
    }
}
