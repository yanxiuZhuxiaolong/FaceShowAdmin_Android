package com.yanxiu.gphone.faceshowadmin_android.model;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/14 17:26.
 * Function :
 */
public class UserInfo {

    private static UserInfo instance;

    public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }


    private Info info;

    public Info getInfo() {
        return this.info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public static void update(Info userInfo) {
        instance.info = userInfo;
    }

    public class Info {

        private int id;
        private int userId;
        private String realName;
        private String mobilePhone;
        private Object email;
        private int stage;
        private int subject;
        private int userStatus;
        private Object ucnterId;
        private Object school;
        private String avatar;
        private String sexName;
        private int sex;
        private String stageName;
        private String subjectName;
        private String classId;
        private String className;
        private String token;

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getSex() {
            return sex;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public int getStage() {
            return stage;
        }

        public void setStage(int stage) {
            this.stage = stage;
        }

        public int getSubject() {
            return subject;
        }

        public void setSubject(int subject) {
            this.subject = subject;
        }

        public int getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(int userStatus) {
            this.userStatus = userStatus;
        }

        public Object getUcnterId() {
            return ucnterId;
        }

        public void setUcnterId(Object ucnterId) {
            this.ucnterId = ucnterId;
        }

        public Object getSchool() {
            return school;
        }

        public void setSchool(Object school) {
            this.school = school;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSexName() {
            return sexName;
        }

        public void setSexName(String sexName) {
            this.sexName = sexName;
        }

        //        public String getSex() {
//            return sex;
//        }
//
//        public void setSex(String sex) {
//            this.sex = sex;
//        }

        public String getStageName() {
            return stageName;
        }

        public void setStageName(String stageName) {
            this.stageName = stageName;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }
}
