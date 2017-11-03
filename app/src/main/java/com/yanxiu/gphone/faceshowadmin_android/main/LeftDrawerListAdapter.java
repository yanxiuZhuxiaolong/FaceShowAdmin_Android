package com.yanxiu.gphone.faceshowadmin_android.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;

/**
 * adapter for LeftDrawerRecyclerView in MainActivity
 * Created by frc on 17-10-26.
 */

public class LeftDrawerListAdapter extends BaseRecyclerViewAdapter {
    private final int TYPE_HEAD = 0X01;
    private final int TYPE_NORMAL = 0X02;

    private int[] itemIconArray = new int[]{R.drawable.ic_home_black,
            R.drawable.ic_person_black, R.drawable.ic_person_black,
            R.drawable.ic_settings_black, R.drawable.ic_exit_to_app_black};
    private String[] itemNameArray = null;

    public LeftDrawerListAdapter(Context context) {
        itemNameArray = context.getResources().getStringArray(R.array.left_drawer_item_names);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new HeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_drawer_list_head_layout, parent, false));
        } else if (viewType == TYPE_NORMAL) {
            return new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_drawer_list_normal_layout, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEAD) {

        } else {
            ((NormalViewHolder) holder).itemName.setText(itemNameArray[position - 1]);
            ((NormalViewHolder) holder).itemIcon.setImageResource(itemIconArray[position - 1]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewItemClickListener != null) {
                        recyclerViewItemClickListener.onItemClick(v, holder.getAdapterPosition() - 1);
                    }
                }
            });


        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return itemIconArray.length + 1;
    }

    private class HeadViewHolder extends RecyclerView.ViewHolder {

        HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemIcon;
        private TextView itemName;

        NormalViewHolder(View itemView) {
            super(itemView);
            itemIcon = itemView.findViewById(R.id.list_item_icon);
            itemName = itemView.findViewById(R.id.list_item_name);
        }
    }
}
