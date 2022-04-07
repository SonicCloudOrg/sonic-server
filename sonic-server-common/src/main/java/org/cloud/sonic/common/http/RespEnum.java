/**
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

/**
 * @author ZhouYiXun
 * @des 接口响应枚举
 * @date 2021/8/15 18:26
 */
public enum RespEnum {
    HANDLE_OK(2000, "操作成功！"),
    SEARCH_OK(2000, "查询成功！"),
    UPDATE_OK(2000, "编辑成功！"),
    DELETE_OK(2000, "删除成功！"),
    UPLOAD_OK(2000, "上传成功！"),
    UNAUTHORIZED(1001, "身份验证异常！"),
    SERVICE_NOT_FOUND(1002, "相关服务不可用！"),
    ID_NOT_FOUND(3001, "id不存在！"),
    DELETE_ERROR(3002, "删除失败！"),
    UPLOAD_ERROR(3003, "上传失败！"),
    PARAMS_MISSING_ERROR(4001, "部分参数缺失！"),
    PARAMS_VIOLATE_ERROR(4002, "字段校验异常！"),
    UNKNOWN_ERROR(4003, "发生未知异常！"),
    PARAMS_NOT_VALID(4004, "字段缺失或校验异常！"),
    PARAMS_NOT_READABLE(4005, "解析参数失败！");

    private int code;
    private String message;

    RespEnum(int code, String message) {
        this.code = code;
        this.message = message;
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

    public void setMessage(String message) {
        this.message = message;
    }
}
