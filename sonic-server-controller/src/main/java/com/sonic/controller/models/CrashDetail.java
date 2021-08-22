package com.sonic.controller.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@ApiModel("异常信息模型")
public class CrashDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @ApiModelProperty(value = "结果id", example = "2")
    int resultId;
    @ApiModelProperty(value = "测试用例id", example = "3")
    int caseId;
    @ApiModelProperty(value = "所属BugId", example = "4")
    int bugId;
    @ApiModelProperty(value = "异常日志名称", example = "1.log")
    String logName;
    @ApiModelProperty(value = "下载路径", example = "http://xxxx.log")
    String logUrl;
    @ApiModelProperty(value = "设备id", example = "5")
    int deviceId;
    @ApiModelProperty(value = "发生时间", example = "2021-08-15 10:43:00")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date appearTime;

    public CrashDetail() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getBugId() {
        return bugId;
    }

    public void setBugId(int bugId) {
        this.bugId = bugId;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public Date getAppearTime() {
        return appearTime;
    }

    public void setAppearTime(Date appearTime) {
        this.appearTime = appearTime;
    }

    @Override
    public String toString() {
        return "CrashDetail{" +
                "id=" + id +
                ", resultId=" + resultId +
                ", caseId=" + caseId +
                ", bugId=" + bugId +
                ", logName='" + logName + '\'' +
                ", logUrl='" + logUrl + '\'' +
                ", deviceId=" + deviceId +
                ", appearTime=" + appearTime +
                '}';
    }
}
