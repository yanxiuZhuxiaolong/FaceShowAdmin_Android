package com.yanxiu.gphone.faceshowadmin_android.course;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;

/**
 * Created by frc on 17-11-8.
 */

public class CourseResourceFragment extends FaceShowBaseFragment {
    PublicLoadLayout mPublicLoadLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setContentView(inflater.inflate(R.layout.fragment_course_resource_layout, null));
        return mPublicLoadLayout;
    }
}
