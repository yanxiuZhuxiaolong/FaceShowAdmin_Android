package com.yanxiu.gphone.faceshowadmin_android.login.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.GetClazzListResponse;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frc on 17-10-30.
 */

public class ClassManagerListAdapter extends BaseRecyclerViewAdapter {

    private ArrayList<View> views = new ArrayList<>();

    private List<GetClazzListResponse.DataBean.ClazsInfosBean> data = new ArrayList<>();

    public ClassManagerListAdapter(List<GetClazzListResponse.DataBean.ClazsInfosBean> clazsInfos) {
        this.data = clazsInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_class_adapter_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.classmanger_item_title.setText(data.get(position).getClazsName());
        String start=data.get(position).getStartTime().split(" ")[0];
        String end=data.get(position).getEndTime().split(" ")[0];
        viewHolder.classmanger_item_time.setText(start + " 至 " + end);
        viewHolder.classmanger_item_content.setText(data.get(position).getDescription());
        if(!views.contains(holder.itemView)){
            views.add(holder.itemView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < views.size(); i++) {
                    if(views.get(i) == view){
                        views.get(i).setSelected(true);
                    } else {
                        views.get(i).setSelected(false);
                    }
                }
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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView classmanger_item_title, classmanger_item_time, classmanger_item_content;

        ViewHolder(View itemView) {
            super(itemView);
            classmanger_item_title = itemView.findViewById(R.id.classmanger_item_title);
            classmanger_item_time = itemView.findViewById(R.id.classmanger_item_time);
            classmanger_item_content = itemView.findViewById(R.id.classmanger_item_content);
        }

    }

}
