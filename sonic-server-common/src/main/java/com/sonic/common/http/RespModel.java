package com.sonic.common.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZhouYiXun
 * @des 接口响应模型，后续开发相关模块会共用
 * @date 2021/8/15 18:26
 */
@ApiModel("请求响应模型")
public class RespModel<T> {
    @ApiModelProperty(value = "状态码", example = "2000")
    private int error;
    @ApiModelProperty(value = "状态描述", example = "操作成功！")
    private String errMsg;
    @ApiModelProperty(value = "响应详情")
    private T data;

    public RespModel() {
    }

    public RespModel(int error, String errMsg) {
        this(error, errMsg, null);
    }

    public RespModel(int error, String errMsg, T data) {
        this.error = error;
        this.errMsg = errMsg;
        this.data = data;
    }

    public RespModel(RespEnum respEnum) {
        this.error = respEnum.getError();
        this.errMsg = respEnum.getErrMsg();
    }

    public RespModel(RespEnum respEnum, T data) {
        this.error = respEnum.getError();
        this.errMsg = respEnum.getErrMsg();
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public RespModel<T> setErrMsg(String msg) {
        this.errMsg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public RespModel<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "RespModel{" +
                "error=" + error +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
