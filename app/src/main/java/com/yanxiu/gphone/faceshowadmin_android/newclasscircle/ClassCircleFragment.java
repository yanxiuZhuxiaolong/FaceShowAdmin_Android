package com.yanxiu.gphone.faceshowadmin_android.newclasscircle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yanxiu.common_base.utils.ScreenUtils;
import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.MainActivity;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.customView.SizeChangeCallbackView;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.activity.ClassCircleMessageActivity;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.activity.PublishedMomentListActivity;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.activity.SendClassCircleActivity;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.adapter.ClassCircleAdapter;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.dialog.ClassCircleDialog;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.ClassCircleCancelLikeRequest;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.ClassCircleCommentToMasterRequest;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.ClassCircleCommentToUserRequest;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.ClassCircleDeleteRequest;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.ClassCircleLikeRequest;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.ClassCircleRequest;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.DiscardCommentRequest;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.DiscardMomentRequest;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.ClassCircleCancelLikeResponse;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.ClassCircleDeleteResponse;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.CommentResponse;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.Comments;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.DiscardMomentResponse;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.LikeResponse;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.RefreshClassCircle;
import com.yanxiu.gphone.faceshowadmin_android.utils.ClassCircleTimeUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.Logger;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;



import java.util.List;
import java.util.UUID;


import de.greenrobot.event.EventBus;

import static android.app.Activity.RESULT_OK;
import static com.yanxiu.gphone.faceshowadmin_android.newclasscircle.adapter.ClassCircleAdapter.REFRESH_ANIM_VIEW;

/**
 * 首页 “班级圈”Fragment
 */
public class ClassCircleFragment extends FaceShowBaseFragment implements LoadMoreRecyclerView.LoadMoreListener,
        View.OnClickListener, ClassCircleAdapter.onCommentClickListener, ClassCircleAdapter.onLikeClickListener,
        SwipeRefreshLayout.OnRefreshListener, ClassCircleAdapter.onContentLinesChangedlistener,
        ClassCircleAdapter.onDeleteClickListener, ClassCircleAdapter.onMomentHeadImageClickListener
        , ClassCircleAdapter.onNewMessageButtonClickListener {

    private static final int REQUEST_CODE_MY_PUBLISHED_MOMENTS = 0x000;
    private static final int REQUEST_CODE_PUBLISH_MOMENT = 0x001;
    public boolean firstEnter = true;

    private LoadMoreRecyclerView mClassCircleRecycleView;
    private ClassCircleAdapter mClassCircleAdapter;
    private RelativeLayout mCommentLayout;
    private EditText mCommentView;
    private SizeChangeCallbackView mAdjustPanView;
    private TextView mFunctionView;
    private TextView mTitleView;
    private View mTopView;
    private int mMomentPosition = -1;
    private int mCommentPosition = -1;
    private int mVisibility = View.INVISIBLE;
    private int mHeight;
    private boolean isCommentMaster;
    private String mCameraPath;
    private ClassCircleDialog mClassCircleDialog;
    private SwipeRefreshLayout mRefreshView;
    private PublicLoadLayout rootView;
    private View mDataEmptyView;
    private TextView mTvSureComment;
    private FastScrollLinearLayoutManager mLinearLayoutManager;
    private boolean isCommentLoading = false;

    private UUID mClassCircleRequest;
    private UUID mClassCircleLikeRequest;
    private UUID mClassCircleCancelLikeRequest;
    private UUID mCommentToMasterRequest;
    private UUID mCommentToUserRequest;
    private UUID mCircleDeleteRequest;
    private UUID mDiscardCommentRequest;

    /**
     * 班级圈 顶部 title栏添加 左侧 抽屉菜单
     * */
    private ImageView leftTitleImageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = new PublicLoadLayout(getContext());
        rootView.setContentView(R.layout.fragment_classcircle);
        EventBus.getDefault().register(ClassCircleFragment.this);
        initView(rootView);
        listener();
        initData();
        startRequest("0");
        return rootView;
    }


    /**
     * 手动刷新页面，当底部通知tab被点击时调用
     */
    public void toRefresh() {
        mRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshView.setRefreshing(true);
            }
        });
        startRequest("0");
    }

    /**
     * 显示新的评论消息
     *
     * @param promptNum 新消息数量
     */
    public void showMomentMsg(int promptNum) {
        if (mClassCircleAdapter != null) {
            mClassCircleAdapter.showNewMessageNumber(promptNum);
        }
    }

    /**
     * 隐藏评论数
     */
    public void hideMomentMsg() {
        if (mClassCircleAdapter != null) {
            mClassCircleAdapter.hideNewMessageButton();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(ClassCircleFragment.this);
        if (mClassCircleRequest != null) {
            RequestBase.cancelRequestWithUUID(mClassCircleRequest);
            mClassCircleRequest = null;
        }
        if (mClassCircleLikeRequest != null) {
            RequestBase.cancelRequestWithUUID(mClassCircleLikeRequest);
            mClassCircleLikeRequest = null;
        }
        if (mClassCircleCancelLikeRequest != null) {
            RequestBase.cancelRequestWithUUID(mClassCircleCancelLikeRequest);
            mClassCircleCancelLikeRequest = null;
        }
        if (mCommentToMasterRequest != null) {
            RequestBase.cancelRequestWithUUID(mCommentToMasterRequest);
            mCommentToMasterRequest = null;
        }
        if (mCommentToUserRequest != null) {
            RequestBase.cancelRequestWithUUID(mCommentToUserRequest);
            mCommentToUserRequest = null;
        }
        if (mCircleDeleteRequest != null) {
            RequestBase.cancelRequestWithUUID(mCircleDeleteRequest);
            mCircleDeleteRequest = null;
        }
        if (mDiscardCommentRequest != null) {
            RequestBase.cancelRequestWithUUID(mDiscardCommentRequest);
            mDiscardCommentRequest = null;
        }
    }

    private void initView(View rootView) {
        mTopView = rootView.findViewById(R.id.il_title);
        ImageView mBackView = (ImageView) rootView.findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.INVISIBLE);
        mTitleView = (TextView) rootView.findViewById(R.id.title_layout_title);
        mFunctionView = (TextView) rootView.findViewById(R.id.title_layout_right_txt);
        mFunctionView.setText(R.string.publish);
        mFunctionView.setTextColor(ContextCompat.getColor(ClassCircleFragment.this.getContext(), R.color.color_0068BD));
        mFunctionView.setVisibility(View.VISIBLE);
        mTvSureComment = (TextView) rootView.findViewById(R.id.tv_sure);
        mCommentLayout = (RelativeLayout) rootView.findViewById(R.id.ll_edit);
        mCommentView = (EditText) rootView.findViewById(R.id.ed_comment);
        mAdjustPanView = (SizeChangeCallbackView) rootView.findViewById(R.id.sc_adjustpan);
        mClassCircleRecycleView = (LoadMoreRecyclerView) rootView.findViewById(R.id.lm_class_circle);
        mLinearLayoutManager = new FastScrollLinearLayoutManager(getContext());
        mClassCircleRecycleView.setLayoutManager(mLinearLayoutManager);
        mClassCircleAdapter = new ClassCircleAdapter(getContext());
        mClassCircleRecycleView.setAdapter(mClassCircleAdapter);

        mRefreshView = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_refresh);
        mDataEmptyView = rootView.findViewById(R.id.rl_data_empty);

        /*添加 左侧title*/
        leftTitleImageView=rootView.findViewById(R.id.title_layout_left_img);
    }

    private void listener() {
        mClassCircleRecycleView.setLoadMoreListener(ClassCircleFragment.this);
        mClassCircleAdapter.setCommentClickListener(ClassCircleFragment.this);
        mClassCircleAdapter.setThumbClickListener(ClassCircleFragment.this);
        mClassCircleAdapter.setContentLinesChangedlistener(ClassCircleFragment.this);
        mClassCircleAdapter.setDeleteClickListener(ClassCircleFragment.this);
        mClassCircleAdapter.setMomentHeadImageClickListener(ClassCircleFragment.this);
        mClassCircleAdapter.setNewMessageButtonClickListener(ClassCircleFragment.this);
        mFunctionView.setOnClickListener(ClassCircleFragment.this);
        mRefreshView.setOnRefreshListener(ClassCircleFragment.this);
        rootView.setRetryButtonOnclickListener(this);
        mTvSureComment.setOnClickListener(ClassCircleFragment.this);

        mCommentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mTvSureComment.setEnabled(false);
                } else {
                    mTvSureComment.setEnabled(true);
                }
            }
        });
        leftTitleImageView.setVisibility(View.VISIBLE);
        leftTitleImageView.setImageResource(R.drawable.selector_main_leftdrawer);
        leftTitleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).openLeftDrawer();
//                MainActivity.invoke(getActivity());
            }
        });
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.parseColor("#e6ffffff"));
        mTitleView.setText(R.string.classcircle);
        mClassCircleRecycleView.getItemAnimator().setChangeDuration(0);
        mClassCircleRecycleView.setLoadMoreEnable(true);
        mRefreshView.setProgressViewOffset(false, ScreenUtils.dpToPxInt(getContext(), 44), ScreenUtils.dpToPxInt(getContext(), 100));
        mRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshView.setRefreshing(true);
            }
        });
    }

    public void onEventMainThread(RefreshClassCircle refreshClassCircle) {
        onRefresh();
    }

    /**
     * 班级圈
     */
    private void startRequest(final String offset) {
        ClassCircleRequest circleRequest = new ClassCircleRequest();
        circleRequest.offset = offset;
        //TODO
//        ((MainActivity) getActivity()).hideClassCircleRedDot();
        mClassCircleRequest = circleRequest.startRequest(ClassCircleResponse.class, new HttpCallback<ClassCircleResponse>() {
            @Override
            public void onSuccess(RequestBase request, ClassCircleResponse ret) {
                firstEnter = false;
                mClassCircleRequest = null;
                mRefreshView.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshView.setRefreshing(false);
                    }
                });
                if (ret != null) {
                    if (ret.getCode() == 0 && ret.data != null && ret.data.moments != null) {

                        if (offset.equals("0")) {
                            /*如果 是首次请求数据 覆盖添加到adapter中*/
                            mClassCircleAdapter.setData(ret.data.moments);
                        } else {
                            /*如果offset 不为0  需要拼接数据*/
                            mClassCircleAdapter.addData(ret.data.moments);
                        }
                        if (ret.data.moments == null || ret.data.moments.size() == 0) {
                            mDataEmptyView.setVisibility(View.VISIBLE);
                            /*这里需要清空一次 数据*/


                        } else {
                            mDataEmptyView.setVisibility(View.GONE);
                        }
                        mClassCircleRecycleView.setLoadMoreEnable(ret.data.hasNextPage);
                    } else {
                        if (offset.equals("0")) {
                            rootView.showOtherErrorView(ret.getError().getMessage());
                        } else {
                            ToastUtil.showToast(getContext(), ret.getError().getMessage());
                        }
                    }

                } else {
                    if (offset.equals("0")) {
                        rootView.showOtherErrorView();
//                        mClassCircleAdapter.clear();
                    }
                }
//                if (ret != null && ret.data != null && ret.data.moments != null) {
//                    if (offset.equals("0")) {
//                        mClassCircleAdapter.setData(ret.data.moments);
//                    } else {
//                        mClassCircleAdapter.addData(ret.data.moments);
//                    }
//                    if (ret.data.moments == null || ret.data.moments.size() == 0) {
//                        mDataEmptyView.setVisibility(View.VISIBLE);
//                    } else {
//                        mDataEmptyView.setVisibility(View.GONE);
//                    }
//                    mClassCircleRecycleView.setLoadMoreEnable(ret.data.hasNextPage);
//                } else {
//                    if (offset.equals("0")) {
//                        rootView.showNetErrorView();
//                        mClassCircleAdapter.clear();
//                    }
//                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                firstEnter = false;
                mClassCircleRequest = null;
                mRefreshView.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshView.setRefreshing(false);
                    }
                });
                if (offset.equals("0")) {
                    rootView.showNetErrorView();
                    mClassCircleAdapter.clear();
                }
            }
        });
    }

    /**
     * 赞
     */
    private void startLikeRequest(final int position, final ClassCircleResponse.Data.Moments moments) {
        rootView.showLoadingView();
        ClassCircleLikeRequest classCircleLikeRequest = new ClassCircleLikeRequest();
        classCircleLikeRequest.momentId = moments.id;
        mClassCircleLikeRequest = classCircleLikeRequest.startRequest(LikeResponse.class, new HttpCallback<LikeResponse>() {
            @Override
            public void onSuccess(RequestBase request, LikeResponse ret) {
                rootView.hiddenLoadingView();
                mClassCircleLikeRequest = null;
                if (ret != null && ret.data != null) {
                    ClassCircleResponse.Data.Moments.Likes likes = moments.new Likes();
                    likes.clazsId = ret.data.clazsId;
                    likes.createTime = ret.data.createTime;
                    likes.id = ret.data.id;
                    likes.momentId = ret.data.momentId;
                    likes.publisher = ret.data.publisher;
                    moments.likes.add(likes);
//                    mClassCircleAdapter.notifyItemChanged(position, ClassCircleAdapter.REFRESH_LIKE_DATA);
                    mClassCircleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                mClassCircleLikeRequest = null;
                ToastUtil.showToast(getContext(), error.getMessage());
            }
        });
    }

    private void cancelLikeRequest(final int postion, final ClassCircleResponse.Data.Moments moments) {
        rootView.showLoadingView();
        final ClassCircleCancelLikeRequest classCircleCancelLikeRequest = new ClassCircleCancelLikeRequest();
        classCircleCancelLikeRequest.momentId = moments.id;
        mClassCircleCancelLikeRequest = classCircleCancelLikeRequest.startRequest(ClassCircleCancelLikeResponse.class, new HttpCallback<ClassCircleCancelLikeResponse>() {
            @Override
            public void onSuccess(RequestBase request, ClassCircleCancelLikeResponse ret) {
                rootView.hiddenLoadingView();
                mClassCircleLikeRequest = null;
                if (ret != null && ret.getCode() == 0) {
                    for (int i = 0; i < moments.likes.size(); i++) {
                        if (moments.likes.get(i).publisher.userId.equals(String.valueOf(UserInfo.getInstance().getInfo().getUserId()))) {
                            moments.likes.remove(i);
                        }
                    }
//                    mClassCircleAdapter.notifyItemChanged(postion, ClassCircleAdapter.REFRESH_LIKE_DATA);
                    mClassCircleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                mClassCircleLikeRequest = null;
                ToastUtil.showToast(getContext(), error.getMessage());
            }
        });
    }

    /**
     * 评论
     */
    private void startCommentToMasterRequest(final int position, final String content, final ClassCircleResponse.Data.Moments moments) {
        rootView.showLoadingView();
        ClassCircleCommentToMasterRequest masterRequest = new ClassCircleCommentToMasterRequest();
        masterRequest.clazsId = moments.clazsId;
        masterRequest.content = content;
        masterRequest.momentId = moments.id;
        mCommentToMasterRequest = masterRequest.startRequest(CommentResponse.class, new HttpCallback<CommentResponse>() {
            @Override
            public void onSuccess(RequestBase request, CommentResponse ret) {
                rootView.hiddenLoadingView();
                isCommentLoading = false;
                mCommentToMasterRequest = null;
                if (ret != null) {
                    if (ret.getCode() == 0 && ret.data != null) {
                        moments.comments.add(ret.data);
//                        mClassCircleAdapter.notifyItemChanged(position, ClassCircleAdapter.REFRESH_COMMENT_DATA);
                        mClassCircleAdapter.notifyDataSetChanged();
                        commentFinish();
                        mCommentView.setText("");
                    } else {
                        ToastUtil.showToast(getContext(), ret.getError().getMessage());
                    }
                } else {
                    ToastUtil.showToast(getContext(), "发送失败");
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mCommentToMasterRequest = null;
                isCommentLoading = false;
                ToastUtil.showToast(getContext(), error.getMessage());
                rootView.hiddenLoadingView();
            }
        });
    }

    /**
     * 回复
     */
    private void startCommentToUserRequest(final int position, final String content, final ClassCircleResponse.Data.Moments moments, final Comments comments) {
        rootView.showLoadingView();
        ClassCircleCommentToUserRequest userRequest = new ClassCircleCommentToUserRequest();
        userRequest.clazsId = moments.clazsId;
        userRequest.momentId = moments.id;
        userRequest.content = content;
        userRequest.toUserId = comments.publisher.userId;
        userRequest.commentId = comments.id;
        mCommentToUserRequest = userRequest.startRequest(CommentResponse.class, new HttpCallback<CommentResponse>() {
            @Override
            public void onSuccess(RequestBase request, CommentResponse ret) {
                rootView.hiddenLoadingView();
                isCommentLoading = false;
                mCommentToUserRequest = null;
                if (ret != null && ret.data != null) {
                    moments.comments.add(ret.data);
//                    mClassCircleAdapter.notifyItemChanged(position, ClassCircleAdapter.REFRESH_COMMENT_DATA);
                    mClassCircleAdapter.notifyDataSetChanged();
                    commentFinish();
                    mCommentView.setText("");
                } else {
                    ToastUtil.showToast(getContext(), R.string.error_tip);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                isCommentLoading = false;
                mCommentToUserRequest = null;
                ToastUtil.showToast(getContext(), error.getMessage());
            }
        });
    }

    @Override
    public void onRefresh() {
        startRequest("0");
    }

    @Override
    public void onLoadMore(LoadMoreRecyclerView refreshLayout) {
        startRequest(mClassCircleAdapter.getIdFromLastPosition());
    }

    @Override
    public void onLoadmoreComplte() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_right_txt:
                Intent intent = new Intent(getContext(), SendClassCircleActivity.class);
                startActivityForResult(intent, REQUEST_CODE_PUBLISH_MOMENT);
                break;
            case R.id.retry_button:
                mRefreshView.setRefreshing(true);
                rootView.hiddenNetErrorView();
                onRefresh();
                break;
            case R.id.tv_sure:
                String comment = mCommentView.getText().toString();
                if (!TextUtils.isEmpty(comment) && !isCommentLoading) {
                    isCommentLoading = true;
                    ClassCircleResponse.Data.Moments moments = mClassCircleAdapter.getDataFromPosition(mMomentPosition);
                    if (isCommentMaster) {
                        startCommentToMasterRequest(mMomentPosition, comment, moments);
                    } else {
                        startCommentToUserRequest(mMomentPosition, comment, moments, moments.comments.get(mCommentPosition));
                    }
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void commentClick(final int position, ClassCircleResponse.Data.Moments moments, int commentPosition, Comments comment, boolean isCommentMaster) {
        this.isCommentMaster = isCommentMaster;
        this.mCommentPosition = commentPosition;
        this.mMomentPosition = position;
        if (!isCommentMaster) {
            mCommentView.setHint(String.format(getString(R.string.class_circle_comment_to_user), comment.publisher.realName));
        } else {
            mCommentView.setHint(R.string.class_circle_comment_to_master);
        }

        Logger.d("onSizeChanged", "commentClick");
        mCommentLayout.setVisibility(View.VISIBLE);
        mCommentView.setFocusable(true);
        mCommentView.clearFocus();
        mCommentView.requestFocus();
        if (mVisibility == View.VISIBLE) {
            setScroll(position, mHeight, false);
        }
        mAdjustPanView.setViewSizeChangedCallback(new SizeChangeCallbackView.onViewSizeChangedCallback() {
            @Override
            public void sizeChanged(int visibility, int height) {
                Logger.d("onSizeChanged", "visibility  " + visibility);
                mVisibility = visibility;
                if (visibility == View.VISIBLE) {
                    mHeight = height;
                    ((MainActivity) getActivity()).setBottomVisibility(View.GONE);
                    setScroll(position, height, true);
                } else {
                    ((MainActivity) getActivity()).setBottomVisibility(View.VISIBLE);
                }
            }
        });
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mCommentView, 0);
    }

    @Override
    public void commentCancelClick(int pos, List<ClassCircleResponse.Data.Moments> data, int commentPosition, Comments comment) {
        mMomentPosition = pos;
        mCommentPosition = commentPosition;
        hideSoftInputM();
        showDiscardCommentPopupWindow(data, comment);
    }


    @Override
    public void commentFinish() {
        mVisibility = View.INVISIBLE;
        mAdjustPanView.setViewSizeChangedCallback(null);
        hideSoftInputM();
        ((MainActivity) getActivity()).setBottomVisibility(View.VISIBLE);
        mMomentPosition = -1;
        mCommentPosition = -1;
    }

    private void hideSoftInputM() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mAdjustPanView.getWindowToken(), 0);
        mCommentLayout.setVisibility(View.GONE);
        mCommentView.setText("");
    }

    @Override
    public void onContentLinesChanged(final int position, final boolean isShowAll) {
        mClassCircleRecycleView.post(new Runnable() {
            @Override
            public void run() {
                int visibleStart = ((LinearLayoutManager) mClassCircleRecycleView.getLayoutManager()).findFirstVisibleItemPosition();
                int visibleEnd = ((LinearLayoutManager) mClassCircleRecycleView.getLayoutManager()).findLastVisibleItemPosition();
                if ((position < visibleStart || position > visibleEnd) && !isShowAll) {
                    mClassCircleRecycleView.scrollToPosition(position);
                    mClassCircleRecycleView.post(new Runnable() {
                        @Override
                        public void run() {
                            float diment = getResources().getDimension(R.dimen.top_layout_height);
//                            int length=ScreenUtils.dpToPxInt(getContext(),diment);
                            mClassCircleRecycleView.scrollBy(0, -(int) diment);
                        }
                    });
                }
            }
        });
    }

    /**
     * 将选中item滚动到可见位置
     */
    private void setScroll(final int position, final int height, boolean isShouldScroll) {
        Logger.d("mClassCircleRecycleView", "adapter  position  " + position);
        int visibleStart = ((LinearLayoutManager) mClassCircleRecycleView.getLayoutManager()).findFirstVisibleItemPosition();
        int visibleEnd = ((LinearLayoutManager) mClassCircleRecycleView.getLayoutManager()).findLastVisibleItemPosition();
        if ((mMomentPosition < visibleStart || mMomentPosition > visibleEnd) || isShouldScroll) {
            mClassCircleRecycleView.scrollToPosition(position);
        }
        ClassCircleTimeUtils.creat().start(new ClassCircleTimeUtils.onTimeUplistener() {
            @Override
            public void onTimeUp() {
                setSrcollBy(position, height);
            }
        });
    }

    /**
     * 检查选中item位置进行微调
     */
    private void setSrcollBy(final int position, final int height) {
        int visibleIndex = ((LinearLayoutManager) mClassCircleRecycleView.getLayoutManager()).findFirstVisibleItemPosition();
        int n = position - visibleIndex;
        Logger.d("mClassCircleRecycleView", "visibile position  " + visibleIndex);
        Logger.d("mClassCircleRecycleView", "position  " + n);
        if (0 <= n && n < mClassCircleRecycleView.getChildCount()) {
            int top = mClassCircleRecycleView.getChildAt(n).getTop();
            int bottom = mClassCircleRecycleView.getChildAt(n).getBottom();

            Logger.d("mClassCircleRecycleView", "top " + top);
            Logger.d("mClassCircleRecycleView", "bottom " + bottom);
            Logger.d("mClassCircleRecycleView", "height " + height);

            final int heightMove = bottom - height;
            if (heightMove != 0 && bottom != height) {
                Logger.d("mClassCircleRecycleView", "heightMoves " + heightMove);
                mClassCircleRecycleView.post(new Runnable() {
                    @Override
                    public void run() {
                        mClassCircleRecycleView.scrollBy(0, heightMove);
                    }
                });
            }
        }
        Logger.d("mClassCircleRecycleView", " ");
    }


    @Override
    public void likeClick(int position, ClassCircleResponse.Data.Moments moments) {
        hideSoftInputM();
        startLikeRequest(position, moments);
    }

    @Override
    public void cancelLikeClick(int position, ClassCircleResponse.Data.Moments moments) {
        hideSoftInputM();
        cancelLikeRequest(position, moments);
    }

    @Override
    public void momentPosition(int position) {
        mMomentPosition = position;
    }

    @Override
    public void delete(int position, List<ClassCircleResponse.Data.Moments> data) {
        mMomentPosition = position;
//        mClassCircleAdapter.notifyDataSetChanged();
//        mClassCircleAdapter.notifyItemChanged(mMomentPosition,REFRESH_ANIM_VIEW);
        hideSoftInputM();
        showDiscardMomentPopupWindow(data);

    }
    /**
     * 班主任删除动态
     */
    private void startDeleteRequest(final int position, final ClassCircleResponse.Data.Moments moments) {
        rootView.showLoadingView();
        ClassCircleDeleteRequest circleDeleteRequest = new ClassCircleDeleteRequest();
        circleDeleteRequest.momentId = moments.id;
        mCircleDeleteRequest = circleDeleteRequest.startRequest(ClassCircleDeleteResponse.class, new HttpCallback<ClassCircleDeleteResponse>() {
            @Override
            public void onSuccess(RequestBase request, ClassCircleDeleteResponse ret) {
                rootView.hiddenLoadingView();
                mCircleDeleteRequest = null;
                if (ret != null && ret.getCode() == 0) {
                    mClassCircleAdapter.deleteData(position - 1);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                mCircleDeleteRequest = null;
                ToastUtil.showToast(getContext(), error.getMessage());
            }
        });
    }
    /**
     * 删除自己发布的班级圈
     *
     * @param data 所有班级圈信息
     */
//    private void discardMoment(final List<ClassCircleResponse.Data.Moments> data) {
//        rootView.showLoadingView();
//        DiscardMomentRequest discardMomentRequest = new DiscardMomentRequest();
//        discardMomentRequest.momentId = data.get(mMomentPosition - 1).id;
//        mDiscardMomentRequest = discardMomentRequest.startRequest(DiscardMomentResponse.class, new HttpCallback<DiscardMomentResponse>() {
//            @Override
//            public void onSuccess(RequestBase request, DiscardMomentResponse ret) {
//                rootView.hiddenLoadingView();
//                mDiscardMomentRequest = null;
//                if (ret != null && ret.getCode() == 0) {
//                    data.remove(mMomentPosition - 1);
//                    mClassCircleAdapter.notifyDataSetChanged();
////                    mClassCircleAdapter.notifyItemRemoved(mMomentPosition);
//                } else {
//                    ToastUtil.showToast(ClassCircleFragment.this.getContext(), ret.getError().getMessage());
//                }
//            }
//
//            @Override
//            public void onFail(RequestBase request, Error error) {
//                rootView.hiddenLoadingView();
//                mDiscardMomentRequest = null;
//                ToastUtil.showToast(ClassCircleFragment.this.getContext(), error.getMessage());
//            }
//        });
//    }

    /**
     * 删除自己发布的评论
     *
     * @param data    当前评论所在的话题列表
     * @param comment 当期评论内容
     */
    private void discardComment(final List<ClassCircleResponse.Data.Moments> data, final Comments comment) {
        rootView.showLoadingView();
        DiscardCommentRequest discardCommentRequest = new DiscardCommentRequest();
        discardCommentRequest.commentId = data.get(mMomentPosition - 1).comments.get(mCommentPosition).id;
        mDiscardCommentRequest = discardCommentRequest.startRequest(DiscardMomentResponse.class, new HttpCallback<DiscardMomentResponse>() {
            @Override
            public void onSuccess(RequestBase request, DiscardMomentResponse ret) {
                rootView.hiddenLoadingView();
                mDiscardCommentRequest = null;
                if (ret != null && ret.getCode() == 0) {
                    data.get(mMomentPosition - 1).comments.remove(mCommentPosition);
//                    mClassCircleAdapter.notifyItemChanged(mMomentPosition, ClassCircleAdapter.REFRESH_COMMENT_DATA);
                    mClassCircleAdapter.notifyDataSetChanged();
                    commentFinish();
                } else {
                    ToastUtil.showToast(ClassCircleFragment.this.getContext(), ret.getError().getMessage());
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                mDiscardCommentRequest = null;
                ToastUtil.showToast(ClassCircleFragment.this.getContext(), error.getMessage());
            }
        });


    }


    private PopupWindow mDiscardCommentCancelPopupWindow;
    private PopupWindow mDiscardMomentCancelPopupWindow;

    private void showDiscardCommentPopupWindow(final List<ClassCircleResponse.Data.Moments> data, final Comments comment) {
        mClassCircleAdapter.notifyItemChanged(mMomentPosition, REFRESH_ANIM_VIEW);
        if (mDiscardCommentCancelPopupWindow == null) {
            View pop = LayoutInflater.from(this.getContext()).inflate(R.layout.pop_ask_cancel_layout, null);
            TextView tvDel = (TextView) pop.findViewById(R.id.tv_pop_sure);
            tvDel.setText(R.string.class_circle_delete);
            tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissPopupWindow();
                    discardComment(data, comment);
                }
            });
            (pop.findViewById(R.id.tv_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissPopupWindow();
                }
            });
            mDiscardCommentCancelPopupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mDiscardCommentCancelPopupWindow.setAnimationStyle(R.style.pop_anim);
            mDiscardCommentCancelPopupWindow.setFocusable(true);
            mDiscardCommentCancelPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        }
        mDiscardCommentCancelPopupWindow.showAtLocation(this.getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void showDiscardMomentPopupWindow(final List<ClassCircleResponse.Data.Moments> data) {
        if (mDiscardMomentCancelPopupWindow == null) {
            View pop = LayoutInflater.from(this.getContext()).inflate(R.layout.pop_ask_cancel_layout, null);
            TextView tvDel = (TextView) pop.findViewById(R.id.tv_pop_sure);
            tvDel.setText(R.string.class_circle_delete);
            tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissPopupWindow();
//                    discardMoment(data);
                    startDeleteRequest(mMomentPosition,data.get(mMomentPosition-1));
                }
            });
            (pop.findViewById(R.id.tv_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissPopupWindow();
                }
            });
            mDiscardMomentCancelPopupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mDiscardMomentCancelPopupWindow.setAnimationStyle(R.style.pop_anim);
            mDiscardMomentCancelPopupWindow.setFocusable(true);
            mDiscardMomentCancelPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        }
        mDiscardMomentCancelPopupWindow.showAtLocation(this.getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void dismissPopupWindow() {
        if (mDiscardMomentCancelPopupWindow != null) {
            mDiscardMomentCancelPopupWindow.dismiss();
        }
        if (mDiscardCommentCancelPopupWindow != null) {
            mDiscardCommentCancelPopupWindow.dismiss();
        }
    }


    @Override
    public void myHeadClicked(int position, String userId) {
        Intent intent = new Intent(this.getContext(), PublishedMomentListActivity.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, REQUEST_CODE_MY_PUBLISHED_MOMENTS);
    }

    @Override
    public void otherUserHeadClicked(int positon, String userId) {
        // TODO: 2018/1/17 目前不支持查看其它人发布的班级圈列表
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_MY_PUBLISHED_MOMENTS:
            case REQUEST_CODE_PUBLISH_MOMENT:
                if (resultCode == RESULT_OK) {
                    mLinearLayoutManager.smoothScrollToPosition(mClassCircleRecycleView, null, 0);
                    toRefresh();
                }
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mClassCircleAdapter != null) {
            mClassCircleAdapter.notifyItemChanged(mMomentPosition, REFRESH_ANIM_VIEW);
        }
    }

    @Override
    public void newMessageButtonClick() {
        mClassCircleAdapter.hideNewMessageButton();
        startActivity(new Intent(this.getActivity(), ClassCircleMessageActivity.class));
    }

    public class FastScrollLinearLayoutManager extends LinearLayoutManager {
        public FastScrollLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

                @Override
                public PointF computeScrollVectorForPosition(int targetPosition) {
                    return FastScrollLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
                }

                //该方法控制速度。
                //if returned value is 2 ms, it means scrolling 1000 pixels with LinearInterpolation should take 2 seconds.
                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                /*
                     控制单位速度,  毫秒/像素, 滑动1像素需要多少毫秒.

                     默认为 (25F/densityDpi) 毫秒/像素

                     mdpi上, 1英寸有160个像素点, 25/160,
                     xxhdpi,1英寸有480个像素点, 25/480,
                  */

                    //return 10F / displayMetrics.densityDpi;//可以减少时间，默认25F
                    return super.calculateSpeedPerPixel(displayMetrics);
                }

                //该方法计算滑动所需时间。在此处间接控制速度。
                //Calculates the time it should take to scroll the given distance (in pixels)
                @Override
                protected int calculateTimeForScrolling(int dx) {
               /*
                   控制距离, 然后根据上面那个方(calculateSpeedPerPixel())提供的速度算出时间,

                   默认一次 滚动 TARGET_SEEK_SCROLL_DISTANCE_PX = 10000个像素,

                   在此处可以减少该值来达到减少滚动时间的目的.
                */

                    //间接计算时提高速度，也可以直接在calculateSpeedPerPixel提高
                    if (dx > 3000) {
                        dx = 3000;
                    }

                    int time = super.calculateTimeForScrolling(dx);
//                    LogUtil.LogUtild(time);//打印时间看下

                    return time;
                }
            };

            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

}
