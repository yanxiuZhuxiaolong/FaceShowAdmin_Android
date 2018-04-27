package com.test.yanxiu.im_ui.contacts.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * 用来控制recycleView最多显示几行itemView
 * Created by frc on 2018/3/20.
 */

public class MaxHighLinearLayoutManager extends LinearLayoutManager {

    public MaxHighLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        if (getChildCount() > 4) {
            View firstChildView = recycler.getViewForPosition(0);
            measureChild(firstChildView, widthSpec, heightSpec);
            setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight() * 4);
        } else {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }
    }
}
