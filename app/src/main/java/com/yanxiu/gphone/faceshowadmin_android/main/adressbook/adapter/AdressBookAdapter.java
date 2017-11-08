package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response.AdressBookPeople;
import com.yanxiu.gphone.faceshowadmin_android.utils.CornersImageTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 11:02.
 * Function :
 */
public class AdressBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface onItemClickListener{
        void onItemClick(int position,AdressBookPeople people);
    }

    private static final int TYPE_HEAD=0x000;
    private static final int TYPE_ITEM=0x001;

    private Context mContext;
    private List<AdressBookPeople> mData=new ArrayList<>();
    private onItemClickListener mItemClickListener;

    public AdressBookAdapter(Context context){
        this.mContext=context;
    }

    public void setData(List<AdressBookPeople> data){
        if (data==null||data.size()<=0){
            return;
        }
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<AdressBookPeople> data){
        if (data!=null&&data.size()>0){
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void setItemClickListener(onItemClickListener itemClickListener){
        this.mItemClickListener=itemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        AdressBookPeople people=mData.get(position);
        if (people.type==0){
            return TYPE_ITEM;
        }else {
            return TYPE_HEAD;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_HEAD){
            View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_adress_head,parent,false);
            return new HeadViewHolder(view);
        }else {
            View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_adress_item,parent,false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdressBookPeople bookPeople=mData.get(position);
        if (holder instanceof HeadViewHolder){
            ((HeadViewHolder) holder).mTitleView.setText(bookPeople.text);
            if (position==0){
                ((HeadViewHolder) holder).mLineView.setVisibility(View.GONE);
            }else {
                ((HeadViewHolder) holder).mLineView.setVisibility(View.VISIBLE);
            }
        }else if (holder instanceof ItemViewHolder){
            ((ItemViewHolder) holder).mNameView.setText(bookPeople.realName);
            ((ItemViewHolder) holder).mMobileView.setText(bookPeople.mobilePhone);
//            if (!TextUtils.isEmpty(bookPeople.avatar)) {
                Glide.with(mContext).load(bookPeople.avatar).asBitmap().placeholder(R.drawable.classcircle_headimg_small).centerCrop().into(new CornersImageTarget(mContext, ((ItemViewHolder) holder).mHeadImgView, 10));
//            }
        }
    }

    @Override
    public int getItemCount() {
        return mData!=null?mData.size():0;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder{

        View mLineView;
        TextView mTitleView;

        HeadViewHolder(View itemView) {
            super(itemView);
            mLineView=itemView.findViewById(R.id.line);
            mTitleView=itemView.findViewById(R.id.tv_title);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        ImageView mHeadImgView;
        TextView mNameView;
        TextView mMobileView;

        ItemViewHolder(View itemView) {
            super(itemView);
            mHeadImgView=itemView.findViewById(R.id.iv_head_img);
            mNameView=itemView.findViewById(R.id.tv_name);
            mMobileView=itemView.findViewById(R.id.tv_mobile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener!=null){
                        int position=getLayoutPosition();
                        mItemClickListener.onItemClick(position,mData.get(position));
                    }
                }
            });
        }
    }
}
