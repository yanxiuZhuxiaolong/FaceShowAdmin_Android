package com.yanxiu.gphone.faceshowadmin_android.checkIn;

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
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetCheckInNotesRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetCheckInNotesResponse;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckInNotesActivity extends FaceShowBaseActivity {

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

    PublicLoadLayout publicLoadLayout;
    private CheckInNotesAdapter checkInNotesAdapter;

    public static void toThisAct(Context context, String checkInId) {
        Intent intent = new Intent(context, CheckInNotesActivity.class);
        intent.putExtra("checkInId", checkInId);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicLoadLayout = new PublicLoadLayout(this);
        publicLoadLayout.setContentView(R.layout.activity_check_in_notes);
        setContentView(publicLoadLayout);
        ButterKnife.bind(this);
        initTitle();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        checkInNotesAdapter = new CheckInNotesAdapter();
        recyclerView.setAdapter(checkInNotesAdapter);
        checkInNotesAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // TODO: 17-10-31  
            }
        });

        getCheckInNotesData();
//        publicLoadLayout.setRetryButtonOnclickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getCheckInNotesData();
//            }
//        });
    }

    private void getCheckInNotesData() {
        publicLoadLayout.showLoadingView();
        GetCheckInNotesRequest getCheckInNotesRequest = new GetCheckInNotesRequest();
        getCheckInNotesRequest.clazsId = String.valueOf(SpManager.getCurrentClassInfo().getId());
        getCheckInNotesRequest.startRequest(GetCheckInNotesResponse.class, new HttpCallback<GetCheckInNotesResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetCheckInNotesResponse ret) {
                publicLoadLayout.hiddenLoadingView();
                if (ret.getCode() == ResponseConfig.SUCCESS) {
                    if (ret.getData().getSignIns() != null && ret.getData().getSignIns().size() > 0) {
                        checkInNotesAdapter.update(ret.getData().getSignIns());
                    } else {
                        publicLoadLayout.showOtherErrorView(getString(R.string.check_in_notes_is_null));
                    }

                } else {
                    publicLoadLayout.showOtherErrorView(getString(R.string.data_error));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                publicLoadLayout.hiddenLoadingView();
                publicLoadLayout.showNetErrorView();
            }
        });
    }


    private void initTitle() {
        titleLayoutTitle.setText(R.string.check_in_notes);
        titleLayoutTitle.setVisibility(View.VISIBLE);
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutRightTxt.setText(R.string.create_new_notes);
        titleLayoutRightTxt.setVisibility(View.VISIBLE);
        titleLayoutRightImg.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                CheckInNotesActivity.this.finish();
                break;
            case R.id.title_layout_right_img:
                Intent intent = new Intent(CheckInNotesActivity.this, CreateNewCheckInActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }


}