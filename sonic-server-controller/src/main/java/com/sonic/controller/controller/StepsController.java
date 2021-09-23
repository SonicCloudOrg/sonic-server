package com.sonic.controller.controller;

import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.Steps;
import com.sonic.controller.services.StepsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/9/19 11:45
 */
@Api(tags = "操作步骤相关")
@RestController
@RequestMapping("/steps")
public class StepsController {
    @Autowired
    private StepsService stepsService;

    @WebAspect
    @ApiOperation(value = "查找步骤列表", notes = "查找对应用例id下的步骤列表")
    @ApiImplicitParam(name = "caseId", value = "测试用例id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<List<Steps>> findByCaseIdOrderBySort(@RequestParam(name = "caseId") int caseId) {
        return new RespModel(RespEnum.SEARCH_OK, stepsService.findByCaseIdOrderBySort(caseId));
    }

    @WebAspect
    @ApiOperation(value = "移出测试用例", notes = "将步骤从测试用例移出")
    @ApiImplicitParam(name = "id", value = "步骤id", dataTypeClass = Integer.class)
    @GetMapping("/resetCaseId")
    public RespModel resetCaseId(@RequestParam(name = "id") int id) {
        if (stepsService.resetCaseId(id)) {
            return new RespModel(0, "移出测试用例成功！");
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }
}
