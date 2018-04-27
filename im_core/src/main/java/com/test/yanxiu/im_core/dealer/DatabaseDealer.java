package com.test.yanxiu.im_core.dealer;

import android.content.ContentValues;

import com.orhanobut.logger.Logger;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbMyMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.http.common.ImMember;
import com.test.yanxiu.im_core.http.common.ImMsg;
import com.test.yanxiu.im_core.http.common.ImTopic;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.ClusterQuery;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cailei on 06/03/2018.
 */
// litepal已知问题列表
// 1, 只能两表关联，且不支持多个外键（会重名）
// 2, ClusterQuery中每种类型的只能有一次，连续.where两次，则覆盖

public class DatabaseDealer {
    public final static int pagesize = 20;

    // 每个用户用自己不同的数据库，db_<userId>作为数据库名
    public static void useDbForUser(String userId) {
        if (userId == null) {
            Logger.e("error: create db for " + userId);
            return;
        }

        LitePalDB db = new LitePalDB("db_" + userId, 1);
        db.addClassName(DbMember.class.getName());
        db.addClassName(DbTopic.class.getName());
        db.addClassName(DbMsg.class.getName());
        db.addClassName(DbMyMsg.class.getName());
        LitePal.use(db);
    }


    public static void genMockData() {
        DbMember memberA = new DbMember();
        memberA.setImId(111111);

        DbMember memberB = new DbMember();
        memberB.setImId(222222);

        DbMember memberC = new DbMember();
        memberC.setImId(333333);

        DbMember memberM = new DbMember();
        memberM.setImId(454545);

        // 设置 topic 1
        DbTopic t1 = new DbTopic();
        t1.setTopicId(1);

        t1.getMembers().add(memberA);
        t1.getMembers().add(memberM);

        DbMsg m11 = new DbMsg();
        m11.setReqId("m11");
        m11.setTopicId(t1.getTopicId());
        m11.setSenderId(memberA.getImId());
        m11.setSendTime(1);
        m11.setMsgId(1);

        DbMyMsg my1 = new DbMyMsg();
        my1.setReqId("my1");
        my1.setTopicId(t1.getTopicId());
        my1.setSenderId(memberM.getImId());
        my1.setSendTime(2);
        my1.setMsgId(1);

        DbMsg m12 = new DbMsg();
        m12.setReqId("m12");
        m12.setTopicId(t1.getTopicId());
        m12.setSenderId(memberA.getImId());
        m12.setSendTime(5);
        m12.setMsgId(4);

        DbMyMsg my2 = new DbMyMsg();
        my2.setReqId("my2");
        my2.setTopicId(t1.getTopicId());
        my2.setSenderId(memberM.getImId());
        my2.setSendTime(9);
        my2.setMsgId(4);

        // 设置 topic 2
        DbTopic t2 = new DbTopic();
        t2.setTopicId(2);

        t2.getMembers().add(memberB);

        DbMsg m21 = new DbMsg();
        m21.setReqId("m21");
        m21.setTopicId(t2.getTopicId());
        m21.setSenderId(memberB.getImId());
        m21.setSendTime(3);
        m21.setMsgId(2);

        // 设置 topic 3
        DbTopic t3 = new DbTopic();
        t3.setTopicId(3);

        t3.getMembers().add(memberA);
        t3.getMembers().add(memberB);
        t3.getMembers().add(memberM);
        t3.getMembers().add(memberC);

        DbMsg m31 = new DbMsg();
        m31.setReqId("m31");
        m31.setTopicId(t3.getTopicId());
        m31.setSenderId(memberA.getImId());
        m31.setSendTime(4);
        m31.setMsgId(3);

        DbMsg m32 = new DbMsg();
        m32.setReqId("m32");
        m32.setTopicId(t3.getTopicId());
        m32.setSenderId(memberB.getImId());
        m32.setSendTime(6);
        m32.setMsgId(6);

        DbMyMsg my3 = new DbMyMsg();
        my3.setReqId("my3");
        my3.setTopicId(t3.getTopicId());
        my3.setSenderId(memberM.getImId());
        my3.setSendTime(7);
        my3.setMsgId(6);

        DbMsg m33 = new DbMsg();
        m33.setReqId("m33");
        m33.setTopicId(t3.getTopicId());
        m33.setSenderId(memberC.getImId());
        m33.setSendTime(8);
        m33.setMsgId(8);

        memberA.save();
        memberB.save();
        memberC.save();
        memberM.save();

        t1.save();
        m11.save();
        my1.save();
        m12.save();
        my2.save();

        t2.save();
        m21.save();

        t3.save();
        m31.save();
        m32.save();
        my3.save();
        m33.save();
    }

    /**
     * 获取此topic的从startMsgId开始的DbMsg中count条数据以及DbMyMsg中相关的数据，msgId值大的在前
     *
     * @param startMsgId : 最大的msgId. 传-1为从最近一条msg开始
     * @param count      : 每页的数据个数，默认同server保持一致为20
     * @return 返回merge后的数组，如果数据足够，数组size() >= count，如果数组size()小于count值，则表明已经取完所有数据
     */
    public static List<DbMsg> getTopicMsgs(long topicId, long startMsgId, int count) {
        ClusterQuery otherQuery = null;
        ClusterQuery myQuery = null;

        if (startMsgId == -1) {
            otherQuery = DataSupport
                    .where("topicId = ?", Long.toString(topicId))
                    .limit(count)
                    .order("msgid desc");   // server按照msgId插入，这里也可以用sendtime
            myQuery = DataSupport
                    .where("topicid = ?", Long.toString(topicId))
                    .limit(count)
                    .order("sendtime desc");

        } else {
            otherQuery = DataSupport
                    .where("topicId = ? and msgid <= ?",
                            Long.toString(topicId),
                            Long.toString(startMsgId))
                    .limit(count)
                    .order("msgid desc");   // server按照msgId插入，这里也可以用sendtime
            myQuery = DataSupport
                    .where("topicId = ? and msgid <= ?",
                            Long.toString(topicId),
                            Long.toString(startMsgId))
                    .limit(count)
                    .order("sendtime desc");
        }

        List<DbMsg> otherMsgs = otherQuery.find(DbMsg.class);
        List<DbMyMsg> myMsgs = myQuery.find(DbMyMsg.class);
        if (myMsgs.size() > 0) {
            merge(otherMsgs, myMsgs);
        }

        int actualCount = Math.min(otherMsgs.size(), count);
        for (Iterator<DbMsg> i = otherMsgs.iterator(); i.hasNext(); ) {
            actualCount--;
            i.next();
            if (actualCount < 0) {
                i.remove();
            }
        }

        return otherMsgs;
    }

    private static void merge(List<DbMsg> otherMsgs, List<DbMyMsg> myMsgs) {
        int i = 0, j = 0;
        while (i < otherMsgs.size() && j < myMsgs.size()) {
            DbMsg otherMsg = otherMsgs.get(i);
            DbMyMsg myMsg = myMsgs.get(j);
            if (otherMsg.getMsgId() > myMsg.getMsgId()) {
                i++;
            } else {
                // 倒叙则先插 本地send的msg，然后是网上获得的
                otherMsgs.add(i, myMsg);
                i++;
                j++;
            }
        }

        while (j < myMsgs.size()) {
            DbMyMsg myMsg = myMsgs.get(j);
            otherMsgs.add(myMsg);
            j++;
        }
    }

    /**
     * 从数据库重建Topic List，每条topic带最新一页pagesize条msgs，且topic list按照topic里最新的msg排序
     */
    public static List<DbTopic> topicsFromDb() {
        List<DbTopic> topics = DataSupport.findAll(DbTopic.class, true);
        for (DbTopic topic : topics) {
            List<DbMsg> msgs = getTopicMsgs(topic.getTopicId(), -1, pagesize);
            // 理论上讲应该每个topic的msgs里至少有一条消息
            topic.latestMsgId = -1;
            if ((msgs != null) && (msgs.size() > 0)) {
                topic.latestMsgId = msgs.get(0).getMsgId();
                topic.latestMsgTime = msgs.get(0).getSendTime();
            }
            topic.mergedMsgs = msgs;
        }

        Collections.sort(topics, topicComparator);

        return topics;
    }

    /**
     * 首先判断 是否需要人工置顶
     * 同时最多有两个同时为置顶标志的topic
     * 此时比较两者的localoperateTime
     *
     */
    public static Comparator<DbTopic> topicComparatorInsertTopBaseOnLocalTime = new Comparator<DbTopic>() {
        @Override
        public int compare(DbTopic topic1, DbTopic topic2) {
            //置顶标志的topic 上升
            if (topic1.shouldInsertToTop && !topic2.shouldInsertToTop) {
                return -1;
            } else if (!topic1.shouldInsertToTop && topic2.shouldInsertToTop) {
                return 1;
            } else { //其余无法通过 置顶标志位进行判断的 一律采用最新msg time 进行排序


                //查看是否最后一条是离线数据
                DbMyMsg localMsg1=getLatestMyMsgByMsgId(topic1.latestMsgId);
                DbMyMsg localMsg2=getLatestMyMsgByMsgId(topic2.latestMsgId);

                long rangeTime1=topic1.latestMsgTime;
                long rangeTime2=topic2.latestMsgTime;
                if (localMsg1 != null) {
                    rangeTime1=localMsg1.getSendTime();
                }
                if (localMsg2 != null) {
                    rangeTime2=localMsg2.getSendTime();
                }

                if( rangeTime1< rangeTime2){
                    return 1;
                }else if (rangeTime1>rangeTime2){
                    return -1;
                }
                return 0;
            }
//            return 0;
        }
    };

    public static Comparator<DbTopic> topicComparatorBaseOnBothLocalAndServerTime=new Comparator<DbTopic>() {
        @Override
        public int compare(DbTopic topic1, DbTopic topic2) {
            DbMyMsg localMsg1=getLatestMyMsgByMsgId(topic1.latestMsgId);
            DbMyMsg localMsg2=getLatestMyMsgByMsgId(topic2.latestMsgId);

            long rangeTime1=topic1.latestMsgTime;
            long rangeTime2=topic2.latestMsgTime;
            if (localMsg1 != null) {
                rangeTime1=localMsg1.getSendTime();
            }
            if (localMsg2 != null) {
                rangeTime2=localMsg2.getSendTime();
            }

           if( rangeTime1< rangeTime2){
                return 1;
           }else if (rangeTime1>rangeTime2){
               return -1;
           }
            return 0;
        }
    };

    public static Comparator<DbTopic> topicComparator = new Comparator<DbTopic>() {
        @Override
        public int compare(DbTopic t1, DbTopic t2) {
            //由于加入了离线消息的时间判断 这里要对离线消息进行比较
            long t1Time = t1.latestMsgTime;
            long t2Time = t2.latestMsgTime;
            if (t1Time < t2Time) {
                return 1;
            }
            if (t1Time > t2Time) {
                return -1;
            }
            return 0;
        }
    };

    public static Comparator<DbMsg> msgComparator = new Comparator<DbMsg>() {
        @Override
        public int compare(DbMsg m1, DbMsg m2) {
            if (m1.getMsgId() < m2.getMsgId()) {
                return 1;
            }
            if (m1.getMsgId() > m2.getMsgId()) {
                return -1;
            }
            return 0;
        }
    };

    public static DbTopic updateDbTopicWithImTopic(ImTopic topic) {
        List<DbTopic> topics = DataSupport
                .where("topicid=?", Long.toString(topic.topicId))
                .find(DbTopic.class, true);

        DbTopic dbTopic = new DbTopic();
        if (topics.size() > 0) {
            dbTopic = topics.get(0);
        }
        dbTopic.setTopicId(topic.topicId);
        dbTopic.setName(topic.topicName);
        dbTopic.setType(topic.topicType);
        dbTopic.setChange(topic.topicChange);
        dbTopic.setGroup(topic.topicGroup);
//        dbTopic.getMembers().clear();
//
//        for (ImTopic.Member member : topic.members) {
//            ImMember imMember = member.memberInfo;
//            DbMember dbMember = updateDbMemberWithImMember(imMember);
//            dbTopic.getMembers().add(dbMember);
//            dbMember.getTopics().add(dbTopic);
//            dbMember.save();
//        }

        updateMembersThatNeedUpdate(dbTopic, topic);
        dbTopic.save();
        return dbTopic;
    }

    // 将topic新增的member，和有信息改变的member，存DB
    private static void updateMembersThatNeedUpdate(DbTopic dbTopic, ImTopic imTopic) {
        List<DbMember> ret = new ArrayList<>();
        if (imTopic.members == null) {
            return ;
        }
        for (ImTopic.Member member : imTopic.members) {
            ImMember imMember = member.memberInfo;
            DbMember dbMember = null;
            for (DbMember m : dbTopic.getMembers()) {
                if (m.getImId() == imMember.imId) {
                    dbMember = m;
                    break;
                }
            }

            if (dbMember == null) {
                // 此member为新增人员
                dbMember = updateDbMemberWithImMember(imMember);
                dbTopic.getMembers().add(dbMember);
                dbMember.getTopics().add(dbTopic);
                dbMember.save();
            } else {
                // 此member为已有人员
                if (hasMemberUpdate(dbMember, imMember)) {
                    // 且此member有更新信息
                    dbMember.setName(imMember.memberName);
                    dbMember.setAvatar(imMember.avatar);
                    dbMember.save();
                }
            }
        }
    }

    // 判断相比于DB中的dbMember,HTTP返回的imMember是否有改动
    private static boolean hasMemberUpdate(DbMember dbMember, ImMember imMember) {
        boolean ret = false;
        if (!dbMember.getName().equals(imMember.memberName)) {
            ret = true;
        }

        if (!dbMember.getAvatar().equals(imMember.avatar)) {
            ret = true;
        }

        return ret;
    }

    public static DbMember updateDbMemberWithImMember(ImMember member) {
        List<DbMember> members = DataSupport
                .where("imid = ?", Long.toString(member.imId))
                .find(DbMember.class, true);

        DbMember dbMember = new DbMember();
        if (members.size() > 0) {
            dbMember = members.get(0);
        }
        dbMember.setImId(member.imId);
        dbMember.setName(member.memberName);
        dbMember.setAvatar(member.avatar);
        dbMember.save();
        return dbMember;
    }

    /**
     * 更新 重发消息的数据库状态
     * 由于直接采用save()方法 会出现 失败的异常
     * 采用数据库更新对已经存在数据库中的 未发送成功的数据进行状态参数的更新
     */
    public static DbMyMsg updateResendMsg(DbMyMsg dbMsg, String from) {
        DbMyMsg theMsg = DataSupport
                .where("reqid = ?", dbMsg.getReqId())
                .findFirst(DbMyMsg.class);
        if (theMsg == null) {
            //如果 数据库中还没有保存这条信息  进行保存并返回
            dbMsg.save();
            return dbMsg;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("from", from);
        contentValues.put("msgId", dbMsg.getMsgId());
        contentValues.put("sendTime", dbMsg.getSendTime());
        contentValues.put("state", dbMsg.getState());
        DataSupport.updateAll(DbMyMsg.class, contentValues, "reqid = ?", dbMsg.getReqId());
        theMsg = DataSupport
                .where("reqid = ?", dbMsg.getReqId())
                .findFirst(DbMyMsg.class);
        return theMsg;
    }

    /**
     * 获取此topic的从startMsgId开始的DbMsg中count条数据以及DbMyMsg中相关的数据，msgId值大的在前
     *
     * @param from        : http 或 mqtt
     * @param curUserImId : 当前app的登录用户的imId
     * @return 返回merge后的数组，如果数据足够，数组size() >= count，如果数组size()小于count值，则表明已经取完所有数据
     */
    public static DbMsg updateDbMsgWithImMsg(ImMsg msg, String from, long curUserImId) {
        DbMsg theMsg;
        if (msg.senderId == curUserImId) {
            // 我发的消息不入库，以后有删除后，重拉消息列表时，应该入DbMyMsg库
            DbMyMsg dbMyMsg = DataSupport
                    .where("reqid = ?", msg.reqId)
                    .findFirst(DbMyMsg.class);
            if (dbMyMsg == null) {
                dbMyMsg = new DbMyMsg();
            }

            dbMyMsg.setState(DbMyMsg.State.Success.ordinal());    // http来的消息都是以完成的消息
            theMsg = dbMyMsg;
        } else {
            DbMsg dbMsg = DataSupport
                    .where("reqid = ?", msg.reqId)
                    .findFirst(DbMsg.class);
            if (dbMsg == null) {
                dbMsg = new DbMsg();
            }

            theMsg = dbMsg;
        }

        theMsg.setReqId(msg.reqId);
        theMsg.setMsgId(msg.msgId);
        theMsg.setTopicId(msg.topicId);
        theMsg.setSenderId(msg.senderId);
        theMsg.setSendTime(msg.sendTime);
        theMsg.setContentType(msg.contentType);
        theMsg.setMsg(msg.contentData.msg);
        theMsg.setThumbnail(msg.contentData.thumbnail);
        theMsg.setViewUrl(msg.contentData.viewUrl);
        theMsg.setWith(msg.contentData.width);
        theMsg.setHeight(msg.contentData.height);
        theMsg.setFrom(from);
        theMsg.save();
        return theMsg;
    }

    /**
     * 根据给定的topic id 删除数据库信息
     */
    public static void deleteTopicById(long topicId) {
        DataSupport.deleteAll(DbTopic.class,
                "topicId = ? ", String.valueOf(topicId));

    }

    /**
     * 获取到服务器返回的最新消息下 查找本地是否有 未发送成功msg
     * 如果有 认为 本地未发送成功的mymsg 为最新消息 供topicList 展示
     */
    public static DbMyMsg getLatestMyMsgByMsgId(long msgId) {
        DbMyMsg latestMyMsg = DataSupport.where("msgId = ?", Long.toString(msgId))
                .order("sendTime desc")
                .findFirst(DbMyMsg.class);
        return latestMyMsg;
    }
    //region util

    public static long getLatestMsgIdForTopic(long topicId) {
        long ret = -1;
        DbMsg msg = DataSupport
                .where("topicid = ?", Long.toString(topicId))
                .order("msgid desc")
                .findFirst(DbMsg.class);

        DbMyMsg myMsg = DataSupport
                .where("topicid = ?", Long.toString(topicId))
                .order("msgid desc")
                .findFirst(DbMyMsg.class);

        if (msg != null) {
            ret = msg.getMsgId();
        }

        if (myMsg != null) {
            ret = ret > myMsg.getMsgId() ? ret : myMsg.getMsgId();
        }

        return ret;
    }

    public static DbMember getMemberById(long memberId) {
        DbMember member = null;
        List<DbMember> members = DataSupport
                .where("imid = ?", Long.toString(memberId))
                .find(DbMember.class, true);

        if (members.size() > 0) {
            member = members.get(0);
        }
        return member;
    }

    public static String getTopicTitle(DbTopic topic, long curUserImId) {
        String ret = "";
        if (topic.getType().equals("1")) { // 私聊
            for (DbMember member : topic.getMembers()) {
                if (member.getImId() != curUserImId) {
                    ret = member.getName();
                    break;
                }
            }
        }

        if (topic.getType().equals("2")) { // 群聊
            ret = "班级群聊 (" + topic.getMembers().size() + ")";
        }
        return ret;
    }

    public static void pendingMsgToTopic(DbMsg msg, DbTopic topic) {
        for (DbMsg m : topic.mergedMsgs) {
            if (m.getReqId().equals(msg.getReqId())) {
                // 已经存在了，不理了
                return;
            }
        }

        topic.mergedMsgs.add(0, msg);
        topic.latestMsgId = msg.getMsgId();
        topic.latestMsgTime = msg.getSendTime();
        //记录本地操作时间
        topic.latestOperateLocalTime = System.currentTimeMillis();
    }
    //endregion

    public static DbTopic mockTopic() {
        DbTopic topic = DataSupport
                .order("topicid asc")
                .findFirst(DbTopic.class);

        DbTopic ret = new DbTopic();
        if (topic == null) {
            ret.setTopicId(-1);
        } else if (topic.getTopicId() >= 0) {
            ret.setTopicId(-1);
        } else {
            ret.setTopicId(topic.getTopicId() - 1);
        }

        ret.setName("mock name");
        ret.setType("1");
        ret.setChange("mock change");
        ret.setGroup("mock group");

        return ret;
    }

    // 负数为临时topic，为了保留无网络状态下发送的msg用
    public static boolean isMockTopic(DbTopic topic) {
        if (topic == null) {
            return true;
        }

        return topic.getTopicId() < 0 ? true : false;
    }

    public static void removeMockTopic(DbTopic topic) {
        if (topic == null) {
            return;
        }

        if (!isMockTopic(topic)) {
            return;
        }

        topic.delete();
    }

    public static void migrateMsgsForMockTopic(DbTopic mockTopic, DbTopic realTopic) {
        // 对于DB处理
        List<DbMyMsg> myMsgs = DataSupport
                .where("topicid = ?", Long.toString(mockTopic.getTopicId()))
                .find(DbMyMsg.class);

        for (DbMyMsg myMsg : myMsgs) {
            myMsg.setTopicId(realTopic.getTopicId());
            myMsg.save();
        }

        // 对于UI处理
        realTopic.mergedMsgs.addAll(mockTopic.mergedMsgs);
        for (DbMsg msg : mockTopic.mergedMsgs) {
            if (msg instanceof DbMyMsg) {
                DbMyMsg myMsg = (DbMyMsg) msg;
                myMsg.setTopicId(realTopic.getTopicId());
            }
        }
    }

    // 将mock topicz中的msg都设置为发送失败
    public static void topicCreateFailed(DbTopic mockTopic) {
        // 对于DB处理
        List<DbMyMsg> myMsgs = DataSupport
                .where("topicid = ?", Long.toString(mockTopic.getTopicId()))
                .find(DbMyMsg.class);

        for (DbMyMsg myMsg : myMsgs) {
            myMsg.setState(DbMyMsg.State.Failed.ordinal());
            myMsg.save();
        }

        // 对于UI处理
        for (DbMsg msg : mockTopic.mergedMsgs) {
            if (msg instanceof DbMyMsg) {
                DbMyMsg myMsg = (DbMyMsg) msg;
                myMsg.setState(DbMyMsg.State.Failed.ordinal());
            }
        }
    }
}
