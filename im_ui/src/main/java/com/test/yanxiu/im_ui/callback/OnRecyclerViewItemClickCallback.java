package com.test.yanxiu.im_ui.callback;

public interface OnRecyclerViewItemClickCallback<T extends Object> {
    void onItemClick(int position,T t);
}