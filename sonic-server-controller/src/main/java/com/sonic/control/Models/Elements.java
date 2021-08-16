package com.sonic.control.Models;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Entity
@ApiModel("页面控件模型")
public class Elements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @NotNull
    @ApiModelProperty(value = "控件名称", required = true, example = "首页底部按钮")
    String eleName;
    @NotNull
    @ApiModelProperty(value = "控件类型", required = true, example = "xpath")
    String eleType;
    @NotNull
    @ApiModelProperty(value = "控件值", required = true, example = "//@[text()='home']")
    String eleValue;
    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    int projectId;
    //因为一个控件可以存在于多个步骤，也可以一个步骤有多个同样的控件，所以是多对多关系
    @ManyToMany(mappedBy = "elements")
    @JsonIgnore
    @JSONField(serialize = false)
    List<Steps> steps;

    public Elements() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEleName() {
        return eleName;
    }

    public void setEleName(String eleName) {
        this.eleName = eleName;
    }

    public String getEleType() {
        return eleType;
    }

    public void setEleType(String eleType) {
        this.eleType = eleType;
    }

    public String getEleValue() {
        return eleValue;
    }

    public void setEleValue(String eleValue) {
        this.eleValue = eleValue;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    /**
     * @return java.lang.String
     * @author ZhouYiXun
     * @des 注意，打印不能打印步骤模型，会造成堆溢出
     * @date 2021/8/15 19:50
     */
    @Override
    public String toString() {
        return "Elements{" +
                "id=" + id +
                ", eleName='" + eleName + '\'' +
                ", eleType='" + eleType + '\'' +
                ", eleValue='" + eleValue + '\'' +
                ", projectId=" + projectId +
                '}';
    }
}
