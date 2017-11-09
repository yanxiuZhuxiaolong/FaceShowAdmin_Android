package com.yanxiu.gphone.faceshowadmin_android.net.resource;

import com.yanxiu.gphone.faceshowadmin_android.model.BaseBean;

/**
 * 课程详情里的pdf，word等附件信息
 * Created by 戴延枫 on 2017/9/21.
 */

public class AttachmentInfosBean extends BaseBean {

    private String resId;
    private String resName;
    private String resType;//WORD:word,EXCEL:,PPT:ppt,PDF:pdf,TEXT:tx
    private String ext;
    private String downloadUrl;
    private String previewUrl;
    private String transcodeStatus;
    private String resThumb;

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getTranscodeStatus() {
        return transcodeStatus;
    }

    public void setTranscodeStatus(String transcodeStatus) {
        this.transcodeStatus = transcodeStatus;
    }

    public String getResThumb() {
        return resThumb;
    }

    public void setResThumb(String resThumb) {
        this.resThumb = resThumb;
    }
}
