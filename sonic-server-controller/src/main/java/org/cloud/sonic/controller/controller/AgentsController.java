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
package org.cloud.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.domain.Agents;
import org.cloud.sonic.common.models.dto.AgentsDTO;
import org.cloud.sonic.common.services.AgentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/8/28 21:49
 */
@Api(tags = "Agent端相关")
@RestController
@RequestMapping("/agents")
public class AgentsController {

    @Autowired
    private AgentsService agentsService;

    @WebAspect
    @ApiOperation(value = "查询所有Agent端", notes = "获取所有Agent端以及详细信息")
    @GetMapping("/list")
    public RespModel<List<AgentsDTO>> findAgents() {
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                agentsService.findAgents().stream().map(TypeConverter::convertTo).collect(Collectors.toList())
        );
    }

    @WebAspect
    @PutMapping
    public RespModel<String> save(@RequestBody JSONObject jsonObject) {
        agentsService.saveAgents(jsonObject);
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    @WebAspect
    @PutMapping("/updateName")
    public RespModel<String> updateName(@RequestBody JSONObject jsonObject) {
        agentsService.updateName(jsonObject.getInteger("id"), jsonObject.getString("name"));
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    @WebAspect
    @GetMapping("/offLine")
    public RespModel<String> offLine(@RequestParam(name = "id") int id) {
        agentsService.offLine(id);
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    @WebAspect
    @GetMapping("/auth")
    public RespModel<Integer> auth(@RequestParam(name = "key") String key) {
        return new RespModel<>(RespEnum.SEARCH_OK, agentsService.auth(key));
    }

    @WebAspect
    @GetMapping("/findKeyById")
    public RespModel<String> findKeyById(@RequestParam(name = "id") int id) {
        String key = agentsService.findKeyById(id);
        if (key != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, key);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "查询Agent端信息", notes = "获取对应id的Agent信息")
    @GetMapping
    public RespModel<?> findOne(@RequestParam(name = "id") int id) {
        Agents agents = agentsService.findById(id);
        if (agents != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, agents);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

}
