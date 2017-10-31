package com.yanxiu.gphone.faceshowadmin_android.checkIn;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.base.FaceShowBaseActivity;

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

    @Override
    protected void toThisAct(Context context) {
        super.toThisAct(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_notes);
        ButterKnife.bind(this);
        initTitle();
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
                break;
            case R.id.title_layout_right_img:
                break;
        }
    }
}
