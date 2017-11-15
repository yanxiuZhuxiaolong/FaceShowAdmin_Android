package com.yanxiu.gphone.faceshowadmin_android.task.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseCommentRecordsResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetReplyRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetReplyResponse;
import com.yanxiu.gphone.faceshowadmin_android.task.adapter.ReplyDetailAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author frc
 */
public class ReplyDetailActivity extends FaceShowBaseActivity {

    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    ReplyDetailAdapter mReplyDetailAdapter;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private List<GetCourseCommentRecordsResponse.ElementsBean> mRecords = new ArrayList<>();
    private UUID mGetReplyRequestUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPublicLoadLayout = new PublicLoadLayout(this);
        mPublicLoadLayout.setContentView(R.layout.activity_relpy_detail);
        setContentView(mPublicLoadLayout);
        ButterKnife.bind(this);
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
        mTitleLayoutTitle.setText(R.string.reply);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReplys();
            }
        });
        getReplys();
    }

    private void getReplys() {
        mPublicLoadLayout.showLoadingView();
        GetReplyRequest getReplyRequest = new GetReplyRequest();
        getReplyRequest.questionId = "687";
//        getReplyRequest.questionId = getIntent().getStringExtra("questionId");
        mGetReplyRequestUUID = getReplyRequest.startRequest(GetReplyResponse.class, new HttpCallback<GetReplyResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetReplyResponse ret) {
                mPublicLoadLayout.hiddenOtherErrorView();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (ResponseConfig.SUCCESS == ret.getCode()) {
                    if (ret.getData() != null && ret.getData() != null) {
                        if (mReplyDetailAdapter != null) {
                            mReplyDetailAdapter.update(ret.getData(), getIntent().getStringExtra("title"), String.valueOf(ret.getData().size()));
                        } else {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReplyDetailActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mReplyDetailAdapter = new ReplyDetailAdapter(ret.getData(), getIntent().getStringExtra("title"), String.valueOf(ret.getData().size()));
                            mRecyclerView.setAdapter(mReplyDetailAdapter);
                        }
                    }
                } else {
                    mPublicLoadLayout.showOtherErrorView(ret.getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.hiddenOtherErrorView();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mPublicLoadLayout.showOtherErrorView(error.getMessage());
            }
        });
    }


    @OnClick(R.id.title_layout_left_img)
    public void onViewClicked() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGetReplyRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetReplyRequestUUID);
        }
    }
}
