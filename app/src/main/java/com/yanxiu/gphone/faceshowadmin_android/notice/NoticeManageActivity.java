package com.yanxiu.gphone.faceshowadmin_android.notice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeRequestResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdate;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lufengqing on 2017/11/1.
 */

public class NoticeManageActivity extends FaceShowBaseActivity implements View.OnClickListener {
    public static final int NOTICE_DETAIL = 1001;
    public static final int NOTICE_POST = 1002;
    private final int PAGESIZE = 20;
    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_right_img)
    ImageView titleLayoutRightImg;
    @BindView(R.id.title_layout_signIn)
    TextView titleLayoutRightTxt;
    @BindView(R.id.recyclerView)
    LoadMoreRecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private Context mContext;
    private PublicLoadLayout mRootView;
    private Unbinder unbinder;
    private NoticeRequestResponse.DataBean mData;
    private NoticeManagerListAdapter mNoticeManagerListAdapter;
    private int mCurrentNoticePosition;
    private ArrayList<NoticeRequestResponse.DataBean.NoticeInfosBean.ElementsBean> mNoticeList = new ArrayList<>();
    private int mNewCount;
    private String mRequestId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mRootView = new PublicLoadLayout(mContext);
        mRootView.setContentView(R.layout.activity_notice_manage);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        unbinder = ButterKnife.bind(this);
        initView();
        initListener();
        requestData(false);
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                mRequestId = null;
                mNoticeList.clear();
                requestData(true);
            }
        });
        recyclerView.setLoadMoreEnable(true);
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore(LoadMoreRecyclerView refreshLayout) {
                if (mNewCount >= PAGESIZE) {
                    requestData(false);
                } else {
                    recyclerView.finishLoadMore();
                    ToastUtil.showToast(NoticeManageActivity.this, "没有更多数据了");
                }

            }

            @Override
            public void onLoadmoreComplte() {
            }
        });
    }

    private void requestData(final boolean isRefreshIng) {
        if (!isRefreshIng)
            mRootView.showLoadingView();
        NoticeRequest noticeRequest = new NoticeRequest();
        noticeRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        if(mRequestId != null) {
            noticeRequest.id = mRequestId;
        }
        noticeRequest.pageSize = Integer.toString(PAGESIZE);
        noticeRequest.startRequest(NoticeRequestResponse.class, new HttpCallback<NoticeRequestResponse>() {
            @Override
            public void onSuccess(RequestBase request, NoticeRequestResponse ret) {
                mRootView.finish();
                if (isRefreshIng)
                    swipeRefreshLayout.setRefreshing(false);
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData() != null) {
                        mData = ret.getData();
                        mRequestId = mData.getNoticeInfos().getCallbackValue();
                        mNewCount = mData.getNoticeInfos().getElements().size();
                        if (mNewCount >= PAGESIZE) {
                            recyclerView.setLoadMoreEnable(true);
                        } else {
                            recyclerView.setLoadMoreEnable(false);
                        }
                        mNoticeList.addAll(mData.getNoticeInfos().getElements());
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
                mRootView.showNetErrorView();
                if (isRefreshIng)
                    swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void setData() {
        if (mData != null && mData.getNoticeInfos() != null) {
            mNoticeManagerListAdapter.setData(mNoticeList, mData.getStudentNum());
        }
    }


    private void initView() {
        titleLayoutTitle.setText(R.string.notice_manage_title);
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutRightTxt.setVisibility(View.VISIBLE);
        titleLayoutRightImg.setVisibility(View.VISIBLE);
        titleLayoutRightImg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.selector_send_notice));
        titleLayoutRightTxt.setText(R.string.notice_post);
        titleLayoutRightTxt.setTextColor(Color.parseColor("#0068BD"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mNoticeManagerListAdapter = new NoticeManagerListAdapter(this);
        recyclerView.setAdapter(mNoticeManagerListAdapter);
        mNoticeManagerListAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                mCurrentNoticePosition = position;
                Intent intent = new Intent(NoticeManageActivity.this, NoticeDetailActivity.class);
                intent.putExtra("NOTICE_DETAIL", mNoticeList.get(position));
                intent.putExtra("NOTICE_TOTAL_READ_NUM", mData.getStudentNum());
                startActivityForResult(intent, NOTICE_DETAIL);
//                NoticeDetailActivity.invoke(NoticeManageActivity.this, mData.getNoticeInfos().getElements().get(position),mData.getStudentNum());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NOTICE_DETAIL) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Boolean isDeleteSuccess = data.getBooleanExtra("isDeleteSuccess", false);
                    if (isDeleteSuccess) {
                        mNoticeList.remove(mCurrentNoticePosition);
                        setData();
                    }
                }
            }
        } else if (requestCode == NOTICE_POST) {
            if (resultCode == RESULT_OK) {
                requestData(false);
//                if (data != null) {
//                    Boolean isPostSuccess = data.getBooleanExtra("isPostSuccess", false);
//                    NoticeRequestResponse.DataBean.NoticeInfosBean.ElementsBean bean = (NoticeRequestResponse.DataBean.NoticeInfosBean.ElementsBean) data.getSerializableExtra("noticeBean");
//                    if (isPostSuccess) {
//                        mNoticeList.add(0, bean);
//                        setData();
//                    }
//                }
            }
        }
    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_signIn, R.id.title_layout_right_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                finish();
                break;
            case R.id.title_layout_right_img:
            case R.id.title_layout_signIn:
                EventUpdate.onSendNotify(mContext);
                postNotice();
                break;
        }
    }

    private void postNotice() {
        Intent intent = new Intent(NoticeManageActivity.this, NoticePostActivity.class);
        startActivityForResult(intent, NOTICE_POST);
//        NoticePostActivity.invoke(this);
    }


    public static void invoke(Context context) {
        Intent intent = new Intent(context, NoticeManageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retry_button:
                requestData(false);
                break;
            default:
                break;
        }
    }
}
