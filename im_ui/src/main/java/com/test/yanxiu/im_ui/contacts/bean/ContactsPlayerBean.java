package com.test.yanxiu.im_ui.contacts.bean;

import com.test.yanxiu.im_core.http.GetContactsResponse;
import com.test.yanxiu.im_ui.contacts.DatabaseFramework.annotation.DbPrimaryKey;
import com.test.yanxiu.im_ui.contacts.DatabaseFramework.annotation.DbTable;

/**
 * Created by frc on 2018/3/13.
 */
@DbTable("contacts")
public class ContactsPlayerBean {

    private Long id;
    private Long bizSource;
    private Long memberType;
    @DbPrimaryKey
    private Long userId;
    private String name;
    private String avatar;
    private Long state;

    private Long classId;
    private String className;

    public ContactsPlayerBean(GetContactsResponse.MemberInfoBean memberInfoBean, Long classId,String className) {
        this.id = memberInfoBean.getId();
        this.bizSource = memberInfoBean.getBizSource();
        this.memberType = memberInfoBean.getMemberType();
        this.userId = memberInfoBean.getUserId();
        this.name = memberInfoBean.getMemberName();
        this.avatar = memberInfoBean.getAvatar();
        this.state = memberInfoBean.getState();
        this.classId = classId;
        this.className=className;
    }

    public ContactsPlayerBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBizSource() {
        return bizSource;
    }

    public void setBizSource(Long bizSource) {
        this.bizSource = bizSource;
    }

    public Long getMemberType() {
        return memberType;
    }

    public void setMemberType(Long memberType) {
        this.memberType = memberType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
