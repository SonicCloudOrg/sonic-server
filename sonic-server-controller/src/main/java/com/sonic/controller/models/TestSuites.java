package com.sonic.controller.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

@Entity
@ApiModel("测试套件模型")
public class TestSuites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @NotNull
    @ApiModelProperty(value = "测试套件名称", required = true, example = "首页测试套件")
    String name;
    @Positive
    @ApiModelProperty(value = "测试套件平台类型", required = true, example = "1")
    int platform;
    @Positive
    @ApiModelProperty(value = "覆盖类型", required = true, example = "1")
    int cover;
    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    int projectId;
    @ApiModelProperty(value = "包含的测试用例")
    @ManyToMany(fetch = FetchType.EAGER)
    List<TestCases> testCases;
    @ApiModelProperty(value = "指定设备列表")
    @ManyToMany(fetch = FetchType.EAGER)
    List<Devices> devices;

    public TestSuites() {
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

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public List<TestCases> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCases> testCases) {
        this.testCases = testCases;
    }

    public List<Devices> getDevices() {
        return devices;
    }

    public void setDevices(List<Devices> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "TestSuites{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", platform=" + platform +
                ", cover=" + cover +
                ", projectId=" + projectId +
                ", testCases=" + testCases +
                ", devices=" + devices +
                '}';
    }
}
