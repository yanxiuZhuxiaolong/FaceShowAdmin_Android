package com.yanxiu.gphone.faceshowadmin_android.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yanxiu.gphone.faceshowadmin_android.R;

import butterknife.BindView;

/**
 * main tab
 * Created by frc on 17-10-26.
 */

public class MainFragment extends Fragment {
    @BindView(R.id.tab_recyclerView)
    RecyclerView mTab_recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_layout, container, false);
        initView();
        return view;
    }

    private void initView(){

    }
}
