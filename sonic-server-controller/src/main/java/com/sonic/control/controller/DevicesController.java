package com.sonic.control.controller;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.control.models.Devices;
import com.sonic.control.models.http.DevicePwdChange;
import com.sonic.control.services.DevicesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "设备管理相关")
@RestController
@RequestMapping("/devices")
public class DevicesController {
    @Autowired
    private DevicesService devicesService;

    @WebAspect
    @ApiOperation(value = "修改设备安装密码", notes = "修改对应设备id的安装密码")
    @PutMapping("/savePwd")
    public RespModel savePwd(@Validated @RequestBody DevicePwdChange devicePwdChange) {
        if (devicesService.savePwd(devicePwdChange)) {
            return new RespModel(RespEnum.UPDATE_OK);
        } else {
            return new RespModel(3000, "保存异常！");
        }
    }

    @WebAspect
    @ApiOperation(value = "查询所有设备", notes = "查找筛选条件下的所有设备，带[]的参数可以重复传")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "androidVersion[]", value = "安卓版本", dataTypeClass = String.class),
            @ApiImplicitParam(name = "iOSVersion[]", value = "iOS版本", dataTypeClass = String.class),
            @ApiImplicitParam(name = "manufacturer[]", value = "制造商", dataTypeClass = String.class),
            @ApiImplicitParam(name = "cpu[]", value = "cpu类型", dataTypeClass = String.class),
            @ApiImplicitParam(name = "size[]", value = "屏幕尺寸", dataTypeClass = String.class),
            @ApiImplicitParam(name = "agentId[]", value = "所在Agent", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "status[]", value = "当前状态", dataTypeClass = String.class),
            @ApiImplicitParam(name = "deviceInfo", value = "设备型号或udId", dataTypeClass = String.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class)
    })
    @GetMapping("/list")
    public RespModel<Page<Devices>> findAll(@RequestParam(name = "androidVersion[]", required = false) List<String> androidVersion,
                                            @RequestParam(name = "iOSVersion[]", required = false) List<String> iOSVersion,
                                            @RequestParam(name = "manufacturer[]", required = false) List<String> manufacturer,
                                            @RequestParam(name = "cpu[]", required = false) List<String> cpu,
                                            @RequestParam(name = "size[]", required = false) List<String> size,
                                            @RequestParam(name = "agentId[]", required = false) List<Integer> agentId,
                                            @RequestParam(name = "status[]", required = false) List<String> status,
                                            @RequestParam(name = "deviceInfo", required = false) String deviceInfo,
                                            @RequestParam(name = "page") int page,
                                            @RequestParam(name = "pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return new RespModel(RespEnum.SEARCH_OK,
                devicesService.findAll(iOSVersion, androidVersion, manufacturer, cpu, size,
                        agentId, status, deviceInfo, pageable));
    }

    @WebAspect
    @ApiOperation(value = "批量查询设备", notes = "查找id列表的设备信息，可以传多个id[]")
    @ApiImplicitParam(name = "ids[]", value = "id列表", dataTypeClass = Integer.class)
    @GetMapping("/findByIdIn")
    public RespModel<List<Devices>> findByIdIn(@RequestParam(name = "ids[]") List<Integer> ids) {
        return new RespModel(RespEnum.SEARCH_OK,
                devicesService.findByIdIn(ids));
    }

    @WebAspect
    @ApiOperation(value = "获取查询条件", notes = "获取现有筛选条件")
    @GetMapping("/getFilterOption")
    public RespModel<JSONObject> getFilterOption() {
        return new RespModel(RespEnum.SEARCH_OK, devicesService.getFilterOption());
    }

    @WebAspect
    @ApiOperation(value = "查询单个设备信息", notes = "获取单个设备的详细信息")
    @ApiImplicitParam(name = "udId", value = "设备序列号", dataTypeClass = String.class)
    @GetMapping
    public RespModel<Devices> findByUdId(@RequestParam(name = "udId") String udId) {
        Devices devices = devicesService.findByUdId(udId);
        if (devices != null) {
            return new RespModel(RespEnum.SEARCH_OK, devices);
        } else {
            return new RespModel(3000, "设备不存在！");
        }
    }
}
