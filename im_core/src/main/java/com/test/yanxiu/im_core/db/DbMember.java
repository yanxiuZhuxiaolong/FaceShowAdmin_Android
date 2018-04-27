package com.test.yanxiu.im_core.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cailei on 06/03/2018.
 */

public class DbMember extends DataSupport {
    @Column(unique = true, defaultValue = "unknown", nullable = false)
    private long imId;
    private String name;
    private String avatar;

    private long groupId;
    private String groupName;

    private List<DbTopic> topics = new ArrayList<>();   // 表明此用户加入了哪些topic

    // 只为UI显示用，不做数据库存储用
    @Column(ignore = true)
    public long fromTopicId = -1;

    //region getter setter
    public long getImId() {
        return imId;
    }

    public void setImId(long imId) {
        this.imId = imId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<DbTopic> getTopics() {
        return topics;
    }

    public void setTopics(List<DbTopic> topics) {
        this.topics = topics;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    //endregion
}
