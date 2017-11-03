
package com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 支持上拉加载更多的recyclerView
 *
 * @dyf 2017年9月14日11:13:44
 */
public class LoadMoreRecyclerView extends RecyclerView {

    /**
     * 上拉加载更多是否可用
     * true 可用
     * false 不可用
     */
    private boolean loadMoreEnable;

    /**
     * 上拉加载更多是否可用
     * true 可用
     * false 不可用
     */
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    /**
     * 加载更多监听器
     */
    public interface LoadMoreListener {
        /**
         * 加载更多中
         */
        void onLoadMore(LoadMoreRecyclerView refreshLayout);

        void onLoadmoreComplte();
    }

    private LoadMoreListener mListener;

    public void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * loadMore结束
     */
    public void finishLoadMore() {
        loadMoreEnable = true;
        if (mListener != null) {
            mListener.onLoadmoreComplte();
        }
    }

    public LoadMoreRecyclerView(Context context) {
        super(context);
        initView();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        setOnScrollListener(listener);
    }

    private OnScrollListener listener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (loadMoreEnable) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (manager.findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1) {
                    if (mListener != null) {
                        loadMoreEnable = false;
                        mListener.onLoadMore(LoadMoreRecyclerView.this);
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };


}