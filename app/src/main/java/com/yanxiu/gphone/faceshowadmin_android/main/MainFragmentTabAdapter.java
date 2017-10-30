package com.yanxiu.gphone.faceshowadmin_android.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.utils.recyclerView.BaseRecyclerViewAdapter;

/**
 * Created by 戴延枫
 * 首页fragment里的几个tab的adapter
 */

public class MainFragmentTabAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;

    private final int COURSE_DATE = 1;//课程日期
    private final int COURSE_COTENT = 2;//内容

//    private ArrayList<CourseBean> mList;


    public MainFragmentTabAdapter(Context context) {
        mContext = context;
    }

//    public void setData(ArrayList<CourseBean> list) {
//        mList = list;
//    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder viewHolder = null;
//        View view1 = inflater.inflate(R.layout.course_arrange_item1, parent, false);
//        viewHolder = new CourseDateViewHolder(view1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        final CourseBean data = mList.get(position);
//
//        CourseContentViewHolder holder2 = (CourseContentViewHolder) holder;
//        holder2.course_name.setText(data.getCourseName());
//        holder2.course_location.setText(TextUtils.isEmpty(data.getSite()) ? "待定" : data.getSite());
//        holder2.course_teacher.setText(data.getLecturer());
//        holder2.course_time.setText(StringUtils.getCourseTime(data.getStartTime()));
//        holder2.course_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (recyclerViewItemClickListener != null) {
//                    recyclerViewItemClickListener.onItemClick(position, data);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
//        return mList.size();
        return 0;
    }
//
//    /**
//     * 课程日期
//     */
//    class CourseDateViewHolder extends RecyclerView.ViewHolder {
//        private TextView course_date;
//
//        public CourseDateViewHolder(View itemView) {
//            super(itemView);
//            course_date = (TextView) itemView.findViewById(R.id.course_date);
//        }
//    }

//    /**
//     * 课程内容
//     */
//    class CourseContentViewHolder extends RecyclerView.ViewHolder {
//        private View course_layout;
//        private TextView course_name;
//        private TextView course_time;
//        private TextView course_teacher;
//        private TextView course_location;
//
//        public CourseContentViewHolder(View itemView) {
//            super(itemView);
//            course_layout = itemView.findViewById(R.id.course_layout);
//            course_name = (TextView) itemView.findViewById(R.id.course_name);
//            course_time = (TextView) itemView.findViewById(R.id.course_time);
//            course_teacher = (TextView) itemView.findViewById(R.id.course_teacher);
//            course_location = (TextView) itemView.findViewById(R.id.course_location);
////            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_PLAY, mPrefixNumber, mPostfixNumber);
//        }
//    }
}


