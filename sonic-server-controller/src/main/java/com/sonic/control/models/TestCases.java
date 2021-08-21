package com.sonic.control.models;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@ApiModel("测试用例模型")
@EntityListeners(AuditingEntityListener.class)
public class TestCases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @NotNull
    @ApiModelProperty(value = "用例名称", required = true, example = "测试用例")
    String name;
    @Positive
    @ApiModelProperty(value = "所属平台", required = true, example = "1")
    int platform;
    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    int projectId;
    @NotNull
    @ApiModelProperty(value = "模块名称", required = true, example = "xxx模块")
    String module;
    @NotNull
    @ApiModelProperty(value = "项目迭代名称", required = true, example = "v1.0.0需求增加")
    String version;
    @NotNull
    @ApiModelProperty(value = "用例描述", required = true, example = "xxx测试用例描述")
    String des;
    @NotNull
    @ApiModelProperty(value = "用例设计人", required = true, example = "YiXunZhou")
    String designer;
    @ApiModelProperty(value = "最后修改日期", required = true, example = "2021-08-15 11:10:00")
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date editTime;
    @ManyToMany(mappedBy = "testCases")
    @JsonIgnore
    @JSONField(serialize = false)
    Set<TestSuites> testSuites;

    public TestCases() {
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Set<TestSuites> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(Set<TestSuites> testSuites) {
        this.testSuites = testSuites;
    }

    @Override
    public String toString() {
        return "TestCases{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", platform=" + platform +
                ", projectId=" + projectId +
                ", module='" + module + '\'' +
                ", version='" + version + '\'' +
                ", des='" + des + '\'' +
                ", designer='" + designer + '\'' +
                ", editTime=" + editTime +
                '}';
    }
}
