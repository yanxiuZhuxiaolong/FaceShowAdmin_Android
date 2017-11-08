package com.yanxiu.gphone.faceshowadmin_android.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/15 17:21.
 * Function :
 */
public class SizeChangeCallbackView extends View {

    private onViewSizeChangedCallback mViewSizeChangedCallback;
    private int mHeight;

    public interface onViewSizeChangedCallback {
        void sizeChanged(int visibility, int bottom);
    }

    public SizeChangeCallbackView(Context context) {
        super(context);
    }

    public SizeChangeCallbackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SizeChangeCallbackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewSizeChangedCallback(onViewSizeChangedCallback viewSizeChangedCallback) {
        this.mViewSizeChangedCallback = viewSizeChangedCallback;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        Logger.d("onSizeChanged", "height" + h);
        if (mHeight == 0) {
            mHeight = h*2/3;
        }
        int visibility;
        if (h > mHeight) {
            visibility = INVISIBLE;
        } else {
            visibility = VISIBLE;
        }
        if (mViewSizeChangedCallback != null) {
            mViewSizeChangedCallback.sizeChanged(visibility, h);
        }
    }
}
