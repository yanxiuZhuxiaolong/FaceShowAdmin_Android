package com.yanxiu.gphone.faceshowadmin_android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.yanxiu.gphone.faceshowadmin_android.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/22 16:09.
 * Function :
 */
public class CornersImageTarget extends BitmapImageViewTarget {

    private Context mContext;
    private int mRadius;

    public CornersImageTarget(Context context, ImageView view, int radius) {
        super(view);
        this.mContext=context;
        this.mRadius=radius;
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getContext().getResources(), drawableToBitmap(placeholder));
        circularBitmapDrawable.setCornerRadius(mRadius);
        view.setImageDrawable(circularBitmapDrawable);
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getContext().getResources(), drawableToBitmap(errorDrawable));
        circularBitmapDrawable.setCornerRadius(mRadius);
        view.setImageDrawable(circularBitmapDrawable);
    }

    @Override
    protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getContext().getResources(), resource);
        circularBitmapDrawable.setCornerRadius(mRadius);
        view.setImageDrawable(circularBitmapDrawable);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(ContextCompat.getColor(mContext, R.color.color_dadde0));
//        canvas.drawColor(ContextCompat.getColor(mContext, R.color.color_EBEFF2));
        drawable.setBounds(width * 3 / 16, height * 3 / 16, width * 13 / 16, height * 13 / 16);
        drawable.draw(canvas);
        return bitmap;
    }

}
