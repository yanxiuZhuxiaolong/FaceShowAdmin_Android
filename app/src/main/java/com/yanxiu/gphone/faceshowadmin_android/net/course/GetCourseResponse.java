package com.yanxiu.gphone.faceshowadmin_android.net.course;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by frc on 17-11-8.
 */

public class GetCourseResponse extends FaceShowBaseResponse {


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

    public static class DataBean implements Serializable {


        private CourseBean course;
        private List<InteractStepsBean> interactSteps;

        public CourseBean getCourse() {
            return course;
        }

        public void setCourse(CourseBean course) {
            this.course = course;
        }

        public List<InteractStepsBean> getInteractSteps() {
            return interactSteps;
        }

        public void setInteractSteps(List<InteractStepsBean> interactSteps) {
            this.interactSteps = interactSteps;
        }


    }

    public static class CourseBean implements Serializable {
        /**
         * id : 10
         * subscriberId : 9
         * subscriberType : clazs
         * courseName : 第三课
         * lecturer : null
         * site : 第三教室
         * startTime : 2017-09-25 08:00:00
         * endTime : 2017-09-25 16:00:00
         * briefing : 课程介绍课程介绍课程介绍课程介绍
         * attachments : 27252331,28938625,28938631
         * courseStatus : 1
         * lecturerInfos : [{"lecturerName":"jack","lecturerBriefing":"XXXXXX","lecturerAvatar":"http://s1.jsyxw.cn/yanxiu/u/68/74/Img917468_80.jpg"}]
         * attachmentInfos : null
         * steps : null
         */

        private int id;
        private int subscriberId;
        private String subscriberType;
        private String courseName;
        private Object lecturer;
        private String site;
        private String startTime;
        private String endTime;
        private String briefing;
        private String attachments;
        private int courseStatus;
        private Object attachmentInfos;
        private Object steps;
        private List<LecturerInfosBean> lecturerInfos;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSubscriberId() {
            return subscriberId;
        }

        public void setSubscriberId(int subscriberId) {
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

        public Object getLecturer() {
            return lecturer;
        }

        public void setLecturer(Object lecturer) {
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

        public int getCourseStatus() {
            return courseStatus;
        }

        public void setCourseStatus(int courseStatus) {
            this.courseStatus = courseStatus;
        }

        public Object getAttachmentInfos() {
            return attachmentInfos;
        }

        public void setAttachmentInfos(Object attachmentInfos) {
            this.attachmentInfos = attachmentInfos;
        }

        public Object getSteps() {
            return steps;
        }

        public void setSteps(Object steps) {
            this.steps = steps;
        }

        public List<LecturerInfosBean> getLecturerInfos() {
            return lecturerInfos;
        }

        public void setLecturerInfos(List<LecturerInfosBean> lecturerInfos) {
            this.lecturerInfos = lecturerInfos;
        }

        public static class LecturerInfosBean {
            /**
             * lecturerName : jack
             * lecturerBriefing : XXXXXX
             * lecturerAvatar : http://s1.jsyxw.cn/yanxiu/u/68/74/Img917468_80.jpg
             */

            private String lecturerName;
            private String lecturerBriefing;
            private String lecturerAvatar;

            public String getLecturerName() {
                return lecturerName;
            }

            public void setLecturerName(String lecturerName) {
                this.lecturerName = lecturerName;
            }

            public String getLecturerBriefing() {
                return lecturerBriefing;
            }

            public void setLecturerBriefing(String lecturerBriefing) {
                this.lecturerBriefing = lecturerBriefing;
            }

            public String getLecturerAvatar() {
                return lecturerAvatar;
            }

            public void setLecturerAvatar(String lecturerAvatar) {
                this.lecturerAvatar = lecturerAvatar;
            }
        }
    }

    public static class InteractStepsBean implements Serializable {
        /**
         * stepId : 556
         * projectId : 17
         * clazsId : 9
         * courseId : 10
         * interactName : 123
         * interactType : 6
         * interactId : 438
         * stepStatus : 1
         * createTime : 2017-09-27 10:31:12
         * stepFinished : null
         * interactTypeName : 签到
         * totalStudentNum : 5
         * finishedStudentNum : 0
         * percent : 0
         */

        private int stepId;
        private int projectId;
        private int clazsId;
        private int courseId;
        private String interactName;
        private int interactType;
        private int interactId;
        private int stepStatus;
        private String createTime;
        private Object stepFinished;
        private String interactTypeName;
        private int totalStudentNum;
        private int finishedStudentNum;
        private int percent;

        public int getStepId() {
            return stepId;
        }

        public void setStepId(int stepId) {
            this.stepId = stepId;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public int getClazsId() {
            return clazsId;
        }

        public void setClazsId(int clazsId) {
            this.clazsId = clazsId;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getInteractName() {
            return interactName;
        }

        public void setInteractName(String interactName) {
            this.interactName = interactName;
        }

        public int getInteractType() {
            return interactType;
        }

        public void setInteractType(int interactType) {
            this.interactType = interactType;
        }

        public int getInteractId() {
            return interactId;
        }

        public void setInteractId(int interactId) {
            this.interactId = interactId;
        }

        public int getStepStatus() {
            return stepStatus;
        }

        public void setStepStatus(int stepStatus) {
            this.stepStatus = stepStatus;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getStepFinished() {
            return stepFinished;
        }

        public void setStepFinished(Object stepFinished) {
            this.stepFinished = stepFinished;
        }

        public String getInteractTypeName() {
            return interactTypeName;
        }

        public void setInteractTypeName(String interactTypeName) {
            this.interactTypeName = interactTypeName;
        }

        public int getTotalStudentNum() {
            return totalStudentNum;
        }

        public void setTotalStudentNum(int totalStudentNum) {
            this.totalStudentNum = totalStudentNum;
        }

        public int getFinishedStudentNum() {
            return finishedStudentNum;
        }

        public void setFinishedStudentNum(int finishedStudentNum) {
            this.finishedStudentNum = finishedStudentNum;
        }

        public int getPercent() {
            return percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }
    }

}
