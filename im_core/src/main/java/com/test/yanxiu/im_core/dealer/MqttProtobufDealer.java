package com.test.yanxiu.im_core.dealer;

import android.os.Looper;
import android.util.Log;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.http.common.ImMsg;
import com.test.yanxiu.im_core.protobuf.ImMqttProto;
import com.test.yanxiu.im_core.protobuf.MqttMsgProto;
import com.test.yanxiu.im_core.protobuf.TopicGetProto;
import com.test.yanxiu.im_core.protobuf.TopicMsgProto;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by cailei on 05/03/2018.
 */

public class MqttProtobufDealer {
    public enum TopicChange {
        AddTo,
        RemoveFrom
    }

    public static class NewMsgEvent {
        public ImMsg msg;
    }

    public static class TopicChangeEvent {
        public long topicId;
        public TopicChange type;
    }

    public static class TopicUpdateEvent {
        public long topicId;
        public List<DbMsg> newMsgs;
    }

    public static void dealWithData(byte[] rawData) throws InvalidProtocolBufferException {
        MqttMsgProto.MqttMsg mqttMsg = MqttMsgProto.MqttMsg.parseFrom(rawData);

        //目前只有一种type，且type为""
        //if (mqttMsg.getType() == "xxx") {
        // 是im的消息
        ImMqttProto.ImMqtt imMqtt = ImMqttProto.ImMqtt.parseFrom(mqttMsg.getData());
        if ((imMqtt.getImEvent() == 101)            // 请求主题数据（client通过topicId向server请求主题全部数据）
                || (imMqtt.getImEvent() == 111)     // 主题添加新成员，同101事件（client通过topicId向server请求主题全部数据）
                || (imMqtt.getImEvent() == 112))    // 主题删除成员，同101事件（client通过topicId向server请求主题全部数据）
        {
            for (ByteString item : imMqtt.getBodyList()) {
                TopicGetProto.TopicGet topicProto = TopicGetProto.TopicGet.parseFrom(item);
                long topicId = topicProto.getTopicId();
                // EventBus发现topic更新
                if (imMqtt.getImEvent() == 112) {
                    onTopicChange(topicId, TopicChange.RemoveFrom);
                } else {
                    onTopicChange(topicId, TopicChange.AddTo);
                }
            }
        }

        if (imMqtt.getImEvent() == 121)         // 下发主题聊天消息
        {
            for (ByteString item : imMqtt.getBodyList()) {
                TopicMsgProto.TopicMsg msgProto = TopicMsgProto.TopicMsg.parseFrom(item);
                ImMsg msg = new ImMsg();
                msg.reqId = msgProto.getReqId();
                msg.msgId = msgProto.getId();
                msg.topicId = msgProto.getTopicId();
                msg.senderId = msgProto.getSenderId();
                msg.contentType = msgProto.getContentType();
                msg.sendTime = msgProto.getSendTime();
                msg.contentData = new ImMsg.ContentData();
                msg.contentData.msg = msgProto.getContentData().getMsg();
                msg.contentData.viewUrl = msgProto.getContentData().getViewUrl();
                Log.e("frc","msgProto  width:"+msgProto.getContentData().getWidth());
                Log.e("frc","msgProto  height:"+msgProto.getContentData().getHeight());

                msg.contentData.width = msgProto.getContentData().getWidth();
                msg.contentData.height = msgProto.getContentData().getHeight();

                // EventBus发现topic更新

                onNewMsg(msg);
            }
        }
        //}
    }

    public static void updateDbWithNewMsg(ImMsg msg) {

    }

    public static void onNewMsg(ImMsg msg) {
        if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
            Log.d("Tag", "main thread");
        } else { // 非UI主线程
            Log.d("Tag", "other thread");
        }

        NewMsgEvent event = new NewMsgEvent();
        event.msg = msg;
        EventBus.getDefault().post(event);
    }

    public static void onTopicChange(long topicId, TopicChange type) {
        TopicChangeEvent event = new TopicChangeEvent();
        event.topicId = topicId;
        event.type = type;
        EventBus.getDefault().post(event);

    }

    public static void onTopicUpdate(long topicId) {
        TopicUpdateEvent event = new TopicUpdateEvent();
        event.topicId=topicId;
        EventBus.getDefault().post(event);
    }
}
