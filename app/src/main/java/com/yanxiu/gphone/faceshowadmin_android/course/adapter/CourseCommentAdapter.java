package com.yanxiu.gphone.faceshowadmin_android.course.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.net.base.ResponseConfig;
import com.yanxiu.gphone.faceshowadmin_android.net.course.DeleteUserCommentRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.DeleteUserCommentResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.course.GetCourseCommentRecordsResponse;
import com.yanxiu.gphone.faceshowadmin_android.net.course.LikeCommentRecordRequest;
import com.yanxiu.gphone.faceshowadmin_android.net.course.LikeCommentRecordResponse;
import com.yanxiu.gphone.faceshowadmin_android.utils.DateFormatUtil;
import com.yanxiu.gphone.faceshowadmin_android.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by frc on 17-11-10.
 */

public class CourseCommentAdapter extends RecyclerView.Adapter {

    private static final int TYPE_TITLE = 0x0000;
    private static final int TYPE_DEFAULT = 0x0001;
    private static final int ANIM_OPEN = 0x0002;
    private static final int ANIM_CLOSE = 0x0003;
    private static final int ANIM_DURATION = 200;
    private static final int ANIM_POSITION_DEFAULT = -1;
    private static final int REFRESH_ANIM_VIEW = 0x0004;

    private int animPosition = ANIM_POSITION_DEFAULT;

    private List<GetCourseCommentRecordsResponse.ElementsBean> records = new ArrayList<>();
    private String description;
    private String totalNum;
    private final int TYPE_HEAD = 0;
    private final int TYPE_NORMAL = 1;
    private final int TYPE_FOOTER = 2;

    public CourseCommentAdapter(List<GetCourseCommentRecordsResponse.ElementsBean> records, String description, String s) {
        this.records = records;
        this.description = description;
        this.totalNum = s;
    }


    public void deleteData(int position) {
        if (position > -1 && position < getItemCount()) {
            records.remove(position - 1);
            this.notifyDataSetChanged();
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                return new HeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_comment_head_layout, parent, false));
            default:
                return new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_comment_normal_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if ((int) payloads.get(0) == REFRESH_ANIM_VIEW) {
                ((NormalViewHolder) holder).mLlAnim.setVisibility(View.INVISIBLE);
                ((NormalViewHolder) holder).mLlAnim.setEnabled(false);
            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEAD:

                ((HeadViewHolder) holder).setData(description, totalNum);
                break;
            default:
                if (position == getItemCount() - 1) {
                    ((NormalViewHolder) holder).line.setVisibility(View.GONE);
                } else {
                    ((NormalViewHolder) holder).line.setVisibility(View.VISIBLE);
                }
                ((NormalViewHolder) holder).mLlAnim.setVisibility(View.INVISIBLE);
                ((NormalViewHolder) holder).mLlAnim.setEnabled(false);
                ((NormalViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (animPosition != ANIM_POSITION_DEFAULT) {
                            notifyItemChanged(animPosition, REFRESH_ANIM_VIEW);
                            animPosition = ANIM_POSITION_DEFAULT;
                        }
                    }
                });

                ((NormalViewHolder) holder).mZan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setViewAnim((holder).getAdapterPosition(), ((NormalViewHolder) holder).mLlAnim);
                    }
                });

                ((NormalViewHolder) holder).setData(records.get(position - 1));

                ((NormalViewHolder) holder).mTvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.showToast(((NormalViewHolder) holder).itemView.getContext(), "delete");
                        if (animPosition != ANIM_POSITION_DEFAULT) {
                            notifyItemChanged(animPosition, REFRESH_ANIM_VIEW);
                            animPosition = ANIM_POSITION_DEFAULT;
                        }
                        deleteReply((NormalViewHolder) holder, String.valueOf(records.get(position).getId()));
                    }
                });

                ((NormalViewHolder) holder).mTvThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (animPosition != ANIM_POSITION_DEFAULT) {
                            notifyItemChanged(animPosition, REFRESH_ANIM_VIEW);
                            animPosition = ANIM_POSITION_DEFAULT;
                        }
                        tumb((NormalViewHolder) holder, String.valueOf(records.get(position - 1).getId()));
                    }
                });


        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;

        }
    }

    @Override
    public int getItemCount() {
        return records.size() + 1;
    }

    public void update(List<GetCourseCommentRecordsResponse.ElementsBean> records, String description, String totalNum) {
        this.records = records;
        this.description = description;
        this.totalNum = totalNum;
        notifyDataSetChanged();
    }

    private void deleteReply(final NormalViewHolder holder, String id) {
        DeleteUserCommentRequest deleteUserCommentRequest = new DeleteUserCommentRequest();
        deleteUserCommentRequest.commentRecordId = id;
        deleteUserCommentRequest.startRequest(DeleteUserCommentResponse.class, new HttpCallback<DeleteUserCommentResponse>() {
            @Override
            public void onSuccess(RequestBase request, DeleteUserCommentResponse ret) {
                if (ret.getCode() == ResponseConfig.SUCCESS) {
//                    deleteItem(holder.getAdapterPosition());
                } else {
                    ToastUtil.showToast(holder.itemView.getContext(), ret.getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                ToastUtil.showToast(holder.itemView.getContext(), error.getMessage());

            }
        });
    }

    private void modifyTumbNumber(int itemPosition) {

    }

    private void tumb(final NormalViewHolder holder, String id) {
        LikeCommentRecordRequest likeCommentRecordRequest = new LikeCommentRecordRequest();
        likeCommentRecordRequest.commentRecordId = id;
        likeCommentRecordRequest.startRequest(LikeCommentRecordResponse.class, new HttpCallback<LikeCommentRecordResponse>() {
            @Override
            public void onSuccess(RequestBase request, LikeCommentRecordResponse ret) {
                if (ResponseConfig.SUCCESS == ret.getCode()) {
//                    modifyTumbNumber(itemPosition);
                    holder.mZanNumber.setText(holder.itemView.getContext().getString(R.string.zan_number, ret.getData().getUserNum()));
                } else {
                    ToastUtil.showToast(holder.itemView.getContext(), ret.getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                ToastUtil.showToast(holder
                        .itemView.getContext(), error.getMessage());
            }
        });
    }


    static class HeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_discuss_name)
        TextView mTvDiscussName;
        @BindView(R.id.tv_reply_number)
        TextView mTvReplyNumber;

        HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(String description, String number) {
            mTvDiscussName.setText(description);
            mTvReplyNumber.setText(itemView.getContext().getString(R.string.reply_number, number));
        }

    }


    static class NormalViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        @BindView(R.id.reply_person_name)
        TextView mReplyPersonName;
        @BindView(R.id.tv_reply_content)
        TextView mTvReplyContent;
        @BindView(R.id.tv_reply_time)
        TextView mTvReplyTime;
        @BindView(R.id.zan)
        ImageView mZan;
        @BindView(R.id.zan_number)
        TextView mZanNumber;
        @BindView(R.id.tv_thumb)
        RelativeLayout mTvThumb;
        @BindView(R.id.tv_delete)
        RelativeLayout mTvDelete;
        @BindView(R.id.ll_anim)
        LinearLayout mLlAnim;
        @BindView(R.id.line)
        View line;

        NormalViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        void setData(GetCourseCommentRecordsResponse.ElementsBean element) {
            mReplyPersonName.setText(element.getUserName());
            mTvReplyContent.setText(element.getContent());
            mTvReplyTime.setText(DateFormatUtil.getReplyTime(element.getCreateTime()));
            mZanNumber.setText(itemView.getContext().getString(R.string.zan_number, element.getLikeNum()));
        }
    }

    private void setViewAnim(int position, View view) {
        int width = view.getWidth();
        float scaleStart;
        float scaleEnd;
        int translationStart;
        final int translationEnd;
        final int animType;
        if (animPosition == ANIM_POSITION_DEFAULT) {
            scaleStart = 0f;
            scaleEnd = 1f;
            translationStart = width / 2;
            translationEnd = 0;
            animType = ANIM_OPEN;
            animPosition = position;
        } else if (animPosition == position) {
            scaleStart = 1f;
            scaleEnd = 0f;
            translationStart = 0;
            translationEnd = width / 2;
            animType = ANIM_CLOSE;
            animPosition = ANIM_POSITION_DEFAULT;
        } else {
            notifyItemChanged(animPosition, REFRESH_ANIM_VIEW);
            scaleStart = 0f;
            scaleEnd = 1f;
            translationStart = width / 2;
            translationEnd = 0;
            animType = ANIM_OPEN;
            animPosition = position;
        }

        ViewCompat.setScaleX(view, scaleStart);
        ViewCompat.setTranslationX(view, translationStart);
        ViewCompat.animate(view).setDuration(ANIM_DURATION).translationX(translationEnd).scaleX(scaleEnd).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                view.setEnabled(true);
                if (animType == ANIM_OPEN) {
                    view.setVisibility(View.VISIBLE);
                    view.setEnabled(true);
                }
            }

            @Override
            public void onAnimationEnd(View view) {
                if (animType == ANIM_CLOSE) {
                    view.setVisibility(View.INVISIBLE);
                    view.setEnabled(false);
                }
            }

            @Override
            public void onAnimationCancel(View view) {
                if (animType == ANIM_CLOSE) {
                    view.setVisibility(View.INVISIBLE);
                    view.setEnabled(false);
                }
            }
        }).start();
    }

}
