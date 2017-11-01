package com.yanxiu.gphone.faceshowadmin_android.login.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.FSAApplication;
import com.yanxiu.gphone.faceshowadmin_android.MainActivity;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.GetClazzListRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.GetClazzListResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.login.GetUserInfoRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.login.GetUserInfoResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.login.SignInRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.login.SignInResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.Utils;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 登录页面
 * created by frc
 */
public class LoginActivity extends FaceShowBaseActivity {

    private static final int TO_FORGET_PASSWORD_REQUEST_CODE = 0X01;

    private Unbinder unbinder;
    private Context mContext;
    private PublicLoadLayout rootView;
    @BindView(R.id.edt_account_number)
    EditText edt_account_number;
    @BindView(R.id.edt_account_password)
    EditText edt_account_password;
    @BindView(R.id.img_show_password)
    ImageView img_show_password;
    @BindView(R.id.tv_sign_in)
    TextView tv_sign_in;
    @BindView(R.id.tv_forget_password)
    TextView tv_forget_password;
    private boolean isPasswordShow = false;
    private UUID mSignInRequestUUID;
    private String token;
    private String passPort;

    public static void toThisAct(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_login);
        setContentView(rootView);
//    如果修改状态栏则ｓｃｒｏｌｌＶｉｅｗ失效    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            StatusBarUtils.setStatusBarFullScreen(LoginActivity.this);
//        }
        unbinder = ButterKnife.bind(this);
        if (SpManager.isFirstStartUp()) {
            SpManager.setFirstStartUp(false);
        }

        edt_account_number.addTextChangedListener(accountNumberChangedListener);
        edt_account_password.addTextChangedListener(accountPasswordChangedListener);
        if (edt_account_number.getText().length() > 0 && edt_account_password.getText().length() > 0) {
            tv_sign_in.setBackgroundResource(R.drawable.selector_sign_in_bg);
        } else {
            tv_sign_in.setBackgroundResource(R.drawable.shape_sign_in_normal_bg);
        }
    }

    private TextWatcher accountNumberChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() > 0 && edt_account_password.getText().length() > 0) {
                tv_sign_in.setBackgroundResource(R.drawable.selector_sign_in_bg);
            } else {
                tv_sign_in.setBackgroundResource(R.drawable.shape_sign_in_normal_bg);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    private TextWatcher accountPasswordChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() > 0 && edt_account_number.getText().length() > 0) {
                tv_sign_in.setBackgroundResource(R.drawable.selector_sign_in_bg);
            } else {
                tv_sign_in.setBackgroundResource(R.drawable.shape_sign_in_normal_bg);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mSignInRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mSignInRequestUUID);
        }
    }

    @OnClick({R.id.img_show_password, R.id.tv_sign_in, R.id.tv_forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_show_password:
                if (isPasswordShow) {
                    edt_account_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_show_password.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.selector_show_password));
                    isPasswordShow = false;
                } else {
                    edt_account_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_show_password.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.selector_hide_password));
                    isPasswordShow = true;
                }
                break;
            case R.id.tv_sign_in:
                if (edt_account_number.getText().length() <= 0) {
                    Toast.makeText(mContext, R.string.account_name_can_not_be_null, Toast.LENGTH_SHORT).show();
                } else if (edt_account_password.getText().length() <= 0) {
                    Toast.makeText(mContext, R.string.account_password_can_not_be_null, Toast.LENGTH_SHORT).show();
                } else {
                    signInRequest();
                }

                break;
            case R.id.tv_forget_password:
                startActivityForResult(new Intent(LoginActivity.this, ForgetPasswordActivity.class), TO_FORGET_PASSWORD_REQUEST_CODE);
                break;
                default:
        }
    }


    private void signInRequest() {
        rootView.showLoadingView();
        /* clear some info */
        SpManager.saveToken(null);
        SpManager.saveCurrentClassInfo(null);
        SpManager.saveCurrentClassInfo(null);

        SignInRequest signInRequest = new SignInRequest();
        signInRequest.loginName = edt_account_number.getText().toString();
        signInRequest.password = Utils.MD5Helper(edt_account_password.getText().toString());

        mSignInRequestUUID = signInRequest.startRequest(SignInResponse.class, new HttpCallback<SignInResponse>() {
            @Override
            public void onSuccess(RequestBase request, SignInResponse ret) {

                if (ret.getCode() == 0) {
                    token = ret.getToken();
                    passPort = ret.getPassport();
                    requestClassList(LoginActivity.this);
                } else {
                    Toast.makeText(mContext, ret.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    rootView.hiddenLoadingView();
                    edt_account_password.setText(null);
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                edt_account_password.setText(null);
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getUserInfo(final Activity activity, final GetClazzListResponse.DataBean data) {
        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
        getUserInfoRequest.token = token;
        getUserInfoRequest.startRequest(GetUserInfoResponse.class, new HttpCallback<GetUserInfoResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetUserInfoResponse ret) {
                rootView.hiddenLoadingView();
                if (ret.getCode() == 0) {
                    SpManager.saveToken(token);
                    SpManager.savePassPort(passPort);
                    String userInfoStr = RequestBase.getGson().toJson(ret.getData());
                    SpManager.saveUserInfo(userInfoStr);
                    UserInfo.getInstance().setInfo(ret.getData());
                    SpManager.saveClassListInfo(data);
                    if (data.getClazsInfos().size() == 1) {
                        SpManager.saveCurrentClassInfo(data.getClazsInfos().get(0));
                        MainActivity.invoke(activity, data.getClazsInfos().get(0));
                    } else {
                        ClassManageActivity.toThisAct(activity, data);
                    }
                    LoginActivity.this.finish();
                } else {
                    ToastUtil.showToast(activity, ret.getError().getMessage());
                    rootView.hiddenLoadingView();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(FSAApplication.getInstance().getApplicationContext(), error.getMessage());
            }
        });
    }

    /**
     * 请求项目班级信息，如果code!=0，那么，不能进入首页，并且弹出toast提示
     */
    private void requestClassList(final Activity activity) {
        GetClazzListRequest getClazzListRequest = new GetClazzListRequest();
        getClazzListRequest.token = token;
        getClazzListRequest.startRequest(GetClazzListResponse.class, new HttpCallback<GetClazzListResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetClazzListResponse ret) {
                if (ret != null && ret.getCode() == 0 && ret.getData().getClazsInfos() != null && ret.getData().getClazsInfos().size() > 0) {
                    getUserInfo(activity, ret.getData());
                } else {
                    if (ret != null && ret.getError() != null) {
                        ToastUtil.showToast(FSAApplication.getInstance().getApplicationContext(), ret.getError().getMessage());
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                if (error != null) {
                    ToastUtil.showToast(FSAApplication.getInstance().getApplicationContext(), error.getMessage());
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_FORGET_PASSWORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    edt_account_number.setText(data.getStringExtra("phoneNumber"));
                    edt_account_password.setText(data.getStringExtra("password"));
                }
            }
        }
    }
}
