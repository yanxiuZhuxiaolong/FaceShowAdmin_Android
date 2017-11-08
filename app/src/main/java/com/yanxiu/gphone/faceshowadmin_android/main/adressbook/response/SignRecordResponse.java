package com.yanxiu.gphone.faceshowadmin_android.main.adressbook.response;

import com.yanxiu.gphone.faceshowadmin_android.net.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/7 17:05.
 * Function :
 */
public class SignRecordResponse extends FaceShowBaseResponse {

    public SignRecordData data;

    public class SignRecordData{

        public List<SignIns> signIns;

        public class SignIns{

            public int antiCheat;
            public int bizId;
            public String bizSource;
            public String createTime;
            public String endTime;
            public int id;
            public int openStatus;
            public String opentStatusName;
            public double percent;
            public int qrcodeRefreshRate;
            public int signInUserNum;
            public String startTime;
            public int stepId;
            public int stepFinished;
            public String successPrompt;
            public String title;
            public int totalUserNum;

        }
    }
}
