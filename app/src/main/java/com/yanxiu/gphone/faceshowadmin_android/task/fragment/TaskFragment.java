package com.yanxiu.gphone.faceshowadmin_android.task.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.MainActivity;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.activity.CheckInDetailActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetTasksRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetTasksResponse;
import com.yanxiu.gphone.faceshowadmin_android.task.activity.QuestionnaireActivity;
import com.yanxiu.gphone.faceshowadmin_android.task.activity.VoteActivity;
import com.yanxiu.gphone.faceshowadmin_android.task.adapter.TaskAdapter;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * task tab
 * Created by frc on 17-10-26.
 */

public class TaskFragment extends FaceShowBaseFragment {
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private PublicLoadLayout mPublicLoadLayout;
    private TaskAdapter mTaskAdapter;
    private UUID mUUID;
    private List<GetTasksResponse.TasksBean> mTasks;
    public static final int typeVote = 3;
    public static final int typeSignIn = 6;
    public static final int typeQuestionnaire = 5;
    private static final int REQUEST_CODE_TO_SIGN_IN = 0x200;
    private RecyclerViewItemClickListener mRecyclerViewItemClickListener = new RecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            GetTasksResponse.TasksBean task = mTasks.get(position);
            Intent intent;
            switch (task.getInteractType()) {
                case typeVote:
                    intent = new Intent(getContext(), VoteActivity.class);
                    intent.putExtra("stepId", String.valueOf(task.getStepId()));
                    startActivity(intent);
                    break;
                case typeSignIn:
                    intent = new Intent(getContext(), CheckInDetailActivity.class);
                    intent.putExtra("stepId", String.valueOf(task.getStepId()));
                    startActivityForResult(intent, REQUEST_CODE_TO_SIGN_IN);
                    break;
                case typeQuestionnaire:
                    intent = new Intent(getContext(), QuestionnaireActivity.class);
                    intent.putExtra("stepId", String.valueOf(task.getStepId()));
                    startActivity(intent);
                    break;
                default:
            }
        }
    };

    public void refresh(){
        getTasks();
    }
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setContentView(R.layout.fragment_task_layout);
        mPublicLoadLayout.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTasks();
            }
        });
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);
        initTitle();
        initRecyclerView();
        mPublicLoadLayout.showLoadingView();
        getTasks();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTasks();
            }
        });
        return mPublicLoadLayout;
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mTaskAdapter = new TaskAdapter();
        mRecyclerView.setAdapter(mTaskAdapter);
        mTaskAdapter.addItemClickListener(mRecyclerViewItemClickListener);
    }

    private void initTitle() {
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
        mTitleLayoutLeftImg.setImageResource(R.drawable.selector_main_leftdrawer);
        mTitleLayoutTitle.setText(R.string.task);
        mTitleLayoutTitle.setVisibility(View.VISIBLE);
    }


    private void getTasks() {

        GetTasksRequest getTasksRequest = new GetTasksRequest();
        getTasksRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        mUUID = getTasksRequest.startRequest(GetTasksResponse.class, new HttpCallback<GetTasksResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetTasksResponse ret) {
                mPublicLoadLayout.finish();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    if (ret.getData() != null && ret.getData().getTasks() != null && ret.getData().getTasks().size() > 0) {
                        mTasks = ret.getData().getTasks();
                        mTaskAdapter.update(ret.getData().getTasks());
                    } else {
                        mPublicLoadLayout.showOtherErrorView(getString(R.string.no_task));
                    }
                } else {
                    mPublicLoadLayout.showOtherErrorView(ret.getError().getMessage());

                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.finish();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mPublicLoadLayout.showNetErrorView();
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mUUID != null) {
            RequestBase.cancelRequestWithUUID(mUUID);
        }
    }

    @OnClick(R.id.title_layout_left_img)
    public void onViewClicked() {
        ((MainActivity) getActivity()).openLeftDrawer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_TO_SIGN_IN == requestCode) {
            if (RESULT_OK == resultCode) {
                mPublicLoadLayout.showLoadingView();
                getTasks();
            }
        }
    }
}
