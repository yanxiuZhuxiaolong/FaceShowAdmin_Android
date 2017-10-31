package com.yanxiu.gphone.faceshowadmin_android.login.activity;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.MainActivity;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.net.login.GetUserInfoRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.login.GetUserInfoResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.Utils;
import com.yanxiu.gphone.faceshowadmin_android.utils.permission.OnPermissionCallback;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 欢迎页面
 * Created by 戴延枫 on 2017/9/14.
 */

public class WelcomeActivity extends FaceShowBaseActivity {

    /**
     * add LOAD_TIME and change time
     * cwq
     */
    private static final int LOAD_TIME = 400;
    private Handler mHandler;
    private final static int GO_LOGIN = 0x0001;
    private final static int GO_MAIN = 0x0002;

    private ImageView mImgLogo;
    private static boolean isAnimationEnd = false;
    private static boolean isCanLogin = false;
    private static boolean isGetUserInfoEnd = false;
    private Animator.AnimatorListener logoAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            isAnimationEnd = false;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            isAnimationEnd = true;
            if (isGetUserInfoEnd) {
                if (SpManager.getCurrentClassInfo() == null) {
                    ClassManageActivity.toThisAct(WelcomeActivity.this, SpManager.getClassListInfo());
                } else {
                    MainActivity.invoke(WelcomeActivity.this, SpManager.getCurrentClassInfo());
                }
                WelcomeActivity.this.finish();
            }
            if (isCanLogin) {
                LoginActivity.toThisAct(WelcomeActivity.this);
                WelcomeActivity.this.finish();
            }

        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        String[] perms = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};
        requestPermissions(perms, new OnPermissionCallback() {
            @Override
            public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                /*欢迎页logo的动画效果*/
                int ANIMATION_DURATION = 1000;//动画时长
                mImgLogo.animate().translationY(-Utils.dip2px(getApplicationContext(), 375)).setDuration(ANIMATION_DURATION).setListener(logoAnimatorListener);
                checkUserStatus();
            }

            @Override
            public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                WelcomeActivity.this.finish();
            }
        });

    }

    private void initView() {
        mImgLogo = findViewById(R.id.img_logo);
        mHandler = new WelcomeHandler(this);

    }

    /**
     * 检查用户
     */
    private void checkUserStatus() {
        if (TextUtils.isEmpty(SpManager.getToken())) {
            //用户信息不完整,跳转登录页
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, LOAD_TIME);
        } else {
            //用户信息完整，跳转首页
            mHandler.sendEmptyMessageDelayed(GO_MAIN, LOAD_TIME);
        }
    }

    private static class WelcomeHandler extends Handler {

        private WeakReference<WelcomeActivity> mActivity;

        WelcomeHandler(WelcomeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final WelcomeActivity activity = mActivity.get();

            switch (msg.what) {
                case GO_LOGIN:
                    isCanLogin = true;
                    if (isAnimationEnd) {
                        LoginActivity.toThisAct(activity);
                        activity.finish();
                    }
                    break;
                case GO_MAIN:
                    //获取用户基本信息
                    getUserInfo(activity);
                    break;
            }
        }
    }

    private static void getUserInfo(final Activity activity) {
        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
        getUserInfoRequest.startRequest(GetUserInfoResponse.class, new HttpCallback<GetUserInfoResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetUserInfoResponse ret) {
                if (ret.getCode() == 0) {
                    UserInfo.getInstance().setInfo(ret.getData());
                } else {
                    UserInfo.getInstance().setInfo(SpManager.getUserInfo());
                }
                if (isAnimationEnd) {
                    // TODO: 17-10-30
                    isToClassInfoOrMainAct(activity);
                    activity.finish();
                }
                isGetUserInfoEnd = true;
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                UserInfo.getInstance().setInfo(SpManager.getUserInfo());
                if (isAnimationEnd) {
                    // TODO: 17-10-30
                    isToClassInfoOrMainAct(activity);
                    activity.finish();
                }
                isGetUserInfoEnd = true;

            }
        });
    }

    private static void isToClassInfoOrMainAct(Activity activity) {
        if (SpManager.getCurrentClassInfo() == null) {
            ClassManageActivity.toThisAct(activity, SpManager.getClassListInfo());
        } else {
            MainActivity.invoke(activity, SpManager.getCurrentClassInfo());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //退出程序
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}