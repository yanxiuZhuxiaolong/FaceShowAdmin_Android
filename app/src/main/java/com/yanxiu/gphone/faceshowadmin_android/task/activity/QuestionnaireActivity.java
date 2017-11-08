package com.yanxiu.gphone.faceshowadmin_android.task.activity;

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
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetVotesRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetVotesResponse;
import com.yanxiu.gphone.faceshowadmin_android.task.adapter.QuestionnaireAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author frc
 */
public class QuestionnaireActivity extends FaceShowBaseActivity {
    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPublicLoadLayout = new PublicLoadLayout(this);
        mPublicLoadLayout.setContentView(R.layout.activity_vote);
        setContentView(mPublicLoadLayout);
        ButterKnife.bind(this);
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
        mTitleLayoutTitle.setText(R.string.questionnaire_detail);
        mTitleLayoutTitle.setVisibility(View.VISIBLE);


        getVotes();
    }

    private void getVotes() {
        GetVotesRequest getVotesRequest = new GetVotesRequest();
        getVotesRequest.stepId = getIntent().getStringExtra("stepId");
        getVotesRequest.startRequest(GetVotesResponse.class, new HttpCallback<GetVotesResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetVotesResponse ret) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QuestionnaireActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                QuestionnaireAdapter questionnaireAdapter = new QuestionnaireAdapter(ret.getData().getQuestionGroup());
                mRecyclerView.setAdapter(questionnaireAdapter);

            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

    @OnClick(R.id.title_layout_left_img)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;

            default:
        }
    }
}
