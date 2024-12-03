package org.cloud.sonic.models.http;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

@Schema(name = "拖拽排序请求模型")
public class StepSort implements Serializable {
    @NotNull
    @Schema(description = "测试用例id", required = true, example = "1")
    private int caseId;
    @NotNull
    @Schema(description = "拖拽方向", required = true, example = "up | down")
    private String direction;
    @Positive
    @Schema(description = "移动后被影响的第一个步骤sort序号", required = true, example = "1")
    private int startId;
    @Positive
    @Schema(description = "移动后被影响的最后一个步骤sort序号", required = true, example = "9")
    private int endId;
    @Schema(description = "移动步骤发生分组更改的新parentId", required = false, example = "1")
    private Integer newParentId;
    @Schema(description = "更换分组后在新分组中新的index", required = false, example = "1")
    private Integer newIndex;
    @Schema(description = "被移动步骤的主键id", required = false, example = "1")
    private Integer stepsId;

    public Integer getStepsId() {
        return stepsId;
    }

    public void setStepsId(Integer stepsId) {
        this.stepsId = stepsId;
    }

    public Integer getNewIndex() {
        return newIndex;
    }

    public void setNewIndex(Integer newIndex) {
        this.newIndex = newIndex;
    }

    public Integer getNewParentId() {
        return newParentId;
    }

    public void setNewParentId(Integer newParentId) {
        this.newParentId = newParentId;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public int getEndId() {
        return endId;
    }

    public void setEndId(int endId) {
        this.endId = endId;
    }

    @Override
    public String toString() {
        return "StepSort{" +
                "caseId=" + caseId +
                ", direction='" + direction + '\'' +
                ", startId=" + startId +
                ", endId=" + endId +
                ", newParentId=" + newParentId +
                ", newIndex=" + newIndex +
                ", stepsId=" + stepsId +
                '}';
    }
}
