package com.sonic.controller.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
    @ApiModelProperty(value = "测试套件类型", required = true, example = "1")
    int type;
    @Positive
    @ApiModelProperty(value = "模块并发线程", required = true, example = "1")
    int moduleThread;
    @Positive
    @ApiModelProperty(value = "用例并发线程", required = true, example = "1")
    int caseThread;
    @Positive
    @ApiModelProperty(value = "设备并发线程", required = true, example = "10")
    int deviceThread;
    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    int projectId;
    @ApiModelProperty(value = "包含的测试用例")
    @ManyToMany(fetch = FetchType.EAGER)
    Set<TestCases> testCases;
    @ApiModelProperty(value = "指定设备列表")
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Devices> devices;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getModuleThread() {
        return moduleThread;
    }

    public void setModuleThread(int moduleThread) {
        this.moduleThread = moduleThread;
    }

    public int getCaseThread() {
        return caseThread;
    }

    public void setCaseThread(int caseThread) {
        this.caseThread = caseThread;
    }

    public int getDeviceThread() {
        return deviceThread;
    }

    public void setDeviceThread(int deviceThread) {
        this.deviceThread = deviceThread;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Set<TestCases> getTestCases() {
        return testCases;
    }

    public void setTestCases(Set<TestCases> testCases) {
        this.testCases = testCases;
    }

    public Set<Devices> getDevices() {
        return devices;
    }

    public void setDevices(Set<Devices> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "TestSuites{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", platform=" + platform +
                ", type=" + type +
                ", moduleThread=" + moduleThread +
                ", caseThread=" + caseThread +
                ", deviceThread=" + deviceThread +
                ", projectId=" + projectId +
                ", testCases=" + testCases +
                ", devices=" + devices +
                '}';
    }
}
