package com.test.yanxiu.im_ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.util.Utils;
import com.test.yanxiu.common_base.utils.EscapeCharacterUtils;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_ui.callback.OnRecyclerViewItemClickCallback;
import com.test.yanxiu.im_ui.view.CircleView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by cailei on 09/03/2018.
 */

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicViewHolder> {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private List<DbTopic> mDatas;
    private OnRecyclerViewItemClickCallback mOnItemClickCallback;

    public void setmOnItemClickCallback(OnRecyclerViewItemClickCallback mOnItemClickCallback) {
        this.mOnItemClickCallback = mOnItemClickCallback;
    }

    TopicListAdapter(Context context, List<DbTopic> topics) {
        mContext = context;
        mDatas = topics;
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(mContext, v);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, final int position) {
        final DbTopic topic = mDatas.get(position);
        // 被提了bug
//        if (topic.latestMsgId == 0){
//            setVisibile(false,holder.itemView);
//        }else {
//            setVisibile(true,holder.itemView);
//        }
        setVisibile(true, holder.itemView);
        holder.setData(topic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickCallback != null) {
                    mOnItemClickCallback.onItemClick(position, topic);
                }
            }
        });
    }

    public void setVisibile(boolean isVisible, View view) {
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (isVisible) {
            param.height = Utils.dp2px(mContext, 71);// 这里注意使用自己布局的根布局类型
            param.width = RelativeLayout.LayoutParams.MATCH_PARENT;// 这里注意使用自己布局的根布局类型
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
        view.setLayoutParams(param);
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;

        private ImageView mAvatarImageView;
        private TextView mSenderTextView;
        private TextView mTimeTextView;
        private TextView mMsgTextView;
        private CircleView mRedDotCircleView;

        public TopicViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;

            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            mSenderTextView = itemView.findViewById(R.id.sender_textview);
            mTimeTextView = itemView.findViewById(R.id.time_textView);
            mMsgTextView = itemView.findViewById(R.id.msg_textview);
            mRedDotCircleView = itemView.findViewById(R.id.reddot_circleview);
        }

        public void setData(final DbTopic topic) {
            // 默认
            mAvatarImageView.setImageResource(R.drawable.icon_chat_class);
            mSenderTextView.setText("");
            mTimeTextView.setText("");
            mMsgTextView.setText("");
            mRedDotCircleView.setVisibility(View.INVISIBLE);
            List<DbMember> members = topic.getMembers();
//            //如果没有member信息return
//            if ((members == null) || (members.size() == 0)) {
//                // 尚且没有member信息，全部用默认
//                return;
//            }

            DbMsg latestMsg = null;
            if (topic.mergedMsgs.size() > 0) {
                latestMsg = topic.mergedMsgs.get(0);
            }
            //群聊与私聊的不同
            //判断 聊天类型
            Log.i(TAG, "setData: ");
            if (topic.getType().equals("1")) {
                //私聊 显示对方头像 和 topic 名称
                mAvatarImageView.setImageResource(R.drawable.im_chat_default);
                if (members != null) {
                    for (DbMember member : topic.getMembers()) {
                        if (member.getImId() != Constants.imId) {
                            Glide.with(mContext)
                                    .load(member.getAvatar())
                                    .placeholder(R.drawable.im_chat_default)
                                    .into(mAvatarImageView);
                            mSenderTextView.setText(EscapeCharacterUtils.unescape(member.getName()));
                            break;
                        }
                    }
                }
            } else if (topic.getType().equals("2")) {
                //群聊
                // 1, 显示班级默认图片
                mAvatarImageView.setImageResource(R.drawable.icon_chat_class);
                // 2, 显示班级群聊(班级名) 在消息判断外侧，保证列表 显示群组名称
                mSenderTextView.setText("班级群聊" + "(" + EscapeCharacterUtils.unescape(topic.getGroup()) + ")");
            }

            //显示最新消息
            if (latestMsg == null) {
                //topic 最新消息为空 最新消息显示空
                return;
            }
            //1 判断本地消息
            DbMsg localMsg = DatabaseDealer.getLatestMyMsgByMsgId(latestMsg.getMsgId());
            if (localMsg != null) {
                latestMsg = localMsg;
            }
            //2 获取最后一条信息的发送者信息
            DbMember sender = null;
            //member列表中查找
            if (members != null) {
                for (DbMember member : topic.getMembers()) {
                    if (latestMsg.getSenderId() == member.getImId()) {
                        sender = member;
                    }
                }
            }
            // 本地用户数据库
            if (sender == null) {
                sender = DatabaseDealer.getMemberById(latestMsg.getSenderId());
            }

            //显示最有一条信息的发送者
            StringBuilder latestMsgTxt = new StringBuilder();

            //群聊时最后一条 要显示发送者的名字
            if (topic.getType().equals("2")) {
                latestMsgTxt.append(sender == null ? "" : EscapeCharacterUtils.unescape(sender.getName()));
                latestMsgTxt.append(":");
            }


            //3 确定消息内容
            if (latestMsg.getContentType()==10) {
                //七牛传过来的图片类型 图片url 不为空 并且 返回的内容文字为 qiniu
                boolean isImageMsg= (!TextUtils.isEmpty(latestMsg.getLocalViewUrl())||!TextUtils.isEmpty(latestMsg.getViewUrl()))
                        &&TextUtils.equals("qiniu",latestMsg.getMsg());
                if (isImageMsg) {
                    latestMsgTxt.append("[图片]");
                }else {
                    latestMsgTxt.append(EscapeCharacterUtils.unescape(latestMsg.getMsg()));
                }

            }else if (latestMsg.getContentType()==20){
                //图片信息 本地消息可以用 contenttype 判断图片
                latestMsgTxt.append("[图片]");
            }else {
                // TODO: 2018/4/11  预留 语音 视频
            }

            mMsgTextView.setText(latestMsgTxt.toString());
            //4 显示消息时间
            mTimeTextView.setText(timeStr(latestMsg.getSendTime()));
            //显示红点
            if (topic.isShowDot()&&topic.latestMsgId!=-1) {
                mRedDotCircleView.setVisibility(View.VISIBLE);
            }
        }

        private String timeStr(long timestamp) {
            String ret = null;

            Date now = new Date();
            Date date = new Date(timestamp);


            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
            String nowStr = formatter.format(now);
            String dateStr = formatter.format(date);


            // 由于server time可能有误差，所有未来时间也当做今天
            if ((nowStr.equals(dateStr)) || (date.getTime() > now.getTime())) {
                // 在同一天，显示"上午 10:36"
                SimpleDateFormat dateFormat = new SimpleDateFormat("a hh:mm", Locale.CHINA);
                ret = dateFormat.format(date);
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
                ret = "昨天";
                return ret;
            }
            //如果日期小于6天 显示星期
            if ((nowZero.getTime()-date.getTime())<6*24*60*60*1000){
                // 星期三 周三->星期三
                SimpleDateFormat formatter2 = new SimpleDateFormat("EEEE ", Locale.CHINA);
                ret = formatter2.format(date);
                return ret;
            }

            //时间早于6天  显示具体日期
            SimpleDateFormat format=new SimpleDateFormat("MM月dd日",Locale.CHINA);
            ret=format.format(date);
            return ret;
        }
    }
}
