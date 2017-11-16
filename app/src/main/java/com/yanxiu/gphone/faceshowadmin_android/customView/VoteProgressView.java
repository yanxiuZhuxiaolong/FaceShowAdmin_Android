package com.yanxiu.gphone.faceshowadmin_android.customView;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.yanxiu.gphone.faceshowadmin_android.utils.ScreenUtils;


/**
 * 查看投票的进度条，不通用
 * Created by 戴延枫 on 2017年9月19日19:41:15.
 */

public class VoteProgressView extends View {
    /**
     * 进度条最大值
     */
    private float maxCount;
    /**
     * 进度条当前值
     */
    private float mCurrentProgress;
    /**
     * 每一道题的增长比例
     */
    private float mPercent;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;

    public VoteProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public VoteProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VoteProgressView(Context context) {
        super(context);
        initView(context);
    }


    private void initView(Context context) {
        if (isInEditMode()) {
            return;
        }
        mScreenWidth = ScreenUtils.dpToPxInt(getContext(),260);
    }

    /***
     * 设置最大的进度值
     *
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
        mPercent = mScreenWidth / maxCount;//每一道题的进度值
    }

    /***
     * 更新当前的进度值
     * @ currentProgress 当前答题的数量
     */
    public void updateProgress(int currentAnswerNumber) {
        if (mCurrentProgress >= 0 && mCurrentProgress <= mScreenWidth) {
            mCurrentProgress = currentAnswerNumber * mPercent;
//            mCurrentProgress += mPercent;
            if (mCurrentProgress <= mScreenWidth) {
                getLayoutParams().width = (int) mCurrentProgress;
                requestLayout();
            }
        }
    }

    /***
     * 设置当前的百分比进度
     * @ percent
     */
    public void setPercent(float percent) {
        if (mCurrentProgress <= mScreenWidth && percent >= 0 && percent <= 1) {
            mCurrentProgress = mScreenWidth * percent;
            if (mCurrentProgress >= 0 && mCurrentProgress <= mScreenWidth) {
                getLayoutParams().width = (int) mCurrentProgress;
                requestLayout();
            }
        }
    }

}
