package com.yanxiu.gphone.faceshowadmin_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.customView.PublicLoadLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.hello_word)
    TextView helloWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PublicLoadLayout publicLoadLayout = new PublicLoadLayout(this);
        publicLoadLayout.setContentView(R.layout.activity_main);
        setContentView(publicLoadLayout);
        publicLoadLayout.showOtherErrorView("nothing");

        ButterKnife.bind(this);
    }
}
