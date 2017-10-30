package com.yanxiu.gphone.faceshowadmin_android.interf;

import android.view.View;

/**
 * MainFragment里面的嵌套的三个RecylerVIew的点击回调
 */

public interface MainFragmentRecyclerViewItemClickListener {

    /**
     * 首页5个tab的点击
     *
     * @param v
     * @param position
     */
    void onTabItemClick(View v, int position);

    /**
     * 签到item点击
     *
     * @param v
     * @param position
     */
    void onCheckInItemClick(View v, int position);

    /**
     * 课程item点击
     *
     * @param v
     * @param position
     */
    void onCourseTabItemClick(View v, int position);

}
