package com.yanxiu.gphone.faceshowadmin_android.course.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetClassCoursesResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by frc on 17-11-6.
 */

public class CourseStepsAdapter extends RecyclerView.Adapter {
    private List<GetClassCoursesResponse.StepsBean> data = new ArrayList<>();

    CourseStepsAdapter(List<GetClassCoursesResponse.StepsBean> steps) {
        this.data = steps;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_steps_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).mStepName.setText(Html.fromHtml(holder.itemView.getContext().getString(R.string.sign_in_number,data.get(position).getInteractName(), data.get(position).getFinishedStudentNum(), data.get(position).getTotalStudentNum())));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.step_name)
        TextView mStepName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
