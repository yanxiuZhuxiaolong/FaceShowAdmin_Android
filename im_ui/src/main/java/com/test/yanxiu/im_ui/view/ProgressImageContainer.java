package com.test.yanxiu.im_ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.im_ui.R;

/**
 * Created by sunpeng on 2018/3/29.
 */

public class ProgressImageContainer extends FrameLayout {
    private XCRoundRectImageView mRoundCornerImage;
    private ImageView mAnimationImage;
    private TextView mProgress;
    private View mOverLayer;
    private View mImageViewOver;

    public ProgressImageContainer(@NonNull Context context) {
        super(context);
    }

    public ProgressImageContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressImageContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressImageContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.progress_container, this, true);
        mRoundCornerImage = view.findViewById(R.id.roundImageView);
        mAnimationImage = view.findViewById(R.id.animationImageView);
        mProgress = view.findViewById(R.id.progressText);
        mOverLayer = view.findViewById(R.id.overLayer);
        mImageViewOver = view.findViewById(R.id.img_over);
        mOverLayer.setVisibility(INVISIBLE);


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
//        rotateAnimation.setFillAfter(true);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        mAnimationImage.setAnimation(rotateAnimation);
        rotateAnimation.start();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimationImage.clearAnimation();
    }

    public void setProgress(int progress) {
        mProgress.setText(progress + "%");
        addOverLayer();
        if (progress >= 100 || progress < 0) {
            clearOverLayer();

        }
    }


    public void setImageResource(int resId) {
        mRoundCornerImage.setImageResource(resId);
    }

    private Bitmap bitmap;

    public void setImageBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        mRoundCornerImage.setImageBitmap(bitmap);
        mImageViewOver.setLayoutParams(mRoundCornerImage.getLayoutParams());
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void clearOverLayer() {
        mImageViewOver.setVisibility(GONE);
        mOverLayer.setVisibility(INVISIBLE);
    }

    public void addOverLayer() {
        mImageViewOver.setVisibility(VISIBLE);
        mOverLayer.setVisibility(VISIBLE);
    }


}
