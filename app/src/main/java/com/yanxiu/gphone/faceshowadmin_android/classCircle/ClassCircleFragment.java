package com.yanxiu.gphone.faceshowadmin_android.classCircle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.MainActivity;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.activity.SendClassCircleActivity;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.adapter.ClassCircleAdapter;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.dialog.ClassCircleDialog;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.request.ClassCircleCommentToMasterRequest;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.request.ClassCircleCommentToUserRequest;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.request.ClassCircleDeleteRequest;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.request.ClassCircleLikeRequest;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.request.ClassCircleRequest;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.ClassCircleDeleteResponse;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.CommentResponse;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.Comments;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.LikeResponse;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.RefreshClassCircle;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.customView.SizeChangeCallbackView;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshowadmin_android.login.activity.ClassManageActivity;
import com.yanxiu.gphone.faceshowadmin_android.utils.ClassCircleTimeUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdata;
import com.yanxiu.gphone.faceshowadmin_android.utils.FileUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.Logger;
import com.yanxiu.gphone.faceshowadmin_android.utils.ScreenUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.permission.OnPermissionCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * classCircle tab
 * Created by frc on 17-10-26.
 */

public class ClassCircleFragment extends Fragment implements LoadMoreRecyclerView.LoadMoreListener, View.OnClickListener, ClassCircleAdapter.onCommentClickListener, View.OnLongClickListener, ClassCircleAdapter.onLikeClickListener, SwipeRefreshLayout.OnRefreshListener, TextView.OnEditorActionListener, ClassCircleAdapter.onContentLinesChangedlistener, ClassCircleAdapter.onDeleteClickListener {

    private static final int REQUEST_CODE_ALBUM = 0x000;
    private static final int REQUEST_CODE_CAMERA = 0x001;
    private static final int REQUEST_CODE_CROP = 0x002;

    private LoadMoreRecyclerView mClassCircleRecycleView;
    private ClassCircleAdapter mClassCircleAdapter;
    private RelativeLayout mCommentLayout;
    private EditText mCommentView;
    private SizeChangeCallbackView mAdjustPanView;
    private ImageView mFunctionView;
    private TextView mTitleView;
    private ImageView mBackView;
    private View mTopView;
    private int mMomentPosition = -1;
    private int mCommentPosition = -1;
    private int mVisibility = View.INVISIBLE;
    private int mHeight;
    private boolean isCommentMaster;
    private String mCameraPath;
    private String mCropPath;
    private ClassCircleDialog mClassCircleDialog;
    private SwipeRefreshLayout mRefreshView;
    private PublicLoadLayout rootView;

    private boolean isCommentLoading = false;

    private UUID mClassCircleRequest;
    private UUID mClassCircleLikeRequest;
    private UUID mCommentToMasterRequest;
    private UUID mCommentToUserRequest;
    private UUID mCircleDeleteRequest;

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
    }

    private void initView(View rootView) {
        mTopView = rootView.findViewById(R.id.il_title);
        mBackView = (ImageView) rootView.findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.VISIBLE);
        mBackView.setImageResource(R.drawable.selector_main_leftdrawer);

        mTitleView = (TextView) rootView.findViewById(R.id.title_layout_title);
        mFunctionView = (ImageView) rootView.findViewById(R.id.title_layout_right_img);
        mFunctionView.setVisibility(View.VISIBLE);

        mCommentLayout = (RelativeLayout) rootView.findViewById(R.id.ll_edit);
        mCommentView = (EditText) rootView.findViewById(R.id.ed_comment);
        mAdjustPanView = (SizeChangeCallbackView) rootView.findViewById(R.id.sc_adjustpan);
        mClassCircleRecycleView = (LoadMoreRecyclerView) rootView.findViewById(R.id.lm_class_circle);
        mClassCircleRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mClassCircleAdapter = new ClassCircleAdapter(getContext());
        mClassCircleRecycleView.setAdapter(mClassCircleAdapter);

        mRefreshView = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_refresh);
    }

    private void listener() {
        mBackView.setOnClickListener(ClassCircleFragment.this);
        mClassCircleRecycleView.setLoadMoreListener(ClassCircleFragment.this);
        mClassCircleAdapter.setCommentClickListener(ClassCircleFragment.this);
        mClassCircleAdapter.setDeleteClickListener(ClassCircleFragment.this);
        mClassCircleAdapter.setThumbClickListener(ClassCircleFragment.this);
        mClassCircleAdapter.setContentLinesChangedlistener(ClassCircleFragment.this);
        mFunctionView.setOnClickListener(ClassCircleFragment.this);
        mFunctionView.setOnLongClickListener(ClassCircleFragment.this);
        mCommentView.setOnEditorActionListener(ClassCircleFragment.this);
        mRefreshView.setOnRefreshListener(ClassCircleFragment.this);
        rootView.setRetryButtonOnclickListener(this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.parseColor("#e6ffffff"));
        mTitleView.setText(R.string.classcircle);
        mFunctionView.setBackgroundResource(R.drawable.selector_classcircle_photo);
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
        mClassCircleRequest = circleRequest.startRequest(ClassCircleResponse.class, new HttpCallback<ClassCircleResponse>() {
            @Override
            public void onSuccess(RequestBase request, ClassCircleResponse ret) {
                mClassCircleRequest = null;
                mRefreshView.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshView.setRefreshing(false);
                    }
                });
                if (ret != null && ret.data != null && ret.data.moments != null) {
                    if (offset.equals("0")) {
                        mClassCircleAdapter.setData(ret.data.moments);
                    } else {
                        mClassCircleAdapter.addData(ret.data.moments);
                    }
                    mClassCircleRecycleView.setLoadMoreEnable(ret.data.hasNextPage);
                } else {
                    if (offset.equals("0")) {
                        rootView.showNetErrorView();
                        mClassCircleAdapter.clear();
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
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
     * 点赞
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
                    mClassCircleAdapter.notifyItemChanged(position, ClassCircleAdapter.REFRESH_LIKE_DATA);
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
                if (ret != null && ret.data != null) {
                    moments.comments.add(ret.data);
                    mClassCircleAdapter.notifyItemChanged(position, ClassCircleAdapter.REFRESH_COMMENT_DATA);
                    commentFinish();
                    mCommentView.setText("");
                } else {
                    ToastUtil.showToast(getContext(), R.string.error_tip);
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
                    mClassCircleAdapter.notifyItemChanged(position, ClassCircleAdapter.REFRESH_COMMENT_DATA);
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
            case R.id.title_layout_right_img:
                EventUpdata.onSendClassCircle(getContext());
                showDialog();
                break;
            case R.id.title_layout_left_img:
                ((MainActivity) getActivity()).openLeftDrawer();
                break;
            case R.id.retry_button:
                mRefreshView.setRefreshing(true);
                rootView.hiddenNetErrorView();
                onRefresh();
                break;
        }
    }

    private void showDialog() {
        if (mClassCircleDialog == null) {
            mClassCircleDialog = new ClassCircleDialog(getContext());
            mClassCircleDialog.setClickListener(new ClassCircleDialog.OnViewClickListener() {
                @Override
                public void onAlbumClick() {
                    FaceShowBaseActivity.requestWriteAndReadPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, REQUEST_CODE_ALBUM);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            ToastUtil.showToast(getContext(), R.string.no_storage_permissions);
                        }
                    });
                }

                @Override
                public void onCameraClick() {
                    FaceShowBaseActivity.requestCameraPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                            mCameraPath = FileUtils.getImageCatchPath(System.currentTimeMillis() + ".jpg");
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            File file = new File(mCameraPath);
                            if (file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Uri uri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(intent, REQUEST_CODE_CAMERA);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            ToastUtil.showToast(getContext(), R.string.no_camera_permissions);
                        }
                    });
                }
            });
        }
        mClassCircleDialog.show();
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
    public void deleteClick(int position, ClassCircleResponse.Data.Moments response) {
        EventUpdata.onDeleteClassCircle(getContext());
        startDeleteRequest(position, response);
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
    public void commentFinish() {
        mVisibility = View.INVISIBLE;
        mAdjustPanView.setViewSizeChangedCallback(null);
        mCommentLayout.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mAdjustPanView.getWindowToken(), 0);
        ((MainActivity) getActivity()).setBottomVisibility(View.VISIBLE);
        mMomentPosition = -1;
        mCommentPosition = -1;
    }

    @Override
    public boolean onLongClick(View v) {
        SendClassCircleActivity.LuanchActivity(getContext(), SendClassCircleActivity.TYPE_TEXT, null);
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
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
//            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(mAdjustPanView.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    @Override
    public void likeClick(int position, ClassCircleResponse.Data.Moments moments) {
        startLikeRequest(position, moments);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ALBUM:
                if (data != null) {
                    Uri uri = data.getData();
//                    mCropPath=FileUtil.getImageCatchPath(System.currentTimeMillis()+".jpg");
//                    startCropImg(uri,mCropPath);
                    String path = FileUtils.getRealFilePath(getContext(), uri);
                    startIntent(path);
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (!TextUtils.isEmpty(mCameraPath)) {
//                    mCropPath=FileUtil.getImageCatchPath(System.currentTimeMillis()+".jpg");
//                    startCropImg(Uri.fromFile(new File(mCameraPath)),mCropPath);
                    try {
                        new FileInputStream(new File(mCameraPath));
                        startIntent(mCameraPath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_CODE_CROP:
                if (!TextUtils.isEmpty(mCropPath)) {
                    if (new File(mCropPath).exists()) {
                        startIntent(mCropPath);
                    }
                }
                break;
        }
    }

    private void startCropImg(Uri uri, String savePath) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(savePath)));
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    private void startIntent(String path) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(path);
        SendClassCircleActivity.LuanchActivity(getContext(), SendClassCircleActivity.TYPE_IMAGE, strings);
    }

    @OnClick(R.id.title_layout_left_img)
    public void onViewClicked() {
        ((MainActivity) getActivity()).openLeftDrawer();
    }
}
