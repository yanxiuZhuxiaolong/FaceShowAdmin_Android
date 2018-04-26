package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.EllipsizingTextView;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request.ClassCircleNewMessageResponse;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author frc on 2018/1/18.
 */

public class ClassCircleMessageAdapter extends RecyclerView.Adapter<ClassCircleMessageAdapter.ViewHolder> {


    private IRecyclerViewItemClick mIRecyclerViewItemClick;
    private List<ClassCircleNewMessageResponse.DataBean.MsgsBean> mMsgsBeanList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classcircle_message_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ClassCircleNewMessageResponse.DataBean.MsgsBean data = mMsgsBeanList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIRecyclerViewItemClick != null) {
                    mIRecyclerViewItemClick.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });

        holder.mTvPublisherName.setText(data.getUserName());
        holder.mTvPublishTime.setText(data.getCreateTime());

        if (data.getMsgType() == 1) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            for (int i = 0; i < data.getLike(); i++) {
                builder.append(setImageSpan(holder.itemView.getContext()));
            }
            holder.mTvMessageContent.setText(builder, TextView.BufferType.SPANNABLE);
        } else if (data.getMsgType() == 2) {
            holder.mTvMessageContent.setText(data.getComment());
        }

        if (!TextUtils.isEmpty(data.getMomentSimple().getImage())) {
            holder.mImgPic.setVisibility(View.VISIBLE);
            holder.mTvPic.setVisibility(View.GONE);
            holder.mTvPic.setMaxLines(4);
            Glide.with(holder.itemView.getContext()).load(data.getMomentSimple().getImage()).into(holder.mImgPic);
        } else {
            holder.mTvPic.setVisibility(View.VISIBLE);
            holder.mImgPic.setVisibility(View.GONE);
            holder.mTvPic.setMaxLines(4);
            holder.mTvPic.setText(data.getMomentSimple().getContent());
        }
    }

    private SpannableString setImageSpan(Context context) {
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new ImageSpan(context, R.drawable.classcircle_like, DynamicDrawableSpan.ALIGN_BASELINE),
                0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }

    @Override
    public int getItemCount() {
        return mMsgsBeanList != null ? mMsgsBeanList.size() : 0;
    }

    public void update(List<ClassCircleNewMessageResponse.DataBean.MsgsBean> msgs) {
        mMsgsBeanList = msgs;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_pic)
        ImageView mImgPic;
        @BindView(R.id.tv_publisher_name)
        TextView mTvPublisherName;
        @BindView(R.id.tv_message_content)
        TextView mTvMessageContent;
        @BindView(R.id.tv_publish_time)
        TextView mTvPublishTime;
        @BindView(R.id.tv_pic)
        EllipsizingTextView mTvPic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setIRecyclerViewItemClick(IRecyclerViewItemClick iRecyclerViewItemClick) {
        mIRecyclerViewItemClick = iRecyclerViewItemClick;
    }
}
