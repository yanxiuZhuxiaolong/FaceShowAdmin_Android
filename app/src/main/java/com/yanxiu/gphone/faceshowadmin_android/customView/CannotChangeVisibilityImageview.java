package com.yanxiu.gphone.faceshowadmin_android.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/20 17:51.
 * Function :
 */
public class CannotChangeVisibilityImageview extends android.support.v7.widget.AppCompatImageView {
    public CannotChangeVisibilityImageview(Context context) {
        super(context);
    }

    public CannotChangeVisibilityImageview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CannotChangeVisibilityImageview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setVisibility(int visibility) {
//        super.setVisibility(visibility);
    }
}

