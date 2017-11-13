package com.yanxiu.gphone.faceshowadmin_android.task.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseCommentRecordsResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetReplyResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.DateFormatUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by frc on 17-11-10.
 */

public class ReplyDetailAdapter extends RecyclerView.Adapter {

    private List<GetReplyResponse.DataBean> records = new ArrayList<>();
    private String description;
    private String totalNum;
    private final int TYPE_HEAD = 0;
    private final int TYPE_NORMAL = 1;
    private final int TYPE_FOOTER = 2;

    public ReplyDetailAdapter(List<GetReplyResponse.DataBean> records, String description, String s) {
        this.records = records;
        this.description = description;
        this.totalNum = s;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                return new HeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply_head_layout, parent, false));
            case TYPE_FOOTER:
                return new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply_footer_layout, parent, false));
            default:
                return new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply_normal_layout, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                ((HeadViewHolder) holder).setData(description, totalNum);
                break;
            case TYPE_FOOTER:
                ((NormalViewHolder) holder).setData(records.get(position - 1));
                break;
            default:
                ((NormalViewHolder) holder).setData(records.get(position - 1));
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;

        }
    }

    @Override
    public int getItemCount() {
        return records.size() + 1;
    }

    public void update(List<GetReplyResponse.DataBean> records, String description, String totalNum) {
        this.records = records;
        this.description = description;
        this.totalNum = totalNum;
        notifyDataSetChanged();
    }

    static class HeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_discuss_name)
        TextView mTvDiscussName;
        @BindView(R.id.tv_reply_number)
        TextView mTvReplyNumber;

        HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(String description, String number) {
            mTvDiscussName.setText(description);
            mTvReplyNumber.setText(itemView.getContext().getString(R.string.reply_number, number));
        }

    }

    static class NormalViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        @BindView(R.id.reply_person_name)
        TextView mReplyPersonName;
        @BindView(R.id.tv_reply_content)
        TextView mTvReplyContent;
        @BindView(R.id.tv_reply_time)
        TextView mTvReplyTime;

        NormalViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        void setData(GetReplyResponse.DataBean element) {
            mReplyPersonName.setText(element.getUserName());
            mTvReplyContent.setText(element.getAnswer());
            mTvReplyTime.setText(DateFormatUtil.getReplyTime(element.getCreateTime()));
        }
    }
}