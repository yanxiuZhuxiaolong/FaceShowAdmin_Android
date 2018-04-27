package com.test.yanxiu.im_ui.view;

import android.content.Context;
import android.support.v4.view.ScrollingView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.im_ui.callback.OnPullToRefreshCallback;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Created by cailei on 15/03/2018.
 */

public class RecyclerViewPullToRefreshHelper {
    private OnPullToRefreshCallback mCallback;
    private boolean isLoading = false;

    private float startY;
    boolean isPullDown;

    public RecyclerViewPullToRefreshHelper(Context context, final RecyclerView v, final View.OnTouchListener onTouchListener) {
        RecyclerView.OnScrollListener listener;

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onTouchListener.onTouch(view, motionEvent);

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float currentY = motionEvent.getY();
                        isPullDown = currentY - startY > 10 ? true : false;

                        if ((!isLoading) && (isPullDown) && (!v.canScrollVertically(-1)))  {
                            LinearLayoutManager manager = (LinearLayoutManager) v.getLayoutManager();
                            if (manager.findFirstVisibleItemPosition() == 0) {
                                if (mCallback != null) {
                                    isLoading = true;
                                    mCallback.onLoadMore();
                                }
                            }
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        isPullDown = false;
                        break;
                }
                return false;
            }
        });
    }

    public void setmCallback(OnPullToRefreshCallback mCallback) {
        this.mCallback = mCallback;
    }

    public void loadingComplete() {
        isLoading = false;
    }
}
