package com.yanxiu.gphone.faceshowadmin_android.main.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ProjectDetailFragment extends  FaceShowBaseFragment{
    private PublicLoadLayout mRootView;
    private String mProjectName;
    private String mStartTime;
    private String mEndTime;
    private String mDescription;

    @BindView(R.id.detail_title)
    TextView detail_title;
    @BindView(R.id.detail_duration)
    TextView detail_duration;
    @BindView(R.id.detail_descr)
    TextView detail_descr;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_project_detail);
        unbinder = ButterKnife.bind(this ,mRootView);
        initData();
        return mRootView;
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mProjectName = bundle.getString("projectName");
            mStartTime = bundle.getString("startTime");
            mEndTime = bundle.getString("endTime");
            mDescription = bundle.getString("description");
        }
        detail_title.setText(mProjectName);
        detail_duration.setText(getResources().getString(R.string.detail_duration, mStartTime.split(" ")[0], mEndTime.split(" ")[0]));
        detail_descr.setText(mDescription);
    }
}
