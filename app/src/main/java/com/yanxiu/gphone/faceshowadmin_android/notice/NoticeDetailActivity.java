package com.yanxiu.gphone.faceshowadmin_android.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeRequestResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    private NoticeRequestResponse.DataBean mData;
    private NoticeManagerListAdapter mNoticeManagerListAdapter;

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
        NoticeRequestResponse.DataBean.NoticeInfosBean.NoticeBean noticeBean = (NoticeRequestResponse.DataBean.NoticeInfosBean.NoticeBean) getIntent().getSerializableExtra("NOTICE_DETAIL");
        int mNoticeNum = getIntent().getIntExtra("NOTICE_TOTAL_READ_NUM", 0);
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
                break;
        }
    }

    public static void invoke(Context context, NoticeRequestResponse.DataBean.NoticeInfosBean.NoticeBean noticeBean, int totalNum) {
        Intent intent = new Intent(context, NoticeDetailActivity.class);
        intent.putExtra("NOTICE_DETAIL", noticeBean);
        intent.putExtra("NOTICE_TOTAL_READ_NUM", totalNum);
        context.startActivity(intent);
    }
}
