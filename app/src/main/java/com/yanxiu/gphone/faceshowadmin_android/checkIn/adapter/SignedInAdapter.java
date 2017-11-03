package com.yanxiu.gphone.faceshowadmin_android.checkIn.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetClassUserResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.DateFormatUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * adapter fot signed in fragment
 * Created by frc on 17-11-2.
 */

public class SignedInAdapter extends BaseRecyclerViewAdapter {
    private List<GetClassUserResponse.DataBean.ElementsBean> data = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signed_in_fragment_adpter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(data.get(position));

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
        @BindView(R.id.tv_student_mobile)
        TextView mTvStudentMobile;
        @BindView(R.id.tv_sign_in_time)
        TextView mTvSignInTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(GetClassUserResponse.DataBean.ElementsBean data) {
            mTvSignInTime.setText(DateFormatUtil.translationBetweenTwoFormat(data.getSignInTime(), DateFormatUtil.FORMAT_ONE, DateFormatUtil.FORMAT_TWO));
            mTvStudentMobile.setText(data.getMobilePhone());
            mTvStudentName.setText(data.getUserName());
        }
    }

}
