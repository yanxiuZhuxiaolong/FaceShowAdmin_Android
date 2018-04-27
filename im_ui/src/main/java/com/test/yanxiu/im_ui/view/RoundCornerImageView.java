package com.test.yanxiu.im_ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.test.yanxiu.im_ui.R;

/**
 * Created by sunpeng on 2018/3/29.
 */

public class RoundCornerImageView extends AppCompatImageView {
    private float mCornerRadius = 0f;
    private int mOverColor = Color.TRANSPARENT;
    public RoundCornerImageView(Context context) {
        super(context);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttrs(context,attrs);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttrs(context,attrs);
    }

    private void setupAttrs(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView);
        mCornerRadius = a.getDimensionPixelOffset(R.styleable.RoundCornerImageView_radius,0);
        mOverColor = a.getColor(R.styleable.RoundCornerImageView_over_color,Color.TRANSPARENT);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mCornerRadius > 0){
            Path path = new Path();
            RectF rectF = new RectF(0,0,getWidth(),getHeight());
            path.addRoundRect(rectF,mCornerRadius,mCornerRadius,Path.Direction.CW);
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
        if(mOverColor != Color.TRANSPARENT){
            canvas.drawColor(mOverColor);
        }
    }

    public void clearOverLayer(){
        mOverColor = Color.TRANSPARENT;
        postInvalidate();
    }
}
