package com.yanxiu.gphone.faceshowadmin_android.notice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.net.notice.NoticeRequestResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.StringUtils;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by lufengqing on 2017/11/1.
 */

class NoticeManagerListAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;

    private ArrayList<NoticeRequestResponse.DataBean.NoticeInfosBean.ElementsBean> mList = new ArrayList<>();
    private int mNoticeNum;

    public NoticeManagerListAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<NoticeRequestResponse.DataBean.NoticeInfosBean.ElementsBean> list, int totalNoticeNum) {
        mList = list;
        mNoticeNum = totalNoticeNum;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.notice_manage_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NoticeRequestResponse.DataBean.NoticeInfosBean.ElementsBean data = mList.get(position);

        ViewHolder holder2 = (ViewHolder) holder;
        holder2.notice_name.setText(data.getTitle());
        holder2.num_reader.setText(data.getNoticeReadUserNum() + "/" + mNoticeNum);
        holder2.notice_content.setText(data.getContent());
        holder2.notice_time.setText(StringUtils.getCourseTime(data.getCreateTime()));
        holder2.notice_manage_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 课程内容
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private View notice_manage_layout;
        private TextView notice_name;
        private TextView notice_time;
        private TextView notice_content;
        private TextView num_reader;

        public ViewHolder(View itemView) {
            super(itemView);
            notice_manage_layout = itemView.findViewById(R.id.notice_manage_layout);
            notice_name = (TextView) itemView.findViewById(R.id.notice_name);
            notice_time = (TextView) itemView.findViewById(R.id.notice_time);
            notice_content = (TextView) itemView.findViewById(R.id.notice_content);
            num_reader = (TextView) itemView.findViewById(R.id.num_reader);
        }


    }

}

