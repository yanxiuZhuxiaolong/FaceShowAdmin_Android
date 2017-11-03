package com.yanxiu.gphone.faceshowadmin_android.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.interf.MainFragmentRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.TodaySignInBean;
import com.yanxiu.gphone.faceshowadmin_android.utils.StringUtils;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by 戴延枫
 * 首页fragment里今日签到的adapter
 */

public class MainFragmentCheckInAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;

    private ArrayList<TodaySignInBean> mList;
    private MainFragmentRecyclerViewItemClickListener mListener;

    public MainFragmentCheckInAdapter(Context context, MainFragmentRecyclerViewItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(ArrayList<TodaySignInBean> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.mainfragment_checkin_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TodaySignInBean data = mList.get(position);
        ViewHolder holder2 = (ViewHolder) holder;
        holder2.checkin_item_title.setText(data.getTitle());
        holder2.checkin_item_time.setText(StringUtils.getCourseTime(data.getStartTime(),data.getEndTime()));
        holder2.checkin_item_checkin.setText(data.getSignInUserNum() + "/" + data.getTotalUserNum());
        String percent;
        try {
            percent = NumberFormat.getPercentInstance().format(Float.valueOf(data.getPercent()));
        } catch (Exception e) {
            percent = "0%";
        }
        holder2.checkin_item_peecent.setText(percent);
        holder2.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCheckInItemClick(v, position);
                }
            }
        });

        if (position < (mList.size() - 1)) {
            holder2.checkin_item_line.setVisibility(View.VISIBLE);
        } else {
            holder2.checkin_item_line.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 课程内容
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private View layout;
        private View checkin_item_line;
        private TextView checkin_item_title;
        private TextView checkin_item_time;
        private TextView checkin_item_checkin;
        private TextView checkin_item_peecent;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.checkin_item_layout);
            checkin_item_line = itemView.findViewById(R.id.checkin_item_line);
            checkin_item_title = itemView.findViewById(R.id.checkin_item_title);
            checkin_item_time = itemView.findViewById(R.id.checkin_item_time);
            checkin_item_checkin = itemView.findViewById(R.id.checkin_item_checkin);
            checkin_item_peecent = itemView.findViewById(R.id.checkin_item_peecent);

//            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_PLAY, mPrefixNumber, mPostfixNumber);
        }


    }

}


