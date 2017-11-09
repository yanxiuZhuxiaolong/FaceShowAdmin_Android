package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response.SignRecordResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.DateFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 17:11.
 * Function :
 */
public class SignRecordAdapter extends RecyclerView.Adapter<SignRecordAdapter.SignRecordViewHolder> {

    public interface onSignClickListener{
        void onSignClick(int position,SignRecordResponse.SignRecordData.SignIns signIns);
    }

    private Context mContext;
    private List<SignRecordResponse.SignRecordData.SignIns> mData=new ArrayList<>();
    private onSignClickListener mSignClickListener;

    public SignRecordAdapter(Context context){
        this.mContext=context;
    }

    public void setData(List<SignRecordResponse.SignRecordData.SignIns> data){
        if (data.isEmpty()){
            return;
        }
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setSignSuccess(int position){
        if (position>-1&&position<mData.size()){
            mData.get(position).stepFinished=1;
            notifyDataSetChanged();
        }
    }

    public void setSignClickListener(onSignClickListener signClickListener){
        this.mSignClickListener=signClickListener;
    }

    @Override
    public SignRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_signrecord,parent,false);
        return new SignRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SignRecordViewHolder holder, final int position) {
        final SignRecordResponse.SignRecordData.SignIns signIns=mData.get(position);
        final String time;
        if (signIns.startTime == null || signIns.endTime == null) {
            time = "";
        } else {
            String translationTime = DateFormatUtil.translationBetweenTwoFormat(signIns.startTime, DateFormatUtil.FORMAT_ONE, DateFormatUtil.FORMAT_TWO);
            String[] startTimes = translationTime.split(" ");
            String[] startTime = startTimes[1].split(":");
            String[] endTime = signIns.endTime.split(" ")[1].split(":");
            time = startTimes[0] + " " + startTime[0] + ":" + startTime[1] + "-" + endTime[0] + ":" + endTime[1];
        }
        holder.mTimeView.setText(time);
        holder.mContentView.setText(signIns.title);
        String type;
        if (signIns.stepFinished==0){
            type="补签";
        }else {
            type="已签到";
        }
        holder.mTypeView.setText(type);
        holder.mTypeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSignClickListener!=null&&signIns.stepFinished==0){
                    mSignClickListener.onSignClick(position,signIns);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData!=null?mData.size():0;
    }

    class SignRecordViewHolder extends RecyclerView.ViewHolder{

        TextView mContentView;
        TextView mTimeView;
        TextView mTypeView;

        SignRecordViewHolder(View itemView) {
            super(itemView);
            mContentView=itemView.findViewById(R.id.tv_content);
            mTimeView=itemView.findViewById(R.id.tv_time);
            mTypeView=itemView.findViewById(R.id.tv_type);
        }
    }

}
