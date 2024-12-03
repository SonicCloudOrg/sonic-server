/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.common.http;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author ZhouYiXun
 * @des 接口响应模型，后续开发相关模块会共用
 * @date 2021/8/15 18:26
 */
@Schema(name = "请求响应模型")
public class RespModel<T> {
    @Schema(description = "状态码")
    private int code;
    @Schema(description = "状态描述")
    private String message;
    @Schema(description = "响应详情")
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

    public static RespModel result(RespEnum respEnum) {
        return new RespModel(respEnum);
    }

    public static <T> RespModel<T> result(RespEnum respEnum, T data) {
        return new RespModel<T>(respEnum, data);
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
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
