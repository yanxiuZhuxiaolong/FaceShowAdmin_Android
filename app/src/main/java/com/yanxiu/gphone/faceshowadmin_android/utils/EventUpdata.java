package com.yanxiu.gphone.faceshowadmin_android.utils;

import android.content.Context;

import com.tendcloud.tenddata.TCAgent;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/15 14:35.
 * <p>
 * Function :打点，点击事件记录
 */
public class EventUpdata {

    /**
     * 登录
     */
    public static void onForgetPassWord(Context context) {
        TCAgent.onEvent(context, "忘记密码");
    }

    /**
     * 左侧抽屉
     */
    public static void onChangeClass(Context context) {
        TCAgent.onEvent(context, "点击切换班级");
    }

    /**
     * 左侧抽屉
     */
    public static void onMainClass(Context context) {
        TCAgent.onEvent(context, "点击班级首页");
    }

    /**
     * 左侧抽屉
     */
    public static void onLogOut(Context context) {
        TCAgent.onEvent(context, "退出登录");
    }

    /**
     * 首页
     */
    public static void onShowDrawerLeft(Context context) {
        TCAgent.onEvent(context, "打开左侧抽屉");
    }

    /**
     * 首页
     */
    public static void onSeeClassDetail(Context context) {
        TCAgent.onEvent(context, "查看班级详情");
    }

    /**
     * 首页
     */
    public static void onEnterAdressBook(Context context) {
        TCAgent.onEvent(context, "进入通讯录");
    }

    /**
     * 首页
     */
    public static void onEnterNotify(Context context) {
        TCAgent.onEvent(context, "进入通知管理");
    }

    /**
     * 首页
     */
    public static void onEnterCheckKinRecord(Context context) {
        TCAgent.onEvent(context, "进入签到记录");
    }

    /**
     * 首页
     */
    public static void onEnterSchedule(Context context) {
        TCAgent.onEvent(context, "进入日程管理");
    }

    /**
     * 首页
     */
    public static void onEnterResource(Context context) {
        TCAgent.onEvent(context, "进入资源管理");
    }

    /**
     * 通知管理页面
     */
    public static void onSendNotify(Context context) {
        TCAgent.onEvent(context, "发布通知");
    }

    /**
     * 发布通知页面
     */
    public static void onSendNotifyAddPicture(Context context) {
        TCAgent.onEvent(context, "通知中添加照片");
    }

    /**
     * 通知详情页面
     */
    public static void onDeleteNotify(Context context) {
        TCAgent.onEvent(context, "删除通知");
    }

    /**
     * 签到记录页面
     */
    public static void onSendCheckKin(Context context) {
        TCAgent.onEvent(context, "发布签到");
    }

    /**
     * 通讯录页面
     */
    public static void onAddStudent(Context context) {
        TCAgent.onEvent(context, "添加学员");
    }

    /**
     * 学员详情页面
     */
    public static void onSeeStudentCheckKinRecord(Context context) {
        TCAgent.onEvent(context, "查看学员签到记录");
    }

    /**
     * 签到记录页面
     */
    public static void onStudentRetroactive(Context context) {
        TCAgent.onEvent(context, "个人签到记录补签");
    }

    /**
     * 班级圈页面
     */
    public static void onSendClassCircle(Context context) {
        TCAgent.onEvent(context, "发布班级圈");
    }

    /**
     * 班级圈页面
     */
    public static void onDeleteClassCircle(Context context) {
        TCAgent.onEvent(context, "删除班级圈");
    }

    /**
     * 日程管理页面
     */
    public static void onDeleteCalendar(Context context) {
        TCAgent.onEvent(context, "删除日程");
    }

    /**
     * 课程详情页面
     */
    public static void onSeeCourseProfile(Context context) {
        TCAgent.onEvent(context, "查看课程简介");
    }

    /**
     * 课程讨论页面
     */
    public static void onDeleteDiscuss(Context context) {
        TCAgent.onEvent(context, "删除讨论");
    }

    /**
     * 签到详情页面
     */
    public static void onSeeSignInQrCode(Context context) {
        TCAgent.onEvent(context, "查看签到二维码");
    }

    /**
     * 签到详情页面
     */
    public static void onSignInDetailRetroactive(Context context) {
        TCAgent.onEvent(context, "签到详情补签");
    }

    /**
     * 新建签到页面
     * <p>
     * 在新建签到提交时，“只在签到时间内签到有效”功能只要处于打开状态就调用该方法
     */
    public static void onCreateNewSignIn1(Context context) {
        TCAgent.onEvent(context, "限制为在签到日期有效");
    }

    /**
     * 新建签到页面
     * <p>
     * 在新建签到提交时，“使用动态刷新的二维码”功能只要处于打开状态就调用该方法
     */
    public static void onCreateNewSignIn2(Context context) {
        TCAgent.onEvent(context, "使用动态二维码");
    }

    /**
     * 资源管理页面
     */
    public static void onEnterSendRecourseClass(Context context) {
        TCAgent.onEvent(context, "发布资源");
    }

    /**
     * 上传资源页面
     */
    public static void onSendRecourse(Context context) {
        TCAgent.onEvent(context, "提交资源");
    }

    /**
     * 投票详情页面
     */
    public static void onSeeVoteDetail(Context context) {
        TCAgent.onEvent(context, "查看投票人数详情");
    }

    /**
     * 问卷详情页面
     */
    public static void onQuestionnairesDetail(Context context) {
        TCAgent.onEvent(context, "查看问卷人数详情");
    }

    /**
     * 删除签到
     */
    public static void onDeleteSignIn(Context context) {
        TCAgent.onEvent(context, "删除签到");
    }
}
