package com.sonic.task.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@ApiModel("定时任务实体")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @NotNull
    @ApiModelProperty(value = "定时任务名称", required = true, example = "每周三跑一次")
    String name;
    @Positive
    @ApiModelProperty(value = "测试套件id", required = true, example = "1")
    int suiteId;
    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    int projectId;
    @ApiModelProperty(value = "状态（1为开启，2为关闭）", example = "1")
    Integer status;
    @NotNull
    @ApiModelProperty(value = "cron表达式", required = true, example = "* 30 * * * ?")
    String cronExpression;

    public Tasks() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(int suiteId) {
        this.suiteId = suiteId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Override
    public String toString() {
        return "Tasks{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", suiteId=" + suiteId +
                ", projectId=" + projectId +
                ", status=" + status +
                ", cronExpression='" + cronExpression + '\'' +
                '}';
    }
}