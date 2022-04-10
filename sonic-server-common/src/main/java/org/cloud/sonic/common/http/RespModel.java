/*
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.common.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Locale;
import java.util.ResourceBundle;

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
    ResourceBundle resourceBundle;

    public RespModel() {
        resourceBundle = ResourceBundle.getBundle("i18n/sonic", new Locale("zh_CN"));
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
