package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.request;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.util.List;

/**
 * @author frc on 2018/1/18.
 */

public class ClassCircleNewMessageResponse extends FaceShowBaseResponse {


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
        private List<MsgsBean> msgs;

        public List<MsgsBean> getMsgs() {
            return msgs;
        }

        public void setMsgs(List<MsgsBean> msgs) {
            this.msgs = msgs;
        }

        public static class MsgsBean {

            private long id;
            private int momentId;
            private int msgType;
            private int userId;
            private String userName;
            private String comment;
            private int like;
            private String createTime;
            private MomentSimpleBean momentSimple;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getMomentId() {
                return momentId;
            }

            public void setMomentId(int momentId) {
                this.momentId = momentId;
            }

            public int getMsgType() {
                return msgType;
            }

            public void setMsgType(int msgType) {
                this.msgType = msgType;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public int getLike() {
                return like;
            }

            public void setLike(int like) {
                this.like = like;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public MomentSimpleBean getMomentSimple() {
                return momentSimple;
            }

            public void setMomentSimple(MomentSimpleBean momentSimple) {
                this.momentSimple = momentSimple;
            }

            public static class MomentSimpleBean {
                /**
                 * id : 884
                 * content :
                 * image : http://p2xuvkfak.bkt.clouddn.com/Fqw-Fskg1UzRxEsIWU1Er24No6lP
                 */

                private int id;
                private String content;
                private String image;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }
            }
        }
    }
}
