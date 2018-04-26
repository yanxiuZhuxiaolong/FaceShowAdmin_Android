package com.test.yanxiu.common_base.utils.talkingdata;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/16 10:44.
 * Function :
 */
public class ActivityNameUtils {

    public static String getActivityName(String simpleName) {
        String name;
        switch (simpleName) {
            case "WelcomeActivity":
                name = "";
                break;
            case "MainActivity":
                name = "首页";
                break;
            case "LoginActivity":
                name = "登录";
                break;
            case "ForgetPasswordActivity":
                name = "忘记密码";
                break;
            case "CourseActivity":
                name = "课程详情";
                break;
            case "CheckInSuccessActivity":
                name = "签到成功";
                break;
            case "CheckInErrorActivity":
                name = "签到异常";
                break;
            case "CheckInDetailActivity":
                name = "签到详情";
                break;
            case "PhotoActivity":
                name = "";
                break;
            case "NotificationDetailActivity":
                name = "通知详情";
                break;
            case "CheckInNotesActivity":
                name = "签到记录";
                break;
            case "ProfileActivity":
                name = "个人详情";
                break;
            case "WebViewActivity":
                name = "";
                break;
            case "SendClassCircleActivity":
                name = "班级圈";
                break;
            case "PDFViewActivity":
                name = "";
                break;
            case "EvaluationActivity":
                name = "评论";
                break;
            case "SpecialistIntroductionActivity":
                name = "讲师介绍";
                break;
            case "CourseIntroductionActivity":
                name = "课程介绍";
                break;
            case "CourseDiscussActivity":
                name = "课程讨论";
                break;
            case "VoteActivity":
                name = "投票详情";
                break;
            case "QuestionnaireActivity":
                name = "问卷详情";
                break;
            case "ModifyUserNameActivity":
                name = "修改用户名";
                break;
            case "ModifyUserSexActivity":
                name = "修改用户性别";
                break;
            case "ModifyUserStageActivity":
                name = "修改学段";
                break;
            case "ModifyUserSubjectActivity":
                name = "修改学科";
                break;
            case "CheckInByQRActivity":
                name = "二维码扫描";
                break;
            case "ChooseClassActivity":
                name = "课程选择";
                break;
            default:
                name = "";
        }
        return name;
    }

}
