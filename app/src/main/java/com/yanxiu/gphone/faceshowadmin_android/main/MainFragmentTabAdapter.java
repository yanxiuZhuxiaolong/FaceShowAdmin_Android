package com.yanxiu.gphone.faceshowadmin_android.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.interf.MainFragmentRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshowadmin_android.utils.recyclerView.BaseRecyclerViewAdapter;

import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by 戴延枫
 * 首页fragment里的几个tab的adapter
 */

public class MainFragmentTabAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;

    private ArrayList<MainTabBean> mList;
    private MainFragmentRecyclerViewItemClickListener mListener;

    public MainFragmentTabAdapter(Context context, MainFragmentRecyclerViewItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(ArrayList<MainTabBean> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.mainfragment_tab_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MainTabBean data = mList.get(position);

        ViewHolder holder2 = (ViewHolder) holder;
        holder2.name.setText(data.getName());
        holder2.imageView.setImageResource(data.getImgResourcesId());
        holder2.setPosition(position);


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
        private ImageView imageView;
        private TextView name;

        private int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.tab_item_layout);
            imageView = itemView.findViewById(R.id.tab_img);
            name = itemView.findViewById(R.id.tab_txt);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onTabItemClick(v, position);
                    }
                }
            });
//            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_PLAY, mPrefixNumber, mPostfixNumber);
        }


    }

}


