package com.yanxiu.gphone.faceshowadmin_android.checkIn;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

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
        setContentView(R.layout.activity_create__new__check_in);
        ButterKnife.bind(this);
        initTitle();
        checkInName.addTextChangedListener(textWatcher);
        checkInSuccessToast.addTextChangedListener(textWatcher);
        tvCheckInEndTime.addTextChangedListener(textWatcher);
        tvCheckInStartTime.addTextChangedListener(textWatcher);
        tvCheckInTime.addTextChangedListener(textWatcher);
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
                break;
            case R.id.rl_check_in_start_time:
                break;
            case R.id.rl_check_in_end_time:
                break;

        }
    }

    private void toCommit() {
        // TODO: 17-10-31
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
}
