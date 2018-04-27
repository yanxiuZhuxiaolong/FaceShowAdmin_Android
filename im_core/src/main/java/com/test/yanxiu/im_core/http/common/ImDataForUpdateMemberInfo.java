package com.test.yanxiu.im_core.http.common;

import java.util.List;

/**
 * Created by srt on 2018/3/27.
 */

public class ImDataForUpdateMemberInfo {

    /**
     * imEvent : 201
     * reqId : f7c59515-dc72-4304-ab92-c86140906927
     * members : [{"id":230,"bizSource":22,"memberType":1,"userId":2.3249306E7,"memberName":"小龙","avatar":"http://s2.jsyxw.cn/yanxiu/h2.jpg","state":1},{"id":293,"bizSource":22,"memberType":1,"userId":9801843,"memberName":"18900000000","avatar":"http://s2.jsyxw.cn/yanxiu/h2.jpg","state":1}]
     */

    private int imEvent;
    private String reqId;
    private List<MembersBean> members;

    public int getImEvent() {
        return imEvent;
    }

    public void setImEvent(int imEvent) {
        this.imEvent = imEvent;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public List<MembersBean> getMembers() {
        return members;
    }

    public void setMembers(List<MembersBean> members) {
        this.members = members;
    }

    public static class MembersBean {
        /**
         * id : 230
         * bizSource : 22
         * memberType : 1
         * userId : 2.3249306E7
         * memberName : 小龙
         * avatar : http://s2.jsyxw.cn/yanxiu/h2.jpg
         * state : 1
         */

        private int id;
        private int bizSource;
        private int memberType;
        private long userId;
        private String memberName;
        private String avatar;
        private int state;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getBizSource() {
            return bizSource;
        }

        public void setBizSource(int bizSource) {
            this.bizSource = bizSource;
        }

        public int getMemberType() {
            return memberType;
        }

        public void setMemberType(int memberType) {
            this.memberType = memberType;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
