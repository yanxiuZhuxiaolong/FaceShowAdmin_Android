package com.yanxiu.gphone.faceshowadmin_android.net.notice;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufengqing on 2017/11/1.
 */

public class NoticeRequestResponse extends FaceShowBaseResponse {


    /**
     * data : {"noticeInfos":{"totalElements":0,"elements":[{"id":214,"title":"111","content":"11","authorId":23248709,"clazzId":15,"createTime":"2017-10-27 14:40:21","updateTime":"2017-10-27 14:40:21","state":1,"attachUrl":"http://upload.ugc.yanxiu.com/img/c39610587bd14adf63051166765a223f.bmp?from=22&resId=28939360","readNum":3,"attachName":"584_小鸭子bmp.bmp","authorName":"杨大班","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false},{"id":213,"title":"今天下雪","content":"好大的雪","authorId":23248709,"clazzId":15,"createTime":"2017-10-27 10:00:12","updateTime":"2017-10-27 10:00:12","state":1,"attachUrl":"","readNum":1,"attachName":"","authorName":"杨大班","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false},{"id":191,"title":"这是一条测试数据","content":"这是一条测试数据的详情，修改一下呢","authorId":9768712,"clazzId":15,"createTime":"2017-10-24 09:15:14","updateTime":"2017-10-27 10:02:29","state":1,"attachUrl":"","readNum":7,"attachName":"","authorName":"多隆测试","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":3,"viewed":false},{"id":190,"title":"发个通知试试","content":"发个通知试试11111111111111","authorId":9768712,"clazzId":15,"createTime":"2017-10-23 18:25:35","updateTime":"2017-10-23 18:25:35","state":1,"attachUrl":"","readNum":1,"attachName":"","authorName":"多隆测试","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false},{"id":189,"title":"111111111111","content":"11111111111111","authorId":9768712,"clazzId":15,"createTime":"2017-10-23 16:05:14","updateTime":"2017-10-23 16:05:14","state":1,"attachUrl":"","readNum":1,"attachName":"","authorName":"多隆测试","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false}],"callbackParam":"id","callbackValue":189,"callbacks":[{"callbackParam":"id","callbackValue":189}]},"studentNum":5}
     * currentUser :
     * currentTime : 1509507324542
     * error : null
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * noticeInfos : {"totalElements":0,"elements":[{"id":214,"title":"111","content":"11","authorId":23248709,"clazzId":15,"createTime":"2017-10-27 14:40:21","updateTime":"2017-10-27 14:40:21","state":1,"attachUrl":"http://upload.ugc.yanxiu.com/img/c39610587bd14adf63051166765a223f.bmp?from=22&resId=28939360","readNum":3,"attachName":"584_小鸭子bmp.bmp","authorName":"杨大班","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false},{"id":213,"title":"今天下雪","content":"好大的雪","authorId":23248709,"clazzId":15,"createTime":"2017-10-27 10:00:12","updateTime":"2017-10-27 10:00:12","state":1,"attachUrl":"","readNum":1,"attachName":"","authorName":"杨大班","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false},{"id":191,"title":"这是一条测试数据","content":"这是一条测试数据的详情，修改一下呢","authorId":9768712,"clazzId":15,"createTime":"2017-10-24 09:15:14","updateTime":"2017-10-27 10:02:29","state":1,"attachUrl":"","readNum":7,"attachName":"","authorName":"多隆测试","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":3,"viewed":false},{"id":190,"title":"发个通知试试","content":"发个通知试试11111111111111","authorId":9768712,"clazzId":15,"createTime":"2017-10-23 18:25:35","updateTime":"2017-10-23 18:25:35","state":1,"attachUrl":"","readNum":1,"attachName":"","authorName":"多隆测试","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false},{"id":189,"title":"111111111111","content":"11111111111111","authorId":9768712,"clazzId":15,"createTime":"2017-10-23 16:05:14","updateTime":"2017-10-23 16:05:14","state":1,"attachUrl":"","readNum":1,"attachName":"","authorName":"多隆测试","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false}],"callbackParam":"id","callbackValue":189,"callbacks":[{"callbackParam":"id","callbackValue":189}]}
         * studentNum : 5
         */

        private NoticeInfosBean noticeInfos;
        private int studentNum;

        public NoticeInfosBean getNoticeInfos() {
            return noticeInfos;
        }

        public void setNoticeInfos(NoticeInfosBean noticeInfos) {
            this.noticeInfos = noticeInfos;
        }

        public int getStudentNum() {
            return studentNum;
        }

        public void setStudentNum(int studentNum) {
            this.studentNum = studentNum;
        }

        public static class NoticeInfosBean {
            /**
             * totalElements : 0
             * elements : [{"id":214,"title":"111","content":"11","authorId":23248709,"clazzId":15,"createTime":"2017-10-27 14:40:21","updateTime":"2017-10-27 14:40:21","state":1,"attachUrl":"http://upload.ugc.yanxiu.com/img/c39610587bd14adf63051166765a223f.bmp?from=22&resId=28939360","readNum":3,"attachName":"584_小鸭子bmp.bmp","authorName":"杨大班","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false},{"id":213,"title":"今天下雪","content":"好大的雪","authorId":23248709,"clazzId":15,"createTime":"2017-10-27 10:00:12","updateTime":"2017-10-27 10:00:12","state":1,"attachUrl":"","readNum":1,"attachName":"","authorName":"杨大班","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false},{"id":191,"title":"这是一条测试数据","content":"这是一条测试数据的详情，修改一下呢","authorId":9768712,"clazzId":15,"createTime":"2017-10-24 09:15:14","updateTime":"2017-10-27 10:02:29","state":1,"attachUrl":"","readNum":7,"attachName":"","authorName":"多隆测试","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":3,"viewed":false},{"id":190,"title":"发个通知试试","content":"发个通知试试11111111111111","authorId":9768712,"clazzId":15,"createTime":"2017-10-23 18:25:35","updateTime":"2017-10-23 18:25:35","state":1,"attachUrl":"","readNum":1,"attachName":"","authorName":"多隆测试","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false},{"id":189,"title":"111111111111","content":"11111111111111","authorId":9768712,"clazzId":15,"createTime":"2017-10-23 16:05:14","updateTime":"2017-10-23 16:05:14","state":1,"attachUrl":"","readNum":1,"attachName":"","authorName":"多隆测试","createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"noticeReadUserNum":1,"viewed":false}]
             * callbackParam : id
             * callbackValue : 189
             * callbacks : [{"callbackParam":"id","callbackValue":189}]
             */

            private int totalElements;
            private String callbackParam;
            private String callbackValue;
            private List<ElementsBean> elements;
            private List<CallbacksBean> callbacks;

            public int getTotalElements() {
                return totalElements;
            }

            public void setTotalElements(int totalElements) {
                this.totalElements = totalElements;
            }

            public String getCallbackParam() {
                return callbackParam;
            }

            public void setCallbackParam(String callbackParam) {
                this.callbackParam = callbackParam;
            }

            public String getCallbackValue() {
                return callbackValue;
            }

            public void setCallbackValue(String callbackValue) {
                this.callbackValue = callbackValue;
            }

            public List<ElementsBean> getElements() {
                return elements;
            }

            public void setElements(List<ElementsBean> elements) {
                this.elements = elements;
            }

            public List<CallbacksBean> getCallbacks() {
                return callbacks;
            }

            public void setCallbacks(List<CallbacksBean> callbacks) {
                this.callbacks = callbacks;
            }

            public static class ElementsBean implements Serializable{
                /**
                 * id : 214
                 * title : 111
                 * content : 11
                 * authorId : 23248709
                 * clazzId : 15
                 * createTime : 2017-10-27 14:40:21
                 * updateTime : 2017-10-27 14:40:21
                 * state : 1
                 * attachUrl : http://upload.ugc.yanxiu.com/img/c39610587bd14adf63051166765a223f.bmp?from=22&resId=28939360
                 * readNum : 3
                 * attachName : 584_小鸭子bmp.bmp
                 * authorName : 杨大班
                 * createTimeStr : null
                 * updateTimeStr : null
                 * noticeNum : null
                 * noticeReadNumSum : null
                 * noticeReadUserNum : 1
                 * viewed : false
                 */

                private String id;
                private String title;
                private String content;
                private int authorId;
                private int clazzId;
                private String createTime;
                private String updateTime;
                private int state;
                private String attachUrl;
                private int readNum;
                private String attachName;
                private String authorName;
                private String createTimeStr;
                private String updateTimeStr;
                private int noticeNum;
                private int noticeReadNumSum;
                private int noticeReadUserNum;
                private boolean viewed;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public int getAuthorId() {
                    return authorId;
                }

                public void setAuthorId(int authorId) {
                    this.authorId = authorId;
                }

                public int getClazzId() {
                    return clazzId;
                }

                public void setClazzId(int clazzId) {
                    this.clazzId = clazzId;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public String getUpdateTime() {
                    return updateTime;
                }

                public void setUpdateTime(String updateTime) {
                    this.updateTime = updateTime;
                }

                public int getState() {
                    return state;
                }

                public void setState(int state) {
                    this.state = state;
                }

                public String getAttachUrl() {
                    return attachUrl;
                }

                public void setAttachUrl(String attachUrl) {
                    this.attachUrl = attachUrl;
                }

                public int getReadNum() {
                    return readNum;
                }

                public void setReadNum(int readNum) {
                    this.readNum = readNum;
                }

                public String getAttachName() {
                    return attachName;
                }

                public void setAttachName(String attachName) {
                    this.attachName = attachName;
                }

                public String getAuthorName() {
                    return authorName;
                }

                public void setAuthorName(String authorName) {
                    this.authorName = authorName;
                }

                public String getCreateTimeStr() {
                    return createTimeStr;
                }

                public void setCreateTimeStr(String createTimeStr) {
                    this.createTimeStr = createTimeStr;
                }

                public String getUpdateTimeStr() {
                    return updateTimeStr;
                }

                public void setUpdateTimeStr(String updateTimeStr) {
                    this.updateTimeStr = updateTimeStr;
                }

                public int getNoticeNum() {
                    return noticeNum;
                }

                public void setNoticeNum(int noticeNum) {
                    this.noticeNum = noticeNum;
                }

                public int getNoticeReadNumSum() {
                    return noticeReadNumSum;
                }

                public void setNoticeReadNumSum(int noticeReadNumSum) {
                    this.noticeReadNumSum = noticeReadNumSum;
                }

                public int getNoticeReadUserNum() {
                    return noticeReadUserNum;
                }

                public void setNoticeReadUserNum(int noticeReadUserNum) {
                    this.noticeReadUserNum = noticeReadUserNum;
                }

                public boolean isViewed() {
                    return viewed;
                }

                public void setViewed(boolean viewed) {
                    this.viewed = viewed;
                }
            }

            public static class CallbacksBean {
                /**
                 * callbackParam : id
                 * callbackValue : 189
                 */

                private String callbackParam;
                private int callbackValue;

                public String getCallbackParam() {
                    return callbackParam;
                }

                public void setCallbackParam(String callbackParam) {
                    this.callbackParam = callbackParam;
                }

                public int getCallbackValue() {
                    return callbackValue;
                }

                public void setCallbackValue(int callbackValue) {
                    this.callbackValue = callbackValue;
                }
            }
        }
    }
}
