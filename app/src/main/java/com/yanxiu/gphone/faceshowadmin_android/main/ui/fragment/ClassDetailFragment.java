package com.yanxiu.gphone.faceshowadmin_android.main.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lufengqing on 2017/10/31.
 */

public class ClassDetailFragment extends FaceShowBaseFragment{
    private PublicLoadLayout mRootView;
    private String mClassName;
    private String mStartTime;
    private String mEndTime;
    private String mDescription;
    private String mStudensNum;
    private String mMasterNum;

    @BindView(R.id.detail_title)
    TextView detail_title;
    @BindView(R.id.detail_duration)
    TextView detail_duration;
    @BindView(R.id.detail_descr)
    TextView detail_descr;
    @BindView(R.id.num_class_member)
    TextView num_class_member;
    @BindView(R.id.num_master)
    TextView num_master;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_class_detail);
        unbinder = ButterKnife.bind(this,mRootView);
        initData();
        return mRootView;
    }


    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mClassName = bundle.getString("className");
            mStartTime = bundle.getString("startTime");
            mEndTime = bundle.getString("endTime");
            mDescription = bundle.getString("description");
            mStudensNum = bundle.getString("studensNum");
            mMasterNum = bundle.getString("masterNum");
        }
        detail_title.setText(mClassName);
        detail_duration.setText(getString(R.string.detail_duration, mStartTime, mEndTime));
        detail_descr.setText(mDescription);
        num_master.setText(getResources().getString(R.string.num_master, mMasterNum));
        num_class_member.setText(getResources().getString(R.string.num_class_member, mStudensNum));
    }

}
