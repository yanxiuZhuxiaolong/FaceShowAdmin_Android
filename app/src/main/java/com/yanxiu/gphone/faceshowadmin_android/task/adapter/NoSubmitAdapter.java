package com.yanxiu.gphone.faceshowadmin_android.task.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;
import com.yanxiu.gphone.faceshowadmin_android.net.clazz.checkIn.GetClassUserResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.task.GetClazsUserQuestionResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by frc on 17-11-2.
 */

public class NoSubmitAdapter extends BaseRecyclerViewAdapter {
    private List<GetClazsUserQuestionResponse.ElementsBean> data = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_submit_fragment_dapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void update(List<GetClazsUserQuestionResponse.ElementsBean> elements) {
        this.data = elements;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_student_name)
        TextView mTvStudentName;
        @BindView(R.id.tv_student_mobile_number)
        TextView mTvStudentMobileNumber;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setData(GetClazsUserQuestionResponse.ElementsBean data) {
            mTvStudentName.setText(data.getUserName());
            mTvStudentMobileNumber.setText(data.getMobilePhone());


        }
    }
}
