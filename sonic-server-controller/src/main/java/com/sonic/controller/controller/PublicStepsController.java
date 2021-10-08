package com.sonic.controller.controller;

import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.PublicSteps;
import com.sonic.controller.services.PublicStepsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "公共步骤相关")
@RestController
@RequestMapping("/publicSteps")
public class PublicStepsController {
    @Autowired
    private PublicStepsService publicStepsService;

    @WebAspect
    @ApiOperation(value = "查询公共步骤列表1", notes = "查找对应项目id下的公共步骤列表（分页）")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class)
    })
    @GetMapping("/list")
    public RespModel<Page<PublicSteps>> findByProjectId(@RequestParam(name = "projectId") int projectId,
                                                                   @RequestParam(name = "page") int page,
                                                                   @RequestParam(name = "pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        return new RespModel(RespEnum.SEARCH_OK, publicStepsService.findByProjectId(projectId, pageable));
    }

    @WebAspect
    @ApiOperation(value = "查询公共步骤列表2", notes = "查找对应项目id下的公共步骤列表（不分页，只查询id和name）")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "platform", value = "平台", dataTypeClass = Integer.class),
    })
    @GetMapping("/findNameByProjectId")
    public RespModel<List<Map<Integer, String>>> findByProjectId(@RequestParam(name = "projectId") int projectId,
                                                                 @RequestParam(name = "platform") int platform) {
        return new RespModel(RespEnum.SEARCH_OK, publicStepsService.findByProjectIdAndPlatform(projectId, platform));
    }

    @WebAspect
    @ApiOperation(value = "更新公共步骤信息", notes = "新增或更新公共步骤信息")
    @PutMapping
    public RespModel save(@Validated @RequestBody PublicSteps publicSteps) {
        publicStepsService.save(publicSteps);
        return new RespModel(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "删除公共步骤", notes = "删除对应公共步骤id，包含的操作步骤不会被删除")
    @ApiImplicitParam(name = "id", value = "公共步骤id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel delete(@RequestParam(name = "id") int id) {
        if (publicStepsService.delete(id)) {
            return new RespModel(RespEnum.DELETE_OK);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "查找公共步骤信息", notes = "查询对应公共步骤的详细信息")
    @ApiImplicitParam(name = "id", value = "公共步骤id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<PublicSteps> findById(@RequestParam(name = "id") int id) {
        PublicSteps publicSteps = publicStepsService.findById(id);
        if (publicSteps != null) {
            return new RespModel(RespEnum.SEARCH_OK, publicSteps);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }
}
