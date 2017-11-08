package com.yanxiu.gphone.faceshowadmin_android.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.model.VoteInfoBean;
import com.yanxiu.gphone.faceshowadmin_android.model.VoteItemBean;

import java.util.ArrayList;

/**
 * 投票结果控件
 *
 * @author dyf
 */
public class VoteResultLayout extends LinearLayout {

    private Context mContext;
    private VoteInfoBean mData;

    private final String[] mEms = new String[]{" A.", " B.", " C.", " D.", " E.", " F.", " G.", " H.", " I.", " J.", " K.", " L.", " M.", " N."};

    public VoteResultLayout(Context context) {
        super(context);
        init(context);
    }

    public VoteResultLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoteResultLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        if (isInEditMode()) {
            return;
        }
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public void setData(VoteInfoBean data) {
        mData = data;
        addChildView(data);
    }

    private void addChildView(final VoteInfoBean data) {
        this.removeAllViews();
        ArrayList<VoteItemBean> list = data.getVoteItems();
        for (int i = 0; i < list.size(); i++) {
            VoteItemBean bean = list.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_vote_result_item, this, false);
            final ViewHolder holder = new ViewHolder();
            holder.position = i;
            holder.voteTitle = view.findViewById(R.id.vote_title);
            holder.voteResultCount = view.findViewById(R.id.voteResult_count);
            holder.voteResultProgress = view.findViewById(R.id.voteResult_progress);
            holder.voteResultProgress.setPercent(Float.valueOf(bean.getPercent()));

            holder.voteTitle.setText(mEms[i] + bean.getItemName());
            holder.voteResultCount.setText(bean.getSelectedNum());
            this.addView(view);
        }
    }

    public class ViewHolder {
        public int position;
        TextView voteTitle;
        TextView voteResultCount;
        VoteProgressView voteResultProgress;
    }

}
