package com.yanxiu.gphone.faceshowadmin_android.utils.recyclerView;

import android.support.v7.widget.RecyclerView;

import com.yanxiu.gphone.faceshowadmin_android.interf.RecyclerViewItemClickListener;

/**
 *
 * Created by frc on 17-10-27.
 */

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter {
    protected RecyclerViewItemClickListener recyclerViewItemClickListener;

    public void addItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

}
