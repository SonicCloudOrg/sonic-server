package com.sonic.controller.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@ApiModel("测试结果详情模型")
@Table(indexes = {
        @Index(name = "IDX_RESULT_ID", columnList = "resultId"),
        @Index(name = "IDX_TIME", columnList = "time"),
        @Index(name = "IDX_RESULT_ID_CASE_ID_TYPE_DEVICE_ID", columnList = "resultId"),
        @Index(name = "IDX_RESULT_ID_CASE_ID_TYPE_DEVICE_ID", columnList = "caseId"),
        @Index(name = "IDX_RESULT_ID_CASE_ID_TYPE_DEVICE_ID", columnList = "type"),
        @Index(name = "IDX_RESULT_ID_CASE_ID_TYPE_DEVICE_ID", columnList = "deviceId"),
})
public class ResultDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @ApiModelProperty(value = "测试用例id", example = "1")
    int caseId;
    @ApiModelProperty(value = "测试结果id", example = "1")
    int resultId;
    @ApiModelProperty(value = "测试结果详情类型", example = "step")
    String type;
    @ApiModelProperty(value = "测试结果详情描述", example = "点击xxx")
    String des;
    @ApiModelProperty(value = "测试结果详情状态", example = "1")
    int status;
    @ApiModelProperty(value = "设备id", example = "1")
    int deviceId;
    @ApiModelProperty(value = "测试结果详情详细日志", example = "点击xpath://*[@text()='xxx']")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    String log;
    @ApiModelProperty(value = "时间", example = "16:00:00")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    Date time;

    public ResultDetail() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ResultDetail{" +
                "id=" + id +
                ", caseId=" + caseId +
                ", resultId=" + resultId +
                ", type='" + type + '\'' +
                ", des='" + des + '\'' +
                ", status=" + status +
                ", deviceId=" + deviceId +
                ", log='" + log + '\'' +
                ", time=" + time +
                '}';
    }
}
