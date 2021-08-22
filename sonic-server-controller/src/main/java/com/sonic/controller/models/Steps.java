package com.sonic.controller.models;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Entity
@ApiModel("运行步骤模型")
//添加caseId字段索引
@Table(indexes = {@Index(columnList = "caseId")})
public class Steps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    int projectId;
    @ApiModelProperty(value = "测试用例id", example = "1")
    int caseId;
    @Positive
    @ApiModelProperty(value = "类型", required = true, example = "1")
    int platform;
    @NotNull
    @ApiModelProperty(value = "步骤类型", required = true, example = "click")
    String stepType;
    @NotNull
    @ApiModelProperty(value = "输入文本", required = true, example = "123")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    String content;
    @NotNull
    @ApiModelProperty(value = "其他信息", required = true, example = "456")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    String text;
    @ApiModelProperty(value = "排序号", example = "123")
    int sort;
    @ApiModelProperty(value = "包含元素列表")
    @ManyToMany(fetch = FetchType.EAGER)
    List<Elements> elements;
    @ManyToMany(mappedBy = "steps")
    @JsonIgnore
    @JSONField(serialize = false)
    List<PublicSteps> publicSteps;

    public Steps() {
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

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<Elements> getElements() {
        return elements;
    }

    public void setElements(List<Elements> elements) {
        this.elements = elements;
    }

    public List<PublicSteps> getPublicSteps() {
        return publicSteps;
    }

    public void setPublicSteps(List<PublicSteps> publicSteps) {
        this.publicSteps = publicSteps;
    }

    /**
     * @return java.lang.String
     * @author ZhouYiXun
     * @des 注意，打印不能打印公共步骤模型，会造成堆溢出
     * @date 2021/8/15 19:50
     */
    @Override
    public String toString() {
        return "Steps{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", caseId=" + caseId +
                ", platform=" + platform +
                ", stepType='" + stepType + '\'' +
                ", elements=" + elements +
                ", content='" + content + '\'' +
                ", text='" + text + '\'' +
                ", sort=" + sort +
                '}';
    }
}
