package com.test.yanxiu.im_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.test.yanxiu.im_ui.R;

/**
 * Created by cailei on 12/03/2018.
 */

public class RoundCornerMaskView extends View {
    public void setmColor(int color) {
        this.mColor = color;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public RoundCornerMaskView(Context context) {
        super(context);
        setup(context,null);
    }

    public RoundCornerMaskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context,attrs);
    }

    public RoundCornerMaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context,attrs);
    }

    private void setup(Context context, @Nullable AttributeSet attrs) {
        mColor = Color.RED;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerMaskView);
            mColor = ta.getColor(R.styleable.RoundCornerMaskView_mask_color, 0xffffff); //默认白色
            mRadius = ta.getDimensionPixelSize(R.styleable.RoundCornerMaskView_corner_radius, 10);
        }
    }

    private int mColor;
    private int mRadius = 10;

    private Bitmap drawRoundCorner(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFFFFF); //notice:cannot set transparent color here.otherwise cannot clip at final.

        //左上
        c.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), 180, 90, true, p);
        // 左下
        c.drawArc(new RectF(0, getHeight() - mRadius * 2, mRadius * 2, getHeight()), 90, 90, true, p);
        // 右上
        c.drawArc(new RectF(getWidth() - mRadius * 2, 0, getWidth(), mRadius * 2), 270, 90, true, p);
        // 右下
        c.drawArc(new RectF(getWidth() - mRadius * 2, getHeight() - mRadius * 2, getWidth(), getHeight()), 0, 90, true, p);
        return bm;
    }

    private Bitmap drawRectCorner(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(mColor);
        // 左上
        c.drawRect(new RectF(0, 0, mRadius, mRadius), p);
        // 左下
        c.drawRect(new RectF(0, getHeight() - mRadius, mRadius, getHeight()), p);
        // 右上
        c.drawRect(new RectF(getWidth() - mRadius, 0, getWidth(), mRadius), p);
        // 右下
        c.drawRect(new RectF(getWidth() - mRadius, getHeight() - mRadius, getWidth(), getHeight()), p);
        return bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setFilterBitmap(false);
        paint.setStyle(Paint.Style.FILL);

        //create a canvas layer to show the mix-result
        @SuppressLint("WrongConstant")
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
                Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        // 用SRC_OUT方式，只留四角，挖空中间
        canvas.drawBitmap(drawRoundCorner(getWidth(), getHeight()), 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(drawRectCorner(getWidth(), getHeight()), 0, 0, paint);
        paint.setXfermode(null);
        //restore the canvas
        canvas.restoreToCount(sc);
    }
}
