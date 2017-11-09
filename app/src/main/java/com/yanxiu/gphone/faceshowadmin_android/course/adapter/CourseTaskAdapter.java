package com.yanxiu.gphone.faceshowadmin_android.course.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author frc on 17-11-8.
 */

public class CourseTaskAdapter extends BaseRecyclerViewAdapter {
    private List<GetCourseResponse.InteractStepsBean> data = new ArrayList<>();

    public CourseTaskAdapter(List<GetCourseResponse.InteractStepsBean> interactSteps) {
        this.data = interactSteps;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_task_adapter_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_task_icon)
        ImageView mImgTaskIcon;
        @BindView(R.id.tv_task_name)
        TextView mTvTaskName;
        @BindView(R.id.tv_task_statue)
        TextView mTvTaskStatue;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(GetCourseResponse.InteractStepsBean interactStep) {
            mTvTaskName.setText(interactStep.getInteractName());
            int discussType = 4;
            if (interactStep.getInteractType() == discussType) {
                int openType = 1;
                //     int closeType = 0;
                if (openType == interactStep.getStepStatus()) {
                    mTvTaskStatue.setText("已开启");
                } else {
                    mTvTaskStatue.setText("未开启");
                }

            } else {
                mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_finish_number, interactStep.getFinishedStudentNum(), interactStep.getTotalStudentNum())));
            }
        }
    }
}
