package com.yanxiu.gphone.faceshowadmin_android.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.faceshowadmin_android.R;

/**
 * task tab
 * Created by frc on 17-10-26.
 */

public class TaskFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_task_layout,container,false);
        return view;
    }
}
