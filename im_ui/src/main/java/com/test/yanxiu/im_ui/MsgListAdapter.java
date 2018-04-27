package com.test.yanxiu.im_ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.test.yanxiu.common_base.utils.EscapeCharacterUtils;
import com.test.yanxiu.common_base.utils.ScreenUtils;
import com.test.yanxiu.common_base.utils.SharedSingleton;
import com.test.yanxiu.common_base.utils.talkingdata.EventUpdate;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbMyMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_core.http.common.ImTopic;
import com.test.yanxiu.im_ui.callback.OnRecyclerViewItemClickCallback;
import com.test.yanxiu.im_ui.view.ProgressImageContainer;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by cailei on 14/03/2018.
 */

public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.MsgListItemViewHolder> {
    private final String TAG = getClass().getSimpleName();

    enum ItemType {
        LOADING,
        DATETIME,
        DBMSG,
        DBMYMSG,

        BOTTOM
    }

    private Context mContext;
    private List<DbMsg> mDatas;
    private List<Item> mUiDatas;
    private DbTopic topic;
    /**
     * 当前群成员列表 不包含聊天记录中有但是被删除的成员
     * 用于判断 点击头像进行私聊的判断
     */
    private List<ImTopic.Member> remainMemberList;
    /**
     * 占位图缓存
     * */
    private LruCache<String,Bitmap> placeHolderBitmapCache=new LruCache<>(10);


    public void setRemainMemberList(List<ImTopic.Member> remainMemberList) {
        this.remainMemberList = remainMemberList;
    }

    public DbTopic getTopic() {
        return topic;
    }

    /**
     * 统一数据源入口 防止 topic 与 datalist 指向不同的列表
     */
    public void setTopic(DbTopic topic) {
        if (topic == null) {
            setmDatas(new ArrayList<DbMsg>());
            return;
        }
        this.topic = topic;
        setmDatas(topic.mergedMsgs);

    }

    public MsgListAdapter(Context context) {
        mContext = context;
    }

    private OnRecyclerViewItemClickCallback mOnItemClickCallback;

    public void setmOnItemClickCallback(OnRecyclerViewItemClickCallback mOnItemClickCallback) {
        this.mOnItemClickCallback = mOnItemClickCallback;
    }

    private boolean isLoading;
    private Item loadingItem = new Item(ItemType.LOADING);

    public void setIsLoading(boolean loading) {
        if (isLoading == loading) {
            return;
        }

        if (loading) {
            mUiDatas.add(0, loadingItem);
        } else {
            mUiDatas.remove(loadingItem);
        }

        isLoading = loading;
    }


    //统一数据入口
    private void setmDatas(List<DbMsg> mDatas) {
        this.mDatas = mDatas;
        // 重新生成用于显示的mUiDatas
        generateUiDatas();
        //notifyDataSetChanged();
    }

    // 从现有的mDatas，生成mUiDatas
    private void generateUiDatas() {
        mUiDatas = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            DbMsg curDbMsg = mDatas.get(i);
            // 最后一条跟当前时间比较，其余的跟前一条时间比较
            long nextSendTime = Long.MAX_VALUE;
            if (i != (mDatas.size() - 1)) {
                DbMsg nextDbMsg = mDatas.get(i + 1);
                nextSendTime = nextDbMsg.getSendTime();
            }

            // 当前msg入队
            Item msgItem = new Item();
            if (curDbMsg.getSenderId() == Constants.imId) {
                msgItem.setType(ItemType.DBMYMSG);
                msgItem.setMyMsg((DbMyMsg) curDbMsg);
            } else {
                msgItem.setType(ItemType.DBMSG);
                msgItem.setMsg(curDbMsg);
            }
            mUiDatas.add(0, msgItem);

            // 如果超过5分钟，则插入时间
            if ((curDbMsg.getSendTime() - nextSendTime) > 5 * 60 * 1000) {
                Item timeItem = new Item();
                timeItem.setType(ItemType.DATETIME);
                timeItem.setTimestamp(curDbMsg.getSendTime());
                mUiDatas.add(0, timeItem);
            }

        }

        // 最后一条消息插入时间
        if ((mDatas != null) && (mDatas.size() > 0)) {
            Item timeItem = new Item();
            timeItem.setType(ItemType.DATETIME);
            timeItem.setTimestamp(mDatas.get(mDatas.size() - 1).getSendTime());

            mUiDatas.add(0, timeItem);
        }

        // 底下留白
        Item bottomItem = new Item();
        bottomItem.setType(ItemType.BOTTOM);
        mUiDatas.add(bottomItem);
    }


    // 加载更多时需要滚动到相应的位置
    public int uiAddedNumberForMsg(DbMsg msg) {
        int position = 0;
        for (Item uiItem : mUiDatas) {
            if ((uiItem.getMsg() == msg) || (uiItem.getMyMsg() == msg)) {
                position = mUiDatas.indexOf(uiItem);
                break;
            }
        }


        return position == 1 ? 0 : position; // 因为为最初的一个消息时，本来之前就插入了一个Datetime
    }

    /**
     * 获取当前数据对应RecyclerView中的positon
     *
     * @param msg
     * @return
     */
    public int getCurrentDbMsgPosition(DbMsg msg) {
        int position = 0;
        for (Item uiItem : mUiDatas) {
            if ((uiItem.getMsg() == msg) || (uiItem.getMyMsg() == msg)) {
                position = mUiDatas.indexOf(uiItem);
                break;
            }
        }
        if (isLoading) {
            position++;
        }
        return position;

    }


    @Override
    public int getItemViewType(int position) {
        Item item = mUiDatas.get(position);
        return item.type.ordinal();
    }

    @Override
    public MsgListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemType.LOADING.ordinal()) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_msg_loading, parent, false);
            return new LoadingViewHolder(v);
        }

        if (viewType == ItemType.DATETIME.ordinal()) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_msg_datetime, parent, false);
            return new DatetimeViewHolder(v);
        }

        if (viewType == ItemType.DBMSG.ordinal()) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_msg_dbmsg, parent, false);
            return new MsgViewHolder(v);
        }

        if (viewType == ItemType.DBMYMSG.ordinal()) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_msg_dbmymsg, parent, false);
            return new MyMsgViewHolder(v);
        }

        if (viewType == ItemType.BOTTOM.ordinal()) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_padding_layout, parent, false);
            return new BottomViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MsgListItemViewHolder holder, final int position) {
        final Item item = mUiDatas.get(position);

        holder.setData(item);


        if (holder instanceof MyMsgViewHolder) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickCallback != null) {
                        mOnItemClickCallback.onItemClick(position, item.getMyMsg());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mUiDatas.size();
    }

    public abstract class MsgListItemViewHolder extends RecyclerView.ViewHolder {
        public MsgListItemViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void setData(Item item);
    }

    public class BottomViewHolder extends MsgListItemViewHolder {
        public BottomViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Item item) {

        }
    }

    public class LoadingViewHolder extends MsgListItemViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Item item) {

        }
    }

    public class DatetimeViewHolder extends MsgListItemViewHolder {
        private TextView mDatetimeTextView;

        public DatetimeViewHolder(View itemView) {
            super(itemView);
            mDatetimeTextView = itemView.findViewById(R.id.datetime_textview);
        }

        @Override
        public void setData(Item item) {
            mDatetimeTextView.setText(timeStr(item.timestamp));
        }

        private String timeStr(long timestamp) {
            String ret = null;

            Date now = new Date();
            Date date = new Date(timestamp);


            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
            String nowStr = formatter.format(now);
            String dateStr = formatter.format(date);

            SimpleDateFormat timeFormatter = new SimpleDateFormat("a hh:mm", Locale.CHINA);
            ret = timeFormatter.format(date);

            // 由于server time可能有误差，所有未来时间也当做今天
            if ((nowStr.equals(dateStr)) || (date.getTime() > now.getTime())) {
                // 在同一天，显示"上午 10:36"
                ret = timeFormatter.format(date);
                return ret;
            }

            Date nowZero = null; // 今天零点
            try {
                nowZero = formatter.parse(nowStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((nowZero.getTime() - date.getTime()) < 24 * 60 * 60 * 1000) {
                // 昨天
                ret = "昨天 " + timeFormatter.format(date);
                return ret;
            }

            //如果日期小于6天 显示星期
            if ((nowZero.getTime() - date.getTime()) < 6 * 24 * 60 * 60 * 1000) {
                // 星期三 周三->星期三
                SimpleDateFormat formatter2 = new SimpleDateFormat("EEEE", Locale.CHINA);
                ret = formatter2.format(date) + " " + timeFormatter.format(date);
                return ret;
            }
            //时间早于6天  显示具体日期
            SimpleDateFormat format = new SimpleDateFormat("MM月dd日", Locale.CHINA);
            ret = format.format(date);
            return ret;
        }
    }

    public class MsgViewHolder extends MsgListItemViewHolder {
        private ImageView mAvatarImageView;
        private TextView mNameTextView;
        private TextView mMsgTextView;
        private ProgressImageContainer mMsgImageView;
//        private RoundCornerMaskView mMsgImageViewRound;

        public MsgViewHolder(View itemView) {
            super(itemView);
            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            mNameTextView = itemView.findViewById(R.id.name_textview);
            mMsgTextView = itemView.findViewById(R.id.msg_textview);
            mMsgImageView = itemView.findViewById(R.id.msg_imageView);
//            mMsgImageViewRound = itemView.findViewById(R.id.msg_imageView_round);
            mMsgTextView.setTextIsSelectable(true);
        }

        @Override
        public void setData(Item item) {
            final DbMsg msg = item.getMsg();
            mMsgImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showPreviewPicListener != null) {
                        showPreviewPicListener.picClick(getAdapterPosition(), mMsgImageView, msg.getViewUrl());
                    }
                }
            });
            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            mNameTextView.setText("");
            final DbMember sender = DatabaseDealer.getMemberById(msg.getSenderId());
            if (sender != null) {
                Glide.with(mContext)
                        .load(sender.getAvatar())
                        .placeholder(R.drawable.icon_chat_unknown)
                        .error(R.drawable.icon_chat_unknown)
                        .into(mAvatarImageView);
                //对用户名字进行转义处理
                mNameTextView.setText(EscapeCharacterUtils.unescape(sender.getName()));
            } else {
                Glide.with(mContext)
                        .load(R.drawable.im_chat_default)
                        .into(mAvatarImageView);
                mNameTextView.setText("");
            }

            if (msg.getContentType() == 20) {
                mMsgTextView.setVisibility(View.GONE);
                mMsgImageView.setVisibility(View.VISIBLE);
                mMsgImageView.clearOverLayer();
                final Integer[] wh = getPicShowWH(itemView.getContext(), msg.getWith(), msg.getHeight());
                //接收的消息 目前没有本地缓存 首先展示占位图
//                mMsgImageView.setImageResource(R.drawable.bg_im_pic_holder_view);

                mMsgImageView.setTag(msg.getViewUrl());

               Bitmap holdBitmap = placeHolderBitmapCache.get(wh[0]+"*"+wh[1]);
                if (holdBitmap==null) {
                    //如果没有缓存 生成占位图
                    Bitmap bitmap = BitmapFactory.decodeResource(itemView.getResources(), R.drawable.bg_im_pic_holder_view);
                    holdBitmap = Bitmap.createScaledBitmap(bitmap, wh[0], wh[1], true);
                    //加入缓存
                    placeHolderBitmapCache.put(wh[0]+"*"+wh[1],holdBitmap);
                }
                mMsgImageView.setImageBitmap(holdBitmap);

                //执行加载图片
                final Bitmap finalHoldBitmap = holdBitmap;
                Glide.with(itemView.getContext())
                        .load(msg.getViewUrl())
                        .asBitmap()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(new SimpleTarget<Bitmap>(wh[0], wh[1]) {
                            @Override
                            public void onStart() {
                                super.onStart();
                                mMsgImageView.clearOverLayer();
                                Log.i(TAG, "glide load onStart: ");
                            }

                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                if (SharedSingleton.getInstance().get(msg.getViewUrl()) == null) {
                                    SharedSingleton.getInstance().cacheBitmap(msg.getViewUrl(),resource);
                                }
                                if (TextUtils.equals(msg.getViewUrl(), (CharSequence) mMsgImageView.getTag())) {
                                    mMsgImageView.setImageBitmap(resource);
                                }
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                mMsgImageView.setImageBitmap(finalHoldBitmap);
                            }
                        });


            } else if (msg.getContentType() == 30) {
                mMsgTextView.setVisibility(View.GONE);

            } else {
                mMsgTextView.setVisibility(View.VISIBLE);
                mMsgImageView.setVisibility(View.GONE);
                //对聊天内容中的转义字符进行处理
                mMsgTextView.setText(EscapeCharacterUtils.unescape(msg.getMsg()));
            }

            mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //事件统计 点击群聊 头像
                    if (mContext != null) {
                        EventUpdate.onClickGroupAvatarEvent(mContext);
                    }

                    if ((topic != null) && (topic.getType().equals("2"))) {
                        // 群聊点击头像


                        //判断成员是否依然在群组中  
                        // TODO: 2018/4/8 这里出现了sender 为空的情况 原因暂未查明
                        if (sender != null && isRemainMember(sender.getImId())) {
                            sender.fromTopicId = topic.getTopicId();
                            EventBus.getDefault().post(sender);
                        } else {
                            if (mContext != null) {
                                Toast.makeText(mContext, "【已被移出此班】", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            });
        }
    }

    /**
     * 通过imId 判断被点击头像的成员是否还在群组中
     */
    private boolean isRemainMember(long imId) {
        if (remainMemberList != null) {
            for (ImTopic.Member member : remainMemberList) {
                if (member.memberInfo.imId == imId) {
                    return true;
                }
            }
        }
        return false;
    }

    public class MyMsgViewHolder extends MsgListItemViewHolder {
        private ImageView mAvatarImageView;
        private TextView mMsgTextView;
        private ProgressBar mStateSendingProgressBar;
        private ImageView mStateFailedImageView;
        private ProgressImageContainer mMsgImageView;
        private DbMyMsg myMsg;
//        private ImageView mImgGlide;

        public MyMsgViewHolder(View itemView) {
            super(itemView);
            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            mMsgTextView = itemView.findViewById(R.id.msg_textview);
            mMsgTextView.setTextIsSelectable(true);
            mStateSendingProgressBar = itemView.findViewById(R.id.state_sending_progressbar);
            mMsgImageView = itemView.findViewById(R.id.msg_imageView);
            mStateFailedImageView = itemView.findViewById(R.id.state_fail_imageview);
//            mImgGlide =itemView.findViewById(R.id.img_glide);
        }

        @Override
        public void setData(final Item item) {
            myMsg = item.getMyMsg();

            mMsgImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showPreviewPicListener != null) {
                        String url;
                        if (TextUtils.isEmpty(myMsg.getLocalViewUrl())) {
                            url = myMsg.getViewUrl();
                        } else {
                            url = myMsg.getLocalViewUrl();
                        }
                        showPreviewPicListener.picClick(getAdapterPosition(), mMsgImageView, url);
                    }
                }
            });


            // 设置头像
            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            DbMember sender = DatabaseDealer.getMemberById(Constants.imId);
            if (sender != null) {
                Glide.with(mContext)
                        .load(sender.getAvatar())
                        .placeholder(R.drawable.icon_chat_unknown)
                        .error(R.drawable.icon_chat_unknown)
                        .into(mAvatarImageView);

            }

            // 设置消息内容
            if (myMsg.getContentType() == 20) {

                final Integer[] wh = getPicShowWH(itemView.getContext(), myMsg.getWith(), myMsg.getHeight());

                mMsgTextView.setVisibility(View.GONE);
                mMsgImageView.setVisibility(View.VISIBLE);

                Bitmap holdBitmap = placeHolderBitmapCache.get(wh[0]+"*"+wh[1]);
                if (holdBitmap==null) {
                    //如果没有缓存 生成占位图
                    Bitmap bitmap = BitmapFactory.decodeResource(itemView.getResources(), R.drawable.bg_im_pic_holder_view);
                    holdBitmap = Bitmap.createScaledBitmap(bitmap, wh[0], wh[1], true);
                    //加入缓存
                    placeHolderBitmapCache.put(wh[0]+"*"+wh[1],holdBitmap);
                }
                mMsgImageView.setImageBitmap(holdBitmap);

                final String picUrl;
                //如果有本地地址则用本地  没有本地的将使用线上的
                if (!TextUtils.isEmpty(myMsg.getLocalViewUrl())) {
                    picUrl = myMsg.getLocalViewUrl();
                } else {
                    picUrl = myMsg.getViewUrl();
                }


                mMsgImageView.setTag(picUrl);
                final Bitmap finalHoldBitmap = holdBitmap;
                final Bitmap finalHoldBitmap1 = holdBitmap;
                Glide.with(itemView.getContext())
                        .load(picUrl)
                        .asBitmap()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(new SimpleTarget<Bitmap>(wh[0], wh[1]) {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                if (TextUtils.equals(picUrl, (CharSequence) mMsgImageView.getTag())) {
                                    mMsgImageView.clearOverLayer();
//                                    if (mMsgImageView.getBitmap() == null) {
//                                        Bitmap bitmap = BitmapFactory.decodeResource(itemView.getResources(), R.drawable.bg_im_pic_holder_view);
//                                        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, wh[0], wh[1], true);
//                                        mMsgImageView.setImageBitmap(bitmap1);
//                                    } else {
////                                        mMsgImageView.setImageBitmap(mMsgImageView.getBitmap());
//                                    }
                                }
                            }

                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                if (SharedSingleton.getInstance().get(picUrl) == null) {
                                    SharedSingleton.getInstance().cacheBitmap(picUrl,resource);
                                }
                                if (TextUtils.equals(picUrl, (CharSequence) mMsgImageView.getTag())) {
//                                    mMsgImageView.clearOverLayer();
                                    if (resource != null) {
                                        mMsgImageView.setImageBitmap(resource);
                                    } else {
//                                        Bitmap bitmap = BitmapFactory.decodeResource(itemView.getResources(), R.drawable.bg_im_pic_holder_view);
//                                        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, wh[0], wh[1], true);
                                        mMsgImageView.setImageBitmap(finalHoldBitmap);
                                    }
                                }
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                mMsgImageView.setImageBitmap(finalHoldBitmap1);
                            }
                        });

            } else if (myMsg.getContentType() == 30) {
                mMsgTextView.setVisibility(View.GONE);

            } else {
                mMsgTextView.setVisibility(View.VISIBLE);
                mMsgImageView.setVisibility(View.GONE);

                mMsgTextView.setText(myMsg.getMsg());
            }


            if (myMsg.getState() == DbMyMsg.State.Sending.ordinal()) {
                mStateSendingProgressBar.setVisibility(View.VISIBLE);
                mStateFailedImageView.setVisibility(View.GONE);
            }

            if (myMsg.getState() == DbMyMsg.State.Failed.ordinal()) {
                mStateSendingProgressBar.setVisibility(View.GONE);
                mStateFailedImageView.setVisibility(View.VISIBLE);
            }
            if (myMsg.getState() == DbMyMsg.State.Success.ordinal()) {
                mStateSendingProgressBar.setVisibility(View.GONE);
                mStateFailedImageView.setVisibility(View.GONE);
            }
        }
    }

    public class Item {
        private ItemType type;   // 0-loading, 1-datetime, 2-dbmsg, 3-dbmymsg
        private DbMsg msg;
        private DbMyMsg myMsg;
        private long timestamp;

        public Item() {

        }

        public Item(ItemType t) {
            type = t;
        }

        public ItemType getType() {
            return type;
        }

        public void setType(ItemType type) {
            this.type = type;
        }

        public DbMsg getMsg() {
            return msg;
        }

        public void setMsg(DbMsg msg) {
            this.msg = msg;
        }

        public DbMyMsg getMyMsg() {
            return myMsg;
        }

        public void setMyMsg(DbMyMsg myMsg) {
            this.myMsg = myMsg;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }


    /**
     * 获取图片应该显示的长和宽
     *
     * @param width  真实宽度
     * @param height 真实长度
     * @return
     */
    private Integer[] getPicShowWH(Context context, int width, int height) {
        float baseSize = ScreenUtils.dpToPx(context, 140);
        float iResultWidth = baseSize;
        float iResultHeight = baseSize;

        //水平显示
        if (width > height) {
            float scaleSize = baseSize / width;
            iResultWidth = baseSize;
            iResultHeight = height * scaleSize;
        }
        //垂直显示
        if (width < height) {
            float scaleSize = baseSize / height;
            iResultHeight = baseSize;
            iResultWidth = width * scaleSize;
        }

        if (iResultHeight < baseSize / 2) {
            iResultHeight = baseSize / 2;
        }
        if (iResultWidth < baseSize / 2) {
            iResultWidth = baseSize / 2;
        }


        return new Integer[]{(int) iResultWidth, (int) iResultHeight};
    }

    /**
     * 获取图片应该显示的长和宽
     *
     * @param width  真实宽度
     * @param height 真实长度
     * @return
     */

    private float getPicScaleSize(Context context, int width, int height) {
        float baseSize = ScreenUtils.dpToPx(context, 140);
        float scaleSize = 0;
        if (width <= 0) {
            return 0;
        }
        if (height <= 0) {
            return 0;
        }
        //水平显示
        if (width > height) {
            scaleSize = baseSize / width;
        }
        //垂直显示
        if (width < height) {
            scaleSize = baseSize / height;
        }

        return scaleSize;

    }

    @Override
    public void onBindViewHolder(MsgListItemViewHolder holder, int position, List<Object> payloads) {

        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Object payload = payloads.get(0);
            if (payload instanceof PayLoad) {

                switch (((PayLoad) payload).type) {
                    case PayLoad.CHANG_SEND_PROGRESS:
                        ((MyMsgViewHolder) holder).mMsgImageView.addOverLayer();
                        int progress = (int) (((Double) ((PayLoad) payload).data) * 100);
                        ((MyMsgViewHolder) holder).mMsgImageView.setProgress(progress);
                        break;
                    case PayLoad.CHANG_SEND_STATUE:
                        MyMsgViewHolder myMsgViewHolder = (MyMsgViewHolder) holder;
                        if (myMsgViewHolder.myMsg.getState() == DbMyMsg.State.Sending.ordinal()) {
                            myMsgViewHolder.mStateSendingProgressBar.setVisibility(View.VISIBLE);
                            myMsgViewHolder.mStateFailedImageView.setVisibility(View.GONE);
                        }

                        if (myMsgViewHolder.myMsg.getState() == DbMyMsg.State.Failed.ordinal()) {
                            ((MyMsgViewHolder) holder).mMsgImageView.setProgress(100);
                            myMsgViewHolder.mStateSendingProgressBar.setVisibility(View.GONE);
                            myMsgViewHolder.mStateFailedImageView.setVisibility(View.VISIBLE);
                            ((MyMsgViewHolder) holder).mMsgImageView.clearOverLayer();
                        }
                        if (myMsgViewHolder.myMsg.getState() == DbMyMsg.State.Success.ordinal()) {
                            ((MyMsgViewHolder) holder).mMsgImageView.setProgress(100);
                            myMsgViewHolder.mStateSendingProgressBar.setVisibility(View.GONE);
                            myMsgViewHolder.mStateFailedImageView.setVisibility(View.GONE);
                            ((MyMsgViewHolder) holder).mMsgImageView.clearOverLayer();
                        }
                        break;
                }
            }


        }
    }

    /**
     * 计算图片的宽高
     *
     * @param imgPath 图片路径
     * @return Integer【】 第一个参数表示width 第二个参数表示height
     */
    private Integer[] getPicWithAndHeight(String imgPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        return new Integer[]{options.outWidth, options.outHeight};
    }

    public static class PayLoad {
        static final int CHANG_SEND_STATUE = 0x01;
        static final int CHANG_SEND_PROGRESS = 0x02;


        int type;
        Object data;

        public PayLoad(int type) {
            this.type = type;
        }

        public PayLoad(int type, Object data) {
            this.type = type;
            this.data = data;
        }
    }

    private ShowPreviewPicListener showPreviewPicListener;

    public void setShowPreviewPicListener(ShowPreviewPicListener showPreviewPicListener) {
        this.showPreviewPicListener = showPreviewPicListener;
    }

    public interface ShowPreviewPicListener {
        void picClick(int position, View view, String viewUrl);
    }


//    @Override
//    public long getItemId(int position) {
//        return position;
//    }


}
