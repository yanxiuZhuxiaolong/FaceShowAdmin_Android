package com.yanxiu.gphone.faceshowadmin_android.classCircle.response;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/21 17:37.
 * Function :
 */
public class Comments {
    public String id;
    public String clazsId;
    public String momentId;
    public String parentId;
    public String content;
    public String toUserId;
    public String updateTime;
    public String userId;
    /**
     * 1为评论，2为回复
     */
    public String level;
    public String createTime;
    /**
     * 评论人
     */
    public Publisher publisher;

    /**
     * 被评论人
     * */
    public Publisher toUser;
}
