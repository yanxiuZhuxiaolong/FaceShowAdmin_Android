package com.test.yanxiu.im_ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yanxiu.im_ui.callback.OnNaviLeftBackCallback;

/**
 * Created by cailei on 08/03/2018.
 */

public class ImTitleLayout extends RelativeLayout {
    private OnNaviLeftBackCallback onNaviLeftBackCallback;
    private TextView mTitleTextView;
    private LinearLayout mLeftView;
    private LinearLayout mRightView;

    private ImageView mDefaultBackImageView;

    // 和setLeftView互斥
    public void setOnNaviLeftBackCallback(final OnNaviLeftBackCallback onNaviLeftBackCallback) {
        this.onNaviLeftBackCallback = onNaviLeftBackCallback;
//        setLeftView(mDefaultBackImageView);
        mDefaultBackImageView.setVisibility(VISIBLE);
        mDefaultBackImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onNaviLeftBackCallback != null) {
                    onNaviLeftBackCallback.onNaviBack();
                }
            }
        });
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    // 和setOnNaviLeftBackCallback互斥
    public void setLeftView(View v) {
        mDefaultBackImageView.setVisibility(GONE);
        makeViewIndependence(v);
        mLeftView.addView(v);
    }

    public void setRightView(View v) {
        makeViewIndependence(v);
        mRightView.addView(v);
    }

    private void makeViewIndependence(View v) {
        ViewGroup vg = (ViewGroup) v.getParent();
        if (vg != null) {
            vg.removeView(v);
            v.setVisibility(View.VISIBLE);
        }
    }

    public ImTitleLayout(Context context) {
        super(context);
        setup(context);
    }

    public ImTitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public ImTitleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    private void setup(Context context) {
        inflate(context, R.layout.layout_im_title, this);
        mTitleTextView = findViewById(R.id.title_textview);
        mLeftView = findViewById(R.id.left_view);
        mRightView = findViewById(R.id.right_view);
        mDefaultBackImageView = findViewById(R.id.navi_left_back_imageview);

    }
}
