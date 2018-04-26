package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.mock;

import com.google.gson.Gson;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.response.ClassCircleResponse;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/14 17:49.
 * Function :
 */
public class MockUtil {

    private static Gson gson=new Gson();

    private static String s="{\n" +
            "    \"code\": 0,\n" +
            "    \"currentUser\": {},\n" +
            "    \"data\": {\n" +
            "        \"moments\": [\n" +
            "            {\n" +
            "                \"album\": [\n" +
            "                    {\n" +
            "                        \"attachment\": {\n" +
            "                            \"downloadUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505888919591&di=426f2da564efba23387a89f463e7dec9&imgtype=0&src=http%3A%2F%2Fimg0.ph.126.net%2FoL9w-hqs35LNd8YzMSAMzA%3D%3D%2F2684708327883256010.jpg\",\n" +
            "                            \"previewUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505888937926&di=3af3bd46d68992ee4d391d7646d91ec0&imgtype=0&src=http%3A%2F%2Fuploads.gz2010.cn%2Fallimg%2F160204%2F0FA2MH-1.jpg\",\n" +
            "                            \"resId\": 11,\n" +
            "                            \"resName\": \"ha\",\n" +
            "                            \"resType\": \"txt\"\n" +
            "                        },\n" +
            "                        \"id\": 1,\n" +
            "                        \"momentId\": 22\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"claszId\": 1,\n" +
            "                \"commentedNum\": 0,\n" +
            "                \"comments\": [\n" +
            "                    {\n" +
            "                        \"claszId\": 33,\n" +
            "                        \"content\": \"222\",\n" +
            "                        \"createTime\": \"2017-09-15 10:54:12\",\n" +
            "                        \"id\": 22,\n" +
            "                        \"level\": 1,\n" +
            "                        \"momentId\": 22,\n" +
            "                        \"parentId\": 22,\n" +
            "                        \"publisher\": {\n" +
            "                            \"avatar\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505888958440&di=6786adb246dc173c27566a759b008ae4&imgtype=0&src=http%3A%2F%2Fimg21.mtime.cn%2Fmg%2F2011%2F11%2F06%2F232359.77581566.jpg\",\n" +
            "                            \"realName\": \"张磊\",\n" +
            "                            \"userId\": 22\n" +
            "                        },\n" +
            "                        \"toUser\": {\n" +
            "                            \"avatar\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505888958440&di=6786adb246dc173c27566a759b008ae4&imgtype=0&src=http%3A%2F%2Fimg21.mtime.cn%2Fmg%2F2011%2F11%2F06%2F232359.77581566.jpg\",\n" +
            "                            \"realName\": \"张磊\",\n" +
            "                            \"userId\": 22\n" +
            "                        }\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"content\": \"这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息这是一条测试消息\",\n" +
            "                \"id\": 1,\n" +
            "                \"likedNum\": 0,\n" +
            "                \"likes\": [\n" +
            "                    {\n" +
            "                        \"claszId\": 33,\n" +
            "                        \"createTime\": \"2017-09-15 10:54:12\",\n" +
            "                        \"id\": 22,\n" +
            "                        \"momentId\": 22,\n" +
            "                        \"publisher\": {\n" +
            "                            \"avatar\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505888958440&di=6786adb246dc173c27566a759b008ae4&imgtype=0&src=http%3A%2F%2Fimg21.mtime.cn%2Fmg%2F2011%2F11%2F06%2F232359.77581566.jpg\",\n" +
            "                            \"realName\": \"张磊\",\n" +
            "                            \"userId\": 22\n" +
            "                        }\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"publishTime\": \"2017-09-15 10:54:12\",\n" +
            "                \"publishTimeDesc\": \"5分钟前\",\n" +
            "                \"publisher\": {\n" +
            "                    \"avatar\": \"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2495606779,3923488157&fm=27&gp=0.jpg\",\n" +
            "                    \"realName\": \"张磊\",\n" +
            "                    \"userId\": 22\n" +
            "                },\n" +
            "                \"readedNum\": 0\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"message\": \"\"\n" +
            "}";

    public static List<ClassCircleResponse.Data.Moments> getClassCircleMockList(){
        ClassCircleResponse response=gson.fromJson(s,ClassCircleResponse.class);
        return response.data.moments;
    }

}