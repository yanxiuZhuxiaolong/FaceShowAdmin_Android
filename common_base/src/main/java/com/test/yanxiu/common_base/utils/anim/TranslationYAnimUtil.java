package com.test.yanxiu.common_base.utils.anim;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.test.yanxiu.common_base.utils.ScreenUtils;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/29 12:16.
 * Function :
 */
public class TranslationYAnimUtil {

    private static final int DEFAULT_DURATION = 200;

    private static TranslationYAnimUtil translationYAnimUtil;
    private TranslationAnim translationAnim = new TranslationAnim();
    private TranslationAnimUpdata translationAnimUpdata = new TranslationAnimUpdata();
    private int mTranslationY = 0;
    private int mDuration = 0;

    private TranslationYAnimUtil() {
        translationAnim.clear();
        translationAnimUpdata.clear();
    }

    public static TranslationYAnimUtil getInstence() {
        translationYAnimUtil = new TranslationYAnimUtil();
        return translationYAnimUtil;
    }

    public TranslationYAnimUtil setAnimViewHeight(Context context, @DimenRes int resId) {
        mTranslationY = context.getResources().getDimensionPixelSize(resId);
        int windowHeight = ScreenUtils.getScreenHeight(context);
        mDuration = DEFAULT_DURATION;
//        mDuration = mDuration * (windowHeight / mTranslationY);
        return translationYAnimUtil;
    }

    public TranslationYAnimUtil setDuration(Context context, int duration) {
        this.mDuration = duration;
//        int windowHeight = ScreenUtils.getScreenHeight(context);
//        mDuration = mDuration * (windowHeight / mTranslationY);
        return translationYAnimUtil;
    }

    public TranslationYAnimUtil setBgGradation(View view, float start, float end) {
        translationAnimUpdata.setParame(view, start, end, mTranslationY);
        translationAnim.setParame(view, start, end,translationAnimUpdata);
        return translationYAnimUtil;
    }

    public void setStartAnim(View view) {
        ViewCompat.setTranslationY(view, mTranslationY);

        ViewCompat.animate(view)
                .setInterpolator(new LinearInterpolator())
                .translationY(0)
                .setDuration(mDuration)
                .setUpdateListener(translationAnimUpdata)
                .setListener(translationAnim);
    }

    public void setCloseAnim(View view,onCloseFinishedListener listener){
        ViewCompat.setTranslationY(view, 0);
        translationAnim.setListener(listener);
        translationAnimUpdata.setListener(listener);
        ViewCompat.animate(view)
                .setInterpolator(new LinearInterpolator())
                .translationY(mTranslationY)
                .setDuration(mDuration)
                .setUpdateListener(translationAnimUpdata)
                .setListener(translationAnim);
    }

    private static class TranslationAnim implements ViewPropertyAnimatorListener {

        private View mWindowView;
        private TranslationAnimUpdata mAnimUpdata;
        private onCloseFinishedListener mListener;
        private float mStart;
        private float mEnd;

        private TranslationAnim() {

        }

        private void setParame(View view, float start, float end,TranslationAnimUpdata animUpdata) {
            this.mWindowView = view;
            this.mAnimUpdata=animUpdata;
            this.mStart = start;
            this.mEnd = end;
        }

        public void clear() {
            this.mListener=null;
            this.mWindowView = null;
            this.mStart = 0f;
            this.mEnd = 0f;
        }

        public void setListener(onCloseFinishedListener listener){
            this.mListener=listener;
        }

        @Override
        public void onAnimationStart(View view) {
            if (mWindowView != null) {
                mWindowView.setAlpha(mStart);
            }
        }

        @Override
        public void onAnimationEnd(View view) {
            if (mWindowView != null) {
                mWindowView.setAlpha(mEnd);
            }
            if (mListener!=null){
                mListener.onFinished();
            }
            clear();
            mAnimUpdata.clear();
        }

        @Override
        public void onAnimationCancel(View view) {
            clear();
            mAnimUpdata.clear();
        }
    }

    private static class TranslationAnimUpdata implements ViewPropertyAnimatorUpdateListener {

        private View mWindowView;
        private onCloseFinishedListener mListener;
        private float mStart;
        private float mEnd;
        private int mTranslationY;

        private TranslationAnimUpdata() {

        }

        private void setParame(View view, float start, float end, int translationY) {
            this.mWindowView = view;
            this.mStart = start;
            this.mEnd = end;
            this.mTranslationY = translationY;
        }

        public void clear() {
            this.mListener=null;
            this.mWindowView = null;
            this.mStart = 0f;
            this.mEnd = 0f;
            this.mTranslationY = 0;
        }

        public void setListener(onCloseFinishedListener listener){
            this.mListener=listener;
        }

        @Override
        public void onAnimationUpdate(final View view) {
            if (mWindowView != null) {
                int translationY = (int) view.getTranslationY();
//                        Logger.d("anim",translationY+"");
                if (mListener!=null){
                    float ratio=((float) Math.abs(mTranslationY) - (float) Math.abs(translationY))/ (float) Math.abs(mTranslationY);
                    mWindowView.setAlpha((mStart - mEnd) * ratio + mEnd);
                }else {
                    float ratio = ((float) Math.abs(mTranslationY) - (float) Math.abs(translationY)) / (float) Math.abs(mTranslationY);
                    mWindowView.setAlpha((mEnd - mStart) * ratio + mStart);
                }
            }
        }
    }

    public interface onCloseFinishedListener{
        void onFinished();
    }
}
