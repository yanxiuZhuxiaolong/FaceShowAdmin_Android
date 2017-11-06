package com.yanxiu.gphone.faceshowadmin_android.course;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetClassCoursesResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * adapter for expandListView
 * Created by frc on 17-11-6.
 */

public class CoursesAdapter extends BaseExpandableListAdapter {
    private GetClassCoursesResponse.DataBean data;
    private Context mContext;

    CoursesAdapter(Context context, GetClassCoursesResponse.DataBean dataBean) {
        mContext = context;
        data = dataBean;
    }


    @Override
    public int getGroupCount() {
        return data.getCourses().size();
    }

    @Override
    public int getChildrenCount(int parentPosition) {
        if (parentPosition >= getGroupCount()) {
            return 0;
        } else {
            return data.getCourses().get(parentPosition).getCoursesList().size();
        }
    }

    @Override
    public Object getGroup(int parentPosition) {
        return data.getCourses().get(parentPosition);
    }

    @Override
    public Object getChild(int parentPosition, int childPosition) {
        return data.getCourses().get(parentPosition).getCoursesList().get(childPosition);
    }

    @Override
    public long getGroupId(int parentPosition) {
        return parentPosition;
    }

    @Override
    public long getChildId(int i, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_course_list_head_layout, viewGroup, false);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if (data.getCourses().get(i).isIsToday()) {
            groupViewHolder.mCourseData.setText(R.string.today_course);
        } else {
            groupViewHolder.mCourseData.setText(data.getCourses().get(i).getDate());
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupItem, int childItem, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_course_list_layout, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        GetClassCoursesResponse.CoursesListBean coursesListBean = data.getCourses().get(groupItem).getCoursesList().get(childItem);
        childViewHolder.mCourseName.setText(coursesListBean.getCourseName());
        childViewHolder.mCourseTeacher.setText(coursesListBean.getLecturer() != null ? String.valueOf(coursesListBean.getLecturer()) : "无");
        childViewHolder.mCourseLocation.setText(TextUtils.isEmpty(coursesListBean.getSite()) ? convertView.getContext().getString(R.string.wait_for) : coursesListBean.getSite());
        childViewHolder.mCourseTime.setText(StringUtils.getCourseTime(coursesListBean.getStartTime(), coursesListBean.getEndTime()));
        List<GetClassCoursesResponse.StepsBean> steps = new ArrayList<>();
        for (int i = 0; i < coursesListBean.getSteps().size(); i++) {
            int signInType = 6;
            if (signInType == coursesListBean.getSteps().get(i).getInteractType()) {
                steps.add(coursesListBean.getSteps().get(i));
            }
        }
        CourseStepsAdapter courseStepsAdapter = new CourseStepsAdapter(steps);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(convertView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        childViewHolder.mRecyclerVIew.setLayoutManager(linearLayoutManager);
        childViewHolder.mRecyclerVIew.setAdapter(courseStepsAdapter);
        // TODO: 17-11-6   need test when data enough
        if (isLastChild) {
            childViewHolder.mItemView.setVisibility(View.GONE);
        } else {
            childViewHolder.mItemView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class ChildViewHolder {
        @BindView(R.id.course_name)
        TextView mCourseName;
        @BindView(R.id.course_time)
        TextView mCourseTime;
        @BindView(R.id.course_teacher)
        TextView mCourseTeacher;
        @BindView(R.id.course_location)
        TextView mCourseLocation;
        @BindView(R.id.recyclerView)
        RecyclerView mRecyclerVIew;
        @BindView(R.id.check_in_item_line)
        View mItemView;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class GroupViewHolder {
        @BindView(R.id.course_data)
        TextView mCourseData;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
