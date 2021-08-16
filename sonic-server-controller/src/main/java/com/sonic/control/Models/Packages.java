package com.sonic.control.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@ApiModel("安装包模型")
@EntityListeners(AuditingEntityListener.class)
public class Packages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @ApiModelProperty(value = "项目id", example = "1")
    int projectId;
    @ApiModelProperty(value = "平台", example = "1")
    int platform;
    @ApiModelProperty(value = "分支名称", example = "v1.0.0/release")
    String branchName;
    @ApiModelProperty(value = "Jenkins构建id（$BUILD_NUMBER in Jenkins）", example = "123")
    int buildId;
    @ApiModelProperty(value = "下载路径（如无下载路径可以传本地安装路径）", example = "http://localhost:123/abc.apk")
    String url;
    @ApiModelProperty(value = "符号表路径（iOS only）", example = "./dSYM/Test.app.dSYM")
    String dsymUrl;
    @ApiModelProperty(value = "版本号", example = "1.0.0")
    String appVersion;
    @ApiModelProperty(value = "已运行次数", example = "1")
    int runNum;
    @ApiModelProperty(value = "状态（1为开启，2为关闭）", example = "2")
    int isOpen;
    @ApiModelProperty(value = "创建日期", example = "2021-08-15 10:10:00")
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createTime;

    public Packages() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int getBuildId() {
        return buildId;
    }

    public void setBuildId(int buildId) {
        this.buildId = buildId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDsymUrl() {
        return dsymUrl;
    }

    public void setDsymUrl(String dsymUrl) {
        this.dsymUrl = dsymUrl;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getRunNum() {
        return runNum;
    }

    public void setRunNum(int runNum) {
        this.runNum = runNum;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Packages{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", platform='" + platform + '\'' +
                ", branchName='" + branchName + '\'' +
                ", buildId=" + buildId +
                ", url='" + url + '\'' +
                ", dsymUrl='" + dsymUrl + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", runNum=" + runNum +
                ", isOpen=" + isOpen +
                ", createTime=" + createTime +
                '}';
    }
}
