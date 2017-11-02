package com.yanxiu.gphone.faceshowadmin_android.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeRequestResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lufengqing on 2017/11/1.
 */

public class NoticeManageActivity extends FaceShowBaseActivity {
    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_right_img)
    ImageView titleLayoutRightImg;
    @BindView(R.id.title_layout_right_txt)
    TextView titleLayoutRightTxt;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
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
        mRootView.setContentView(R.layout.activity_notice_manage);
        setContentView(mRootView);
        unbinder = ButterKnife.bind(this);
        initView();
        requestData();
    }

    private void requestData() {
        mRootView.showLoadingView();
        NoticeRequest courseArrangeRequest = new NoticeRequest();
        courseArrangeRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        courseArrangeRequest.clazsId = "9";
        courseArrangeRequest.startRequest(NoticeRequestResponse.class, new HttpCallback<NoticeRequestResponse>() {
            @Override
            public void onSuccess(RequestBase request, NoticeRequestResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData() != null) {
                        mData = ret.getData();
                        setData();
                    } else {
                        mRootView.showOtherErrorView();
                    }

                } else {
                    mRootView.showOtherErrorView();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();
            }
        });

    }

    private void setData() {
        if(mData != null && mData.getNoticeInfos() != null) {
            mNoticeManagerListAdapter.setData(mData.getNoticeInfos().getElements(), mData.getStudentNum());
        }
    }



    private void initView() {
        titleLayoutTitle.setText(R.string.notice_manage_title);
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutRightTxt.setText(R.string.notice_post);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mNoticeManagerListAdapter = new NoticeManagerListAdapter(this);
        recyclerView.setAdapter(mNoticeManagerListAdapter);
        mNoticeManagerListAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                NoticeDetailActivity.invoke(NoticeManageActivity.this, mData.getNoticeInfos().getElements().get(position),mData.getStudentNum());
            }
        });
    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                finish();
                break;
            case R.id.title_layout_right_txt:
                break;
        }
    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, NoticeManageActivity.class);
        context.startActivity(intent);
    }
}
