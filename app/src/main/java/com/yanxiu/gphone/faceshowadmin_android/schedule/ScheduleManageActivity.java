package com.yanxiu.gphone.faceshowadmin_android.schedule;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.FSAApplication;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.schedule.ScheduleDeleteRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.schedule.ScheduleDeleteResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.schedule.ScheduleRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.schedule.ScheduleResponse;
import com.yanxiu.gphone.faceshowadmin_android.schedule.bean.ScheduleBean;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdate;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.YXPictureManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 日程管理页面
 */
public class ScheduleManageActivity extends FaceShowBaseActivity {

    public static final String SCHEDULE_BEAN = "schedule_bean";

    private PublicLoadLayout mRootView;

    @BindView(R.id.title_layout_left_img)
    ImageView title_layout_left_img;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_right_img)
    ImageView title_layout_right_img;

    @BindView(R.id.schedule_text)
    TextView schedule_text;
    @BindView(R.id.schedule_img)
    ImageView schedule_img;

    private PopupWindow mPopupWindow;

    private ScheduleBean mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_schedule_manage);
        setContentView(mRootView);
        ButterKnife.bind(this);
        initView();
        mData = (ScheduleBean) getIntent().getSerializableExtra(SCHEDULE_BEAN);
        if (mData != null) {
            setData();
        } else {
            requestData();
        }
    }

    private void initView() {
        titleLayoutTitle.setText(R.string.manager_schedule);
        title_layout_left_img.setVisibility(View.VISIBLE);
        title_layout_right_img.setVisibility(View.VISIBLE);
        title_layout_right_img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selector_notice_detil_more));
    }

    private void setData() {
        schedule_text.setText(mData.getSubject());
        YXPictureManager.getInstance().showPic(this, mData.getImageUrl(), schedule_img);
//        YXPictureManager.getInstance().showPic(this, "http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg", schedule_img);
    }

    @OnClick({R.id.title_layout_right_img, R.id.retry_button, R.id.title_layout_left_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_right_img:
                showCancelPopupWindow();
                break;
            case R.id.retry_button:
                requestData();
                break;
            case R.id.title_layout_left_img:
                finish();
                break;
            default:
        }
    }

    /**
     * 获取日程信息
     */
    private void requestData() {
        mRootView.showLoadingView();
        ScheduleRequest scheduleRequest = new ScheduleRequest();
        scheduleRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        scheduleRequest.startRequest(ScheduleResponse.class, new HttpCallback<ScheduleResponse>() {
            @Override
            public void onSuccess(RequestBase request, ScheduleResponse ret) {
                mRootView.hiddenLoadingView();
                if (ret != null && ret.getCode() == 0 && ret.getSchedule() != null) {
                    mData = ret.getSchedule();
                    setData();
                } else {
                    mRootView.finish();
                    PublishScheduleActivity.invoke(ScheduleManageActivity.this);
                    finish();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.showNetErrorView();
                if (error != null) {
                    ToastUtil.showToast(FSAApplication.getInstance().getApplicationContext(), error.getMessage());
                }
            }
        });

    }

    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, ScheduleManageActivity.class);
        activity.startActivity(intent);
    }

    public static void invoke(Activity activity, ScheduleBean bean) {
        Intent intent = new Intent(activity, ScheduleManageActivity.class);
        intent.putExtra(SCHEDULE_BEAN, bean);
        activity.startActivity(intent);
    }

    public static void invoke(Activity activity, int result_code) {
        activity.setResult(result_code);
        activity.finish();
    }

    private void showCancelPopupWindow() {
        if (mPopupWindow == null) {
            View pop = LayoutInflater.from(this).inflate(R.layout.popupwindow_schedule, null);
            (pop.findViewById(R.id.notice_update)).setOnClickListener(popupWindowClickListener);
            (pop.findViewById(R.id.notice_delete)).setOnClickListener(popupWindowClickListener);
            (pop.findViewById(R.id.notice_cancel)).setOnClickListener(popupWindowClickListener);
            mPopupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setAnimationStyle(R.style.pop_anim);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        }
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void dismissPopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    View.OnClickListener popupWindowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.notice_update:
                    UpdateScheduleActivity.invokeUpdate(ScheduleManageActivity.this, mData);
                    dismissPopupWindow();
                    break;
                case R.id.notice_delete:
                    EventUpdate.onDeleteCalendar(ScheduleManageActivity.this);
                    requestDeleteSchedule();
                    dismissPopupWindow();
                    break;
                case R.id.notice_cancel:
                    dismissPopupWindow();
                    break;
            }

        }
    };

    private void requestDeleteSchedule() {
        mRootView.showLoadingView();
        ScheduleDeleteRequest scheduleDeleteRequest = new ScheduleDeleteRequest();
        scheduleDeleteRequest.scheduleId = mData.getId();
        scheduleDeleteRequest.startRequest(ScheduleDeleteResponse.class, new HttpCallback<ScheduleDeleteResponse>() {
            @Override
            public void onSuccess(RequestBase request, ScheduleDeleteResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    ToastUtil.showToast(ScheduleManageActivity.this, "删除成功");
                    finish();
                } else {
                    ToastUtil.showToast(ScheduleManageActivity.this, ret.getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.finish();
                ToastUtil.showToast(ScheduleManageActivity.this, "删除失败");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UpdateScheduleActivity.RESULT_UPDATE:
                requestData();
                break;
        }

    }

}
