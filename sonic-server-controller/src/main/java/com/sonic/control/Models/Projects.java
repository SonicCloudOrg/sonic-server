package com.sonic.control.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@ApiModel("项目模型")
@EntityListeners(AuditingEntityListener.class)
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @NotNull
    @ApiModelProperty(value = "项目名称", required = true, example = "test")
    String projectName;
    @NotNull
    @ApiModelProperty(value = "项目描述", required = true, example = "Sonic项目描述")
    String projectDes;
    @NotNull
    @ApiModelProperty(value = "钉钉机器人token", required = true, example = "http://dingTalk.com?token=*****")
    String dingTalkToken;
    @NotNull
    @ApiModelProperty(value = "钉钉机器人加签密钥", required = true, example = "qwe***")
    String dingTalkSecret;
    @NotNull
    @ApiModelProperty(value = "项目图标", required = true, example = "http://img.jpg")
    String projectImg;
    @ApiModelProperty(value = "最后修改日期", example = "2021-08-15 11:23:00")
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date editTime;

    public Projects() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDes() {
        return projectDes;
    }

    public void setProjectDes(String projectDes) {
        this.projectDes = projectDes;
    }

    public String getDingTalkToken() {
        return dingTalkToken;
    }

    public void setDingTalkToken(String dingTalkToken) {
        this.dingTalkToken = dingTalkToken;
    }

    public String getDingTalkSecret() {
        return dingTalkSecret;
    }

    public void setDingTalkSecret(String dingTalkSecret) {
        this.dingTalkSecret = dingTalkSecret;
    }

    public String getProjectImg() {
        return projectImg;
    }

    public void setProjectImg(String projectImg) {
        this.projectImg = projectImg;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Override
    public String toString() {
        return "Projects{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", projectDes='" + projectDes + '\'' +
                ", dingTalkToken='" + dingTalkToken + '\'' +
                ", dingTalkSecret='" + dingTalkSecret + '\'' +
                ", projectImg='" + projectImg + '\'' +
                ", editTime=" + editTime +
                '}';
    }
}
