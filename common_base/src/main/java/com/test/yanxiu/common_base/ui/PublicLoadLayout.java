package com.test.yanxiu.common_base.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yanxiu.common_base.R;


/**
 * Created by 戴延枫 on 2017/5/17.
 */

public class PublicLoadLayout extends FrameLayout {

    private Context mContext;
    private FrameLayout mContentViewContainer;//加载具体页面的contentView
    private RelativeLayout mNetErrorLayoutContainer;//错误页面容器，默认网络错误页面
    private RelativeLayout mOtherErrorLayoutContainer;//其他错误页面容器，默认没有数据页面
    //    private View mNetErrorLayout;//错误页面容器，默认网络错误页面
    private TextView mRetry_button;//重试按钮
    private View mLoadingLayout;//loadingLayout
    private ImageView mLoadingView;// loadingView
    private Animation mLoadingAnim;//loadingView动画
    private ImageView mDataEmptyTopView;
    private TextView mDataEmptyContentView;

    public PublicLoadLayout(Context context) {
        this(context, null);
    }

    public PublicLoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PublicLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化view
     */
    private void initView(Context context) {
        mContext = context;
        inflate(context, R.layout.public_load_layout, this);
        mContentViewContainer = (FrameLayout) findViewById(R.id.contentViewContainer);
        mNetErrorLayoutContainer = (RelativeLayout) findViewById(R.id.netErrorLayoutContainer);
        mOtherErrorLayoutContainer = (RelativeLayout) findViewById(R.id.otherErrorLayoutContainer);
        initDefaultLayout();
        initLoadingView();
        initDataEmptyView();
    }

    private void initDataEmptyView() {
        inflate(mContext, R.layout.data_empty_layout, mOtherErrorLayoutContainer);
        mDataEmptyTopView = (ImageView) findViewById(R.id.empty_top);
        mDataEmptyContentView = (TextView) findViewById(R.id.empty_content);
    }

    /**
     * 加载默认布局（网络错误页面）
     */
    private void initDefaultLayout() {
//        mNetErrorLayout = inflate(mContext, R.layout.net_error_layout, mNetErrorLayoutContainer);
        inflate(mContext, R.layout.net_error_layout, mNetErrorLayoutContainer);
        mRetry_button = (TextView) findViewById(R.id.retry_button);
    }

    /**
     * 初始化加载动画布局
     */
    private void initLoadingView() {
        mLoadingLayout = findViewById(R.id.loadingLayout);
        mLoadingView = (ImageView) findViewById(R.id.loadingView);
        mLoadingAnim = AnimationUtils.loadAnimation(mContext, R.anim.lodingview_progress);
        LinearInterpolator lin = new LinearInterpolator();
        mLoadingAnim.setInterpolator(lin);
        mLoadingLayout.setVisibility(GONE);
    }


    /**
     * 给默认的网络错误界面的重试button添加点击事件。
     * 如果是网络错误界面，必须传入该监听，否则，点击无效。
     *
     * @param onclickListener
     */
    public void setRetryButtonOnclickListener(OnClickListener onclickListener) {
        mRetry_button.setOnClickListener(onclickListener);
    }

    /**
     * 获取重试按钮
     *
     * @return
     */
    public View getReturyButton() {
        return mRetry_button;
    }


    /**
     * 设置rootview
     *
     * @param rootViewId 页面的根viewid
     */
    public void setContentView(int rootViewId) {
        inflate(mContext, rootViewId, mContentViewContainer);//加载传进来的具体页面的contentView

    }

    /**
     * 设置rootview
     *
     * @param rootView 页面的根viewid
     */
    public void setContentView(View rootView) {
        mContentViewContainer.addView(rootView);//加载传进来的具体页面的contentView
    }

    /**
     * 设置要展示的view（如数据为空的view）
     */
    public void setOtherView(int viewId) {
        int childCound = mOtherErrorLayoutContainer.getChildCount();
        if (childCound > 0)
            mOtherErrorLayoutContainer.removeAllViews();//先移除容器内的默认布局
        inflate(mContext, viewId, mOtherErrorLayoutContainer);//加载其他页面（如数据为空页面）
    }

    /**
     * 设置要展示的view（如数据为空的view）
     */
    public void setOtherView(View view) {
        int childCound = mOtherErrorLayoutContainer.getChildCount();
        if (childCound > 0)
            mOtherErrorLayoutContainer.removeAllViews();//先移除容器内的默认布局
        mOtherErrorLayoutContainer.addView(view);//加载其他页面（如数据为空页面）
    }

    /**
     * 显示loadingView
     */
    public void showLoadingView() {
        if (mLoadingView != null && mLoadingAnim != null) {
            mLoadingLayout.setVisibility(View.VISIBLE);
            mLoadingView.clearAnimation();
            mLoadingView.startAnimation(mLoadingAnim);
        }
    }

    /**
     * 隐藏LoadingView
     */
    public void hiddenLoadingView() {
        if (mLoadingView != null && mLoadingAnim != null) {
            mLoadingView.clearAnimation();
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 显示net界面
     */
    public void showNetErrorView() {
        hiddenLoadingView();
        if (mOtherErrorLayoutContainer != null) {
            mOtherErrorLayoutContainer.setVisibility(GONE);
        }
        if (mNetErrorLayoutContainer != null) {
            mNetErrorLayoutContainer.setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏net界面
     */
    public void hiddenNetErrorView() {
        hiddenLoadingView();
        if (mNetErrorLayoutContainer != null) {
            mNetErrorLayoutContainer.setVisibility(GONE);
        }
    }

    /**
     * 显示Other界面
     */
    public void showOtherErrorView(String text) {
        hiddenLoadingView();
        if (mNetErrorLayoutContainer != null) {
            mNetErrorLayoutContainer.setVisibility(GONE);
        }
        if (mOtherErrorLayoutContainer != null) {
            mOtherErrorLayoutContainer.setVisibility(VISIBLE);
        }
        mDataEmptyContentView.setText(text);
    }

    /**
     * 显示Other界面，并传入提示文字
     */
    public void showOtherErrorView() {
        hiddenLoadingView();
        if (mNetErrorLayoutContainer != null) {
            mNetErrorLayoutContainer.setVisibility(GONE);
        }
        if (mOtherErrorLayoutContainer != null) {
            mOtherErrorLayoutContainer.setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏Other界面
     */
    public void hiddenOtherErrorView() {
        hiddenLoadingView();
        if (mOtherErrorLayoutContainer != null) {
            mOtherErrorLayoutContainer.setVisibility(GONE);
        }
    }

    /**
     * 設置空界面顯示
     */
    public void setDataEmptyErrorView(@DrawableRes int resId, String content) {
        mDataEmptyTopView.setBackgroundResource(resId);
        mDataEmptyContentView.setText(content);
    }

    /**
     * Errorlayout默认topMargin = 55dp，使rootview中的titlebar能够露出来
     * 调用该方法topMargin = 0dp，errorlayout全屏
     */
    public void setErrorLayoutFullScreen() {
        LayoutParams lp = (LayoutParams) mNetErrorLayoutContainer.getLayoutParams();
        lp.topMargin = 0;
        mNetErrorLayoutContainer.setLayoutParams(lp);

        LayoutParams otherLp = (LayoutParams) mOtherErrorLayoutContainer.getLayoutParams();
        otherLp.topMargin = 0;
        mOtherErrorLayoutContainer.setLayoutParams(lp);
//      mErrorLayoutContainer.setPadding(0,0,0,0);
    }

    /**
     * 隐藏异常界面和lodingView，展示正常界面
     */
    public void finish() {
        hiddenLoadingView();
        mNetErrorLayoutContainer.setVisibility(GONE);
        mOtherErrorLayoutContainer.setVisibility(GONE);
    }

}
