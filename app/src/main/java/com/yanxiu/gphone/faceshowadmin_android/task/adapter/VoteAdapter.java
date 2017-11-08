package com.yanxiu.gphone.faceshowadmin_android.task.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.VoteResultLayout;
import com.yanxiu.gphone.faceshowadmin_android.model.QusetionBean;
import com.yanxiu.gphone.faceshowadmin_android.model.QusetionGroupBean;
import com.yanxiu.gphone.faceshowadmin_android.task.activity.VoteDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yanxiu.gphone.faceshowadmin_android.model.VoteBean.TYPE_MULTI;
import static com.yanxiu.gphone.faceshowadmin_android.model.VoteBean.TYPE_SINGLE;
import static com.yanxiu.gphone.faceshowadmin_android.model.VoteBean.TYPE_TEXT;

/**
 * adapter  for voteActivity RecyclerView
 * Created by frc on 17-11-7.
 */

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.BaseViewHolder> {
    private final int TYPE_HEAD = 0x04;
    private QusetionGroupBean data;

    public VoteAdapter(QusetionGroupBean questionGroup) {
        this.data = questionGroup;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEAD:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vote_list_head_layout, parent, false);
                return new HeadViewHolder(view);
            case TYPE_SINGLE:
            case TYPE_MULTI:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vote_list_result_progress__layout, parent, false);
                return new ProgressViewHolder(view);
            case TYPE_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vote_list_answer_layout, parent, false);
                return new ReplyViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                holder.setData(data);
                break;
            case TYPE_SINGLE:
            case TYPE_MULTI:
                holder.setData(data.getQuestions().get(position - 1));
                break;
            case TYPE_TEXT:
                holder.setData(data.getQuestions().get(position - 1));

                break;
            default:
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEAD;
            default:
                return data.getQuestions().get(position - 1).getQuestionType();
        }
    }

    @Override
    public int getItemCount() {
        return data.getQuestions().size() + 1;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(Object obj) {

        }
    }

    class HeadViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_vote_name)
        TextView mTvVoteName;
        @BindView(R.id.tv_see_detail)
        TextView mTvSeeDetail;
        @BindView(R.id.tv_voted_person_number)
        TextView mTvVotedPersonNumber;

        HeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void setData(Object obj) {
            final QusetionGroupBean qusetionGroupBean = (QusetionGroupBean) obj;
            mTvVoteName.setText(qusetionGroupBean.getTitle());
            mTvVotedPersonNumber.setText(Html.fromHtml(itemView.getContext().getString(R.string.vote_person_number, qusetionGroupBean.getAnswerNum(), qusetionGroupBean.getTotalUserNum())));
            mTvSeeDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), VoteDetailActivity.class);
                    intent.putExtra("submitNum", qusetionGroupBean.getAnswerUserNum());
                    intent.putExtra("totalNum", qusetionGroupBean.getTotalUserNum());
                    intent.putExtra("stepId", String.valueOf(qusetionGroupBean.getStepId()));
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    class ProgressViewHolder extends BaseViewHolder {
        @BindView(R.id.voteResult_title)
        TextView mVoteResultTitle;
        @BindView(R.id.voteResult_Layout)
        VoteResultLayout mVoteResultLayout;

        ProgressViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void setData(Object obj) {
            QusetionBean qusetionBean = (QusetionBean) obj;
            mVoteResultTitle.setText(getAdapterPosition() + "、" + qusetionBean.getTitle() + "(" + qusetionBean.getQuestionTypeName() + ")");
            mVoteResultLayout.setData(qusetionBean.getVoteInfo());
        }
    }

    class ReplyViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_vote_title)
        TextView mTvVoteTitle;
        @BindView(R.id.tv_see_reply)
        TextView mTvSeeReply;

        ReplyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void setData(Object obj) {
            QusetionBean qusetionBean = (QusetionBean) obj;
            mTvVoteTitle.setText(getAdapterPosition() + "、" + qusetionBean.getTitle());
            mTvSeeReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
