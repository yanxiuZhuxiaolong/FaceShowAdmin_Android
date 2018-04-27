package com.test.yanxiu.im_core.http;

import java.util.List;

/**
 * @author by frc on 2018/3/20.
 */

public class GetContactsResponse extends ImResponseBase {


    private DataBean data;
    private long currentTime;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public static class DataBean {


        private int imEvent;
        private String reqId;
        private Object topicChange;
        private Object topic;
        private Object topicMsg;
        private Object chatroom;
        private Object members;
        private ContactsBeanX contacts;

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

        public Object getTopicChange() {
            return topicChange;
        }

        public void setTopicChange(Object topicChange) {
            this.topicChange = topicChange;
        }

        public Object getTopic() {
            return topic;
        }

        public void setTopic(Object topic) {
            this.topic = topic;
        }

        public Object getTopicMsg() {
            return topicMsg;
        }

        public void setTopicMsg(Object topicMsg) {
            this.topicMsg = topicMsg;
        }

        public Object getChatroom() {
            return chatroom;
        }

        public void setChatroom(Object chatroom) {
            this.chatroom = chatroom;
        }

        public Object getMembers() {
            return members;
        }

        public void setMembers(Object members) {
            this.members = members;
        }

        public ContactsBeanX getContacts() {
            return contacts;
        }

        public void setContacts(ContactsBeanX contacts) {
            this.contacts = contacts;
        }

        public static class ContactsBeanX {
            private List<GroupsBean> groups;
            private List<PersonalsBean> personals;

            public List<GroupsBean> getGroups() {
                return groups;
            }

            public void setGroups(List<GroupsBean> groups) {
                this.groups = groups;
            }

            public List<PersonalsBean> getPersonals() {
                return personals;
            }

            public void setPersonals(List<PersonalsBean> personals) {
                this.personals = personals;
            }


            public static class PersonalsBean {
                /**
                 * id : 8
                 * bizSource : 22
                 * memberId : 216
                 * contactId : 217
                 * contactType : 1
                 * memberInfo : {"id":217,"bizSource":22,"memberType":1,"userId":23249110,"memberName":"1111","avatar":"http://s2.jsyxw.cn/yanxiu/h2.jpg","state":1}
                 */

                private int id;
                private int bizSource;
                private int memberId;
                private int contactId;
                private int contactType;
                private MemberInfoBeanX memberInfo;

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

                public int getMemberId() {
                    return memberId;
                }

                public void setMemberId(int memberId) {
                    this.memberId = memberId;
                }

                public int getContactId() {
                    return contactId;
                }

                public void setContactId(int contactId) {
                    this.contactId = contactId;
                }

                public int getContactType() {
                    return contactType;
                }

                public void setContactType(int contactType) {
                    this.contactType = contactType;
                }

                public MemberInfoBeanX getMemberInfo() {
                    return memberInfo;
                }

                public void setMemberInfo(MemberInfoBeanX memberInfo) {
                    this.memberInfo = memberInfo;
                }

                public static class MemberInfoBeanX {
                    /**
                     * id : 217
                     * bizSource : 22
                     * memberType : 1
                     * userId : 23249110
                     * memberName : 1111
                     * avatar : http://s2.jsyxw.cn/yanxiu/h2.jpg
                     * state : 1
                     */

                    private int id;
                    private int bizSource;
                    private int memberType;
                    private int userId;
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

                    public int getUserId() {
                        return userId;
                    }

                    public void setUserId(int userId) {
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
        }
    }

    public static class MemberInfoBean {


        private Long id;
        private Long bizSource;
        private Long memberType;
        private Long userId;
        private String memberName;
        private String avatar;
        private Long state;

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

        public Long getState() {
            return state;
        }

        public void setState(Long state) {
            this.state = state;
        }
    }

    public static class ContactsBean {
        /**
         * id : null
         * bizSource : 22
         * memberId : 216
         * contactId : 216
         * contactType : 2
         * memberInfo : {"id":216,"bizSource":22,"memberType":1,"userId":23249307,"memberName":"133","avatar":"http://s2.jsyxw.cn/yanxiu/h2.jpg","state":1}
         */

        private Object id;
        private Long bizSource;
        private Long memberId;
        private Long contactId;
        private Long contactType;
        private MemberInfoBean memberInfo;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Long getBizSource() {
            return bizSource;
        }

        public void setBizSource(Long bizSource) {
            this.bizSource = bizSource;
        }

        public Long getMemberId() {
            return memberId;
        }

        public void setMemberId(Long memberId) {
            this.memberId = memberId;
        }

        public Long getContactId() {
            return contactId;
        }

        public void setContactId(Long contactId) {
            this.contactId = contactId;
        }

        public Long getContactType() {
            return contactType;
        }

        public void setContactType(Long contactType) {
            this.contactType = contactType;
        }

        public MemberInfoBean getMemberInfo() {
            return memberInfo;
        }

        public void setMemberInfo(MemberInfoBean memberInfo) {
            this.memberInfo = memberInfo;
        }
    }

    public static class GroupsBean {


        private Long groupId;
        private String groupName;
        private List<ContactsBean> contacts;

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public List<ContactsBean> getContacts() {
            return contacts;
        }

        public void setContacts(List<ContactsBean> contacts) {
            this.contacts = contacts;
        }


    }
}

