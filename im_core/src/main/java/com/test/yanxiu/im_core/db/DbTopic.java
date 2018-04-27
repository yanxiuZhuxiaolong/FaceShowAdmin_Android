package com.test.yanxiu.im_core.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cailei on 06/03/2018.
 */

public class DbTopic extends DataSupport {
    @Column(unique = true, defaultValue = "unknown", nullable = false)
    private long topicId;
    private String name;
    private String type;
    private String change;  // 如果topic有名称、人员变动，则server的值和db值不相等，需要重新获取topic信息

    private List<DbMember> members = new ArrayList<>();
    private boolean showDot = false;

    private String group;
    private String fromTopic;   // 从哪个topic来的私聊


    // 只为UI显示用，不做数据库存储用

    // 用于本地的排序 采用置顶的方式进行topic 排序
    // 最近被操作的topic 置顶 不再依据时间排序
    @Column(ignore = true)
    public boolean shouldInsertToTop=false;
    //最后一次本地操作的时间
    @Column(ignore = true)
    public long latestOperateLocalTime;
    @Column(ignore = true)
    public long latestMsgId;
    @Column(ignore = true)
    public long latestMsgTime;
    @Column(ignore = true)
    public List<DbMsg> mergedMsgs = new ArrayList<>();

    //region getter setter
    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DbMember> getMembers() {
        return members;
    }

    public void setMembers(List<DbMember> members) {
        this.members = members;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public boolean isShowDot() {
        return showDot;
    }

    public void setShowDot(boolean showDot) {
        this.showDot = showDot;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFromTopic() {
        return fromTopic;
    }

    public void setFromTopic(String fromTopic) {
        this.fromTopic = fromTopic;
    }

    //endregion
}
