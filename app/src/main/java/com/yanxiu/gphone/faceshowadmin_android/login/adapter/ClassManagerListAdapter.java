package com.yanxiu.gphone.faceshowadmin_android.login.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.GetClazzListResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.recyclerView.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frc on 17-10-30.
 */

public class ClassManagerListAdapter extends BaseRecyclerView {

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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView projectName, className;

        ViewHolder(View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.project_name);
            className = itemView.findViewById(R.id.class_name);
        }

        void setData(GetClazzListResponse.DataBean.ClazsInfosBean clazsInfosBean) {
            projectName.setText(clazsInfosBean.getDescription());
            className.setText(clazsInfosBean.getClazsName());
        }


    }

}
