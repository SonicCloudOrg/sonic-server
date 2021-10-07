package com.sonic.controller.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Entity
@ApiModel("公共步骤模型")
@Table(indexes = {@Index(columnList = "projectId")})
public class PublicSteps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    int projectId;
    @Positive
    @ApiModelProperty(value = "平台", required = true, example = "1")
    int platform;
    @NotNull
    @ApiModelProperty(value = "公共步骤名称", required = true, example = "登陆步骤")
    String name;
    @ApiModelProperty(value = "包含操作步骤列表")
    @ManyToMany(fetch = FetchType.EAGER)
    List<Steps> steps;

    public PublicSteps() {
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

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "PublicSteps{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", platform=" + platform +
                ", name='" + name + '\'' +
                ", steps=" + steps +
                '}';
    }
}
