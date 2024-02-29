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
package org.cloud.sonic.controller.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.controller.mapper.ProjectsMembersMapper;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.ProjectsMembers;
import org.cloud.sonic.controller.models.domain.Users;
import org.cloud.sonic.controller.models.dto.ProjectsMembersDTO;
import org.cloud.sonic.controller.services.ProjectsMembersService;
import org.cloud.sonic.controller.services.UsersService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liulijun
 * @des 项目成员逻辑实现
 * @date 2023/4/30 20:57
 */
@Service
public class ProjectsMembersServiceImpl extends SonicServiceImpl<ProjectsMembersMapper, ProjectsMembers> implements ProjectsMembersService {

    @Resource
    private ProjectsMembersMapper projectsMembersMapper;

    @Resource
    private UsersService usersService;


    /**
     * llj: 根据主键ID删除项目成员
     *
     * @param id 主键ID
     * @throws SonicException 抛异常
     */
    @Override
    public void deleteById(int id) throws SonicException {
        getBaseMapper().deleteById(id);
    }

    /**
     * llj: 根据项目ID删除项目成员
     *
     * @param projectId 项目ID
     * @throws SonicException 抛异常
     */
    @Override
    public void deleteByProjectId(int projectId) throws SonicException {
        baseMapper.delete((new LambdaQueryWrapper<ProjectsMembers>().eq(ProjectsMembers::getProjectId, projectId)));
    }

    /**
     * llj: 新增项目成员
     *
     * @param projectsMembers 实体类
     * @throws SonicException 抛异常
     */
    @Override
    public void add(ProjectsMembers projectsMembers) throws SonicException {

        // 获取用户信息
        Users users = usersService.findByUserName(projectsMembers.getUserName());

        if (users != null) {// 添加的用户名在用户表中是否存在
            // 根据项目ID+用户ID查询项目成员
            ProjectsMembers member = projectsMembersMapper.selectOne(new LambdaQueryWrapper<ProjectsMembers>()
                    .eq(ProjectsMembers::getProjectId, projectsMembers.getProjectId())
                    .eq(ProjectsMembers::getUserId, users.getId()));

            if (member == null) {//用户是否在项目成员表中
                projectsMembers.setUserId(users.getId());
                getBaseMapper().insert(projectsMembers);
            } else {
                throw new SonicException("exists.member");
            }
        } else {
            throw new SonicException("not.user");
        }
    }

    /**
     * llj: 根据项目ID分页查找项目成员
     * @param page      页数
     * @param projectId 项目ID
     * @return 结果集
     */
    @Override
    public CommentPage<ProjectsMembersDTO> findByProjectId(Page<ProjectsMembers> page, int projectId) {

        // 分页查询项目成员
        Page<ProjectsMembers> members = lambdaQuery()
                .eq(ProjectsMembers::getProjectId, projectId)
                .orderByAsc(ProjectsMembers::getId)
                .page(page);

        List<ProjectsMembersDTO> listMembers = members.getRecords().stream()
                .map(e -> {
                    ProjectsMembersDTO projectsMembers = e.convertTo();
                    projectsMembers.setProjectId(projectId);
                    return projectsMembers;
                }).collect(Collectors.toList());

        return CommentPage.convertFrom(page, listMembers);
    }
}
