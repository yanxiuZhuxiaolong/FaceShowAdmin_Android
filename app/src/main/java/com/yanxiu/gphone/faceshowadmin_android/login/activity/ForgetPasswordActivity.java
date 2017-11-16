package com.yanxiu.gphone.faceshowadmin_android.login.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.ClearEditText;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseCallback;
import com.yanxiu.gphone.faceshowadmin_android.net.login.GetVerificationCodeRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.login.GetVerificationCodeResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.login.ModifyPasswordRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.login.ModifyPasswordResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.Utils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.edt_phone_number)
    ClearEditText edtPhoneNumber;
    @BindView(R.id.tv_get_verification_code)
    TextView tvGetVerificationCode;
    @BindView(R.id.edt_verification_code)
    ClearEditText edtVerificationCode;
    @BindView(R.id.edt_input_new_password)
    EditText edtInputNewPassword;
    @BindView(R.id.forget_password_sure)
    TextView forgetPasswordSure;
    PublicLoadLayout publicLoadLayout;
    @BindView(R.id.img_show_password)
    ImageView mImgShowPassword;

    private boolean isPhoneNumber = false;
    private boolean isVerificationCodeNull = true;
    private boolean isNewPasswordNull = true;
    private boolean isPasswordShow = false;

    private int time = 60;
    private MyHandler handler;

    public void dealMessage() {
        time--;
        tvGetVerificationCode.setText(getString(R.string.time_count, time));
        tvGetVerificationCode.setTextSize(19);
        if (time == 0) {
            time = 60;
            if (Utils.isMobileNO(edtPhoneNumber.getText().toString())) {
                isPhoneNumber = true;
                tvGetVerificationCode.setTextColor(Color.parseColor("#0068bd"));
            } else {
                isPhoneNumber = false;
                tvGetVerificationCode.setTextColor(Color.parseColor("#999999"));
            }
            tvGetVerificationCode.setTextSize(14);
            tvGetVerificationCode.setText(R.string.get_verification_code);
        } else {
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    }


    static class MyHandler extends Handler {
        WeakReference<ForgetPasswordActivity> mActivityWeakReference;

        public MyHandler(ForgetPasswordActivity activityWeakReference) {
            mActivityWeakReference = new WeakReference<>(activityWeakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mActivityWeakReference.get().dealMessage();
        }
    }


    private TextWatcher mPhoneNumberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (Utils.isMobileNO(s)) {
                isPhoneNumber = true;
                tvGetVerificationCode.setTextColor(Color.parseColor("#0068bd"));
            } else {
                isPhoneNumber = false;
                tvGetVerificationCode.setTextColor(Color.parseColor("#999999"));
            }
            changSureBtnColor();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher mVerificationCodeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isVerificationCodeNull = TextUtils.isEmpty(s);
            changSureBtnColor();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher mNewPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isNewPasswordNull = s.length() > 20 || s.length() < 6;
            changSureBtnColor();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 是否可以点击确定按钮
     */
    private boolean isCanSure() {
        return isPhoneNumber && !isNewPasswordNull && !isVerificationCodeNull;
    }

    private void changSureBtnColor() {
        if (isCanSure()) {
            forgetPasswordSure.setBackgroundResource(R.drawable.selector_can_commit_new_password_bg);
        } else {
            forgetPasswordSure.setBackgroundResource(R.drawable.shape_cannot_commit_new_password_bg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicLoadLayout = new PublicLoadLayout(this);
        publicLoadLayout.setContentView(R.layout.activity_forget_password);
        setContentView(publicLoadLayout);
        ButterKnife.bind(this);
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutTitle.setVisibility(View.VISIBLE);
        titleLayoutTitle.setText(R.string.forget_password_title);
        tvGetVerificationCode.setTextColor(Color.parseColor("#999999"));
        edtPhoneNumber.addTextChangedListener(mPhoneNumberTextWatcher);
        edtVerificationCode.addTextChangedListener(mVerificationCodeTextWatcher);
        edtInputNewPassword.addTextChangedListener(mNewPasswordTextWatcher);


    }


    @OnClick({R.id.title_layout_left_img, R.id.tv_get_verification_code, R.id.forget_password_sure, R.id.img_show_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.tv_get_verification_code:
                if (isPhoneNumber) {
                    getVerificationCode();
                } else {
                    ToastUtil.showToast(getApplicationContext(), "请填入正确的手机号");
                }
                break;
            case R.id.forget_password_sure:
                if (isCanSure()) {
                    resetNewPassword();
                } else {
                    if (!isPhoneNumber) {
                        ToastUtil.showToast(getApplicationContext(), "请填入正确的手机号");
                    } else if (isVerificationCodeNull) {
                        ToastUtil.showToast(getApplicationContext(), "验证码不能为空");
                    } else if (isNewPasswordNull) {
                        ToastUtil.showToast(getApplicationContext(), R.string.please_input_new_password);
                    }
                }
                break;
            case R.id.img_show_password:

                if (isPasswordShow) {
                    edtInputNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mImgShowPassword.setImageDrawable(ContextCompat.getDrawable(ForgetPasswordActivity.this, R.drawable.selector_show_password));
                    isPasswordShow = false;
                } else {
                    edtInputNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mImgShowPassword.setImageDrawable(ContextCompat.getDrawable(ForgetPasswordActivity.this, R.drawable.selector_hide_password));
                    isPasswordShow = true;
                }
                break;
            default:
        }
    }

    private void resetNewPassword() {
        final ModifyPasswordRequest modifyPasswordRequest = new ModifyPasswordRequest();
        modifyPasswordRequest.mobile = edtPhoneNumber.getText().toString();
        modifyPasswordRequest.code = edtVerificationCode.getText().toString();
        modifyPasswordRequest.password = Utils.MD5Helper(edtInputNewPassword.getText().toString());
        publicLoadLayout.showLoadingView();
        modifyPasswordRequest.startRequest(ModifyPasswordResponse.class, new FaceShowBaseCallback<ModifyPasswordResponse>() {
            @Override
            protected void onResponse(RequestBase request, ModifyPasswordResponse response) {
                publicLoadLayout.hiddenLoadingView();
                if (response.getCode() == 0) {
                    ToastUtil.showToast(getApplicationContext(), getString(R.string.reset_password_success));
                    Intent intent = new Intent();
                    intent.putExtra("password", edtInputNewPassword.getText().toString());
                    intent.putExtra("phoneNumber", modifyPasswordRequest.mobile);
                    ForgetPasswordActivity.this.setResult(RESULT_OK, intent);
                    ForgetPasswordActivity.this.finish();
                } else {
                    ToastUtil.showToast(getApplicationContext(), response.getMessage());
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                publicLoadLayout.hiddenLoadingView();
                ToastUtil.showToast(getApplicationContext(), error.getMessage());
            }
        });


    }

    private void getVerificationCode() {
        publicLoadLayout.showLoadingView();
        GetVerificationCodeRequest getVerificationCodeRequest = new GetVerificationCodeRequest();
        getVerificationCodeRequest.mobile = edtPhoneNumber.getText().toString();
        getVerificationCodeRequest.startRequest(GetVerificationCodeResponse.class, new FaceShowBaseCallback<GetVerificationCodeResponse>() {
            @Override
            protected void onResponse(RequestBase request, GetVerificationCodeResponse response) {
                publicLoadLayout.hiddenLoadingView();
                if (response.getCode() == 0) {
                    handler = new MyHandler(ForgetPasswordActivity.this);
                    handler.sendEmptyMessage(1);
                    tvGetVerificationCode.setBackground(null);
                    ToastUtil.showToast(getApplicationContext(), response.getMessage());
                } else {
                    ToastUtil.showToast(getApplicationContext(), response.getMessage());
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                publicLoadLayout.hiddenLoadingView();
                ToastUtil.showToast(getApplicationContext(), error.getMessage());

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && handler.hasMessages(1)) {
            handler.removeMessages(1);
            handler = null;
        }


    }
}
