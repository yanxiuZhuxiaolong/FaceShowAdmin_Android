package com.yanxiu.gphone.faceshowadmin_android.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetTasksRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetTasksResponse;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    private PublicLoadLayout mPublicLoadLayout;
    private TaskAdapter mTaskAdapter;
    private UUID mUUID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setContentView(R.layout.fragment_task_layout);
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
        mTitleLayoutTitle.setText(R.string.task);
        mTitleLayoutTitle.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mTaskAdapter = new TaskAdapter();
        mRecyclerView.setAdapter(mTaskAdapter);
        mPublicLoadLayout.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTasks();
            }
        });
        mTaskAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });
        getTasks();
        return mPublicLoadLayout;
    }

    private void getTasks() {
        mPublicLoadLayout.showLoadingView();
        GetTasksRequest getTasksRequest = new GetTasksRequest();
        getTasksRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        mUUID = getTasksRequest.startRequest(GetTasksResponse.class, new HttpCallback<GetTasksResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetTasksResponse ret) {

                mPublicLoadLayout.hiddenOtherErrorView();
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    if (ret.getData() != null && ret.getData().getTasks() != null && ret.getData().getTasks().size() > 0) {
                        mTaskAdapter.update(ret.getData().getTasks());
                    } else {
                        mPublicLoadLayout.showOtherErrorView(getString(R.string.data_empty));
                    }
                } else {
                    mPublicLoadLayout.showOtherErrorView(ret.getMessage());

                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.hiddenOtherErrorView();
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
    }
}
