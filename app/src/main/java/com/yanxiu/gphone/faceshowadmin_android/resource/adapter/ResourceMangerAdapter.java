package com.yanxiu.gphone.faceshowadmin_android.resource.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;
import com.yanxiu.gphone.faceshowadmin_android.resource.bean.ResourceBean;
import com.yanxiu.gphone.faceshowadmin_android.utils.StringUtils;
import com.yanxiu.gphone.faceshowadmin_android.utils.YXPictureManager;

import java.util.ArrayList;


/**
 * Created by 戴延枫
 * 资源管理adapter
 */

public class ResourceMangerAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;

    private ArrayList<ResourceBean> mList;


    public ResourceMangerAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<ResourceBean> list) {
        mList = list;
    }

    public void addData(ArrayList<ResourceBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View header = inflater.inflate(R.layout.item_manager_rersource_adapter_layout, parent, false);

        RecyclerView.ViewHolder viewHolder = new ResourceItemViewHolder(header);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ResourceBean data = mList.get(position);
        ResourceItemViewHolder holder2 = (ResourceItemViewHolder) holder;
        holder2.resource_item_title.setText(data.getResName());
        holder2.resource_item_num.setText(data.getDownNum() + "/" + data.getViewNum());
        holder2.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewItemClickListener.onItemClick(view,position);
            }
        });
        switch (data.getType()) {
            case "0":
//                holder2.resource_item_img.setImageResource(R.drawable.course_discuss_laud_clicked);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 资源item
     */
    class ResourceItemViewHolder extends RecyclerView.ViewHolder {
        private TextView resource_item_title;
        private TextView resource_item_num;
        private ImageView resource_item_img;

        public ResourceItemViewHolder(View itemView) {
            super(itemView);
            resource_item_title = itemView.findViewById(R.id.resource_item_title);
            resource_item_num = itemView.findViewById(R.id.resource_item_num);
            resource_item_img = itemView.findViewById(R.id.resource_item_img);
        }
    }
}


