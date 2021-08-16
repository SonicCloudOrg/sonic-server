package com.sonic.control.Models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Entity
@ApiModel("版本迭代模型")
public class Versions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @Positive
    @ApiModelProperty(value = "项目id", example = "1")
    int projectId;
    @NotNull
    @ApiModelProperty(value = "迭代名称", example = "xxx迭代")
    String versionName;
    @NotNull
    @ApiModelProperty(value = "日期", example = "2021-08-15T16:00:00.000+00:00")
    Date createTime;

    public Versions(){}

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

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Versions{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", versionName='" + versionName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
