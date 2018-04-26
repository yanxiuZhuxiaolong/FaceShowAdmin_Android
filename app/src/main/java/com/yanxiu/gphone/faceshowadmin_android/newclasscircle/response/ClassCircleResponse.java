package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response;


import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.util.ArrayList;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/14 16:09.
 * Function :
 */
public class ClassCircleResponse extends FaceShowBaseResponse {

    public Data data;

    public class Data {
        public boolean hasNextPage;

        public ArrayList<Moments> moments;

        public class Moments {
            public String id;
            /**
             * 相册图片列表
             */
            public ArrayList<Album> album;
            public String clazsId;
            /**
             * 评论数量，包括一级评论和回复
             */
            public String commentedNum;
            public ArrayList<Comments> comments;
            public boolean isShowAll = false;
            public String content;
            /**
             * 点赞数量
             */
            public String likedNum;
            /**
             * 点赞
             */
            public ArrayList<Likes> likes;
            public String publishTime;
            public String publishTimeDesc;
            public Publisher publisher;
            /**
             * 阅读数量
             */
            public String readedNum;

            public class Likes {
                public String clazsId;
                public String createTime;
                public String id;
                public String momentId;
                public Publisher publisher;

            }

            public class Album {
                public String id;
                public String momentId;
                /**
                 * 图片详细信息
                 */
                public Attachment attachment;

                public class Attachment {
                    public int resId;
                    public String resKey;
                    public String resName;
                    public String resType;
                    public String ext;
                    public String downloadUrl;
                    public String previewUrl;
                    public int transcodeStatus;
                    public String resThumb;
                    public Object resSource;
                }
            }
        }
    }
}
