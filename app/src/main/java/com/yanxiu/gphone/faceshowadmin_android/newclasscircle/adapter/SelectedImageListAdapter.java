package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.bean.ImageItem;
import com.yanxiu.gphone.faceshowadmin_android.R;


import java.util.ArrayList;
import java.util.List;

/**
 * @author frc on 2018/1/12.
 */

public class SelectedImageListAdapter extends RecyclerView.Adapter<SelectedImageListAdapter.ViewHolder> {
    private List<ImageItem> data;

    public SelectedImageListAdapter(ArrayList<ImageItem> data) {
        this.data = data;
    }

    private PicClickListener mPicClickListener;
    private DataRemoveListener mDataRemoveListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_image_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            //最后一个现实添加图片文案
            Glide.with(holder.itemView.getContext()).load(R.drawable.class_circle_add_picture).into(holder.mSelectedImage);

            holder.mImgDel.setVisibility(View.GONE);
        } else {
            Glide.with(holder.itemView.getContext()).load(data.get(position).path).into(holder.mSelectedImage);
            holder.mImgDel.setVisibility(View.VISIBLE);
        }

        holder.mSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getAdapterPosition() == getItemCount() - 1) {
                    mPicClickListener.addPic();
                } else {
                    mPicClickListener.showBigPic(holder.getAdapterPosition());
                }

            }
        });
        holder.mImgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataRemoveListener != null) {
                    data.remove(holder.getAdapterPosition());
                    mDataRemoveListener.remove(holder.getAdapterPosition());
                }


            }
        });
    }


    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void update(List<ImageItem> selectedImageList) {
        this.data = selectedImageList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mSelectedImage, mImgDel;

        public ViewHolder(View itemView) {
            super(itemView);
            mSelectedImage = (ImageView) itemView.findViewById(R.id.img_selected);
            mImgDel = (ImageView) itemView.findViewById(R.id.img_delete);
        }
    }

    public void addPicClickListener(PicClickListener picClickListener) {
        this.mPicClickListener = picClickListener;
    }

    public void setDataRemoveListener(DataRemoveListener dataRemoveListener) {
        this.mDataRemoveListener = dataRemoveListener;
    }

    public interface PicClickListener {
        /**
         * 添加图片的回调
         */
        void addPic();

        /**
         * 显示大图的回调
         *
         * @param position 图片index
         */
        void showBigPic(int position);
    }

    public interface DataRemoveListener {
        /**
         * 移除选中图片的回调
         *
         * @param pos 被移除图片的index
         */
        void remove(int pos);
    }

}
