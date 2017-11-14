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
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseResourcesResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author frc on 17-11-9.
 */

public class CourseResourceAdapter extends BaseRecyclerViewAdapter {
    private List<GetCourseResourcesResponse.ElementsBean> data = new ArrayList<>();

    public CourseResourceAdapter(List<GetCourseResourcesResponse.ElementsBean> elements) {
        this.data = elements;
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

        void setData(GetCourseResourcesResponse.ElementsBean element) {
            mTvTaskName.setText(element.getResName());
            mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_read_person_number, element.getViewClazsStudentNum(), element.getTotalClazsStudentNum())));
            switch (element.getSuffix()) {
                case "word":
                case "doc":
                case "docx":
                    mImgTaskIcon.setImageResource(R.drawable.word);
                    break;
                case "xls":
                case "xlsx":
                case "excel":
                    mImgTaskIcon.setImageResource(R.drawable.excel);
                    break;
                case "ppt":
                case "pptx":
                    mImgTaskIcon.setImageResource(R.drawable.ppt);
                    break;
                case "pdf":
                    mImgTaskIcon.setImageResource(R.drawable.pdf);
                    break;
                case "text":
                case "txt":
                    mImgTaskIcon.setImageResource(R.drawable.txt);
                    break;
                case "video":
                case "mp4":
                case "m3u8":
                    mImgTaskIcon.setImageResource(R.drawable.mp4);
                    break;
                case "audio":
                case "mp3":
                    mImgTaskIcon.setImageResource(R.drawable.mp3);
                    break;
                case "image":
                case "jpg":
                case "jpeg":
                case "gif":
                case "png":
                    mImgTaskIcon.setImageResource(R.drawable.jpg);
                    break;
                default:
                    mImgTaskIcon.setImageResource(R.drawable.weizhi);
                    break;
            }
            if ("1".equals(element.getType())) {
                mImgTaskIcon.setImageResource(R.drawable.html);
            }
        }
    }
}
