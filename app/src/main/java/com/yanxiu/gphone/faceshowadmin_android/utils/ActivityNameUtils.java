package com.yanxiu.gphone.faceshowadmin_android.utils;

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
                name="首页";
                break;
            case "LoginActivity":
                name="登录";
                break;
            case "ForgetPasswordActivity":
                name="忘记密码";
                break;
            case "ClassManageActivity":
                name="课程选择";
                break;
            case "CheckInNotesActivity":
                name="签到记录";
                break;
            case "CreateNewCheckInActivity":
                name="新建签到";
                break;
            case "MainDetailActivity":
                name="班级详情";
                break;
            case "CheckInDetailActivity":
                name="签到详情";
                break;
            case "NoticeManageActivity":
                name="通知管理";
                break;
            case "NoticeDetailActivity":
                name="通知详情";
                break;
            case "QrCodeShowActivity":
                name="签到二维码";
                break;
            case "NoticePostActivity":
                name="发布通知";
                break;
            case "ScheduleManageActivity":
                name="日程管理";
                break;
            case "PublishScheduleActivity":
                name="发布日程";
                break;
            case "UpdateScheduleActivity":
                name="发布日程";
                break;
            case "ResourceMangerActivity":
                name="资源管理";
                break;
            case "PublishResourceActivity":
                name="上传资源";
                break;
            case "WebViewActivity":
                name="";
                break;
            case "SendClassCircleActivity":
                name="班级圈";
                break;
            case "PDFViewActivity":
                name="";
                break;
            case "PhotoActivity":
                name="";
                break;
            case "AdressBookActivity":
                name="通讯录";
                break;
            case "PersonalDetailsActivity":
                name="学员详情";
                break;
            case "SignRecordActivity":
                name="个人签到记录";
                break;
            case "UserMessageActivity":
                name="我的资料";
                break;
            case "AddStudentActivity":
                name="添加成员";
                break;
            case "EditNameActivity":
                name="编辑姓名";
                break;
            case "EditSchoolActivity":
                name="编辑学校";
                break;
            case "VoteActivity":
                name="投票详情";
                break;
            case "QuestionnaireActivity":
                name="问卷详情";
                break;
            case "SubmitDetailActivity":
                name="";
                break;
            case "VoteDetailActivity":
                name="";
                break;
            case "CourseDetailActivity":
                name="";
                break;
            case "CourseMessageActivity":
                name="课程信息";
                break;
            case "CourseCommentActivity":
                name="课程讨论";
                break;
            case "ReplyDetailActivity":
                name="回复";
                break;
            default:
                name = "";
                break;
        }
        return name;
    }

}
