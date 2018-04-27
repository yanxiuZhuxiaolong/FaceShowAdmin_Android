package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.ClassCircleCommentLayout;
import com.yanxiu.gphone.faceshowadmin_android.customView.ClassCircleThumbView;
import com.yanxiu.gphone.faceshowadmin_android.customView.MaxLineTextLayout;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.nineGridView.GlideNineImageLoader;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.ninegrid.ImageInfo;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.ninegrid.NineGridView;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.ninegrid.NineGridViewWrapper;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.ninegrid.preview.NineGridViewClickAdapter;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.Comments;
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

    public void showNewMessageNumber(int newMessageNumber) {
        mNewMessageNumber = newMessageNumber;
        notifyItemChanged(0, SHOW_NEW_MESSAGE);
    }

    public interface onCommentClickListener {
        void commentClick(int position, ClassCircleResponse.Data.Moments response, int commentPosition, Comments comment, boolean isCommentMaster);

        void commentFinish();


        void commentCancelClick(int pos, List<ClassCircleResponse.Data.Moments> data, int
                commentPosition, Comments comment);

    }

    public void deleteData(int position) {
        if (position > -1 && position < mData.size()) {
            mData.remove(position);
            this.notifyDataSetChanged();
        }
    }
    public interface onNewMessageButtonClickListener {
        void newMessageButtonClick();
    }

    public interface onLikeClickListener {
        void likeClick(int position, ClassCircleResponse.Data.Moments response);

        void cancelLikeClick(int position, ClassCircleResponse.Data.Moments response);

        void momentPosition(int position);
    }

    public interface onContentLinesChangedlistener {
        void onContentLinesChanged(int position, boolean isShowAll);
    }

    public interface onDeleteClickListener {
        void delete(int position, List<ClassCircleResponse.Data.Moments> data);
    }

    public interface onMomentHeadImageClickListener {
        void myHeadClicked(int position, String userId);

        void otherUserHeadClicked(int positon, String userId);
    }

    private static final int TYPE_TITLE = 0x0000;
    private static final int TYPE_DEFAULT = 0x0001;
    private static final int ANIM_OPEN = 0x0002;
    private static final int ANIM_CLOSE = 0x0003;
    private static final int ANIM_DURATION = 200;
    private static final int ANIM_POSITION_DEFAULT = -1;
    public static final int REFRESH_ANIM_VIEW = 0x0004;
    public static final int REFRESH_LIKE_DATA = 0x0005;
    public static final int REFRESH_COMMENT_DATA = 0x0006;
    private static final int SHOW_NEW_MESSAGE = 0x0007;

    private int mNewMessageNumber = 0;
    private Context mContext;
    private List<ClassCircleResponse.Data.Moments> mData = new ArrayList<>();
    private int animPosition = ANIM_POSITION_DEFAULT;
    private onCommentClickListener mCommentClickListener;
    private onLikeClickListener mLikeClickListener;
    private onContentLinesChangedlistener mContentLinesChangedlistener;
    private onDeleteClickListener mDeleteClickListener;
    private onMomentHeadImageClickListener mMomentHeadImageClickListener;
    private onNewMessageButtonClickListener mNewMessageButtonClickListener;

    public ClassCircleAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 仅仅在offset =0 时候才会调用
     * 也就是在刷新数据的时候才会调用
     * */
    public void setData(List<ClassCircleResponse.Data.Moments> list) {
        /*刷新获取的数据为空 */
        if (list == null || list.size() == 0) {
            /*原来有数据 需要尽行清空操作*/
            if (this.mData != null) {
                this.mData.clear();
            }
            this.notifyDataSetChanged();
        } else {
            /*刷新获取的数据不为空 */
            if (this.mData != null) {
                this.mData.clear();
            }else {
                this.mData=new ArrayList<>();
            }
            this.mData.addAll(list);
            GlideNineImageLoader glideNineImageLoader = new GlideNineImageLoader();
            NineGridView.setImageLoader(glideNineImageLoader);
            this.notifyDataSetChanged();
        }
    }
    /**
     * offset !=0 时候  说明 是需要增加数据
     * */
    public void addData(List<ClassCircleResponse.Data.Moments> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.mData.addAll(list);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.mData.clear();
        this.notifyDataSetChanged();
    }

    public void hideNewMessageButton() {
        mNewMessageNumber = 0;
        notifyItemChanged(0, SHOW_NEW_MESSAGE);
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

    public void setContentLinesChangedlistener(onContentLinesChangedlistener contentLinesChangedlistener) {
        this.mContentLinesChangedlistener = contentLinesChangedlistener;
    }

    public void setDeleteClickListener(onDeleteClickListener onDeleteClickListener) {
        this.mDeleteClickListener = onDeleteClickListener;
    }

    public void setMomentHeadImageClickListener(onMomentHeadImageClickListener momentHeadImageClickListener) {
        this.mMomentHeadImageClickListener = momentHeadImageClickListener;
    }

    public void setNewMessageButtonClickListener(onNewMessageButtonClickListener newMessageButtonClickListener) {
        this.mNewMessageButtonClickListener = newMessageButtonClickListener;
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
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {

            int refresh = (int) payloads.get(0);
            switch (refresh) {
                case REFRESH_ANIM_VIEW:
                    ClassCircleViewHolder classCircleViewHolder = (ClassCircleViewHolder) holder;
                    classCircleViewHolder.mAnimLayout.setVisibility(View.INVISIBLE);
                    classCircleViewHolder.mAnimLayout.setEnabled(false);
                    break;
                case REFRESH_COMMENT_DATA:
                    ClassCircleViewHolder classCircleViewHolder1 = (ClassCircleViewHolder) holder;
                    ClassCircleResponse.Data.Moments moments = mData.get(position - 1);
                    setViewVisibly(classCircleViewHolder1, moments);
                    classCircleViewHolder1.mCircleCommentLayout.setData(moments.comments);
                    break;
                case REFRESH_LIKE_DATA:
                    ClassCircleViewHolder classCircleViewHolder2 = (ClassCircleViewHolder) holder;
                    ClassCircleResponse.Data.Moments moments2 = mData.get(position - 1);
                    setViewVisibly(classCircleViewHolder2, moments2);
                    classCircleViewHolder2.mCircleThumbView.setData(moments2.likes);
                    break;
                case SHOW_NEW_MESSAGE:
                    if (mNewMessageNumber > 0) {
                        final TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                        titleViewHolder.mRlNewMessage.setVisibility(View.VISIBLE);
                        titleViewHolder.mTvMessageNumber.setText(holder.itemView.getContext().getString(R.string.new_message, mNewMessageNumber));
                        titleViewHolder.mRlNewMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mNewMessageButtonClickListener != null) {
                                    mNewMessageButtonClickListener.newMessageButtonClick();
                                }
                            }
                        });
                    } else {
                        final TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                        titleViewHolder.mRlNewMessage.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }

        }
    }

    private void setViewVisibly(ClassCircleViewHolder classCircleViewHolder, ClassCircleResponse.Data.Moments moments) {
        boolean isLikeHasData = moments.likes != null && moments.likes.size() > 0;
        boolean isCommentHasData = moments.comments != null && moments.comments.size() > 0;
        boolean isSelf = isSelf(moments);
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
            classCircleViewHolder.mThumbView.setVisibility(View.VISIBLE);
            classCircleViewHolder.mThumbViewContent.setText("取消");
        } else {
            classCircleViewHolder.mThumbView.setVisibility(View.VISIBLE);
            classCircleViewHolder.mThumbViewContent.setText("赞");
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            final TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.mNameView.setText(UserInfo.getInstance().getInfo().getRealName());
            Glide.with(mContext).load(UserInfo.getInstance().getInfo().getAvatar()).asBitmap().placeholder(R.drawable.classcircle_headimg).centerCrop().into(new CornersImageTarget(mContext, titleViewHolder.mHeadImgView, 10));
            titleViewHolder.mHeadImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMomentHeadImageClickListener != null) {
                        mMomentHeadImageClickListener.myHeadClicked(titleViewHolder.getAdapterPosition(), String.valueOf(UserInfo.getInstance().getInfo().getUserId()));
                    }
                }
            });
            if (mNewMessageNumber == 0) {
                titleViewHolder.mRlNewMessage.setVisibility(View.GONE);
            } else {
                titleViewHolder.mRlNewMessage.setVisibility(View.VISIBLE);
                titleViewHolder.mTvMessageNumber.setText(holder.itemView.getContext().getString(R.string.new_message, mNewMessageNumber));
                titleViewHolder.mRlNewMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mNewMessageButtonClickListener != null) {
                            mNewMessageButtonClickListener.newMessageButtonClick();
                        }
                    }
                });
            }

        } else if (holder instanceof ClassCircleViewHolder) {
            final ClassCircleViewHolder classCircleViewHolder = (ClassCircleViewHolder) holder;
            final ClassCircleResponse.Data.Moments moments = mData.get(position - 1);

            String headimg = "";
            if (moments.publisher != null) {
                headimg = moments.publisher.avatar;
            }
            Glide.with(mContext).load(headimg).asBitmap().placeholder(R.drawable.icon_classcircle_headimg_small).centerCrop().into(new CornersImageTarget(mContext, classCircleViewHolder.mHeadImgView, 10));
            if (moments.album != null && moments.album.size() > 0) {
                classCircleViewHolder.mContentImageView.setVisibility(View.VISIBLE);
                ((NineGridView) classCircleViewHolder.mContentImageView).setViewOntouch(new NineGridViewWrapper.onViewOntouch() {
                    @Override
                    public void onViewTouch(MotionEvent event) {
                        if (animPosition != ANIM_POSITION_DEFAULT) {
                            notifyItemChanged(animPosition, REFRESH_ANIM_VIEW);
                            animPosition = ANIM_POSITION_DEFAULT;
                        }
                        if (mCommentClickListener != null) {
                            mCommentClickListener.commentFinish();
                        }
                    }
                });
                if (moments.album != null && moments.album.size() > 0) {
                    ArrayList<ImageInfo> imageInfos = new ArrayList<>();
                    for (int i = 0; i < moments.album.size(); i++) {
                        /*遍历album*/
                        ImageInfo imageInfo = new ImageInfo();
                        if (moments.album.size() > 1) {
                            //使用七牛对获取宽为200px的缩略图
                            if (moments.album.get(i).attachment != null) {
                                imageInfo.setThumbnailUrl(moments.album.get(i).attachment.resThumb + "?imageView2/0/w/200");
                            } else {
                                Log.e("error", "onBindViewHolder: attachment is null");
                            }
                        } else {
                            if (moments.album.get(i).attachment != null) {
                                imageInfo.setThumbnailUrl(moments.album.get(i).attachment.resThumb);
                            } else {
                                Log.e("error", "onBindViewHolder: attachment is null");
                            }
                        }

                        if (moments.album.get(i) != null && moments.album.get(i).attachment != null) {
                            imageInfo.setBigImageUrl(moments.album.get(i).attachment.previewUrl);
                        } else {
                            imageInfo.setBigImageUrl("");
                        }
                        imageInfos.add(imageInfo);
                    }
                    ((ClassCircleViewHolder) holder).mContentImageView.setAdapter(new NineGridViewClickAdapter(holder.itemView.getContext(), imageInfos));
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
                    moments.isShowAll = isShowAll;
                    if (mContentLinesChangedlistener != null) {
                        mContentLinesChangedlistener.onContentLinesChanged(classCircleViewHolder.getAdapterPosition(), isShowAll);
                    }
                }
            });
            classCircleViewHolder.mTimeView.setText(moments.publishTimeDesc);
            classCircleViewHolder.mAnimLayout.setVisibility(View.INVISIBLE);
            classCircleViewHolder.mAnimLayout.setEnabled(false);
            classCircleViewHolder.mCircleThumbView.setData(moments.likes);
            classCircleViewHolder.mCircleCommentLayout.setData(moments.comments);

            setViewVisibly(classCircleViewHolder, moments);

            classCircleViewHolder.mCircleCommentLayout.setItemClickListener(new ClassCircleCommentLayout.onItemClickListener() {
                @Override
                public void onItemClick(Comments comments, int position) {
                    if (mCommentClickListener != null) {
                        if (animPosition != ANIM_POSITION_DEFAULT) {
                            setViewAnim(classCircleViewHolder.getAdapterPosition(), classCircleViewHolder.mAnimLayout);
                        }
                        if (comments.publisher.userId.equals(String.valueOf(UserInfo.getInstance().getInfo().getUserId()))) {
                            mCommentClickListener.commentCancelClick(classCircleViewHolder.getAdapterPosition(), mData, position, comments);
                        } else {
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
                        notifyItemChanged(animPosition, REFRESH_ANIM_VIEW);
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
            classCircleViewHolder.mCommentDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDeleteClickListener != null) {
                        mDeleteClickListener.delete(classCircleViewHolder.getAdapterPosition(), mData);
                    }
                }
            });
            classCircleViewHolder.mCommentView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        if (checkIsThumb(moments.likes)) {
//                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_press_80);
//                        } else {
//                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_press_right);
//                        }
//                    } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
//                        if (checkIsThumb(moments.likes)) {
//                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal_80);
//                        } else {
//                            if (isSelf(moments)) {
//                                classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal);
//                            } else {
//                                classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal2);
//                            }
//                        }
//                    }

                    return false;
                }
            });
            classCircleViewHolder.mThumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLikeClickListener != null) {
                        if (checkIsThumb(moments.likes)) {
                            mLikeClickListener.cancelLikeClick(classCircleViewHolder.getAdapterPosition(), moments);
                        } else {
                            mLikeClickListener.likeClick(classCircleViewHolder.getAdapterPosition(), moments);
                        }
                    }
                    classCircleViewHolder.mAnimLayout.setVisibility(View.INVISIBLE);
                    classCircleViewHolder.mAnimLayout.setEnabled(false);
                    animPosition = ANIM_POSITION_DEFAULT;
                }
            });
            classCircleViewHolder.mThumbView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_press_left);
//                    } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
//                        if (isSelf(moments)) {
//                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal);
//                        } else {
//                            classCircleViewHolder.mAnimLayout.setBackgroundResource(R.drawable.shape_class_circle_aime_normal2);
//                        }
//                    }
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

    private boolean isSelf(ClassCircleResponse.Data.Moments moments) {
        if (moments == null) {
            return false;
        }
        if (moments.publisher != null && !TextUtils.isEmpty(moments.publisher.userId)
                && moments.publisher.userId.equals(String.valueOf(UserInfo.getInstance().getInfo().getUserId()))) {
            return true;
        } else {
            return false;
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
        mLikeClickListener.momentPosition(position);
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
        NineGridView mContentImageView;
        @BindView(R.id.cc_thumb)
        ClassCircleThumbView mCircleThumbView;
        @BindView(R.id.cc_comments)
        ClassCircleCommentLayout mCircleCommentLayout;
        @BindView(R.id.ll_anim)
        LinearLayout mAnimLayout;
        @BindView(R.id.tv_thumb)
        RelativeLayout mThumbView;
        @BindView(R.id.tv_thumb_content)
        TextView mThumbViewContent;
        @BindView(R.id.tv_comment)
        RelativeLayout mCommentView;
        @BindView(R.id.like_comment_line)
        View mLikeCommentLineView;
        @BindView(R.id.ll_like_comment)
        LinearLayout mLikeCommentLayout;
        @BindView(R.id.tv_delete)
        RelativeLayout mCommentDel;

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
        @BindView(R.id.rl_new_message)
        RelativeLayout mRlNewMessage;
        @BindView(R.id.tv_message_number)
        TextView mTvMessageNumber;

        TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
