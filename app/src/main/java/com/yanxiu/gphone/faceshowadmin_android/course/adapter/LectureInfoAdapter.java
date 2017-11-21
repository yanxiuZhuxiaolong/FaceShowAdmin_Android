package com.yanxiu.gphone.faceshowadmin_android.course.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.CornersImageTarget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author frc on 17-11-9.
 */

public class LectureInfoAdapter extends BaseRecyclerViewAdapter {
    private List<GetCourseResponse.CourseBean.LecturerInfosBean> data = new ArrayList<>();

    public LectureInfoAdapter(List<GetCourseResponse.CourseBean.LecturerInfosBean> lecturerInfos) {
        this.data = lecturerInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture_info_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(data.get(position));
        if (position == getItemCount() - 1) {
            ((ViewHolder) holder).line.setVisibility(View.GONE);
        } else {
            ((ViewHolder) holder).line.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_head)
        ImageView mImgHead;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.rv_lecture_brief)
        TextView mRvLectureBrief;
        @BindView(R.id.line)
        View line;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(GetCourseResponse.CourseBean.LecturerInfosBean lecturerInfosBean) {
            mTvName.setText(lecturerInfosBean.getLecturerName());
            mRvLectureBrief.setText(lecturerInfosBean.getLecturerBriefing());
            Glide.with(itemView.getContext()).load(lecturerInfosBean.getLecturerAvatar()).asBitmap().placeholder(R.drawable.classcircle_headimg).centerCrop().into(new CornersImageTarget(itemView.getContext(), mImgHead, 10));
        }
    }
}
