package com.yanxiu.gphone.faceshowadmin_android.net.task;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by frc on 17-11-6.
 */

public class GetTasksResponse extends FaceShowBaseResponse {


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

    public static class DataBean {
        private List<TasksBean> tasks;

        public List<TasksBean> getTasks() {
            return tasks;
        }

        public void setTasks(List<TasksBean> tasks) {
            this.tasks = tasks;
        }


    }

    public static class TasksBean {

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
