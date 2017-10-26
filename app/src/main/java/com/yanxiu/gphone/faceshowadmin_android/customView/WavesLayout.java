package com.yanxiu.gphone.faceshowadmin_android.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.faceshowadmin_android.R;


/**
 * Created by Canghaixiao.
 * Time : 2017/5/4 11:33.
 * Function :
 */

@SuppressWarnings("unused")
public class WavesLayout extends RelativeLayout {

    private static final int DEFAULT_FRAME_RATE = 10;
    private static final int DEFAULT_DURATION = 200;
    private static final int DEFAULT_ALPHA = 255;
    private static final int DEFAULT_END_ALPHA=100;
    private static final int DEFAULT_ALPHA_STEP = 5;
    private static final int DEFAULT_RADIUS = 10;
    public static final int DEFAULT_COLOR = Color.LTGRAY;
    private static final int DEFAULT_SHAPE_RADIUS = 0;
    private static final boolean DEFAULT_ENABLE = true;
    /**
     * 动画帧率
     */
    private int mFrameRate = DEFAULT_FRAME_RATE;
    /**
     * 渐变动画持续时间
     */
    private int mDuration = DEFAULT_DURATION;
    /**
     * 颜色的初始alpha值, (0, 255)
     */
    private int mColorAlpha = DEFAULT_ALPHA;
    /**
     * 最终的颜色alpha值,(0,255)需要小于初始值
     * */
    private int mAnimEndColorAlpha;
    /**
     * 每次动画Alpha的渐变递减值
     */
    private int mAlphaStep = DEFAULT_ALPHA_STEP;
    /**
     * 起始的圆形背景半径
     */
    private int mRadius = DEFAULT_RADIUS;
    /**
     * 最大的半径
     */
    private int mMaxRadius = DEFAULT_RADIUS;
    /**
     * 渐变的背景色
     */
    private int mCirclelColor = DEFAULT_COLOR;
    /**
     * 画笔
     */
    private Paint mPaint = new Paint();
    /**
     * 圆角信息
     */
    private int mShapeRadius;
    private int mShapeRadius_top_left;
    private int mShapeRadius_top_right;
    private int mShapeRadius_bottom_left;
    private int mShapeRadius_bottom_right;
    /**
     * 被点击的位置
     */
    private Point mClickPoint = null;
    /**
     * 视图的Rect
     */
    private RectF mTargetRectf;
    /**
     * 每次重绘时半径的增幅
     */
    private int mRadiusStep = 1;
    /**
     * 保存用户设置的alpha值
     */
    private int mBackupAlpha;
    /**
     * 被电击的view
     */
    private View mTargetView;
    /**
     * 默认是否可以显示波纹
     */
    private boolean mCanShowWave = true;

    private View mOtherView;//dyf添加

    private float[] floats = new float[8];

    public WavesLayout(Context context) {
        this(context, null);
    }

    public WavesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WavesLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        if (attrs != null) {
            initTypedArray(context, attrs);
        }

        initPaint();

        this.setWillNotDraw(false);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WavesLayout);
        mCirclelColor = typedArray.getColor(R.styleable.WavesLayout_waves_color, DEFAULT_COLOR);
        mDuration = typedArray.getInteger(R.styleable.WavesLayout_waves_duration, DEFAULT_DURATION);
        mFrameRate = typedArray.getInteger(R.styleable.WavesLayout_waves_framerate, DEFAULT_FRAME_RATE);
        mColorAlpha = typedArray.getInteger(R.styleable.WavesLayout_waves_alpha, DEFAULT_ALPHA);
        mAnimEndColorAlpha=typedArray.getInteger(R.styleable.WavesLayout_waves_animend_alpha,DEFAULT_END_ALPHA);
        mCanShowWave = typedArray.getBoolean(R.styleable.WavesLayout_waves_canshowwave, DEFAULT_ENABLE);
        mShapeRadius = typedArray.getDimensionPixelSize(R.styleable.WavesLayout_waves_radius, DEFAULT_SHAPE_RADIUS);
        mShapeRadius_bottom_left = typedArray.getDimensionPixelSize(R.styleable.WavesLayout_waves_radius_bottom_left, DEFAULT_SHAPE_RADIUS);
        mShapeRadius_bottom_right = typedArray.getDimensionPixelSize(R.styleable.WavesLayout_waves_radius_bottom_right, DEFAULT_SHAPE_RADIUS);
        mShapeRadius_top_left = typedArray.getDimensionPixelSize(R.styleable.WavesLayout_waves_radius_top_left, DEFAULT_SHAPE_RADIUS);
        mShapeRadius_top_right = typedArray.getDimensionPixelSize(R.styleable.WavesLayout_waves_radius_top_right, DEFAULT_SHAPE_RADIUS);
        typedArray.recycle();
        initFloats();
    }

    private void initFloats() {
        floats[0] = mShapeRadius_top_left;
        floats[1] = mShapeRadius_top_left;
        floats[2] = mShapeRadius_top_right;
        floats[3] = mShapeRadius_top_right;
        floats[4] = mShapeRadius_bottom_right;
        floats[5] = mShapeRadius_bottom_right;
        floats[6] = mShapeRadius_bottom_left;
        floats[7] = mShapeRadius_bottom_left;
    }

    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCirclelColor);
        mPaint.setAlpha(mColorAlpha);
        mBackupAlpha = mColorAlpha;
    }

    public void setWaveColor(@ColorInt int color){
        this.mCirclelColor=color;
        mPaint.setColor(mCirclelColor);
    }
    public void setOtherView(View view){
        mOtherView = view;
    }

    public void setCanShowWave(boolean isShowWave) {
        this.mCanShowWave = isShowWave;
    }

    public void setRadiusCorner(int radiusCorner) {
        this.mShapeRadius = radiusCorner;
    }

    public void setRadiusCorner(int top_left, int top_right, int bottom_left, int bottom_right) {
        this.mShapeRadius = 0;
        this.mShapeRadius_top_left = top_left;
        this.mShapeRadius_top_right = top_right;
        this.mShapeRadius_bottom_left = bottom_left;
        this.mShapeRadius_bottom_right = bottom_right;
        initFloats();
    }

    private boolean isInFrame(View touchView, float x, float y) {
        initViewRect(touchView);
        return mTargetRectf.contains(x, y);
    }

    private void initViewRect(View touchView) {
        int[] location = new int[2];
        touchView.getLocationOnScreen(location);
        mTargetRectf = new RectF(location[0], location[1], location[0] + touchView.getWidth(), location[1] + touchView.getHeight());
    }

    /**
     * 换算当前view的位置
     */
    private void removeExtraWH(float x, float y) {
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        mTargetRectf.top -= location[1];
        mTargetRectf.bottom -= location[1];
        mTargetRectf.left -= location[0];
        mTargetRectf.right -= location[0];
        mClickPoint = new Point((int) x, (int) y);
    }

    private View findViewByLocation(ViewGroup viewGroup, float x, float y) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof ViewGroup) {
                return findViewByLocation((ViewGroup) childView, x, y);
            } else if (isInFrame(childView, x, y)) {
                return childView;
            }
        }
        return null;
    }

    private boolean isAnimEnd() {
        return mRadius >= mMaxRadius;
    }

    private void calculateMaxRadius(View view) {

        int width = view.getWidth();
        int height = view.getHeight();

        mMaxRadius = (int) Math.sqrt(width * width + height * height);

        int redrawCount = mDuration / mFrameRate;
        mRadiusStep = (mMaxRadius - DEFAULT_RADIUS) / redrawCount;
        mAlphaStep = (mColorAlpha - mAnimEndColorAlpha) / redrawCount;
    }

    private void deliveryTouchDownEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTargetView = findViewByLocation(this, event.getRawX(), event.getRawY());
            if (mTargetView != null) {
                removeExtraWH(event.getX(), event.getY());
                calculateMaxRadius(mTargetView);
                invalidate();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCanShowWave) {
                    deliveryTouchDownEvent(ev);
                }
                if(mOtherView != null)
                    mOtherView.setPressed(true);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(mOtherView != null)
                    mOtherView.setPressed(false);
                reset();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawRippleIfNecessary(canvas);
    }

    private void drawRippleIfNecessary(Canvas canvas) {
        if (isFoundTouchedSubView()) {
            mRadius += mRadiusStep;
            mColorAlpha -= mAlphaStep;

            Path path = new Path();
            if (mShapeRadius != 0) {
                path.addRoundRect(mTargetRectf, mShapeRadius, mShapeRadius, Path.Direction.CW);
            } else {
                path.addRoundRect(mTargetRectf, floats, Path.Direction.CW);
            }
            canvas.clipPath(path);

            mPaint.setAlpha(mColorAlpha);
            canvas.drawCircle(mClickPoint.x, mClickPoint.y, mRadius, mPaint);
        }

        if (!isAnimEnd()) {
            invalidateDelayed();
        }
    }

    private void invalidateDelayed() {
        this.postDelayed(new drawRunable(), mFrameRate);
    }

    private class drawRunable implements Runnable {

        @Override
        public void run() {
            invalidate();
        }
    }

    private boolean isFoundTouchedSubView() {
        return mClickPoint != null && mTargetView != null;
    }

    private void reset() {
        mClickPoint = null;
        mTargetRectf = null;
        mRadius = DEFAULT_RADIUS;
        mColorAlpha = mBackupAlpha;
        mTargetView = null;
        invalidate();
    }
}
