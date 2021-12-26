package com.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.base.CommentPage;
import com.sonic.controller.models.domain.ResultDetail;
import com.sonic.controller.services.ResultDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/8/29 16:59
 */
@Api(tags = "测试结果详情相关")
@RestController
@RequestMapping("/resultDetail")
public class ResultDetailController {

    @Autowired
    private ResultDetailService resultDetailService;

    @WebAspect
    @PostMapping
    public RespModel<String> save(@RequestBody JSONObject jsonObject) {
        resultDetailService.saveByTransport(jsonObject);
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查找测试结果详情", notes = "查找对应测试结果详情")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "caseId", value = "测试用例id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "resultId", value = "测试结果id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "deviceId", value = "设备id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "type", value = "类型", dataTypeClass = String.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class)
    })
    @GetMapping("/list")
    public RespModel<CommentPage<ResultDetail>> findAll(@RequestParam(name = "caseId") int caseId,
                                                        @RequestParam(name = "resultId") int resultId,
                                                        @RequestParam(name = "deviceId") int deviceId,
                                                        @RequestParam(name = "type") String type,
                                                        @RequestParam(name = "page") int page) {
        Page<ResultDetail> pageable = new Page<>(page, 20);
        return new RespModel<>(RespEnum.SEARCH_OK,
                CommentPage.convertFrom(
                        resultDetailService.findAll(resultId, caseId, type, deviceId, pageable))
        );
    }

    @WebAspect
    @ApiOperation(value = "查找测试结果详情2", notes = "查找对应测试结果详情")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "caseId", value = "测试用例id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "resultId", value = "测试结果id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "deviceId", value = "设备id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "type", value = "类型", dataTypeClass = String.class),
    })
    @GetMapping("/listAll")
    public RespModel<List<ResultDetail>> findAll(@RequestParam(name = "caseId") int caseId,
                                                 @RequestParam(name = "resultId") int resultId,
                                                 @RequestParam(name = "deviceId") int deviceId,
                                                 @RequestParam(name = "type") String type) {
        return new RespModel<>(RespEnum.SEARCH_OK,
                resultDetailService.findAll(resultId, caseId, type, deviceId));
    }
}
