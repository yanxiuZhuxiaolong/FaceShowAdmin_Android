package com.yanxiu.gphone.faceshowadmin_android.checkIn.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.GetClazzListResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetClassUserResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.recyclerView.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by frc on 17-11-2.
 */

public class NoSignInAdapter extends BaseRecyclerViewAdapter {
    private List<GetClassUserResponse.DataBean.ElementsBean> data = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_sign_in_fragment_dapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).setData(data.get(position));
        ((ViewHolder) holder).mTvRetroactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void update(List<GetClassUserResponse.DataBean.ElementsBean> elements) {
        this.data = elements;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_student_name)
        TextView mTvStudentName;
        @BindView(R.id.tv_student_mobile_number)
        TextView mTvStudentMobileNumber;
        @BindView(R.id.tv_retroactive)
        TextView mTvRetroactive;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setData(GetClassUserResponse.DataBean.ElementsBean data) {
            mTvStudentName.setText(data.getUserName());
            mTvStudentMobileNumber.setText(data.getMobilePhone());


        }
    }
}
