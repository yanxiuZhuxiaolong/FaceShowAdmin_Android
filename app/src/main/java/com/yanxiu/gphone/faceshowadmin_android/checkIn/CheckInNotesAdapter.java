package com.yanxiu.gphone.faceshowadmin_android.checkIn;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetCheckInNotesResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.recyclerView.BaseRecyclerViewAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * adapter for checkInNotesActivity recyclerView
 * Created by frc on 17-10-31.
 */

public class CheckInNotesAdapter extends BaseRecyclerViewAdapter {
    private List<GetCheckInNotesResponse.DataBean.SignInsBean> data = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_in_notes_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(data.get(position));


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void update(List<GetCheckInNotesResponse.DataBean.SignInsBean> signIns) {
        this.data = signIns;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.check_in_item_title)
        TextView checkInItemTitle;
        @BindView(R.id.check_in_item_time)
        TextView checkInItemTime;
        @BindView(R.id.has_check_in_Proportion)
        TextView hasCheckInProportion;
        @BindView(R.id.check_in_percentage)
        TextView checkInPercentage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(final GetCheckInNotesResponse.DataBean.SignInsBean data) {
            checkInItemTitle.setText(data.getTitle());
            final String proportion = itemView.getContext().getString(R.string.has_check_in_proportion, data.getSignInUserNum(), data.getTotalUserNum());
            hasCheckInProportion.setText(proportion);
            checkInPercentage.setText(getPercent(data.getSignInUserNum(), data.getTotalUserNum()));
            final String time;
            if (data.getStartTime() == null || data.getEndTime() == null) {
                time = "";
            } else {
                String[] startTimes = data.getStartTime().split(" ");
                String[] startTime = startTimes[1].split(":");
                String[] endTime = data.getEndTime().split(" ")[1].split(":");
                time = startTimes[0] + " " + startTime[0] + ":" + startTime[1] + "-" + endTime[0] + ":" + endTime[1];
            }
            checkInItemTime.setText(time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), CheckInDetailActivity.class);
                    intent.putExtra("title", data.getTitle());
                    intent.putExtra("time", time);
                    intent.putExtra("percentage", getPercent(data.getSignInUserNum(), data.getTotalUserNum()));
                    intent.putExtra("proportion", proportion);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        String getPercent(int y, int z) {
            if (y == 0) {
                return "0%";
            } else {
                String baifenbi = "";// 接受百分比的值
                double baiy = y * 1.0;
                double baiz = z * 1.0;
                double fen = baiy / baiz;
                // NumberFormat nf = NumberFormat.getPercentInstance(); 注释掉的也是一种方法
                // nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位
                DecimalFormat df1 = new DecimalFormat("##.00%"); // ##.00%
                // 百分比格式，后面不足2位的用0补齐
                // baifenbi=nf.format(fen);
                baifenbi = df1.format(fen);
                System.out.println(baifenbi);
                return baifenbi;
            }
        }

    }
}
