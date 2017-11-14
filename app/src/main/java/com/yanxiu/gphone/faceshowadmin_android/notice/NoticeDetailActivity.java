package com.yanxiu.gphone.faceshowadmin_android.notice;

import android.app.Activity;
import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeDeleteRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeRequestResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yanxiu.gphone.faceshowadmin_android.notice.NoticeManageActivity.NOTICE_DETAIL;

/**
 * Created by lufengqing on 2017/11/1.
 */

public class NoticeDetailActivity extends FaceShowBaseActivity {
    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_right_img)
    ImageView titleLayoutRightImg;
    @BindView(R.id.detail_title)
    TextView detailTitle;
    @BindView(R.id.notice_create_time)
    TextView noticeCreateTime;
    @BindView(R.id.read_percent)
    TextView readPercent;
    @BindView(R.id.notice_descr)
    TextView noticeDescr;
    @BindView(R.id.notice_attachpic)
    ImageView noticeAttachpic;
    private Context mContext;
    private PublicLoadLayout mRootView;
    private Unbinder unbinder;
    private PopupWindow mPopupWindow;
    private String mNoticeId;
    private boolean isDeleteSuccess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mRootView = new PublicLoadLayout(mContext);
        mRootView.setContentView(R.layout.activity_notice_detail);
        setContentView(mRootView);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleLayoutTitle.setText(R.string.notice_detail_title);
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutRightImg.setVisibility(View.VISIBLE);
       titleLayoutRightImg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.selector_notice_detil_more));
        NoticeRequestResponse.DataBean.NoticeInfosBean.ElementsBean noticeBean = (NoticeRequestResponse.DataBean.NoticeInfosBean.ElementsBean) getIntent().getSerializableExtra("NOTICE_DETAIL");
        int mNoticeNum = getIntent().getIntExtra("NOTICE_TOTAL_READ_NUM", 0);
        mNoticeId = noticeBean.getId();
        detailTitle.setText(noticeBean.getTitle());
        noticeCreateTime.setText(noticeBean.getCreateTime());
        readPercent.setText(noticeBean.getNoticeReadNumSum() + "/" + mNoticeNum);
        noticeDescr.setText(noticeBean.getContent());
        String attachUrl = noticeBean.getAttachUrl();
        if(attachUrl != null) {
            Glide.with(this).load(attachUrl).fitCenter().into(noticeAttachpic);
            noticeAttachpic.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                finish();
                break;
            case R.id.title_layout_right_img:
                showCancelPopupWindow(NoticeDetailActivity.this);
                break;
        }
    }

    private void showCancelPopupWindow(Activity context) {
        if (mPopupWindow == null) {
            View pop = LayoutInflater.from(context).inflate(R.layout.popupwindow_notice, null);
            (pop.findViewById(R.id.notice_delete)).setOnClickListener(popupWindowClickListener);
            (pop.findViewById(R.id.notice_cancel)).setOnClickListener(popupWindowClickListener);
            mPopupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setAnimationStyle(R.style.pop_anim);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        }
        mPopupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void dismissPopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    View.OnClickListener popupWindowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView view = (TextView)v;
//            view.setTextColor(getResources().getColor(R.color.color_white));
//            view.setBackgroundColor(getResources().getColor(R.color.color_0068bd));
            switch (v.getId()) {
                case R.id.notice_delete:
                    requestDeleteNotice();
                    dismissPopupWindow();
                    break;
                case R.id.notice_cancel:
                    dismissPopupWindow();
                    break;
            }

        }
    };

    private void requestDeleteNotice() {
        mRootView.showLoadingView();
        NoticeDeleteRequest noticeDeleteRequest = new NoticeDeleteRequest();
        noticeDeleteRequest.noticeId = mNoticeId;
        noticeDeleteRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        noticeDeleteRequest.startRequest(FaceShowBaseResponse.class, new HttpCallback<FaceShowBaseResponse>() {
            @Override
            public void onSuccess(RequestBase request, FaceShowBaseResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    ToastUtil.showToast(NoticeDetailActivity.this, "删除成功");
                    isDeleteSuccess = true;
                    Intent intent = new Intent();
                    intent.putExtra("isDeleteSuccess", isDeleteSuccess);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtil.showToast(NoticeDetailActivity.this, "删除失败");
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                ToastUtil.showToast(NoticeDetailActivity.this, "删除失败");
            }
        });
    }

    public static void invoke(Context context, NoticeRequestResponse.DataBean.NoticeInfosBean.ElementsBean noticeBean, int totalNum) {
        Intent intent = new Intent(context, NoticeDetailActivity.class);
        intent.putExtra("NOTICE_DETAIL", noticeBean);
        intent.putExtra("NOTICE_TOTAL_READ_NUM", totalNum);
        context.startActivity(intent);
    }
}
