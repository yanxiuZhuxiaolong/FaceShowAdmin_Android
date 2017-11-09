package com.yanxiu.gphone.faceshowadmin_android.course.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author frc  on 17-11-9.
 */

public class CourseBriefIntroductionFragment extends FaceShowBaseFragment {
    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setContentView(R.layout.fragment_brief_introduction_layout);
        GetCourseResponse.CourseBean course = getArguments() != null ? (GetCourseResponse.CourseBean) getArguments().get("data") : null;
        if (course != null && !TextUtils.isEmpty(course.getBriefing())) {
            mTvContent.setText(Html.fromHtml(course.getBriefing()));
        } else {
            mPublicLoadLayout.showOtherErrorView(getString(R.string.no_brief));
        }
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);
        return mPublicLoadLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
