package com.sonic.controller.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@ApiModel("测试结果模型")
@Table(indexes = {@Index(columnList = "projectId")})
@EntityListeners(AuditingEntityListener.class)
public class Results {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @ApiModelProperty(value = "测试套件id", example = "1")
    int suiteId;
    @ApiModelProperty(value = "测试套件名称", example = "测试套件A")
    String suiteName;
    @ApiModelProperty(value = "项目id", example = "1")
    int projectId;
    @ApiModelProperty(value = "触发者", example = "ZhouYiXun")
    String strike;
    @ApiModelProperty(value = "发送的消息数量", example = "1")
    int sendMsgCount;
    @ApiModelProperty(value = "接收的消息数量", example = "2")
    int receiveMsgCount;
    @ApiModelProperty(value = "状态", example = "WARN")
    int status;
    @ApiModelProperty(value = "创建时间", example = "2021-08-15 11:36:00")
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createTime;
    @ApiModelProperty(value = "结束时间", example = "2021-08-15 11:36:00")
    Date endTime;

    public Results() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(int suiteId) {
        this.suiteId = suiteId;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getStrike() {
        return strike;
    }

    public void setStrike(String strike) {
        this.strike = strike;
    }

    public int getSendMsgCount() {
        return sendMsgCount;
    }

    public void setSendMsgCount(int sendMsgCount) {
        this.sendMsgCount = sendMsgCount;
    }

    public int getReceiveMsgCount() {
        return receiveMsgCount;
    }

    public void setReceiveMsgCount(int receiveMsgCount) {
        this.receiveMsgCount = receiveMsgCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Results{" +
                "id=" + id +
                ", suiteId=" + suiteId +
                ", suiteName='" + suiteName + '\'' +
                ", projectId=" + projectId +
                ", strike='" + strike + '\'' +
                ", sendMsgCount=" + sendMsgCount +
                ", receiveMsgCount=" + receiveMsgCount +
                ", status=" + status +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                '}';
    }
}
