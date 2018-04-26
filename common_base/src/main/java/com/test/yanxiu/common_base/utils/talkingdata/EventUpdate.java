package com.test.yanxiu.common_base.utils.talkingdata;

import android.content.Context;

import com.tendcloud.tenddata.TCAgent;

/**
 * @author by frc.
 *         Time : 2017/11/15 14:35.
 *         <p>
 *         Function :打点，点击事件记录
 */
public class EventUpdate {

    public static void tcAgentInit(Context context,String key,String channel){
        TCAgent.LOG_ON = true;
        TCAgent.init(context, key,channel);
        TCAgent.setReportUncaughtExceptions(true);
    }
    /**
     * 界面统计  处理 jar 重复依赖
     * */
    public static void onPageStart(Context context,String eventName){
        TCAgent.onPageStart(context,eventName);
    }
    public static void onPageEnd(Context context,String eventName){
        TCAgent.onPageEnd(context,eventName);
    }

    /**
     * 首页tab 课程
     * */
    public static void onHomeCourseTab(Context context) {
        TCAgent.onEvent(context,"点击课程");
    }
    /**
     * 首页tab 日程
     * */
    public static void onHomeScheduleTab(Context context) {
        TCAgent.onEvent(context,"点击日程");
    }

    /**
     * 首页tab 任务
     * */
    public static void onHomeTaskTab(Context context) {
        TCAgent.onEvent(context,"点击任务");
    }

    /**
     * 首页tab 资源
     * */
    public static void onHomeResTab(Context context) {
        TCAgent.onEvent(context,"点击资源");
    }


    /**
     * 点击首页签到
     */
    public static void onHomeSignInButton(Context context) {
    TCAgent.onEvent(context,"点击首页签到");
    }

    /**u
     * 点击课程
     */
    public static void onCourseButton(Context context) {
        TCAgent.onEvent(context,"点击课程");
    }
    /**
     * 点击资源
     */
    public static void onResourceButton(Context context) {
        TCAgent.onEvent(context,"点击资源");
    }
    /**
     * 点击任务
     */
    public static void onTaskButton(Context context) {
        TCAgent.onEvent(context,"点击任务");
    }
    /**
     * 点击日程
     */
    public static void onScheduleButton(Context context) {
        TCAgent.onEvent(context,"点击日程");
    }

    /**
     * 查看课程详情
     */
    public static void onCourseDetailButton(Context context){
        TCAgent.onEvent(context,"查看课程详情");
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
     * 点击签到详情页中的签到按钮
     */
    public static void onSignInButtonInSignInDetailPage(Context context){
        TCAgent.onEvent(context,"点击签到详情页中的签到按钮");
    }

    /**
     * 点击修改学段学科
     */
    public static void onChooseStageSubjectButton(Context context){
        TCAgent.onEvent(context,"点击修改学段学科");
    }


    /**
     * IM 部分
     * */

    /**
     * 私聊界面 start
     * */
    public static void onPrivatePageStart(Context context){
        TCAgent.onPageStart(context,"私聊页面");
    }
    /**
     * 私聊界面 stop
     * */
    public static void onPrivatePageEnd(Context context){
        TCAgent.onPageEnd(context,"私聊页面");
    }
    /**
     * 群聊界面 start
     * */
    public static void onGroupPageStart(Context context){
        TCAgent.onPageStart(context,"群聊页面");
    }
    /**
     * 群聊界面 stop
     * */
    public static void onGroupPageEnd(Context context){
        TCAgent.onPageEnd(context,"群聊页面");
    }

    /**
     * 聊聊发送
     * */
    public static void onClickMsgSendEvent(Context context){
        TCAgent.onEvent(context,"点击聊聊发送");
    }
    /**
     * 聊聊相机
     * */
    public static void onClickMsgCameraEvent(Context context){
        TCAgent.onEvent(context,"点击聊聊相机");
    }
    /**
     * 聊聊搜索框
     * */
    public static void onClickMsgContactSearchEvent(Context context){
        TCAgent.onEvent(context,"点击聊聊搜索框");
    }
    /**
     * 聊聊切换班级
     * */
    public static void onClickMsgChangeClassEvent(Context context){
        TCAgent.onEvent(context,"点击聊聊切换班级");
    }
    /**
     * 点击通讯录头像
     * */
    public static void onClickMsgContactImageEvent(Context context){
        TCAgent.onEvent(context,"点击通讯录中头像");
    }

    /**
     * 点击群聊头像
     * */
    public static void onClickGroupAvatarEvent(Context context){
        TCAgent.onEvent(context,"点击班级群聊头像");
    }
    /**
     * 点击通讯录
     * */
    public static void onClickContactEvent(Context context){
        TCAgent.onEvent(context,"通讯录");
    }
    /**
     * 点击班级群聊
     * */
    public static void onClickGroupTopicEvent(Context context){
        TCAgent.onEvent(context,"点击班级群聊");
    }
}
