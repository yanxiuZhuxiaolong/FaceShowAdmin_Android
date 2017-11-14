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
        holder2.resource_item_num.setText(data.getViewClazsStudentNum() + "/" + data.getTotalClazsStudentNum());
        holder2.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewItemClickListener.onItemClick(view, position);
            }
        });
        switch (data.getSuffix()) {
            case "word":
            case "doc":
            case "docx":
                holder2.resource_item_img.setImageResource(R.drawable.word);
                break;
            case "xls":
            case "xlsx":
            case "excel":
                holder2.resource_item_img.setImageResource(R.drawable.excel);
                break;
            case "ppt":
            case "pptx":
                holder2.resource_item_img.setImageResource(R.drawable.ppt);
                break;
            case "pdf":
                holder2.resource_item_img.setImageResource(R.drawable.pdf);
                break;
            case "text":
            case "txt":
                holder2.resource_item_img.setImageResource(R.drawable.txt);
                break;
            case "video":
            case "mp4":
            case "m3u8":
                holder2.resource_item_img.setImageResource(R.drawable.mp4);
                break;
            case "audio":
            case "mp3":
                holder2.resource_item_img.setImageResource(R.drawable.mp3);
                break;
            case "image":
            case "jpg":
            case "jpeg":
            case "gif":
            case "png":
                holder2.resource_item_img.setImageResource(R.drawable.jpg);
                break;
            default:
                holder2.resource_item_img.setImageResource(R.drawable.weizhi);
                break;
        }
        if ("1".equals(data.getType())) {
            holder2.resource_item_img.setImageResource(R.drawable.html);
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


