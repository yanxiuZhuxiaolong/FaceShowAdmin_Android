package com.yanxiu.gphone.faceshowadmin_android.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.interf.MainFragmentRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.CourseBean;
import com.yanxiu.gphone.faceshowadmin_android.utils.StringUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.recyclerView.BaseRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by 戴延枫
 * 首页fragment里的今日课程的adapter
 */

public class MainFragmentCourseAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;

    private ArrayList<CourseBean> mList;
    private MainFragmentRecyclerViewItemClickListener mListener;

    public MainFragmentCourseAdapter(Context context, MainFragmentRecyclerViewItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(ArrayList<CourseBean> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.mainfragment_course_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CourseBean data = mList.get(position);

        ViewHolder holder2 = (ViewHolder) holder;
        holder2.course_name.setText(data.getCourseName());
        holder2.course_location.setText(TextUtils.isEmpty(data.getSite()) ? "待定" : data.getSite());
        holder2.course_teacher.setText(data.getLecturer());
        holder2.course_time.setText(StringUtils.getCourseTime(data.getStartTime()));
        holder2.course_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCourseTabItemClick(v, position);
                }
            }
        });
        if (position < (mList.size() - 1)) {
            holder2.checkin_item_line.setVisibility(View.VISIBLE);
        } else {
            holder2.checkin_item_line.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 课程内容
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private View course_layout;
        private View checkin_item_line;
        private TextView course_name;
        private TextView course_time;
        private TextView course_teacher;
        private TextView course_location;

        public ViewHolder(View itemView) {
            super(itemView);
            course_layout = itemView.findViewById(R.id.course_layout);
            checkin_item_line = itemView.findViewById(R.id.checkin_item_line);
            course_name = itemView.findViewById(R.id.course_name);
            course_time = itemView.findViewById(R.id.course_time);
            course_teacher = itemView.findViewById(R.id.course_teacher);
            course_location = itemView.findViewById(R.id.course_location);
//            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_PLAY, mPrefixNumber, mPostfixNumber);
        }


    }

}


