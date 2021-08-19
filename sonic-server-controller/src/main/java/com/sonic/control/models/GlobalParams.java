package com.sonic.control.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@ApiModel("全局参数模型")
public class GlobalParams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    int projectId;
    @NotNull
    @ApiModelProperty(value = "参数名", required = true, example = "account")
    String paramsKey;
    @NotNull
    @ApiModelProperty(value = "参数值", required = true, example = "123456789")
    String paramsValue;

    public GlobalParams() {
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

    public String getParamsKey() {
        return paramsKey;
    }

    public void setParamsKey(String paramsKey) {
        this.paramsKey = paramsKey;
    }

    public String getParamsValue() {
        return paramsValue;
    }

    public void setParamsValue(String paramsValue) {
        this.paramsValue = paramsValue;
    }

    @Override
    public String toString() {
        return "GlobalParams{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", paramsKey='" + paramsKey + '\'' +
                ", paramsValue='" + paramsValue + '\'' +
                '}';
    }
}
