package com.yanxiu.gphone.faceshowadmin_android.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/20 12:01.
 * Function :解决glide加载图片不能按比例放大的问题
 */
public class FitXYImageView extends android.support.v7.widget.AppCompatImageView {

    private int mWidth;
    private int mHeight;

    public FitXYImageView(Context context) {
        super(context);
        init(context);
    }

    public FitXYImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FitXYImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm.getWidth() < mWidth) {
            int w = bm.getWidth();
            int h = bm.getHeight();
            bm = Bitmap.createScaledBitmap(bm, mWidth, h * mWidth / w, false);
        }
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bm=bitmapDrawable.getBitmap();
                if (bm.getWidth() < mWidth) {
                    int w = bm.getWidth();
                    int h = bm.getHeight();
                    bm = Bitmap.createScaledBitmap(bm, mWidth, h * mWidth / w, false);
                    drawable=new BitmapDrawable(bm);
                }
            } else if (drawable instanceof GlideBitmapDrawable) {
                GlideBitmapDrawable glideBitmapDrawable= (GlideBitmapDrawable) drawable;
                Bitmap bm=glideBitmapDrawable.getBitmap();
                if (bm.getWidth() < mWidth) {
                    int w = bm.getWidth();
                    int h = bm.getHeight();
                    bm = Bitmap.createScaledBitmap(bm, mWidth, h * mWidth / w, false);
                    drawable=new GlideBitmapDrawable(getResources(),bm);
                }
            }
        }
        super.setImageDrawable(drawable);
    }
}
