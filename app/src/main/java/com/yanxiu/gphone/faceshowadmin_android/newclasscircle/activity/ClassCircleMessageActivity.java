package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.adapter.ClassCircleMessageAdapter;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.adapter.IRecyclerViewItemClick;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.ClassCircleNewMessageRequest;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.ClassCircleNewMessageResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 班级圈消息列表
 *
 * @author frc
 */
public class ClassCircleMessageActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private PublicLoadLayout mRootView;
    private ClassCircleMessageAdapter mClassCircleMessageAdapter;
    private List data;
    private ClassCircleNewMessageResponse mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_class_circle_message);
        setContentView(mRootView);
        ButterKnife.bind(this);
        initTitle();
        initRecyclerView();
        initListener();
        mRootView.showLoadingView();
        getMessageList();
    }

    private void initTitle() {
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
        mTitleLayoutTitle.setText(R.string.message);
        mTitleLayoutTitle.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        mClassCircleMessageAdapter = new ClassCircleMessageAdapter();
        mRecycler.setAdapter(mClassCircleMessageAdapter);

    }

    private void initListener() {
        mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootView.showLoadingView();
                getMessageList();
            }
        });
        mClassCircleMessageAdapter.setIRecyclerViewItemClick(new IRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(ClassCircleMessageActivity.this, ClassCircleDetailActivity.class);
                intent.putExtra("momentId", String.valueOf(mData.getData().getMsgs().get(postion).getMomentId()));
                startActivity(intent);
            }
        });

    }


    @OnClick(R.id.title_layout_left_img)
    public void onViewClicked() {
        this.finish();
    }

    private void getMessageList() {
        mRootView.showLoadingView();
        ClassCircleNewMessageRequest classCircleNewMessageRequest = new ClassCircleNewMessageRequest();
        classCircleNewMessageRequest.clazsId = SpManager.getUserInfo().getClassId();
        classCircleNewMessageRequest.startRequest(ClassCircleNewMessageResponse.class, new HttpCallback<ClassCircleNewMessageResponse>() {
            @Override
            public void onSuccess(RequestBase request, ClassCircleNewMessageResponse ret) {
                hideLoading();
                if (ret != null && ret.getCode() == 0) {
                    mData = ret;
                    if (ret.getData() != null && ret.getData().getMsgs() != null && ret.getData().getMsgs().size() > 0) {
                        if (data == null) {
                            data = new ArrayList();
                        }
                        data.addAll(ret.getData().getMsgs());
                        mClassCircleMessageAdapter.update(data);
                    } else {
                        mRootView.showOtherErrorView("无新消息");
                    }
                } else {
                    showOnSuccessErrorStatue(mRootView, data, ret);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                hideLoading();
                showOnFailErrorStatue(mRootView, data, error.getMessage());
            }
        });
    }

    private void hideLoading() {
        mRootView.finish();
    }

    /**
     * 如何显示网络请求Success回调异常页面
     *
     * @param publicLoadLayout 多状态封装体
     * @param data             已存储的数据
     * @param response         网络返回数据
     */
    private void showOnSuccessErrorStatue(PublicLoadLayout publicLoadLayout, List data, FaceShowBaseResponse response) {
        if (data != null && data.size() > 0) {
            if (response != null && response.getError() != null && !TextUtils.isEmpty(response.getError().getMessage())) {
                ToastUtil.showToast(getApplicationContext(), response.getError().getMessage());
            } else {
                ToastUtil.showToast(getApplicationContext(), getString(R.string.data_refresh_fail));
            }
        } else {
            if (response != null && response.getError() != null && !TextUtils.isEmpty(response.getError().getMessage())) {
                publicLoadLayout.showOtherErrorView(response.getError().getMessage());
            } else {
                publicLoadLayout.showOtherErrorView();
            }

        }
    }

    /**
     * 如何显示网络请求Fail回调异常页面
     *
     * @param publicLoadLayout 多状态封装体
     * @param data             已存储的数据
     * @param message          提示语
     */
    private void showOnFailErrorStatue(PublicLoadLayout publicLoadLayout, List data, String message) {
        if (data != null && data.size() > 0) {
            ToastUtil.showToast(getApplicationContext(), TextUtils.isEmpty(message) ? getString(R.string.net_error) : message);
        } else {
            publicLoadLayout.showOtherErrorView();
        }
    }


}
