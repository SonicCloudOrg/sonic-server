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

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.cloud.sonic.controller.mapper.ProjectsMapper;
import org.cloud.sonic.controller.models.domain.Projects;
import org.cloud.sonic.controller.models.domain.Results;
import org.cloud.sonic.controller.models.domain.Users;
import org.cloud.sonic.controller.models.dto.ProjectsMembersDTO;
import org.cloud.sonic.controller.services.*;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhouYiXun
 * @des 项目逻辑实现
 * @date 2021/8/21 20:57
 */
@Service
public class ProjectsServiceImpl extends SonicServiceImpl<ProjectsMapper, Projects> implements ProjectsService {

    @Resource
    private ElementsService elementsService;
    @Resource
    private GlobalParamsService globalParamsService;
    @Resource
    private ModulesService modulesService;
    @Resource
    private VersionsService versionsService;
    @Resource
    private PublicStepsService publicStepsService;
    @Resource
    private ResultsService resultsService;
    @Resource
    private ResultDetailService resultDetailService;
    @Resource
    private StepsService stepsService;
    @Resource
    private TestSuitesService testSuitesService;
    @Resource
    private TestCasesService testCasesService;
    @Resource
    private ScriptsService scriptsService;
    @Resource
    private JWTTokenTool jwtTokenTool;
    @Resource
    private ProjectsMembersService projectsMembersService;
    @Resource
    private UsersService usersService;
    @Resource
    private ProjectsMapper projectsMapper;


    @Override
    public Projects findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<Projects> findAll() {
        return list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(int id) throws SonicException {
        try {
            testSuitesService.deleteByProjectId(id);
            publicStepsService.deleteByProjectId(id);
            testCasesService.deleteByProjectId(id);
            stepsService.deleteByProjectId(id);
            elementsService.deleteByProjectId(id);
            modulesService.deleteByProjectId(id);
            globalParamsService.deleteByProjectId(id);
            List<Results> resultsList = resultsService.findByProjectId(id);
            for (Results results : resultsList) {
                resultDetailService.deleteByResultId(results.getId());
            }
            resultsService.deleteByProjectId(id);
            versionsService.deleteByProjectId(id);
            scriptsService.deleteByProjectId(id);
            baseMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SonicException("project.delete.fail");
        }
    }

    /**
     * llj：新增或更新项目信息
     * @param projects 项目实体
     * @param users 用户实体
     * @throws SonicException 异常
     */
    @SneakyThrows
    @Override
    public void saveProject(Projects projects, Users users) throws SonicException {
        // 初始项目ID为空时保存项目信息，否则更新项目信息
        if (projects.getId()==null){
            // 保存项目信息
            projects.setCreateBy(users.getId());
            baseMapper.insert(projects);

            // 将项目创建人写入项目成员表
            ProjectsMembersDTO projectsMembers = new ProjectsMembersDTO();
            projectsMembers.setProjectId(projects.getId());// 保存成功后可以拿到主键ID
            projectsMembers.setUserId(users.getId());
            projectsMembers.setUserName(users.getUserName());
            projectsMembers.setMemberRole(1);// 1-项目创建人
            projectsMembersService.add(projectsMembers.convertTo());
        }else{
            // 更新项目信息
            baseMapper.updateById(projects);
        }
    }

    /**
     * llj：根据用户token查找项目集合
     * @param token token
     * @return 结果集
     */
    @Override
    public List<Projects> findProjectsByToken(String token) {
        List<Projects> listProjects = new ArrayList<>();
        String name = jwtTokenTool.getUserName(token);
        if (name != null) {
            Users users = usersService.findByUserName(name);
            int userId = users.getId();
            listProjects = projectsMapper.listByProjects(userId);
        }
        return listProjects;
    }
}
