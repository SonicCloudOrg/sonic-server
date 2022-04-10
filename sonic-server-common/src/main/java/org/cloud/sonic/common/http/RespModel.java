package org.cloud.sonic.common.http;

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
    private int code;
    @ApiModelProperty(value = "状态描述", example = "操作成功！")
    private String message;
    @ApiModelProperty(value = "响应详情")
    private T data;

    public RespModel() {
    }

    public RespModel(int code, String message) {
        this(code, message, null);
    }

    public RespModel(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RespModel(RespEnum respEnum) {
        this.code = respEnum.getCode();
        this.message = respEnum.getMessage();
    }

    public RespModel(RespEnum respEnum, T data) {
        this.code = respEnum.getCode();
        this.message = respEnum.getMessage();
        this.data = data;
    }

    public RespModel(RespEnum respEnum, String message) {
        this.code = respEnum.getCode();
        this.message = message;
        this.data = null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public RespModel<T> setMessage(String msg) {
        this.message = msg;
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
                "error=" + code +
                ", errMsg='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
