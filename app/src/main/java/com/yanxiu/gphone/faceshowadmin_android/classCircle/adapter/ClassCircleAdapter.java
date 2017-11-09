package com.yanxiu.gphone.faceshowadmin_android.classCircle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.Comments;
import com.yanxiu.gphone.faceshowadmin_android.common.activity.PhotoActivity;
import com.yanxiu.gphone.faceshowadmin_android.customView.ClassCircleCommentLayout;
import com.yanxiu.gphone.faceshowadmin_android.customView.ClassCircleThumbView;
import com.yanxiu.gphone.faceshowadmin_android.customView.MaxLineTextLayout;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.utils.CornersImageTarget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/14 16:07.
 * Function :
 */
public class ClassCircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface onCommentClickListener {
        void commentClick(int position, ClassCircleResponse.Data.Moments response, int commentPosition, Comments comment, boolean isCommentMaster);

        void commentFinish();
    }

    public interface onLikeClickListener {
        void likeClick(int position, ClassCircleResponse.Data.Moments response);
    }

    public interface onDeleteClickListener{
        void deleteClick(int position, ClassCircleResponse.Data.Moments response);
    }

    public interface onContentLinesChangedlistener{
        void onContentLinesChanged(int position, boolean isShowAll);
    }

    private static final int TYPE_TITLE = 0x0000;
    private static final int TYPE_DEFAULT = 0x0001;
    private static final int ANIM_OPEN = 0x0002;
    private static final int ANIM_CLOSE = 0x0003;
    private static final int ANIM_DURATION = 200;
    private static final int ANIM_POSITION_DEFAULT = -1;
    private static final int REFRESH_ANIM_VIEW=0x0004;
    public static final int REFRESH_LIKE_DATA=0x0005;
    public static final int REFRESH_COMMENT_DATA=0x0006;

    private Context mContext;
    private List<ClassCircleResponse.Data.Moments> mData = new ArrayList<>();
    private int animPosition = ANIM_POSITION_DEFAULT;
    private onCommentClickListener mCommentClickListener;
    private onLikeClickListener mLikeClickListener;
    private onDeleteClickListener mDeleteClickListener;
    private onContentLinesChangedlistener mContentLinesChangedlistener;

    public ClassCircleAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<ClassCircleResponse.Data.Moments> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.mData.clear();
        this.mData.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addData(List<ClassCircleResponse.Data.Moments> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.mData.addAll(list);
        this.notifyDataSetChanged();
    }

    public void deleteData(int position){
        if (position>-1&&position<mData.size()){
            mData.remove(position);
            this.notifyDataSetChanged();
        }
    }

    public void clear() {
        this.mData.clear();
        this.notifyDataSetChanged();
    }

    public ClassCircleResponse.Data.Moments getDataFromPosition(int position) {
        if (position < 1 || position > mData.size()) {
            return null;
        }
        return mData.get(position - 1);
    }

    public String getIdFromLastPosition() {
        if (mData.size() > 0) {
            return mData.get(mData.size() - 1).id;
        } else {
            return "0";
        }
    }

    public void setCommentClickListener(onCommentClickListener commentClickListener) {
        this.mCommentClickListener = commentClickListener;
    }

    public void setThumbClickListener(onLikeClickListener likeClickListener) {
        this.mLikeClickListener = likeClickListener;
    }

    public void setDeleteClickListener(onDeleteClickListener deleteClickListener){
        this.mDeleteClickListener=deleteClickListener;
    }

    public void setContentLinesChangedlistener(onContentLinesChangedlistener contentLinesChangedlistener){
        this.mContentLinesChangedlistener=contentLinesChangedlistener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TITLE;
        }
        return TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_class_circle_title, parent, false);
            return new TitleViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_class_circle_item, parent, false);
            return new ClassCircleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()){
            onBindViewHolder(holder, position);
        }else {
            final ClassCircleViewHolder classCircleViewHolder = (ClassCircleViewHolder) holder;
            final ClassCircleResponse.Data.Moments moments = mData.get(position - 1);
            int refresh= (int) payloads.get(0);
            switch (refresh){
                case REFRESH_ANIM_VIEW:
                    classCircleViewHolder.mAnimLayout.setVisibility(View.INVISIBLE);
                    classCircleViewHolder.mAnimLayout.setEnabled(false);
                    break;
                case REFRESH_COMMENT_DATA:
                    setViewVisibly(classCircleViewHolder,moments);
                    classCircleViewHolder.mCircleCommentLayout.setData(moments.comments);
                    break;
                case REFRESH_LIKE_DATA:
                    setViewVisibly(classCircleViewHolder,moments);
                    classCircleViewHolder.mCircleThumbView.setData(moments.likes);
                    break;
            }
        }
    }

    private void setViewVisibly(ClassCircleViewHolder classCircleViewHolder,ClassCircleResponse.Data.Moments moments){
        boolean isLikeHasData = moments.likes != null && moments.likes.size() > 0;
        boolean isCommentHasData = moments.comments != null && moments.comments.size() > 0;
        if (isCommentHasData && isLikeHasData) {
            classCircleViewHolder.mLikeCommentLineView.setVisibility(View.VISIBLE);
        } else {
            classCircleViewHolder.mLikeCommentLineView.setVisibility(View.GONE);
        }
        if (isCommentHasData || isLikeHasData) {
            classCircleViewHolder.mLikeCommentLayout.setVisibility(View.VISIBLE);
        } else {
            classCircleViewHolder.mLikeCommentLayout.setVisibility(View.GONE);
        }
        if (checkIsThumb(moments.likes)) {
            classCircleViewHolder.mThumbView.setVisibility(View.GONE);
            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal_2);
        } else {
            classCircleViewHolder.mThumbView.setVisibility(View.VISIBLE);
            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal_3);
        }
    }

    private class ClassCircleImageTager extends BitmapImageViewTarget{

        ClassCircleImageTager(ImageView view) {
            super(view);
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            view.setImageDrawable(errorDrawable);
            view.setEnabled(false);
        }

        @Override
        protected void setResource(Bitmap resource) {
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setImageBitmap(resource);
            view.setEnabled(true);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.mNameView.setText(UserInfo.getInstance().getInfo().getRealName());
            Glide.with(mContext).load(UserInfo.getInstance().getInfo().getAvatar()).asBitmap().placeholder(R.drawable.classcircle_headimg).centerCrop().into(new CornersImageTarget(mContext, titleViewHolder.mHeadImgView, 10));
        } else if (holder instanceof ClassCircleViewHolder) {
            final ClassCircleViewHolder classCircleViewHolder = (ClassCircleViewHolder) holder;
            final ClassCircleResponse.Data.Moments moments = mData.get(position - 1);

            String headimg = "";
            if (moments.publisher != null) {
                headimg = moments.publisher.avatar;
            }
            Glide.with(mContext).load(headimg).asBitmap().placeholder(R.drawable.classcircle_headimg_small).centerCrop().into(new CornersImageTarget(mContext, classCircleViewHolder.mHeadImgView, 10));
            classCircleViewHolder.mContentImageView.setEnabled(false);
            if (moments.album != null && moments.album.size() > 0) {
                classCircleViewHolder.mContentImageView.setVisibility(View.VISIBLE);
                if (moments.album.get(0).attachment != null) {
                    String imgPath = moments.album.get(0).attachment.previewUrl;
                    if (classCircleViewHolder.mContentImgUrl == null || !classCircleViewHolder.mContentImgUrl.equals(imgPath)) {
                        Glide.with(mContext).load(imgPath).asBitmap().error(R.drawable.net_error_picture).into(new ClassCircleImageTager(classCircleViewHolder.mContentImageView));
                        classCircleViewHolder.mContentImgUrl = imgPath;

                        classCircleViewHolder.mContentImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<String> list = new ArrayList<>();
                                list.add(moments.album.get(0).attachment.downloadUrl);
                                PhotoActivity.LaunchActivity(mContext, list, 0, mContext.hashCode(), PhotoActivity.DELETE_CANNOT);
                            }
                        });
                    }
                }
            } else {
                classCircleViewHolder.mContentImageView.setVisibility(View.GONE);
            }
            if (moments.publisher != null) {
                classCircleViewHolder.mNameView.setText(moments.publisher.realName);
            }
            classCircleViewHolder.mContentView.setData(moments.content, moments.isShowAll, new MaxLineTextLayout.onLinesChangedListener() {
                @Override
                public void onLinesChanged(boolean isShowAll) {
                    moments.isShowAll=isShowAll;
                    if (mContentLinesChangedlistener!=null){
                        mContentLinesChangedlistener.onContentLinesChanged(classCircleViewHolder.getAdapterPosition(),isShowAll);
                    }
                }
            });
            classCircleViewHolder.mTimeView.setText(moments.publishTimeDesc);
            classCircleViewHolder.mAnimLayout.setVisibility(View.INVISIBLE);
            classCircleViewHolder.mAnimLayout.setEnabled(false);
            classCircleViewHolder.mCircleThumbView.setData(moments.likes);
            classCircleViewHolder.mCircleCommentLayout.setData(moments.comments);

            setViewVisibly(classCircleViewHolder,moments);

            classCircleViewHolder.mCircleCommentLayout.setItemClickListener(new ClassCircleCommentLayout.onItemClickListener() {
                @Override
                public void onItemClick(Comments comments, int position) {
                    if (!comments.publisher.userId.equals(String.valueOf(UserInfo.getInstance().getInfo().getUserId()))) {
                        if (mCommentClickListener != null) {
                            mCommentClickListener.commentClick(classCircleViewHolder.getAdapterPosition(), moments, position, comments, false);
                        }
                    }
                }
            });

            classCircleViewHolder.mFunctionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setViewAnim(classCircleViewHolder.getAdapterPosition(), classCircleViewHolder.mAnimLayout);
                }
            });
            classCircleViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (animPosition != ANIM_POSITION_DEFAULT) {
                        notifyItemChanged(animPosition,REFRESH_ANIM_VIEW);
                        animPosition = ANIM_POSITION_DEFAULT;
                    }
                    if (mCommentClickListener != null) {
                        mCommentClickListener.commentFinish();
                    }
                    return false;
                }
            });
            classCircleViewHolder.mCommentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCommentClickListener != null) {
                        mCommentClickListener.commentClick(classCircleViewHolder.getAdapterPosition(), moments, -1, null, true);
                    }
                    classCircleViewHolder.mAnimLayout.setVisibility(View.INVISIBLE);
                    classCircleViewHolder.mAnimLayout.setEnabled(false);
                    animPosition = ANIM_POSITION_DEFAULT;
                }
            });
            classCircleViewHolder.mCommentView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (checkIsThumb(moments.likes)) {
                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_press_left_2);
                        } else {
                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_press_center_3);
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        if (checkIsThumb(moments.likes)) {
                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal_2);
                        } else {
                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal_3);
                        }
                    }
                    return false;
                }
            });
            classCircleViewHolder.mThumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLikeClickListener != null) {
                        mLikeClickListener.likeClick(classCircleViewHolder.getAdapterPosition(), moments);
                    }
                    classCircleViewHolder.mAnimLayout.setVisibility(View.INVISIBLE);
                    classCircleViewHolder.mAnimLayout.setEnabled(false);
                    animPosition = ANIM_POSITION_DEFAULT;
                }
            });
            classCircleViewHolder.mThumbView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_press_left_3);
                    } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal_3);
                    }
                    return false;
                }
            });
            classCircleViewHolder.mDeleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDeleteClickListener != null) {
                        mDeleteClickListener.deleteClick(classCircleViewHolder.getAdapterPosition(), moments);
                    }
                    classCircleViewHolder.mAnimLayout.setVisibility(View.INVISIBLE);
                    classCircleViewHolder.mAnimLayout.setEnabled(false);
                    animPosition = ANIM_POSITION_DEFAULT;
                }
            });
            classCircleViewHolder.mDeleteView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        if (checkIsThumb(moments.likes)) {
                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_press_right_2);
                        } else {
                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_press_right_3);
                        }
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        if (checkIsThumb(moments.likes)) {
                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal_2);
                        } else {
                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal_3);
                        }
                    }
                    return false;
                }
            });
        }
    }

    private boolean checkIsThumb(ArrayList<ClassCircleResponse.Data.Moments.Likes> likes) {
        for (ClassCircleResponse.Data.Moments.Likes like : likes) {
            if (like.publisher != null && !TextUtils.isEmpty(like.publisher.userId)) {
                if (like.publisher.userId.equals(String.valueOf(UserInfo.getInstance().getInfo().getUserId()))) {
                    return true;
                }
            }
        }
        return false;
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
            notifyItemChanged(animPosition,REFRESH_ANIM_VIEW);
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

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() + 1 : 1;
    }

    class ClassCircleViewHolder extends RecyclerView.ViewHolder {

        String mContentImgUrl;

        @BindView(R.id.iv_head_img)
        ImageView mHeadImgView;
        @BindView(R.id.tv_name)
        TextView mNameView;
        @BindView(R.id.tv_content)
        MaxLineTextLayout mContentView;
        @BindView(R.id.tv_time)
        TextView mTimeView;
        @BindView(R.id.iv_function)
        ImageView mFunctionView;
        @BindView(R.id.gv_imgs)
        ImageView mContentImageView;
        @BindView(R.id.cc_thumb)
        ClassCircleThumbView mCircleThumbView;
        @BindView(R.id.cc_comments)
        ClassCircleCommentLayout mCircleCommentLayout;
        @BindView(R.id.ll_anim)
        LinearLayout mAnimLayout;
        @BindView(R.id.tv_thumb)
        RelativeLayout mThumbView;
        @BindView(R.id.tv_comment)
        RelativeLayout mCommentView;
        @BindView(R.id.tv_delete)
        RelativeLayout mDeleteView;
        @BindView(R.id.like_comment_line)
        View mLikeCommentLineView;
        @BindView(R.id.ll_like_comment)
        LinearLayout mLikeCommentLayout;

        ClassCircleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_head_img)
        ImageView mHeadImgView;
        @BindView(R.id.tv_name)
        TextView mNameView;

        TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
