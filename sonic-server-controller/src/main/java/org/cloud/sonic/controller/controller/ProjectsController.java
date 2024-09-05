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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.config.WhiteUrl;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Projects;
import org.cloud.sonic.controller.models.domain.Users;
import org.cloud.sonic.controller.models.dto.ProjectsDTO;
import org.cloud.sonic.controller.models.dto.ProjectsMembersDTO;
import org.cloud.sonic.controller.services.ProjectsMembersService;
import org.cloud.sonic.controller.services.ProjectsService;
import org.cloud.sonic.controller.services.UsersService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/9/9 22:46
 */
@Tag(name = "项目管理相关")
@RestController
@RequestMapping("/projects")
public class ProjectsController {

    @Resource
    private ProjectsService projectsService;
    @Resource
    private ProjectsMembersService projectsMembersService;
    @Resource
    private JWTTokenTool jwtTokenTool;
    @Resource
    private UsersService usersService;

    @WebAspect
    @Operation(summary = "更新项目信息", description = "新增或更新项目信息")
    @PutMapping
    public RespModel<String> save(HttpServletRequest request, @Validated @RequestBody ProjectsDTO projects) {
        //projectsService.save(projects.convertTo());
        //return new RespModel<>(RespEnum.UPDATE_OK);
        //llj修改：增加更新人
        String token = request.getHeader("SonicToken");
        String name = jwtTokenTool.getUserName(token);
        if (name != null) {
            Users users = usersService.findByUserName(name);
            projectsService.saveProject(projects.convertTo(), users);
        }
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @WhiteUrl
    @Operation(summary = "查找所有项目", description = "查找所有项目列表")
    @GetMapping("/list")
    public RespModel<List<ProjectsDTO>> findAll() {
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                projectsService.findAll().stream().map(TypeConverter::convertTo).collect(Collectors.toList())
        );
    }

    @WebAspect
    @Operation(summary = "查询项目信息", description = "查找对应id下的详细信息")
    @Parameter(name = "id", description = "项目id")
    @GetMapping
    public RespModel<?> findById(@RequestParam(name = "id") int id) {
        Projects projects = projectsService.findById(id);
        if (projects != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, projects.convertTo());
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "删除", description = "删除对应id下的详细信息")
    @Parameter(name = "id", description = "项目id")
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) throws SonicException {
        projectsService.delete(id);
        return new RespModel<>(RespEnum.DELETE_OK);
    }


    @WebAspect
    @WhiteUrl
    @Operation(summary = "查找项目成员", description = "查找项目成员列表")
    @GetMapping("/listMembers")
    @Parameters(value = {
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "每页显示行数"),
            @Parameter(name = "projectId", description = "项目ID"),
    })
    public RespModel<CommentPage<ProjectsMembersDTO>> findMembers(@RequestParam(name = "page") int page,
                                                                  @RequestParam(name = "pageSize") int pageSize,
                                                                  @RequestParam(name = "projectId") int projectId) {
        return RespModel.result(RespEnum.SEARCH_OK, projectsMembersService.findByProjectId(new Page<>(page, pageSize),projectId));
    }

    @WebAspect
    @WhiteUrl
    @Operation(summary = "添加项目成员", description = "返回当前第一页用户")
    @PostMapping("/addMember")
    public RespModel<String> addMember(@Validated @RequestBody ProjectsMembersDTO projectsMembers) {
        projectsMembersService.add(projectsMembers.convertTo());
        return RespModel.result(RespEnum.ADD_OK);
    }

    @WebAspect
    @WhiteUrl
    @Operation(summary = "删除项目成员", description = "返回当前第一页用户")
    @Parameter(name = "id", description = "关联id")
    @DeleteMapping("/deleteMember")
    public RespModel<String> deleteMember(@RequestParam(name = "id") int id) {
        projectsMembersService.deleteById(id);
        return RespModel.result(RespEnum.DELETE_OK);
    }
}
