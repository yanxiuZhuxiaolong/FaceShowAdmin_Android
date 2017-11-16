package com.yanxiu.gphone.faceshowadmin_android.course.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.checkIn.activity.CheckInDetailActivity;
import com.yanxiu.gphone.faceshowadmin_android.course.activity.CourseCommentActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResponse;
import com.yanxiu.gphone.faceshowadmin_android.task.activity.QuestionnaireActivity;
import com.yanxiu.gphone.faceshowadmin_android.task.activity.VoteActivity;

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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(data.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });

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
            switch (interactStep.getInteractType()) {
                case 6:
                    mImgTaskIcon.setImageResource(R.drawable.ic_sign_in);
                    mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_finish_number, interactStep.getFinishedStudentNum(), interactStep.getTotalStudentNum())));
                    break;
                case 5:
                    mImgTaskIcon.setImageResource(R.drawable.icon_questionnaire);
                    mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_finish_number, interactStep.getFinishedStudentNum(), interactStep.getTotalStudentNum())));
                    break;
                case 4:
                    int openType = 1;
                    if (openType == interactStep.getStepStatus()) {
                        mTvTaskStatue.setText("已开启");
                    } else {
                        mTvTaskStatue.setText("未开启");
                    }
                    mImgTaskIcon.setImageResource(R.drawable.ic_comment);
                    break;
                case 3:
                    mImgTaskIcon.setImageResource(R.drawable.icon_vote);
                    mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_finish_number, interactStep.getFinishedStudentNum(), interactStep.getTotalStudentNum())));
                    break;
                default:
                    mImgTaskIcon.setImageResource(R.drawable.ic_do_not_know);
            }

        }
    }
}
