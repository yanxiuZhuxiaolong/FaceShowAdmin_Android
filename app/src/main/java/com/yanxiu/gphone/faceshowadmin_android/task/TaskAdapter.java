package com.yanxiu.gphone.faceshowadmin_android.task;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetTasksResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by frc on 17-11-6.
 */

public class TaskAdapter extends BaseRecyclerViewAdapter {
    List<GetTasksResponse.TasksBean> data = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_layout, parent, false);
        return new ViewHolder(view);
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

    public void update(List<GetTasksResponse.TasksBean> tasks) {
        data = tasks;
        notifyDataSetChanged();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_task_icon)
        ImageView mImgTaskIcon;
        @BindView(R.id.tv_task_name)
        TextView mTvTaskName;
        @BindView(R.id.tv_have_read_person_number)
        TextView mTvHaveReadPersonNumber;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setData(GetTasksResponse.TasksBean task) {
            mTvTaskName.setText(task.getInteractName());
            mTvHaveReadPersonNumber.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_read_person_number, task.getFinishedStudentNum(), task.getTotalStudentNum())));

        }
    }
}
