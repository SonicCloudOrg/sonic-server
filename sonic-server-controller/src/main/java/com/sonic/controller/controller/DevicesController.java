package com.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.base.CommentPage;
import com.sonic.controller.models.domain.Devices;
import com.sonic.controller.models.http.DeviceDetailChange;
import com.sonic.controller.models.http.UpdateDeviceImg;
import com.sonic.controller.services.DevicesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PutMapping("/saveDetail")
    public RespModel<String> saveDetail(@Validated @RequestBody DeviceDetailChange deviceDetailChange) {
        if (devicesService.saveDetail(deviceDetailChange)) {
            return new RespModel<>(RespEnum.UPDATE_OK);
        } else {
            return new RespModel<>(3000, "保存异常！");
        }
    }

    @PutMapping("/updateDevicesUser")
    public RespModel<String> updateDevicesUser(@RequestBody JSONObject jsonObject) {
        devicesService.updateDevicesUser(jsonObject);
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @PutMapping("/refreshDevicesTemper")
    public RespModel<String> refreshDevicesTemper(@RequestBody JSONObject jsonObject) {
        devicesService.refreshDevicesTemper(jsonObject);
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "修改设备图片", notes = "修改对应设备id的图片")
    @PutMapping("/updateImg")
    public RespModel<String> updateImg(@Validated @RequestBody UpdateDeviceImg updateDeviceImg) {
        devicesService.updateImg(updateDeviceImg);
        return new RespModel<>(RespEnum.UPDATE_OK);
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
    public RespModel<CommentPage<Devices>> findAll(@RequestParam(name = "androidVersion[]", required = false) List<String> androidVersion,
                                                   @RequestParam(name = "iOSVersion[]", required = false) List<String> iOSVersion,
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
                        devicesService.findAll(iOSVersion, androidVersion, manufacturer, cpu, size,
                                agentId, status, deviceInfo, pageable)
                )
        );
    }

    @WebAspect
    @ApiOperation(value = "查询所有设备", notes = "不分页的设备列表")
    @ApiImplicitParam(name = "platform", value = "平台", dataTypeClass = Integer.class)
    @GetMapping("/listAll")
    public RespModel<List<Devices>> listAll(@RequestParam(name = "platform") int platform) {
        return new RespModel<>(RespEnum.SEARCH_OK,
                devicesService.findAll(platform));
    }

    @WebAspect
    @ApiOperation(value = "批量查询设备", notes = "查找id列表的设备信息，可以传多个ids[]")
    @ApiImplicitParam(name = "ids[]", value = "id列表", dataTypeClass = Integer.class)
    @GetMapping("/findByIdIn")
    public RespModel<List<Devices>> findByIdIn(@RequestParam(name = "ids[]") List<Integer> ids) {
        return new RespModel<>(RespEnum.SEARCH_OK,
                devicesService.findByIdIn(ids));
    }

    @WebAspect
    @ApiOperation(value = "获取查询条件", notes = "获取现有筛选条件（所有设备有的条件）")
    @GetMapping("/getFilterOption")
    public RespModel<JSONObject> getFilterOption() {
        return new RespModel<>(RespEnum.SEARCH_OK, devicesService.getFilterOption());
    }

//    @WebAspect
//    @ApiOperation(value = "查询单个设备信息", notes = "获取单个设备的详细信息")
//    @ApiImplicitParam(name = "udId", value = "设备序列号", dataTypeClass = String.class)
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
    @PutMapping("/deviceStatus")
    public RespModel<String> deviceStatus(@RequestBody JSONObject jsonObject) {
        devicesService.deviceStatus(jsonObject);
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @GetMapping
    public RespModel<Devices> findById(@RequestParam(name = "id") int id) {
        Devices devices = devicesService.findById(id);
        if (devices != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, devices);
        } else {
            return new RespModel<>(3000, "设备不存在！");
        }
    }

    @WebAspect
    @ApiOperation(value = "获取温度概况", notes = "获取现有温度概况")
    @GetMapping("/findTemper")
    public RespModel<Integer> findTemper() {
        return new RespModel<>(RespEnum.SEARCH_OK, devicesService.findTemper());
    }
}
