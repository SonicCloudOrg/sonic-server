package org.cloud.sonic.controller.models.http;

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
                '}';
    }
}
