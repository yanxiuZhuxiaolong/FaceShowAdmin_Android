package com.yanxiu.gphone.faceshowadmin_android.course.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.course.adapter.CourseCommentAdapter;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.course.DeleteUserCommentRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.DeleteUserCommentResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.course.DiscussSaveRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseCommentRecordsRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseCommentRecordsResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseReplyRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseReplyResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.course.LikeCommentRecordRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.LikeCommentRecordResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdate;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author frc
 */
public class CourseCommentActivity extends FaceShowBaseActivity {

    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.ed_comment)
    EditText mEdComment;
    @BindView(R.id.ll_edit)
    RelativeLayout mLlEdit;

    private CourseCommentAdapter mCourseCommentAdapter;
    private UUID mGetCourseReplyRequestUUID;
    private UUID mGetCourseCommentRecordsRequestUUID;
    private String mStepId;
    private String mDescription;
    private String id = "";
    private List<GetCourseCommentRecordsResponse.ElementsBean> mRecords = new ArrayList<>();
    private boolean mRefresh = false;


    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String comment = mEdComment.getText().toString();
                if (!TextUtils.isEmpty(comment)) {
                    submitData(comment);
                }
                return true;
            }
            return false;
        }
    };

    /**
     * 提交讨论数据
     */
    private void submitData(String content) {
        mPublicLoadLayout.showLoadingView();
        DiscussSaveRequest discussSaveRequest = new DiscussSaveRequest();
        discussSaveRequest.content = content;
        discussSaveRequest.stepId = mStepId;
        discussSaveRequest.startRequest(FaceShowBaseResponse.class, new HttpCallback<FaceShowBaseResponse>() {
            @Override
            public void onSuccess(RequestBase request, FaceShowBaseResponse ret) {
                mPublicLoadLayout.finish();
                hiddenInputMethod();
                if (ret.getCode() == 0) {
                    ToastUtil.showToast(CourseCommentActivity.this, ret.getMessage());
                } else {
                    ToastUtil.showToast(CourseCommentActivity.this, ret.getError().getMessage());
                }
                mPublicLoadLayout.showLoadingView();
                id = "";
                mRecords.clear();
                getCommentRecords();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.finish();
                ToastUtil.showToast(CourseCommentActivity.this, error.getMessage());
                hiddenInputMethod();
            }
        });

    }

    private void hiddenInputMethod() {
        mEdComment.setText("");
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPublicLoadLayout = new PublicLoadLayout(this);
        mPublicLoadLayout.setContentView(R.layout.activity_course_comment_detail);
        setContentView(mPublicLoadLayout);
        ButterKnife.bind(this);
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
        mTitleLayoutTitle.setText(R.string.course_comment);
        mEdComment.setOnEditorActionListener(mOnEditorActionListener);
        mStepId = getIntent().getStringExtra("stepId");
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                id = "";
                mRefresh = true;
                getCommentRecords();
            }
        });
        getCommentTitle();
    }

    private void getCommentRecords() {
        GetCourseCommentRecordsRequest getCourseCommentRecordsRequest = new GetCourseCommentRecordsRequest();
        getCourseCommentRecordsRequest.stepId = mStepId;
        getCourseCommentRecordsRequest.id = id;
        getCourseCommentRecordsRequest.limit = "10000";
        getCourseCommentRecordsRequest.order = "desc";
        mGetCourseCommentRecordsRequestUUID = getCourseCommentRecordsRequest.startRequest(GetCourseCommentRecordsResponse.class, new HttpCallback<GetCourseCommentRecordsResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetCourseCommentRecordsResponse ret) {
                mPublicLoadLayout.finish();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (ResponseConfig.SUCCESS == ret.getCode()) {
                    if (ret.getData().getElements() != null) {
                        if (mRefresh) {
                            mRecords.clear();
                        }
                        id = String.valueOf(ret.getData().getCallbackValue());
                        mRecords.addAll(ret.getData().getElements());
                        if (mCourseCommentAdapter == null) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CourseCommentActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mCourseCommentAdapter = new CourseCommentAdapter(mRecords, mDescription, String.valueOf(ret.getData().getTotalElements()));
                            mRecyclerView.setAdapter(mCourseCommentAdapter);
                            mCourseCommentAdapter.addDeleteCommentClickListener(mDeleteCommentClickListener);
                            mCourseCommentAdapter.addThumbClickListener(mThumbClickListener);
                        } else {
                            mCourseCommentAdapter.update(mRecords, mDescription, String.valueOf(ret.getData().getTotalElements()));
                        }
                    }

                } else {
                    if (TextUtils.isEmpty(id)) {
                        mPublicLoadLayout.showOtherErrorView(ret.getError().getMessage());
                    } else {
                        ToastUtil.showToast(getApplicationContext(), ret.getError().getMessage());
                    }
                }
                mRefresh = false;
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRefresh = false;
                mPublicLoadLayout.finish();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (TextUtils.isEmpty(id)) {
                    mPublicLoadLayout.showNetErrorView();
                } else {
                    ToastUtil.showToast(getApplicationContext(), error.getMessage());
                }
            }
        });
    }

    private CourseCommentAdapter.DeleteCommentClickListener mDeleteCommentClickListener = new CourseCommentAdapter.DeleteCommentClickListener() {

        @Override
        public void delete(View view, int position) {
            EventUpdate.onDeleteDiscuss(CourseCommentActivity.this);
            deleteComment(position);
        }
    };

    private CourseCommentAdapter.ThumbClickListener mThumbClickListener = new CourseCommentAdapter.ThumbClickListener() {
        @Override
        public void thumb(View view, int position) {
            thumbComment(position);
        }
    };

    private void deleteComment(final int position) {
        DeleteUserCommentRequest deleteUserCommentRequest = new DeleteUserCommentRequest();
        deleteUserCommentRequest.commentRecordId = String.valueOf(mRecords.get(position - 1).getId());
        deleteUserCommentRequest.startRequest(DeleteUserCommentResponse.class, new HttpCallback<DeleteUserCommentResponse>() {
            @Override
            public void onSuccess(RequestBase request, DeleteUserCommentResponse ret) {
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    mCourseCommentAdapter.deleteItem(position);
                } else {
                    ToastUtil.showToast(getApplicationContext(), ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                ToastUtil.showToast(getApplicationContext(), error.getMessage());

            }
        });
    }

    private void thumbComment(final int position) {
        LikeCommentRecordRequest likeCommentRecordRequest = new LikeCommentRecordRequest();
        likeCommentRecordRequest.commentRecordId = String.valueOf(mRecords.get(position - 1).getId());
        likeCommentRecordRequest.startRequest(LikeCommentRecordResponse.class, new HttpCallback<LikeCommentRecordResponse>() {
            @Override
            public void onSuccess(RequestBase request, LikeCommentRecordResponse ret) {
                if (ResponseConfig.SUCCESS == ret.getCode()) {
                    mCourseCommentAdapter.modifyThumbNumber(position, ret.getData().getUserNum());
                } else {
                    ToastUtil.showToast(getApplicationContext(), ret.getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                ToastUtil.showToast(getApplicationContext(), error.getMessage());
            }
        });
    }


    private void getCommentTitle() {
        mPublicLoadLayout.showLoadingView();
        GetCourseReplyRequest getCourseReplyRequest = new GetCourseReplyRequest();
        getCourseReplyRequest.stepId = mStepId;
        mGetCourseReplyRequestUUID = getCourseReplyRequest.startRequest(GetCourseReplyResponse.class, new HttpCallback<GetCourseReplyResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetCourseReplyResponse ret) {
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    mDescription = ret.getData().getDescription();
                    getCommentRecords();
                } else {
                    mPublicLoadLayout.hiddenOtherErrorView();
                    mPublicLoadLayout.showOtherErrorView(ret.getMessage());
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.hiddenOtherErrorView();
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
        if (mGetCourseReplyRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetCourseReplyRequestUUID);
        }
        if (mGetCourseCommentRecordsRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetCourseCommentRecordsRequestUUID);
        }
    }
}
