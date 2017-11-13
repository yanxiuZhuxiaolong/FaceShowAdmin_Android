package com.yanxiu.gphone.faceshowadmin_android.main.bean;

import android.text.TextUtils;

import com.yanxiu.gphone.faceshowadmin_android.model.BaseBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 课程
 * Created by 戴延枫 on 2017/9/15.
 */

public class CourseBean extends BaseBean {
    private String id;
    private String subscriberId;
    private String subscriberType;
    private String courseName;
    private String lecturer;//讲师
    private String site;
    private String startTime;
    private String endTime;
    private String briefing;
    private String attachments;
    private String courseStatus;
    private List<mLecturerInfos> lecturerInfos;

    public class mLecturerInfos{
        private String lecturerAvatar;
        private String lecturerBriefing;
        private String lecturerName;

        public String getLecturerAvatar() {
            return lecturerAvatar;
        }

        public void setLecturerAvatar(String lecturerAvatar) {
            this.lecturerAvatar = lecturerAvatar;
        }

        public String getLecturerBriefing() {
            return lecturerBriefing;
        }

        public void setLecturerBriefing(String lecturerBriefing) {
            this.lecturerBriefing = lecturerBriefing;
        }

        public String getLecturerName() {
            return lecturerName;
        }

        public void setLecturerName(String lecturerName) {
            this.lecturerName = lecturerName;
        }
    }

    public List<mLecturerInfos> getLecturerInfos() {
        return lecturerInfos;
    }

    public void setLecturerInfos(List<mLecturerInfos> lecturerInfos) {
        this.lecturerInfos = lecturerInfos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscriberType() {
        return subscriberType;
    }

    public void setSubscriberType(String subscriberType) {
        this.subscriberType = subscriberType;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLecturer() {
        if (TextUtils.isEmpty(lecturer)) {
            lecturer = "暂无";
        }
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBriefing() {
        return briefing;
    }

    public void setBriefing(String briefing) {
        this.briefing = briefing;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

}
