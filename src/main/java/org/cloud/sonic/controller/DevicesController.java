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
package org.cloud.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Devices;
import org.cloud.sonic.controller.models.http.DeviceDetailChange;
import org.cloud.sonic.controller.models.http.OccupyParams;
import org.cloud.sonic.controller.models.http.UpdateDeviceImg;
import org.cloud.sonic.controller.services.DevicesService;
import org.cloud.sonic.controller.transport.TransportWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "设备管理相关")
@RestController
@RequestMapping("/devices")
public class DevicesController {

    @Autowired
    private DevicesService devicesService;

    @WebAspect
    @Operation(summary = "通过REST API占用设备", description = "远程占用设备并开启相关端口")
    @PostMapping("/occupy")
    public RespModel occupy(@Validated @RequestBody OccupyParams occupyParams, HttpServletRequest request) {
        String token = request.getHeader("SonicToken");
        if (token == null) {
            return new RespModel(RespEnum.UNAUTHORIZED);
        }
        return devicesService.occupy(occupyParams, token);
    }

    @WebAspect
    @Operation(summary = "通过REST API释放设备", description = "远程释放设备并释放相关端口，只能由占用者释放")
    @Parameter(name = "udId", description = "设备序列号")
    @GetMapping("/release")
    public RespModel release(@RequestParam(name = "udId") String udId, HttpServletRequest request) {
        String token = request.getHeader("SonicToken");
        if (token == null) {
            return new RespModel(RespEnum.UNAUTHORIZED);
        }
        return devicesService.release(udId, token);
    }

    @WebAspect
    @Operation(summary = "强制解除设备占用", description = "强制解除设备占用")
    @Parameter(name = "udId", description = "设备序列号")
    @GetMapping("/stopDebug")
    public RespModel<List<Devices>> stopDebug(@RequestParam(name = "udId") String udId) {
        Devices devices = devicesService.findByUdId(udId);
        if (devices != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "stopDebug");
            jsonObject.put("udId", udId);
            TransportWorker.send(devices.getAgentId(), jsonObject);
            return new RespModel<>(RespEnum.HANDLE_OK);
        } else {
            return new RespModel<>(RespEnum.DEVICE_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "查询Agent所有设备", description = "不分页的设备列表")
    @Parameter(name = "agentId", description = "平台")
    @GetMapping("/listByAgentId")
    public RespModel<List<Devices>> listByAgentId(@RequestParam(name = "agentId") int agentId) {
        return new RespModel<>(RespEnum.SEARCH_OK,
                devicesService.listByAgentId(agentId));
    }

    @WebAspect
    @Operation(summary = "修改设备安装密码", description = "修改对应设备id的安装密码")
    @PutMapping("/saveDetail")
    public RespModel<String> saveDetail(@Validated @RequestBody DeviceDetailChange deviceDetailChange) {
        if (devicesService.saveDetail(deviceDetailChange)) {
            return new RespModel<>(RespEnum.UPDATE_OK);
        } else {
            return new RespModel<>(3000, "fail.save");
        }
    }

    @WebAspect
    @Operation(summary = "更新设备Pos", description = "更新设备Pos")
    @Parameters(value = {
            @Parameter(name = "id", description = "id"),
            @Parameter(name = "position", description = "position")
    })
    @GetMapping("/updatePosition")
    public RespModel updatePosition(@RequestParam(name = "id") int id, @RequestParam(name = "position") int position) {
        devicesService.updatePosition(id, position);
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    @WebAspect
    @Operation(summary = "修改设备图片", description = "修改对应设备id的图片")
    @PutMapping("/updateImg")
    public RespModel<String> updateImg(@Validated @RequestBody UpdateDeviceImg updateDeviceImg) {
        devicesService.updateImg(updateDeviceImg);
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @Operation(summary = "查询所有设备", description = "查找筛选条件下的所有设备，带[]的参数可以重复传")
    @Parameters(value = {
            @Parameter(name = "androidVersion[]", description = "安卓版本"),
            @Parameter(name = "iOSVersion[]", description = "iOS版本"),
            @Parameter(name = "hmVersion[]", description = "鸿蒙版本"),
            @Parameter(name = "manufacturer[]", description = "制造商"),
            @Parameter(name = "cpu[]", description = "cpu类型"),
            @Parameter(name = "size[]", description = "屏幕尺寸"),
            @Parameter(name = "agentId[]", description = "所在Agent"),
            @Parameter(name = "status[]", description = "当前状态"),
            @Parameter(name = "deviceInfo", description = "设备型号或udId"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "页数据大小")
    })
    @GetMapping("/list")
    public RespModel<CommentPage<Devices>> findAll(@RequestParam(name = "androidVersion[]", required = false) List<String> androidVersion,
                                                   @RequestParam(name = "iOSVersion[]", required = false) List<String> iOSVersion,
                                                   @RequestParam(name = "hmVersion[]", required = false) List<String> hmVersion,
                                                   @RequestParam(name = "manufacturer[]", required = false) List<String> manufacturer,
                                                   @RequestParam(name = "cpu[]", required = false) List<String> cpu,
                                                   @RequestParam(name = "size[]", required = false) List<String> size,
                                                   @RequestParam(name = "agentId[]", required = false) List<Integer> agentId,
                                                   @RequestParam(name = "status[]", required = false) List<String> status,
                                                   @RequestParam(name = "deviceInfo", required = false) String deviceInfo,
                                                   @RequestParam(name = "page") int page,
                                                   @RequestParam(name = "pageSize") int pageSize) {
        Page<Devices> pageable = new Page<>(page, pageSize);
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                CommentPage.convertFrom(
                        devicesService.findAll(iOSVersion, androidVersion, hmVersion, manufacturer, cpu, size,
                                agentId, status, deviceInfo, pageable)
                )
        );
    }

    @WebAspect
    @Operation(summary = "查询所有设备", description = "不分页的设备列表")
    @Parameter(name = "platform", description = "平台")
    @GetMapping("/listAll")
    public RespModel<List<Devices>> listAll(@RequestParam(name = "platform") int platform) {
        return new RespModel<>(RespEnum.SEARCH_OK,
                devicesService.findAll(platform));
    }

    @WebAspect
    @Operation(summary = "批量查询设备", description = "查找id列表的设备信息，可以传多个ids[]")
    @Parameter(name = "ids[]", description = "id列表")
    @GetMapping("/findByIdIn")
    public RespModel<List<Devices>> findByIdIn(@RequestParam(name = "ids[]") List<Integer> ids) {
        return new RespModel<>(RespEnum.SEARCH_OK,
                devicesService.findByIdIn(ids));
    }

    @WebAspect
    @Operation(summary = "获取查询条件", description = "获取现有筛选条件（所有设备有的条件）")
    @GetMapping("/getFilterOption")
    public RespModel<JSONObject> getFilterOption() {
        return new RespModel<>(RespEnum.SEARCH_OK, devicesService.getFilterOption());
    }

//    @WebAspect
//    @Operation(summary = "查询单个设备信息", description = "获取单个设备的详细信息")
//    @Parameter(name = "udId", value = "设备序列号")
//    @GetMapping
//    public RespModel<Devices> findByUdId(@RequestParam(name = "udId") String udId) {
//        Devices devices = devicesService.findByUdId(udId);
//        if (devices != null) {
//            return new RespModel(RespEnum.SEARCH_OK, devices);
//        } else {
//            return new RespModel(3000, "设备不存在！");
//        }
//    }

    @WebAspect
    @Operation(summary = "设备信息", description = "获取指定设备信息")
    @GetMapping
    public RespModel<Devices> findById(@RequestParam(name = "id") int id) {
        Devices devices = devicesService.findById(id);
        if (devices != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, devices);
        } else {
            return new RespModel<>(RespEnum.DEVICE_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "获取电池概况", description = "获取现有电池概况")
    @GetMapping("/findTemper")
    public RespModel<Integer> findTemper() {
        return new RespModel<>(RespEnum.SEARCH_OK, devicesService.findTemper());
    }

    @WebAspect
    @Operation(summary = "删除设备", description = "设备必须离线才能删除，会删除设备与套件绑定关系")
    @DeleteMapping()
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        return devicesService.delete(id);
    }
}
