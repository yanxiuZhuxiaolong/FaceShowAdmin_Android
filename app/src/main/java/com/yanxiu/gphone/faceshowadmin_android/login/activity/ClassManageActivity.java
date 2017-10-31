package com.yanxiu.gphone.faceshowadmin_android.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.MainActivity;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshowadmin_android.db.SpManager;
import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.login.adapter.ClassManagerListAdapter;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.GetClazzListResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassManageActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    public static void toThisAct(Activity activity, GetClazzListResponse.DataBean data) {
        Intent intent = new Intent(activity, ClassManageActivity.class);
        intent.putExtra("classInfos", data);
        activity.startActivity(intent);
    }

    private GetClazzListResponse.DataBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_manage_actvity);
        ButterKnife.bind(this);
        data = (GetClazzListResponse.DataBean) getIntent().getSerializableExtra("classInfos");
        titleLayoutTitle.setText(R.string.manager_clazz);
        titleLayoutTitle.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        ClassManagerListAdapter classManagerListAdapter = new ClassManagerListAdapter(data.getClazsInfos());
        recyclerView.setAdapter(classManagerListAdapter);
        classManagerListAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                SpManager.saveCurrentClassInfo(data.getClazsInfos().get(position));
                MainActivity.invoke(ClassManageActivity.this, data.getClazsInfos().get(position));
                ClassManageActivity.this.finish();
            }
        });
    }

}
