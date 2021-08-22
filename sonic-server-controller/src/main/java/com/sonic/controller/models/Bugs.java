package com.sonic.controller.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Entity
@ApiModel("Bugs模型")
public class Bugs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    int projectId;
    @Positive
    @ApiModelProperty(value = "类型", required = true, example = "1")
    int type;
    @NotBlank
    @ApiModelProperty(value = "平台类型", required = true, example = "1")
    int platform;
    @NotNull
    @ApiModelProperty(value = "版本号", required = true, example = "1.0.0")
    String version;
    @NotNull
    @ApiModelProperty(value = "异常类型", required = true, example = "java.lang.RunTimeException")
    String errorType;
    @NotNull
    @ApiModelProperty(value = "异常信息", required = true, example = "Fragment already added: LoadingDialogFragment")
    String errorMessage;
    @NotNull
    @ApiModelProperty(value = "异常堆栈", required = true, example = "androidx.fragment.app.addFragment(main:10)")
    String errorStack;
    @ApiModelProperty(value = "出现次数", example = "10")
    int appearTime;
    @ApiModelProperty(value = "影响设备", example = "10")
    int affectTime;
    @NotNull
    @ApiModelProperty(value = "首次出现时间", required = true, example = "2021-08-15 17:10:00")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date firstTime;
    @NotNull
    @ApiModelProperty(value = "最后出现时间", required = true, example = "2021-08-15 18:10:00")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date lastTime;
    @Positive
    @ApiModelProperty(value = "状态", required = true, example = "1")
    int status;
    @Positive
    @ApiModelProperty(value = "风险级别", required = true, example = "1")
    int level;
    @NotNull
    @ApiModelProperty(value = "描述", required = true, example = "第三方插件引起错误")
    String des;
    @NotNull
    @ApiModelProperty(value = "最后编辑", required = true, example = "YiXunZhou")
    String editor;

    public Bugs() {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorStack() {
        return errorStack;
    }

    public void setErrorStack(String errorStack) {
        this.errorStack = errorStack;
    }

    public int getAppearTime() {
        return appearTime;
    }

    public void setAppearTime(int appearTime) {
        this.appearTime = appearTime;
    }

    public int getAffectTime() {
        return affectTime;
    }

    public void setAffectTime(int affectTime) {
        this.affectTime = affectTime;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Override
    public String toString() {
        return "Bugs{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", type=" + type +
                ", platform='" + platform + '\'' +
                ", version='" + version + '\'' +
                ", errorType='" + errorType + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorStack='" + errorStack + '\'' +
                ", appearTime=" + appearTime +
                ", affectTime=" + affectTime +
                ", firstTime=" + firstTime +
                ", lastTime=" + lastTime +
                ", status=" + status +
                ", level=" + level +
                ", des='" + des + '\'' +
                ", editor='" + editor + '\'' +
                '}';
    }
}
